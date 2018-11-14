package cn.thinkfree.service.platform.designer.impl;

import cn.thinkfree.core.constants.ConstructionStateEnumB;
import cn.thinkfree.core.constants.DesignStateEnum;
import cn.thinkfree.core.constants.ProjectSource;
import cn.thinkfree.core.constants.RoleFunctionEnum;
import cn.thinkfree.database.mapper.*;
import cn.thinkfree.database.model.*;
import cn.thinkfree.service.platform.basics.BasicsService;
import cn.thinkfree.service.platform.designer.DesignDispatchService;
import cn.thinkfree.service.platform.designer.DesignerService;
import cn.thinkfree.service.platform.designer.UserCenterService;
import cn.thinkfree.service.platform.employee.ProjectUserService;
import cn.thinkfree.service.platform.vo.*;
import cn.thinkfree.service.utils.DateUtils;
import cn.thinkfree.service.utils.ExcelUtil;
import cn.thinkfree.service.utils.OrderNoUtils;
import cn.thinkfree.service.utils.ReflectUtils;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author xusonghui
 * 设计派单服务实现类
 */
@Service
public class DesignDispatchServiceImpl implements DesignDispatchService {

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

    /**
     * 查询设计订单，主表为design_order,附表为project
     *
     * @param queryStage         具体查询阶段
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
            String queryStage, String companyId, String projectNo, String userMsg, String orderSource, String createTimeStart, String createTimeEnd,
            String styleCode, String money, String acreage, int designerOrderState, String companyState, String optionUserName,
            String optionTimeStart, String optionTimeEnd, int pageSize, int pageIndex, int stateType) {
        //TODO 模糊查询用户信息
        List<String> userIds = new ArrayList<>();
        //TODO 根据公司状态查询公司ID
        List<String> companyIds = new ArrayList<>();
        ProjectExample projectExample = new ProjectExample();
        ProjectExample.Criteria projectCriteria = projectExample.createCriteria();
        if (StringUtils.isBlank(companyId)) {
            throw new RuntimeException("公司缺失");
        }
        if (!userIds.isEmpty()) {
            projectCriteria.andOwnerIdIn(ReflectUtils.listToList(userIds));
        }
        if (StringUtils.isNotBlank(orderSource)) {
            projectCriteria.andOrderSourceEqualTo(Integer.parseInt(orderSource));
        }
        if (StringUtils.isNotBlank(projectNo)) {
            projectCriteria.andProjectNoLike(projectNo);
        }
        if (StringUtils.isNotBlank(money)) {
            projectCriteria.andDecorationBudgetEqualTo(Integer.parseInt(money));
        }
        List<Project> projects = projectMapper.selectByExample(projectExample);
        Map<String, Project> projectMap = ReflectUtils.listToMap(projects, "projectNo");
        List<String> projectNos = ReflectUtils.getList(projects, "projectNo");
        OptionLogExample optionLogExample = new OptionLogExample();
        OptionLogExample.Criteria logExampleCriteria = optionLogExample.createCriteria();
        if (StringUtils.isNotBlank(optionUserName)) {
            logExampleCriteria.andOptionUserNameLike(optionUserName);
        }
        if (StringUtils.isNotBlank(optionTimeStart)) {
            logExampleCriteria.andOptionTimeGreaterThanOrEqualTo(DateUtils.strToDate(optionTimeStart));
        }
        if (StringUtils.isNotBlank(optionTimeEnd)) {
            logExampleCriteria.andOptionTimeLessThanOrEqualTo(DateUtils.strToDate(optionTimeEnd));
        }
        List<OptionLog> optionLogs = optionLogMapper.selectByExample(optionLogExample);
        List<String> orderNos = ReflectUtils.getList(optionLogs, "linkNo");
        DesignerOrderExample orderExample = new DesignerOrderExample();
        DesignerOrderExample.Criteria orderExampleCriteria = orderExample.createCriteria();
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
        if("DOCL".equals(queryStage)){
            List<Integer> orderStates = Arrays.asList(DesignStateEnum.STATE_130.getState());
            orderExampleCriteria.andOrderStageIn(orderStates);
        }
        if (!projectNos.isEmpty()) {
            orderExampleCriteria.andProjectNoIn(projectNos);
        }
        if (!orderNos.isEmpty()) {
            orderExampleCriteria.andOrderNoIn(orderNos);
        }
        long total = DesignerOrderMapper.countByExample(orderExample);
        PageHelper.startPage(pageIndex - 1, pageSize);
        List<DesignerOrder> DesignerOrders = DesignerOrderMapper.selectByExample(orderExample);
        List<DesignerOrderVo> DesignerOrderVos = new ArrayList<>();
        Map<String, DesignerStyleConfigVo> designerStyleConfigMap = queryDesignerStyleConfig();
        userIds = new ArrayList<>();
        for (DesignerOrder DesignerOrder : DesignerOrders) {
            Project project = projectMap.get(DesignerOrder.getProjectNo());
            if (!userIds.contains(DesignerOrder.getUserId())) {
                userIds.add(DesignerOrder.getUserId());
            }
            if (!userIds.contains(project.getOwnerId())) {
                userIds.add(project.getOwnerId());
            }
        }
        Map<String, UserMsgVo> msgVoMap = userService.queryUserMap(userIds);
        for (DesignerOrder DesignerOrder : DesignerOrders) {
            Project project = projectMap.get(DesignerOrder.getProjectNo());
            DesignerOrderVo DesignerOrderVo = getDesignerOrderVo(stateType, designerStyleConfigMap, DesignerOrder, project, msgVoMap);
            DesignerOrderVos.add(DesignerOrderVo);
        }
        PageVo<List<DesignerOrderVo>> pageVo = new PageVo<>();
        pageVo.setPageSize(pageSize);
        pageVo.setTotal(total);
        pageVo.setData(DesignerOrderVos);
        pageVo.setPageIndex(pageIndex);
        return pageVo;
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
    private DesignerOrderVo getDesignerOrderVo(int stateType, Map<String, DesignerStyleConfigVo> designerStyleConfigMap, DesignerOrder DesignerOrder,
                                               Project project, Map<String, UserMsgVo> msgVoMap) {
        DesignerOrderVo DesignerOrderVo = new DesignerOrderVo();
        DesignerOrderVo.setProjectNo(project.getProjectNo());
        DesignerOrderVo.setDesignOrderNo(DesignerOrder.getOrderNo());
        UserMsgVo ownerMsg = msgVoMap.get(project.getOwnerId());
        if (ownerMsg != null) {
            DesignerOrderVo.setOwnerName(ownerMsg.getUserName());
            DesignerOrderVo.setOwnerPhone(ownerMsg.getUserPhone());
        }
        DesignerOrderVo.setAddress(project.getAddressDetail());
        DesignerOrderVo.setOrderSource(ProjectSource.queryByState(project.getOrderSource()).getSourceName());
        DesignerOrderVo.setCreateTime(DateUtils.dateToStr(project.getCreateTime()));
        DesignerStyleConfigVo designerStyleConfig = designerStyleConfigMap.get(DesignerOrder.getStyleType());
        if (designerStyleConfig != null) {
            DesignerOrderVo.setStyleName(designerStyleConfig.getStyleName());
        }
        DesignerOrderVo.setBudget(project.getDecorationBudget() + "");
        DesignerOrderVo.setArea(project.getArea() + "");
        DesignerOrderVo.setCompanyName("--");
        DesignerOrderVo.setCompanyState("--");
        UserMsgVo designMsg = msgVoMap.get(project.getOwnerId());
        if (designMsg != null) {
            DesignerOrderVo.setDesignerName(designMsg.getRealName());
        }
        DesignerOrderVo.setOrderStateName(DesignStateEnum.queryByState(DesignerOrder.getOrderStage()).getStateName(stateType));
        DesignerOrderVo.setOptionUserName("----");
        DesignerOrderVo.setOrderState(DesignerOrder.getOrderStage() + "");
        DesignerOrderVo.setOptionTime("----");
        DesignerOrderVo.setProjectMoney(project.getDecorationBudget() + "");
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
    public void designerOrderExcel(String companyId, String projectNo, String userMsg, String orderSource, String createTimeStart, String createTimeEnd,
                                   String styleCode, String money, String acreage, int designerOrderState, String companyState, String optionUserName,
                                   String optionTimeStart, String optionTimeEnd, int stateType, String fileName, HttpServletResponse response) {
        PageVo<List<DesignerOrderVo>> pageVo = queryDesignerOrder(null, companyId, projectNo, userMsg, orderSource, createTimeStart, createTimeEnd, styleCode,
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
     * @param projectNo    项目编号
     * @param contractType 合同类型，1全款合同，2分期合同
     * @param companyId    公司ID
     * @param optionId     操作人ID
     * @param optionName   操作人名称
     */
    @Override
    public void reviewPass(String projectNo, int contractType, String companyId, String optionId, String optionName) {
        if (contractType != 1 && contractType != 2) {
            throw new RuntimeException("必须声明合同类型");
        }
        DesignStateEnum stateEnum = DesignStateEnum.STATE_220;
        if (contractType == 2) {
            stateEnum = DesignStateEnum.STATE_140;
        }
        Project project = queryProjectByNo(projectNo);
        DesignerOrder designerOrder = queryDesignerOrder(projectNo);
        if (!designerOrder.getCompanyId().equals(companyId)) {
            throw new RuntimeException("无权操作");
        }
        DesignerOrder updateOrder = new DesignerOrder();
        updateOrder.setId(designerOrder.getId());
        updateOrder.setContractType(contractType);
        updateOrder.setOrderStage(stateEnum.getState());
        DesignerOrderMapper.updateByPrimaryKeySelective(updateOrder);
        //记录操作日志
        saveOptionLog(designerOrder.getOrderNo(), optionId, optionName, "合同审核通过");
        saveLog(stateEnum.getState(), project);
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
        constructionOrder.setOrderStage(ConstructionStateEnumB.STATE_500.getState());
        // 1小包，2大包
        if (project.getContractType() == 2) {
            DesignerOrder designerOrders = queryDesignerOrder(projectNo);
            String companyId = designerOrders.getCompanyId();
            constructionOrder.setCompanyId(companyId);
            constructionOrder.setOrderStage(ConstructionStateEnumB.STATE_520.getState());
        }
        constructionOrderMapper.insertSelective(constructionOrder);
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
        checkOrderState(designerOrders, DesignStateEnum.STATE_999);
        //设置该设计订单所属公司
        DesignerOrder updateOrder = new DesignerOrder();
        updateOrder.setOrderStage(DesignStateEnum.STATE_999.getState());
        DesignerOrderExample orderExample = new DesignerOrderExample();
        orderExample.createCriteria().andOrderNoEqualTo(designerOrders.getOrderNo());
        DesignerOrderMapper.updateByExampleSelective(updateOrder, orderExample);
        //记录操作日志
        saveOptionLog(designerOrders.getOrderNo(), optionUserId, optionUserName, reason);
        saveLog(DesignStateEnum.STATE_999.getState(), project);
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
    }

    @Override
    public DesignOrderDelVo queryDesignerOrderVoByProjectNo(String projectNo, int stateType) {
        Map<String, DesignerStyleConfigVo> designerStyleConfigMap = queryDesignerStyleConfig();
        DesignerOrder designerOrder = queryDesignerOrder(projectNo);
        Project project = queryProjectByNo(projectNo);
        List<String> userIds = new ArrayList<>();
        userIds.add(designerOrder.getUserId());
        userIds.add(project.getOwnerId());
        Map<String, UserMsgVo> msgVoMap = userService.queryUserMap(userIds);
        DesignerOrderVo designerOrderVo = getDesignerOrderVo(stateType, designerStyleConfigMap, designerOrder, project, msgVoMap);
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
    }

    /**
     * 设计师发起量房预约
     *
     * @param projectNo      项目编号
     * @param designerUserId 设计师ID
     * @param volumeRoomDate 预约时间
     */
    @Override
    public void makeAnAppointmentVolumeRoom(String projectNo, String designerUserId, String volumeRoomDate) {
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
        DesignerOrderExample orderExample = new DesignerOrderExample();
        orderExample.createCriteria().andOrderNoEqualTo(designerOrder.getOrderNo());
        DesignerOrderMapper.updateByExampleSelective(updateOrder, orderExample);
        //TODO 需要发出通知给业主
        //记录操作日志
        String remark = "设计师【" + optionUserName + "】发起量房预约";
        saveOptionLog(designerOrder.getOrderNo(), designerUserId, optionUserName, remark);
        saveLog(DesignStateEnum.STATE_40.getState(), project);
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
        remindOwnerLogMapper.insertSelective(remindOwnerLog);
    }

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
            createConstructionOrder(projectNo);
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
            createConstructionOrder(projectNo);
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
            case STATE_40:
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
        if (stateEnum == DesignStateEnum.STATE_270 || stateEnum == DesignStateEnum.STATE_210) {
            createConstructionOrder(project.getProjectNo());
        }
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
                timeOutState = DesignStateEnum.STATE_80;
                break;
            case STATE_140:
            case STATE_170:
            case STATE_220:
                timeOutState = DesignStateEnum.STATE_280;
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
        try {
            checkOrderState(designerOrder, DesignStateEnum.STATE_330);
        } catch (Exception e) {
            throw new RuntimeException("当前订单不可取消，如有疑问，请联系客服");
        }
        DesignerOrder updateOrder = new DesignerOrder();
        //清空设计师ID
        updateOrder.setOrderStage(DesignStateEnum.STATE_330.getState());
        DesignerOrderExample orderExample = new DesignerOrderExample();
        orderExample.createCriteria().andOrderNoEqualTo(designerOrder.getOrderNo());
        DesignerOrderMapper.updateByExampleSelective(updateOrder, orderExample);
        //TODO 需要发出通知给业主
        //记录操作日志
        String remark = DesignStateEnum.STATE_330.getLogText();
        saveOptionLog(designerOrder.getOrderNo(), userId, "业主", remark);
        saveLog(DesignStateEnum.STATE_330.getState(), project);
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
            throw new RuntimeException("没有查询到该设计师");
        }
        return designerMsgs.get(0);
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
     * @return ["LFFY(提醒支付量房费用)","LFZL(提交量房资料)","HTQY(发起合同签约)","SJZL(提交设计资料)","CKHT(查看合同)"]
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
            case STATE_40:
                btns.add("LFFY");
                break;
            case STATE_50:
                btns.add("LFZL");
                break;
            case STATE_130:
                btns.add("HTQY");
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
            case STATE_180:
            case STATE_230:
            case STATE_250:
                btns.add("SJZL");
                btns.add("CKHT");
                break;
        }
        return btns;
    }
}
