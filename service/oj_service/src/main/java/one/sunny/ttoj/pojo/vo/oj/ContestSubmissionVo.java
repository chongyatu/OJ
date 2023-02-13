package one.sunny.ttoj.pojo.vo.oj;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.Date;

@Data
public class ContestSubmissionVo {
    private Long id;
    private Long userId;
    private String username;
    private Long problemId;
    private String problemName;
    private String problemDisplayId;
    private Date submitTime;
    private Integer codeLength;
    private String result;
    private String language;
    private Integer timeUse;
    private Integer memoryUse;
}
