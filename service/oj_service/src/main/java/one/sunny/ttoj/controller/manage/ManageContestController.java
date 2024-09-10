package one.sunny.ttoj.controller.manage;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import one.sunny.commonutils.R;
import one.sunny.ttoj.entity.Contest;
import one.sunny.ttoj.pojo.params.oj.ContestQueryParams;
import one.sunny.ttoj.pojo.params.manage.ManageContestCreateParams;
import one.sunny.ttoj.pojo.vo.manage.ManageContestVo;
import one.sunny.ttoj.service.ContestService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * Done
 */
@Api("admin_contest_service")
@RestController
@RequestMapping("manage/contest")
@CrossOrigin
public class ManageContestController {
    @Autowired
    private ContestService contestService;

    @ApiOperation(value = "带条件分页查询比赛列表")
    @PostMapping("getContestsByCondition")
    public R getContestsByCondition(@Validated @RequestBody ContestQueryParams contestQueryParams){
        Map<String, Object> contestsByConditionMap = contestService.getContestsByCondition(contestQueryParams, true);
        Long total = (Long) contestsByConditionMap.get("total");
        List<ManageContestVo> manageContestVos = (List<ManageContestVo>) contestsByConditionMap.get("contests");
        return R.ok().data("contests", manageContestVos).data("total", total);
    }

    @ApiOperation("通过题目id修改题目是否可见")
    @PostMapping("changeContestVisibility/{contestId}/{visible}")
    public R changeContestVisibility(@PathVariable("contestId") Long contestId,
                                     @PathVariable("visible") Boolean visible){
        Boolean change = contestService.changeContestVisibility(contestId, visible);
        return change ? R.ok().message("修改成功") : R.ok().message("修改失败");
    }

    @ApiOperation("创建比赛")
    @PostMapping("save")
    public R saveContest(@Validated @RequestBody ManageContestCreateParams manageContestCreateParams){
        Boolean save = contestService.saveContest(manageContestCreateParams);
        return save ? R.ok().message("保存成功") : R.ok().message("保存失败");
    }

    @ApiOperation("通过比赛id获取具体比赛")
    @GetMapping("{id}")
    public R getContestById(@PathVariable("id") Long id){
        Contest contest = contestService.getById(id);
        ManageContestVo manageContestVo = new ManageContestVo();
        BeanUtils.copyProperties(contest, manageContestVo);
        return R.ok().data("contest", manageContestVo);
    }

    @ApiOperation("修改比赛")
    @PostMapping("update")
    public R updateContest(@Validated @RequestBody Contest contest){
        Boolean update = contestService.updateContest(contest);
        return update ? R.ok().message("更新成功") : R.ok().message("更新失败");
    }

    @ApiOperation("删除比赛")
    @PostMapping("delete")
    public R deleteContest(@RequestBody @NotNull Long contestId){
        Boolean deleted = contestService.deleteContestByContestId(contestId);
        return deleted ? R.ok().message("删除成功") : R.ok().message("删除失败");
    }
}

