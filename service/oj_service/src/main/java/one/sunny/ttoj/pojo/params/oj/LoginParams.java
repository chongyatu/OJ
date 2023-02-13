package one.sunny.ttoj.pojo.params.oj;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
public class LoginParams {
    @NotNull(message = "LoginParams-username不能为空")
    @Length(min = 3, max = 12, message = "LoginParams-username长度在3-12位")
    private String username;
    @NotNull(message = "LoginParams-password不能为空")
    @Length(min = 3, max = 18, message = "LoginParams-password长度在3-18位")
    private String password;
    private String confirmPassword;
}
