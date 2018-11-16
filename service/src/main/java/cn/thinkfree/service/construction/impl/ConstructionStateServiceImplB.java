package cn.thinkfree.service.construction.impl;


import cn.thinkfree.core.base.RespData;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ConstructionStateEnumB;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.mapper.ConstructionOrderMapper;
import cn.thinkfree.database.mapper.ConstructionOrderPayMapper;
import cn.thinkfree.database.mapper.ProjectBigSchedulingMapper;
import cn.thinkfree.database.mapper.ProjectMapper;
import cn.thinkfree.database.model.*;
import cn.thinkfree.service.construction.CommonService;
import cn.thinkfree.service.construction.ConstructionStateServiceB;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Max;
import java.util.ArrayList;
import java.util.Collections;
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
    @Autowired
    ProjectMapper projectMapper;
    @Autowired
    ConstructionOrderPayMapper constructionOrderPayMapper;
    @Autowired
    ProjectBigSchedulingMapper projectBigSchedulingMapper;

    /**
     * 查询当前状态
     *
     * @param type ，1获取平台状态，2获取装饰公司状态，3获取施工人员状态，4获取消费者状态
     * @return
     */
    @Override
    public MyRespBundle<String> getStateInfo(String orderNo, int type) {
        if (StringUtils.isBlank(orderNo)) {
            return RespData.error(ResultMessage.ERROR.code, "订单编号不能为空");
        }
        Integer stageCode = commonService.queryStateCodeByOrderNo(orderNo);
        String stateInfo = ConstructionStateEnumB.queryStateByRole(stageCode, type);
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
        List<ConstructionStateEnumB> nextStateCode = ConstructionStateEnumB.STATE_500.getNextStates();
        if (stageCode.equals(ConstructionStateEnumB.STATE_500.getState())) {
            if (commonService.updateStateCodeByOrderNo(orderNo, nextStateCode.get(0).getState())) {
                return RespData.success();
            }
        }
        return RespData.error(ResultMessage.ERROR.code, "该订单不符合派单状态");
    }


    /**
     * 装饰公司
     * 1派单给服务人员 2施工报价完成  4合同录入 5确认线下签约完成（自动创建工地项目）
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
                stage = ConstructionStateEnumB.STATE_510.getState();
                nextStateCode = ConstructionStateEnumB.STATE_510.getNextStates();
                break;
            case 2:
                stage = ConstructionStateEnumB.STATE_520.getState();
                nextStateCode = ConstructionStateEnumB.STATE_520.getNextStates();
                break;

            case 4:
                stage = ConstructionStateEnumB.STATE_540.getState();
                nextStateCode = ConstructionStateEnumB.STATE_540.getNextStates();
                break;
            case 5:
                stage = ConstructionStateEnumB.STATE_550.getState();
                nextStateCode = ConstructionStateEnumB.STATE_550.getNextStates();
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
     * 装饰公司
     * 3审核完成 （审核是否通过）
     */
    @Override
    public MyRespBundle<String> constructionStateOfExamine(String orderNo, int type, int isPass) {

        if (StringUtils.isBlank(String.valueOf(isPass))) {
            return RespData.error(ResultMessage.ERROR.code, "审核是否通过状态未知");
        }


        Integer stage = ConstructionStateEnumB.STATE_530.getState();
        Integer stageCode = commonService.queryStateCodeByOrderNo(orderNo);

        List<ConstructionStateEnumB> nextStateCode = ConstructionStateEnumB.STATE_530.getNextStates();

        if (stageCode.equals(stage)) {
            if (isPass == 1) {   //下一步
                if (commonService.updateStateCodeByOrderNo(orderNo, nextStateCode.get(0).getState())) {
                    return RespData.success();
                }
            } else {  //上一步
                if (commonService.updateStateCodeByOrderNo(orderNo, nextStateCode.get(1).getState())) {
                    return RespData.success();
                }
            }
        } else {
            return RespData.error(ResultMessage.ERROR.code, "该订单不符合审核状态");
        }

        return RespData.error(ResultMessage.ERROR.code, "状态更新失败状态-请稍后重试");
    }


    /**
     * 支付
     */
    @Override
    public MyRespBundle<String> customerPay(String orderNo, String feeName, String sort, String isEnd) {
        if (StringUtils.isBlank(orderNo)) {
            return RespData.error(ResultMessage.ERROR.code, "订单编号不能为空");
        }

        ConstructionOrderPayExample example = new ConstructionOrderPayExample();
        example.createCriteria().andOrderNoEqualTo(orderNo);
        List<ConstructionOrderPay> list = constructionOrderPayMapper.selectByExample(example);
        if (list.size() > 0) {
            ConstructionOrderPay constructionOrderPay = new ConstructionOrderPay();
            constructionOrderPay.setFeeName(feeName);
            constructionOrderPay.setSort(sort);
            constructionOrderPay.setIsEnd(isEnd);
            constructionOrderPayMapper.updateByExampleSelective(constructionOrderPay, example);
        } else {
            ConstructionOrderPay constructionOrderPay = new ConstructionOrderPay();
            constructionOrderPay.setOrderNo(orderNo);
            constructionOrderPay.setFeeName(feeName);
            constructionOrderPay.setSort(sort);
            constructionOrderPay.setIsEnd(isEnd);
            constructionOrderPayMapper.insertSelective(constructionOrderPay);
        }

        return RespData.error(ResultMessage.ERROR.code, "操作-请稍后重试");
    }

    /**
     * 施工阶段方案
     */
    @Override
    public MyRespBundle<String> constructionPlan(String projectNo, String sort, String isEnd) {

        if (StringUtils.isBlank(projectNo)) {
            return RespData.error(ResultMessage.ERROR.code, "项目编号不能为空");
        }

        ConstructionOrderPay constructionOrderPay = new ConstructionOrderPay();

        //通过projectNo查询orderNo
        ConstructionOrderExample example = new ConstructionOrderExample();
        example.createCriteria().andProjectNoEqualTo(projectNo);
        List<ConstructionOrder> constructionOrderList = constructionOrderMapper.selectByExample(example);
        for (ConstructionOrder constructionOrder : constructionOrderList) {

            constructionOrderPay.setOrderNo(constructionOrder.getOrderNo());

            //通过sort 查找feeName
            List<Integer> listNum = new ArrayList<>();
            ProjectBigSchedulingExample example1 = new ProjectBigSchedulingExample();
            example1.createCriteria().andSchemeNoEqualTo(constructionOrder.getSchemeNo());
            //查询施工阶段 排序
            List<ProjectBigScheduling> schedulingList1 = projectBigSchedulingMapper.selectByExample(example1);
            for (ProjectBigScheduling projectBigScheduling : schedulingList1) {
                //查询是否竣工
                listNum.add(projectBigScheduling.getSort());
                Collections.sort(listNum);

            }

            //查询当前施工阶段
            example1.createCriteria().andSortEqualTo(Integer.parseInt(sort));
            List<ProjectBigScheduling> schedulingList2 = projectBigSchedulingMapper.selectByExample(example1);
            for (ProjectBigScheduling projectBigScheduling : schedulingList2) {

                constructionOrderPay.setFeeName(projectBigScheduling.getName());

                if (isEnd.equals("B")){
                    if (listNum.get(listNum.size() - 1).equals(projectBigScheduling.getSort())){
                        constructionOrderPay.setIsEnd("C");
                    }else {
                        constructionOrderPay.setIsEnd("B");
                    }
                }else {
                    constructionOrderPay.setIsEnd("A");
                }

            }

            // 入库
            ConstructionOrderPayExample exampleC = new ConstructionOrderPayExample();
            exampleC.createCriteria().andOrderNoEqualTo(constructionOrder.getOrderNo());
            List<ConstructionOrderPay> list = constructionOrderPayMapper.selectByExample(exampleC);
            if (list.size() > 0) {
                constructionOrderPay.setSort(sort);
                constructionOrderPayMapper.updateByExampleSelective(constructionOrderPay, exampleC);
            } else {
                constructionOrderPay.setSort(sort);
                constructionOrderPayMapper.insertSelective(constructionOrderPay);
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
    public MyRespBundle<String> customerCancelOrder(String userId, String orderNo, String cancelReason) {
        if (StringUtils.isBlank(orderNo)) {
            return RespData.error(ResultMessage.ERROR.code, "订单编号不能为空");
        }

        if (StringUtils.isBlank(userId)) {
            return RespData.error(ResultMessage.ERROR.code, "用户ID不能为空");
        }

        ProjectExample example = new ProjectExample();
        example.createCriteria().andOwnerIdEqualTo(userId).andStatusEqualTo(1);
        List<Project> list = projectMapper.selectByExample(example);
        if (list.size() <= 0) {
            return RespData.error(ResultMessage.ERROR.code, "用户不存在");
        }

        Integer stageCode = commonService.queryStateCodeByOrderNo(orderNo);
        if (ConstructionStateEnumB.STATE_600.getState() <= stageCode) {
            return RespData.error(ResultMessage.ERROR.code, "当前状态不能取消订单");
        }

        ConstructionOrderExample example2 = new ConstructionOrderExample();
        example2.createCriteria().andOrderNoEqualTo(orderNo);
        ConstructionOrder constructionOrder = new ConstructionOrder();
        constructionOrder.setOrderStage(ConstructionStateEnumB.STATE_888.getState());
        constructionOrder.setRemark(cancelReason);
        int isUpdate = constructionOrderMapper.updateByExampleSelective(constructionOrder, example2);
        if (isUpdate == 1) {
            return RespData.success();
        } else {
            return RespData.error(ResultMessage.ERROR.code, "取消订单失败-请稍后重试");
        }
    }

    /**
     * 消费者
     * 取消订单
     * 签约阶段逆向
     * 查看状态
     */
    @Override
    public Boolean customerCancelOrderState(String userId, String orderNo) {
        if (StringUtils.isBlank(orderNo)) {
            return false;
        }

        if (StringUtils.isBlank(userId)) {
            return false;
        }

        ProjectExample example = new ProjectExample();
        example.createCriteria().andOwnerIdEqualTo(userId).andStatusEqualTo(1);
        List<Project> list = projectMapper.selectByExample(example);
        if (list.size() <= 0) {
            return false;
        }

        Integer stageCode = commonService.queryStateCodeByOrderNo(orderNo);
        if (ConstructionStateEnumB.STATE_600.getState() <= stageCode) {
            return false;
        }
        return true;
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

        if (commonService.updateStateCodeByOrderNo(orderNo, ConstructionStateEnumB.STATE_888.getState())) {
            return RespData.success();
        }

        return RespData.error(ResultMessage.ERROR.code, "操作失败-请稍后重试");
    }

}
