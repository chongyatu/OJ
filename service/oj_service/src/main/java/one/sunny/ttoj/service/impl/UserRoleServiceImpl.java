package one.sunny.ttoj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import one.sunny.ttoj.entity.UserRole;
import one.sunny.ttoj.mapper.UserRoleMapper;
import one.sunny.ttoj.service.UserRoleService;
import org.springframework.stereotype.Service;

@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

}
