package one.sunny.ttoj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import one.sunny.commonutils.R;
import one.sunny.ttoj.entity.User;
import one.sunny.ttoj.pojo.bo.LoginUserBo;
import one.sunny.ttoj.pojo.params.manage.ManageUserParams;
import one.sunny.ttoj.pojo.params.oj.LoginParams;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface UserService extends IService<User> {
    User getUserByUsername(String username);

    R getCurrentUser();

    Map<String, Object> getUserList(ManageUserParams manageUserParams);

    @Transactional
    void updateUserRoles(Long userId, List<String> roles);

    User login(LoginParams loginParams);

    LoginUserBo loadUserByUsername(String username);
}
