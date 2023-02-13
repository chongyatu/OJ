package one.sunny.ttoj.pojo.vo.manage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManageRouterVo {
    private Long id;
    private String name;
    private Long parentId;
    private List<ManageRouterVo> children;
}
