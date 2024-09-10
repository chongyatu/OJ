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

    //确保 pageSize 不能为 null。如果为空，将会抛出带有指定消息的验证异常。
    @NotNull(message = "ManageUserParams-pageSize参数为空")
    //确保 pageSize 的值最小为 1，避免不合理的分页大小。
    @Min(1)
    private Integer pageSize;
    @NotNull(message = "ManageUserParams-currentPage参数为空")
    @Min(1)
    private Integer currentPage;
    private String username;
}
