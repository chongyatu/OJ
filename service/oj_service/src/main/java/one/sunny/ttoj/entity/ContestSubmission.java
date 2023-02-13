package one.sunny.ttoj.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_contest_submission")
@ApiModel(value="ContestSubmission对象", description="")
public class ContestSubmission implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id")
    private Long id;
    private Long contestId;
    private Long userId;
    private String username;
    private Long problemId;
    private String problemName;
    private String problemDisplayId;
    private Date submitTime;
    private String code;
    private String result;
    private String language;
    private Integer timeUse;
    private Integer memoryUse;
    private Integer codeLength;
}
