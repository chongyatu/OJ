package one.sunny.ttoj.test;

import one.sunny.commonutils.RedisCache;
import one.sunny.ttoj.config.RedisConfig;
import one.sunny.ttoj.service.ContestService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MyTest {
    @Autowired
    private ContestService contestService;
    @Test
    public void test(){
        System.out.println(String.valueOf(1) + System.currentTimeMillis());
    }
    @Test
    public void testRedis(){
        contestService.saveContest2Redis(1623506241649319937L, 10L);
        contestService.saveContest2Redis(1622480034707148801L, 10L);
        contestService.saveContest2Redis(1623540501005406209L, 10L);
    }
}
