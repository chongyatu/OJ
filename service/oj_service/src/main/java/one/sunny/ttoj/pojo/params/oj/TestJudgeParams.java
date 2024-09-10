package one.sunny.ttoj.pojo.params.oj;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestJudgeParams {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    public Long userId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    public Long problemId;
    public String code;
    public String testcase;
    public String language;
}
