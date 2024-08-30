package one.sunny.ttoj.interceptor;

import io.jsonwebtoken.Claims;
import one.sunny.commonutils.BaseContext;
import one.sunny.commonutils.JwtUtil;
import one.sunny.commonutils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 如Spring框架中的拦截器（Interceptor）通常在请求到达处理器（Controller）之前和响应返回客户端之后起作用。
 */
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
        String userId;
        //2.解析token
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userId = claims.getSubject();
            //3.保存到ThreadLocalMap中
            //页面刷新一次就产生一个新的线程，出现一个新的ThreadLocal，
            // 所以每次拦截器中都要放入一次用户userId
            //TODO:ThreadLocal原理
            BaseContext.setCurrentId(Long.valueOf(userId));
            return true;
        } catch (Exception e) {
            response.setStatus(401);
            return false;
        }
    }
}
