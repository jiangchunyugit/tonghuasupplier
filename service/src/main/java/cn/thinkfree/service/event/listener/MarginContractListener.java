package cn.thinkfree.service.event.listener;

import cn.thinkfree.core.logger.AbsLogPrinter;
import cn.thinkfree.database.event.MarginContractEvent;
import cn.thinkfree.database.event.sync.CompanyJoin;
import cn.thinkfree.database.vo.MarginContractVO;
import cn.thinkfree.database.vo.remote.SyncTransactionVO;
import cn.thinkfree.service.company.CompanyInfoService;
import cn.thinkfree.service.pcthirdpartdate.ThirdPartDateService;
import cn.thinkfree.service.remote.CloudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author jiangchunyu(后台)
 * @date 20181109
 * @Description远程端同步数据监听器
 */
@Component
public class MarginContractListener extends AbsLogPrinter {

    @Autowired
    CloudService cloudService;

    @Autowired
    ThirdPartDateService thirdPartDateService;
    /**
     * 同步合同数据
     */
    @EventListener
    public void marginContract(MarginContractEvent marginContractEvent){

        String contractID = marginContractEvent.getSource();
        MarginContractVO flag = thirdPartDateService.getMarginContract(contractID);
        if(flag != null){
            cloudService.marginContractTransaction(flag);
        }

    }
}
