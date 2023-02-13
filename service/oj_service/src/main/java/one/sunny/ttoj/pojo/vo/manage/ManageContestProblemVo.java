package one.sunny.ttoj.pojo.vo.manage;

import lombok.Data;

@Data
public class ManageContestProblemVo {
    private Long problemId;
    private String problemName;
    private String problemDisplayId;
    private Boolean visible;
    private String authorName;
}
