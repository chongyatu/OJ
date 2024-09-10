package one.sunny.ttoj.controller.oj;


import io.swagger.annotations.ApiOperation;
import one.sunny.commonutils.R;
import one.sunny.ttoj.pojo.params.oj.SubmissionParams;
import one.sunny.ttoj.service.SubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Done
 */
@RestController
@RequestMapping("/submission")
@CrossOrigin
public class SubmissionController {
    @Autowired
    private SubmissionService submissionService;

    @ApiOperation("通过题目id获取比赛的所有提交,按时间晚优先")
    @PostMapping("getSubmissionByCondition")
    private R getSubmissionByCondition(@RequestBody SubmissionParams submissionParams){
        Map<String, Object> map = submissionService.getSubmissionByCondition(submissionParams);
        if (map != null){
            return R.ok().data("submissions", map.get("submissions")).data("total", map.get("total"));
        }
        return R.error().message("获取失败");
    }
}

