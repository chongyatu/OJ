package one.sunny.ttoj.service.impl;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import one.sunny.baseservice.exception.TTOJException;
import one.sunny.commonutils.Constants;
import one.sunny.commonutils.ErrorCode;
import one.sunny.ttoj.entity.*;
import one.sunny.ttoj.mapper.ProblemMapper;
import one.sunny.ttoj.pojo.dto.TestCaseResultsDto;
import one.sunny.ttoj.pojo.params.oj.ArchiveJudgeParams;
import one.sunny.ttoj.pojo.params.oj.ContestSubmitParams;
import one.sunny.ttoj.pojo.params.oj.JudgeParams;
import one.sunny.ttoj.pojo.params.oj.TestJudgeParams;
import one.sunny.ttoj.pojo.vo.oj.JudgeRes;
import one.sunny.ttoj.pojo.vo.oj.JudgeResultVo;
import one.sunny.ttoj.service.*;
import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static one.sunny.commonutils.Constants.RootID;

@Slf4j(topic = "TTOJ-JudgeImpl")
@Service
public class JudgeServiceImpl implements JudgeService {
    @Autowired
    private ProblemService problemService;
    @Autowired
    private RestTemplate restTemplate;
    @Value("${judge.host}")
    private String judgeHost;
    @Value("${judge.port}")
    private String judgePort;
    @Value("${testCaseSaveLocation}")
    private String testCaseSaveLocation;
    @Autowired
    private SubmissionService submissionService;
    @Autowired
    private JudgeService judgeService;
    @Autowired
    private ContestService contestService;
    @Autowired
    private ContestProblemService contestProblemService;
    @Autowired
    private ContestUserProblemService contestUserProblemService;
    @Autowired
    private ContestSubmissionService contestSubmissionService;
    @Autowired
    private UserAcProblemService userAcProblemService;
    @Autowired
    ProblemMapper problemMapper;

    @Override
    public JudgeRes judge(JudgeParams judgeParams) {
        HttpHeaders headers = new HttpHeaders();
        Map<String, Object> verifyParams = new HashMap<String, Object>();
        verifyParams.put("language_config", judgeParams.getLanguage_config());
        verifyParams.put("max_cpu_time", judgeParams.getMax_cpu_time());
        verifyParams.put("max_memory", judgeParams.getMax_memory() * 1024 * 1024);
        Boolean output = judgeParams.getOutput();
        verifyParams.put("output", output);
        verifyParams.put("src", judgeParams.getSrc());
        verifyParams.put("test_case_id", judgeParams.getTest_case_id());
        headers.add("X-Judge-Server-Token", Constants.JudgeToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<Map<String, Object>>(verifyParams, headers);
        JudgeResultVo judgeResultVo = restTemplate.postForObject("http://" + judgeHost + ":" + judgePort + "/judge", entity, JudgeResultVo.class);
        // 问题记录:postForObject返回的ResultVo里面的data可能是编译错误string,也可能是list
        Assert.notNull(judgeResultVo, "判题请求错误");
        if (judgeResultVo.getErr() != null) {
            JudgeRes judgeRes = new JudgeRes();
            judgeRes.setResult(judgeResultVo.getErr());
            judgeRes.setCompileErr((String) judgeResultVo.getData());
            judgeRes.setTimeUse(0);
            judgeRes.setMemoryUse(0);
            return judgeRes;
        }
        String json = JSONObject.toJSONString(judgeResultVo.getData());
        List<TestCaseResultsDto> data = JSON.parseArray(json, TestCaseResultsDto.class);
        Integer wrongReason = 0;
        int timeUse = 0;
        int memoryUse = 0;
        for (TestCaseResultsDto result : data) {
            timeUse = Math.max(timeUse, result.getCpu_time());
            memoryUse = Math.max(memoryUse, result.getMemory());
            if (result.getResult() != 0) {
                wrongReason = result.getResult();
                break;
            }
        }
        String message;
        if (wrongReason == 0) {
            message = Constants.Accepted;
        } else if (wrongReason == -1) {
            message = Constants.WrongAnswer;
        } else if (wrongReason == 1) {
            message = Constants.TimeLimitExceeded;
        } else if (wrongReason == 2) {
            message = Constants.RealTimeLimitExceeded;
        } else if (wrongReason == 3) {
            message = Constants.MemoryLimitExceeded;
        } else if (wrongReason == 4) {
            message = Constants.RuntimeError;
        } else if (wrongReason == 5) {
            throw new TTOJException(ErrorCode.UNKNOWN_ERROR.getCode(), ErrorCode.UNKNOWN_ERROR.getMsg() + "评测机未知错误");
        } else {
            message = Constants.UnknownErr;
        }
        return new JudgeRes(output ? (wrongReason <= 0 ? "" : message) : message, null, timeUse, memoryUse / 1024 / 1024, output ? data.get(0).getOutput() : null);
    }

    @Override
    public JudgeRes problemArchiveJudge(ArchiveJudgeParams archiveJudgeParams) {
        Date submitTime = new Date();
        Long userId = archiveJudgeParams.getUserId();
        String username = archiveJudgeParams.getUsername();
        Long problemId = archiveJudgeParams.getProblemId();
        String problemName = archiveJudgeParams.getProblemName();
        String code = archiveJudgeParams.getCode();
        String language = archiveJudgeParams.getLanguage();

        Problem problem = problemService.getById(problemId);
        Assert.notNull(problem, "题目不存在");

        JudgeParams judgeParams = new JudgeParams(language,
                code,
                problem.getTimeLimit(),
                problem.getMemoryLimit(),
                problem.getTestCaseDir(),
                false);
        JudgeRes judgeRes = judge(judgeParams);
        String result = judgeRes.getResult();
        Submission submission = new Submission(null, userId, username, problemId, problemName, submitTime, code, result, language, judgeRes.getTimeUse(), judgeRes.getMemoryUse());
        // 增加题目提交数
        LambdaUpdateWrapper<Problem> problemUpdateWrapper = new LambdaUpdateWrapper<>();
        problemUpdateWrapper.eq(Problem::getId, problemId).set(Problem::getSubmitNum, problem.getSubmitNum() + 1);
        if (Constants.Accepted.equals(result)) {
            // 保存AC记录
            if (userAcProblemService.count(new LambdaQueryWrapper<UserAcProblem>()
                    .eq(UserAcProblem::getUserId, userId)
                    .eq(UserAcProblem::getProblemId, problemId)) == 0) {
                boolean save = userAcProblemService.save(new UserAcProblem(null, userId, problemId));
                Assert.isTrue(save, "保存AC记录失败");
            }
            // 增加题目通过数
            problemUpdateWrapper.set(Problem::getAcNum, problem.getAcNum() + 1);
        }
        boolean update = problemService.update(problemUpdateWrapper);
        Assert.isTrue(update, "题目更新失败");
        boolean save = submissionService.save(submission);
        Assert.isTrue(save, "提交保存失败");
        return judgeRes;
    }

    public ContestSubmission copy(ContestSubmitParams contestSubmitParams) {
        ContestSubmission contestSubmission = new ContestSubmission();
        BeanUtils.copyProperties(contestSubmitParams, contestSubmission);
        return contestSubmission;
    }

    @Override
    public JudgeRes contestSubmit(ContestSubmitParams contestSubmitParams) {
        Long userId = contestSubmitParams.getUserId();
        String username = contestSubmitParams.getUsername();
        Long contestId = contestSubmitParams.getContestId();
        Long problemId = contestSubmitParams.getProblemId();
        String problemDisplayId = contestSubmitParams.getProblemDisplayId();
        String code = contestSubmitParams.getCode();
        String language = contestSubmitParams.getLanguage();
        // 获取比赛
        Contest contest = contestService.getOne(new LambdaQueryWrapper<Contest>().
                eq(Contest::getId, contestId)
        );
        Assert.notNull(contest, "无效的提交, 比赛不存在");
        Long creatorId = contest.getCreatorId();
        // 比赛开始前只有root和比赛创建者能提交
        DateTime submitTime = new DateTime();
        DateTime startTime = new DateTime(contest.getStartTime());
        DateTime endTime = new DateTime(contest.getEndTime());
        boolean updateRank = !userId.equals(RootID) && !userId.equals(creatorId) && submitTime.isBefore(endTime);
        if (updateRank) {
            Assert.state(submitTime.isAfter(startTime), "比赛还未开始");
        }
        // 查找题目
        Problem problem = problemService.getById(problemId);
        Assert.notNull(problem, "无效的提交, 题目不存在");
        // 判题
        JudgeParams judgeParams = new JudgeParams(
                language,
                code,
                problem.getTimeLimit(),
                problem.getMemoryLimit(),
                problem.getTestCaseDir(),
                false
        );
        JudgeRes judgeRes = judgeService.judge(judgeParams);
        // 设置比赛提交信息
        ContestSubmission contestSubmission = copy(contestSubmitParams);
        contestSubmission.setProblemName(problem.getName());
        contestSubmission.setResult(judgeRes.getResult());
        contestSubmission.setTimeUse(judgeRes.getTimeUse());
        contestSubmission.setMemoryUse(judgeRes.getMemoryUse());
        contestSubmission.setSubmitTime(submitTime.toDate());
        contestSubmission.setCodeLength(code.getBytes().length);
        // 获取对应比赛题目信息
        ContestProblem contestProblem = contestProblemService.getOne(new LambdaQueryWrapper<ContestProblem>()
                .eq(ContestProblem::getContestId, contestId)
                .eq(ContestProblem::getProblemId, problemId)
                .eq(ContestProblem::getProblemDisplayId, problemDisplayId)
        );
        Assert.notNull(contestProblem, "contestProblem == null");
        // 获取用户对于题目的信息
        ContestUserProblem contestUserProblem = contestUserProblemService.getOne(new LambdaQueryWrapper<ContestUserProblem>()
                .eq(ContestUserProblem::getContestId, contestId)
                .eq(ContestUserProblem::getProblemId, problemId)
                .eq(ContestUserProblem::getUserId, userId)
        );
        boolean hadGetContestUserProblem = true;
        // 用户此前未提交这个题目
        if (contestUserProblem == null) {
            hadGetContestUserProblem = false;
            // 附上初始值
            contestUserProblem = new ContestUserProblem();
            contestUserProblem.setUserId(userId);
            contestUserProblem.setUsername(username);
            contestUserProblem.setContestId(contestId);
            contestUserProblem.setProblemId(problemId);
            contestUserProblem.setProblemDisplayId(problemDisplayId);
            contestUserProblem.setWaTimes(0);
            contestUserProblem.setFirstAcTime(null);
            contestUserProblem.setFirstBlood(false);
        }
        String compileErr = judgeRes.getCompileErr();
        if (compileErr != null) {
            contestProblem.setSubmitNum(contestProblem.getSubmitNum() + 1);
        }
        Integer firstAcTime = contestUserProblem.getFirstAcTime();
        if (Constants.Accepted.equals(judgeRes.getResult())) {
            // 比赛结束后的提交与root和比赛创建者的提交不算
            contestProblem.setAcNum(contestProblem.getAcNum() + 1);
            int distant = Minutes.minutesBetween(startTime, submitTime).getMinutes();
            // 是否本题第一次通过
            Long firstAcUserId = contestProblem.getFirstAcUserId();
            if (firstAcUserId == null) {
                contestProblem.setFirstAcUserId(userId);
                contestProblem.setFirstAcUsername(username);
                contestProblem.setProblemFirstAcTime(distant);
                contestUserProblem.setFirstBlood(true);
            }
            // 该用户第一次通过该题
            if (firstAcTime == null) {
                contestUserProblem.setFirstAcTime(distant);
            }
        } else {
            // 若不是编译错误并且此前未AC, 增加用户对本题的错误次数
            if (firstAcTime == null && compileErr == null) {
                contestUserProblem.setWaTimes(contestUserProblem.getWaTimes() + 1);
            }
        }
        contestSubmissionService.save(contestSubmission);
        if (!updateRank) {
            return judgeRes;
        }
        if (hadGetContestUserProblem) {
            contestUserProblemService.updateById(contestUserProblem);
        } else {
            contestUserProblemService.save(contestUserProblem);
        }
        // 更新比赛题目信息
        contestProblemService.updateById(contestProblem);
        // 加入比赛提交里
        return judgeRes;
    }

    @Override
    public JudgeRes testJudge(TestJudgeParams testJudgeParams) {
        Long problemId = testJudgeParams.getProblemId();
        Long userId = testJudgeParams.getUserId();
        String code = testJudgeParams.getCode();
        String testcase = testJudgeParams.getTestcase();
        String language = testJudgeParams.getLanguage();

        String testCaseDir = String.valueOf(userId) + "-" + System.currentTimeMillis();
        FileUtil.appendString(testcase, testCaseSaveLocation + testCaseDir + "/1.in", StandardCharsets.UTF_8);
        FileUtil.appendString("", testCaseSaveLocation + testCaseDir + "/1.out", StandardCharsets.UTF_8);
        String info = "{" +
                "\"test_case_number\": 1, " +
                "\"spj\": false," +
                "\"test_cases\": { " +
                "\"1\":{" +
                "\"input_name\": \"1.in\"," +
                "\"input_size\": 1," +
                "\"output_md5\": \"1\"," +
                "\"output_name\": \"1.out\"," +
                "\"output_size\": 1," +
                "\"stripped_output_md5\": \"1\"" +
                " }" +
                "}" +
                "}";
        FileUtil.appendString(info, testCaseSaveLocation + testCaseDir + "/info", StandardCharsets.UTF_8);
        Problem problem = problemService.getById(problemId);
        Assert.notNull(problem, "题目不存在");
        JudgeRes judgeRes = null;
        try {
            JudgeParams judgeParams = new JudgeParams(language,
                    code,
                    problem.getTimeLimit(),
                    problem.getMemoryLimit(),
                    testCaseDir,
                    true);
            judgeRes = judge(judgeParams);
        } catch (Exception e) {
            FileUtil.del(testCaseSaveLocation + testCaseDir);
            throw e;
        } finally {
            FileUtil.del(testCaseSaveLocation + testCaseDir);
        }
        return judgeRes;
    }
}
