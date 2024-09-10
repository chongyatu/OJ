package one.sunny.ttoj.interceptor;

import cn.hutool.core.util.StrUtil;
import io.jsonwebtoken.Claims;
import one.sunny.commonutils.JwtUtil;
import one.sunny.commonutils.RedisCache;
import one.sunny.ttoj.pojo.bo.BaseContext;
import one.sunny.ttoj.pojo.bo.LoginUserBo;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 刷新token有效期
 */
public class RefreshTokenInterceptor implements HandlerInterceptor {
    private RedisCache redisCache;

    public RefreshTokenInterceptor(RedisCache redisCache){
        this.redisCache = redisCache;
    }
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        if(StrUtil.isBlank(token)){
            return true;
        }
        //解析token
        String userId;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userId = claims.getSubject();
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
        String redisKey = "login:" + userId;
        LoginUserBo loginUserBo = redisCache.getCacheObject(redisKey);
        BaseContext.setCurrentLoginUserBo(loginUserBo);
        redisCache.expire(redisKey, 30*24*60*60);
        return true;
    }
}
