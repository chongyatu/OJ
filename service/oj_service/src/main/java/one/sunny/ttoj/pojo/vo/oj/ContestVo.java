package one.sunny.ttoj.pojo.vo.oj;

import lombok.Data;

import java.util.Date;

@Data
public class ContestVo{
    private Long id;
    private String name;
    private String description;
    private Date startTime;
    private Date endTime;
    private Integer remainSeconds;
    private Integer status;
    private Integer penalty;
}
