package one.sunny.ttoj.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_contest_user_problem")
@ApiModel(value="ContestUserProblem对象", description="")
public class ContestUserProblem implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.ASSIGN_ID)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;
    private String username;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long contestId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long problemId;
    private String problemDisplayId;
    private Integer waTimes;
    private Integer firstAcTime;
    private Boolean firstBlood;
}
