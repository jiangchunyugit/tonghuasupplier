package cn.thinkfree.service.event.listener;

import cn.thinkfree.core.logger.AbsLogPrinter;
import cn.thinkfree.database.event.SendValidateCode;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * 验证码相关
 */
public class ValidateListener extends AbsLogPrinter {

    /**
     * 发送验证码
     * @param sendValidateCode
     */
    @TransactionalEventListener
    @Async
    public void sendValidateCode(SendValidateCode sendValidateCode){
        //TODO 发送验证码
        return;
    }

}
