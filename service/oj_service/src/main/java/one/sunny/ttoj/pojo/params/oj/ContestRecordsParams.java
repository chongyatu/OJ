package one.sunny.ttoj.pojo.params.oj;

import lombok.Data;

@Data
public class ContestRecordsParams {
    private Long userId;
    private Integer currentPage;
    private Integer pageSize;
}
