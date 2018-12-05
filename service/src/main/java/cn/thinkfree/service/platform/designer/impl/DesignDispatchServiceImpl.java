package cn.thinkfree.service.platform.designer.impl;

import cn.thinkfree.core.base.RespData;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ConstructionStateEnum;
import cn.thinkfree.core.constants.DesignStateEnum;
import cn.thinkfree.core.constants.ProjectSource;
import cn.thinkfree.core.constants.RoleFunctionEnum;
import cn.thinkfree.core.utils.JSONUtil;
import cn.thinkfree.database.appvo.*;
import cn.thinkfree.database.mapper.*;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.vo.CompanyInfoVo;
import cn.thinkfree.database.vo.VolumeReservationDetailsVO;
import cn.thinkfree.service.config.HttpLinks;
import cn.thinkfree.service.constants.ProjectDataStatus;
import cn.thinkfree.service.construction.ConstructionAndPayStateService;
import cn.thinkfree.service.neworder.NewOrderUserService;
import cn.thinkfree.service.platform.basics.BasicsService;
import cn.thinkfree.service.platform.build.BuildConfigService;
import cn.thinkfree.service.platform.designer.CreatePayOrderService;
import cn.thinkfree.service.platform.designer.DesignDispatchService;
import cn.thinkfree.service.platform.designer.DesignerService;
import cn.thinkfree.service.platform.designer.UserCenterService;
import cn.thinkfree.service.platform.employee.ProjectUserService;
import cn.thinkfree.service.platform.vo.*;
import cn.thinkfree.service.utils.*;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author xusonghui
 * 设计派单服务实现类
 */
@Service
public class DesignDispatchServiceImpl implements DesignDispatchService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DesignDispatchServiceImpl.class);

    @Autowired
    private OptionLogMapper optionLogMapper;
    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private DesignerOrderMapper DesignerOrderMapper;
    @Autowired
    private DesignerMsgMapper designerMsgMapper;
    @Autowired
    private EmployeeMsgMapper employeeMsgMapper;
    @Autowired
    private RemindOwnerLogMapper remindOwnerLogMapper;
    @Autowired
    private BasicsService basicsService;
    @Autowired
    private ConstructionOrderMapper constructionOrderMapper;
    @Autowired
    private UserCenterService userService;
    @Autowired
    private ProjectStageLogMapper stageLogMapper;
    @Autowired
    private ProjectUserService projectUserService;
    @Autowired
    private DesignerService designerService;
    @Autowired
    private CompanyInfoMapper companyInfoMapper;
    @Autowired
    private OrderUserMapper orderUserMapper;
    @Autowired
    private CreatePayOrderService createPayOrderService;
    @Autowired
    private BuildConfigService buildConfigService;
    @Autowired
    private ConstructionAndPayStateService constructionAndPayStateService;
    @Autowired
    NewOrderUserService newOrderUserService;
    @Autowired
    private HttpLinks httpLinks;

    /**
     * 查询设计订单，主表为design_order,附表为project
     *
     * @param queryStage         具体查询阶段
     * @param orderTpye
     * @param projectNo          订单编号（project.project_no）
     * @param userMsg            业主姓名或电话（调用用户中心查询获取userId）
     * @param orderSource        订单来源（project.order_source）
     * @param createTimeStart    创建时间开始（design_order.create_time）
     * @param createTimeEnd      创建时间结束（design_order.create_time）
     * @param styleCode          装饰风格（design_order.style_type）
     * @param money              装修预算（project.decoration_budget）
     * @param acreage            建筑面积（project.area）
     * @param designerOrderState 订单状态（design_order.order_stage）
     * @param companyState       公司状态（根据状态查询公司）
     * @param optionUserName     操作人姓名（option_log.option_user_name）
     * @param optionTimeStart    操作时间开始（option_log.option_time）
     * @param optionTimeEnd      操作时间结束（option_log.option_time）
     * @param stateType          状态类型
     * @return
     */
    @Override
    public PageVo<List<DesignerOrderVo>> queryDesignerOrder(
            String queryStage, Integer orderTpye, String companyId, String projectNo, String userMsg, String orderSource, String createTimeStart, String createTimeEnd,
            String styleCode, String money, String acreage, int designerOrderState, int companyState, String optionUserName,
            String optionTimeStart, String optionTimeEnd, int pageSize, int pageIndex, int stateType) {
        if (orderTpye == null) {
            throw new RuntimeException("请输入订单类别");
        }
        // 模糊查询用户信息
        List<String> queryProjectNo = queryProjectNos(userMsg);
        // 根据公司状态查询公司ID
        List<String> companyIds = queryCompanyIds(companyState);
        List<String> projectNos = getProjectNos(projectNo, orderSource, money, queryProjectNo);
        DesignerOrderExample orderExample = new DesignerOrderExample();
        DesignerOrderExample.Criteria orderExampleCriteria = orderExample.createCriteria();
        if(orderTpye == 1){
            orderExampleCriteria.andOrderStageEqualTo(DesignStateEnum.STATE_10.getState()).andCompanyIdEqualTo(companyId);
        }
        if(orderTpye == 2){
            orderExampleCriteria.andOrderStageGreaterThan(DesignStateEnum.STATE_30.getState());
        }
        if (StringUtils.isNotBlank(createTimeStart)) {
            orderExampleCriteria.andCreateTimeGreaterThanOrEqualTo(DateUtils.strToDate(createTimeStart));
        }
        if (StringUtils.isNotBlank(createTimeEnd)) {
            orderExampleCriteria.andCreateTimeLessThanOrEqualTo(DateUtils.strToDate(createTimeEnd));
        }
        if (StringUtils.isNotBlank(styleCode)) {
            orderExampleCriteria.andStyleTypeEqualTo(styleCode);
        }
        if (designerOrderState > 0) {
            orderExampleCriteria.andOrderStageIn(DesignStateEnum.queryStatesByState(designerOrderState, stateType));
        }
        if ("DOCL".equals(queryStage)) {
            List<Integer> orderStates = Arrays.asList(DesignStateEnum.STATE_130.getState());
            orderExampleCriteria.andOrderStageIn(orderStates);
        }
        if (!projectNos.isEmpty()) {
            orderExampleCriteria.andProjectNoIn(projectNos);
        }
        if (!companyIds.isEmpty()) {
            orderExampleCriteria.andCompanyIdIn(companyIds);
        }
        long total = DesignerOrderMapper.countByExample(orderExample);
        PageHelper.startPage(pageIndex, pageSize);
        List<DesignerOrder> designerOrders = DesignerOrderMapper.selectByExample(orderExample);
        if (designerOrders.isEmpty()) {
            return PageVo.def(new ArrayList<>());
        }
        List<DesignerOrderVo> DesignerOrderVos = new ArrayList<>();
        Map<String, DesignerStyleConfigVo> designerStyleConfigMap = queryDesignerStyleConfig();
        projectNos.clear();
        projectNos = ReflectUtils.getList(designerOrders, "projectNo");
        companyIds.clear();
        companyIds = ReflectUtils.getList(designerOrders, "companyId");
        OrderUserExample userExample = new OrderUserExample();
        userExample.createCriteria().andProjectNoIn(projectNos);
        List<OrderUser> orderUsers = orderUserMapper.selectByExample(userExample);
        List<String> userIds = ReflectUtils.getList(orderUsers, "userId");
        Map<String, CompanyInfo> companyInfoMap = getCompanyByIds(companyIds);
        Map<String, UserMsgVo> msgVoMap = userService.queryUserMap(userIds);
        Map<String, Project> projectMap = getProjectMap(projectNos);
        for (DesignerOrder designerOrder : designerOrders) {
            CompanyInfo companyInfo = companyInfoMap.get(designerOrder.getCompanyId());
            Project project = projectMap.get(designerOrder.getProjectNo());
            DesignerOrderVo DesignerOrderVo = getDesignerOrderVo(companyInfo, stateType, designerStyleConfigMap, designerOrder, project, msgVoMap);
            DesignerOrderVos.add(DesignerOrderVo);
        }
        PageVo<List<DesignerOrderVo>> pageVo = new PageVo<>();
        pageVo.setPageSize(pageSize);
        pageVo.setTotal(total);
        pageVo.setData(DesignerOrderVos);
        pageVo.setPageIndex(pageIndex);
        return pageVo;
    }

    private Map<String, Project> getProjectMap(List<String> projectNos) {
        if (projectNos == null || projectNos.isEmpty()) {
            return new HashMap<>();
        }
        ProjectExample projectExample = new ProjectExample();
        projectExample.createCriteria().andProjectNoIn(projectNos);
        List<Project> projects = projectMapper.selectByExample(projectExample);
        return ReflectUtils.listToMap(projects, "projectNo");
    }

    @Override
    public PageVo<List<DesignerOrderVo>> queryDesignerOrderByCompanyId(
            String queryStage, Integer orderTpye, String companyId, String projectNo, String userMsg, String orderSource, String createTimeStart, String createTimeEnd,
            String styleCode, String money, String acreage, int designerOrderState, String optionUserName,
            String optionTimeStart, String optionTimeEnd, int pageSize, int pageIndex, int stateType) {
        if (StringUtils.isBlank(companyId)) {
            throw new RuntimeException("公司缺失");
        }
        if (orderTpye == null) {
            throw new RuntimeException("请输入订单类别");
        }
        // 模糊查询用户信息
        List<String> queryProjectNo = queryProjectNos(userMsg);
        // 根据公司状态查询公司ID
        List<String> projectNos = getProjectNos(projectNo, orderSource, money, queryProjectNo);
        DesignerOrderExample orderExample = new DesignerOrderExample();
        DesignerOrderExample.Criteria orderExampleCriteria = orderExample.createCriteria();
        if(orderTpye == 1){
            orderExampleCriteria.andOrderStageEqualTo(DesignStateEnum.STATE_10.getState()).andCompanyIdEqualTo(companyId);
        }
        if(orderTpye == 2){
            orderExampleCriteria.andOrderStageGreaterThan(DesignStateEnum.STATE_30.getState());
        }
        if (StringUtils.isNotBlank(createTimeStart)) {
            orderExampleCriteria.andCreateTimeGreaterThanOrEqualTo(DateUtils.strToDate(createTimeStart));
        }
        if (StringUtils.isNotBlank(createTimeEnd)) {
            orderExampleCriteria.andCreateTimeLessThanOrEqualTo(DateUtils.strToDate(createTimeEnd));
        }
        if (StringUtils.isNotBlank(styleCode)) {
            orderExampleCriteria.andStyleTypeEqualTo(styleCode);
        }
        if (designerOrderState > 0) {
            orderExampleCriteria.andOrderStageIn(DesignStateEnum.queryStatesByState(designerOrderState, stateType));
        }
        if ("DOCL".equals(queryStage)) {
            List<Integer> orderStates = Arrays.asList(DesignStateEnum.STATE_130.getState());
            orderExampleCriteria.andOrderStageIn(orderStates);
        }
        if (!projectNos.isEmpty()) {
            orderExampleCriteria.andProjectNoIn(projectNos);
        }
        orderExampleCriteria.andCompanyIdEqualTo(companyId);
        long total = DesignerOrderMapper.countByExample(orderExample);
        PageHelper.startPage(pageIndex, pageSize);
        List<DesignerOrder> designerOrders = DesignerOrderMapper.selectByExample(orderExample);
        if (designerOrders.isEmpty()) {
            return PageVo.def(new ArrayList<>());
        }
        List<DesignerOrderVo> DesignerOrderVos = new ArrayList<>();
        Map<String, DesignerStyleConfigVo> designerStyleConfigMap = queryDesignerStyleConfig();
        List<String> companyIds = new ArrayList<>();
        companyIds.add(companyId);
        companyIds = ReflectUtils.getList(designerOrders, "companyId");
        projectNos.clear();
        projectNos = ReflectUtils.getList(designerOrders, "projectNo");
        OrderUserExample userExample = new OrderUserExample();
        userExample.createCriteria().andProjectNoIn(projectNos);
        List<OrderUser> orderUsers = orderUserMapper.selectByExample(userExample);
        List<String> userIds = ReflectUtils.getList(orderUsers, "userId");
        Map<String, CompanyInfo> companyInfoMap = getCompanyByIds(companyIds);
        Map<String, UserMsgVo> msgVoMap = userService.queryUserMap(userIds);
        Map<String, Project> projectMap = getProjectMap(projectNos);
        for (DesignerOrder designerOrder : designerOrders) {
            CompanyInfo companyInfo = companyInfoMap.get(designerOrder.getCompanyId());
            Project project = projectMap.get(designerOrder.getProjectNo());
            DesignerOrderVo DesignerOrderVo = getDesignerOrderVo(companyInfo, stateType, designerStyleConfigMap, designerOrder, project, msgVoMap);
            DesignerOrderVos.add(DesignerOrderVo);
        }
        PageVo<List<DesignerOrderVo>> pageVo = new PageVo<>();
        pageVo.setPageSize(pageSize);
        pageVo.setTotal(total);
        pageVo.setData(DesignerOrderVos);
        pageVo.setPageIndex(pageIndex);
        return pageVo;
    }

    private List<String> getProjectNos(String projectNo, String orderSource, String money, List<String> queryProjectNo) {
        ProjectExample projectExample = new ProjectExample();
        ProjectExample.Criteria projectCriteria = projectExample.createCriteria();
        int conditionNum = 0;
        if (StringUtils.isNotBlank(orderSource)) {
            projectCriteria.andOrderSourceEqualTo(Integer.parseInt(orderSource));
            conditionNum++;
        }
        if (StringUtils.isNotBlank(projectNo)) {
            projectCriteria.andProjectNoLike(projectNo);
            conditionNum++;
        }
        if (StringUtils.isNotBlank(money)) {
            projectCriteria.andDecorationBudgetEqualTo(Integer.parseInt(money));
            conditionNum++;
        }
        if (!queryProjectNo.isEmpty()) {
            projectCriteria.andProjectNoIn(queryProjectNo);
            conditionNum++;
        }
        if (conditionNum == 0) {
            return new ArrayList<>();
        }
        List<Project> projects = projectMapper.selectByExample(projectExample);
        Map<String, Project> projectMap = ReflectUtils.listToMap(projects, "projectNo");
        List<String> projectNos = ReflectUtils.getList(projects, "projectNo");
        return projectNos;
    }

    private List<String> queryProjectNos(String userMsg) {
        if (StringUtils.isBlank(userMsg)) {
            return new ArrayList<>();
        }
        List<UserMsgVo> msgVos = userService.queryUserMsg(userMsg);
        if (msgVos.isEmpty()) {
            return new ArrayList<>();
        }
        List<String> userIds = ReflectUtils.getList(msgVos, "userId");
        OrderUserExample userExample = new OrderUserExample();
        userExample.createCriteria().andUserIdIn(userIds);
        List<OrderUser> userList = orderUserMapper.selectByExample(userExample);
        return ReflectUtils.getList(userList, "projectNo");
    }

    /**
     * @param companyState 0入驻中 1资质待审核 2资质审核通过 3资质审核不通过4财务审核中5财务审核成功6财务审核失败7待交保证金8入驻成功
     * @return
     */
    private List<String> queryCompanyIds(int companyState) {
        if (companyState < 0) {
            return new ArrayList<>();
        }
        CompanyInfoExample infoExample = new CompanyInfoExample();
        CompanyInfoExample.Criteria criteria = infoExample.createCriteria();
        if (companyState > 0) {
            criteria.andAuditStatusEqualTo(companyState + "");
        }
        List<CompanyInfo> companyInfos = companyInfoMapper.selectByExample(infoExample);
        return ReflectUtils.getList(companyInfos, "companyId");
    }

    /**
     * 根据公司ID查询公司信息
     *
     * @param companyIds
     * @return
     */
    private Map<String, CompanyInfo> getCompanyByIds(List<String> companyIds) {
        if (companyIds == null || companyIds.isEmpty()) {
            return new HashMap<>();
        }
        CompanyInfoExample infoExample = new CompanyInfoExample();
        CompanyInfoExample.Criteria criteria = infoExample.createCriteria();
        criteria.andCompanyIdIn(companyIds);
        List<CompanyInfo> companyInfos = companyInfoMapper.selectByExample(infoExample);
        return ReflectUtils.listToMap(companyInfos, "companyId");
    }

    /**
     * 返回一个设计订单详情
     *
     * @param stateType              1获取平台状态，2获取设计公司状态，3获取设计师状态，4获取消费者状态
     * @param designerStyleConfigMap
     * @param DesignerOrder          设计订单信息
     * @param project                项目信息
     * @param msgVoMap               用户信息
     * @return
     */
    private DesignerOrderVo getDesignerOrderVo(
            CompanyInfo companyInfo, int stateType, Map<String, DesignerStyleConfigVo> designerStyleConfigMap, DesignerOrder DesignerOrder,
            Project project, Map<String, UserMsgVo> msgVoMap) {
        DesignerOrderVo DesignerOrderVo = new DesignerOrderVo();
        DesignerOrderVo.setProjectNo(project.getProjectNo());
        DesignerOrderVo.setDesignOrderNo(DesignerOrder.getOrderNo());
        String ownerId = projectUserService.queryUserIdOne(project.getProjectNo(), RoleFunctionEnum.OWNER_POWER);
        UserMsgVo ownerMsg = msgVoMap.get(ownerId);
        if (ownerMsg != null) {
            DesignerOrderVo.setOwnerName(ownerMsg.getUserName());
            DesignerOrderVo.setOwnerPhone(ownerMsg.getUserPhone());
        }
        DesignerOrderVo.setAddress(project.getAddressDetail());
        try {
            DesignerOrderVo.setOrderSource(ProjectSource.queryByState(project.getOrderSource()).getSourceName());
        } catch (Exception e) {
            DesignerOrderVo.setOrderSource("未知");
        }
        DesignerOrderVo.setCreateTime(DateUtils.dateToStr(project.getCreateTime()));
        DesignerStyleConfigVo designerStyleConfig = designerStyleConfigMap.get(DesignerOrder.getStyleType());
        if (designerStyleConfig != null) {
            DesignerOrderVo.setStyleName(designerStyleConfig.getStyleName());
        }
        DesignerOrderVo.setBudget(project.getDecorationBudget() + "");
        DesignerOrderVo.setArea(project.getArea() + "");
        if (companyInfo != null) {
            DesignerOrderVo.setCompanyName(companyInfo.getCompanyName());
            DesignerOrderVo.setCompanyState(companyInfo.getAuditStatus());
        }
        String designerId = projectUserService.queryUserIdOne(project.getProjectNo(), RoleFunctionEnum.DESIGN_POWER);
        EmployeeMsg employeeMsg = employeeMsgMapper.selectByPrimaryKey(designerId);
        if (employeeMsg != null) {
            DesignerOrderVo.setDesignerName(employeeMsg.getRealName());
        }
        try {
            DesignerOrderVo.setOrderStateName(DesignStateEnum.queryByState(DesignerOrder.getOrderStage()).getStateName(stateType));
        } catch (Exception e) {
            DesignerOrderVo.setOrderStateName("未知");
        }
        DesignerOrderVo.setOrderState(DesignerOrder.getOrderStage() + "");
        DesignerOrderVo.setProjectMoney(project.getDecorationBudget() + "");
        OptionLogExample logExample = new OptionLogExample();
        logExample.createCriteria().andOptionTypeEqualTo("DO");
        logExample.setOrderByClause(" option_time desc limit 1");
        List<OptionLog> optionLogs = optionLogMapper.selectByExample(logExample);
        if (!optionLogs.isEmpty()) {
            OptionLog optionLog = optionLogs.get(0);
            DesignerOrderVo.setOptionUserName(optionLog.getOptionUserName());
            DesignerOrderVo.setOptionTime(optionLog.getOptionTime().getTime() + "");
        }
        return DesignerOrderVo;
    }

    /**
     * 查询设计风格
     *
     * @return
     */
    private Map<String, DesignerStyleConfigVo> queryDesignerStyleConfig() {
        List<DesignerStyleConfigVo> styleConfigs = designerService.queryDesignerStyle();
        return ReflectUtils.listToMap(styleConfigs, "styleCode");
    }

    @Override
    public void designerOrderExcel(Integer orderTpye, String companyId, String projectNo, String userMsg, String orderSource, String createTimeStart, String createTimeEnd,
                                   String styleCode, String money, String acreage, int designerOrderState, int companyState, String optionUserName,
                                   String optionTimeStart, String optionTimeEnd, int stateType, String fileName, HttpServletResponse response) {
        PageVo<List<DesignerOrderVo>> pageVo = queryDesignerOrder(null, orderTpye, companyId, projectNo, userMsg, orderSource, createTimeStart, createTimeEnd, styleCode,
                money, acreage, designerOrderState, companyState, optionUserName, optionTimeStart, optionTimeEnd, 1000000, 1, stateType);

        List<List<String>> lists = new ArrayList<>();
        lists.add(Arrays.asList("序号", "订单编号", "订单子编号", "业主姓名", "业主电话", "所在地", "订单来源", "创建时间",
                "装饰风格", "建筑面积", "建筑预算", "归属设计公司", "公司状态", "归属设计师", "订单状态", "操作人", "操作时间"));
        List<DesignerOrderVo> DesignerOrderVos = pageVo.getData();
        int index = 1;
        for (DesignerOrderVo DesignerOrderVo : DesignerOrderVos) {
            List<String> excelContent = new ArrayList<>();
            excelContent.add(index + "");
            excelContent.add(DesignerOrderVo.getProjectNo());
            excelContent.add(DesignerOrderVo.getDesignOrderNo());
            excelContent.add(DesignerOrderVo.getOwnerName());
            excelContent.add(DesignerOrderVo.getOwnerPhone());
            excelContent.add(DesignerOrderVo.getAddress());
            excelContent.add(DesignerOrderVo.getOrderSource());
            excelContent.add(DesignerOrderVo.getCreateTime());
            excelContent.add(DesignerOrderVo.getStyleName());
            excelContent.add(DesignerOrderVo.getArea());
            excelContent.add(DesignerOrderVo.getBudget());
            excelContent.add(DesignerOrderVo.getCompanyName());
            excelContent.add(DesignerOrderVo.getCompanyState());
            excelContent.add(DesignerOrderVo.getDesignerName());
            excelContent.add(DesignerOrderVo.getOrderStateName());
            excelContent.add(DesignerOrderVo.getOptionUserName());
            excelContent.add(DesignerOrderVo.getOptionTime());
            lists.add(excelContent);
            index++;
        }
        ExcelUtil.loadExcel(lists, fileName, response);
    }

    /**
     * 合同审核通过
     *
     * @param orderNo      设计订单编号
     * @param contractType 合同类型，1全款合同，2分期合同
     */
    @Override
    public void reviewPass(String orderNo, int contractType) {
        if (contractType != 1 && contractType != 2) {
            throw new RuntimeException("必须声明合同类型");
        }
        DesignStateEnum stateEnum = DesignStateEnum.STATE_220;
        if (contractType == 2) {
            stateEnum = DesignStateEnum.STATE_140;
        }
        DesignerOrder designerOrder = queryDesignerOrderByOrderNo(orderNo);
        Project project = queryProjectByNo(designerOrder.getProjectNo());
//        if (!designerOrder.getCompanyId().equals(companyId)) {
//            throw new RuntimeException("无权操作");
//        }
        DesignerOrder updateOrder = new DesignerOrder();
        updateOrder.setId(designerOrder.getId());
        updateOrder.setContractType(contractType);
        updateOrder.setOrderStage(stateEnum.getState());
        DesignerOrderMapper.updateByPrimaryKeySelective(updateOrder);
        //记录操作日志
        saveOptionLog(designerOrder.getOrderNo(), "system", "system", "合同审核通过");
        saveLog(stateEnum.getState(), project);
        updateProjectState(project.getProjectNo(), stateEnum.getState());
        // 支付阶段通知
        constructionAndPayStateService.notifyPay(designerOrder.getOrderNo(), 1);
    }

    @Override
    public void setDesignId(String projectNo, String designId, String designerId) {
        Project project = queryProjectByNo(projectNo);
        DesignerOrder designerOrder = queryDesignerOrder(projectNo);
        //查询设计师
        String userId = projectUserService.queryUserIdOne(projectNo, RoleFunctionEnum.DESIGN_POWER);
        if (userId == null || !userId.equals(designerId)) {
            throw new RuntimeException("无权操作");
        }
        DesignerOrder updateOrder = new DesignerOrder();
        updateOrder.setDesignId(designId);
        updateOrder.setId(designerOrder.getId());
        DesignerOrderMapper.updateByPrimaryKeySelective(updateOrder);
    }

    /**
     * 创建施工订单
     *
     * @param projectNo
     */
    @Override
    public void createConstructionOrder(String projectNo) {
        Project project = queryProjectByNo(projectNo);
        ConstructionOrder constructionOrder = new ConstructionOrder();
        constructionOrder.setOrderNo(OrderNoUtils.getNo("CO"));
        constructionOrder.setCreateTime(new Date());
        constructionOrder.setProjectNo(projectNo);
        constructionOrder.setStatus(1);
        constructionOrder.setOrderStage(ConstructionStateEnum.STATE_500.getState());
        // 1小包，2大包
        if (project.getContractType() != null && project.getContractType() == 2) {
            DesignerOrder designerOrders = queryDesignerOrder(projectNo);
            String companyId = designerOrders.getCompanyId();
            constructionOrder.setCompanyId(companyId);
            constructionOrder.setSchemeNo(buildConfigService.getSchemeNoByCompanyId(companyId));
            constructionOrder.setOrderStage(ConstructionStateEnum.STATE_520.getState());
        }
        constructionOrderMapper.insertSelective(constructionOrder);
        updateProjectState(projectNo, constructionOrder.getOrderStage());
    }

    /**
     * 不指派
     *
     * @param projectNo      项目编号
     * @param reason         不派单原因
     * @param optionUserId   操作人ID
     * @param optionUserName 操作人姓名
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void notDispatch(String projectNo, String reason, String optionUserId, String optionUserName) {
        Project project = queryProjectByNo(projectNo);
        DesignerOrder designerOrders = queryDesignerOrder(projectNo);
        checkOrderState(designerOrders, DesignStateEnum.STATE_CLOSE_PLATFORM);
        //设置该设计订单所属公司
        DesignerOrder updateOrder = new DesignerOrder();
        updateOrder.setOrderStage(DesignStateEnum.STATE_CLOSE_PLATFORM.getState());
        DesignerOrderExample orderExample = new DesignerOrderExample();
        orderExample.createCriteria().andOrderNoEqualTo(designerOrders.getOrderNo());
        DesignerOrderMapper.updateByExampleSelective(updateOrder, orderExample);
        //记录操作日志
        saveOptionLog(designerOrders.getOrderNo(), optionUserId, optionUserName, reason);
        saveLog(DesignStateEnum.STATE_CLOSE_PLATFORM.getState(), project);
        updateProjectState(projectNo, DesignStateEnum.STATE_CLOSE_PLATFORM.getState());
    }

    /**
     * 指派设计公司
     *
     * @param projectNo      项目编号
     * @param companyId      公司ID
     * @param optionUserId   操作人ID
     * @param optionUserName 操作人姓名
     * @param contractType   承包类型，1小包，2大包
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void dispatch(String projectNo, String companyId, String optionUserId, String optionUserName, int contractType) {
        Project project = queryProjectByNo(projectNo);
        project.setContractType(contractType);
        DesignerOrder designerOrder = queryDesignerOrder(projectNo);
        //设置该设计订单所属公司
        checkOrderState(designerOrder, DesignStateEnum.STATE_10);
        DesignerOrder updateOrder = new DesignerOrder();
        updateOrder.setCompanyId(companyId);
        updateOrder.setOrderStage(DesignStateEnum.STATE_10.getState());
        DesignerOrderExample orderExample = new DesignerOrderExample();
        orderExample.createCriteria().andOrderNoEqualTo(designerOrder.getOrderNo());
        DesignerOrderMapper.updateByExampleSelective(updateOrder, orderExample);
        //记录操作日志
        String remark = "指派订单给公司【" + companyId + "】";
        saveOptionLog(designerOrder.getOrderNo(), optionUserId, optionUserName, remark);
        saveLog(DesignStateEnum.STATE_10.getState(), project);
        updateProjectState(projectNo, DesignStateEnum.STATE_10.getState());
    }

    @Override
    public DesignOrderDelVo queryDesignerOrderVoByProjectNo(String projectNo, int stateType) {
        Map<String, DesignerStyleConfigVo> designerStyleConfigMap = queryDesignerStyleConfig();
        DesignerOrder designerOrder = queryDesignerOrder(projectNo);
        Project project = queryProjectByNo(projectNo);
        List<String> userIds = new ArrayList<>();
        userIds.add(projectUserService.queryUserIdOne(projectNo, RoleFunctionEnum.OWNER_POWER));
        userIds.add(projectUserService.queryUserIdOne(projectNo, RoleFunctionEnum.DESIGN_POWER));
        Map<String, UserMsgVo> msgVoMap = userService.queryUserMap(userIds);
        Map<String, CompanyInfo> companyInfoMap = getCompanyByIds(Arrays.asList(designerOrder.getCompanyId()));
        DesignerOrderVo designerOrderVo = getDesignerOrderVo(companyInfoMap.get(designerOrder.getCompanyId()), stateType, designerStyleConfigMap, designerOrder, project, msgVoMap);
        DesignStateEnum stateEnum = DesignStateEnum.queryByState(designerOrder.getOrderStage());
        DesignOrderDelVo designOrderDelVo = new DesignOrderDelVo(designerOrderVo, 1, stateEnum.getStateName(stateType),
                "我也不知道这是啥", "小区名称", designerOrder.getProjectNo(),
                DateUtils.dateToStr(designerOrder.getVolumeRoomTime(), "yyyy-MM-dd"), "这是合同名称", "这是合同地址");
        return designOrderDelVo;
    }

    /**
     * 设计公司拒绝接单
     *
     * @param projectNo      项目编号
     * @param companyId      设计公司ID
     * @param reason         拒绝原因
     * @param optionUserId   操作人ID
     * @param optionUserName 操作人名称
     */
    @Override
    public void refuseOrder(String projectNo, String companyId, String reason, String optionUserId, String optionUserName) {
        Project project = queryProjectByNo(projectNo);
        DesignerOrder designerOrder = queryDesignerOrder(projectNo);
        if (!designerOrder.getCompanyId().equals(companyId)) {
            throw new RuntimeException("无权操作");
        }
        checkOrderState(designerOrder, DesignStateEnum.STATE_1);
        //设置该设计订单所属公司
        DesignerOrder updateOrder = new DesignerOrder();
        updateOrder.setCompanyId("");
        updateOrder.setOrderStage(DesignStateEnum.STATE_1.getState());
        DesignerOrderExample orderExample = new DesignerOrderExample();
        orderExample.createCriteria().andOrderNoEqualTo(designerOrder.getOrderNo());
        DesignerOrderMapper.updateByExampleSelective(updateOrder, orderExample);
        //记录操作日志
        String remark = "公司编号为【" + companyId + "】的公司拒绝接单，拒绝原因：" + reason;
        saveOptionLog(designerOrder.getOrderNo(), optionUserId, optionUserName, remark);
        saveLog(DesignStateEnum.STATE_1.getState(), project);
        updateProjectState(projectNo, DesignStateEnum.STATE_1.getState());
    }

    /**
     * 设计公司指派设计师
     *
     * @param projectNo      项目编号
     * @param companyId      公司ID
     * @param designerUserId 设计师ID
     * @param optionUserId   操作人ID
     * @param optionUserName 操作人名称
     */
    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void assignDesigner(String projectNo, String companyId, String designerUserId, String optionUserId, String optionUserName) {
        Project project = queryProjectByNo(projectNo);
        DesignerOrder designerOrder = queryDesignerOrder(projectNo);
        if (!designerOrder.getCompanyId().equals(companyId)) {
            throw new RuntimeException("无权操作");
        }
        checkOrderState(designerOrder, DesignStateEnum.STATE_20);
        //添加设计师
        projectUserService.addUserId(designerOrder.getOrderNo(), designerOrder.getProjectNo(), designerUserId, RoleFunctionEnum.DESIGN_POWER);
        //设置该设计订单所属公司
        DesignerOrder updateOrder = new DesignerOrder();
        updateOrder.setOrderStage(DesignStateEnum.STATE_20.getState());
        DesignerOrderExample orderExample = new DesignerOrderExample();
        orderExample.createCriteria().andOrderNoEqualTo(designerOrder.getOrderNo());
        DesignerOrderMapper.updateByExampleSelective(updateOrder, orderExample);
        //TODO 需要发出通知给设计师
        //记录操作日志
        String remark = "公司编号为【" + companyId + "】的公司指派设计师";
        saveOptionLog(designerOrder.getOrderNo(), optionUserId, optionUserName, remark);
        saveLog(DesignStateEnum.STATE_20.getState(), project);
        updateProjectState(projectNo, DesignStateEnum.STATE_20.getState());
    }

    /**
     * 设计师拒绝接单
     *
     * @param projectNo      项目编号
     * @param reason         拒绝原因
     * @param designerUserId 设计师ID
     */
    @Override
    public void designerRefuse(String projectNo, String reason, String designerUserId) {
        Project project = queryProjectByNo(projectNo);
        DesignerOrder designerOrder = queryDesignerOrder(projectNo);
        //查询设计师
        String userId = projectUserService.queryUserIdOne(projectNo, RoleFunctionEnum.DESIGN_POWER);
        if (userId == null || !userId.equals(designerUserId)) {
            throw new RuntimeException("无权操作");
        }
        checkOrderState(designerOrder, DesignStateEnum.STATE_10);
        queryDesignerMsg(designerUserId);
        EmployeeMsg employeeMsg = queryEmployeeMsg(designerUserId);
        String optionUserName = employeeMsg.getRealName();
        //设置该设计订单所属公司
        DesignerOrder updateOrder = new DesignerOrder();
        //清空设计师ID
        //updateOrder.setUserId("");
        projectUserService.delUserRel(designerOrder.getOrderNo(), designerOrder.getProjectNo(), designerUserId, RoleFunctionEnum.DESIGN_POWER);
        updateOrder.setOrderStage(DesignStateEnum.STATE_10.getState());
        DesignerOrderExample orderExample = new DesignerOrderExample();
        orderExample.createCriteria().andOrderNoEqualTo(designerOrder.getOrderNo());
        DesignerOrderMapper.updateByExampleSelective(updateOrder, orderExample);
        //TODO 需要发出通知给设计师
        //记录操作日志
        String remark = "设计师【" + optionUserName + "】拒绝接单";
        saveOptionLog(designerOrder.getOrderNo(), designerUserId, optionUserName, remark);
        saveLog(DesignStateEnum.STATE_10.getState(), project);
        updateProjectState(projectNo, DesignStateEnum.STATE_10.getState());
    }

    /**
     * 设计师接单
     *
     * @param projectNo      项目编号
     * @param designerUserId 设计师ID
     */
    @Override
    public void designerReceipt(String projectNo, String designerUserId) {
        //设计师接单
        Project project = queryProjectByNo(projectNo);
        DesignerOrder designerOrder = queryDesignerOrder(projectNo);
        //查询设计师
        String userId = projectUserService.queryUserIdOne(projectNo, RoleFunctionEnum.DESIGN_POWER);
        if (userId == null || !userId.equals(designerUserId)) {
            throw new RuntimeException("无权操作");
        }
        checkOrderState(designerOrder, DesignStateEnum.STATE_30);
        queryDesignerMsg(designerUserId);
        EmployeeMsg employeeMsg = queryEmployeeMsg(designerUserId);
        String optionUserName = employeeMsg.getRealName();
        //设置该设计订单所属公司
        DesignerOrder updateOrder = new DesignerOrder();
        //清空设计师ID
        updateOrder.setOrderStage(DesignStateEnum.STATE_30.getState());
        DesignerOrderExample orderExample = new DesignerOrderExample();
        orderExample.createCriteria().andOrderNoEqualTo(designerOrder.getOrderNo());
        DesignerOrderMapper.updateByExampleSelective(updateOrder, orderExample);
        //TODO 需要发出通知给设计师
        //记录操作日志
        String remark = "设计师【" + optionUserName + "】已接单";
        saveOptionLog(designerOrder.getOrderNo(), designerUserId, optionUserName, remark);
        saveLog(DesignStateEnum.STATE_30.getState(), project);
        updateProjectState(projectNo, DesignStateEnum.STATE_30.getState());
    }

    /**
     * 设计师发起量房预约
     *
     * @param projectNo      项目编号
     * @param designerUserId 设计师ID
     * @param volumeRoomDate 预约时间
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void makeAnAppointmentVolumeRoom(String projectNo, String designerUserId, String volumeRoomDate, String appointmentAmount) {
        //设计师接单
        Project project = queryProjectByNo(projectNo);
        DesignerOrder designerOrder = queryDesignerOrder(projectNo);
        String userId = projectUserService.queryUserIdOne(projectNo, RoleFunctionEnum.DESIGN_POWER);
        if (userId == null || !userId.equals(designerUserId)) {
            throw new RuntimeException("无权操作");
        }
        checkOrderState(designerOrder, DesignStateEnum.STATE_40);
        queryDesignerMsg(designerUserId);
        EmployeeMsg employeeMsg = queryEmployeeMsg(designerUserId);
        String optionUserName = employeeMsg.getRealName();
        //设置该设计订单所属公司
        DesignerOrder updateOrder = new DesignerOrder();
        //清空设计师ID
        updateOrder.setOrderStage(DesignStateEnum.STATE_40.getState());
        Date date = DateUtils.strToDate(volumeRoomDate, "yyyy-MM-dd");
        if (date.getTime() == 0) {
            throw new RuntimeException("无效的预约时间");
        }
        updateOrder.setVolumeRoomTime(date);
        //预约金额
        updateOrder.setVolumeRoomMoney(MathUtil.getFen(appointmentAmount));
        DesignerOrderExample orderExample = new DesignerOrderExample();
        orderExample.createCriteria().andOrderNoEqualTo(designerOrder.getOrderNo());
        DesignerOrderMapper.updateByExampleSelective(updateOrder, orderExample);
        //TODO 需要发出通知给业主
        //记录操作日志
        String remark = "设计师【" + optionUserName + "】发起量房预约";
        saveOptionLog(designerOrder.getOrderNo(), designerUserId, optionUserName, remark);
        saveLog(DesignStateEnum.STATE_40.getState(), project);

        updateProjectState(projectNo, DesignStateEnum.STATE_40.getState());
    }

    @Override
    public void remindOwner(String projectNo, String designerUserId) {
        //设计师接单
        Project project = queryProjectByNo(projectNo);
        DesignerOrder designerOrder = queryDesignerOrder(projectNo);
        String userId = projectUserService.queryUserIdOne(projectNo, RoleFunctionEnum.DESIGN_POWER);
        if (userId == null || !userId.equals(designerUserId)) {
            throw new RuntimeException("无权操作");
        }
        RemindOwnerLogExample logExample = new RemindOwnerLogExample();
        //24小时内只能提示一次
        logExample.createCriteria().andDesignerOrderNoEqualTo(designerOrder.getOrderNo())
                .andRemindTimeGreaterThanOrEqualTo(new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24));
        List<RemindOwnerLog> remindOwnerLogs = remindOwnerLogMapper.selectByExample(logExample);
       if (!remindOwnerLogs.isEmpty()) {
            throw new RuntimeException("每24小时能只能提醒一次~");
        }
        String ownerId = projectUserService.queryUserIdOne(projectNo, RoleFunctionEnum.OWNER_POWER);
        if (ownerId == null) {
            throw new RuntimeException("没有查询到业主信息");
        }
        RemindOwnerLog remindOwnerLog = new RemindOwnerLog();
        remindOwnerLog.setDesignerOrderNo(designerOrder.getOrderNo());
        remindOwnerLog.setOwnerId(ownerId);
        remindOwnerLog.setRemindTime(new Date());
        sendMessage(project.getProjectNo(), userId, remindOwnerLog.getOwnerId(), "请支付支付量房费");
        remindOwnerLogMapper.insertSelective(remindOwnerLog);
    }
    /**
     * @Author jiang
     * @Description 提醒业主支付量房费
     * @Date
     * @Param
     * @return
     **/
    private void sendMessage(String projectNo, String sendUserId, String subUserId, String content) {
        Map<String, String> requestMsg = new HashMap<>();
        requestMsg.put("projectNo", projectNo);
        requestMsg.put("userNo", "[\"" + subUserId + "\"]");
        requestMsg.put("senderId", sendUserId);
        requestMsg.put("content", content);
        requestMsg.put("dynamicId", "0");
        requestMsg.put("type", "2");
        LOGGER.info("发送消息：requestMsg：{}", requestMsg);
        try {
                HttpUtils.HttpRespMsg respMsg = HttpUtils.post(httpLinks.getMessageSave(), requestMsg);
                LOGGER.info("respMsg:{}", JSONUtil.bean2JsonStr(respMsg));
        } catch (Exception e) {
            LOGGER.error("发送消息出错", e);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateOrderState(String projectNo, int orderState, String optionId, String optionName) {
        //设计师接单
        Project project = queryProjectByNo(projectNo);
        DesignerOrder designerOrder = queryDesignerOrder(projectNo);
        DesignStateEnum stateEnum = DesignStateEnum.queryByState(orderState);
        checkOrderState(designerOrder, stateEnum);
        //设置该设计订单所属公司
        DesignerOrder updateOrder = new DesignerOrder();
        //清空设计师ID
        updateOrder.setOrderStage(orderState);
        DesignerOrderExample orderExample = new DesignerOrderExample();
        orderExample.createCriteria().andOrderNoEqualTo(designerOrder.getOrderNo());
        DesignerOrderMapper.updateByExampleSelective(updateOrder, orderExample);
        //TODO 需要发出通知给业主
        //记录操作日志
        String remark = stateEnum.getLogText();
        saveOptionLog(designerOrder.getOrderNo(), optionId, optionName, remark);
        saveLog(stateEnum.getState(), project);
        //订单交易完成
        if (orderState == DesignStateEnum.STATE_270.getState() || orderState == DesignStateEnum.STATE_210.getState()) {
//            createConstructionOrder(projectNo);
        } else {
            updateProjectState(projectNo, orderState);
        }
        if (orderState == DesignStateEnum.STATE_170.getState()) {
            // 支付阶段通知
            constructionAndPayStateService.notifyPay(designerOrder.getOrderNo(), 2);
        }
        if (orderState == DesignStateEnum.STATE_200.getState()) {
            // 支付阶段通知
            constructionAndPayStateService.notifyPay(designerOrder.getOrderNo(), 3);
        }
    }

    @Override
    public void updateOrderState(String projectNo, int orderState, String optionId, String optionName, String reason) {
        //设计师接单
        Project project = queryProjectByNo(projectNo);
        DesignerOrder designerOrder = queryDesignerOrder(projectNo);
        DesignStateEnum stateEnum = DesignStateEnum.queryByState(orderState);
        checkOrderState(designerOrder, stateEnum);
        //设置该设计订单所属公司
        DesignerOrder updateOrder = new DesignerOrder();
        //清空设计师ID
        updateOrder.setOrderStage(orderState);
        DesignerOrderExample orderExample = new DesignerOrderExample();
        orderExample.createCriteria().andOrderNoEqualTo(designerOrder.getOrderNo());
        DesignerOrderMapper.updateByExampleSelective(updateOrder, orderExample);
        //TODO 需要发出通知给业主
        //记录操作日志
        saveOptionLog(designerOrder.getOrderNo(), optionId, optionName, reason);
        saveLog(stateEnum.getState(), project);
        //订单交易完成
        if (orderState == DesignStateEnum.STATE_270.getState() || orderState == DesignStateEnum.STATE_210.getState()) {
//            createConstructionOrder(projectNo);
        } else {
            updateProjectState(projectNo, orderState);
        }
        if (orderState == DesignStateEnum.STATE_170.getState()) {
            // 支付阶段通知
            constructionAndPayStateService.notifyPay(designerOrder.getOrderNo(), 2);
        }
        if (orderState == DesignStateEnum.STATE_200.getState()) {
            // 支付阶段通知
            constructionAndPayStateService.notifyPay(designerOrder.getOrderNo(), 3);
        }
    }

    @Override
    public void confirmedDeliveries(String projectNo, String optionId) {
        //设计师接单
        Project project = queryProjectByNo(projectNo);
        DesignerOrder designerOrders = queryDesignerOrder(projectNo);
        checkOrderState(designerOrders, DesignStateEnum.STATE_70);
        //设置该设计订单所属公司
        DesignerOrder updateOrder = new DesignerOrder();
        //清空设计师ID
        updateOrder.setOrderStage(DesignStateEnum.STATE_70.getState());
        DesignerOrderExample orderExample = new DesignerOrderExample();
        orderExample.createCriteria().andOrderNoEqualTo(designerOrders.getOrderNo());
        DesignerOrderMapper.updateByExampleSelective(updateOrder, orderExample);
        //TODO 需要发出通知给业主
        //记录操作日志
        String remark = DesignStateEnum.STATE_70.getLogText();
        saveOptionLog(designerOrders.getOrderNo(), optionId, "业主", remark);
        saveLog(DesignStateEnum.STATE_70.getState(), project);
        updateProjectState(projectNo, DesignStateEnum.STATE_70.getState());
    }


    @Override
    public void paySuccess(String orderNo) {
        //设计师接单
        DesignerOrder designerOrder = queryDesignerOrderByOrderNo(orderNo);
        Project project = queryProjectByNo(designerOrder.getProjectNo());
        DesignStateEnum designStateEnum = DesignStateEnum.queryByState(designerOrder.getOrderStage());
        //设置该设计订单所属公司
        DesignerOrder updateOrder = new DesignerOrder();
        DesignStateEnum stateEnum = null;
        switch (designStateEnum) {
            case STATE_45:
                stateEnum = DesignStateEnum.STATE_50;
                break;
            case STATE_140:
                stateEnum = DesignStateEnum.STATE_150;
                break;
            case STATE_170:
                stateEnum = DesignStateEnum.STATE_180;
                break;
            case STATE_200:
                stateEnum = DesignStateEnum.STATE_210;
                break;
            case STATE_220:
                stateEnum = DesignStateEnum.STATE_230;
                break;
            default:
                throw new RuntimeException("无效的状态");
        }
        updateOrder.setOrderStage(stateEnum.getState());
        DesignerOrderExample orderExample = new DesignerOrderExample();
        orderExample.createCriteria().andOrderNoEqualTo(designerOrder.getOrderNo());
        DesignerOrderMapper.updateByExampleSelective(updateOrder, orderExample);
        //TODO 需要发出通知给业主
        //记录操作日志
        String remark = stateEnum.getLogText();
        saveOptionLog(designerOrder.getOrderNo(), "system", "system", remark);
        saveLog(stateEnum.getState(), project);
        //订单交易完成
//        if (stateEnum == DesignStateEnum.STATE_270 || stateEnum == DesignStateEnum.STATE_210) {
//            createConstructionOrder(project.getProjectNo());
//        }
        updateProjectState(project.getProjectNo(), stateEnum.getState());
    }


    @Override
    public void payTimeOut(String projectNo) {
        //设计师接单
        Project project = queryProjectByNo(projectNo);
        DesignerOrder designerOrder = queryDesignerOrder(projectNo);
        DesignStateEnum designStateEnum = DesignStateEnum.queryByState(designerOrder.getOrderStage());
        DesignStateEnum timeOutState = null;
        switch (designStateEnum) {
            case STATE_40:
                timeOutState = DesignStateEnum.STATE_42;
                break;
            case STATE_140:
                timeOutState = DesignStateEnum.STATE_141;
                break;
            case STATE_170:
                timeOutState = DesignStateEnum.STATE_171;
                break;
            case STATE_220:
                timeOutState = DesignStateEnum.STATE_221;
                break;
            default:
                throw new RuntimeException("无效的订单状态");
        }
        updateOrderState(projectNo, timeOutState.getState(), "system", "system");
        saveLog(timeOutState.getState(), project);
    }

    /**
     * 业主发起终止订单操作
     *
     * @param projectNo 项目编号
     * @param userId    用户Id
     * @param reason    终止合同原因
     */
    @Override
    public void endOrder(String projectNo, String userId, String reason) {
        //设计师接单
        Project project = queryProjectByNo(projectNo);
        DesignerOrder designerOrder = queryDesignerOrder(projectNo);
        if (!project.getOwnerId().equals(userId)) {
            throw new RuntimeException("无权操作");
        }
        DesignStateEnum stateEnum = null;
        try {
            stateEnum(DesignStateEnum.queryByState(designerOrder.getOrderStage()));
            checkOrderState(designerOrder, stateEnum);
        } catch (Exception e) {
            throw new RuntimeException("当前订单不可取消，如有疑问，请联系客服");
        }
        DesignerOrder updateOrder = new DesignerOrder();
        //清空设计师ID
        updateOrder.setOrderStage(stateEnum.getState());
        DesignerOrderExample orderExample = new DesignerOrderExample();
        orderExample.createCriteria().andOrderNoEqualTo(designerOrder.getOrderNo());
        DesignerOrderMapper.updateByExampleSelective(updateOrder, orderExample);
        //TODO 需要发出通知给业主
        //记录操作日志
        String remark = stateEnum.getLogText();
        saveOptionLog(designerOrder.getOrderNo(), userId, "业主", remark);
        saveLog(stateEnum.getState(), project);
        updateProjectState(projectNo, stateEnum.getState());
    }

    /**
     * 获取关闭订单的状态值
     *
     * @param stateEnum
     * @return
     */
    private DesignStateEnum stateEnum(DesignStateEnum stateEnum) {
        switch (stateEnum) {
            case STATE_1:
                return DesignStateEnum.STATE_ORDER_END_3;
            case STATE_10:
                return DesignStateEnum.STATE_ORDER_END_11;
            case STATE_20:
                return DesignStateEnum.STATE_ORDER_END_21;
            case STATE_30:
                return DesignStateEnum.STATE_ORDER_END_31;
            case STATE_40:
                return DesignStateEnum.STATE_ORDER_END_41;
            default:
                throw new RuntimeException("无效的订单状态值");
        }
    }

    /**
     * 检查设计订单状态
     *
     * @param DesignerOrder   设计订单
     * @param designStateEnum 目标状态
     */
    @Override
    public void checkOrderState(DesignerOrder DesignerOrder, DesignStateEnum designStateEnum) {
        int orderState = DesignerOrder.getOrderStage();
        DesignStateEnum currentState = DesignStateEnum.queryByState(orderState);
        if (!currentState.getNextStates().contains(designStateEnum)) {
            throw new RuntimeException("不能进行该操作");
        }
    }

    /**
     * 根据设计师ID查询设计师信息,1审核通过的
     *
     * @param designerUserId 设计师ID
     * @return
     */
    private DesignerMsg queryDesignerMsg(String designerUserId) {
        DesignerMsgExample msgExample = new DesignerMsgExample();
        msgExample.createCriteria().andUserIdEqualTo(designerUserId).andReviewStateEqualTo(2);
        List<DesignerMsg> designerMsgs = designerMsgMapper.selectByExample(msgExample);
        if (designerMsgs.isEmpty()) {
            return createDesignerMsg(designerUserId);
        }
        return designerMsgs.get(0);
    }

    private DesignerMsg createDesignerMsg(String designerUserId) {
        DesignerMsg designerMsg = new DesignerMsg();
        designerMsg.setUserId(designerUserId);
        designerMsg.setVolumeRoomMoney(new BigDecimal(100));
        designerMsg.setDesignerMoneyLow(new BigDecimal(100));
        designerMsg.setDesignerMoneyHigh(new BigDecimal(100));
        designerMsg.setIdentity(Long.parseLong("1"));
        designerMsg.setTag(Long.parseLong("1"));
        designerMsg.setReviewState(2);
        designerMsg.setLevel(Long.parseLong("1"));
        designerMsgMapper.insertSelective(designerMsg);
        return designerMsg;
    }

    /**
     * 根据设计师ID查询员工信息，1已实名认证，2在职
     *
     * @param employeeMsgId 员工ID
     * @return
     */
    private EmployeeMsg queryEmployeeMsg(String employeeMsgId) {
        EmployeeMsgExample msgExample = new EmployeeMsgExample();
        msgExample.createCriteria().andUserIdEqualTo(employeeMsgId).andEmployeeStateEqualTo(1).andAuthStateEqualTo(2);
        List<EmployeeMsg> employeeMsgs = employeeMsgMapper.selectByExample(msgExample);
        if (employeeMsgs.isEmpty()) {
            throw new RuntimeException("没有查询到员工");
        }
        return employeeMsgs.get(0);
    }

    /**
     * 记录操作日志
     *
     * @param orderNo        记录订单编号
     * @param optionUserId   操作人ID
     * @param optionUserName 操作人姓名
     * @param remark         备注
     */
    private void saveOptionLog(String orderNo, String optionUserId, String optionUserName, String remark) {
        //记录操作日志
        OptionLog optionLog = new OptionLog();
        optionLog.setLinkNo(orderNo);
        optionLog.setOptionTime(new Date());
        optionLog.setOptionType("DO");
        optionLog.setOptionUserId(optionUserId);
        optionLog.setOptionUserName(optionUserName);
        optionLog.setRemark(remark);
        optionLogMapper.insertSelective(optionLog);
    }

    /**
     * 根据项目编号查询项目信息
     *
     * @param projectNo 项目编号
     * @return
     */
    @Override
    public Project queryProjectByNo(String projectNo) {
        ProjectExample projectExample = new ProjectExample();
        projectExample.createCriteria().andProjectNoEqualTo(projectNo);
        List<Project> projects = projectMapper.selectByExample(projectExample);
        if (projects.isEmpty()) {
            throw new RuntimeException("没有查询到该项目");
        }
        return projects.get(0);
    }

    /**
     * 根据项目编号查询设计订单
     *
     * @param projectNo 项目编号
     * @return
     */
    @Override
    public DesignerOrder queryDesignerOrder(String projectNo) {
        DesignerOrderExample orderExample = new DesignerOrderExample();
        orderExample.createCriteria().andProjectNoEqualTo(projectNo).andStatusEqualTo(1);
        List<DesignerOrder> designerOrders = DesignerOrderMapper.selectByExample(orderExample);
        if (designerOrders.isEmpty()) {
            throw new RuntimeException("没有查询到相关设计订单");
        }
        return designerOrders.get(0);
    }

    /**
     * 根据项目编号查询设计订单
     *
     * @param orderNo 设计订单编号
     * @return
     */
    @Override
    public DesignerOrder queryDesignerOrderByOrderNo(String orderNo) {
        DesignerOrderExample orderExample = new DesignerOrderExample();
        orderExample.createCriteria().andOrderNoEqualTo(orderNo).andStatusEqualTo(1);
        List<DesignerOrder> designerOrders = DesignerOrderMapper.selectByExample(orderExample);
        if (designerOrders.isEmpty()) {
            throw new RuntimeException("没有查询到相关设计订单");
        }
        return designerOrders.get(0);
    }

    /**
     * 记录操作日志
     *
     * @param state   设计订单状态
     * @param project 项目信息
     */
    private void saveLog(int state, Project project) {
        ProjectStageLog stageLog = new ProjectStageLog();
        stageLog.setCreateTime(new Date());
        stageLog.setStage(state);
        stageLog.setProjectNo(project.getProjectNo());
        stageLog.setBeginTime(new Date());
        stageLog.setType(project.getContractType());
        stageLogMapper.insertSelective(stageLog);
    }

    /**
     * @param designOrderNo 设计订单编号
     * @return ["LFYY(量房预约),LFFY(提醒支付量房费用)","LFZL(提交量房资料)",
     * "SJZL(提交设计资料)","SGZL(提交施工资料)","CKHT(查看合同)","YJD(预交底)"]
     */
    @Override
    public List<String> showBtn(String designOrderNo) {
        DesignerOrderExample orderExample = new DesignerOrderExample();
        orderExample.createCriteria().andOrderNoEqualTo(designOrderNo).andStatusEqualTo(1);
        List<DesignerOrder> designerOrders = DesignerOrderMapper.selectByExample(orderExample);
        if (designerOrders.isEmpty()) {
            throw new RuntimeException("无效的订单编号");
        }
        DesignerOrder designerOrder = designerOrders.get(0);
        DesignStateEnum stateEnum = DesignStateEnum.queryByState(designerOrder.getOrderStage());
        List<String> btns = new ArrayList<>();
        switch (stateEnum) {
            case STATE_30:
                btns.add("LFYY");
            case STATE_45:
                btns.add("LFFY");
                break;
            case STATE_50:
                btns.add("LFZL");
                break;
            case STATE_130:
                btns.add("CKHT");
                break;
            case STATE_140:
            case STATE_160:
            case STATE_170:
            case STATE_190:
            case STATE_200:
            case STATE_210:
            case STATE_220:
            case STATE_240:
            case STATE_260:
            case STATE_270:
                btns.add("CKHT");
                break;
            case STATE_150:
            case STATE_230:
                btns.add("SJZL");
                btns.add("CKHT");
                break;
            case STATE_180:
            case STATE_250:
                btns.add("SGZL");
                btns.add("CKHT");
                break;
        }
        if (stateEnum != DesignStateEnum.STATE_270 && stateEnum != DesignStateEnum.STATE_210) {
            return btns;
        }
        if (designerOrder.getPreviewState() == 2) {
            btns.add("YJD");
        }
        return btns;
    }

    @Override
    public void updateProjectState(String projectNo, int state) {
        ProjectExample projectExample = new ProjectExample();
        projectExample.createCriteria().andProjectNoEqualTo(projectNo);
        Project project = new Project();
        project.setStage(state);
        projectMapper.updateByExampleSelective(project, projectExample);
    }

    @Override
    public ContractMsgVo queryContractMsg(String projectNo) {
        Project project = queryProjectByNo(projectNo);
        CompanyInfoVo companyInfo = getCompanyMsg(projectNo);
        String ownerId = projectUserService.queryUserIdOne(projectNo, RoleFunctionEnum.OWNER_POWER);
        UserMsgVo userMsgVo = userService.queryUser(ownerId);
        ContractMsgVo contractMsgVo = new ContractMsgVo();
        contractMsgVo.setOwnerAddress(project.getAddressDetail());
        contractMsgVo.setOwnerCardNo(userMsgVo.getMemberEcode());
        contractMsgVo.setOwnerEmaile("");
        contractMsgVo.setOwnerName(userMsgVo.getUserName());
        contractMsgVo.setOwnerPhone(userMsgVo.getUserPhone());
        contractMsgVo.setCompanyName(companyInfo.getCompanyName());
        contractMsgVo.setCompanyLegalPerson(companyInfo.getLegalName());
        return contractMsgVo;
    }

    /**
     * 设计师发起量房预约详情页
     *
     * @param projectNo
     * @return
     */
    @Override
    public MyRespBundle<VolumeReservationDetailsVO> queryVolumeReservationDetails(String projectNo) {
        if (projectNo == null || projectNo.trim().isEmpty()) {
            return RespData.error("projectNo 不可为空!");
        }
        VolumeReservationDetailsVO volumeReservationDetailsVO = new VolumeReservationDetailsVO();
        ProjectExample example = new ProjectExample();
        ProjectExample.Criteria criteria = example.createCriteria();
        criteria.andProjectNoEqualTo(projectNo);
        List<Project> projects = projectMapper.selectByExample(example);
        if (projects.size() == 0) {
            return RespData.error("项目不存在!!");
        }
        Project project = projects.get(0);
        //添加业主信息
        PersionVo owner = new PersionVo();
        try {
            Map userName1 = newOrderUserService.getUserName(project.getOwnerId(), ProjectDataStatus.OWNER.getDescription());
            owner.setPhone(userName1.get("phone").toString());
            owner.setName(userName1.get("nickName").toString());
        } catch (Exception e) {
            e.printStackTrace();
            return RespData.error("调取人员信息失败!");
        }
        volumeReservationDetailsVO.setOwnerName(owner.getName());
        volumeReservationDetailsVO.setOwnerPhone(owner.getPhone());
        DesignerOrderExample designerOrderExample = new DesignerOrderExample();
        DesignerOrderExample.Criteria designCriteria = designerOrderExample.createCriteria();
        designCriteria.andProjectNoEqualTo(projectNo);
        designCriteria.andStatusEqualTo(ProjectDataStatus.BASE_STATUS.getValue());
        List<DesignerOrder> designerOrders = DesignerOrderMapper.selectByExample(designerOrderExample);
        if (designerOrders.size() == ProjectDataStatus.INSERT_FAILD.getValue()) {
            return RespData.error("查无此设计订单");
        }
        DesignerOrder designerOrder = designerOrders.get(0);
        OrderPlayVo designOrderPlayVo = DesignerOrderMapper.selectByProjectNoAndStatus(projectNo, ProjectDataStatus.BASE_STATUS.getValue());
        String designerId = projectUserService.queryUserIdOne(projectNo, RoleFunctionEnum.DESIGN_POWER);
        PersionVo persionVo = employeeMsgMapper.selectByUserId(designerId);
        if (persionVo != null){
            volumeReservationDetailsVO.setDesignerName(persionVo.getName());
        }
        volumeReservationDetailsVO.setDesignOrderNo(designerOrder.getOrderNo());
        //订单来源
        switch (project.getOrderSource()) {
            case 1:
                volumeReservationDetailsVO.setOrderSource("天猫");
                break;
            case 2:
                volumeReservationDetailsVO.setOrderSource("平台创建");
                break;
            case 3:
                volumeReservationDetailsVO.setOrderSource("设计公司创建");
                break;
            default:
                volumeReservationDetailsVO.setOrderSource("其他");
                break;
        }
        volumeReservationDetailsVO.setHouseType(project.getHouseRoom() + "室" + project.getHouseToilet() + "厅");
        volumeReservationDetailsVO.setPermanentResidents(project.getPeopleNo());
        volumeReservationDetailsVO.setArea(project.getArea());
        volumeReservationDetailsVO.setCompanyName(designOrderPlayVo.getConstructionCompany());
        volumeReservationDetailsVO.setPropertyType(project.getHouseType() == 1 ? "新房" : "旧房");
        volumeReservationDetailsVO.setDecorationLocation(project.getAddressDetail());
        volumeReservationDetailsVO.setMeasuringRoomLocation(project.getAddressDetail());
        if (designerOrder.getVolumeRoomTime() != null) {
            volumeReservationDetailsVO.setVolumeRoomDate(designerOrder.getVolumeRoomTime().getTime());
        }
        if (designerOrder.getVolumeRoomMoney() != null) {
            volumeReservationDetailsVO.setAppointmentAmount(MathUtil.getYuan(designerOrder.getVolumeRoomMoney()));
        }
        return RespData.success(volumeReservationDetailsVO);
    }

    private CompanyInfoVo getCompanyMsg(String projectNo) {
        CompanyInfoVo companyInfo;
        ConstructionOrderExample constructionOrderExample = new ConstructionOrderExample();
        constructionOrderExample.createCriteria().andProjectNoEqualTo(projectNo);
        List<ConstructionOrder> constructionOrders = constructionOrderMapper.selectByExample(constructionOrderExample);
        String companyId = null;
        if (!constructionOrders.isEmpty()) {
            companyId = constructionOrders.get(0).getCompanyId();
        } else {
            return null;
        }
        companyInfo = companyInfoMapper.selectByCompanyId(companyId);
        return companyInfo;
    }

    /**
     * app-C端确认量房
     *
     * @param projectNo
     * @param userId
     * @return
     */
    @Override
    public MyRespBundle confirmeVolumeRoom(String projectNo, String userId) {
        if (projectNo == null || projectNo.trim().isEmpty()) {
            return RespData.error("projectNo 不可为空!");
        }
        if (userId == null || userId.trim().isEmpty()) {
            return RespData.error("userId 不可为空!");
        }
        DesignerOrderExample designerOrderExample = new DesignerOrderExample();
        DesignerOrderExample.Criteria designCriteria = designerOrderExample.createCriteria();
        designCriteria.andProjectNoEqualTo(projectNo);
        designCriteria.andStatusEqualTo(ProjectDataStatus.BASE_STATUS.getValue());
        List<DesignerOrder> designerOrders = DesignerOrderMapper.selectByExample(designerOrderExample);
        if (designerOrders.size() == ProjectDataStatus.INSERT_FAILD.getValue()) {
            return RespData.error("查无此设计订单");
        }
        DesignerOrder designerOrder = designerOrders.get(0);
        updateOrderState(projectNo, DesignStateEnum.STATE_45.getState(), userId, "");
        createPayOrderService.createVolumeRoomPay(projectNo, MathUtil.getYuan(designerOrder.getVolumeRoomMoney()));
        return RespData.success();
    }
}
