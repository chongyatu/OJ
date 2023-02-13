package one.sunny.ttoj.pojo.vo.oj;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContestRankVo implements Serializable {
    private List<ContestUserRankVo> userRanks;
    private List<ContestProblemVo> contestProblems;
}
