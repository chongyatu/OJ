package one.sunny.ttoj.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_submission")
@AllArgsConstructor
@NoArgsConstructor
public class Submission implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private Long userId;
    private String username;
    private Long problemId;
    private String problemName;
    private Date submitTime;
    private String code;
    private String result;
    private String language;
    private Integer timeUse;
    private Integer memoryUse;
}