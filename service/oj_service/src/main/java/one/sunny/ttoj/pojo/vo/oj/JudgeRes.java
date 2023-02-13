package one.sunny.ttoj.pojo.vo.oj;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JudgeRes {
    private String result;
    private String compileErr;
    private Integer timeUse;
    private Integer memoryUse;
    private String output;
}
