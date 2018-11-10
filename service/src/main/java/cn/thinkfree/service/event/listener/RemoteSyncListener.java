package cn.thinkfree.service.event.listener;

import cn.thinkfree.core.event.AbsBaseEvent;
import cn.thinkfree.core.logger.AbsLogPrinter;
import cn.thinkfree.database.event.account.ResetPassWord;
import cn.thinkfree.database.event.sync.CompanyJoin;
import cn.thinkfree.database.event.sync.FinishContract;
import cn.thinkfree.database.model.PcCompanyFinancial;
import cn.thinkfree.database.vo.remote.SyncContractVO;
import cn.thinkfree.database.vo.remote.SyncTransactionVO;
import cn.thinkfree.service.company.CompanyInfoService;
import cn.thinkfree.service.contract.ContractService;
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

    @Autowired
    ContractService contractService;
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

    /**
     *  合同完成之后
     *  1.推送合同主体
     *  2.推送结算信息
     * @param finishContract
     */
//    @TransactionalEventListener
//    @Async
    @EventListener
    public void finishContractAfter(FinishContract finishContract){

        Optional<SyncContractVO> flag = contractService.selectSyncDateByContractNumber(finishContract.getSource());
        if(flag.isPresent()){
            cloudService.syncContract(flag.get());
        }
    }

    @EventListener
    public void system(ApplicationEvent applicationEvent){
        System.out.println(applicationEvent);
    }

}
