package one.sunny.ttoj.pojo.params.oj;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ContestQueryParams {
    @NotNull(message = "ContestQueryParams-currentPage不能为空")
    @Min(value = 1, message = "ContestQueryParams-currentPage最小为1")
    private Integer currentPage;
    @NotNull(message = "ContestQueryParams-pageSize不能为空")
    @Min(value = 1, message = "ContestQueryParams-pageSize最小为1")
    private Integer pageSize;
    private String name;
    private Boolean visible;
    private String creatorName;
}
