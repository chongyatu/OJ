package one.sunny.ttoj.service;

import one.sunny.commonutils.R;
import one.sunny.ttoj.pojo.params.oj.LoginParams;
import org.springframework.transaction.annotation.Transactional;

public interface SsoService {
    R login(LoginParams loginParams);
    @Transactional
    R register(LoginParams loginParams);
    R logout();
}
