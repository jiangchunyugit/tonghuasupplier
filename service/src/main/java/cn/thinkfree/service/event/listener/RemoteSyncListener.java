package cn.thinkfree.service.event.listener;

import cn.thinkfree.core.event.AbsBaseEvent;
import cn.thinkfree.core.logger.AbsLogPrinter;
import cn.thinkfree.database.event.account.ResetPassWord;
import cn.thinkfree.database.event.sync.CompanyJoin;
import cn.thinkfree.database.vo.remote.SyncTransactionVO;
import cn.thinkfree.service.company.CompanyInfoService;
import cn.thinkfree.service.remote.CloudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Optional;

/**
 * 远程端同步数据监听器
 */
@Component
public class RemoteSyncListener extends AbsLogPrinter {

    @Autowired
    CloudService cloudService;

    @Autowired
    CompanyInfoService companyInfoService;
    /**
     * 公司资质确认后
     * @param companyJoin
     */
//    @TransactionalEventListener
//    @Async
    @EventListener
    public void companyJoinAfter(CompanyJoin companyJoin){

        String companyID = companyJoin.getSource();
        Optional<SyncTransactionVO> flag = companyInfoService.selectSyncDateByCompanyID(companyID);
        if(flag.isPresent()){
            cloudService.syncTransaction(flag.get());
        }


    }

//    @EventListener
//    public void test(AbsBaseEvent event){
//        System.out.println("bug");
//    }

    @EventListener
    public void feiqi(ApplicationEvent applicationEvent){
        System.out.println(applicationEvent);
    }

}
