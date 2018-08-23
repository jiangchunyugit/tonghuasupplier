package cn.thinkfree;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by lenovo on 2017/5/3.
 */
@SpringBootConfiguration
@Configuration
@EnableTransactionManagement
@EnableAutoConfiguration
@EnableWebMvc
@EnableSwagger2
@ComponentScan( basePackages = {"cn.thinkfree"} )
@MapperScan(basePackages = "cn.thinkfree.database.mapper")
//@EnableFeignClients(basePackages = "cn.thinkfree.service.feign")
public class ApplicationBoot extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(ApplicationBoot.class, args);
    }
    @Override//为了打包springboot项目
    protected SpringApplicationBuilder configure(
            SpringApplicationBuilder builder) {
        return builder.sources(this.getClass());
    }

    @Bean
    public InternalResourceViewResolver internalResourceViewResolver(){
        InternalResourceViewResolver internalResourceViewResolver = new InternalResourceViewResolver();
        internalResourceViewResolver.setPrefix("/WEB-INF/pages/");
        internalResourceViewResolver.setSuffix(".jsp");
        return internalResourceViewResolver;
    }


}
