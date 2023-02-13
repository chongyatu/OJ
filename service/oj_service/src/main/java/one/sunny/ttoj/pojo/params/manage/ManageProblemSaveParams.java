package one.sunny.ttoj.pojo.params.manage;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class ManageProblemSaveParams {
    private Long id;
    @NotNull(message = "ManageProblemSaveParams-displayId为空")
    private String displayId;
    @NotNull(message = "ManageProblemSaveParams-visible为空")
    private Boolean visible;
    @NotNull(message = "ManageProblemSaveParams-name为空")
    private String name;
    @NotNull(message = "ManageProblemSaveParams-description为空")
    private String description;
    @NotNull(message = "ManageProblemSaveParams-input为空")
    private String input;
    @NotNull(message = "ManageProblemSaveParams-output为空")
    private String output;
    @NotNull(message = "ManageProblemSaveParams-hint为空")
    private String hint;
    @NotNull(message = "ManageProblemSaveParams-level为空")
    private String level;
    @NotNull(message = "ManageProblemSaveParams-timeLimit为空")
    private Integer timeLimit;
    @NotNull(message = "ManageProblemSaveParams-memoryLimit为空")
    private Integer memoryLimit;
    @NotNull(message = "ManageProblemSaveParams-languages为空")
    private String languages;
    private Integer submitNum;
    private Integer acNum;
    private String authorName;
    private String testCaseDir;
    @NotNull(message = "ManageProblemSaveParams-sampleCase为空")
    private String sampleCase;
    private Long authorId;
}
