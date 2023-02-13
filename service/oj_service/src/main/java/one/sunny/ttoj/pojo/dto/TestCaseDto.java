package one.sunny.ttoj.pojo.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestCaseDto {
    private String  stripped_output_md5;
    private Integer output_size;
    private String  output_md5;
    private String input_name;
    private Integer input_size;
    private String output_name;
}
