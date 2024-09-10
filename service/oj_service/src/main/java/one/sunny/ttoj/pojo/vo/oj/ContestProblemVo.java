package one.sunny.ttoj.pojo.vo.oj;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class ContestProblemVo {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long problemId;
    private String problemName;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long contestId;
    private String problemDisplayId;
    private Integer submitNum;
    private Integer acNum;
    private String firstAcUsername;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long firstAcUserId;
    private Integer problemFirstAcTime;
    private String authorName;
    private Boolean alreadyPassed;
}
