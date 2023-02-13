package one.sunny.ttoj.pojo.bo;

import lombok.Data;
import one.sunny.ttoj.pojo.vo.manage.ManageSearchProblemVo;

import java.util.List;

@Data
public class NotAddedProblemBo {
    private Integer total;
    private List<ManageSearchProblemVo> problems;
}
