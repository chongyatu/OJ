package one.sunny.ttoj.pojo.params.manage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManageContestCreateParams implements Serializable {
    @NotBlank(message = "ContestCreateParams-name参数不能为空")
    private String name;
    @NotBlank(message = "ContestCreateParams-description参数不能为空")
    private String description;
    @NotNull(message = "ContestCreateParams-startTime参数不能为空")
    private Date startTime;
    @NotNull(message = "ContestCreateParams-endTime参数不能为空")
    private Date endTime;
    @NotNull(message = "ContestCreateParams-visible参数不能为空")
    private Boolean visible;
    @NotBlank(message = "ContestCreateParams-creatorName参数不能为空")
    private String creatorName;
    @NotNull(message = "ContestCreateParams-creatorId参数不能为空")
    private Long creatorId;
    @NotNull(message = "ContestCreateParams-penalty参数不能为空")
    @Min(0)
    private Integer penalty;
}
