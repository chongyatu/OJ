package one.sunny.ttoj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.swagger.models.auth.In;
import one.sunny.ttoj.entity.ContestUserProblem;
import one.sunny.ttoj.pojo.params.oj.ContestRankingsParams;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContestUserProblemMapper extends BaseMapper<ContestUserProblem> {
    List<ContestUserProblem> getRankingsByCondition(
            @Param("params")ContestRankingsParams contestRankingsParams,
            @Param("penalty") Integer penalty,
            @Param("creatorId") Long creatorId
    );
}
