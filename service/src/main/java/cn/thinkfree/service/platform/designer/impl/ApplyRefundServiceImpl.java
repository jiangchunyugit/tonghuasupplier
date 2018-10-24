package cn.thinkfree.service.platform.designer.impl;

import cn.thinkfree.core.constants.DesignStateEnum;
import cn.thinkfree.database.mapper.ApplyRefundLogMapper;
import cn.thinkfree.database.mapper.ApplyRefundOptionLogMapper;
import cn.thinkfree.database.model.*;
import cn.thinkfree.service.platform.designer.ApplyRefundService;
import cn.thinkfree.service.platform.designer.DesignDispatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author xusonghui
 * 申请退款服务实现
 */
@Service
public class ApplyRefundServiceImpl implements ApplyRefundService {

    @Autowired
    private DesignDispatchService designDispatchService;
    @Autowired
    private ApplyRefundLogMapper applyRefundLogMapper;
    @Autowired
    private ApplyRefundOptionLogMapper optionLogMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void applyRefund(String orderNo, String userId, String reason, String money, String title, String flowNumber) {
        DesignOrder designOrder = designDispatchService.queryDesignOrderByOrderNo(orderNo);
        Project project = designDispatchService.queryProjectByNo(designOrder.getProjectNo());
        if (userId == null || !userId.equals(project.getOwnerId())) {
            throw new RuntimeException("无权操作");
        }
        DesignStateEnum stateEnum = getRefundNextState(designOrder);
        designDispatchService.updateOrderState(designOrder.getProjectNo(), stateEnum.getState(), project.getOwnerId(), "业主");
        // TODO 记录退款申请
        ApplyRefundLog refundLog = new ApplyRefundLog();
        refundLog.setApplyTime(new Date());
        refundLog.setCompanyId(designOrder.getCompanyId());
        refundLog.setFlownumber(flowNumber);
        refundLog.setMoney(money);
        refundLog.setReason(reason);
        refundLog.setRefundNo(UUID.randomUUID().toString().replaceAll("-", ""));
        refundLog.setTitle(title);
        refundLog.setOrderNo(orderNo);
        refundLog.setOrderType(1);
        applyRefundLogMapper.insertSelective(refundLog);
        saveRefundOptionLog(project.getOwnerId(), refundLog.getRefundNo(), "业主发起了退款申请");
    }

    @Override
    public void companyRefundAgree(String refundNo, String optionId, String optionName) {
        refundAgree(refundNo, optionId, optionName, "设计公司同意了退款申请");
    }

    @Override
    public void companyRefundReject(String refundNo, String optionId, String reason, String optionName) {
        reason = "设计公司驳回了退款申请：" + reason;
        refundReject(refundNo, optionId, reason, optionName);
    }

    @Override
    public void platformRefundAgree(String refundNo, String optionId, String optionName) {
        refundAgree(refundNo, optionId, optionName, "平台同意了退款申请");
    }

    @Override
    public void platformRefundReject(String refundNo, String optionId, String reason, String optionName) {
        reason = "平台驳回了退款申请：" + reason;
        refundReject(refundNo, optionId, reason, optionName);
    }

    @Override
    public void financeRefundAgree(String refundNo, String optionId, String optionName) {
        refundAgree(refundNo, optionId, optionName, "财务同意了退款申请");
    }

    /**
     * 驳回退款申请
     *
     * @param refundNo   退款申请业务编号
     * @param optionId   操作人ID
     * @param reason     驳回原因
     * @param optionName 操作人姓名
     */
    private void refundReject(String refundNo, String optionId, String reason, String optionName) {
        ApplyRefundLog applyRefundLog = checkRefundApply(refundNo);
        saveRefundOptionLog(optionId, refundNo, reason);
        DesignOrder designOrder = designDispatchService.queryDesignOrderByOrderNo(applyRefundLog.getOrderNo());
        DesignStateEnum stateEnum = getRejectState(designOrder);
        designDispatchService.updateOrderState(designOrder.getProjectNo(), stateEnum.getState(), optionId, optionName, reason);
    }

    /**
     * 同意退款申请
     *
     * @param refundNo   退款申请业务编号
     * @param optionId   操作人ID
     * @param optionName 操作人姓名
     * @param reason     描述
     */
    private void refundAgree(String refundNo, String optionId, String optionName, String reason) {
        ApplyRefundLog applyRefundLog = checkRefundApply(refundNo);
        saveRefundOptionLog(optionId, refundNo, reason);
        DesignOrder designOrder = designDispatchService.queryDesignOrderByOrderNo(applyRefundLog.getOrderNo());
        DesignStateEnum stateEnum = getRefundNextState(designOrder);
        designDispatchService.updateOrderState(designOrder.getProjectNo(), stateEnum.getState(), optionId, optionName);
    }

    /**
     * 检查退款申请记录是否存在
     *
     * @param refundNo 退款申请业务编号
     */
    private ApplyRefundLog checkRefundApply(String refundNo) {
        ApplyRefundLogExample logExample = new ApplyRefundLogExample();
        ApplyRefundLogExample.Criteria criteria = logExample.createCriteria();
        criteria.andRefundNoEqualTo(refundNo).andOrderTypeEqualTo(1);
        List<ApplyRefundLog> refundLogs = applyRefundLogMapper.selectByExample(logExample);
        if (refundLogs.isEmpty()) {
            throw new RuntimeException("无效的数据");
        }
        return refundLogs.get(0);
    }

    private void saveRefundOptionLog(String optionId, String refundNo, String reason) {
        ApplyRefundOptionLog optionLog = new ApplyRefundOptionLog();
        optionLog.setOptionId(optionId);
        optionLog.setOptionTime(new Date());
        optionLog.setRefundNo(refundNo);
        optionLog.setRemark(reason);
        optionLogMapper.insertSelective(optionLog);
    }

    /**
     * 根据订单状态查询退款回退状态
     *
     * @param designOrder 订单信息
     * @return
     */
    private DesignStateEnum getRejectState(DesignOrder designOrder) {
        int orderState = designOrder.getOrderStage();
        DesignStateEnum stateEnum = DesignStateEnum.queryByState(orderState);
        switch (stateEnum) {
            case STATE_90:
            case STATE_100:
            case STATE_110:
            case STATE_120:
                return DesignStateEnum.STATE_50;
            case STATE_1501:
            case STATE_1502:
            case STATE_1503:
            case STATE_1504:
                return DesignStateEnum.STATE_150;
            case STATE_1601:
            case STATE_1602:
            case STATE_1603:
            case STATE_1604:
                return DesignStateEnum.STATE_160;
            case STATE_1801:
            case STATE_1802:
            case STATE_1803:
            case STATE_1804:
                return DesignStateEnum.STATE_180;
            case STATE_1901:
            case STATE_1902:
            case STATE_1903:
            case STATE_1904:
                return DesignStateEnum.STATE_190;
            case STATE_2301:
            case STATE_2302:
            case STATE_2303:
            case STATE_2304:
                return DesignStateEnum.STATE_230;
            case STATE_2401:
            case STATE_2402:
            case STATE_2403:
            case STATE_2404:
                return DesignStateEnum.STATE_240;
            default:
                throw new RuntimeException("无效的订单状态");
        }
    }

    /**
     * 根据当前订单，查询退款状态
     *
     * @param designOrder
     * @return
     */
    private DesignStateEnum getRefundNextState(DesignOrder designOrder) {
        int orderState = designOrder.getOrderStage();
        DesignStateEnum stateEnum = DesignStateEnum.queryByState(orderState);
        switch (stateEnum) {
            //量房退款状态
            case STATE_50:
                return DesignStateEnum.STATE_90;
            case STATE_90:
                return DesignStateEnum.STATE_100;
            case STATE_100:
                return DesignStateEnum.STATE_110;
            case STATE_110:
                return DesignStateEnum.STATE_120;
            //首期款退款状态
            case STATE_150:
                return DesignStateEnum.STATE_1501;
            case STATE_1501:
                return DesignStateEnum.STATE_1502;
            case STATE_1502:
                return DesignStateEnum.STATE_1503;
            case STATE_1503:
                return DesignStateEnum.STATE_1504;
            //首期款退款状态
            case STATE_160:
                return DesignStateEnum.STATE_1601;
            case STATE_1601:
                return DesignStateEnum.STATE_1602;
            case STATE_1602:
                return DesignStateEnum.STATE_1603;
            case STATE_1603:
                return DesignStateEnum.STATE_1604;
            //中期款退款状态
            case STATE_180:
                return DesignStateEnum.STATE_1801;
            case STATE_1801:
                return DesignStateEnum.STATE_1802;
            case STATE_1802:
                return DesignStateEnum.STATE_1803;
            case STATE_1803:
                return DesignStateEnum.STATE_1804;
            //中期款退款状态
            case STATE_190:
                return DesignStateEnum.STATE_1901;
            case STATE_1901:
                return DesignStateEnum.STATE_1902;
            case STATE_1902:
                return DesignStateEnum.STATE_1903;
            case STATE_1903:
                return DesignStateEnum.STATE_1904;
            //全款退款状态
            case STATE_230:
                return DesignStateEnum.STATE_2301;
            case STATE_2301:
                return DesignStateEnum.STATE_2302;
            case STATE_2302:
                return DesignStateEnum.STATE_2303;
            case STATE_2303:
                return DesignStateEnum.STATE_2304;
            //全款退款状态
            case STATE_240:
                return DesignStateEnum.STATE_2401;
            case STATE_2401:
                return DesignStateEnum.STATE_2402;
            case STATE_2402:
                return DesignStateEnum.STATE_2403;
            case STATE_2403:
                return DesignStateEnum.STATE_2404;
            default:
                throw new RuntimeException("无效的订单状态");
        }
    }

}
