package one.sunny.ttoj;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;
import java.util.TimeZone;


@ComponentScan("one.sunny")
@SpringBootApplication(scanBasePackages = {"one.sunny"})
@MapperScan("one.sunny.ttoj.mapper")
public class OJApplication {
    @PostConstruct
    void started() {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Shanghai");
        TimeZone.setDefault(timeZone);
    }
    public static void main(String[] args) {
        SpringApplication.run(OJApplication.class, args);
    }
}
