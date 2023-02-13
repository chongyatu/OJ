package one.sunny.ttoj.pojo.vo.manage;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.Date;

@Data
public class ManageProblemVo {
    private Long id;
    private String displayId;
    private Boolean visible;
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
}
