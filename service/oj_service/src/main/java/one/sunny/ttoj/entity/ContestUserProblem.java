package one.sunny.ttoj.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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

    @TableId(value = "id")
    private Long id;
    private Long userId;
    private String username;
    private Long contestId;
    private Long problemId;
    private String problemDisplayId;
    private Integer waTimes;
    private Integer firstAcTime;
    private Boolean firstBlood;
}
