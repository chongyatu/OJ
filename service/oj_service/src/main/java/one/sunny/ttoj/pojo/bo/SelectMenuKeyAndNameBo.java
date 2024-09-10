package one.sunny.ttoj.pojo.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class SelectMenuKeyAndNameBo {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    private String key;
    private String name;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long parentId;
}
