package one.sunny.ttoj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import one.sunny.baseservice.exception.TTOJException;
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
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
