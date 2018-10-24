package cn.thinkfree.service.event;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import cn.thinkfree.database.model.ContractInfo;

/**
 * 发布消息服务
 * @ClassName: CustomListenerServie 
 * @author lqd
 * @Date 14点19分 
 */
@Service(value="customListenerServie")
public class CustomListenerServie {
	 private Logger logger = LoggerFactory.getLogger(CustomListenerServie.class);    
	    
	    //上下文对象
	    @Resource
	    private ApplicationContext applicationContext;

	    /**
	     * 发布消息
	     * @Title: publish 
	     * @author OnlyMate
	     * @Date 2018年9月14日 下午3:18:35 
	     * @param msg
	     */
	    public void publish(ContractInfo msg) {
	        //通过上下文对象发布监听new CustomEvent(this,msg)
	        applicationContext.publishEvent(new AuditEvent(msg));
	        logger.info("CustomListenerServie ==> publish method : {}", msg);
	    }
}
