package one.sunny.ttoj.pojo.params.manage;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class ManageContestProblemQueryParams {
    @NotNull(message = "ManageContestProblemQueryParams-contestId不能为空")
    private Long contestId;
    @NotNull(message = "ManageContestProblemQueryParams-currentPage不能为空")
    @Min(1)
    private Integer currentPage;
    @NotNull(message = "ManageContestProblemQueryParams-pageSize不能为空")
    @Min(1)
    private Integer pageSize;
}
