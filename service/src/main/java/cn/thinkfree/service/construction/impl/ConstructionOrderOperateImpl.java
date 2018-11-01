package cn.thinkfree.service.construction.impl;

import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.base.RespData;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.database.mapper.ConstructionOrderMapper;
import cn.thinkfree.database.model.ConstructionOrder;
import cn.thinkfree.database.model.ConstructionOrderExample;
import cn.thinkfree.service.construction.ConstructionOrderOperate;
import cn.thinkfree.service.construction.vo.ConstructionOrderManageVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ConstructionOrderOperateImpl implements ConstructionOrderOperate {

    @Autowired
    ConstructionOrderMapper constructionOrderMapper;


    /**
     * 施工订单管理-列表
     * 运营后台
     *
     * @return
     */
    @Override
    public MyRespBundle<PageInfo<ConstructionOrderManageVo>> getConstructionOrderList(int pageNum, int pageSize) {


        PageHelper.startPage(pageNum, pageSize);
        PageInfo<ConstructionOrderManageVo> pageInfo = new PageInfo<>();
        ConstructionOrderExample example = new ConstructionOrderExample();
        example.setOrderByClause("create_time DESC");
        List<ConstructionOrder> list = constructionOrderMapper.selectByExample(example);

        ConstructionOrderManageVo constructionOrderManageVo = new ConstructionOrderManageVo();
        for (ConstructionOrder constructionOrder:list){
            constructionOrderManageVo.setAddress("所属地区");
            constructionOrderManageVo.setCompanyName("公司名称");
            constructionOrderManageVo.setOrderNo(constructionOrder.getOrderNo());
            constructionOrderManageVo.setProjectNo(constructionOrder.getProjectNo());
            constructionOrderManageVo.setAppointmentTime(new Date());       // TODO 预约日期
            constructionOrderManageVo.setSignedTime(new Date());            // TODO 签约日期
            constructionOrderManageVo.setAddressDetail("项目地址");
            constructionOrderManageVo.setOwner("业主");
            constructionOrderManageVo.setPhone("手机号");
            constructionOrderManageVo.setReducedContractAmount(0);          // TODO 折后合同额10
            constructionOrderManageVo.setHavePaid(1);                       // TODO 已支付11
            constructionOrderManageVo.setOrderStage(constructionOrder.getOrderStage());
            constructionOrderManageVo.setConstructionProgress("施工进度");              // TODO 施工进度13
            constructionOrderManageVo.setCheckCondition(3);                           // TODO   最近验收情况14

            constructionOrderManageVo.setDelayDays(15);                      // TODO   延期天数15
            constructionOrderManageVo.setProjectManager("项目经理");           //TODO 项目经理16
            constructionOrderManageVo.setDesignerName("设计师17");           //TODO 设计师17

        }
        List<ConstructionOrderManageVo> list1 = new ArrayList<>();
        list1.add(constructionOrderManageVo);

        pageInfo.setList(list1);


        return RespData.success(pageInfo);
    }
}
