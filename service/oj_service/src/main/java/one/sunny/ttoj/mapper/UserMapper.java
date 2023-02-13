package one.sunny.ttoj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import one.sunny.ttoj.entity.User;
import one.sunny.ttoj.pojo.bo.UserWithRolesBo;
import org.apache.ibatis.annotations.Param;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface UserMapper extends BaseMapper<User> {
    List<UserWithRolesBo> selectUserWithRolesBoPage(@Param("currentPage")Integer currentPage, @Param("pageSize")Integer pageSize, @Param("username")String username);
}
