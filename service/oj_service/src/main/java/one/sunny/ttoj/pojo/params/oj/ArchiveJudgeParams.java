package one.sunny.ttoj.pojo.params.oj;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
public class ArchiveJudgeParams {
    @NotNull(message = "ArchiveJudgeParams-userId参数不能为空")
    private Long userId;
    @NotNull(message = "ArchiveJudgeParams-username参数不能为空")
    @Length(min = 3, max=12, message = "ArchiveJudgeParams-username长度必须为3-12位")
    private String username;
    @NotNull(message = "ArchiveJudgeParams-problemId参数不能为空")
    private Long problemId;
    @NotNull(message = "ArchiveJudgeParams-problemName参数不能为空")
    private String problemName;
    // 不需要判空,为后端设置
    private Long submitTime;
    @NotNull(message = "ArchiveJudgeParams-code参数不能为空")
    private String code;
    @NotNull(message = "ArchiveJudgeParams-language参数不能为空")
    private String language;
}
