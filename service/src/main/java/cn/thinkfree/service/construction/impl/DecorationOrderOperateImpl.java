package cn.thinkfree.service.construction.impl;

import cn.thinkfree.core.base.RespData;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ConstructionStateEnum;
import cn.thinkfree.database.mapper.ConstructionOrderMapper;
import cn.thinkfree.database.model.ConstructionOrder;
import cn.thinkfree.database.model.ConstructionOrderExample;
import cn.thinkfree.service.construction.CommonService;
import cn.thinkfree.service.construction.DecorationOrderOperate;
import cn.thinkfree.service.construction.OrderListCommonService;
import cn.thinkfree.service.construction.vo.ConstructionOrderCommonVo;
import cn.thinkfree.service.construction.vo.ConstructionOrderListVo;
import cn.thinkfree.service.construction.vo.ConstructionOrderManageVo;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class DecorationOrderOperateImpl implements DecorationOrderOperate {

    @Autowired
    OrderListCommonService orderListCommonService;
    @Autowired
    ConstructionOrderMapper constructionOrderMapper;
    @Autowired
    CommonService commonService;

    /**
     * 订单列表
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public MyRespBundle<ConstructionOrderCommonVo> getDecorationOrderList(String companyNo,int pageNum, int pageSize) {
        PageInfo<ConstructionOrderListVo> pageInfo = orderListCommonService.getDecorateOrderList(companyNo,pageNum,pageSize);
        ConstructionOrderCommonVo constructionOrderCommonVo = new ConstructionOrderCommonVo();
        constructionOrderCommonVo.setCountPageNum((int) pageInfo.getTotal());
        constructionOrderCommonVo.setOrderList(pageInfo.getList());
        return RespData.success(constructionOrderCommonVo);
    }

    /**
     * 施工订单列表统计
     * @return
     */

    @Override
    public MyRespBundle<ConstructionOrderManageVo> getDecorationtOrderNum() {

        ConstructionOrderExample example = new ConstructionOrderExample();
        List<ConstructionOrder> list = constructionOrderMapper.selectByExample(example);

        /* 统计状态个数 */
        int waitExamine = 0, waitSign = 0;
        for (ConstructionOrder constructionOrder : list) {
            // 订单状态 统计
            int stage = constructionOrder.getOrderStage();
            if (stage == ConstructionStateEnum.STATE_540.getState()) {
                waitExamine++;
            }
            if (stage == ConstructionStateEnum.STATE_560.getState()) {
                waitSign++;
            }
        }

        ConstructionOrderManageVo constructionOrderManageVo = new ConstructionOrderManageVo();
        constructionOrderManageVo.setCityList(commonService.getCityList());
        constructionOrderManageVo.setWaitExamine(waitExamine);
        constructionOrderManageVo.setWaitSign(waitSign);
        return RespData.success(constructionOrderManageVo);
    }
}