package one.sunny.ttoj.pojo.params.oj;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubmissionParams {
    private Integer currentPage;
    private Integer pageSize;
    private Long userId;
    private Long problemId;
}
