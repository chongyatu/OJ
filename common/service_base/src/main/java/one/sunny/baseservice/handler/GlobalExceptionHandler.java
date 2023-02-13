package one.sunny.baseservice.handler;

import lombok.extern.slf4j.Slf4j;
import one.sunny.baseservice.exception.TTOJException;
import one.sunny.commonutils.ErrorCode;
import one.sunny.commonutils.R;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public R error(Exception e){
        log.error(e.getMessage());
        return R.error().message(e.getMessage());
    }

    @ExceptionHandler(TTOJException.class)
    @ResponseBody
    public R error(TTOJException e){
        log.error(e.getMessage());
        return R.error().message(e.getMsg()).code(e.getCode());
    }

    /**
     * @ExceptionHandler 是不能捕捉到过滤链中的异常的
     * @param e
     */
    @ExceptionHandler(AuthenticationException.class)
    public void error(AuthenticationException e) {
        throw e;
    }


    @ExceptionHandler(AccessDeniedException.class)
    public void error(AccessDeniedException e) {
        throw e;
    }

    /**
     * validator 统一异常封装
     */
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public R handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        String msgs = this.handle(e.getBindingResult().getFieldErrors());
        log.warn("URL:{} ,参数校验异常:{}", request.getRequestURI(),msgs);
        return R.error().code(ErrorCode.PARAM_ERROR.getCode()).message(msgs);
    }

    private String handle(List<FieldError> fieldErrors) {
        StringBuilder sb = new StringBuilder();
        for (FieldError obj : fieldErrors) {
            sb.append(obj.getField());
            sb.append("=[");
            sb.append(obj.getDefaultMessage());
            sb.append("]  ");
        }
        return sb.toString();
    }

    /**
     * Assert的异常统一封装
     */
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public R illegalArgumentException(IllegalArgumentException e, HttpServletRequest request) {
        log.warn("业务校验异常:{}", e);
        return R.error().code(ErrorCode.PARAM_ERROR.getCode()).message(e.getMessage());
    }
}

