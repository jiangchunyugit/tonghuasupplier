package cn.thinkfree.service.construction.impl;


import cn.thinkfree.core.constants.ComplaintStateEnum;
import cn.thinkfree.core.constants.ConstructOrderConstants;
import cn.thinkfree.core.constants.ConstructionStateEnum;
import cn.thinkfree.database.appvo.PersionVo;
import cn.thinkfree.database.mapper.*;
import cn.thinkfree.database.model.*;
import cn.thinkfree.service.approvalflow.AfInstanceService;
import cn.thinkfree.service.construction.CommonService;
import cn.thinkfree.service.construction.ConstructionStateService;
import cn.thinkfree.service.construction.OrderListCommonService;
import cn.thinkfree.service.construction.vo.ConstructionOrderListVo;
import cn.thinkfree.service.construction.vo.DecorationOrderListVo;
import cn.thinkfree.service.neworder.NewOrderUserService;
import cn.thinkfree.service.utils.DateUtil;
import cn.thinkfree.service.utils.HttpUtils;
import cn.thinkfree.service.utils.PageInfoUtils;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class OrderListCommonServiceImpl implements OrderListCommonService {

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
    ProjectQuotationMapper projectQuotationMapper;
    @Autowired
    private OrderContractMapper orderContractMapper;
    @Autowired
    private ConstructionStateService constructionStateService;

    /**
     * 用户中心地址接口
     */
    @Value("${custom.service.ip}")
    private String userCenterIp;

    @Value("${custom.service.port}")
    private String userCenterPort;

    private static String userCenterUrl = "/userapi/other/api/user/getListUserByUserIds";

    public static final String FORMAT = "yyyy-MM-dd HH:mm:ss";

    public String getUrl(String suffix) {
        return "http://" + userCenterIp + ":" + userCenterPort + suffix;
    }

    /**
     * 施工订单
     *
     * @param pageNum
     * @param pageSize
     * @param cityName
     * @param orderType 1派单列表，2订单列表
     * @return
     */
    @Override
    public PageInfo<ConstructionOrderListVo> getConstructionOrderList(int pageNum, int pageSize, String cityName, int orderType) {
        if(pageSize < 5){
            pageSize = 5;
        }
        PageHelper.startPage(pageNum, pageSize);
        ConstructionOrderExample example = new ConstructionOrderExample();
        example.setOrderByClause("create_time DESC");
        ConstructionOrderExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo(1);
        if(orderType == 1){
            criteria.andOrderStageIn(Arrays.asList(ConstructionStateEnum.STATE_500.getState(),ConstructionStateEnum.STATE_510.getState()));
        }else{
            criteria.andOrderStageGreaterThanOrEqualTo(ConstructionStateEnum.STATE_540.getState());
        }
        List<ConstructionOrder> list = constructionOrderMapper.selectByExample(example);
        PageInfo<ConstructionOrder> orderPageInfo = new PageInfo<>(list);
        List<ConstructionOrderListVo> listVo = new ArrayList<>();
        /* 项目编号List */
        List<String> listProjectNo = new ArrayList<>();
        for (ConstructionOrder constructionOrder : list) {
            listProjectNo.add(constructionOrder.getProjectNo());
        }
        /* 用户编号List */
        List<Map<String, String>> listUserNo = new ArrayList<>();
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
                    constructionOrderListVo.setAddress(commonService.getCityNameByCode(project.getCity()));
                    constructionOrderListVo.setAddressDetail(project.getAddressDetail());
                    constructionOrderListVo.setAppointmentTime(project.getCreateTime());
                    // 业主 & 手机号
                    Map<String, String> map = new HashMap<>();
                    map.put("userId", project.getOwnerId());
                    map.put("roleId", "CC");
                    listUserNo.add(map);
                    List<PersionVo>  owner = getOwnerId(listUserNo);
                    for (PersionVo persionVo : owner){
                        if (project.getOwnerId().equals(persionVo.getUserId())){
                            constructionOrderListVo.setOwner(persionVo.getName());
                            constructionOrderListVo.setPhone(persionVo.getPhone());
                        }
                    }
                }
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
            // 是否可以派单 （运营平台指派装饰公司）
            // TODO 510
            if (constructionOrder.getOrderStage().equals(ConstructionStateEnum.STATE_500.getState())) {
                constructionOrderListVo.setIsDistribution(1);
            } else {
                constructionOrderListVo.setIsDistribution(0);
            }
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
        PageInfo<ConstructionOrderListVo> pageInfo = PageInfoUtils.pageInfo(orderPageInfo,listVo);
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
    @Override
    public PageInfo<ConstructionOrderListVo> getDecorateOrderList(String companyNo, int pageNum, int pageSize) {
        if(pageSize < 5){
            pageSize = 5;
        }
        if (StringUtils.isBlank(companyNo)) {
            return new PageInfo<>();
        }

        PageHelper.startPage(pageNum, pageSize);

        ConstructionOrderExample example = new ConstructionOrderExample();
        example.setOrderByClause("create_time DESC");
        example.createCriteria().andCompanyIdEqualTo(companyNo).andStatusEqualTo(1);

        List<ConstructionOrder> list = constructionOrderMapper.selectByExample(example);
        if (list.size() <= 0) {
            return new PageInfo<>();
        }
        PageInfo<ConstructionOrder> pageInfo2 = new PageInfo<>(list);
        List<ConstructionOrderListVo> listVo = new ArrayList<>();

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
        /* 用户编号List */
        List<Map<String, String>> listUserNo = new ArrayList<>();

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
                    // 业主 & 手机号
                    Map<String, String> map = new HashMap<>();
                    map.put("userId", project.getOwnerId());
                    map.put("roleId", "CC");
                    listUserNo.add(map);
                    List<PersionVo>  owner = getOwnerId(listUserNo);
                    for (PersionVo persionVo : owner){
                        if (project.getOwnerId().equals(persionVo.getUserId())){
                            constructionOrderListVo.setOwner(persionVo.getName());
                            constructionOrderListVo.setPhone(persionVo.getPhone());
                        }
                    }
                }
            }

            OrderContractExample orderContractExample = new OrderContractExample();
            orderContractExample.createCriteria().andOrderNumberEqualTo(constructionOrder.getOrderNo());
            List<OrderContract> orderContracts = orderContractMapper.selectByExample(orderContractExample);
            if (orderContracts != null && ! orderContracts.isEmpty()) {
                constructionOrderListVo.setContractNo(orderContracts.get(0).getContractNumber());
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

        PageInfo<ConstructionOrderListVo> pageInfo = PageInfoUtils.pageInfo(pageInfo2,listVo);
        return pageInfo;
    }

    /**
     * 项目派单 给员工
     *
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
    @Override
    public PageInfo<DecorationOrderListVo> getDecorationOrderList(String companyNo, int pageNum, int pageSize, String projectNo, String appointmentTime,
                                                                  String addressDetail, String owner, String phone, String orderStage) {

        if (StringUtils.isBlank(companyNo)) {
            return new PageInfo<>();
        }
        PageHelper.startPage(pageNum, pageSize);

        ConstructionOrderExample example = new ConstructionOrderExample();
        example.setOrderByClause("create_time DESC");
        ConstructionOrderExample.Criteria criteria = example.createCriteria();
        criteria.andCompanyIdEqualTo(companyNo).andStatusEqualTo(1);

        List<Integer> complaintStates = new ArrayList<>();
        complaintStates.add(ComplaintStateEnum.STATE_1.getState());
        complaintStates.add(ComplaintStateEnum.STATE_4.getState());
        criteria.andComplaintStateIn(complaintStates);

        if (!StringUtils.isBlank(projectNo)) {
            criteria.andProjectNoEqualTo(projectNo);
        }

        List<ConstructionOrder> list = constructionOrderMapper.selectByExample(example);
        List<DecorationOrderListVo> listVo = new ArrayList<>();

        PageInfo pageInfo = new PageInfo<>(list);

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
        /* 用户编号List */
        List<Map<String, String>> listUserNo = new ArrayList<>();

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
        //预算报价
        List<ProjectQuotation> list6 = getPrice(listProjectNo);

        conOut:
        for (ConstructionOrder constructionOrder : list) {
            DecorationOrderListVo decorationOrderListVo = new DecorationOrderListVo();
            // 项目地址 & 预约日期
            for (Project project : list1) {
                if (constructionOrder.getProjectNo().equals(project.getProjectNo())) {
                    decorationOrderListVo.setAddressDetail(project.getAddressDetail());
                    decorationOrderListVo.setAppointmentTime(project.getCreateTime());
                    // 业主 & 手机号
                    Map<String, String> map = new HashMap<>();
                    map.put("userId", project.getOwnerId());
                    map.put("roleId", "CC");
                    listUserNo.add(map);
                    List<PersionVo>  owne = getOwnerId(listUserNo);
                    for (PersionVo persionVo : owne){
                        if (project.getOwnerId().equals(persionVo.getUserId())){
                            decorationOrderListVo.setOwner(persionVo.getName());
                            decorationOrderListVo.setPhone(persionVo.getPhone());
                        }
                    }
                }
            }
            // 预算报价
            for (ProjectQuotation projectQuotation : list6){
                if (constructionOrder.getProjectNo().equals(projectQuotation.getProjectNo())) {
                    decorationOrderListVo.setAppointmentPrice(projectQuotation.getTotalPrice());
                }
            }
            // 项目编号
            decorationOrderListVo.setProjectNo(constructionOrder.getProjectNo());
            // 订单编号
            decorationOrderListVo.setOrderNo(constructionOrder.getOrderNo());
            // 派单给员工-是否可以
            if (constructionOrder.getOrderStage().equals(ConstructionStateEnum.STATE_510.getState())) {
                decorationOrderListVo.setIsCheck(1);
            } else {
                decorationOrderListVo.setIsCheck(0);
            }
            // 订单状态
            if (!StringUtils.isBlank(orderStage)) {
                if (!orderStage.equals(ConstructionStateEnum.getNowStateInfo(constructionOrder.getOrderStage(), 2))) {
                    continue conOut;
                }
            }
            ConstructionStateEnum constructionState = constructionStateService.getState(constructionOrder.getOrderStage(), constructionOrder.getComplaintState());
            decorationOrderListVo.setOrderStage(constructionState.getStateName(ConstructOrderConstants.APP_TYPE_CONSTRUCT));
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

        return pageInfo;
    }


    /**
     * 查询项目信息
     *
     * @param listProjectNo
     * @return
     */
    @Override
    public List<Project> getProjectInfo(List<String> listProjectNo) {
        if(listProjectNo == null || listProjectNo.isEmpty()){
            return new ArrayList<>();
        }
        ProjectExample example = new ProjectExample();
        example.createCriteria().andProjectNoIn(listProjectNo);
        return projectMapper.selectByExample(example);
    }

    /**
     * 查询用户信息 -用户中心接口
     *
     * @param listUserNo
     * @return
     */
    @Override
    public List<PersionVo> getOwnerId (List<Map<String, String>> listUserNo) {
        if(listUserNo == null || listUserNo.isEmpty()){
            return new ArrayList<>();
        }
        HttpUtils.HttpRespMsg httpRespMsg = HttpUtils.postJson(getUrl(userCenterUrl), JSONObject.toJSONString(listUserNo));
        if (httpRespMsg.getResponseCode() != 200) {
            //用户中心服务异常
            throw new RuntimeException("用户中心异常");
        }
        JSONObject jsonObject = JSONObject.parseObject(httpRespMsg.getContent());
        if (!"1000".equals(jsonObject.getString("code"))) {
            throw new RuntimeException("无效的用户ID");
        }
        JSONObject dataObj = jsonObject.getJSONObject("data");
        List<PersionVo>  list = new ArrayList<>();
        for (Map<String, String> map: listUserNo){

            if (!dataObj.containsKey(map.get("userId"))) {
                continue;
            }
            JSONObject userMsg = dataObj.getJSONObject(map.get("userId"));
            PersionVo persionVo = new PersionVo();
            persionVo.setUserId(userMsg.getString("consumerId"));
            persionVo.setName(userMsg.getString("nickName"));
            persionVo.setPhone(userMsg.getString("phone"));
            list.add(persionVo);
        }
        return list;
    }

    /**
     * 设计师 & 项目经理
     *
     * @param listProjectNo
     * @return
     */
    @Override
    public List<Map<String, String>> getEmployeeInfo(List<String> listProjectNo, String role) {
        if(listProjectNo == null || listProjectNo.isEmpty()){
            return new ArrayList<>();
        }
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
    @Override
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
    @Override
    public String getContstructionStage(Integer stage) {
        if (null == stage){
            return "";
        }
        ProjectBigSchedulingExample example = new ProjectBigSchedulingExample();
        example.createCriteria().andSortEqualTo(stage);
        List<ProjectBigScheduling> list = projectBigSchedulingMapper.selectByExample(example);
        if(list == null || list.isEmpty()){
            return "";
        }
        return list.get(0).getName();
    }

    /**
     * 查询 延期天数/开工时间/竣工时间
     *
     * @param listProjectNo
     * @return
     */
    @Override
    public List<ProjectScheduling> getdelayDay(List<String> listProjectNo) {
        if(listProjectNo == null || listProjectNo.isEmpty()){
            return new ArrayList<>();
        }
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
    @Override
    public Map<String, Integer> getApprove(List<String> listProjectNo) {
        if(listProjectNo == null || listProjectNo.isEmpty()){
            return new HashMap<>();
        }
        return afInstanceService.getProjectCheckResult(listProjectNo);
    }

    /**
     * 查询 支付
     *
     * @param listProjectNo
     * @return
     */
    @Override
    public List<FundsOrder> getFundsOrder(List<String> listProjectNo) {
        if(listProjectNo == null || listProjectNo.isEmpty()){
            return new ArrayList<>();
        }
        FundsOrderExample example = new FundsOrderExample();
        example.createCriteria().andProjectNoIn(listProjectNo);
        List<FundsOrder> list = fundsOrderMapper.selectByExample(example);
        return list;
    }

    /**
     * 报价
     *
     * @param listProjectNo
     * @return
     */
    @Override
    public List<ProjectQuotation> getPrice(List<String> listProjectNo) {
        if(listProjectNo == null || listProjectNo.isEmpty()){
            return new ArrayList<>();
        }
        ProjectQuotationExample example = new ProjectQuotationExample();
        example.createCriteria().andProjectNoIn(listProjectNo);
        List<ProjectQuotation> list = projectQuotationMapper.selectByExample(example);
        return list;
    }

}
