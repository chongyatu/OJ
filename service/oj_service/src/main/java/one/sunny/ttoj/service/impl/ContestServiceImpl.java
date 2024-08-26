package one.sunny.ttoj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import one.sunny.commonutils.Constants;
import one.sunny.ttoj.entity.Contest;
import one.sunny.ttoj.entity.ContestProblem;
import one.sunny.ttoj.entity.ContestUserProblem;
import one.sunny.ttoj.mapper.ContestMapper;
import one.sunny.ttoj.mapper.ContestUserProblemMapper;
import one.sunny.ttoj.pojo.bo.LoginUserBo;
import one.sunny.ttoj.pojo.params.oj.ContestQueryParams;
import one.sunny.ttoj.pojo.params.manage.ManageContestCreateParams;
import one.sunny.ttoj.pojo.params.oj.ContestRankingsParams;
import one.sunny.ttoj.pojo.vo.manage.ManageContestVo;
import one.sunny.ttoj.pojo.vo.oj.*;
import one.sunny.ttoj.service.ContestProblemService;
import one.sunny.ttoj.service.ContestService;
import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.*;

@Slf4j(topic = "ContestServiceImpl")
@Service
public class ContestServiceImpl extends ServiceImpl<ContestMapper, Contest> implements ContestService {
    @Autowired
    private ContestMapper contestMapper;
    @Autowired
    private ContestProblemService contestProblemService;
    @Autowired
    private ContestUserProblemMapper contestUserProblemMapper;

    public ArrayList<Integer> getContestStatus(Date ds, Date de) {
        DateTime startTime = new DateTime(ds);
        DateTime endTime = new DateTime(de);
        int remainSecond, status = 0;
        DateTime now = DateTime.now();
        /*
             remainSecond
             0 -> 比赛中
             -1 -> 比赛结束
         */
        if (now.isBefore(startTime)) {
            status = -1;
            remainSecond = Seconds.secondsBetween(now, startTime).getSeconds();
        } else if (now.isAfter(endTime)) {
            status = 1;
            remainSecond = -1;
        } else {
            remainSecond = Seconds.secondsBetween(now, endTime).getSeconds();
        }
        return new ArrayList<>(Arrays.asList(remainSecond, status));
    }

    public ContestVo getContestVoById(Long id) {
        Contest contest = contestMapper.selectById(id);
        return copy(contest);
    }

    public <T> ContestVo copy(T contest) {
        ContestVo contestVo = new ContestVo();
        BeanUtils.copyProperties(contest, contestVo);
        ArrayList<Integer> contestStatus = getContestStatus(contestVo.getStartTime(), contestVo.getEndTime());
        contestVo.setRemainSeconds(contestStatus.get(0));
        contestVo.setStatus(contestStatus.get(1));
        return contestVo;
    }

    public <T> ManageContestVo adminCopy(T contest) {
        ManageContestVo manageContestVo = new ManageContestVo();
        BeanUtils.copyProperties(contest, manageContestVo);
        ArrayList<Integer> contestStatus = getContestStatus(manageContestVo.getStartTime(), manageContestVo.getEndTime());
        manageContestVo.setStatus(contestStatus.get(1));
        return manageContestVo;
    }

    public <T> List<ManageContestVo> adminCopyList(List<T> contests) {
        List<ManageContestVo> manageContestVos = new ArrayList<>();
        contests.forEach((item) -> manageContestVos.add(adminCopy(item)));
        return manageContestVos;
    }

    @Override
    public Map<String, Object> getRankingsByCondition(ContestRankingsParams contestRankingsParams) {
        Long contestId = contestRankingsParams.getContestId();
        // 获得指定排名用户的所有题目通过情况(比赛时间内的提交)
        Contest contest = contestMapper.selectById(contestId);
        Assert.notNull(contest, "比赛不存在");
        List<ContestUserProblem> contestUserProblems =
                contestUserProblemMapper.getRankingsByCondition(contestRankingsParams,
                        contest.getPenalty(), contest.getCreatorId());
        // 获得所有 visible = true 的 contest problem 按 displayId 升序
        List<ContestProblem> contestProblems = contestProblemService.getContestProblemsByContestIdOrderByDisplayId(contestId, true);
        List<ContestProblemVo> contestProblemVos = copyContestProblemList(contestProblems);
        //
        List<ContestUserRankVo> rankVoList = new ArrayList<>();
        Map<String, Integer> firstAcTimeMap = new HashMap<>();
        Map<String, Boolean> firstBloodMap = new HashMap<>();
        Map<String, Integer> waTimesMap = new HashMap<>();
        int acNum = 0;
        int totalTime = 0;
        //
        for (int i = 0; i < contestUserProblems.size(); i++) {
            ContestUserProblem contestUserProblem = contestUserProblems.get(i);
            Long userId = contestUserProblem.getUserId();
            String username = contestUserProblem.getUsername();
            Integer firstAcTime = contestUserProblem.getFirstAcTime();
            Boolean firstBlood = contestUserProblem.getFirstBlood();
            String problemDisplayId = contestUserProblem.getProblemDisplayId();
            Integer waTimes = contestUserProblem.getWaTimes();
            if (firstAcTime != null) {
                acNum++;
                totalTime += firstAcTime + waTimes * contest.getPenalty();
            }
            firstAcTimeMap.put(problemDisplayId, firstAcTime);
            firstBloodMap.put(problemDisplayId, firstBlood);
            waTimesMap.put(problemDisplayId, waTimes);
            if (i == contestUserProblems.size() - 1 || !userId.equals(contestUserProblems.get(i + 1).getUserId())) {
                ContestUserRankVo contestUserRankVo = new ContestUserRankVo();
                contestUserRankVo.setUsername(username);
                contestUserRankVo.setUserId(userId);
                contestUserRankVo.setAcNum(acNum);
                contestUserRankVo.setTotalTime(totalTime);
                contestUserRankVo.setIsFirstBlood(firstBloodMap);
                contestUserRankVo.setFirstAcTime(firstAcTimeMap);
                contestUserRankVo.setWaTimes(waTimesMap);
                // 重新创建 map, 不能直接清空之前的数据
                firstBloodMap = new HashMap<>();
                firstAcTimeMap = new HashMap<>();
                waTimesMap = new HashMap<>();
                acNum = 0;
                totalTime = 0;
                rankVoList.add(contestUserRankVo);
            }
        }
        // 按通过题目数和总时间排序
        rankVoList.sort((o1, o2) -> {
            Integer acNum1 = o1.getAcNum();
            Integer acNum2 = o2.getAcNum();
            return acNum1.equals(acNum2) ? o1.getTotalTime() - o2.getTotalTime() : acNum2 - acNum1;
        });
        // 封装结果返回
        Map<String, Object> contestRankByConditionMap = new HashMap<>();
        contestRankByConditionMap.put("contestRank", new ContestRankVo(rankVoList, contestProblemVos));
        Long total = contestUserProblemMapper.selectCount(new QueryWrapper<ContestUserProblem>()
                .select("DISTINCT user_id").lambda()
                .eq(ContestUserProblem::getContestId, contestId)
        );
        contestRankByConditionMap.put("total", total);
        return contestRankByConditionMap;
    }

    private List<ContestProblemVo> copyContestProblemList(List<ContestProblem> contestProblems) {
        List<ContestProblemVo> list = new ArrayList<>();
        contestProblems.forEach((item) -> list.add(copy(item)));
        return list;
    }

    public List<ContestVo> copyList(List<Contest> contests) {
        List<ContestVo> contestVos = new ArrayList<>();
        contests.forEach((item) -> contestVos.add(copy(item)));
        return contestVos;
    }

    private ContestProblemVo copy(ContestProblem contestProblem) {
        ContestProblemVo contestProblemVo = new ContestProblemVo();
        BeanUtils.copyProperties(contestProblem, contestProblemVo);
        return contestProblemVo;
    }

    @Override
    public Map<String, Object> getContestsByCondition(ContestQueryParams contestQueryParams, Boolean admin) {
        // 不是 admin只能看 visible = true 的
        if (!admin) {
            contestQueryParams.setVisible(true);
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 如果没有管理所有比赛权限并且是获取管理数据, 就返回是比赛出题人的比赛
        Long userId = null;
        if (!(authentication instanceof AnonymousAuthenticationToken) && admin) {
            LoginUserBo loginUserBo = (LoginUserBo) authentication.getPrincipal();
            List<String> permissions = loginUserBo.getPermissions();
            if (!permissions.contains(Constants.MANAGE_ALL_CONTEST)) {
                userId = loginUserBo.getUser().getId();
            }
        }

        String creatorName = contestQueryParams.getCreatorName();
        String name = contestQueryParams.getName();
        Boolean visible = contestQueryParams.getVisible();
        Integer currentPage = contestQueryParams.getCurrentPage();
        Integer pageSize = contestQueryParams.getPageSize();
        Page<Contest> page = new Page<>(currentPage, pageSize);

        contestMapper.selectPage(page, new LambdaQueryWrapper<Contest>()
                .like(!StringUtils.isEmpty(name), Contest::getName, name)
                .like(!StringUtils.isEmpty(creatorName), Contest::getCreatorName, creatorName)
                .eq(visible != null, Contest::getVisible, visible)
                .eq(userId != null, Contest::getCreatorId, userId)
                .orderByDesc(Contest::getStartTime)
        );

        Map<String, Object> contestsByConditionMap = new HashMap<>();
        contestsByConditionMap.put("total", page.getTotal());

        List<Contest> records = page.getRecords();
        if (admin) {
            List<ManageContestVo> manageContestVos = adminCopyList(records);
            contestsByConditionMap.put("contests", manageContestVos);
            return contestsByConditionMap;
        } else {
            List<ContestVo> contestVos = copyList(records);
            contestsByConditionMap.put("contests", contestVos);
            return contestsByConditionMap;
        }
    }

    @Override
    public Boolean changeContestVisibility(Long contestId, Boolean visible) {
        contestMapper.update(null, new LambdaUpdateWrapper<Contest>()
                .eq(Contest::getId, contestId)
                .set(Contest::getVisible, visible));
        return true;
    }

    @Override
    public Boolean saveContest(ManageContestCreateParams manageContestCreateParams) {
        Contest contest = new Contest();
        BeanUtils.copyProperties(manageContestCreateParams, contest);
        Assert.state(contest.getEndTime().after(contest.getStartTime()), "比赛结束时间需要晚于开始时间");
        String contestName = contest.getName().trim();
        Assert.state(contestMapper.selectCount(
                new LambdaQueryWrapper<Contest>().eq(Contest::getName, contestName)) == 0,
                "比赛名重复"
        );
        contest.setName(contestName);
        contestMapper.insert(contest);
        return true;
    }

    @Override
    public Boolean updateContest(Contest contest) {
        Assert.state(contest.getEndTime().after(contest.getStartTime()), "比赛结束时间需要晚于开始时间");
        String contestName = contest.getName().trim();
        Assert.state(contestMapper.selectCount(new LambdaQueryWrapper<Contest>()
                        .eq(Contest::getName, contestName).ne(Contest::getId, contest.getId())) == 0,
                "比赛名重复"
        );
        contest.setName(contestName);
        /**
         * updateById方法在插入时，会根据实体类的每个属性进行非空判断，只有非空的属性所对应的字段才会出现在SQL语句中。
         * updateAllColumnById方法在插入时，不管属性是否为空，属性所对应的字段都会出现在
         */
        contestMapper.updateById(contest);
        return true;
    }

    @Override
    public List<RecentContestVo> getRecentContest(Integer day) {
        Assert.state(day > 0, "day必须大于0");
        DateTime now = new DateTime();
        DateTime daysAfterNow = now.minusDays(-day);
        /*
        1. 已经开始,还没结束
        2. day天之内开始
         */
        List<Contest> contests = contestMapper.selectList(new LambdaQueryWrapper<Contest>()
                .eq(Contest::getVisible, true).and(
                        x -> x.le(Contest::getStartTime, now.toDate())
                                .gt(Contest::getEndTime, now.toDate())
                                .or()
                                .le(Contest::getStartTime, daysAfterNow.toDate())
                                .gt(Contest::getStartTime, now.toDate())
                ).select(Contest::getId, Contest::getName, Contest::getStartTime, Contest::getEndTime)
        );
        List<RecentContestVo> recentContestVos = new ArrayList<>();
        for (Contest contest : contests) {
            ArrayList<Integer> contestStatus = getContestStatus(contest.getStartTime(), contest.getEndTime());
            recentContestVos.add(new RecentContestVo(contest.getId(), contest.getName(), contestStatus.get(0), contestStatus.get(1)));
        }
        return recentContestVos;
    }

    @Override
    public Boolean deleteContestByContestId(Long contestId) {
        contestMapper.deleteById(contestId);
        return true;
    }
}
