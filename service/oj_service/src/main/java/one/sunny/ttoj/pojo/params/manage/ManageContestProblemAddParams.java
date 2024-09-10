package one.sunny.ttoj.pojo.params.manage;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ManageContestProblemAddParams {
    @NotNull(message = "ManageContestProblemAddParams-contestId参数为空")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long contestId;
    @NotNull(message = "ManageContestProblemAddParams-problemId参数为空")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long problemId;
    @NotNull(message = "ManageContestProblemAddParams-problemName参数为空")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String problemName;
    @NotNull(message = "ManageContestProblemAddParams-authorName参数为空")
    private String authorName;
    @NotNull(message = "ManageContestProblemAddParams-problemDisplayId参数为空")
    private String problemDisplayId;
}
