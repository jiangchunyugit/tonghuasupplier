package cn.thinkfree.service.event;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import cn.thinkfree.database.model.ContractInfo;

@Component
public class PublicEventListener {

	//异步  监听
	@EventListener
    //@Order(4)
	@Async
    public void onApplicationEvent1(AuditEvent event) {
		ContractInfo user = (ContractInfo) event.getSource();
        System.out.println("===> 审核事件:  {}"+user);
    }

}
