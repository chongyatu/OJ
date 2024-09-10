package one.sunny.ttoj.pojo.params.manage;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ManageChangeContestProblemVisibility {
    @NotNull(message = "ManageChangeContestProblemVisibility-contestId参数不能为空")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long contestId;
    @NotNull(message = "ManageChangeContestProblemVisibility-problemId参数不能为空")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long problemId;
    @NotNull(message = "ManageChangeContestProblemVisibility-visible参数不能为空")
    private Boolean visible;
}
