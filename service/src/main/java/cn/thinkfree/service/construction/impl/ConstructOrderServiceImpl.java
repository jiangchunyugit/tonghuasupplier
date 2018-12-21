package cn.thinkfree.service.construction.impl;

import cn.thinkfree.core.base.RespData;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.*;
import cn.thinkfree.core.utils.JSONUtil;
import cn.thinkfree.database.appvo.ConstructionProjectVo;
import cn.thinkfree.database.mapper.*;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.vo.AfUserDTO;
import cn.thinkfree.database.vo.ConstructCountVO;
import cn.thinkfree.database.vo.construct.*;
import cn.thinkfree.service.approvalflow.AfConfigService;
import cn.thinkfree.service.approvalflow.AfInstanceService;
import cn.thinkfree.service.config.HttpLinks;
import cn.thinkfree.service.construction.CommonService;
import cn.thinkfree.service.construction.ConstructOrderService;
import cn.thinkfree.service.construction.OrderListCommonService;
import cn.thinkfree.service.construction.vo.ConsListVo;
import cn.thinkfree.service.construction.vo.ConstructionOrderListVo;
import cn.thinkfree.service.construction.vo.ConstructionOrderManageVo;
import cn.thinkfree.service.neworder.NewOrderUserService;
import cn.thinkfree.service.newscheduling.NewSchedulingBaseService;
import cn.thinkfree.service.newscheduling.NewSchedulingService;
import cn.thinkfree.service.platform.basics.BasicsService;
import cn.thinkfree.service.platform.designer.UserCenterService;
import cn.thinkfree.service.platform.employee.EmployeeService;
import cn.thinkfree.service.platform.vo.EmployeeMsgVo;
import cn.thinkfree.service.platform.vo.PageVo;
import cn.thinkfree.service.platform.vo.UserMsgVo;
import cn.thinkfree.service.project.AddressService;
import cn.thinkfree.service.project.ProjectService;
import cn.thinkfree.service.utils.AfUtils;
import cn.thinkfree.service.utils.DateUtil;
import cn.thinkfree.service.utils.HttpUtils;
import cn.thinkfree.service.utils.ReflectUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class ConstructOrderServiceImpl implements ConstructOrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConstructOrderServiceImpl.class);

    @Autowired
    private OrderListCommonService orderListCommonService;
    @Autowired
    private ConstructionOrderMapper constructionOrderMapper;
    @Autowired
    private CommonService commonService;
    @Autowired
    private AfConfigService configService;
    @Autowired
    private NewOrderUserService orderUserService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private HttpLinks httpLinks;
    @Autowired
    private NewSchedulingService schedulingService;
    @Autowired
    private NewSchedulingBaseService schedulingBaseService;
    @Autowired
    private AfInstanceService instanceService;
    @Autowired
    private UserCenterService userCenterService;
    @Autowired
    private OrderContractMapper orderContractMapper;
    @Autowired
    private CompanyInfoMapper companyInfoMapper;
    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private OrderUserMapper orderUserMapper;
    @Autowired
    private EmployeeMsgMapper employeeMsgMapper;
    @Autowired
    private BasicsService basicsService;
    @Autowired
    private EmployeeService employeeService;
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
     *
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
            if (stage == ConstructionStateEnum.STATE_530.getState()) {
                waitExamine++;
            }
            if (stage == ConstructionStateEnum.STATE_540.getState()) {
                waitSign++;
            }
            if ((stage >= ConstructionStateEnum.STATE_600.getState() && stage <= ConstructionStateEnum.STATE_700.getState())) {
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

    @Override
    public ConstructCountVO count(String userId, String approvalType, Integer pageNum, Integer pageSize) {
        ConstructCountVO constructCountVO = new ConstructCountVO();

        int total = constructionOrderMapper.countByUserId(userId);
        constructCountVO.setTotal(total);

        List<String> configNos = configService.getConfigNosByApprovalType(AfConstants.APPROVAL_TYPE_SCHEDULE_APPROVAL);
        int count = constructionOrderMapper.countByApproval(userId, configNos, ConstructionStateEnum.STATE_700.getState());
        constructCountVO.setCheckCount(count);

        configNos = configService.getConfigNosByApprovalType(AfConstants.APPROVAL_TYPE_CONSTRUCTION_CHANGE);
        count = constructionOrderMapper.countByApproval(userId, configNos, ConstructionStateEnum.STATE_700.getState());
        constructCountVO.setChangeCount(count);

        configNos = configService.getConfigNosByApprovalType(AfConstants.APPROVAL_TYPE_PROBLEM_RECTIFICATION);
        count = constructionOrderMapper.countByApproval(userId, configNos, ConstructionStateEnum.STATE_700.getState());
        constructCountVO.setProblemCount(count);

        configNos = configService.getConfigNosByApprovalType(AfConstants.APPROVAL_TYPE_DELAY_VERIFY);
        count = constructionOrderMapper.countByApproval(userId, configNos, ConstructionStateEnum.STATE_700.getState());
        constructCountVO.setDelayCount(count);

        configNos = configService.getConfigNosByApprovalType(approvalType);
        List<ConstructionProjectVo> constructionProjectVos = constructionOrderMapper.selectByApproval(userId, configNos, ConstructionStateEnum.STATE_700.getState(), (pageNum - 1) * pageSize, pageSize);
        PageInfo<ConstructionProjectVo> pageInfo = new PageInfo<>(constructionProjectVos);
        count = constructionOrderMapper.countByApproval(userId, configNos, ConstructionStateEnum.STATE_700.getState());
        pageInfo.setTotal(count);
        pageInfo.setPages((count % pageSize == 0) ? (count / pageSize) : (count / pageSize + 1));
        for (ConstructionProjectVo constructionProjectVo : constructionProjectVos) {
            constructionProjectVo.setStageName(ConstructionStateEnum.queryByState(constructionProjectVo.getStage()).getStateName(1));
        }
        constructCountVO.setPageInfo(pageInfo);

        return constructCountVO;
    }

    @Override
    public ConstructionOrder findByProjectNo(String projectNo) {
        ConstructionOrderExample example = new ConstructionOrderExample();
        example.createCriteria().andProjectNoEqualTo(projectNo);
        List<ConstructionOrder> constructionOrders = constructionOrderMapper.selectByExample(example);
        return constructionOrders != null && constructionOrders.size() > 0 ? constructionOrders.get(0) : null;
    }

    @Override
    public ConstructionOrder findByOrderNo(String orderNo) {
        ConstructionOrderExample example = new ConstructionOrderExample();
        example.createCriteria().andOrderNoEqualTo(orderNo);
        List<ConstructionOrder> constructionOrders = constructionOrderMapper.selectByExample(example);
        return constructionOrders != null && constructionOrders.size() > 0 ? constructionOrders.get(0) : null;
    }

    private String getOrderType(String type) {
        BasicsData basicsData = basicsService.queryDataOne(BasicsDataParentEnum.DESIGN_STYLE.getCode(), type);
        if (basicsData == null) {
            LOGGER.error("未查询到装饰风格:type:{}", type);
            throw new RuntimeException();
        }
        return basicsData.getBasicsName();
    }

    @Override
    public ConstructOrderDetailVO detail(String projectNo) {
        ConstructOrderDetailVO constructOrderDetailVO = new ConstructOrderDetailVO();

        ConstructionOrder constructionOrder = findByProjectNo(projectNo);
        Project project = projectService.findByProjectNo(projectNo);
        // 订单信息
        OrderDetailVO orderDetailVO = getOrderDetail(projectNo, constructionOrder.getOrderNo(), project.getStyle());
        constructOrderDetailVO.setOrderDetailVO(orderDetailVO);
        // 业主信息
        ConsumerDetailVO consumerDetailVO = getCustomerDetail(constructionOrder);
        String address = addressService.getAddress(project);
        consumerDetailVO.setProjectAddress(address);
        constructOrderDetailVO.setConsumerDetailVO(consumerDetailVO);

        ProjectScheduling projectScheduling = schedulingService.getProjectScheduling(projectNo).getData();
        // 施工信息
        String schemeNo = constructionOrder.getSchemeNo();
        ConstructDetailVO constructDetailVO = getConstructDetail(projectScheduling, schemeNo);
        constructOrderDetailVO.setConstructDetailVO(constructDetailVO);
        // 服务人员信息
        ServiceStaffsVO serviceStaffsVO = getServiceStaffs(projectNo);
        constructOrderDetailVO.setServiceStaffsVO(serviceStaffsVO);
        // 支付信息
        PayDetailVO payDetailVO = getPayDetail(projectNo);
        constructOrderDetailVO.setPayDetailVO(payDetailVO);
        // 延期信息
        DelayDetailVO delayDetailVO = getDelayDetail(projectScheduling, projectNo);
        constructOrderDetailVO.setDelayDetailVO(delayDetailVO);
        return constructOrderDetailVO;
    }

    private DelayDetailVO getDelayDetail(ProjectScheduling projectScheduling, String projectNo) {
        DelayDetailVO delayDetailVO = new DelayDetailVO();
        int delayDays = instanceService.getDelayDaysByProjectNo(projectNo);
        delayDetailVO.setAffirmDelayDays(delayDays);
        delayDetailVO.setDelayDays(projectScheduling.getDelay() + delayDays);
        return delayDetailVO;
    }

    private PayDetailVO getPayDetail(String projectNo) {
        PayDetailVO payDetailVO = new PayDetailVO();

        HttpUtils.HttpRespMsg httpRespMsg = HttpUtils.post(httpLinks.getProjectPayInfo(), Collections.singletonMap("projectNo", projectNo));
        String content = httpRespMsg.getContent();
        Map map = JSONUtil.json2Bean(content, Map.class);
        Map<String, String> data = (Map<String, String>) map.get("data");

        payDetailVO.setCompactMoney(data.get(ProjectPayInfoKey.CONSTRUCTION_TOTAL));
        payDetailVO.setPayedMoney(data.get(ProjectPayInfoKey.CONSTRUCTION_TOTAL_PAID));

        payDetailVO.setWaitPayMoney(new BigDecimal(data.get(ProjectPayInfoKey.CONSTRUCTION_TOTAL)).subtract(new BigDecimal(data.get(ProjectPayInfoKey.CONSTRUCTION_TOTAL_PAID))).toString());
        return payDetailVO;
    }

    private ServiceStaffsVO getServiceStaffs(String projectNo) {
        ServiceStaffsVO serviceStaffsVO = new ServiceStaffsVO();

        List<OrderUser> orderUsers = orderUserService.findByProjectNo(projectNo);
        if (orderUsers == null || orderUsers.isEmpty()) {
            LOGGER.error("未查询到项目用户关系信息，projectNo:{}", projectNo);
            throw new RuntimeException();
        }

        for (OrderUser orderUser : orderUsers) {
            switch (orderUser.getRoleCode()) {
                case "CP":
                    AfUserDTO userInfo = getUserInfo(orderUser.getUserId(), orderUser.getRoleCode());
                    if (userInfo == null) {
                        LOGGER.error("未查询到项目经理信息，userId:{}", orderUser.getUserId());
                        throw new RuntimeException();
                    }
                    serviceStaffsVO.setProjectManager(userInfo.getUsername());
                    break;
                case "CM":
                    userInfo = getUserInfo(orderUser.getUserId(), orderUser.getRoleCode());
                    if (userInfo == null) {
                        LOGGER.error("未查询到工长信息，userId:{}", orderUser.getUserId());
                        throw new RuntimeException();
                    }
                    serviceStaffsVO.setForeman(userInfo.getUsername());
                    break;
                case "CS":
                    userInfo = getUserInfo(orderUser.getUserId(), orderUser.getRoleCode());
                    if (userInfo == null) {
                        LOGGER.error("未查询到管家信息，userId:{}", orderUser.getUserId());
                        throw new RuntimeException();
                    }
                    serviceStaffsVO.setSteward(userInfo.getUsername());
                    break;
                case "CD":
                    userInfo = getUserInfo(orderUser.getUserId(), orderUser.getRoleCode());
                    if (userInfo == null) {
                        LOGGER.error("未查询到设计师信息，userId:{}", orderUser.getUserId());
                        throw new RuntimeException();
                    }
                    serviceStaffsVO.setDesigner(userInfo.getUsername());
                    break;
                default:
                    break;
            }
        }

        return serviceStaffsVO;
    }

    private AfUserDTO getUserInfo(String userId, String roleId) {
        AfUserDTO userDTO;
        if (Role.CC.id.equals(roleId)) {
            userDTO = AfUtils.getUserInfo(httpLinks.getUserCenterGetUserMsg(), userId, roleId);
        } else {
            userDTO = new AfUserDTO();
            EmployeeMsgVo employeeMsg = employeeService.employeeMsgById(userId);
            if (employeeMsg == null) {
                LOGGER.error("未查询到用户信息：userId:{}", userId);
                throw new RuntimeException();
            }
            userDTO.setHeadPortrait(employeeMsg.getIconUrl());
            userDTO.setUsername(employeeMsg.getRealName());
            userDTO.setUserId(userId);
            userDTO.setPhone(employeeMsg.getPhone());
        }
        return userDTO;
    }

    private ConstructDetailVO getConstructDetail(ProjectScheduling projectScheduling, String schemeNo) {
        ConstructDetailVO constructDetailVO = new ConstructDetailVO();
        constructDetailVO.setStartDate(projectScheduling.getStartTime().getTime());
        constructDetailVO.setCompleteDate(projectScheduling.getEndTime().getTime());
        constructDetailVO.setDelayDays(projectScheduling.getDelay());
        constructDetailVO.setLimitDays(DateUtil.differentDaysByMillisecond(projectScheduling.getStartTime(), projectScheduling.getEndTime()));

        ProjectBigScheduling projectBigScheduling = schedulingBaseService.findBySchemeNoAndSort(schemeNo, projectScheduling.getRate());
        if (projectBigScheduling == null) {
            LOGGER.error("未查询到排期信息，schemeNo:{}, sort:{}", schemeNo, projectScheduling.getRate());
            throw new RuntimeException();
        }
        constructDetailVO.setConstructProgress(projectBigScheduling.getName());
        return constructDetailVO;
    }

    private ConsumerDetailVO getCustomerDetail(ConstructionOrder constructionOrder) {
        ConsumerDetailVO consumerDetailVO = new ConsumerDetailVO();

        OrderUser orderUser = orderUserService.findByProjectNoAndRoleId(constructionOrder.getProjectNo(), Role.CC.id);
        if (orderUser == null) {
            LOGGER.error("项目未配置业主，projectNo:{}", constructionOrder.getProjectNo());
            throw new RuntimeException();
        }
        AfUserDTO userInfo = AfUtils.getUserInfo(httpLinks.getUserCenterGetUserMsg(), orderUser.getUserId(), Role.CC.id);
        if (userInfo == null) {
            LOGGER.error("未查询到业主信息，userId:{}", orderUser.getUserId());
            throw new RuntimeException();
        }
        consumerDetailVO.setName(userInfo.getUsername());
        consumerDetailVO.setPhone(userInfo.getPhone());

        return consumerDetailVO;
    }

    private OrderDetailVO getOrderDetail(String projectNo, String orderNo, String style) {
        OrderDetailVO orderDetailVO = new OrderDetailVO();
        orderDetailVO.setProjectNo(projectNo);
        orderDetailVO.setOrderNo(orderNo);
        String orderType = getOrderType(style);
        orderDetailVO.setOrderType(orderType);
        return orderDetailVO;
    }
    @Override
    public PageVo<List<ConsListVo>> getConsList(
            String projectNo, String companyName, String provinceCode, String cityCode, String areaCode, String createTimeS, String createTimeE,
            String againTimeS, String againTimeE, String address, String ownerName, String ownerPhone, int pageNum, int pageSize) {
        List<String> userProjectNos = searchOwner(ownerName, ownerPhone);
        if(userProjectNos != null && userProjectNos.isEmpty()){
            return PageVo.def(new ArrayList<>());
        }
        List<String> orderNos = searchContract(againTimeS, againTimeE);
        if(orderNos != null && orderNos.isEmpty()){
            return PageVo.def(new ArrayList<>());
        }
        List<String> companyIds = searchCompany(companyName);
        if(companyIds != null && companyIds.isEmpty()){
            return PageVo.def(new ArrayList<>());
        }
        List<String> projectNos = searchProject(projectNo, provinceCode, cityCode, areaCode, createTimeS, createTimeE, address);
        if(projectNos != null && projectNos.isEmpty()){
            return PageVo.def(new ArrayList<>());
        }
        ConstructionOrderExample constructionOrderExample = new ConstructionOrderExample();
        ConstructionOrderExample.Criteria criteria = constructionOrderExample.createCriteria();
        if(userProjectNos != null && !userProjectNos.isEmpty()){
            criteria.andProjectNoIn(userProjectNos);
        }
        if(projectNos != null && !projectNos.isEmpty()){
            criteria.andProjectNoIn(projectNos);
        }
        if(orderNos != null && !orderNos.isEmpty()){
            criteria.andOrderNoIn(orderNos);
        }
        if(companyIds != null && !companyIds.isEmpty()){
            criteria.andCompanyIdIn(companyIds);
        }
        long total = constructionOrderMapper.countByExample(constructionOrderExample);
        PageHelper.startPage(pageNum, pageSize);
        List<ConstructionOrder> constructionOrders = constructionOrderMapper.selectByExample(constructionOrderExample);
        if(constructionOrders.isEmpty()){
            return PageVo.def(new ArrayList<>());
        }
        projectNos = ReflectUtils.getList(constructionOrders,"projectNo");
        companyIds = ReflectUtils.getList(constructionOrders,"companyId");
        orderNos = ReflectUtils.getList(constructionOrders,"orderNo");
        OrderContractExample orderContractExample = new OrderContractExample();
        orderContractExample.createCriteria().andOrderNumberIn(orderNos);
        List<OrderContract> orderContracts = orderContractMapper.selectByExample(orderContractExample);
        Map<String,OrderContract> orderContractMap = ReflectUtils.listToMap(orderContracts,"orderNumber");
        CompanyInfoExample companyInfoExample = new CompanyInfoExample();
        companyInfoExample.createCriteria().andCompanyIdIn(companyIds);
        List<CompanyInfo> companyInfos = companyInfoMapper.selectByExample(companyInfoExample);
        Map<String,CompanyInfo> companyInfoMap = ReflectUtils.listToMap(companyInfos,"companyId");
        Map<String, String[]> userMsgMap = getStaffBy(projectNos);
        ProjectExample projectExample = new ProjectExample();
        projectExample.createCriteria().andProjectNoIn(projectNos);
        List<Project> projects = projectMapper.selectByExample(projectExample);
        Map<String,String> provinceMap = basicsService.getProvince(ReflectUtils.getList(projects,"province").toArray(new String[]{}));
        Map<String,String> cityMap = basicsService.getCity(ReflectUtils.getList(projects,"city").toArray(new String[]{}));
        Map<String,String> areaMap = basicsService.getArea(ReflectUtils.getList(projects,"region").toArray(new String[]{}));
        Map<String,Project> projectMap = ReflectUtils.listToMap(projects, "projectNo");
        List<ConsListVo> consListVos = new ArrayList<>();
        for(ConstructionOrder constructionOrder : constructionOrders){
            Project project = projectMap.get(constructionOrder.getProjectNo());
            ConsListVo consListVo = new ConsListVo();
            if(project != null){
                consListVo.setAddress(project.getAddressDetail());
                consListVo.setAreaName(areaMap.get(project.getRegion()));
                consListVo.setCityName(cityMap.get(project.getCity()));
                if(project.getCreateTime() != null){
                    consListVo.setCreateTime(project.getCreateTime().getTime() + "");
                }
                consListVo.setProjectNo(project.getProjectNo());
                consListVo.setProvinceName(provinceMap.get(project.getProvince()));
                consListVo.setStateName(ConstructionStateEnum.queryByState(constructionOrder.getOrderStage()).getStateName(1));
            }
            String[] userMsg = userMsgMap.get("CD-" + constructionOrder.getProjectNo());
            if(userMsg != null){
                consListVo.setCdName(userMsg[0]);
            }
            userMsg = userMsgMap.get("CM-" + constructionOrder.getProjectNo());
            if(userMsg != null){
                consListVo.setCmName(userMsg[0]);
            }
            userMsg = userMsgMap.get("CS-" + constructionOrder.getProjectNo());
            if(userMsg != null){
                consListVo.setCsName(userMsg[0]);
            }
            userMsg = userMsgMap.get("CP-" + constructionOrder.getProjectNo());
            if(userMsg != null){
                consListVo.setCpName(userMsg[0]);
            }
            userMsg = userMsgMap.get("CC-" + constructionOrder.getProjectNo());
            if(userMsg != null){
                consListVo.setOwnerName(userMsg[0]);
                consListVo.setOwnerPhone(userMsg[1]);
                consListVo.setOwnerId(userMsg[2]);
            }
            consListVo.setOrderNo(constructionOrder.getOrderNo());
            CompanyInfo companyInfo = companyInfoMap.get(constructionOrder.getCompanyId());
            if(companyInfo != null){
                consListVo.setCompanyName(companyInfo.getCompanyName());
            }
            OrderContract orderContract = orderContractMap.get(constructionOrder.getOrderNo());
            if(orderContract != null && orderContract.getSignTime() != null){
                consListVo.setSignTime(orderContract.getSignTime().getTime() + "");
            }
            consListVos.add(consListVo);
        }
        PageVo<List<ConsListVo>> pageVo = PageVo.def(consListVos);
        pageVo.setPageSize(pageSize);
        pageVo.setPageIndex(pageNum);
        pageVo.setTotal(total);
        return pageVo;
    }

    /**
     * 获取该项目下的所有员工
     * @param projectNos
     * @return
     */
    private Map<String, String[]> getStaffBy(List<String> projectNos){
        OrderUserExample orderUserExample = new OrderUserExample();
        orderUserExample.createCriteria().andProjectNoIn(projectNos).andIsTransferEqualTo(Short.parseShort("0"));
        List<OrderUser> orderUsers = orderUserMapper.selectByExample(orderUserExample);
        if(orderUsers.isEmpty()){
            return new HashMap<>();
        }
        List<String> userIds = ReflectUtils.getList(orderUsers,"userId");
        List<UserMsgVo> userMsgVos = userCenterService.queryUsers(userIds);
        Map<String,UserMsgVo> userMsgVoMap = ReflectUtils.listToMap(userMsgVos,"consumerId");
        EmployeeMsgExample msgExample = new EmployeeMsgExample();
        msgExample.createCriteria().andUserIdIn(userIds);
        List<EmployeeMsg> employeeMsgs = employeeMsgMapper.selectByExample(msgExample);
        Map<String,String> employeeNameMap = ReflectUtils.listToMap(employeeMsgs,"userId","realName");
        Map<String, String[]> userNameMap = new HashMap<>();
        for(OrderUser orderUser : orderUsers){
            String userId = orderUser.getUserId();
            String roleCode = orderUser.getRoleCode();
            String projectNo = orderUser.getProjectNo();
            String employeeName = employeeNameMap.get(userId);
            if(StringUtils.isNotBlank(employeeName)){
                userNameMap.put(roleCode + "-" + projectNo, new String[]{employeeName,"", orderUser.getUserId()});
                continue;
            }
            UserMsgVo userMsgVo = userMsgVoMap.get(userId);
            if(userMsgVo == null){
                continue;
            }
            userNameMap.put(roleCode + "-" + projectNo, new String[]{userMsgVo.getUserName(),userMsgVo.getUserPhone(), orderUser.getUserId()});
        }
        return userNameMap;
    }

    /**
     * 搜索业主
     * @param ownerName
     * @param ownerPhone
     * @return
     */
    private List<String> searchOwner(String ownerName, String ownerPhone){
        if(StringUtils.isBlank(ownerName) && StringUtils.isBlank(ownerPhone)){
            return null;
        }
        List<UserMsgVo> userMsgVos = userCenterService.queryUserMsg(ownerName, ownerPhone);
        if(userMsgVos == null || userMsgVos.isEmpty()){
            return new ArrayList<>();
        }
        List<String> userIds = (ReflectUtils.getList(userMsgVos, "consumerId"));
        OrderUserExample orderUserExample = new OrderUserExample();
        orderUserExample.createCriteria().andUserIdIn(userIds).andRoleCodeEqualTo("CC");
        List<OrderUser> orderUsers = orderUserMapper.selectByExample(orderUserExample);
        return (ReflectUtils.getList(orderUsers, "projectNo"));
    }

    /**
     * 搜索合同
     * @param againTimeS
     * @param againTimeE
     * @return
     */
    private List<String> searchContract(String againTimeS, String againTimeE) {
        if(StringUtils.isBlank(againTimeS) && StringUtils.isBlank(againTimeE)){
            return null;
        }
        OrderContractExample orderContractExample = new OrderContractExample();
        OrderContractExample.Criteria criteria = orderContractExample.createCriteria();
        if (StringUtils.isNotBlank(againTimeS)) {
            criteria.andSignTimeGreaterThanOrEqualTo(DateUtil.formateToDate(againTimeS, DateUtil.FORMAT_YYMMDD));
        }
        if (StringUtils.isNotBlank(againTimeE)) {
            criteria.andSignTimeLessThan(DateUtil.formateToDate(againTimeS, DateUtil.FORMAT_YYMMDD));
        }
        List<OrderContract> orderContracts = orderContractMapper.selectByExample(orderContractExample);
        return ReflectUtils.getList(orderContracts, "orderNumber");
    }

    /**
     * 搜索公司
     * @param companyName
     * @return
     */
    private List<String> searchCompany(String companyName) {
        if (StringUtils.isBlank(companyName)) {
            return null;
        }
        CompanyInfoExample companyInfoExample = new CompanyInfoExample();
        companyInfoExample.createCriteria().andCompanyNameLike("%" + companyName + "%");
        List<CompanyInfo> companyInfos = companyInfoMapper.selectByExample(companyInfoExample);
        return ReflectUtils.getList(companyInfos, "companyId");
    }

    /**
     * 搜索项目
     * @param projectNo
     * @param provinceCode
     * @param cityCode
     * @param areaCode
     * @param createTimeS
     * @param createTimeE
     * @param address
     * @return
     */
    private List<String> searchProject(String projectNo, String provinceCode, String cityCode, String areaCode, String createTimeS, String createTimeE, String address) {
        ProjectExample projectExample = new ProjectExample();
        ProjectExample.Criteria criteria = projectExample.createCriteria();
        boolean isWhere = false;
        if (StringUtils.isNotBlank(projectNo)) {
            criteria.andProjectNoLike("%" + projectNo + "%");
            isWhere = true;
        }
        if (StringUtils.isNotBlank(provinceCode)) {
            criteria.andProvinceEqualTo(provinceCode);
            isWhere = true;
        }
        if (StringUtils.isNotBlank(cityCode)) {
            criteria.andCityEqualTo(cityCode);
            isWhere = true;
        }
        if (StringUtils.isNotBlank(areaCode)) {
            criteria.andRegionEqualTo(areaCode);
            isWhere = true;
        }
        if (StringUtils.isNotBlank(address)) {
            criteria.andAddressDetailLike("%" + address + "%");
            isWhere = true;
        }
        if (StringUtils.isNotBlank(createTimeS)) {
            criteria.andCreateTimeGreaterThanOrEqualTo(DateUtil.formateToDate(createTimeS, DateUtil.FORMAT_YYMMDD));
            isWhere = true;
        }
        if (StringUtils.isNotBlank(createTimeE)) {
            criteria.andCreateTimeLessThanOrEqualTo(DateUtil.formateToDate(createTimeE, DateUtil.FORMAT_YYMMDD));
            isWhere = true;
        }
        if (!isWhere) {
            return null;
        }
        List<Project> projects = projectMapper.selectByExample(projectExample);
        return ReflectUtils.getList(projects, "projectNo");
    }}