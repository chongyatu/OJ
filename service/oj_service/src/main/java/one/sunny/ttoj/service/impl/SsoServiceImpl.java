package one.sunny.ttoj.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import one.sunny.commonutils.*;
import one.sunny.ttoj.entity.User;
import one.sunny.ttoj.pojo.bo.BaseContext;
import one.sunny.ttoj.pojo.bo.LoginUserBo;
import one.sunny.ttoj.service.SsoService;
import one.sunny.ttoj.service.UserService;
import one.sunny.ttoj.pojo.params.oj.LoginParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.concurrent.TimeUnit;

import static one.sunny.commonutils.Constants.CACHE_USER_TIME;

/**
 * Done
 */
@Service
public class SsoServiceImpl implements SsoService {
    private static final String salt = "sunny";
    private static final String Cpp = "C++";

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private UserService userService;

//    @Autowired
//    private AuthenticationManager authenticationManager; // in securityConfig

//    @Autowired
//    private PasswordEncoder passwordEncoder;

    /**
     * 用户登陆
     * @param loginParams
     * @return
     */
    @Override
    public R login(LoginParams loginParams) {
        //1.用户登陆获取用户对象
        //2.登陆成功后，生成jwt令牌
        User user = userService.login(loginParams);
        //3.登陆成功后，生成jwt令牌
        String userId = user.getId().toString();
        String jwt = JwtUtil.createJWT(userId);

        /**
         * TODO：刷新token消失，无法实现登出功能
         */
        LoginUserBo loginUserBo = userService.loadUserByUsername(user.getUsername());
        redisCache.setCacheObject("login:"+userId, loginUserBo,CACHE_USER_TIME, TimeUnit.SECONDS);
        return R.ok().data("token", jwt).data("loginUser", loginUserBo).message("登录成功");
    }
//    @Override
//    public R login(LoginParams loginParams) {
//        // UsernamePasswordAuthenticationToken -> 封装的 Authentication对象,只有用户名和密码,没有权限
//        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginParams.getUsername(),loginParams.getPassword());
//        // AuthenticationManager authenticate进行用户认证
//        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
//        //如果认证没通过，给出对应的提示
//        if(Objects.isNull(authenticate)){
//            throw new RuntimeException("登录失败");
//        }
//        //如果认证通过了，使用userid生成一个jwt jwt存入ResponseResult返回
//        LoginUserBo loginUserBo = (LoginUserBo) authenticate.getPrincipal();
//        String userId = loginUserBo.getUser().getId().toString();
//        String jwt = JwtUtil.createJWT(userId);
//        //把完整的用户信息存入redis  userid作为key
//        redisCache.setCacheObject("login:"+userId, loginUserBo, 30*24*60*60, TimeUnit.SECONDS);
//        return R.ok().data("token", jwt).data("loginUser", loginUserBo).message("登录成功");
//    }

//    @Override
//    public R register(LoginParams loginParams) {
//        String username = loginParams.getUsername();
//        String password = loginParams.getPassword();
//        String confirmPassword = loginParams.getConfirmPassword();
//        if (StringUtils.isBlank(username)
//                || StringUtils.isBlank(password)
//                || StringUtils.isBlank(confirmPassword)
//        ){
//            return R.error().message("用户名或密码不能为空");
//        }
//        if (!password.equals(confirmPassword)){
//            return R.error().message("两次输入密码不同");
//        }
//        // 查看用户是否已经存在
//        User user = this.userService.getUserByUsername(username);
//
//        if (user != null){
//            return R.error().message("用户名已被注册");
//        }
//        user = new User();
//        user.setUsername(username);
//        user.setPassword(passwordEncoder.encode(password));
//        userService.save(user);
//        return R.ok();
//    }
//    //TODO:是否可以添加一个添加时间，添加用户，修改时间，修改用户
    @Override
    public R register(LoginParams loginParams) {
        String username = loginParams.getUsername();
        String password = loginParams.getPassword();
        String confirmPassword = loginParams.getConfirmPassword();
        if (StringUtils.isBlank(username)
                || StringUtils.isBlank(password)
                || StringUtils.isBlank(confirmPassword)
        ){
            return R.error().message("用户名或密码不能为空");
        }
        if (!password.equals(confirmPassword)){
            return R.error().message("两次输入密码不同");
        }
        // 查看用户是否已经存在
        User user = userService.getUserByUsername(username);

        if (user != null){
            return R.error().message("用户名已被注册");
        }
        user = new User();
        user.setUsername(username);
        String encodePassword = DigestUtils.md5DigestAsHex(password.getBytes());
        user.setPassword(encodePassword);
        userService.save(user);
        return R.ok();
    }

    @Override
    public R logout() {
//        获取SecurityContextHolder中的用户id
//        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
//        LoginUserBo loginUserBo = (LoginUserBo) authentication.getPrincipal();

        //删除redis中的值
//        Long userId = BaseContext.getCurrentId();
        LoginUserBo loginUserBo = BaseContext.getCurrentLoginUserBo();
        Long userId = loginUserBo.getUser().getId();
//        //TODO:员工退出的时候是否需要移除ThreadLocal中的值
        BaseContext.removeCurrentLoginUserBo();
        redisCache.deleteObject("login:"+userId);
        return R.ok().message("登出成功");
    }
}
