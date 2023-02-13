package one.sunny.ttoj.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestCaseInfo {
    private int test_case_number;
    private boolean spj;
    private Map<String, TestCaseDto> test_cases;
}
