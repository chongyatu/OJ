package one.sunny.ttoj.controller.oj;

import one.sunny.ttoj.pojo.dto.HeartBeat;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 定期向判题服务器发送心跳
 */
@RestController
@RequestMapping("/common")
@CrossOrigin
public class CommonController {
    @PostMapping("heartbeat")
    public Map<String, Boolean> heartbeat(@RequestBody HeartBeat heartBeat){
        Map<String, Boolean> map = new HashMap<>();
        map.put("error", false);
        return map;
    }
}
