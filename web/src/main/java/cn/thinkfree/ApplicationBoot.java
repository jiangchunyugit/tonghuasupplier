package cn.thinkfree;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by lenovo on 2017/5/3.
 */
@Configuration
@EnableTransactionManagement
@EnableAutoConfiguration
@SpringBootConfiguration
@ComponentScan( basePackages = {"cn.thinkfree"} )
@MapperScan(basePackages = "cn.thinkfree.database.mapper")
public class ApplicationBoot{
    public static void main(String[] args) {
        SpringApplication.run(ApplicationBoot.class, args);
    }
}
