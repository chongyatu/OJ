package one.sunny.ttoj.pojo.vo.oj;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class ProblemVo {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    private String displayId;
    private String name;
    private String description;
    private String input;
    private String output;
    private String hint;
    private String level;
    private Integer timeLimit;
    private Integer memoryLimit;
    private String languages;
    private Integer submitNum;
    private Integer acNum;
    private String authorName;
    private String testCaseDir;
    private String sampleCase;
    private Long authorId;
    private Boolean alreadyPassed;
}
