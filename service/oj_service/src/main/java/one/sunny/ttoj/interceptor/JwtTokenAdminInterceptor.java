package one.sunny.ttoj.interceptor;

import one.sunny.ttoj.pojo.bo.BaseContext;
import one.sunny.ttoj.pojo.bo.LoginUserBo;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 如Spring框架中的拦截器（Interceptor）通常在请求到达处理器（Controller）之前和响应返回客户端之后起作用。
 */
public class JwtTokenAdminInterceptor implements HandlerInterceptor{
    /**
     * 校验jwt
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
//    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /**
         * 通过 handler instanceof HandlerMethod
         * 判断当前拦截的请求是否是针对控制器方法的。
         * HandlerMethod 是 Spring MVC 中表示控制器方法的类。
         * 如果不是控制器方法（例如静态资源请求），直接放行。
         */
//        System.out.println("preHandle....");
//        if(!(handler instanceof HandlerMethod)){
//            return true;
//        }
//        //1.从请求头中获取令牌
//        String token = request.getHeader("token");
//        String userId;
//        System.out.println("token + userId");
//        //2.解析token
//        try {
//            Claims claims = JwtUtil.parseJWT(token);
//            userId = claims.getSubject();
//            System.out.println("userId:" + userId);
//            //3.保存到ThreadLocalMap中
//            BaseContext.setCurrentId(Long.valueOf(userId));
//            return true;
//        } catch (Exception e) {
//            response.setStatus(401);
//            return false;
//        }
        LoginUserBo currentLoginUserBo = BaseContext.getCurrentLoginUserBo();
        if(currentLoginUserBo == null){
            response.setStatus(401);
            return false;
        }
        return true;
    }
}
