package one.sunny.ttoj.controller.oj;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import one.sunny.commonutils.R;
import one.sunny.ttoj.pojo.params.oj.ProblemQueryParams;
import one.sunny.ttoj.pojo.vo.oj.ProblemVo;
import one.sunny.ttoj.service.ProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Done
 */
@Slf4j
@Api("problem_service")
@RestController
@RequestMapping("/problem")
@CrossOrigin
public class ProblemController {
    @Autowired
    private ProblemService problemService;

    @ApiOperation(value = "带条件分页查询题目列表")
    @PostMapping("getProblemsByCondition")
    public R getProblemsByCondition(@RequestBody ProblemQueryParams problemQueryParams) {
        Map<String, Object> problemsByConditionMap = problemService.getProblemsByCondition(problemQueryParams, false);
        List<ProblemVo> problemVos = (List<ProblemVo>) problemsByConditionMap.get("problems");
        Long total = (Long) problemsByConditionMap.get("total");
        return R.ok().data("problems", problemVos).data("total", total);
    }

    @ApiOperation("通过id获得单个题目")
    @GetMapping("{problemId}")
    public R getProblemById(@PathVariable("problemId") Long problemId) {
        ProblemVo problem = problemService.getProblemById(problemId, false);
        if (problem == null) {
            return R.error().message("题目不存在或不可见");
        }
        return R.ok().data("problem", problem);
    }
}
