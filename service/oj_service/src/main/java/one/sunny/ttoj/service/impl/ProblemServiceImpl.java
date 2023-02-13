package one.sunny.ttoj.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.crypto.digest.MD5;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import one.sunny.baseservice.exception.TTOJException;
import one.sunny.commonutils.Constants;
import one.sunny.commonutils.ErrorCode;
import one.sunny.commonutils.MyFileUtil;
import one.sunny.ttoj.entity.ContestProblem;
import one.sunny.ttoj.entity.Problem;
import one.sunny.ttoj.entity.UserAcProblem;
import one.sunny.ttoj.mapper.ProblemMapper;
import one.sunny.ttoj.pojo.bo.LoginUserBo;
import one.sunny.ttoj.pojo.dto.TestCaseDto;
import one.sunny.ttoj.pojo.dto.TestCaseInfo;
import one.sunny.ttoj.pojo.params.manage.ManageProblemSaveParams;
import one.sunny.ttoj.pojo.params.oj.ProblemQueryParams;
import one.sunny.ttoj.pojo.vo.manage.ManageProblemVo;
import one.sunny.ttoj.pojo.vo.oj.ProblemVo;
import one.sunny.ttoj.service.ContestProblemService;
import one.sunny.ttoj.service.ContestUserProblemService;
import one.sunny.ttoj.service.ProblemService;
import one.sunny.ttoj.service.UserAcProblemService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j(topic = "ProblemService")
@Service
public class ProblemServiceImpl extends ServiceImpl<ProblemMapper, Problem> implements ProblemService {
    @Autowired
    private ProblemMapper problemMapper;
    @Value("${testCaseSaveLocation}")
    private String testCaseSaveLocation;

    @Autowired
    private ContestProblemService contestProblemService;

    @Autowired
    private UserAcProblemService userAcProblemService;

    public Problem copy(ManageProblemSaveParams manageProblemSaveParams) {
        Problem problem = new Problem();
        BeanUtils.copyProperties(manageProblemSaveParams, problem);
        return problem;
    }

    public <T> ProblemVo copy(T t) {
        ProblemVo problemVo = new ProblemVo();
        BeanUtils.copyProperties(t, problemVo);
        return problemVo;
    }

    public <T> List<ProblemVo> copyList(List<T> list) {
        List<ProblemVo> problemVos = new ArrayList<>();
        for (T t : list) {
            problemVos.add(copy(t));
        }
        return problemVos;
    }

    public <T> ManageProblemVo adminCopy(T t) {
        ManageProblemVo manageProblemVo = new ManageProblemVo();
        BeanUtils.copyProperties(t, manageProblemVo);
        return manageProblemVo;
    }

    public <T> List<ManageProblemVo> adminCopyList(List<T> list) {
        List<ManageProblemVo> manageProblemVos = new ArrayList<>();
        for (T t : list) {
            manageProblemVos.add(adminCopy(t));
        }
        return manageProblemVos;
    }

    @Override
    public Map<String, Object> getProblemsByCondition(ProblemQueryParams problemQueryParams, Boolean admin) {
        //1、取出查询条件
        Integer currentPage = problemQueryParams.getCurrentPage();
        Integer pageSize = problemQueryParams.getPageSize();
        String authorName = problemQueryParams.getAuthorName();
        String name = problemQueryParams.getName();
        String level = problemQueryParams.getLevel();
        //2、判断条件是否为空，如不为空拼写sql
        LambdaQueryWrapper<Problem> problemWrapper = new LambdaQueryWrapper<>();
        problemWrapper.like(!StringUtils.isEmpty(authorName), Problem::getAuthorName, authorName)
                .like(!StringUtils.isEmpty(name), Problem::getName, name)
                .eq(level != null, Problem::getLevel, level)
                .orderByAsc(Problem::getDisplayId)
                // 不是管理员, 只能查看 visible = true 的题目
                .eq(!admin, Problem::getVisible, true);
        // details里面可能存放了当前登录用户的详细信息，也可以通过cast后拿到
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = null;
        // 因为本方法可能没有登录的用户使用
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            LoginUserBo loginUserBo = (LoginUserBo) authentication.getPrincipal();
            List<String> permissions = loginUserBo.getPermissions();
            userId = loginUserBo.getUser().getId();
            // 不是管理员则只能管理自己的题目
            if (admin && !permissions.contains(Constants.MANAGE_ALL_PROBLEM)) {
                problemWrapper.eq(Problem::getAuthorId, userId);
            }
        }
        Page<Problem> page = new Page<>(currentPage, pageSize);
        problemMapper.selectPage(page, problemWrapper);
        List<Problem> problems = page.getRecords();
        long total = page.getTotal();
        Map<String, Object> problemsByConditionMap = new HashMap<>();
        problemsByConditionMap.put("total", total);
        if (admin) {
            List<ManageProblemVo> manageProblemVos = adminCopyList(problems);
            problemsByConditionMap.put("problems", manageProblemVos);
            return problemsByConditionMap;
        } else {
            List<ProblemVo> problemVos = copyList(problems);
            for (ProblemVo problemVo : problemVos) {
                problemVo.setAlreadyPassed(false);
            }
            //如果没有用户信息, 不查询题目通过情况
            if (userId != null) {
                for (ProblemVo problemVo : problemVos) {
                    Long problemId = problemVo.getId();
                    problemVo.setAlreadyPassed(userAcProblemService.count(new LambdaQueryWrapper<UserAcProblem>()
                            .eq(UserAcProblem::getUserId, userId)
                            .eq(UserAcProblem::getProblemId, problemId)
                    ) > 0);
                }
            }
            problemsByConditionMap.put("problems", problemVos);
            return problemsByConditionMap;
        }
    }

    @Override
    public <K> K getProblemById(Long id, Boolean admin) {
        Problem problemWithTagsBo = problemMapper.selectById(id);
        Assert.notNull(problemWithTagsBo, "题目不存在");
        if (admin) {
            ManageProblemVo manageProblemVo = adminCopy(problemWithTagsBo);
            return (K) manageProblemVo;
        } else {
            ProblemVo problemVo = copy(problemWithTagsBo);
            return (K) problemVo;
        }
    }

    @Override
    public Boolean updateProblemVisibility(Long problemId, Boolean visible) {
        LambdaUpdateWrapper<Problem> problemUpdateWrapper = new LambdaUpdateWrapper<>();
        Problem problem = new Problem();
        problem.setId(problemId);
        problem.setVisible(visible);
        problemUpdateWrapper.eq(Problem::getId, problemId).set(Problem::getVisible, visible);
        int update = problemMapper.update(problem, problemUpdateWrapper);
        if (update > 0) {
            return true;
        }
        throw new TTOJException(ErrorCode.UPDATE_ERROR.getCode(), ErrorCode.UPDATE_ERROR.getMsg());
    }

    /**
     * 使用数据库中题目主键 ID 作为测试文件目录名称
     */
    public Boolean saveTestCase(File testcase, String testCaseDir) {
        // 保存文件部分
        testCaseDir = testCaseSaveLocation + testCaseDir;
        // 先删除原有文件夹
        FileUtil.del(testCaseDir);
        File outputDir = new File(testCaseDir);
        if (!outputDir.exists()) {
            boolean made = outputDir.mkdirs();
            Assert.isTrue(made, "文件夹创建失败");
        }
        // 解压到目标路径
        File unziped = ZipUtil.unzip(testcase, outputDir);
        // 读目录
        String[] fileList = unziped.list();
        Assert.state(fileList != null && fileList.length % 2 == 0, "文件数量不为偶数");
        int testCaseNumber = fileList.length / 2;
        // 制作info文件
        TestCaseInfo testCaseInfo = new TestCaseInfo();
        Map<String, TestCaseDto> testCases = new HashMap<>();
        for (int i = 0; i < fileList.length / 2; i++) {
            TestCaseDto testCase = new TestCaseDto();
            // 读取对应下标文件
            String inputFilename = (i + 1) + ".in";
            File inputFile = new File(testCaseDir + File.separator + inputFilename);
            Assert.isTrue(inputFile.exists(), "文件读取失败");
            testCase.setInput_name(inputFilename);
            testCase.setInput_size((int) inputFile.length());
            String outputFilename = (i + 1) + ".out";
            File outputFile = new File(testCaseDir + File.separator + outputFilename);
            Assert.isTrue(outputFile.exists(), "文件读取失败");
            String content = FileUtil.readString(outputFile, StandardCharsets.UTF_8);
            testCase.setOutput_name(outputFilename);
            testCase.setOutput_size((int) outputFile.length());
            testCase.setOutput_md5(MD5.create().digestHex16(content));
            testCase.setStripped_output_md5(MyFileUtil.md5FileByStripTrailing(outputFile));
            testCases.put(String.valueOf(i + 1), testCase);
        }
        // 写入info文件
        testCaseInfo.setTest_case_number(testCaseNumber);
        testCaseInfo.setSpj(false);
        testCaseInfo.setTest_cases(testCases);
        File infoFile = FileUtil.appendString(JSONObject.toJSONString(testCaseInfo), testCaseDir + File.separator + "info", StandardCharsets.UTF_8);
        Assert.isTrue(infoFile.exists(), "info文件制作失败");
        Assert.notNull(infoFile, "info文件制作失败");
        ZipUtil.zip(testCaseDir);
        return true;
    }

    @Override
    public Boolean saveProblem(File testcase, ManageProblemSaveParams adminProblemSaveParams) {
        // 参数判断
        Assert.notNull(testcase, "测试用例文件参数为空");
        Assert.notNull(adminProblemSaveParams, "题目信息为空");
        // 存入数据库
        Problem problem = copy(adminProblemSaveParams);
        problem.setSubmitNum(0);
        problem.setAcNum(0);
        String displayId = problem.getDisplayId().trim();
        String problemName = problem.getName().trim();
        Long count = problemMapper.selectCount(new LambdaQueryWrapper<Problem>()
                .eq(Problem::getName, problemName)
                .or().eq(Problem::getDisplayId, displayId)
        );
        Assert.state(count == 0, "题目显示ID已存在或题目名字已存在");
        problem.setDisplayId(displayId);
        problemMapper.insert(problem);
        Long problemId = problem.getId();
        String testCaseDir = String.valueOf(problemId);
        problem.setTestCaseDir(testCaseDir);
        saveTestCase(testcase, testCaseDir);
        problemMapper.updateById(problem);
        return true;
    }

    @Override
    public Boolean updateProblem(File testcase, ManageProblemSaveParams problemSaveParams) throws IOException {
        // 修改数据库中题目
        Problem problem = copy(problemSaveParams);
        Long problemId = problem.getId();
        String testCaseDir = String.valueOf(problemId);
        problemMapper.updateById(problem);
        if (testcase != null) {
            boolean deleted = FileUtil.del(testCaseSaveLocation + testCaseDir);
            Assert.isTrue(deleted, "测试文件删除失败");
            return saveTestCase(testcase, testCaseDir);
        }
        return true;
    }

    @Override
    public boolean deleteProblemById(Long problemId) {
        long count = contestProblemService.count(new LambdaQueryWrapper<ContestProblem>()
                .eq(ContestProblem::getProblemId, problemId)
        );
        Assert.isTrue(count == 0, "删除失败:该题目属于比赛题目");
        int delete = problemMapper.deleteById(problemId);
        if (delete > 0) {
            FileUtil.del(testCaseSaveLocation + problemId);
            FileUtil.del(testCaseSaveLocation + problemId + ".zip");
        }
        return true;
    }
}
