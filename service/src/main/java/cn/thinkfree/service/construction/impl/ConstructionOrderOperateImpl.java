package cn.thinkfree.service.construction.impl;

import cn.thinkfree.core.base.RespData;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.utils.JSONUtil;
import cn.thinkfree.database.appvo.PersionVo;
import cn.thinkfree.database.mapper.*;
import cn.thinkfree.database.model.*;
import cn.thinkfree.service.constants.HttpLinks;
import cn.thinkfree.service.construction.CommonService;
import cn.thinkfree.service.construction.ConstructionOrderOperate;
import cn.thinkfree.service.construction.vo.ConstructionOrderListVo;
import cn.thinkfree.service.construction.vo.ConstructionOrderManageVo;
import cn.thinkfree.service.neworder.NewOrderUserService;
import cn.thinkfree.service.utils.HttpUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
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

    @Autowired
    ProjectSchedulingMapper projectSchedulingMapper;

    @Resource
    private HttpLinks httpLinks;


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

        /* 项目编号List */
        List<String> listProjectNo = new ArrayList<>();
        for (ConstructionOrder constructionOrder : list){
            listProjectNo.add(constructionOrder.getProjectNo());
        }

        /* 订单编号List */
        List<String> listOrdertNo = new ArrayList<>();
        for (ConstructionOrder constructionOrder : list){
            listOrdertNo.add(constructionOrder.getOrderNo());
        }

        /* 公司编号List */
        List<String> listCompanyNo = new ArrayList<>();
        for (ConstructionOrder constructionOrder : list){
            listCompanyNo.add(constructionOrder.getCompanyId());
        }

        for (ConstructionOrder constructionOrder : list) {
            ConstructionOrderListVo constructionOrderListVo = new ConstructionOrderListVo();

            // 所属地区 & 项目地址 & 预约日期
            List<Project> list1 = getProjectInfo(listProjectNo);
            for (Project project : list1){
                if (constructionOrder.getProjectNo().equals(project.getProjectNo())){
                    constructionOrderListVo.setAddress(project.getCity());
                    constructionOrderListVo.setAddressDetail(project.getAddressDetail());
                    constructionOrderListVo.setAppointmentTime(project.getCreateTime());
                }
                // 业主 & 手机号  TODO 没做批量查询
//                PersionVo owner = getOwnerId(project.getOwnerId());
//                constructionOrderListVo.setOwner(owner.getName());
//                constructionOrderListVo.setPhone(owner.getPhone());
            }

            // 项目经理
            List<EmployeeMsg> list2 = getEmployeeInfo(listCompanyNo, "CP");
            for (EmployeeMsg employeeMsg :list2){
                constructionOrderListVo.setProjectManager(employeeMsg.getRealName());
            }
            // 设计师
            List<EmployeeMsg> list3 = getEmployeeInfo(listCompanyNo, "CD");
            for (EmployeeMsg employeeMsg :list3){
                constructionOrderListVo.setDesignerName(employeeMsg.getRealName());
            }


//            // 公司名称
//            List<CompanyInfo> list3 = getCompanyInfo(constructionOrder.getCompanyId());
//            constructionOrderListVo.setCompanyName(list3.get(0).getCompanyName());
//
//            // 订单编号 & 项目编号
//            constructionOrderListVo.setOrderNo(constructionOrder.getOrderNo());
//            constructionOrderListVo.setProjectNo(constructionOrder.getProjectNo());
//            // 施工阶段
//            constructionOrderListVo.setConstructionProgress(getScheduill(constructionOrder.getConstructionStage()));
//
//            // TODO
//            constructionOrderListVo.setOrderStage(constructionOrder.getOrderStage());
//
//            // TODO 签约日期  sign_time
//            constructionOrderListVo.setSignedTime(new Date());
//            // TODO 折后合同额
//            constructionOrderListVo.setReducedContractAmount(0);
//            // TODO 已支付11
//            constructionOrderListVo.setHavePaid(1);
//
//
//            // TODO  ??????
//            // TODO   最近验收情况14
//            constructionOrderListVo.setCheckCondition(3);
//
//            constructionOrderListVo.setDelayDays(15);                      // TODO   延期天数15


            listVo.add(constructionOrderListVo);
        }


        pageInfo.setList(listVo);


        ConstructionOrderManageVo constructionOrderManageVo = new ConstructionOrderManageVo();
  /*      constructionOrderManageVo.setCityList(commonService.getCityList());*/
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
     * @param listProjectNo
     * @return
     */
    public List<Project> getProjectInfo(List<String> listProjectNo) {
        ProjectExample example = new ProjectExample();
        example.createCriteria().andProjectNoIn(listProjectNo);
        return projectMapper.selectByExample(example);
    }

    /**
     * 查询用户信息 -用户中心接口
     *
     * @param userId
     * @return
     */
    public PersionVo getOwnerId(String userId) {
        PersionVo owner = new PersionVo();
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("userId", userId);
        requestMap.put("roleId", "CC");
        HttpUtils.HttpRespMsg httpRespMsg = HttpUtils.post(httpLinks.getUserCenterGetUserMsgUrl(), requestMap);
        Map responseMap = JSONUtil.json2Bean(httpRespMsg.getContent(), Map.class);
        owner.setName(responseMap.get("nickName").toString());
        owner.setPhone(responseMap.get("phone").toString());
        return owner;
    }

    /**
     * 设计师 & 项目经理
     *
     * @param listCompanyNo
     * @return
     */
    public List<EmployeeMsg> getEmployeeInfo(List<String> listCompanyNo, String role) {
        EmployeeMsgExample example = new EmployeeMsgExample();
        example.createCriteria().andCompanyIdIn(listCompanyNo).andRoleCodeEqualTo(role).andEmployeeStateEqualTo(1);
        List<EmployeeMsg> list = employeeMsgMapper.selectByExample(example);
        return list;
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

    /**
     * 查询 延期天数
     *
     * @param projectNo
     * @return
     */
    public int getdelayDay(String projectNo) {
        ProjectSchedulingExample example = new ProjectSchedulingExample();
        example.createCriteria().andProjectNoEqualTo(projectNo);
        List<ProjectScheduling> list = projectSchedulingMapper.selectByExample(example);
        return list.get(0).getDelay();
    }


}
