package cn.thinkfree.service.construction.impl;

import cn.thinkfree.core.base.RespData;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.database.appvo.PersionVo;
import cn.thinkfree.database.mapper.*;
import cn.thinkfree.database.model.*;
import cn.thinkfree.service.construction.CommonService;
import cn.thinkfree.service.construction.ConstructionOrderOperate;
import cn.thinkfree.service.construction.vo.ConstructionOrderListVo;
import cn.thinkfree.service.construction.vo.ConstructionOrderManageVo;
import cn.thinkfree.service.neworder.NewOrderUserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ConstructionOrderOperateImpl implements ConstructionOrderOperate {

    @Autowired
    ConstructionOrderMapper constructionOrderMapper;

    @Autowired
    CommonService commonService;

    @Autowired
    ProjectMapper projectMapper;

    @Autowired
    NewOrderUserService newOrderUserService;

    @Autowired
    EmployeeMsgMapper employeeMsgMapper;

    @Autowired
    CompanyInfoMapper companyInfoMapper;

    @Autowired
    ProjectBigSchedulingMapper projectBigSchedulingMapper;


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
        List<ConstructionOrderListVo> listVo = new ArrayList<>();

        for (ConstructionOrder constructionOrder : list) {
            ConstructionOrderListVo constructionOrderListVo = new ConstructionOrderListVo();

            // 所属地区 & 项目地址 & 预约日期
            List<Project> list1 = getProjectInfo(constructionOrder.getProjectNo());
            constructionOrderListVo.setAddress(list1.get(0).getCity());
            constructionOrderListVo.setAddressDetail(list1.get(0).getAddressDetail());
            constructionOrderListVo.setAppointmentTime(list1.get(0).getCreateTime());

            // 业主 & 手机号
            PersionVo owner = getOwnerId(constructionOrder.getOrderNo());
            constructionOrderListVo.setOwner(owner.getName());
            constructionOrderListVo.setPhone(owner.getPhone());

            // 项目经理 & 设计师
            constructionOrderListVo.setProjectManager(getEmployeeInfo(constructionOrder.getCompanyId(), "CP"));
            constructionOrderListVo.setDesignerName(getEmployeeInfo(constructionOrder.getCompanyId(), "CD"));

            // 公司名称
            List<CompanyInfo> list3 = getCompanyInfo(constructionOrder.getCompanyId());
            constructionOrderListVo.setCompanyName(list3.get(0).getCompanyName());

            // 订单编号 & 项目编号
            constructionOrderListVo.setOrderNo(constructionOrder.getOrderNo());
            constructionOrderListVo.setProjectNo(constructionOrder.getProjectNo());
            // 施工阶段
            constructionOrderListVo.setConstructionProgress(getScheduill(constructionOrder.getConstructionStage()));

            // TODO
            constructionOrderListVo.setOrderStage(constructionOrder.getOrderStage());

            // TODO 签约日期  sign_time
            constructionOrderListVo.setSignedTime(new Date());
            // TODO 折后合同额
            constructionOrderListVo.setReducedContractAmount(0);
            // TODO 已支付11
            constructionOrderListVo.setHavePaid(1);


            // TODO  ??????
            // TODO   最近验收情况14
            constructionOrderListVo.setCheckCondition(3);

            constructionOrderListVo.setDelayDays(15);                      // TODO   延期天数15


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
     * 查询项目信息
     *
     * @param projectNo
     * @return
     */
    public List<Project> getProjectInfo(String projectNo) {
        ProjectExample example = new ProjectExample();
        example.createCriteria().andProjectNoEqualTo(projectNo);
        List<Project> list = projectMapper.selectByExample(example);
        return list;
    }

    /**
     * 查询用户信息
     *
     * @param userId
     * @return
     */
    public PersionVo getOwnerId(String userId) {
        PersionVo owner = new PersionVo();
        Map userName = newOrderUserService.getUserName(userId, "CC");
        if(userName.isEmpty()){
            owner.setPhone(null);
            owner.setName(null);
            return owner;
        }
        owner.setPhone(userName.get("phone").toString());
        owner.setName(userName.get("nickName").toString());
        return owner;
    }

    /**
     * 设计师 & 项目经理
     *
     * @param companyId
     * @return
     */
    public String getEmployeeInfo(String companyId, String role) {
        EmployeeMsgExample example = new EmployeeMsgExample();
        example.createCriteria().andCompanyIdEqualTo(companyId).andRoleCodeEqualTo(role).andEmployeeStateEqualTo(1);
        List<EmployeeMsg> list = employeeMsgMapper.selectByExample(example);
        return list.get(0).getRealName();
    }

    /**
     * 公司名称
     *
     * @param companyId
     * @return
     */
    public List<CompanyInfo> getCompanyInfo(String companyId) {
        CompanyInfoExample example = new CompanyInfoExample();
        example.createCriteria().andCompanyIdEqualTo(companyId);
        List<CompanyInfo> list = companyInfoMapper.selectByExample(example);
        return list;
    }

    /**
     * 查询 施工阶段
     *
     * @param sort
     * @return
     */
    public String getScheduill(int sort) {
        ProjectBigSchedulingExample example = new ProjectBigSchedulingExample();
        example.createCriteria().andSortEqualTo(sort);
        List<ProjectBigScheduling> list = projectBigSchedulingMapper.selectByExample(example);
        return list.get(0).getName();
    }


}
