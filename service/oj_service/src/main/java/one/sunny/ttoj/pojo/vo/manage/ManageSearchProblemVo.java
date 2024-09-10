package one.sunny.ttoj.pojo.vo.manage;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManageSearchProblemVo {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    private String displayId;
    private String name;
    private String authorName;
    private Boolean visible;
}
