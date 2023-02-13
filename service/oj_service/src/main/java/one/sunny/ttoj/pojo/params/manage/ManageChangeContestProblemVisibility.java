package one.sunny.ttoj.pojo.params.manage;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ManageChangeContestProblemVisibility {
    @NotNull(message = "ManageChangeContestProblemVisibility-contestId参数不能为空")
    private Long contestId;
    @NotNull(message = "ManageChangeContestProblemVisibility-problemId参数不能为空")
    private Long problemId;
    @NotNull(message = "ManageChangeContestProblemVisibility-visible参数不能为空")
    private Boolean visible;
}
