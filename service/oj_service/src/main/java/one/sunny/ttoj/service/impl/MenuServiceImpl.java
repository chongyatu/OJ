package one.sunny.ttoj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import one.sunny.ttoj.entity.Menu;
import one.sunny.ttoj.mapper.MenuMapper;
import one.sunny.ttoj.pojo.bo.SelectMenuKeyAndNameBo;
import one.sunny.ttoj.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {
    @Autowired
    private MenuMapper menuMapper;

    @Override
    public List<SelectMenuKeyAndNameBo> getPermissionsByUserId(Long id) {
        return menuMapper.selectPermissionsByUserId(id);
    }
}
