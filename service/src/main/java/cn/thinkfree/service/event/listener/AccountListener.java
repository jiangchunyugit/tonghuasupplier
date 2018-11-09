package cn.thinkfree.service.event.listener;

import cn.thinkfree.core.logger.AbsLogPrinter;
import cn.thinkfree.database.event.account.AccountCreate;
import cn.thinkfree.database.event.account.ResetPassWord;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * 账号相关事件
 */
@Component
public class AccountListener extends AbsLogPrinter {


    /**
     * 账号创建后事件
     * 发送邮件
     * @param accountCreate
     */
    @TransactionalEventListener
    @Async
    public void accountCreateAfter(AccountCreate accountCreate){
        //TODO 发送邮件
        return;
    }

    /**
     * 重置密码之后
     * @param resetPassWord
     */
    @TransactionalEventListener
    @Async
    public void resetPassWordAfter(ResetPassWord resetPassWord){
        //TODO 发送邮件
        return;
    }

}
