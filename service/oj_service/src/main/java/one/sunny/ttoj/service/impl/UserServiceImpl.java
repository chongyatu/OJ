package one.sunny.ttoj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import one.sunny.baseservice.exception.AccountNotFoundException;
import one.sunny.baseservice.exception.PasswordErrorException;
import one.sunny.baseservice.exception.TTOJException;
import one.sunny.commonutils.Constants;
import one.sunny.commonutils.ErrorCode;
import one.sunny.commonutils.R;
import one.sunny.commonutils.RedisCache;
import one.sunny.ttoj.entity.Role;
import one.sunny.ttoj.entity.User;
import one.sunny.ttoj.entity.UserRole;
import one.sunny.ttoj.mapper.RoleMapper;
import one.sunny.ttoj.mapper.UserMapper;
import one.sunny.ttoj.pojo.bo.LoginUserBo;
import one.sunny.ttoj.pojo.bo.UserWithRolesBo;
import one.sunny.ttoj.pojo.params.manage.ManageUserParams;
import one.sunny.ttoj.pojo.params.oj.LoginParams;
import one.sunny.ttoj.pojo.vo.manage.ManageUserVo;
import one.sunny.ttoj.service.SsoService;
import one.sunny.ttoj.service.UserRoleService;
import one.sunny.ttoj.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;


import java.util.*;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private SsoService ssoService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User getUserByUsername(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user = userMapper.selectOne(queryWrapper);
        return user;
    }

    @Override
    public R getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            LoginUserBo loginUserBo = (LoginUserBo) authentication.getPrincipal();
            if (loginUserBo == null) {
                return R.error().message("获取用户信息失败");
            }
            return R.ok().data("loginUser", loginUserBo);
        } else {
            return R.error().message("暂未登录");
        }

    }

    @Override
    public Map<String, Object> getUserList(ManageUserParams manageUserParams) {
        Integer currentPage = manageUserParams.getCurrentPage();
        Integer pageSize = manageUserParams.getPageSize();
        if (currentPage == null || currentPage <= 0 || pageSize == null || pageSize <= 0) {
            throw new TTOJException(ErrorCode.PARAM_ERROR.getCode(), ErrorCode.UPDATE_ERROR.getMsg() + "分页参数异常");
        }
        String username = manageUserParams.getUsername();
        List<UserWithRolesBo> userWithRolesBos = userMapper.selectUserWithRolesBoPage(currentPage, pageSize, username);
        Integer total = Math.toIntExact(userMapper.selectCount(null));

        Map<String, Object> usersByConditionMap = new HashMap<>();
        List<ManageUserVo> manageUserVos = copyList(userWithRolesBos);
        usersByConditionMap.put("manageUserVos", manageUserVos);
        usersByConditionMap.put("total", total);
        return usersByConditionMap;
    }

    @Override
    public void updateUserRoles(Long userId, List<String> roles) {
//        System.out.println(Arrays.toString(roles.toArray()));
        List<Long> roleIds = new ArrayList<>();
        if (roles.size() > 0) {
            List<Role> roleList = roleMapper.selectList(new LambdaQueryWrapper<Role>()
                    .in(Role::getRoleName, roles).select(Role::getId)
            );
            roleList.forEach(item -> roleIds.add(item.getId()));
        }
        userRoleService.remove(new LambdaQueryWrapper<UserRole>()
                .eq(UserRole::getUserId, userId)
        );
        for (Long roleId : roleIds) {
            userRoleService.save(new UserRole(null, userId, roleId));
        }
        // 去除该用户的token,不去除的话身份需要在下一次登录时才能生效
        redisCache.deleteObject("login:" + userId);
    }

    @Override
    public User login(LoginParams loginParams) {
        //1.获取用户名和密码
        String username = loginParams.getUsername();
        String password = loginParams.getPassword();

        //2.根据用户名查询数据库中的数据
        User user = userMapper.getByUserName(username);

        //3.处理各种异常
        //3.1用户不存在
        if(user == null){
            //账号不存在
            throw new AccountNotFoundException(Constants.AcountNotFound);
        }
        //3.2密码对比 TODO：密码的加密解密
        //对前端传过来的明文密码进行md5加密处理，然后再进行比对
        //password.getBytes()将密码字符串转换为字节数组
        //md5DigestAsHex是DigestUtils类提供的一个静态方法，用于计算给定数据的MD5哈希值
        //并将结果转换为十六进制字符串表示，MD5被认为是不安全的哈希算法

//        String encodePassword = passwordEncoder.encode(password);
        String encodePassword = DigestUtils.md5DigestAsHex(password.getBytes());
        if(!encodePassword.equals(user.getPassword())){
            //密码错误
            throw new PasswordErrorException(Constants.PasswordError);
        }
        return user;
    }

    private <T> List<ManageUserVo> copyList(List<T> list) {
        List<ManageUserVo> manageUserVos = new ManagedList<>();
        for (T t : list) {
            manageUserVos.add(copy(t));
        }
        return manageUserVos;
    }

    private <T> ManageUserVo copy(T t) {
        ManageUserVo manageUserVo = new ManageUserVo();
        BeanUtils.copyProperties(t, manageUserVo);
        return manageUserVo;
    }
}
