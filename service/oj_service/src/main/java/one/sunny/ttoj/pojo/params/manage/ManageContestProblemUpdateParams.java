package one.sunny.ttoj.pojo.params.manage;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ManageContestProblemUpdateParams {
    @NotNull(message = "ManageContestProblemChangeParams-contestId参数为空")
    private Long contestId;
    @NotNull(message = "ManageContestProblemChangeParams-problemId参数为空")
    private Long problemId;
    private String problemDisplayId;
    private Boolean visible;
}
