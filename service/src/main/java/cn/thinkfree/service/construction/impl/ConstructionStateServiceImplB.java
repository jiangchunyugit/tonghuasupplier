package cn.thinkfree.service.construction.impl;


import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.base.RespData;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ConstructionStateEnumB;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.mapper.ConstructionOrderMapper;
import cn.thinkfree.database.model.ConstructionOrderExample;
import cn.thinkfree.service.construction.CommonService;
import cn.thinkfree.service.construction.ConstructionStateServiceB;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * 施工状态
 */
@Service
public class ConstructionStateServiceImplB implements ConstructionStateServiceB {

    @Autowired
    ConstructionOrderMapper constructionOrderMapper;
    @Autowired
    CommonService commonService;

    /**
     * 查询当前状态
     *
     * @param type  ，1获取平台状态，2获取装饰公司状态，3获取施工人员状态，4获取消费者状态
     * @return
     */
    @Override
    public MyRespBundle<String> getStateInfo(String orderNo,int type) {
        if (StringUtils.isBlank(orderNo)) {
            return RespData.error(ResultMessage.ERROR.code, "订单编号不能为空");
        }
        Integer stageCode = commonService.queryStateCodeByOrderNo(orderNo);
        String stateInfo = ConstructionStateEnumB.queryStateByRole(stageCode,type);
        return RespData.success(stateInfo);
    }

    /**
     * 运营平台
     * 派单给装饰公司
     */
    @Override
    public MyRespBundle<String> operateDispatchToConstruction(String orderNo) {
        if (StringUtils.isBlank(orderNo)) {
            return RespData.error(ResultMessage.ERROR.code, "订单编号不能为空");
        }
        Integer stageCode = commonService.queryStateCodeByOrderNo(orderNo);
        List<ConstructionStateEnumB> nextStateCode = ConstructionStateEnumB.STATE_510.getNextStates();
        if (ConstructionStateEnumB.STATE_510.getState() == stageCode) {
            if (commonService.updateStateCodeByOrderNo(orderNo, nextStateCode.get(0).getState())) {
                return RespData.success();
            }
        }
        return RespData.error(ResultMessage.ERROR.code, "该订单不符合派单状态");
    }


    /**
     * 装饰公司
     * 1派单给服务人员 2施工报价完成 3审核完成 4合同录入 5确认线下签约完成（自动创建工地项目）
     */
    @Override
    public MyRespBundle<String> constructionState(String orderNo, int type) {
        if (StringUtils.isBlank(orderNo)) {
            return RespData.error(ResultMessage.ERROR.code, "订单编号不能为空");
        }
        Integer stage = null;
        List<ConstructionStateEnumB> nextStateCode = new ArrayList<>();
        switch (type) {
            case 1:
                stage = ConstructionStateEnumB.STATE_520.getState();
                nextStateCode = ConstructionStateEnumB.STATE_520.getNextStates();
                break;
            case 2:
                stage = ConstructionStateEnumB.STATE_530.getState();
                nextStateCode = ConstructionStateEnumB.STATE_530.getNextStates();
                break;
            case 3:
                stage = ConstructionStateEnumB.STATE_540.getState();
                nextStateCode = ConstructionStateEnumB.STATE_540.getNextStates();
                break;
            case 4:
                stage = ConstructionStateEnumB.STATE_550.getState();
                nextStateCode = ConstructionStateEnumB.STATE_550.getNextStates();
                break;
            case 5:
                stage = ConstructionStateEnumB.STATE_560.getState();
                nextStateCode = ConstructionStateEnumB.STATE_560.getNextStates();
                break;
            default:
                break;
        }
        Integer stageCode = commonService.queryStateCodeByOrderNo(orderNo);
        if (stageCode.equals(stage)) {
            if (commonService.updateStateCodeByOrderNo(orderNo, nextStateCode.get(0).getState())) {
                return RespData.success();
            }
        }
        return RespData.error(ResultMessage.ERROR.code, "操作失败-请稍后重试");
    }

    /**
     * 支付
     * 1.首期款支付 2.开工报告 3.阶段验收通过 4.支付阶段款 5.等待尾款支付（验收通过）
     */
    @Override
    public MyRespBundle<String> customerPay(String orderNo, int type) {
        if (StringUtils.isBlank(orderNo)) {
            return RespData.error(ResultMessage.ERROR.code, "订单编号不能为空");
        }
        Integer stage = null;
        List<ConstructionStateEnumB> nextStateCode = new ArrayList<>();
        switch (type) {
            case 1:
                stage = ConstructionStateEnumB.STATE_600.getState();
                nextStateCode = ConstructionStateEnumB.STATE_600.getNextStates();
                break;
            case 2:
                stage = ConstructionStateEnumB.STATE_605.getState();
                nextStateCode = ConstructionStateEnumB.STATE_605.getNextStates();
                break;
            case 3:
                stage = ConstructionStateEnumB.STATE_610.getState();
                nextStateCode = ConstructionStateEnumB.STATE_610.getNextStates();
                break;
            case 4:
                stage = ConstructionStateEnumB.STATE_615.getState();
                nextStateCode = ConstructionStateEnumB.STATE_615.getNextStates();
                break;
            case 5:
                stage = ConstructionStateEnumB.STATE_690.getState();
                nextStateCode = ConstructionStateEnumB.STATE_690.getNextStates();
                break;
            default:
                break;
        }
        Integer stageCode = commonService.queryStateCodeByOrderNo(orderNo);
        if (stage == stageCode) {
            if (commonService.updateStateCodeByOrderNo(orderNo, nextStateCode.get(0).getState())) {
                return RespData.success();
            }
        }
        return RespData.error(ResultMessage.ERROR.code, "操作-请稍后重试");
    }

    /**
     * 订单完成
     * 支付尾款后
     */
    @Override
    public MyRespBundle<String> orderComplete(String orderNo) {
        if (StringUtils.isBlank(orderNo)) {
            return RespData.error(ResultMessage.ERROR.code, "订单编号不能为空");
        }
        Integer stageCode = commonService.queryStateCodeByOrderNo(orderNo);
        List<ConstructionStateEnumB> nextStateCode = ConstructionStateEnumB.STATE_700.getNextStates();
        if (ConstructionStateEnumB.STATE_700.getState() == stageCode) {
            if (commonService.updateStateCodeByOrderNo(orderNo, nextStateCode.get(0).getState())) {
                return RespData.success();
            }
        }
        return RespData.error(ResultMessage.ERROR.code, "操作失败-请稍后重试");
    }

    /**
     * 消费者
     * 取消订单
     * 签约阶段逆向
     */
    @Override
    public MyRespBundle<String> customerCancelOrder(String orderNo) {
        if (StringUtils.isBlank(orderNo)) {
            return RespData.error(ResultMessage.ERROR.code, "订单编号不能为空");
        }

        if (commonService.updateStateCodeByOrderNo(orderNo,  ConstructionStateEnumB.STATE_888.getState())) {
            return RespData.success();
        }

        return RespData.error(ResultMessage.ERROR.code, "操作失败-请稍后重试");
    }

    /**
     * 消费者
     * 取消订单
     * 支付未开工逆向
     */
    @Override
    public MyRespBundle<String> customerCancelOrderForPay(String orderNo) {
        if (StringUtils.isBlank(orderNo)) {
            return RespData.error(ResultMessage.ERROR.code, "订单编号不能为空");
        }

        if (commonService.updateStateCodeByOrderNo(orderNo,  ConstructionStateEnumB.STATE_888.getState())) {
            return RespData.success();
        }

        return RespData.error(ResultMessage.ERROR.code, "操作失败-请稍后重试");
    }

}
