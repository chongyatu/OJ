package one.sunny.ttoj.pojo.params.oj;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class ContestRecordsParams {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;
    private Integer currentPage;
    private Integer pageSize;
}
