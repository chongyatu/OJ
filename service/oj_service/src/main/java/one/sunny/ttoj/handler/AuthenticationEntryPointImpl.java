//package one.sunny.ttoj.handler;
//
//import com.alibaba.fastjson.JSON;
//import one.sunny.commonutils.R;
//import one.sunny.commonutils.WebUtils;
//import org.springframework.stereotype.Component;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@Component
//public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
//    @Override
//    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
//        R result = R.error().message("登录失败,请重新登录");
//        String json = JSON.toJSONString(result);
//        //处理异常
//        WebUtils.renderString(response,json);
//    }
//}
