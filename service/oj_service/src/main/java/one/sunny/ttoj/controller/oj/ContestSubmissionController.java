package one.sunny.ttoj.controller.oj;

import io.swagger.annotations.ApiOperation;
import one.sunny.commonutils.R;
import one.sunny.ttoj.pojo.params.oj.ContestSubmissionParams;
import one.sunny.ttoj.pojo.vo.oj.ContestSubmissionVo;
import one.sunny.ttoj.service.ContestSubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/contest-submission")
@CrossOrigin
public class ContestSubmissionController {
    @Autowired
    private ContestSubmissionService contestSubmissionService;

    @ApiOperation("通过比赛id获取比赛的所有提交,按时间晚优先")
    @PostMapping
    public R getContestSubmission(@RequestBody ContestSubmissionParams contestSubmissionParams){
        Map<String, Object> contestSubmissionMap = contestSubmissionService.getContestSubmission(contestSubmissionParams);
        List<ContestSubmissionVo> contestSubmissionVos = (List<ContestSubmissionVo>) contestSubmissionMap.get("contestSubmissions");
        Long total = (Long) contestSubmissionMap.get("total");
        return R.ok().data("contestSubmissions", contestSubmissionVos).data("total", total);
    }
}

