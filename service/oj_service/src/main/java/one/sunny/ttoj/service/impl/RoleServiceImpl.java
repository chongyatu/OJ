package one.sunny.ttoj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import one.sunny.ttoj.entity.Role;
import one.sunny.ttoj.mapper.RoleMapper;
import one.sunny.ttoj.service.RoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

}
