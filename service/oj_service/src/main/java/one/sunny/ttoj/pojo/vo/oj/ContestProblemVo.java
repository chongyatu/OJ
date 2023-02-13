package one.sunny.ttoj.pojo.vo.oj;

import lombok.Data;

@Data
public class ContestProblemVo {
    private Long problemId;
    private String problemName;
    private Long contestId;
    private String problemDisplayId;
    private Integer submitNum;
    private Integer acNum;
    private String firstAcUsername;
    private Long firstAcUserId;
    private Integer problemFirstAcTime;
    private String authorName;
    private Boolean alreadyPassed;
}
