package one.sunny.ttoj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import one.sunny.ttoj.entity.ContestProblem;
import one.sunny.ttoj.entity.Problem;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ContestProblemMapper extends BaseMapper<ContestProblem> {
    // 没有 problemDisplayId
    Problem getProblemByContestIdAndDisplayId(@Param("contestId")Long contestId, @Param("problemDisplayId")String problemDisplayId);
}
