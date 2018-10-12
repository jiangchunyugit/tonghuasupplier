package cn.thinkfree.aop;

import cn.thinkfree.core.annotation.MySysLog;
import cn.thinkfree.core.base.MyLogger;
import cn.thinkfree.core.security.filter.util.SecurityRequestUtil;
import cn.thinkfree.core.security.filter.util.SessionUserDetailsUtil;
import cn.thinkfree.core.utils.LogUtil;
import cn.thinkfree.database.model.LogInfo;
import cn.thinkfree.database.vo.UserVO;
import cn.thinkfree.service.system.LogInfoService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

@Aspect
@Component
public class SysLogAop {
    MyLogger  logger = LogUtil.getLogger(SysLogAop.class);

    @Autowired
    LogInfoService logInfoService;

    private static LogInfo DEFAULT_LOGIN_INFO = new LogInfo();

    @Pointcut("@annotation(cn.thinkfree.core.annotation.MySysLog)")
    public void sysLogPoint(){ }




    @AfterThrowing(pointcut = "sysLogPoint()",throwing = "e")
    public void saveLogByAfterThrowing(JoinPoint joinPoint, Throwable e) throws Throwable {
        logger.error("日志切面操作失败:{}",e);
        e.printStackTrace();
    }

    @AfterReturning(pointcut = "sysLogPoint()",returning = "retObj")
    public void saveLogByAfter(JoinPoint joinPoint,Object retObj) throws Throwable {
        // todo 切面执行（服务器上打印出来了）
        // 目标类
        Class targetClass = joinPoint.getTarget().getClass();
        // 目标方法
        String methodName = joinPoint.getSignature().getName();
        // 获取注解详情
        Method method = getMethod(targetClass, methodName);

        MySysLog mySysLog = method.getAnnotation(MySysLog.class);


        LogInfo logInfo = buildLogInfo();

        UserVO userVO = (UserVO) SessionUserDetailsUtil.getUserDetails();
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes)ra;
        HttpServletRequest request = sra.getRequest();
        String ip =SecurityRequestUtil.getRequestIp(request);
        logInfo.setCreateTime(new Date());
        logInfo.setFromIp(ip);
        logInfo.setOperator(userVO.getUsername());
        logInfo.setOperatorName(userVO.getPcUserInfo().getName());
        logInfo.setOperationType(mySysLog.action().code.shortValue());
        logInfo.setOwnModule(mySysLog.module().code.shortValue());
        logInfo.setDetails(mySysLog.desc());
        logInfo.setCompanyId(userVO.getCompanyID());
        logInfoService.save(logInfo);
//        String url = request.getRequestURI();

//        LogInfo systemLog = SystemLogFactory.newLogInfo();
//        boolean hasSignatures = Signatures.class.isAssignableFrom(retObj.getClass());
//        if(hasSignatures){
//            systemLog.setSignatures(((Signatures) retObj).getSignatures());
//        }
//        systemLog.setOperationUrl(url);
//        systemLog.setOwnModel(haoheLog.module().desc());
//        systemLog.setOperationType(haoheLog.action().value());
        // 处理系统默认参数
//        systemLog.setOperator(SessionUserDetailsUtil.getLoginUserID());
//        systemLog.setOperationTime(new Date());
//        logInfoService.saveLogByAOP(systemLog);
    }

    private LogInfo buildLogInfo(){
        try {
            LogInfo log = DEFAULT_LOGIN_INFO.clone();
            return log;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return new LogInfo();
    }



    private Method getMethod(Class targetClass, String methodName) {

        Method[] methods = targetClass.getMethods();
        Method targetMethod = null;
        for(Method m:methods){
            if(methodName.equals(m.getName())){
                targetMethod = m;
                break;
            }
        }
        return targetMethod;
    }

}
