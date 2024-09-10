package one.sunny.ttoj.pojo.vo.oj;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.Date;

@Data
public class ContestSubmissionVo {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;
    private String username;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
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
