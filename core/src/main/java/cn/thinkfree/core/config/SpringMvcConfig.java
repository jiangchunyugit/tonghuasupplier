package cn.thinkfree.core.config;

import cn.thinkfree.core.base.MyLogger;
import cn.thinkfree.core.convert.MySpringConvert;
import cn.thinkfree.core.convert.MySpringProcessor;
import cn.thinkfree.core.resolver.MultiRequestBodyArgumentResolver;
import cn.thinkfree.core.utils.LogUtil;
import com.google.common.collect.Lists;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * web端神奇的配置
 * WebMvcConfigurationSupport
 */
@Configuration
public class SpringMvcConfig extends WebMvcConfigurationSupport {

    MyLogger logger = LogUtil.getLogger(SpringMvcConfig.class);
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/resources/**")
//                .addResourceLocations("classpath:/resources/");
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
//        registry.addResourceHandler("/static/**")
//                .addResourceLocations("classpath:/webapp/**");
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        super.addResourceHandlers(registry);
    }

    /**
     * 配置请求处理
     * @return
     */
    @Bean
    public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {
        logger.info("Config Spring MVC");
        // 继承父级默认配置 为了不干扰别的解析器
        RequestMappingHandlerAdapter requestMappingHandlerAdapter = super.requestMappingHandlerAdapter();
        // 增加新的解析器
        List<HttpMessageConverter<?>> converters = Lists.newArrayList(new MySpringConvert());
        List<HandlerMethodArgumentResolver> argumentResolvers =Lists.newArrayList(new MySpringProcessor(converters));
        argumentResolvers.add(new MultiRequestBodyArgumentResolver());
        requestMappingHandlerAdapter.setCustomArgumentResolvers(argumentResolvers);
        List<HandlerMethodReturnValueHandler> returnValueHandlers = Lists.newArrayList(new MySpringProcessor(converters));
        requestMappingHandlerAdapter.setCustomReturnValueHandlers(returnValueHandlers);
        
        return requestMappingHandlerAdapter;
    }
    
    
//    @Override
//    protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
//    	 //添加json解析器 吕 2018-10-18
//        argumentResolvers.add(new MultiRequestBodyArgumentResolver());
//    }
   
    
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        super.configureMessageConverters(converters);
        converters.add(responseBodyConverter());
        converters.add(byteConvert());
    }
    @Bean
    public HttpMessageConverter<String> responseBodyConverter() {
        return new StringHttpMessageConverter(Charset.forName("UTF-8"));
    }
    @Bean
    public HttpMessageConverter<byte[]> byteConvert(){
        return new ByteArrayHttpMessageConverter();
    }

}
