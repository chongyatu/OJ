package one.sunny.ttoj.interceptor;

import io.jsonwebtoken.Claims;
import one.sunny.commonutils.BaseContext;
import one.sunny.commonutils.JwtUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JwtTokenAdminInterceptor implements HandlerInterceptor {
    /**
     * 校验jwt
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /**
         * 通过 handler instanceof HandlerMethod
         * 判断当前拦截的请求是否是针对控制器方法的。
         * HandlerMethod 是 Spring MVC 中表示控制器方法的类。
         * 如果不是控制器方法（例如静态资源请求），直接放行。
         */
        if(!(handler instanceof HandlerMethod)){
            return true;
        }
        //1.从请求头中获取令牌
        String token = request.getHeader("token");
        //2.解析token
        try {
            System.out.println("token");
            System.out.println(token);
            Claims claims = JwtUtil.parseJWT(token);
            String userId = claims.getSubject();
            //3.保存到ThreadLocalMap中
            BaseContext.setCurrentId(Long.valueOf(userId));
            return true;
        } catch (Exception e) {
            response.setStatus(401);
            return false;
        }
//        return true;
    }
}
