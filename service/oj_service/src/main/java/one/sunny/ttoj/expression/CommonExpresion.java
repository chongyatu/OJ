package one.sunny.ttoj.expression;

import one.sunny.ttoj.pojo.bo.BaseContext;
import one.sunny.ttoj.pojo.bo.LoginUserBo;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;

import java.util.List;

public class CommonExpresion {
    public static boolean hasAuthority(LoginUserBo loginUserBo, String authority){
        if (loginUserBo == null){
            return false;
        }
        return loginUserBo.getPermissions().contains(authority);
    }

    /**
     * 当前用户时候有其中一个权限
     * @param authorities
     * @return
     */
    public static boolean hasAnyAuthority(List<String> authorities){
        LoginUserBo loginUserBo = getLoginUserBo();
        if (loginUserBo == null){
            return false;
        }
        List<String> permissions = loginUserBo.getPermissions();
        for (String authority : authorities){
            if (permissions.contains(authority)){
                return true;
            }
        }
        return false;
    }

    /**
     * 得到当前用户 LoginUserBo
     * @return
     */
    public static LoginUserBo getLoginUserBo(){
        LoginUserBo loginUserBo = BaseContext.getCurrentLoginUserBo();
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication instanceof AnonymousAuthenticationToken){
//            return null;
//        }
//        Object principal = authentication.getPrincipal();
//        LoginUserBo loginUserBo = (LoginUserBo) principal;
//        System.out.println(loginUserBo);
        return loginUserBo;
    }
}
