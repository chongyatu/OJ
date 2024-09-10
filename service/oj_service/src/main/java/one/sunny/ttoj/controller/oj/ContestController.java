package one.sunny.ttoj.controller.oj;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import one.sunny.commonutils.R;
import one.sunny.ttoj.pojo.params.oj.ContestQueryParams;
import one.sunny.ttoj.pojo.params.oj.ContestRankingsParams;
import one.sunny.ttoj.pojo.vo.oj.ContestRankVo;
import one.sunny.ttoj.pojo.vo.oj.ContestVo;
import one.sunny.ttoj.pojo.vo.oj.RecentContestVo;
import one.sunny.ttoj.service.ContestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api("contest_service")
@RestController
@RequestMapping("contest")
@CrossOrigin
public class ContestController {
    @Autowired
    private ContestService contestService;

    @ApiOperation(value = "带条件分页查询比赛列表")
    @PostMapping("getContestsByCondition")
    public R getContestsByCondition(@RequestBody ContestQueryParams contestQueryParams){
        Map<String, Object> contestsByConditionMap = contestService.getContestsByCondition(contestQueryParams, false);
        Long total = (Long) contestsByConditionMap.get("total");
        List<ContestVo> contestVos = (List<ContestVo>) contestsByConditionMap.get("contests");
        return R.ok().data("contests", contestVos).data("total", total);
    }

    @ApiOperation("通过比赛id获取具体比赛")
    @GetMapping("{contestId}")
    public R getContestById(@PathVariable("contestId") Long id){
        ContestVo contestVo = contestService.getContestVoById(id);
        return R.ok().data("contest", contestVo);
    }

    //TODO: 通过id获取比赛排名
    @ApiOperation("通过id获取比赛排名")
    @PostMapping("rankings")
    public R getRankingsByCondition(@RequestBody ContestRankingsParams contestRankingsParams){
        Map<String, Object> rankingsByCondition = contestService.getRankingsByCondition(contestRankingsParams);
        ContestRankVo contestRankVo = (ContestRankVo) rankingsByCondition.get("contestRank");
        long total = (long) rankingsByCondition.get("total");
        return R.ok().data("contestRank", contestRankVo).data("total", total);
    }

    @ApiOperation("获取最近三天的比赛")
    @GetMapping("recent/{day}")
    public R getRecentContest(@PathVariable("day") Integer day){
        List<RecentContestVo> contestVos = contestService.getRecentContest(day);
        return R.ok().data("contests", contestVos);
    }
}
