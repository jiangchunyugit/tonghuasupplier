package cn.thinkfree.service.event.listener;

import cn.thinkfree.core.logger.AbsLogPrinter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class AccountListener extends AbsLogPrinter {

    @TransactionalEventListener
    @Async
    public void accountCreate(){
        return;
    }

}
