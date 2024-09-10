package one.sunny.ttoj.pojo.vo.oj;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.lang.reflect.Type;
import java.util.Date;

@Data
public class ContestVo{
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    private String name;
    private String description;
    private Date startTime;
    private Date endTime;
    private Integer remainSeconds;
    private Integer status;
    private Integer penalty;
}
