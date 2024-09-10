package one.sunny.ttoj.pojo.bo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;
import one.sunny.ttoj.entity.User;
import one.sunny.ttoj.pojo.vo.manage.ManageRouterVo;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class LoginUserBo  {

    private User user;

    private List<String> permissions;

    private List<ManageRouterVo> manageRouters;

    public LoginUserBo(User user, List<String> permissions, List<ManageRouterVo> manageRouters) {
        this.user = user;
        this.permissions = permissions;
        this.manageRouters = manageRouters;
    }

//    @JSONField(serialize = false)
//    private List<SimpleGrantedAuthority> authorities;
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        if (authorities != null) {
//            return authorities;
//        }
//        //把permissions中String类型的权限信息封装成SimpleGrantedAuthority对象
////       authorities = new ArrayList<>();
////        for (String permission : permissions) {
////            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(permission);
////            authorities.add(authority);
////        }
//        if (permissions == null) {
//            return null;
//        }
//        authorities = permissions.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
//        return authorities;
//    }
//
//    @Override
//    public String getPassword() {
//        return user.getPassword();
//    }
//
//    @Override
//    public String getUsername() {
//        return user.getUsername();
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
}
