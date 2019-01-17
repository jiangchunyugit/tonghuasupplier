package cn.thinkfree.config;

import cn.thinkfree.core.security.filter.util.SessionUserDetailsUtil;
import cn.thinkfree.database.vo.UserVO;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author
 */
@Slf4j
@Aspect
@Configuration
public class RequestLogger {

    /**
     * 跟踪访问接口的信息
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around("execution(* cn.thinkfree.controller..*.*(..))")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        String strLog = pjp.getTarget().getClass().getName()+"."+pjp.getSignature().getName();
//        UserVO userVO = (UserVO) SessionUserDetailsUtil.getUserDetails();
        log.info("[request ip     ]：{}", request.getRemoteAddr());
        log.info("[request url    ]：{}", request.getRequestURL().toString());
        log.info("[request method ]：{}", strLog);
        log.info("[request headers]：{}", getHeaders(request));
        log.info("[request params ]：{}", JSONObject.toJSONString(request.getParameterMap()));
//        log.info("[option msg     ]：{}", JSONObject.toJSONString(userVO));
        long time = System.currentTimeMillis();
        Object retVal = pjp.proceed();
        time = System.currentTimeMillis() - time;
        log.info("[response data  ]：{},耗时：{}", JSONObject.toJSONString(retVal), time);
        return retVal;
    }

    /**
     * 获取Headers信息
     * @param request
     * @return
     */
    private static String getHeaders(HttpServletRequest request) {
        Map<String, String> map = new LinkedHashMap<>();
        Enumeration<String> enumeration = request.getHeaderNames();
        while (enumeration.hasMoreElements()) {
            String key = enumeration.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }
        return JSONObject.toJSONString(map);
    }
}
