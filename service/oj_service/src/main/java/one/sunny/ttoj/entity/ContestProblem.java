package one.sunny.ttoj.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_contest_problem")
@ApiModel(value="ContestProblem对象", description="")
@AllArgsConstructor
@NoArgsConstructor
public class ContestProblem implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long problemId;
    private String problemName;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long contestId;
    private String problemDisplayId;
    private Integer submitNum;
    private Integer acNum;
    private Boolean visible;

    private String firstAcUsername;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long firstAcUserId;
    private Integer problemFirstAcTime;
}
