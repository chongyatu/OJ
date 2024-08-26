package one.sunny.ttoj.controller.oj;

import io.swagger.annotations.ApiOperation;
import one.sunny.commonutils.R;
import one.sunny.ttoj.entity.Problem;
import one.sunny.ttoj.pojo.vo.oj.ContestProblemVo;
import one.sunny.ttoj.service.ContestProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/contest-problem")
@CrossOrigin
public class ContestProblemController {
    @Autowired
    private ContestProblemService contestProblemService;

    @ApiOperation("通过比赛id获取所有比赛题目")
    @GetMapping("{contestId}/problems")
    public R getContestProblemVosByContestId(@PathVariable("contestId") @NotNull Long contestId){
        List<ContestProblemVo> contestProblems = contestProblemService.getContestProblemVosByContestIdOrderByDisplayId(contestId, true);
        return R.ok().data("problems", contestProblems);
    }

    @ApiOperation("通过比赛id和题目displayId获取题目")
    @GetMapping("{contestId}/problems/{problemDisplayId}")
    public R getProblemByProblemDisplayId(@PathVariable("contestId") Long contestId,
                                          @PathVariable("problemDisplayId") String problemDisplayId){
        if (contestId == null || problemDisplayId == null){
            return R.error().message("比赛id或题目displayId为空");
        }
        Problem problem = contestProblemService.getProblemByContestIdAndDisplayId(contestId, problemDisplayId);
        if (problem == null){
            return R.error().message("未获取到题目");
        }
        return R.ok().data("problem", problem);
    }
}

