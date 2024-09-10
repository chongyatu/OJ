package one.sunny.ttoj.pojo.vo.oj;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContestUserRankVo implements Serializable {
    private String username;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;
    private Integer acNum;
    private Integer totalTime; // minutes
    private Map<String, Boolean> isFirstBlood;
    private Map<String, Integer> firstAcTime;
    private Map<String, Integer> waTimes;
}
