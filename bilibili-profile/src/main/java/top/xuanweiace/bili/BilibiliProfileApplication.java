package top.xuanweiace.bili;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
@EnableTransactionManagement
@MapperScan({"top.xuanweiace.bili.dao"})
public class BilibiliProfileApplication {

    public static void main(String[] args) {
        SpringApplication.run(BilibiliProfileApplication.class, args);
    }

}
