package one.sunny.ttoj.pojo.vo.manage;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class ManageContestProblemVo {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long problemId;
    private String problemName;
    private String problemDisplayId;
    private Boolean visible;
    private String authorName;
}
