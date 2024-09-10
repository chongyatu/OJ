package one.sunny.ttoj.controller.oj;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import one.sunny.commonutils.R;
import one.sunny.ttoj.pojo.params.oj.LoginParams;
import one.sunny.ttoj.service.SsoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Done
 */
@Slf4j
@Api("sso")
@RestController
@CrossOrigin
@RequestMapping("sso")
public class SsoController {
    @Autowired
    private SsoService ssoService;
    @ApiOperation(" 用户登录")
    @PostMapping("login")
    public R login(@Validated @RequestBody LoginParams loginParams){
        return ssoService.login(loginParams);
    }

    @ApiOperation("用户注册")
    @PostMapping("register")
    public R register(@Validated @RequestBody LoginParams loginParams){
        return ssoService.register(loginParams);
    }

    @ApiOperation("用户退出登录")
    @PostMapping("logout")
    public R logout(){
        return ssoService.logout();
    }
}
