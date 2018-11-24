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
import cn.thinkfree.service.construction.ConstructionAndPayStateService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ConstructionAndPayStateServiceImpl implements ConstructionAndPayStateService {

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
    @Override
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
    @Override
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
     * 竣工验收通过
     * 内部服务-传让
     */
    @Override
    public boolean isBeComplete(String projectNo, Integer sort) {

        //通过projectNo查询orderNo
        ConstructionOrderExample example = new ConstructionOrderExample();
        example.createCriteria().andProjectNoEqualTo(projectNo);
        List<ConstructionOrder> constructionOrderList = constructionOrderMapper.selectByExample(example);
        if (constructionOrderList.isEmpty()) {
            return false;
        }
        String orderNo = constructionOrderList.get(0).getOrderNo();

        /* sort==0 开工报告 */
        if ("0".equals(sort)){
            Integer stateCode = commonService.queryStateCodeByOrderNo(orderNo);
            Integer stage = ConstructionStateEnumB.STATE_600.getState();
            if (stateCode.equals(stage)) {
                return true;
            } else {
                return false;
            }
        }
        else {
            sort +=1;
            ConstructionOrderPayExample example1 = new ConstructionOrderPayExample();
            example1.createCriteria().andSortEqualTo(sort.shortValue()).andIsEndEqualTo("pay");
            List<ConstructionOrderPay> list = constructionOrderPayMapper.selectByExample(example1);
            if (list.isEmpty()) {
                return false;
            }else {
                return true;
            }
        }

    }

}
