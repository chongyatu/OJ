package one.sunny.ttoj.pojo.vo.manage;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class ManageContestVo {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    private String name;
    private String description;
    private Date startTime;
    private Date endTime;
    private Boolean visible;
    private String creatorName;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long creatorId;
    private Integer penalty;
    /*
      -1 -> 未开始
      0 -> 比赛中
      1 -> 已结束
     */
    private Integer status;
}
