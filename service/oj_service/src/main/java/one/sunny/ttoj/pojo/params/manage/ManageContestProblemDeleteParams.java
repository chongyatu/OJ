package one.sunny.ttoj.pojo.params.manage;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ManageContestProblemDeleteParams {
    @NotNull(message = "ManageContestProblemDeleteParams-contestId参数为空")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long contestId;
    @NotNull(message = "ManageContestProblemDeleteParams-problemId参数为空")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long problemId;
}
