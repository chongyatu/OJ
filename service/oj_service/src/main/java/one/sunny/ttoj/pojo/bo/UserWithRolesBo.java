package one.sunny.ttoj.pojo.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UserWithRolesBo {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    private String username;
    private String email;
    private Date createTime;
    private Date lastLogin;
    private List<String> roles;
}
