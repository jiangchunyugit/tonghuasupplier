package cn.thinkfree.service.construction;


import cn.thinkfree.core.base.RespData;
import cn.thinkfree.core.constants.ConstructionStateEnum;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.core.utils.JSONUtil;
import cn.thinkfree.database.appvo.PersionVo;
import cn.thinkfree.database.mapper.*;
import cn.thinkfree.database.model.*;
import cn.thinkfree.service.approvalflow.AfInstanceService;
import cn.thinkfree.service.config.HttpLinks;
import cn.thinkfree.service.construction.vo.ConstructionOrderListVo;
import cn.thinkfree.service.construction.vo.DecorationOrderListVo;
import cn.thinkfree.service.neworder.NewOrderUserService;
import cn.thinkfree.service.utils.DateUtil;
import cn.thinkfree.service.utils.HttpUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderListCommonService {

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

    @Autowired
    OrderUserMapper orderUserMapper;

    @Autowired
    AfInstanceService afInstanceService;

    @Autowired
    FundsOrderMapper fundsOrderMapper;

    @Autowired
    DesignerOrderMapper designerOrderMapper;

    @Autowired
    private HttpLinks httpLinks;

    public static final String FORMAT = "yyyy-MM-dd HH:mm:ss";


    /**
     * 施工订单
     *
     * @param pageNum
     * @param pageSize
     * @param cityName
     * @return
     */
    public PageInfo<ConstructionOrderListVo> getConstructionOrderList(int pageNum, int pageSize, String cityName) {

        PageHelper.startPage(pageNum, pageSize);
        PageInfo<ConstructionOrderListVo> pageInfo = new PageInfo<>();
        PageInfo<ConstructionOrder> pageInfo2 = new PageInfo<>();

        ConstructionOrderExample example = new ConstructionOrderExample();
        example.setOrderByClause("create_time DESC");
        example.createCriteria().andStatusEqualTo(1);

        List<ConstructionOrder> list = constructionOrderMapper.selectByExample(example);
        List<ConstructionOrderListVo> listVo = new ArrayList<>();
        pageInfo2.setList(list);

        /* 项目编号List */
        List<String> listProjectNo = new ArrayList<>();
        for (ConstructionOrder constructionOrder : list) {
            listProjectNo.add(constructionOrder.getProjectNo());
        }

        /* 订单编号List */
        List<String> listOrdertNo = new ArrayList<>();
        for (ConstructionOrder constructionOrder : list) {
            listOrdertNo.add(constructionOrder.getOrderNo());
        }

        /* 公司编号List */
        List<String> listCompanyNo = new ArrayList<>();
        for (ConstructionOrder constructionOrder : list) {
            listCompanyNo.add(constructionOrder.getCompanyId());
        }

        // 所属地区 & 项目地址 & 预约日期
        List<Project> list1 = getProjectInfo(listProjectNo);
        // 项目经理
        List<Map<String, String>> list2 = getEmployeeInfo(listProjectNo, "CP");
        // 设计师
        List<Map<String, String>> list3 = getEmployeeInfo(listProjectNo, "CD");
        // 延期天数
        List<ProjectScheduling> list4 = getdelayDay(listProjectNo);
        // 确认验收
        Map<String, Integer> Map5 = getApprove(listProjectNo);
        // 合同金额/时间
        List<FundsOrder> list6 = getFundsOrder(listProjectNo);

        continueOut:
        for (ConstructionOrder constructionOrder : list) {
            ConstructionOrderListVo constructionOrderListVo = new ConstructionOrderListVo();

            // 所属地区 & 项目地址 & 预约日期
            for (Project project : list1) {
                if (constructionOrder.getProjectNo().equals(project.getProjectNo())) {
                    // 条件筛选 - 城市
                    if (!StringUtils.isBlank(cityName)) {
                        if (!cityName.equals(project.getCity())) {
                            continue continueOut;
                        }
                    }
                    constructionOrderListVo.setAddress(project.getCity());
                    constructionOrderListVo.setAddressDetail(project.getAddressDetail());
                    constructionOrderListVo.setAppointmentTime(project.getCreateTime());
                }
                // 业主 & 手机号  TODO 没做批量查询
//                PersionVo owner = getOwnerId(project.getOwnerId());
//                constructionOrderListVo.setOwner(owner.getName());
//                constructionOrderListVo.setPhone(owner.getPhone());
            }

            // 订单编号 & 项目编号
            constructionOrderListVo.setOrderNo(constructionOrder.getOrderNo());
            constructionOrderListVo.setProjectNo(constructionOrder.getProjectNo());
            // 项目经理
            for (Map<String, String> OrderUser : list2) {
                if (constructionOrder.getProjectNo().equals(OrderUser.get("projectNo"))) {
                    constructionOrderListVo.setProjectManager(OrderUser.get("name"));
                }
            }
            // 设计师
            for (Map<String, String> OrderUser : list3) {
                if (constructionOrder.getProjectNo().equals(OrderUser.get("projectNo"))) {
                    constructionOrderListVo.setDesignerName(OrderUser.get("name"));
                }
            }
            // 公司名称
            constructionOrderListVo.setCompanyName(getCompanyInfo(constructionOrder.getCompanyId()));
            // 施工阶段
            constructionOrderListVo.setConstructionProgress(getContstructionStage(constructionOrder.getConstructionStage()));
            // 订单状态
            constructionOrderListVo.setOrderStage(ConstructionStateEnum.getNowStateInfo(constructionOrder.getOrderStage(), 1));
            //延期天数 开工时间 竣工时间
            for (ProjectScheduling projectScheduling : list4) {
                if (constructionOrder.getProjectNo().equals(projectScheduling.getProjectNo())) {
                    constructionOrderListVo.setDelayDays(projectScheduling.getDelay());
                    constructionOrderListVo.setStartDates(projectScheduling.getStartTime());
                    constructionOrderListVo.setCompletionDays(projectScheduling.getEndTime());
                }
            }
            // 最近验收情况14
            for (Map.Entry<String, Integer> map : Map5.entrySet()) {
                if (constructionOrder.getProjectNo().equals(map.getKey())) {
                    switch (map.getKey()) {
                        case "1":
                            constructionOrderListVo.setCheckCondition("通过");
                        case "2":
                            constructionOrderListVo.setCheckCondition("未通过");
                        default:
                            constructionOrderListVo.setCheckCondition("--");
                    }
                }
            }
            // 签约日期 已支付 应支付金额
            for (FundsOrder fundsOrder : list6) {
                if (constructionOrder.getProjectNo().equals(fundsOrder.getProjectNo())) {
                    constructionOrderListVo.setSignedTime(DateUtil.formateToDate(fundsOrder.getSignedTime(), FORMAT));
                    constructionOrderListVo.setReducedContractAmount(fundsOrder.getActualAmount());
                    constructionOrderListVo.setHavePaid(fundsOrder.getPaidAmount());
                }
            }

            listVo.add(constructionOrderListVo);
        }
        pageInfo.setList(listVo);
        Page p = (Page) pageInfo2.getList();
        pageInfo.setPageNum(p.getPages());
    //    pageInfo.setTotal(pageInfo2.getList().size());
        return pageInfo;
    }


    /**
     * 施工订单 (装饰公司)
     *
     * @param companyNo
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageInfo<ConstructionOrderListVo> getDecorateOrderList(String companyNo,int pageNum, int pageSize) {
        if (StringUtils.isBlank(companyNo)){
            RespData.error(ResultMessage.ERROR.code, "订单编号不能为空");
        }

        PageHelper.startPage(pageNum, pageSize);
        PageInfo<ConstructionOrderListVo> pageInfo = new PageInfo<>();
        PageInfo<ConstructionOrder> pageInfo2 = new PageInfo<>();

        ConstructionOrderExample example = new ConstructionOrderExample();
        example.setOrderByClause("create_time DESC");
        example.createCriteria().andCompanyIdEqualTo(companyNo).andStatusEqualTo(1);

        List<ConstructionOrder> list = constructionOrderMapper.selectByExample(example);
        if(list.size() <= 0){
            RespData.error(ResultMessage.ERROR.code, "订单编号不符");
        }
        List<ConstructionOrderListVo> listVo = new ArrayList<>();

        pageInfo2.setList(list);

        /* 项目编号List */
        List<String> listProjectNo = new ArrayList<>();
        for (ConstructionOrder constructionOrder : list) {
            listProjectNo.add(constructionOrder.getProjectNo());
        }

        /* 订单编号List */
        List<String> listOrdertNo = new ArrayList<>();
        for (ConstructionOrder constructionOrder : list) {
            listOrdertNo.add(constructionOrder.getOrderNo());
        }

        /* 公司编号List */
        List<String> listCompanyNo = new ArrayList<>();
        for (ConstructionOrder constructionOrder : list) {
            listCompanyNo.add(constructionOrder.getCompanyId());
        }

        // 所属地区 & 项目地址 & 预约日期
        List<Project> list1 = getProjectInfo(listProjectNo);
        // 项目经理
        List<Map<String, String>> list2 = getEmployeeInfo(listProjectNo, "CP");
        // 设计师
        List<Map<String, String>> list3 = getEmployeeInfo(listProjectNo, "CD");
        // 延期天数
        List<ProjectScheduling> list4 = getdelayDay(listProjectNo);
        // 确认验收
        Map<String, Integer> Map5 = getApprove(listProjectNo);
        // 合同金额/时间
        List<FundsOrder> list6 = getFundsOrder(listProjectNo);

        continueOut:
        for (ConstructionOrder constructionOrder : list) {
            ConstructionOrderListVo constructionOrderListVo = new ConstructionOrderListVo();

            // 所属地区 & 项目地址 & 预约日期
            for (Project project : list1) {
                if (constructionOrder.getProjectNo().equals(project.getProjectNo())) {
                    constructionOrderListVo.setAddress(project.getCity());
                    constructionOrderListVo.setAddressDetail(project.getAddressDetail());
                    constructionOrderListVo.setAppointmentTime(project.getCreateTime());
                }
                // 业主 & 手机号  TODO 没做批量查询
//                PersionVo owner = getOwnerId(project.getOwnerId());
//                constructionOrderListVo.setOwner(owner.getName());
//                constructionOrderListVo.setPhone(owner.getPhone());
            }

            // 订单编号 & 项目编号
            constructionOrderListVo.setOrderNo(constructionOrder.getOrderNo());
            constructionOrderListVo.setProjectNo(constructionOrder.getProjectNo());
            // 项目经理
            for (Map<String, String> OrderUser : list2) {
                if (constructionOrder.getProjectNo().equals(OrderUser.get("projectNo"))) {
                    constructionOrderListVo.setProjectManager(OrderUser.get("name"));
                }
            }
            // 设计师
            for (Map<String, String> OrderUser : list3) {
                if (constructionOrder.getProjectNo().equals(OrderUser.get("projectNo"))) {
                    constructionOrderListVo.setDesignerName(OrderUser.get("name"));
                }
            }
            // 公司名称
            constructionOrderListVo.setCompanyName(getCompanyInfo(constructionOrder.getCompanyId()));
            // 施工阶段
            constructionOrderListVo.setConstructionProgress(getContstructionStage(constructionOrder.getConstructionStage()));
            // 订单状态
            constructionOrderListVo.setOrderStage(ConstructionStateEnum.getNowStateInfo(constructionOrder.getOrderStage(), 2));
            //延期天数 开工时间 竣工时间
            for (ProjectScheduling projectScheduling : list4) {
                if (constructionOrder.getProjectNo().equals(projectScheduling.getProjectNo())) {
                    constructionOrderListVo.setDelayDays(projectScheduling.getDelay());
                    constructionOrderListVo.setStartDates(projectScheduling.getStartTime());
                    constructionOrderListVo.setCompletionDays(projectScheduling.getEndTime());
                }
            }
            // 最近验收情况14
            for (Map.Entry<String, Integer> map : Map5.entrySet()) {
                if (constructionOrder.getProjectNo().equals(map.getKey())) {
                    switch (map.getKey()) {
                        case "1":
                            constructionOrderListVo.setCheckCondition("通过");
                        case "2":
                            constructionOrderListVo.setCheckCondition("未通过");
                        default:
                            constructionOrderListVo.setCheckCondition("--");
                    }
                }
            }
            // 签约日期 已支付 应支付金额
            for (FundsOrder fundsOrder : list6) {
                if (constructionOrder.getProjectNo().equals(fundsOrder.getProjectNo())) {
                    constructionOrderListVo.setSignedTime(DateUtil.formateToDate(fundsOrder.getSignedTime(), FORMAT));
                    constructionOrderListVo.setReducedContractAmount(fundsOrder.getActualAmount());
                    constructionOrderListVo.setHavePaid(fundsOrder.getPaidAmount());
                }
            }

            listVo.add(constructionOrderListVo);
        }

        pageInfo.setList(listVo);
        Page p = (Page) pageInfo2.getList();
        pageInfo.setPageNum(p.getPages());
        pageInfo.setTotal(pageInfo2.getList().size());
        return pageInfo;
    }

    /**
     *  项目派单 给员工
     * @param pageNum
     * @param pageSize
     * @param projectNo
     * @param appointmentTime
     * @param addressDetail
     * @param owner
     * @param phone
     * @param orderStage
     * @return
     */
    public PageInfo<DecorationOrderListVo> getDecorationOrderList(String companyNo,int pageNum, int pageSize, String projectNo, String appointmentTime,
                                                                  String addressDetail, String owner, String phone, String orderStage) {

        if (StringUtils.isBlank(companyNo)){
            RespData.error(ResultMessage.ERROR.code, "项目编号不能为空");
        }
        PageHelper.startPage(pageNum, pageSize);
        PageInfo<DecorationOrderListVo> pageInfo = new PageInfo<>();
        PageInfo<ConstructionOrder> pageInfo2 = new PageInfo<>();

        ConstructionOrderExample example = new ConstructionOrderExample();
        example.setOrderByClause("create_time DESC");
        example.createCriteria().andCompanyIdEqualTo(companyNo).andStatusEqualTo(1);

        if (!StringUtils.isBlank(projectNo)){
            example.createCriteria().andProjectNoEqualTo(projectNo);
        }

        List<ConstructionOrder> list = constructionOrderMapper.selectByExample(example);
        List<DecorationOrderListVo> listVo = new ArrayList<>();

        pageInfo2.setList(list);

        /* 项目编号List */
        List<String> listProjectNo = new ArrayList<>();
        for (ConstructionOrder constructionOrder : list) {
            listProjectNo.add(constructionOrder.getProjectNo());
        }

        /* 订单编号List */
        List<String> listOrdertNo = new ArrayList<>();
        for (ConstructionOrder constructionOrder : list) {
            listOrdertNo.add(constructionOrder.getOrderNo());
        }

        /* 公司编号List */
        List<String> listCompanyNo = new ArrayList<>();
        for (ConstructionOrder constructionOrder : list) {
            listCompanyNo.add(constructionOrder.getCompanyId());
        }

        // 所属地区 & 项目地址 & 预约日期
        List<Project> list1 = getProjectInfo(listProjectNo);
        // 项目经理
        List<Map<String, String>> list2 = getEmployeeInfo(listProjectNo, "CP");
        // 设计师
        List<Map<String, String>> list3 = getEmployeeInfo(listProjectNo, "CD");
        // 工长
        List<Map<String, String>> list4 = getEmployeeInfo(listProjectNo, "CM");
        // 管家
        List<Map<String, String>> list5 = getEmployeeInfo(listProjectNo, "CS");

        conOut:
        for (ConstructionOrder constructionOrder : list) {
            DecorationOrderListVo decorationOrderListVo = new DecorationOrderListVo();

            // 项目地址 & 预约日期
            for (Project project : list1) {
                if (constructionOrder.getProjectNo().equals(project.getProjectNo())) {
                    decorationOrderListVo.setAddressDetail(project.getAddressDetail());
                    if (!StringUtils.isBlank(addressDetail)){
                        if (!addressDetail.equals(project.getAddressDetail())){
                            continue conOut;
                        }
                    }
                    decorationOrderListVo.setAppointmentTime(project.getCreateTime());
                }
                // 业主 & 手机号  TODO 没做批量查询
//                PersionVo ownerP = getOwnerId(project.getOwnerId());
//                if (!StringUtils.isBlank(owner)){
//                    if (!owner.equals(ownerP.getName())){
//                        continue conOut;
//                    }
//                }
//                if (!StringUtils.isBlank(phone)){
//                    if (!phone.equals(ownerP.getPhone())){
//                        continue conOut;
//                    }
//                }
//                decorationOrderListVo.setOwner(ownerP.getName());
//                decorationOrderListVo.setPhone(ownerP.getPhone());
            }

            // 预算报价 TODO 新建表
            decorationOrderListVo.setAppointmentPrice("");
            if (!StringUtils.isBlank(appointmentTime)){

            }


            // 项目编号
            decorationOrderListVo.setProjectNo(constructionOrder.getProjectNo());
            // 订单编号
            decorationOrderListVo.setOrderNo(constructionOrder.getOrderNo());
            // 订单状态
            if (!StringUtils.isBlank(orderStage)){
                if (!orderStage.equals(ConstructionStateEnum.getNowStateInfo(constructionOrder.getOrderStage(), 2))){
                    continue conOut;
                }
            }
            decorationOrderListVo.setOrderStage(ConstructionStateEnum.getNowStateInfo(constructionOrder.getOrderStage(), 2));

            // 项目经理
            for (Map<String, String> OrderUser : list2) {
                if (constructionOrder.getProjectNo().equals(OrderUser.get("projectNo"))) {
                    decorationOrderListVo.setProjectManager(OrderUser.get("name"));
                }
            }
            // 设计师
            for (Map<String, String> OrderUser : list3) {
                if (constructionOrder.getProjectNo().equals(OrderUser.get("projectNo"))) {
                    decorationOrderListVo.setDesignerName(OrderUser.get("name"));
                }
            }
            // 工长
            for (Map<String, String> OrderUser : list4) {
                if (constructionOrder.getProjectNo().equals(OrderUser.get("projectNo"))) {
                    decorationOrderListVo.setHeadWork(OrderUser.get("name"));
                }
            }
            // 管家
            for (Map<String, String> OrderUser : list5) {
                if (constructionOrder.getProjectNo().equals(OrderUser.get("projectNo"))) {
                    decorationOrderListVo.setHousekeeper(OrderUser.get("name"));
                }
            }

            listVo.add(decorationOrderListVo);
        }
        pageInfo.setList(listVo);
        Page p = (Page) pageInfo2.getList();
        pageInfo.setPageNum(p.getPages());
        pageInfo.setTotal(pageInfo2.getList().size());
        return pageInfo;
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
        HttpUtils.HttpRespMsg httpRespMsg = HttpUtils.post(httpLinks.getUserCenterGetUserMsg(), requestMap);
        Map responseMap = JSONUtil.json2Bean(httpRespMsg.getContent(), Map.class);
        owner.setName(responseMap.get("nickName").toString());
        owner.setPhone(responseMap.get("phone").toString());
        return owner;
    }

    /**
     * 设计师 & 项目经理
     *
     * @param listProjectNo
     * @return
     */
    public List<Map<String, String>> getEmployeeInfo(List<String> listProjectNo, String role) {
        OrderUserExample example = new OrderUserExample();
        example.createCriteria().andProjectNoIn(listProjectNo).andRoleCodeEqualTo(role).andIsTransferEqualTo((short) 0);
        List<OrderUser> list = orderUserMapper.selectByExample(example);
        List<Map<String, String>> listName = new ArrayList<>();
        for (OrderUser orderUser : list) {
            EmployeeMsgExample example2 = new EmployeeMsgExample();
            example2.createCriteria().andUserIdEqualTo(orderUser.getUserId()).andRoleCodeEqualTo(role).andEmployeeStateEqualTo(1);
            List<EmployeeMsg> listEm = employeeMsgMapper.selectByExample(example2);
            for (EmployeeMsg employeeMsg : listEm) {
                Map<String, String> map = new HashMap<>();
                map.put("projectNo", orderUser.getProjectNo());
                map.put("name", employeeMsg.getRealName());
                listName.add(map);
            }
        }
        return listName;
    }

    /**
     * 公司名称
     * TODO 批量
     *
     * @param companyId
     * @return
     */
    public String getCompanyInfo(String companyId) {
        String companyName = constructionOrderMapper.getCompanyName(companyId);
        return companyName;
    }

    /**
     * 查询 施工阶段
     *
     * @param stage
     * @return
     */
    public String getContstructionStage(int stage) {
        ProjectBigSchedulingExample example = new ProjectBigSchedulingExample();
        example.createCriteria().andSortEqualTo(stage);
        List<ProjectBigScheduling> list = projectBigSchedulingMapper.selectByExample(example);
        return list.get(0).getName();
    }

    /**
     * 查询 延期天数/开工时间/竣工时间
     *
     * @param listProjectNo
     * @return
     */
    public List<ProjectScheduling> getdelayDay(List<String> listProjectNo) {
        ProjectSchedulingExample example = new ProjectSchedulingExample();
        example.createCriteria().andProjectNoIn(listProjectNo);
        return projectSchedulingMapper.selectByExample(example);
    }

    /**
     * 查询 延期天数
     *
     * @param listProjectNo
     * @return
     */
    public Map<String, Integer> getApprove(List<String> listProjectNo) {
        return afInstanceService.getProjectCheckResult(listProjectNo);
    }

    /**
     * 查询 支付
     *
     * @param listProjectNo
     * @return
     */
    public List<FundsOrder> getFundsOrder(List<String> listProjectNo) {
        FundsOrderExample example = new FundsOrderExample();
        example.createCriteria().andProjectNoIn(listProjectNo);
        List<FundsOrder> list = fundsOrderMapper.selectByExample(example);
        return list;
    }

}
