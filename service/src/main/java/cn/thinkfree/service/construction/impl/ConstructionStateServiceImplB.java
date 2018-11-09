package cn.thinkfree.service.construction.impl;



import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.base.RespData;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ConstructionStateEnumB;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.mapper.ConstructionOrderMapper;
import cn.thinkfree.database.model.ConstructionOrderExample;
import cn.thinkfree.service.construction.CommonService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 施工状态
 */
@Service
public class ConstructionStateServiceImplB {

    @Autowired
    ConstructionOrderMapper constructionOrderMapper;
    @Autowired
    CommonService commonService;

    /**
     * 运营平台
     *  派单给装饰公司
     */
    public MyRespBundle<String> operateDispatchToConstruction(String orderNo) {
        if (StringUtils.isBlank(orderNo)) {
            return RespData.error(ResultMessage.ERROR.code, "订单编号不能为空");
        }
        Integer stageCode = commonService.queryStateCodeByOrderNo(orderNo);
        List<ConstructionStateEnumB> nextStateCode = ConstructionStateEnumB.STATE_510.getNextStates();
        if (ConstructionStateEnumB.STATE_510.getState() == stageCode){
            if (commonService.updateStateCodeByOrderNo(orderNo,nextStateCode.get(0).getState())){
                return RespData.success();
            }
        }
        return RespData.error(ResultMessage.ERROR.code, "派单失败-请稍后重试");
    }


    /**
     * 装饰公司
     *  1派单给服务人员 2施工报价完成 3审核完成 4
     */
//    public MyRespBundle<String> operateDispatchToConstruction(String orderNo) {
//        if (StringUtils.isBlank(orderNo)) {
//            return RespData.error(ResultMessage.ERROR.code, "订单编号不能为空");
//        }
//        Integer stageCode = commonService.queryStateCodeByOrderNo(orderNo);
//        List<ConstructionStateEnumB> nextStateCode = ConstructionStateEnumB.STATE_510.getNextStates();
//        if (ConstructionStateEnumB.STATE_510.getState() == stageCode){
//            if (commonService.updateStateCodeByOrderNo(orderNo,nextStateCode.get(0).getState())){
//                return RespData.success();
//            }
//        }
//        return RespData.error(ResultMessage.ERROR.code, "派单失败-请稍后重试");
//    }



}
