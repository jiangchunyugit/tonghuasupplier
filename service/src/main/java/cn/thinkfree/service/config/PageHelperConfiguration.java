package cn.thinkfree.service.config;

import cn.thinkfree.core.base.MyLogger;
import cn.thinkfree.core.utils.LogUtil;
import com.github.pagehelper.PageHelper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;


@Configuration
public class PageHelperConfiguration {
    private static final MyLogger log = LogUtil.getLogger(PageHelperConfiguration.class);
    @Bean
    public PageHelper pageHelper() {
        log.info("Config GitHub PageHelper");
        PageHelper pageHelper = new PageHelper();
        Properties p = new Properties();
        p.setProperty("offsetAsPageNum", "true");
        p.setProperty("rowBoundsWithCount", "true");
        p.setProperty("reasonable", "true");
        //通过设置pageSize=0或者RowBounds.limit = 0就会查询出全部的结果。
        p.setProperty("pageSizeZero", "false");
        p.setProperty("dialect","PostgreSQL");
        pageHelper.setProperties(p);
        return pageHelper;
    }
}