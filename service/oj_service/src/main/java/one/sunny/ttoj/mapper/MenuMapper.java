package one.sunny.ttoj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import one.sunny.ttoj.entity.Menu;
import one.sunny.ttoj.pojo.bo.SelectMenuKeyAndNameBo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuMapper extends BaseMapper<Menu> {

    List<SelectMenuKeyAndNameBo> selectPermissionsByUserId(Long userId);
}
