package one.sunny.ttoj.pojo.vo.oj;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JudgeResultVo {
    private String err;
    private Object data;
}
