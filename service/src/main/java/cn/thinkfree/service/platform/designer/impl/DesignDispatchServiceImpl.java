package cn.thinkfree.service.platform.designer.impl;

import cn.thinkfree.core.constants.DesignStateEnum;
import cn.thinkfree.core.constants.ProjectSource;
import cn.thinkfree.database.mapper.*;
import cn.thinkfree.database.model.*;
import cn.thinkfree.service.platform.designer.DesignDispatchService;
import cn.thinkfree.service.platform.designer.UserCenterService;
import cn.thinkfree.service.platform.designer.vo.DesignOrderVo;
import cn.thinkfree.service.platform.designer.vo.PageVo;
import cn.thinkfree.service.platform.designer.vo.UserMsgVo;
import cn.thinkfree.service.utils.DateUtils;
import cn.thinkfree.service.utils.OrderNoUtils;
import cn.thinkfree.service.utils.ReflectUtils;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
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
    private DesignOrderMapper designOrderMapper;
    @Autowired
    private DesignerMsgMapper designerMsgMapper;
    @Autowired
    private EmployeeMsgMapper employeeMsgMapper;
    @Autowired
    private RemindOwnerLogMapper remindOwnerLogMapper;
    @Autowired
    private DesignerStyleConfigMapper designerStyleConfigMapper;
    @Autowired
    private UserCenterService userService;

    /**
     * 查询设计订单，主表为design_order,附表为project
     *
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
    public PageVo<List<DesignOrderVo>> queryDesignerOrder(
            String companyId, String projectNo, String userMsg, String orderSource, String createTimeStart, String createTimeEnd,
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
        projectCriteria.andCompanyIdEqualTo(companyId);
        if (!userIds.isEmpty()) {
            projectCriteria.andOwnerIdIn(ReflectUtils.listToList(userIds));
        }
        if (!companyIds.isEmpty()) {
            projectCriteria.andCompanyIdIn(ReflectUtils.listToList(companyIds));
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
        DesignOrderExample orderExample = new DesignOrderExample();
        DesignOrderExample.Criteria orderExampleCriteria = orderExample.createCriteria();
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
        if (!projectNos.isEmpty()) {
            orderExampleCriteria.andProjectNoIn(projectNos);
        }
        if (!orderNos.isEmpty()) {
            orderExampleCriteria.andOrderNoIn(orderNos);
        }
        long total = designOrderMapper.countByExample(orderExample);
        PageHelper.startPage(pageIndex - 1, pageSize);
        List<DesignOrder> designOrders = designOrderMapper.selectByExample(orderExample);
        List<DesignOrderVo> designOrderVos = new ArrayList<>();
        Map<String, DesignerStyleConfig> designerStyleConfigMap = queryDesignerStyleConfig();
        userIds = new ArrayList<>();
        for (DesignOrder designOrder : designOrders) {
            Project project = projectMap.get(designOrder.getProjectNo());
            if (!userIds.contains(designOrder.getUserId())) {
                userIds.add(designOrder.getUserId());
            }
            if (!userIds.contains(project.getOwnerId())) {
                userIds.add(project.getOwnerId());
            }
        }
        Map<String, UserMsgVo> msgVoMap = userService.queryUserMap(userIds);
        for (DesignOrder designOrder : designOrders) {
            Project project = projectMap.get(designOrder.getProjectNo());
            DesignOrderVo designOrderVo = getDesignOrderVo(stateType, designerStyleConfigMap, designOrder, project, msgVoMap);
            designOrderVos.add(designOrderVo);
        }
        PageVo<List<DesignOrderVo>> pageVo = new PageVo<>();
        pageVo.setPageSize(pageSize);
        pageVo.setTotal(total);
        pageVo.setData(designOrderVos);
        return pageVo;
    }

    /**
     * 返回一个设计订单详情
     *
     * @param stateType              1获取平台状态，2获取设计公司状态，3获取设计师状态，4获取消费者状态
     * @param designerStyleConfigMap
     * @param designOrder            设计订单信息
     * @param project                项目信息
     * @param msgVoMap               用户信息
     * @return
     */
    @NotNull
    private DesignOrderVo getDesignOrderVo(int stateType, Map<String, DesignerStyleConfig> designerStyleConfigMap, DesignOrder designOrder,
                                           Project project, Map<String, UserMsgVo> msgVoMap) {
        DesignOrderVo designOrderVo = new DesignOrderVo();
        designOrderVo.setProjectNo(project.getProjectNo());
        designOrderVo.setDesignOrderNo(designOrder.getOrderNo());
        UserMsgVo ownerMsg = msgVoMap.get(project.getOwnerId());
        if(ownerMsg != null){
            designOrderVo.setOwnerName(ownerMsg.getUserName());
            designOrderVo.setOwnerPhone(ownerMsg.getUserPhone());
        }
        designOrderVo.setAddress(project.getAddress());
        designOrderVo.setOrderSource(ProjectSource.queryByState(project.getOrderSource()).getSourceName());
        designOrderVo.setCreateTime(DateUtils.dateToStr(project.getCreateTime()));
        DesignerStyleConfig designerStyleConfig = designerStyleConfigMap.get(designOrder.getStyleType());
        if (designerStyleConfig != null) {
            designOrderVo.setStyleName(designerStyleConfig.getStyleName());
        }
        designOrderVo.setBudget(project.getDecorationBudget() + "");
        designOrderVo.setArea(project.getArea() + "");
        designOrderVo.setCompanyName("--");
        designOrderVo.setCompanyState("--");
        UserMsgVo designMsg = msgVoMap.get(project.getOwnerId());
        if(designMsg != null){
            designOrderVo.setDesignerName(designMsg.getRealName());
        }
        designOrderVo.setOrderStateName(DesignStateEnum.queryByState(designOrder.getOrderStage()).getStateName(stateType));
        designOrderVo.setOptionUserName("----");
        designOrderVo.setOptionTime("----");
        return designOrderVo;
    }

    /**
     * 查询设计风格
     *
     * @return
     */
    private Map<String, DesignerStyleConfig> queryDesignerStyleConfig() {
        DesignerStyleConfigExample designerStyleConfigExample = new DesignerStyleConfigExample();
        designerStyleConfigExample.createCriteria();
        List<DesignerStyleConfig> styleConfigs = designerStyleConfigMapper.selectByExample(designerStyleConfigExample);
        return ReflectUtils.listToMap(styleConfigs, "styleCode");
    }

    @Override
    public void designOrderExcel(String companyId, String projectNo, String userMsg, String orderSource, String createTimeStart, String createTimeEnd,
                                 String styleCode, String money, String acreage, int designerOrderState, String companyState, String optionUserName,
                                 String optionTimeStart, String optionTimeEnd, int stateType, String fileName, HttpServletResponse response) {
        PageVo<List<DesignOrderVo>> pageVo = queryDesignerOrder(companyId, projectNo, userMsg, orderSource, createTimeStart, createTimeEnd, styleCode,
                money, acreage, designerOrderState, companyState, optionUserName, optionTimeStart, optionTimeEnd, 1000000, 1, stateType);

        List<List<String>> lists = new ArrayList<>();
        lists.add(Arrays.asList("序号", "订单编号", "订单子编号", "业主姓名", "业主电话", "所在地", "订单来源", "创建时间", "装饰风格", "建筑面积", "建筑预算", "归属设计公司", "公司状态", "归属设计师"
                , "订单状态", "操作人", "操作时间"));
        List<DesignOrderVo> designOrderVos = pageVo.getData();
        for (DesignOrderVo designOrderVo : designOrderVos) {

        }
    }


    @Override
    public void reviewPass(String projectNo, int contractType, String companyId, String optionId, String optionName) {
        if (contractType != 1 && contractType != 2) {
            throw new RuntimeException("必须声明合同类型");
        }
        DesignStateEnum stateEnum = DesignStateEnum.STATE_220;
        if (contractType == 2) {
            stateEnum = DesignStateEnum.STATE_140;
        }
        DesignOrder designOrder = queryDesignOrder(projectNo);
        if (!designOrder.getCompanyId().equals(companyId)) {
            throw new RuntimeException("无权操作");
        }
        DesignOrder updateOrder = new DesignOrder();
        updateOrder.setId(designOrder.getId());
        updateOrder.setContractType(contractType);
        updateOrder.setOrderStage(stateEnum.getState());
        designOrderMapper.updateByPrimaryKeySelective(updateOrder);
        //记录操作日志
        saveOptionLog(designOrder.getOrderNo(), optionId, optionName, "合同审核通过");
    }

    @Override
    public void setDesignId(String projectNo, String designId, String designerId) {
        Project project = queryProjectByNo(projectNo);
        DesignOrder designOrder = queryDesignOrder(projectNo);
        if (!designerId.equals(designOrder.getUserId())) {
            throw new RuntimeException("无权操作");
        }
        DesignOrder updateOrder = new DesignOrder();
        updateOrder.setDesignId(designId);
        updateOrder.setId(designOrder.getId());
        designOrderMapper.updateByPrimaryKeySelective(updateOrder);
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
        DesignOrder designOrder = queryDesignOrder(projectNo);
        // 1小包，2大包
        if (project.getContractType() == 2) {
            String companyId = designOrder.getCompanyId();
            constructionOrder.setCompanyId(companyId);
            //TODO 待添加状态
        }

        //constructionOrderMapper.insertSelective(constructionOrder);
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
        DesignOrder designOrder = queryDesignOrder(projectNo);
        checkOrderState(designOrder, DesignStateEnum.STATE_999);
        //设置该设计订单所属公司
        DesignOrder updateOrder = new DesignOrder();
        updateOrder.setOrderStage(DesignStateEnum.STATE_999.getState());
        DesignOrderExample orderExample = new DesignOrderExample();
        orderExample.createCriteria().andOrderNoEqualTo(designOrder.getOrderNo());
        designOrderMapper.updateByExampleSelective(updateOrder, orderExample);
        //记录操作日志
        saveOptionLog(designOrder.getOrderNo(), optionUserId, optionUserName, reason);
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
        DesignOrder designOrder = queryDesignOrder(projectNo);
        //设置该设计订单所属公司
        checkOrderState(designOrder, DesignStateEnum.STATE_10);
        DesignOrder updateOrder = new DesignOrder();
        updateOrder.setCompanyId(companyId);
        updateOrder.setOrderStage(DesignStateEnum.STATE_10.getState());
        DesignOrderExample orderExample = new DesignOrderExample();
        orderExample.createCriteria().andOrderNoEqualTo(designOrder.getOrderNo());
        designOrderMapper.updateByExampleSelective(updateOrder, orderExample);
        //记录操作日志
        String remark = "指派订单给公司【" + companyId + "】";
        saveOptionLog(designOrder.getOrderNo(), optionUserId, optionUserName, remark);
    }

    @Override
    public DesignOrderVo queryDesignOrderVoByProjectNo(String projectNo, int stateType) {
        Map<String, DesignerStyleConfig> designerStyleConfigMap = queryDesignerStyleConfig();
        DesignOrder designOrder = queryDesignOrder(projectNo);
        Project project = queryProjectByNo(projectNo);
        List<String> userIds = new ArrayList<>();
        userIds.add(designOrder.getUserId());
        userIds.add(project.getOwnerId());
        Map<String, UserMsgVo> msgVoMap = userService.queryUserMap(userIds);
        DesignOrderVo designOrderVo = getDesignOrderVo(stateType, designerStyleConfigMap, designOrder, project, msgVoMap);
        return designOrderVo;
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
        DesignOrder designOrder = queryDesignOrder(projectNo);
        if (!designOrder.getCompanyId().equals(companyId)) {
            throw new RuntimeException("无权操作");
        }
        checkOrderState(designOrder, DesignStateEnum.STATE_1);
        //设置该设计订单所属公司
        DesignOrder updateOrder = new DesignOrder();
        updateOrder.setCompanyId("");
        updateOrder.setOrderStage(DesignStateEnum.STATE_1.getState());
        DesignOrderExample orderExample = new DesignOrderExample();
        orderExample.createCriteria().andOrderNoEqualTo(designOrder.getOrderNo());
        designOrderMapper.updateByExampleSelective(updateOrder, orderExample);
        //记录操作日志
        String remark = "公司编号为【" + companyId + "】的公司拒绝接单，拒绝原因：" + reason;
        saveOptionLog(designOrder.getOrderNo(), optionUserId, optionUserName, remark);
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
    @Override
    public void assignDesigner(String projectNo, String companyId, String designerUserId, String optionUserId, String optionUserName) {
        Project project = queryProjectByNo(projectNo);
        DesignOrder designOrder = queryDesignOrder(projectNo);
        if (!designOrder.getCompanyId().equals(companyId)) {
            throw new RuntimeException("无权操作");
        }
        checkOrderState(designOrder, DesignStateEnum.STATE_20);
        //设置该设计订单所属公司
        DesignOrder updateOrder = new DesignOrder();
        //设置设计师ID
        updateOrder.setUserId(designerUserId);
        updateOrder.setOrderStage(DesignStateEnum.STATE_20.getState());
        DesignOrderExample orderExample = new DesignOrderExample();
        orderExample.createCriteria().andOrderNoEqualTo(designOrder.getOrderNo());
        designOrderMapper.updateByExampleSelective(updateOrder, orderExample);
        //TODO 需要发出通知给设计师
        //记录操作日志
        String remark = "公司编号为【" + companyId + "】的公司指派设计师";
        saveOptionLog(designOrder.getOrderNo(), optionUserId, optionUserName, remark);
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
        DesignOrder designOrder = queryDesignOrder(projectNo);
        if (!designOrder.getUserId().equals(designerUserId)) {
            throw new RuntimeException("无权操作");
        }
        checkOrderState(designOrder, DesignStateEnum.STATE_10);
        queryDesignerMsg(designerUserId);
        EmployeeMsg employeeMsg = queryEmployeeMsg(designerUserId);
        String optionUserName = employeeMsg.getRealName();
        //设置该设计订单所属公司
        DesignOrder updateOrder = new DesignOrder();
        //清空设计师ID
        updateOrder.setUserId("");
        updateOrder.setOrderStage(DesignStateEnum.STATE_10.getState());
        DesignOrderExample orderExample = new DesignOrderExample();
        orderExample.createCriteria().andOrderNoEqualTo(designOrder.getOrderNo());
        designOrderMapper.updateByExampleSelective(updateOrder, orderExample);
        //TODO 需要发出通知给设计师
        //记录操作日志
        String remark = "设计师【" + optionUserName + "】拒绝接单";
        saveOptionLog(designOrder.getOrderNo(), designerUserId, optionUserName, remark);
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
        DesignOrder designOrder = queryDesignOrder(projectNo);
        if (!designOrder.getUserId().equals(designerUserId)) {
            throw new RuntimeException("无权操作");
        }
        checkOrderState(designOrder, DesignStateEnum.STATE_30);
        queryDesignerMsg(designerUserId);
        EmployeeMsg employeeMsg = queryEmployeeMsg(designerUserId);
        String optionUserName = employeeMsg.getRealName();
        //设置该设计订单所属公司
        DesignOrder updateOrder = new DesignOrder();
        //清空设计师ID
        updateOrder.setOrderStage(DesignStateEnum.STATE_30.getState());
        DesignOrderExample orderExample = new DesignOrderExample();
        orderExample.createCriteria().andOrderNoEqualTo(designOrder.getOrderNo());
        designOrderMapper.updateByExampleSelective(updateOrder, orderExample);
        //TODO 需要发出通知给设计师
        //记录操作日志
        String remark = "设计师【" + optionUserName + "】已接单";
        saveOptionLog(designOrder.getOrderNo(), designerUserId, optionUserName, remark);
    }

    /**
     * 设计师发起量房预约
     *
     * @param projectNo      项目编号
     * @param designerUserId 设计师ID
     */
    @Override
    public void makeAnAppointmentVolumeRoom(String projectNo, String designerUserId) {
        //设计师接单
        Project project = queryProjectByNo(projectNo);
        DesignOrder designOrder = queryDesignOrder(projectNo);
        if (!designOrder.getUserId().equals(designerUserId)) {
            throw new RuntimeException("无权操作");
        }
        checkOrderState(designOrder, DesignStateEnum.STATE_40);
        queryDesignerMsg(designerUserId);
        EmployeeMsg employeeMsg = queryEmployeeMsg(designerUserId);
        String optionUserName = employeeMsg.getRealName();
        //设置该设计订单所属公司
        DesignOrder updateOrder = new DesignOrder();
        //清空设计师ID
        updateOrder.setOrderStage(DesignStateEnum.STATE_40.getState());
        DesignOrderExample orderExample = new DesignOrderExample();
        orderExample.createCriteria().andOrderNoEqualTo(designOrder.getOrderNo());
        designOrderMapper.updateByExampleSelective(updateOrder, orderExample);
        //TODO 需要发出通知给业主
        //记录操作日志
        String remark = "设计师【" + optionUserName + "】发起量房预约";
        saveOptionLog(designOrder.getOrderNo(), designerUserId, optionUserName, remark);
    }

    @Override
    public void remindOwner(String projectNo, String designerUserId) {
        //设计师接单
        Project project = queryProjectByNo(projectNo);
        DesignOrder designOrder = queryDesignOrder(projectNo);
        if (!designOrder.getUserId().equals(designerUserId)) {
            throw new RuntimeException("无权操作");
        }
        RemindOwnerLogExample logExample = new RemindOwnerLogExample();
        //24小时内只能提示一次
        logExample.createCriteria().andDesignOrderNoEqualTo(designOrder.getOrderNo())
                .andRemindTimeGreaterThanOrEqualTo(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24));
        List<RemindOwnerLog> remindOwnerLogs = remindOwnerLogMapper.selectByExample(logExample);
        if (!remindOwnerLogs.isEmpty()) {
            throw new RuntimeException("每24小时能只能提示一次~");
        }
        RemindOwnerLog remindOwnerLog = new RemindOwnerLog();
        remindOwnerLog.setDesignOrderNo(designOrder.getOrderNo());
        remindOwnerLog.setOwnerId(project.getOwnerId());
        remindOwnerLog.setRemindTime(new Date());
        remindOwnerLogMapper.insertSelective(remindOwnerLog);
    }

    @Override
    public void updateOrderState(String projectNo, int orderState, String optionId, String optionName) {
        //设计师接单
        Project project = queryProjectByNo(projectNo);
        DesignOrder designOrder = queryDesignOrder(projectNo);
        DesignStateEnum stateEnum = DesignStateEnum.queryByState(orderState);
        checkOrderState(designOrder, stateEnum);
        //设置该设计订单所属公司
        DesignOrder updateOrder = new DesignOrder();
        //清空设计师ID
        updateOrder.setOrderStage(orderState);
        DesignOrderExample orderExample = new DesignOrderExample();
        orderExample.createCriteria().andOrderNoEqualTo(designOrder.getOrderNo());
        designOrderMapper.updateByExampleSelective(updateOrder, orderExample);
        //TODO 需要发出通知给业主
        //记录操作日志
        String remark = stateEnum.getLogText();
        saveOptionLog(designOrder.getOrderNo(), optionId, optionName, remark);
    }

    @Override
    public void updateOrderState(String projectNo, int orderState, String optionId, String optionName, String reason) {
        //设计师接单
        Project project = queryProjectByNo(projectNo);
        DesignOrder designOrder = queryDesignOrder(projectNo);
        DesignStateEnum stateEnum = DesignStateEnum.queryByState(orderState);
        checkOrderState(designOrder, stateEnum);
        //设置该设计订单所属公司
        DesignOrder updateOrder = new DesignOrder();
        //清空设计师ID
        updateOrder.setOrderStage(orderState);
        DesignOrderExample orderExample = new DesignOrderExample();
        orderExample.createCriteria().andOrderNoEqualTo(designOrder.getOrderNo());
        designOrderMapper.updateByExampleSelective(updateOrder, orderExample);
        //TODO 需要发出通知给业主
        //记录操作日志
        saveOptionLog(designOrder.getOrderNo(), optionId, optionName, reason);
    }

    @Override
    public void confirmedDeliveries(String projectNo, String optionId) {
        //设计师接单
        Project project = queryProjectByNo(projectNo);
        DesignOrder designOrder = queryDesignOrder(projectNo);
        checkOrderState(designOrder, DesignStateEnum.STATE_70);
        //设置该设计订单所属公司
        DesignOrder updateOrder = new DesignOrder();
        //清空设计师ID
        updateOrder.setOrderStage(DesignStateEnum.STATE_70.getState());
        DesignOrderExample orderExample = new DesignOrderExample();
        orderExample.createCriteria().andOrderNoEqualTo(designOrder.getOrderNo());
        designOrderMapper.updateByExampleSelective(updateOrder, orderExample);
        //TODO 需要发出通知给业主
        //记录操作日志
        String remark = DesignStateEnum.STATE_70.getLogText();
        saveOptionLog(designOrder.getOrderNo(), optionId, "业主", remark);
    }


    @Override
    public void paySuccess(String orderNo) {
        //设计师接单
        DesignOrder designOrder = queryDesignOrderByOrderNo(orderNo);
        DesignStateEnum designStateEnum = DesignStateEnum.queryByState(designOrder.getOrderStage());
        //设置该设计订单所属公司
        DesignOrder updateOrder = new DesignOrder();
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
        DesignOrderExample orderExample = new DesignOrderExample();
        orderExample.createCriteria().andOrderNoEqualTo(designOrder.getOrderNo());
        designOrderMapper.updateByExampleSelective(updateOrder, orderExample);
        //TODO 需要发出通知给业主
        //记录操作日志
        String remark = stateEnum.getLogText();
        saveOptionLog(designOrder.getOrderNo(), "system", "system", remark);
    }


    @Override
    public void payTimeOut(String projectNo) {
        //设计师接单
        Project project = queryProjectByNo(projectNo);
        DesignOrder designOrder = queryDesignOrder(projectNo);
        DesignStateEnum designStateEnum = DesignStateEnum.queryByState(designOrder.getOrderStage());
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
        DesignOrder designOrder = queryDesignOrder(projectNo);
        if (!project.getOwnerId().equals(userId)) {
            throw new RuntimeException("无权操作");
        }
        checkOrderState(designOrder, DesignStateEnum.STATE_330);
        DesignOrder updateOrder = new DesignOrder();
        //清空设计师ID
        updateOrder.setOrderStage(DesignStateEnum.STATE_330.getState());
        DesignOrderExample orderExample = new DesignOrderExample();
        orderExample.createCriteria().andOrderNoEqualTo(designOrder.getOrderNo());
        designOrderMapper.updateByExampleSelective(updateOrder, orderExample);
        //TODO 需要发出通知给业主
        //记录操作日志
        String remark = DesignStateEnum.STATE_330.getLogText();
        saveOptionLog(designOrder.getOrderNo(), userId, "业主", remark);
    }

    /**
     * 检查设计订单状态
     *
     * @param designOrder     设计订单
     * @param designStateEnum 目标状态
     */
    @Override
    public void checkOrderState(DesignOrder designOrder, DesignStateEnum designStateEnum) {
        int orderState = designOrder.getOrderStage();
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
    public DesignOrder queryDesignOrder(String projectNo) {
        DesignOrderExample orderExample = new DesignOrderExample();
        orderExample.createCriteria().andProjectNoEqualTo(projectNo).andStatusEqualTo(1);
        List<DesignOrder> designOrders = designOrderMapper.selectByExample(orderExample);
        if (designOrders.isEmpty()) {
            throw new RuntimeException("没有查询到相关设计订单");
        }
        return designOrders.get(0);
    }

    /**
     * 根据项目编号查询设计订单
     *
     * @param orderNo 设计订单编号
     * @return
     */
    @Override
    public DesignOrder queryDesignOrderByOrderNo(String orderNo) {
        DesignOrderExample orderExample = new DesignOrderExample();
        orderExample.createCriteria().andOrderNoEqualTo(orderNo).andStatusEqualTo(1);
        List<DesignOrder> designOrders = designOrderMapper.selectByExample(orderExample);
        if (designOrders.isEmpty()) {
            throw new RuntimeException("没有查询到相关设计订单");
        }
        return designOrders.get(0);
    }
}
