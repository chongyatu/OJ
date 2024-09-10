package one.sunny.ttoj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import one.sunny.ttoj.entity.Contest;
import one.sunny.ttoj.pojo.params.manage.ManageContestCreateParams;
import one.sunny.ttoj.pojo.params.oj.ContestQueryParams;
import one.sunny.ttoj.pojo.params.oj.ContestRankingsParams;
import one.sunny.ttoj.pojo.vo.oj.ContestVo;
import one.sunny.ttoj.pojo.vo.oj.RecentContestVo;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface ContestService extends IService<Contest> {
    Map<String, Object> getRankingsByCondition(ContestRankingsParams contestRankingsParams);

    Map<String, Object> getContestsByCondition(ContestQueryParams contestQueryParams, Boolean admin);

    @Transactional
    Boolean changeContestVisibility(Long contestId, Boolean visible);

    @Transactional
    Boolean saveContest(ManageContestCreateParams manageContestCreateParams);

    @Transactional
    Boolean updateContest(Contest contest);

    List<RecentContestVo> getRecentContest(Integer day);

    @Transactional
    Boolean deleteContestByContestId(Long contestId);

    ContestVo getContestVoById(Long id);

    void saveContest2Redis(Long id,Long expireSeconds);
}
