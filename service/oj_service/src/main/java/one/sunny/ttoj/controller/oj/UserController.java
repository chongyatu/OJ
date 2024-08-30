package one.sunny.ttoj.controller.oj;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import one.sunny.commonutils.R;
import one.sunny.ttoj.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Slf4j
@Api("user")
@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;

    @ApiOperation("根据token获得当前登录用户")
    @GetMapping("currentUser")
    public R getCurrentUser(){
        return userService.getCurrentUser();
    }
}

