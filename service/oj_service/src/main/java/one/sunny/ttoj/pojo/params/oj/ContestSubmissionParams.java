package one.sunny.ttoj.pojo.params.oj;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class ContestSubmissionParams implements Serializable {
    @NotNull(message = "ContestSubmissionParams-currentPage不能为空")
    @Min(1)
    private Integer currentPage;
    @NotNull(message = "ContestSubmissionParams-pageSize不能为空")
    @Min(1)
    private Integer pageSize;
    @NotNull(message = "ContestSubmissionParams-contestId不能为空")
    private Long contestId;
    @NotNull(message = "ContestSubmissionParams-userId不能为空")
    private Long userId;
}
