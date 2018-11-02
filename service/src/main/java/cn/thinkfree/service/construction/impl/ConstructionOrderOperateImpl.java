package cn.thinkfree.service.construction.impl;

import cn.thinkfree.core.base.RespData;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.database.mapper.ConstructionOrderMapper;
import cn.thinkfree.database.mapper.ProjectMapper;
import cn.thinkfree.database.model.ConstructionOrder;
import cn.thinkfree.database.model.ConstructionOrderExample;
import cn.thinkfree.database.model.Project;
import cn.thinkfree.database.model.ProjectExample;
import cn.thinkfree.service.construction.CommonService;
import cn.thinkfree.service.construction.ConstructionOrderOperate;
import cn.thinkfree.service.construction.vo.ConstructionOrderListVo;
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

    @Autowired
    CommonService commonService;

    @Autowired
    ProjectMapper projectMapper;

    /**
     * 施工订单管理-列表
     * 运营后台
     *
     * @return
     */
    @Override
    public MyRespBundle<ConstructionOrderManageVo> getConstructionOrderList(int pageNum, int pageSize) {


        PageHelper.startPage(pageNum, pageSize);
        PageInfo<ConstructionOrderListVo> pageInfo = new PageInfo<>();
        ConstructionOrderExample example = new ConstructionOrderExample();
        example.setOrderByClause("create_time DESC");
        List<ConstructionOrder> list = constructionOrderMapper.selectByExample(example);
        List<ConstructionOrderListVo> listVo= new ArrayList<>();

        for (ConstructionOrder constructionOrder:list){
            ConstructionOrderListVo constructionOrderListVo = new ConstructionOrderListVo();
            constructionOrderListVo.setAddress("所属地区");
            constructionOrderListVo.setCompanyName("公司名称");
            constructionOrderListVo.setOrderNo(constructionOrder.getOrderNo());
            constructionOrderListVo.setProjectNo(constructionOrder.getProjectNo());
            constructionOrderListVo.setAppointmentTime(new Date());       // TODO 预约日期
            constructionOrderListVo.setSignedTime(new Date());            // TODO 签约日期
            constructionOrderListVo.setAddressDetail("项目地址");
            constructionOrderListVo.setOwner("业主");
            constructionOrderListVo.setPhone("手机号");
            constructionOrderListVo.setReducedContractAmount(0);          // TODO 折后合同额10
            constructionOrderListVo.setHavePaid(1);                       // TODO 已支付11
            constructionOrderListVo.setOrderStage(constructionOrder.getOrderStage());
            constructionOrderListVo.setConstructionProgress("施工进度");              // TODO 施工进度13
            constructionOrderListVo.setCheckCondition(3);                           // TODO   最近验收情况14

            constructionOrderListVo.setDelayDays(15);                      // TODO   延期天数15
            constructionOrderListVo.setProjectManager("项目经理");           //TODO 项目经理16
            constructionOrderListVo.setDesignerName("设计师17");           //TODO 设计师17

            listVo.add(constructionOrderListVo);
        }
        pageInfo.setList(listVo);

        ConstructionOrderManageVo constructionOrderManageVo = new ConstructionOrderManageVo();
        constructionOrderManageVo.setCityList(commonService.getCityList());
        constructionOrderManageVo.setOrderList(pageInfo.getList());
        constructionOrderManageVo.setCountPageNum(500);
        constructionOrderManageVo.setWaitExamine("12");
        constructionOrderManageVo.setWaitSign("34");
        constructionOrderManageVo.setWaitPay("15");
        constructionOrderManageVo.setOrderNum(456);


        return RespData.success(constructionOrderManageVo);
    }

    /**
     *  查询项目信息
     * @param projectNoList
     * @return
     */
    public List<Project> getProjectInfo (List<String> projectNoList){
        ProjectExample example = new ProjectExample();
        example.createCriteria().andProjectNoIn(projectNoList);
        List<Project> list = projectMapper.selectByExample(example);
        if (list.isEmpty()){
            return null;
        }else {
            return list;
        }
    }

}
