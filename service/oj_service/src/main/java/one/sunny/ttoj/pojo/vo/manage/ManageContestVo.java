package one.sunny.ttoj.pojo.vo.manage;

import lombok.Data;

import java.util.Date;

@Data
public class ManageContestVo {
    private Long id;
    private String name;
    private String description;
    private Date startTime;
    private Date endTime;
    private Boolean visible;
    private String creatorName;
    private Long creatorId;
    private Integer penalty;
    /*
      -1 -> 未开始
      0 -> 比赛中
      1 -> 已结束
     */
    private Integer status;
}
