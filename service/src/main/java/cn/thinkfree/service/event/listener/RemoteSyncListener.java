package cn.thinkfree.service.event.listener;

import cn.thinkfree.core.logger.AbsLogPrinter;
import cn.thinkfree.database.event.account.ResetPassWord;
import cn.thinkfree.database.event.sync.CompanyJoin;
import cn.thinkfree.database.vo.remote.SyncTransactionVO;
import cn.thinkfree.service.company.CompanyInfoService;
import cn.thinkfree.service.remote.CloudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Optional;

/**
 * 远程端同步数据监听器
 */
public class RemoteSyncListener extends AbsLogPrinter {

    @Autowired
    CloudService cloudService;

    @Autowired
    CompanyInfoService companyInfoService;
    /**
     * 公司资质确认后
     * @param companyJoin
     */
    @TransactionalEventListener
    @Async
    public void companyJoinAfter(CompanyJoin companyJoin){

        String companyID = companyJoin.getSource();
        Optional<SyncTransactionVO> flag = companyInfoService.selectSyncDateByCompanyID(companyID);
        if(flag.isPresent()){
            cloudService.syncTransaction(flag.get());
        }


    }

}
