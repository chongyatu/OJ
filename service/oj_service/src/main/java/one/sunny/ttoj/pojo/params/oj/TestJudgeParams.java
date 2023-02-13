package one.sunny.ttoj.pojo.params.oj;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestJudgeParams {
    public Long userId;
    public Long problemId;
    public String code;
    public String testcase;
    public String language;
}
