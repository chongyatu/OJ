package one.sunny.ttoj.filter;

import io.jsonwebtoken.Claims;
import one.sunny.commonutils.JwtUtil;
import one.sunny.commonutils.RedisCache;
import one.sunny.ttoj.pojo.bo.LoginUserBo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * OncePerRequestFilter 也是一个 Servlet 过滤器，可以处理 HTTP 请求和响应。
 * TODO:spring Security
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private RedisCache redisCache;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获取token
        String token = request.getHeader("token");
        if (!StringUtils.hasText(token)) {
            filterChain.doFilter(request, response);
            return;
        }
        //解析token
        String userId;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userId = claims.getSubject();
        } catch (Exception e) {
            e.printStackTrace();
            filterChain.doFilter(request, response);
            return;
        }
        //从redis中获取用户信息
        String redisKey = "login:" + userId;
        LoginUserBo loginUserBo = redisCache.getCacheObject(redisKey);
        if(!Objects.isNull(loginUserBo)){
            //存入SecurityContextHolder
            /**
             * 创建一个 UsernamePasswordAuthenticationToken对象，
             * 将 loginUserBo 作为 principal（主要身份），并设置用户的权限（loginUserBo.getAuthorities()）。
             * 然后，将这个身份验证对象存入 SecurityContextHolder，
             * 这样 Spring Security 可以在后续处理过程中识别当前的用户身份。
             * TODO:spring security
             */
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginUserBo,null, loginUserBo.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            //放行
            filterChain.doFilter(request, response);
            return;
        }
        filterChain.doFilter(request, response);
    }
}
