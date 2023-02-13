package one.sunny.ttoj.pojo.vo.oj;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecentContestVo {
    private Long id;
    private String name;
    private Integer remainSeconds;
    private Integer status;
}
