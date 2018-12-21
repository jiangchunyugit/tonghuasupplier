package cn.thinkfree.service.rebate.impl;

import cn.thinkfree.database.mapper.DesignerOrderMapper;
import cn.thinkfree.database.mapper.FundsSettleAccountsNodeLogMapper;
import cn.thinkfree.database.model.ConstructionOrder;
import cn.thinkfree.database.model.DesignerOrder;
import cn.thinkfree.database.model.FundsSettleAccountsNodeLog;
import cn.thinkfree.database.model.SchemeScheduleRebateNode;
import cn.thinkfree.service.construction.ConstructOrderService;
import cn.thinkfree.service.designer.service.DesignerOrderService;
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
    @Autowired
    private ConstructOrderService constructOrderService;
    @Autowired
    private DesignerOrderService designerOrderService;


    @Override
    public void create(String projectNo, Integer scheduleSort) {

        ConstructionOrder constructionOrder = constructOrderService.findByProjectNo(projectNo);
        DesignerOrder designerOrder = designerOrderService.findByProjectNo(projectNo);

        SchemeScheduleRebateNode schemeScheduleRebateNode = schemeScheduleRebateNodeService.findBySchemeNoAndScheduleSort(constructionOrder.getSchemeNo(), scheduleSort);
        if (schemeScheduleRebateNode != null) {
            FundsSettleAccountsNodeLog fundsSettleAccountsNodeLog = new FundsSettleAccountsNodeLog();

            fundsSettleAccountsNodeLog.setNodeNo(schemeScheduleRebateNode.getRebateNodeId().toString());

            String date = DateUtil.formartDate(new Date(), "yyyy-MM-dd");
            fundsSettleAccountsNodeLog.setCompletionDate(date);

            fundsSettleAccountsNodeLog.setCompanyId(constructionOrder.getCompanyId());
            fundsSettleAccountsNodeLog.setDesignerCompanyId(designerOrder.getCompanyId());
            fundsSettleAccountsNodeLog.setProjectNo(projectNo);
            fundsSettleAccountsNodeLog.setOrderNo(constructionOrder.getOrderNo());

            insert(fundsSettleAccountsNodeLog);
        }
    }

    private void insert(FundsSettleAccountsNodeLog fundsSettleAccountsNodeLog) {
        fundsSettleAccountsNodeLogMapper.insertSelective(fundsSettleAccountsNodeLog);
    }
}
