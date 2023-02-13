package one.sunny.ttoj.controller.oj;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import one.sunny.commonutils.R;
import one.sunny.ttoj.pojo.params.oj.ArchiveJudgeParams;
import one.sunny.ttoj.pojo.params.oj.ContestSubmitParams;
import one.sunny.ttoj.pojo.params.oj.TestJudgeParams;
import one.sunny.ttoj.pojo.vo.oj.JudgeRes;
import one.sunny.ttoj.service.JudgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j(topic = "TTOJ-JudgeService")
@Api("judge_service")
@RestController
@RequestMapping("/judge")
@CrossOrigin
public class JudgeController {
    @Autowired
    private JudgeService judgeService;

    @ApiOperation("用户在题库中提交代码")
    @PostMapping("archive")
    public R problemArchiveJudge(@RequestBody ArchiveJudgeParams archiveJudgeParams){
        JudgeRes judgeRes = judgeService.problemArchiveJudge(archiveJudgeParams);
        return R.ok().data("judgeRes", judgeRes);
    }

    @ApiOperation("用户在比赛中提交代码")
    @PostMapping("contestSubmit")
    public R contestSubmit(@RequestBody ContestSubmitParams contestSubmitParams) {
        JudgeRes judgeRes = judgeService.contestSubmit(contestSubmitParams);
        return R.ok().data("judgeRes", judgeRes);
    }

    @ApiOperation("测试运行")
    @PostMapping("test")
    public R testJudge(@RequestBody TestJudgeParams testJudgeParams){
        JudgeRes judgeRes = judgeService.testJudge(testJudgeParams);
        return R.ok().data("judgeRes", judgeRes);
    }
}
