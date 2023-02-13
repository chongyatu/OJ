package one.sunny.ttoj.pojo.bo;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UserWithRolesBo {
    private Long id;
    private String username;
    private String email;
    private Date createTime;
    private Date lastLogin;
    private List<String> roles;
}
