package one.sunny.ttoj.controller.manage;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import one.sunny.commonutils.R;
import one.sunny.ttoj.pojo.params.manage.ManageUserParams;
import one.sunny.ttoj.pojo.vo.manage.ManageUserVo;
import one.sunny.ttoj.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@Slf4j
@Api("manage/user")
@RestController
@RequestMapping("manage/user")
@CrossOrigin
public class ManageUserController {
    @Autowired
    private UserService userService;

    @ApiOperation("管理员获取用户列表")
    @PostMapping("list")
    public R getUserListByCondition(@RequestBody ManageUserParams manageUserParams){
        Map<String, Object> usersByConditionMap = userService.getUserList(manageUserParams);
        List<ManageUserVo> manageUserVos = (List<ManageUserVo>) usersByConditionMap.get("manageUserVos");
        Integer total = (Integer) usersByConditionMap.get("total");
        return R.ok().data("users", manageUserVos).data("total", total);
    }

    @ApiOperation("管理员更新用户信息")
    @PostMapping("updateRoles/{userId}")
    public R updateUserRoles(@PathVariable("userId") Long userId, @RequestBody List<String> roles){
        userService.updateUserRoles(userId, roles);
        return R.ok().message("更新成功");
    }
}

