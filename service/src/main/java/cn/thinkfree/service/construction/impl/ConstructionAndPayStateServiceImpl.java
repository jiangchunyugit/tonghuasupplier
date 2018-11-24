package cn.thinkfree.service.construction.impl;

import cn.thinkfree.core.base.RespData;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ConstructionStateEnumB;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.mapper.ConstructionOrderMapper;
import cn.thinkfree.database.mapper.ConstructionOrderPayMapper;
import cn.thinkfree.database.mapper.ProjectBigSchedulingMapper;
import cn.thinkfree.database.model.*;
import cn.thinkfree.service.construction.CommonService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ConstructionAndPayStateServiceImpl {

    @Autowired
    CommonService commonService;
    @Autowired
    ConstructionOrderMapper constructionOrderMapper;
    @Autowired
    ConstructionOrderPayMapper constructionOrderPayMapper;
    @Autowired
    ProjectBigSchedulingMapper projectBigSchedulingMapper;

    /**
     *
     *  首款 支付
     *  开工报告
     *
     *  阶段1开工
     *  阶段1付款
     *     ~
     *  阶段2开工
     *  阶段2付款
     *
     *  竣工验收
     *  尾款支付
     *
     */


    /**
     * 是否可以支付首款
     * 财务服务-刘博
     */
    public MyRespBundle<Boolean> isFirstPay(String orderNo) {
        if (StringUtils.isBlank(orderNo)) {
            return RespData.error(ResultMessage.ERROR.code, "订单编号不能为空");
        }
        // 是否签约完成-可支付
        Integer stateCode = commonService.queryStateCodeByOrderNo(orderNo);
        Integer stage = ConstructionStateEnumB.STATE_550.getState();
        if (stateCode.equals(stage)) {
            return RespData.success(true);
        } else {
            return RespData.success(false);
        }
    }

    /**
     * 是否可以阶段款
     * 财务服务-刘博
     */
    public MyRespBundle<Boolean> isStagePay(String orderNo) {
        if (StringUtils.isBlank(orderNo)) {
            return RespData.error(ResultMessage.ERROR.code, "订单编号不能为空");
        }
        Integer stateCode = commonService.queryStateCodeByOrderNo(orderNo);
        Integer stage = ConstructionStateEnumB.STATE_620.getState();

        //TODO

        if (stateCode.equals(stage)) {
            return RespData.success(true);
        } else {
            return RespData.success(false);
        }
    }

    /**
     * 是否可以付尾款
     * 财务服务-刘博
     */
    public MyRespBundle<Boolean> isEndPay(String orderNo) {
        if (StringUtils.isBlank(orderNo)) {
            return RespData.error(ResultMessage.ERROR.code, "订单编号不能为空");
        }

        ConstructionOrderPayExample example = new ConstructionOrderPayExample();
        example.createCriteria().andOrderNoEqualTo(orderNo);
        List<ConstructionOrderPay> list =  constructionOrderPayMapper.selectByExample(example);
        if (list.isEmpty()){
            return RespData.success(false);
        }
        if ("C".equals(list.get(0).getIsEnd())){
            return RespData.success(true);
        }else {
            return RespData.success(false);
        }

    }

    /**
     * 开工报告
     * 内部服务-传让
     */
    public boolean isStartReport(String projectNo) {

        //通过projectNo查询orderNo
        ConstructionOrderExample example = new ConstructionOrderExample();
        example.createCriteria().andProjectNoEqualTo(projectNo);
        List<ConstructionOrder> constructionOrderList = constructionOrderMapper.selectByExample(example);
        if (constructionOrderList.isEmpty()) {
            return false;
        }
        String orderNo = constructionOrderList.get(0).getOrderNo();

        // 首款完成-可开工报告
        Integer stateCode = commonService.queryStateCodeByOrderNo(orderNo);
        Integer stage = ConstructionStateEnumB.STATE_600.getState();
        if (stateCode.equals(stage)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 阶段验收通过
     * 内部服务-传让
     */
    public boolean isStageComplete(String projectNo) {

        //通过projectNo查询orderNo
        ConstructionOrderExample example = new ConstructionOrderExample();
        example.createCriteria().andProjectNoEqualTo(projectNo);
        List<ConstructionOrder> constructionOrderList = constructionOrderMapper.selectByExample(example);
        if (constructionOrderList.isEmpty()) {
            return false;
        }
        String orderNo = constructionOrderList.get(0).getOrderNo();

        Integer stateCode = commonService.queryStateCodeByOrderNo(orderNo);
        Integer stage = ConstructionStateEnumB.STATE_630.getState();
        if (stateCode.equals(stage)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 竣工验收通过
     * 内部服务-传让
     */
    public boolean isBeComplete(String projectNo, String sort, String isEnd) {

        //通过projectNo查询orderNo
        ConstructionOrderExample example = new ConstructionOrderExample();
        example.createCriteria().andProjectNoEqualTo(projectNo);
        List<ConstructionOrder> constructionOrderList = constructionOrderMapper.selectByExample(example);
        if (constructionOrderList.isEmpty()) {
            return false;
        }

        //通过sort 查找feeName
        List<Integer> listNum = new ArrayList<>();
        ProjectBigSchedulingExample example1 = new ProjectBigSchedulingExample();
        example1.createCriteria().andSchemeNoEqualTo(constructionOrderList.get(0).getSchemeNo());
        //查询施工阶段 排序
        List<ProjectBigScheduling> schedulingList1 = projectBigSchedulingMapper.selectByExample(example1);
        for (ProjectBigScheduling projectBigScheduling : schedulingList1) {
            //查询是否竣工
            listNum.add(projectBigScheduling.getSort());
            Collections.sort(listNum);
        }

        //查询当前施工阶段
        ProjectBigSchedulingExample example2 = new ProjectBigSchedulingExample();
        example2.createCriteria().andSchemeNoEqualTo(constructionOrderList.get(0).getSchemeNo()).andSortEqualTo(Integer.parseInt(sort));
        List<ProjectBigScheduling> schedulingList2 = projectBigSchedulingMapper.selectByExample(example2);
        //开工报告 传值0
        if (schedulingList2.size() > 0) {
            for (ProjectBigScheduling projectBigScheduling : schedulingList2) {

                // A开工报告 B阶段验收 C竣工验收
                if (isEnd.equals("B")) {
                    if (listNum.get(listNum.size() - 1).equals(projectBigScheduling.getSort())) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        } else {
            return false;
        }
        return false;
    }

}
