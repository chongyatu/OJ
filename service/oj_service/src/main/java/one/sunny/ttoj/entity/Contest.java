package one.sunny.ttoj.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_contest")
@ApiModel(value="Contest对象", description="")
public class Contest implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id")
    private Long id;
    private String name;
    private String description;
    private Date startTime;
    private Date endTime;
    private Boolean visible;
    private String creatorName;
    private Long creatorId;
    private Integer penalty;
}
