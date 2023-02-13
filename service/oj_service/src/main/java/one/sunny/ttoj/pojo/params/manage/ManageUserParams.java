package one.sunny.ttoj.pojo.params.manage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManageUserParams{
    @NotNull(message = "ManageUserParams-pageSize参数为空")
    @Min(1)
    private Integer pageSize;
    @NotNull(message = "ManageUserParams-currentPage参数为空")
    @Min(1)
    private Integer currentPage;
    private String username;
}
