package one.sunny.ttoj.pojo.params.oj;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ContestSubmitParams {
    @NotNull(message = "ContestSubmitParams-contestId不能为空")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long contestId;
    @NotNull(message = "ContestSubmitParams-problemId不能为空")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long problemId;
    @NotNull(message = "ContestSubmitParams-problemDisplayId不能为空")
    private String problemDisplayId;
    @NotNull(message = "ContestSubmitParams-userId不能为空")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;
    @NotNull(message = "ContestSubmitParams-username不能为空")
    private String username;
    @NotNull(message = "ContestSubmitParams-code不能为空")
    private String code;
    @NotNull(message = "ContestSubmitParams-language不能为空")
    private String language;
}
