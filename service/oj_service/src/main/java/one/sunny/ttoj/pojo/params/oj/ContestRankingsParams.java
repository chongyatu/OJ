package one.sunny.ttoj.pojo.params.oj;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class ContestRankingsParams {
    @NotNull(message = "ContestRankingsParams-contestId不能为空")
    private Long contestId;
    @NotNull(message = "ContestRankingsParams-pageSize不能为空")
    @Min(1)
    private Integer pageSize;
    @NotNull(message = "ContestRankingsParams-currentPage不能为空")
    @Min(1)
    private Integer currentPage;
}
