package one.sunny.ttoj.controller.oj;

import one.sunny.commonutils.R;
import one.sunny.ttoj.pojo.dto.HeartBeat;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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
