package cn.thinkfree.core.security.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.web.FilterInvocation;

/**
 * URL转换工具类
 */
public class UrlUtils {

    /**
     * 获取实际URL
     * @param filterInvocation
     * @return
     */
    public static String getRealURL(FilterInvocation filterInvocation){
//       String url = filterInvocation.getRequestUrl().replaceAll("/\\d{1,}","/{id}");
        // 该处将会受理
        // 自增ID    65532
        // 30位的编号  PC000001544006203618Af7Nt00000
        // 2至4位的编号时间戳 SGHT20176657219
        String url = filterInvocation.getRequestUrl().replaceAll("/\\d+|/[A-Za-z0-9]{30}|/[A-Za-z]{2,4}\\d+","/{id}");

        if(StringUtils.isNotBlank(url)&& url.indexOf("?") > 0){
            url = url.substring(0,url.indexOf("?"));
        }
        return url;
    }

    /**
     * 获取访问方法
     * @param filterInvocation
     * @return
     */
    public static String getRequestMethod(FilterInvocation filterInvocation){
        return filterInvocation.getHttpRequest().getMethod().toUpperCase();
    }


}
