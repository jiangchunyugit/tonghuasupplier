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
            case STATE_151:
            case STATE_152:
            case STATE_153:
            case STATE_154:
                return DesignStateEnum.STATE_150;
            case STATE_161:
            case STATE_162:
            case STATE_163:
            case STATE_164:
                return DesignStateEnum.STATE_160;
            case STATE_181:
            case STATE_182:
            case STATE_183:
            case STATE_184:
                return DesignStateEnum.STATE_180;
            case STATE_191:
            case STATE_192:
            case STATE_193:
            case STATE_194:
                return DesignStateEnum.STATE_190;
            case STATE_231:
            case STATE_232:
            case STATE_233:
            case STATE_234:
                return DesignStateEnum.STATE_230;
            case STATE_241:
            case STATE_242:
            case STATE_243:
            case STATE_244:
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
                return DesignStateEnum.STATE_151;
            case STATE_151:
                return DesignStateEnum.STATE_152;
            case STATE_152:
                return DesignStateEnum.STATE_153;
            case STATE_153:
                return DesignStateEnum.STATE_154;
            //首期款退款状态
            case STATE_160:
                return DesignStateEnum.STATE_161;
            case STATE_161:
                return DesignStateEnum.STATE_162;
            case STATE_162:
                return DesignStateEnum.STATE_163;
            case STATE_163:
                return DesignStateEnum.STATE_164;
            //中期款退款状态
            case STATE_180:
                return DesignStateEnum.STATE_181;
            case STATE_181:
                return DesignStateEnum.STATE_182;
            case STATE_182:
                return DesignStateEnum.STATE_183;
            case STATE_183:
                return DesignStateEnum.STATE_184;
            //中期款退款状态
            case STATE_190:
                return DesignStateEnum.STATE_191;
            case STATE_191:
                return DesignStateEnum.STATE_192;
            case STATE_192:
                return DesignStateEnum.STATE_193;
            case STATE_193:
                return DesignStateEnum.STATE_194;
            //全款退款状态
            case STATE_230:
                return DesignStateEnum.STATE_231;
            case STATE_231:
                return DesignStateEnum.STATE_232;
            case STATE_232:
                return DesignStateEnum.STATE_233;
            case STATE_233:
                return DesignStateEnum.STATE_234;
            //全款退款状态
            case STATE_240:
                return DesignStateEnum.STATE_241;
            case STATE_241:
                return DesignStateEnum.STATE_242;
            case STATE_242:
                return DesignStateEnum.STATE_243;
            case STATE_243:
                return DesignStateEnum.STATE_244;
            default:
                throw new RuntimeException("无效的订单状态");
        }
    }

}
