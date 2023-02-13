package one.sunny.ttoj.controller.manage;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import one.sunny.commonutils.R;
import one.sunny.ttoj.pojo.params.manage.ManageContestProblemAddParams;
import one.sunny.ttoj.pojo.params.manage.ManageContestProblemUpdateParams;
import one.sunny.ttoj.pojo.params.manage.ManageContestProblemDeleteParams;
import one.sunny.ttoj.pojo.params.manage.ManageContestProblemQueryParams;
import one.sunny.ttoj.pojo.vo.manage.ManageContestProblemVo;
import one.sunny.ttoj.pojo.vo.manage.ManageSearchProblemVo;
import one.sunny.ttoj.service.ContestProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api("admin_contest_problem_service")
@RestController
@RequestMapping("manage/contestProblem")
@CrossOrigin
public class ManageContestProblemController {
    @Autowired
    private ContestProblemService contestProblemService;

    @ApiOperation("改变比赛题目displayId或者visibility")
    @PostMapping("updateContestProblem")
    public R updateContestProblem(@RequestBody ManageContestProblemUpdateParams manageContestProblemUpdateParams){
        Boolean change = contestProblemService.updateContestProblem(manageContestProblemUpdateParams);
        return change ? R.ok().message("修改成功") : R.ok().message("修改失败");
    }

    @ApiOperation("通过Condition获得比赛题目")
    @PostMapping("getContestProblemsByCondition")
    public R getContestProblemsByCondition(@RequestBody ManageContestProblemQueryParams manageContestProblemQueryParams){
        Map<String, Object> map = contestProblemService.getManageContestProblemsByCondition(manageContestProblemQueryParams);
        List<ManageContestProblemVo> contestProblemVos = (List<ManageContestProblemVo>) map.get("manageContestProblemVos");
        long total = (long) map.get("total");
        return R.ok().data("contestProblems", contestProblemVos).data("total", total);
    }

    @ApiOperation("通过Condition查询题库题目(不包含当前比赛已经有的题目)")
    @PostMapping("getProblemsFromArchiveByCondition")
    public R getProblemsFromArchiveByCondition(@RequestBody ManageContestProblemQueryParams manageContestProblemQueryParams){
        Map<String, Object> map = contestProblemService.getProblemsFromArchiveByCondition(manageContestProblemQueryParams);
        List<ManageSearchProblemVo> searchProblemVos = (List<ManageSearchProblemVo>) map.get("manageSearchProblemVos");
        long total = (long) map.get("total");
        return R.ok().data("problems", searchProblemVos).data("total", total);
    }

    @ApiOperation("通过比赛contestId给比赛增加题目(problemId)")
    @PostMapping("addContestProblem")
    public R addContestProblemById(@RequestBody ManageContestProblemAddParams manageContestProblemAddParams){
        boolean add = contestProblemService.addContestProblemByProblemId(manageContestProblemAddParams);
        return add ? R.ok().message("添加成功") : R.error().message("添加失败");
    }

    @ApiOperation("通过problemId删除contestProblem")
    @PostMapping("deleteContestProblem")
    public R deleteContestProblemByProblemId(@RequestBody ManageContestProblemDeleteParams manageContestProblemDeleteParams){
        boolean delete = contestProblemService.deleteContestProblemByProblemId(manageContestProblemDeleteParams);
        return delete ? R.ok().message("删除成功") : R.ok().message("删除失败");
    }
}
