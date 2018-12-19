package cn.thinkfree.service.rebate.impl;

import cn.thinkfree.database.mapper.FundsSettleAccountsNodeLogMapper;
import cn.thinkfree.database.model.FundsSettleAccountsNodeLog;
import cn.thinkfree.database.model.SchemeScheduleRebateNode;
import cn.thinkfree.service.rebate.FundsSettleAccountsNodeLogService;
import cn.thinkfree.service.rebate.SchemeScheduleRebateNodeService;
import cn.thinkfree.service.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 返款节点日志
 *
 * @author song
 * @version 1.0
 * @date 2018/12/19 15:46
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class FundsSettleAccountsNodeLogServiceImpl implements FundsSettleAccountsNodeLogService {

    @Autowired
    private FundsSettleAccountsNodeLogMapper fundsSettleAccountsNodeLogMapper;
    @Autowired
    private SchemeScheduleRebateNodeService schemeScheduleRebateNodeService;


    @Override
    public void create(String schemeNo, String companyId, String projectNo, String orderNo, Integer scheduleSort) {
        SchemeScheduleRebateNode schemeScheduleRebateNode = schemeScheduleRebateNodeService.findBySchemeNoAndScheduleSort(schemeNo, scheduleSort);
        if (schemeScheduleRebateNode != null) {
            FundsSettleAccountsNodeLog fundsSettleAccountsNodeLog = new FundsSettleAccountsNodeLog();

            fundsSettleAccountsNodeLog.setNodeNo(schemeScheduleRebateNode.getRebateNodeId().toString());

            String date = DateUtil.formartDate(new Date(), "yyyy-MM-dd");
            fundsSettleAccountsNodeLog.setCompletionDate(date);

            fundsSettleAccountsNodeLog.setCompanyId(companyId);
            fundsSettleAccountsNodeLog.setProjectNo(projectNo);
            fundsSettleAccountsNodeLog.setOrderNo(orderNo);

            insert(fundsSettleAccountsNodeLog);
        }
    }

    private void insert(FundsSettleAccountsNodeLog fundsSettleAccountsNodeLog) {
        fundsSettleAccountsNodeLogMapper.insertSelective(fundsSettleAccountsNodeLog);
    }
}
