//package one.sunny.ttoj.handler;
//
//import com.alibaba.fastjson.JSON;
//import one.sunny.commonutils.R;
//import one.sunny.commonutils.WebUtils;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.nio.file.AccessDeniedException;
//
//@Component
//public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
//
//    @Override
//    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
//        R result = R.error().message("您的权限不足");
//        String json = JSON.toJSONString(result);
//        //处理异常
//        WebUtils.renderString(response,json);
//    }
//}
