package cn.thinkfree;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 我不会就是分支了吧……
 * Created by lenovo on 2017/5/3.
 */
@EnableScheduling
@SpringBootConfiguration
@Configuration
@EnableTransactionManagement
@EnableAutoConfiguration
@EnableWebMvc
@EnableSwagger2
@ComponentScan(basePackages = {"cn.thinkfree"})
@MapperScan(basePackages = "cn.thinkfree.database.mapper")
//@EnableFeignClients(basePackages = "cn.thinkfree.service.feign")
public class ApplicationBoot extends SpringBootServletInitializer {
    @Override//为了打包springboot项目
    protected SpringApplicationBuilder configure(
            SpringApplicationBuilder builder) {
        return builder.sources(this.getClass());
    }

    public static void main(String[] args) {
        SpringApplication.run(ApplicationBoot.class, args);
    }

//    @Bean
//    public InternalResourceViewResolver internalResourceViewResolver(){
//        InternalResourceViewResolver internalResourceViewResolver = new InternalResourceViewResolver();
//        internalResourceViewResolver.setPrefix("/WEB-INF/pages/");
//        internalResourceViewResolver.setSuffix(".html");
//        return internalResourceViewResolver;
//    }


}
