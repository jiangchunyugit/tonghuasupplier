package cn.thinkfree.service.construction.impl;

import cn.thinkfree.core.base.RespData;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ConstructionStateEnum;
import cn.thinkfree.database.mapper.ConstructionOrderMapper;
import cn.thinkfree.database.model.ConstructionOrder;
import cn.thinkfree.database.model.ConstructionOrderExample;
import cn.thinkfree.service.construction.CommonService;
import cn.thinkfree.service.construction.ConstructionOrderOperate;
import cn.thinkfree.service.construction.OrderListCommonService;
import cn.thinkfree.service.construction.vo.ConstructionOrderListVo;
import cn.thinkfree.service.construction.vo.ConstructionOrderManageVo;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class ConstructionOrderOperateImpl implements ConstructionOrderOperate {

    @Autowired
    private OrderListCommonService orderListCommonService;
    @Autowired
    private ConstructionOrderMapper constructionOrderMapper;
    @Autowired
    private CommonService commonService;

    /**
     * 订单列表
     *
     * @param pageNum
     * @param pageSize
     * @param cityName
     * @param orderType
     * @return
     */
    @Override
    public PageInfo<ConstructionOrderListVo> getOrderList(int pageNum, int pageSize, String cityName, int orderType) {
        PageInfo<ConstructionOrderListVo> pageInfo = orderListCommonService.getConstructionOrderList(pageNum, pageSize, cityName, orderType);
        return pageInfo;
    }

    /**
     * 施工订单列表统计
     * @return
     */

    @Override
    public MyRespBundle<ConstructionOrderManageVo> getOrderNum() {

        ConstructionOrderExample example = new ConstructionOrderExample();
        List<ConstructionOrder> list = constructionOrderMapper.selectByExample(example);

        /* 统计状态个数 */
        int waitExamine = 0, waitSign = 0, waitPay = 0;
        for (ConstructionOrder constructionOrder : list) {
            // 订单状态 统计
            int stage = constructionOrder.getOrderStage();
            if (stage == ConstructionStateEnum.STATE_520.getState()) {
                waitExamine++;
            }
            if (stage == ConstructionStateEnum.STATE_540.getState()) {
                waitSign++;
            }
            if ((stage >= ConstructionStateEnum.STATE_600.getState() && stage <= ConstructionStateEnum.STATE_690.getState())) {
                waitPay++;
            }
        }

        ConstructionOrderManageVo constructionOrderManageVo = new ConstructionOrderManageVo();
        constructionOrderManageVo.setCityList(commonService.getCityList());
        constructionOrderManageVo.setOrderNum(list.size());
        constructionOrderManageVo.setWaitExamine(waitExamine);
        constructionOrderManageVo.setWaitSign(waitSign);
        constructionOrderManageVo.setWaitPay(waitPay);
        return RespData.success(constructionOrderManageVo);
    }
}