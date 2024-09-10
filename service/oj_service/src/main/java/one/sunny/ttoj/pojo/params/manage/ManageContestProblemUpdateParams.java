package one.sunny.ttoj.pojo.params.manage;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ManageContestProblemUpdateParams {
    @NotNull(message = "ManageContestProblemChangeParams-contestId参数为空")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long contestId;
    @NotNull(message = "ManageContestProblemChangeParams-problemId参数为空")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long problemId;
    private String problemDisplayId;
    private Boolean visible;
}
