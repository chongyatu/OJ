package one.sunny.ttoj.pojo.params.oj;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ProblemQueryParams {
    @NotNull(message = "ProblemQueryParams-currentPage不能为空")
    private Integer currentPage;
    @NotNull(message = "ProblemQueryParams-pageSize不能为空")
    private Integer pageSize;
    private String authorName;
    private String name;
    private String level;
}
