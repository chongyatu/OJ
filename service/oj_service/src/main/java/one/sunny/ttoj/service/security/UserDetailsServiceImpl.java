//package one.sunny.ttoj.service.security;
//
//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//import one.sunny.ttoj.entity.User;
//import one.sunny.ttoj.mapper.UserMapper;
//import one.sunny.ttoj.pojo.bo.LoginUserBo;
//import one.sunny.ttoj.pojo.bo.SelectMenuKeyAndNameBo;
//import one.sunny.ttoj.pojo.vo.manage.ManageRouterVo;
//import one.sunny.ttoj.service.MenuService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Objects;
//
//@Service
//public class UserDetailsServiceImpl implements UserDetailsService {
//
//    @Autowired
//    private UserMapper userMapper;
//
//    @Autowired
//    private MenuService menuService;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        //查询用户信息
//        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
//                .eq(User::getUsername,username)
//        );
//        //如果没有查询到用户就抛出异常
//        if(Objects.isNull(user)){
//            throw new RuntimeException("用户名或者密码错误");
//        }
//
//        List<SelectMenuKeyAndNameBo> keyAndNames = menuService.getPermissionsByUserId(user.getId());
//        List<String> permissions = new ArrayList<>();
//        List<ManageRouterVo> manageRouterVos = new ArrayList<>();
//        keyAndNames.forEach(item -> {
//            permissions.add(item.getKey());
//            manageRouterVos.add(new ManageRouterVo(item.getId(), item.getName(), item.getParentId(), null));
//        });
//        List<ManageRouterVo> manageRouterTree = getManageRouterTree(new ArrayList<>(), 0L, manageRouterVos);
//        System.out.println(permissions);
//        //把数据封装成UserDetails返回
//        return new LoginUserBo(user,permissions,manageRouterTree);
//    }
//
//    List<ManageRouterVo> getManageRouterTree(List<ManageRouterVo> father, Long pid, List<ManageRouterVo> manageRouterVos){
//        for (ManageRouterVo manageRouterVo : manageRouterVos){
//            Long parentId = manageRouterVo.getParentId();
//            if (pid.equals(parentId)){
//                Long id = manageRouterVo.getId();
//                ManageRouterVo now = new ManageRouterVo(id, manageRouterVo.getName(), manageRouterVo.getParentId(), null);
//                List<ManageRouterVo> children = new ArrayList<>();
//                now.setChildren(getManageRouterTree(children, id, manageRouterVos));
//                father.add(now);
//            }
//        }
//        return father;
//    }
//}
