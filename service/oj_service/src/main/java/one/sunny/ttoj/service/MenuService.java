package one.sunny.ttoj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import one.sunny.ttoj.entity.Menu;
import one.sunny.ttoj.pojo.bo.SelectMenuKeyAndNameBo;

import java.util.List;

public interface MenuService extends IService<Menu> {
    List<SelectMenuKeyAndNameBo> getPermissionsByUserId(Long id);
}
