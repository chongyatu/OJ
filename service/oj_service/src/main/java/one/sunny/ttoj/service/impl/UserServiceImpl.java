package one.sunny.ttoj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import one.sunny.commonutils.Constants;
import one.sunny.commonutils.ErrorCode;
import one.sunny.commonutils.R;
import one.sunny.commonutils.RedisCache;
import one.sunny.ttoj.entity.Role;
import one.sunny.ttoj.entity.User;
import one.sunny.ttoj.entity.UserRole;
import one.sunny.ttoj.exception.AccountNotFoundException;
import one.sunny.ttoj.exception.PasswordErrorException;
import one.sunny.ttoj.exception.TTOJException;
import one.sunny.ttoj.mapper.RoleMapper;
import one.sunny.ttoj.mapper.UserMapper;
import one.sunny.ttoj.pojo.bo.BaseContext;
import one.sunny.ttoj.pojo.bo.LoginUserBo;
import one.sunny.ttoj.pojo.bo.SelectMenuKeyAndNameBo;
import one.sunny.ttoj.pojo.bo.UserWithRolesBo;
import one.sunny.ttoj.pojo.params.manage.ManageUserParams;
import one.sunny.ttoj.pojo.params.oj.LoginParams;
import one.sunny.ttoj.pojo.vo.manage.ManageRouterVo;
import one.sunny.ttoj.pojo.vo.manage.ManageUserVo;
import one.sunny.ttoj.service.MenuService;
import one.sunny.ttoj.service.SsoService;
import one.sunny.ttoj.service.UserRoleService;
import one.sunny.ttoj.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;


import java.util.*;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserRoleService userRoleService;

//    @Autowired
//    private PasswordEncoder passwordEncoder;

    @Override
    public User getUserByUsername(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user = userMapper.selectOne(queryWrapper);
        return user;
    }

    @Override
    public R getCurrentUser() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUserBo loginUserBo = BaseContext.getCurrentLoginUserBo();
        if(loginUserBo == null) {
            return R.error().message("获取用户信息失败");
        }else {
            return R.ok().data("loginUser", loginUserBo);
        }
//        if (!(authentication instanceof AnonymousAuthenticationToken)) {
//            LoginUserBo loginUserBo = (LoginUserBo) authentication.getPrincipal();
//            if (loginUserBo == null) {
//                return R.error().message("获取用户信息失败");
//            }
//            return R.ok().data("loginUser", loginUserBo);
//        } else {
//            return R.error().message("暂未登录");
//        }
//        String userId = BaseContext.getCurrentId().toString();
//        if(userId == null)return R.error().message("暂未登陆");
//        String redisKey = "login:" + userId;
//        LoginUserBo loginUserBo = redisCache.getCacheObject(redisKey);
//        if(loginUserBo == null) {
//            return R.error().message("获取用户信息失败");
//        }else{
//            return R.ok().data("loginUser",loginUserBo);
//        }
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

        //selectCount(null) 代表获取表中的记录总数。这里的 null 参数意味着没有额外的查询条件（即计数整个表的记录数）。
        //selectCount 方法返回的通常是 long 类型，因为记录的总数可能很大。为了确保转换过程中的溢出处理和精度控制，
        // 使用 Math.toIntExact(long value) 方法将 long 类型转换为 int 类型。
        Integer total = Math.toIntExact(userMapper.selectCount(null));

        Map<String, Object> usersByConditionMap = new HashMap<>();

        List<ManageUserVo> manageUserVos = copyList(userWithRolesBos);
        usersByConditionMap.put("manageUserVos", manageUserVos);
        usersByConditionMap.put("total", total);
        return usersByConditionMap;
    }

    @Override
    public void updateUserRoles(Long userId, List<String> roles) {
        //这里用的是mybatis-plus
        //roles存储角色名称，根据角色名称查询出角色id存入rolesIds
        List<Long> roleIds = new ArrayList<>();
        if (roles.size() > 0) {
            List<Role> roleList = roleMapper.selectList(
                    new LambdaQueryWrapper<Role>().in(Role::getRoleName, roles).select(Role::getId)
            );
            roleList.forEach(item -> roleIds.add(item.getId()));
        }
        //移除掉t_user_role表中与当前用户userId相关联的角色数据
        userRoleService.remove(new LambdaQueryWrapper<UserRole>()
                .eq(UserRole::getUserId, userId)
        );

        //将新的角色关系插入t_user_role表中
        for (Long roleId : roleIds) {
            userRoleService.save(new UserRole(null, userId, roleId));
        }
        // 去除该用户的token,不去除的话身份需要在下一次登录时才能生效，因为权限改变了
        redisCache.deleteObject("login:" + userId);
    }

    @Override
    public User login(LoginParams loginParams) {
        //1.获取用户名和密码
        String username = loginParams.getUsername();
        String password = loginParams.getPassword();

        //2.根据用户名查询数据库中的数据
        User user = getUserByUsername(username);
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

    @Autowired
    private MenuService menuService;

    @Override
    public LoginUserBo loadUserByUsername(String username)  {
        //查询用户信息
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername,username)
        );
        //如果没有查询到用户就抛出异常
        if(Objects.isNull(user)){
            throw new RuntimeException("用户名或者密码错误");
        }

        List<SelectMenuKeyAndNameBo> keyAndNames = menuService.getPermissionsByUserId(user.getId());
        List<String> permissions = new ArrayList<>();
        List<ManageRouterVo> manageRouterVos = new ArrayList<>();

        keyAndNames.forEach(item -> {
            permissions.add(item.getKey());
            manageRouterVos.add(new ManageRouterVo(item.getId(), item.getName(), item.getParentId(), null));
        });
        List<ManageRouterVo> manageRouterTree = getManageRouterTree(new ArrayList<>(), 0L, manageRouterVos);
        System.out.println(permissions);
        return new LoginUserBo(user,permissions,manageRouterTree);
    }

    List<ManageRouterVo> getManageRouterTree(List<ManageRouterVo> father, Long pid, List<ManageRouterVo> manageRouterVos){
        for (ManageRouterVo manageRouterVo : manageRouterVos){
            Long parentId = manageRouterVo.getParentId();
            if (pid.equals(parentId)){
                Long id = manageRouterVo.getId();
                ManageRouterVo now = new ManageRouterVo(id, manageRouterVo.getName(), manageRouterVo.getParentId(), null);
                List<ManageRouterVo> children = new ArrayList<>();
                now.setChildren(getManageRouterTree(children, id, manageRouterVos));
                father.add(now);
            }
        }
        return father;
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
