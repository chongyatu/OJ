package one.sunny.ttoj.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
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

    @TableId(value = "id",type = IdType.ASSIGN_ID)
//    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    private String name;
    private String description;
    private Date startTime;
    private Date endTime;
    private Boolean visible;
    private String creatorName;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long creatorId;
    private Integer penalty;
}
