package one.sunny.ttoj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import one.sunny.ttoj.entity.*;
import one.sunny.ttoj.mapper.*;
import one.sunny.ttoj.pojo.bo.LoginUserBo;
import one.sunny.ttoj.pojo.params.manage.ManageContestProblemAddParams;
import one.sunny.ttoj.pojo.params.manage.ManageContestProblemDeleteParams;
import one.sunny.ttoj.pojo.params.manage.ManageContestProblemQueryParams;
import one.sunny.ttoj.pojo.params.manage.ManageContestProblemUpdateParams;
import one.sunny.ttoj.pojo.vo.manage.ManageContestProblemVo;
import one.sunny.ttoj.pojo.vo.manage.ManageSearchProblemVo;
import one.sunny.ttoj.pojo.vo.oj.ContestProblemVo;
import one.sunny.ttoj.service.ContestProblemService;
import one.sunny.ttoj.service.ContestUserProblemService;
import one.sunny.ttoj.service.ProblemService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ContestProblemServiceImpl extends ServiceImpl<ContestProblemMapper, ContestProblem> implements ContestProblemService {
    @Autowired
    private ContestProblemMapper contestProblemMapper;
    @Autowired
    private ProblemService problemService;
    @Autowired
    private ProblemMapper problemMapper;
    @Autowired
    private ContestUserProblemMapper contestUserProblemMapper;
    @Autowired
    private ContestSubmissionMapper contestSubmissionMapper;
    @Autowired
    private ContestMapper contestMapper;

    @Override
    public Problem getProblemByContestIdAndDisplayId(Long contestId, String problemDisplayId) {
        return contestProblemMapper.getProblemByContestIdAndDisplayId(contestId, problemDisplayId);
    }

    @Override
    public List<ContestProblemVo> getContestProblemVosByContestIdOrderByDisplayId(Long contestId, Boolean visible) {
        List<ContestProblem> contestProblems = contestProblemMapper.selectList(new LambdaQueryWrapper<ContestProblem>()
                .eq(ContestProblem::getContestId, contestId)
                .orderByAsc(ContestProblem::getProblemDisplayId)
                .eq(visible != null, ContestProblem::getVisible, visible)
        );
        List<ContestProblemVo> contestProblemVos = new ArrayList<>();
        contestProblems.forEach((item) -> {
            ContestProblemVo contestProblemVo = new ContestProblemVo();
            BeanUtils.copyProperties(item, contestProblemVo);
            contestProblemVos.add(contestProblemVo);
        });
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //如果没有用户信息, 不查询题目通过情况
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            LoginUserBo loginUserBo = (LoginUserBo) authentication.getPrincipal();
            Long userId = loginUserBo.getUser().getId();
            for (ContestProblemVo contestProblemVo : contestProblemVos) {
                Long problemId = contestProblemVo.getProblemId();
                ContestUserProblem contestUserProblem = contestUserProblemMapper.selectOne(new LambdaQueryWrapper<ContestUserProblem>()
                        .eq(ContestUserProblem::getUserId, userId)
                        .eq(ContestUserProblem::getProblemId, problemId)
                        .select(ContestUserProblem::getFirstAcTime)
                );
                if (contestUserProblem != null && contestUserProblem.getFirstAcTime() != null) {
                    contestProblemVo.setAlreadyPassed(true);
                }
            }
        }
        return contestProblemVos;
    }

    @Override
    public List<ContestProblem> getContestProblemsByContestIdOrderByDisplayId(Long contestId, Boolean visible) {
        return contestProblemMapper.selectList(new LambdaQueryWrapper<ContestProblem>()
                .eq(ContestProblem::getContestId, contestId)
                .eq(visible != null, ContestProblem::getVisible, visible)
                .orderByAsc(ContestProblem::getProblemDisplayId)
        );
    }

    @Override
    public Map<String, Object> getManageContestProblemsByCondition(ManageContestProblemQueryParams manageContestProblemQueryParams) {
        Long contestId = manageContestProblemQueryParams.getContestId();
        Integer currentPage = manageContestProblemQueryParams.getCurrentPage();
        Integer pageSize = manageContestProblemQueryParams.getPageSize();

        Page<ContestProblem> page = new Page<>(currentPage, pageSize);
        Page<ContestProblem> contestProblemPage = contestProblemMapper.selectPage(page, new LambdaQueryWrapper<ContestProblem>()
                .eq(ContestProblem::getContestId, contestId)
                .orderByAsc(ContestProblem::getProblemDisplayId)
        );
        List<ContestProblem> contestProblems = contestProblemPage.getRecords();
        List<ManageContestProblemVo> manageContestProblemVos = copyContestProblemListToManageContestProblemVoList(contestProblems);

        Map<String, Object> contestProblemPageMap = new HashMap<>();
        contestProblemPageMap.put("manageContestProblemVos", manageContestProblemVos);
        long total = contestProblemPage.getTotal();
        contestProblemPageMap.put("total", total);
        return contestProblemPageMap;
    }

    @Override
    public boolean deleteContestProblemByProblemId(ManageContestProblemDeleteParams manageContestProblemDeleteParams) {
        Long contestId = manageContestProblemDeleteParams.getContestId();
        Long problemId = manageContestProblemDeleteParams.getProblemId();
        contestProblemMapper.delete(new LambdaQueryWrapper<ContestProblem>()
                .eq(ContestProblem::getContestId, contestId)
                .eq(ContestProblem::getProblemId, problemId)
        );
        // 删除对应 contest submission和 contest user-problem
        contestSubmissionMapper.delete(new LambdaQueryWrapper<ContestSubmission>()
                .eq(ContestSubmission::getContestId, contestId)
                .eq(ContestSubmission::getProblemId, problemId)
        );
        contestUserProblemMapper.delete(new LambdaQueryWrapper<ContestUserProblem>()
                .eq(ContestUserProblem::getContestId, contestId)
                .eq(ContestUserProblem::getProblemId, problemId)
        );
        return true;
    }

    @Override
    public Map<String, Object> getProblemsFromArchiveByCondition(ManageContestProblemQueryParams manageContestProblemQueryParams) {
        Long contestId = manageContestProblemQueryParams.getContestId();
        Integer currentPage = manageContestProblemQueryParams.getCurrentPage();
        Integer pageSize = manageContestProblemQueryParams.getPageSize();

        List<ContestProblem> contestProblems = contestProblemMapper.selectList(new LambdaQueryWrapper<ContestProblem>()
                .eq(ContestProblem::getContestId, contestId)
                .select(ContestProblem::getProblemId)
        );
        List<Long> problemIds = new ArrayList<>();
        contestProblems.forEach((item) -> problemIds.add(item.getProblemId()));
        Page<Problem> page = new Page<>(currentPage, pageSize);
        problemMapper.selectPage(page, new LambdaQueryWrapper<Problem>()
                .notIn(problemIds.size() > 0, Problem::getId, problemIds)
                .select(Problem::getId, Problem::getDisplayId, Problem::getName, Problem::getAuthorName, Problem::getVisible)
                .orderByAsc(Problem::getDisplayId)
        );
        List<Problem> records = page.getRecords();
        long total = page.getTotal();
        List<ManageSearchProblemVo> manageSearchProblemVos = new ArrayList<>();
        records.forEach((item) -> {
            ManageSearchProblemVo manageSearchProblemVo = new ManageSearchProblemVo();
            manageSearchProblemVo.setId(item.getId());
            manageSearchProblemVo.setDisplayId(item.getDisplayId());
            manageSearchProblemVo.setName(item.getName());
            manageSearchProblemVo.setVisible(item.getVisible());
            manageSearchProblemVo.setAuthorName(item.getAuthorName());
            manageSearchProblemVos.add(manageSearchProblemVo);
        });

        Map<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("manageSearchProblemVos", manageSearchProblemVos);
        return map;
    }

    private List<ManageContestProblemVo> copyContestProblemListToManageContestProblemVoList(List<ContestProblem> contestProblems) {
        List<ManageContestProblemVo> list = new ArrayList<>();
        contestProblems.forEach((item) -> list.add(copy(item)));
        return list;
    }

    private ManageContestProblemVo copy(ContestProblem contestProblem) {
        ManageContestProblemVo manageContestProblemVo = new ManageContestProblemVo();
        BeanUtils.copyProperties(contestProblem, manageContestProblemVo);
        return manageContestProblemVo;
    }

    public void contestProblemCheck(Long contestId, Long problemId){
        Long count = problemMapper.selectCount(new LambdaQueryWrapper<Problem>()
                .eq(Problem::getId, problemId)
        );
        Assert.state(count == 1, "添加失败,题目不存在");
        count = contestMapper.selectCount(new LambdaQueryWrapper<Contest>()
                .eq(Contest::getId, contestId)
        );
        Assert.state(count == 1, "添加失败,比赛不存在");
    }

    @Override
    public Boolean updateContestProblem(ManageContestProblemUpdateParams manageContestProblemUpdateParams) {
        Long contestId = manageContestProblemUpdateParams.getContestId();
        Long problemId = manageContestProblemUpdateParams.getProblemId();
        String problemDisplayId = manageContestProblemUpdateParams.getProblemDisplayId();
        Boolean visible = manageContestProblemUpdateParams.getVisible();
        // check
        contestProblemCheck(contestId, problemId);
        // 比赛题目是否存在
        Long count = contestProblemMapper.selectCount(new LambdaQueryWrapper<ContestProblem>()
                .eq(ContestProblem::getContestId, contestId)
                .eq(ContestProblem::getProblemId, problemId));
        Assert.state(count > 0, "比赛题目不存在");
        if(problemDisplayId != null){
            count = contestProblemMapper.selectCount(new LambdaQueryWrapper<ContestProblem>()
                    .eq(ContestProblem::getContestId, contestId)
                    .eq(ContestProblem::getProblemDisplayId, problemDisplayId)
                    .ne(ContestProblem::getProblemId, problemId)
            );
            Assert.state(count == 0, "显示ID与其它比赛重复,请重新填写");
        }
        // check
        // 修改 contest problem
        if(problemDisplayId != null || visible != null){
            contestProblemMapper.update(null, new LambdaUpdateWrapper<ContestProblem>()
                    .eq(ContestProblem::getContestId, contestId)
                    .eq(ContestProblem::getProblemId, problemId)
                    .set(problemDisplayId != null, ContestProblem::getProblemDisplayId, problemDisplayId)
                    .set(visible != null, ContestProblem::getVisible, visible)
            );
        }
        // 修改比赛提交中的 problemDisplayId
        if(problemDisplayId != null){
            contestSubmissionMapper.update(null, new LambdaUpdateWrapper<ContestSubmission>()
                    .eq(ContestSubmission::getContestId, contestId)
                    .eq(ContestSubmission::getProblemId, problemId)
                    .set(ContestSubmission::getProblemDisplayId, problemDisplayId)
            );
        }
        // 修改 contest user-problem 中的 problemDisplayId
        if(problemDisplayId != null){
            contestUserProblemMapper.update(null, new LambdaUpdateWrapper<ContestUserProblem>()
                    .eq(ContestUserProblem::getContestId, contestId)
                    .eq(ContestUserProblem::getProblemId, problemId)
                    .set(ContestUserProblem::getProblemDisplayId, problemDisplayId)
            );
        }
        return true;
    }

    @Override
    public boolean addContestProblemByProblemId(ManageContestProblemAddParams manageContestProblemAddParams) {
        Long contestId = manageContestProblemAddParams.getContestId();
        String problemDisplayId = manageContestProblemAddParams.getProblemDisplayId();
        Long problemId = manageContestProblemAddParams.getProblemId();
        // check
        contestProblemCheck(contestId, problemId);
        Long count = contestProblemMapper.selectCount(new LambdaQueryWrapper<ContestProblem>()
                .eq(ContestProblem::getContestId, contestId)
                .eq(ContestProblem::getProblemDisplayId, problemDisplayId)
        );
        Assert.state(count == 0, "显示ID与其它比赛重复,请重新填写");
        count = contestProblemMapper.selectCount(new LambdaQueryWrapper<ContestProblem>()
                .eq(ContestProblem::getContestId, contestId)
                .eq(ContestProblem::getProblemId, problemId)
        );
        Assert.state(count == 0, "比赛题目中已存在该题目");
        // check
        ContestProblem contestProblem = new ContestProblem();
        BeanUtils.copyProperties(manageContestProblemAddParams, contestProblem);
        contestProblem.setSubmitNum(0);
        contestProblem.setAcNum(0);
        contestProblem.setVisible(false);
        contestProblemMapper.insert(contestProblem);
        return true;
    }
}
