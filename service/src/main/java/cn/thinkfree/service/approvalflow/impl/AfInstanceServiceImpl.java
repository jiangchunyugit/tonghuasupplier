package cn.thinkfree.service.approvalflow.impl;

import cn.thinkfree.core.constants.AfConfigs;
import cn.thinkfree.core.constants.AfConstants;
import cn.thinkfree.core.constants.Role;
import cn.thinkfree.core.exception.CommonException;
import cn.thinkfree.core.utils.JSONUtil;
import cn.thinkfree.core.utils.UniqueCodeGenerator;
import cn.thinkfree.database.mapper.AfInstanceMapper;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.vo.*;
import cn.thinkfree.service.approvalflow.*;
import cn.thinkfree.service.config.HttpLinks;
import cn.thinkfree.service.config.PdfConfig;
import cn.thinkfree.service.neworder.NewOrderService;
import cn.thinkfree.service.neworder.NewOrderUserService;
import cn.thinkfree.service.newscheduling.NewSchedulingService;
import cn.thinkfree.service.project.ProjectService;
import cn.thinkfree.service.utils.AfUtils;
import cn.thinkfree.service.utils.DateUtil;
import cn.thinkfree.service.utils.HttpUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 审批流实例服务层
 *
 * @author song
 * @version 1.0
 * @date 2018/10/26 10:22
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class AfInstanceServiceImpl implements AfInstanceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AfInstanceServiceImpl.class);

    @Autowired
    private AfInstanceMapper instanceMapper;
    @Autowired
    private AfApprovalLogService approvalLogService;
    @Autowired
    private NewOrderUserService orderUserService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private AfApprovalRoleService approvalRoleService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private AfConfigService configService;
    @Autowired
    private NewSchedulingService schedulingService;
    @Autowired
    private HttpLinks httpLinks;
    @Autowired
    private AfConfigSchemeService configSchemeService;
    @Autowired
    private AfSubRoleService subRoleService;
    @Autowired
    private NewOrderService orderService;

    @Override
    public AfInstanceDetailVO start(String projectNo, String userId, String configNo, Integer scheduleSort) {
        // TODO 测试用
//        if (!verifyStartApproval(projectNo, configNo, scheduleSort)) {
//            LOGGER.error("无法发起审批");
//            throw new CommonException(500, "无法发起审批");
//        }
        AfInstanceDetailVO instanceDetailVO = new AfInstanceDetailVO();
        Project project = projectService.findByProjectNo(projectNo);
        if (project == null) {
            LOGGER.error("未查询到项目编号为{}的项目", projectNo);
            throw new RuntimeException();
        }

        List<UserRoleSet> allRoles = roleService.findAll();
        String configSchemeNo = configSchemeService.findByProjectNoAndConfigNoAndUserId(projectNo, configNo, userId);
        if (configSchemeNo == null) {
            LOGGER.error("在项目projectNo：{}中，当前用户userId：{}，不具有发起审批流：{}的权限", projectNo, userId, configNo, projectNo);
            throw new RuntimeException();
        }
        List<UserRoleSet> approvalRoles = approvalRoleService.findByConfigSchemeNo(configSchemeNo, allRoles);
        if (approvalRoles == null || approvalRoles.size() < 1) {
            LOGGER.error("未查询到审批角色信息，configSchemeNo：{}", configSchemeNo);
            throw new RuntimeException();
        }

        List<OrderUser> orderUsers = orderUserService.findByProjectNo(projectNo);
        List<AfApprovalLogVO> approvalLogVOs = new ArrayList<>();
        for (UserRoleSet role : approvalRoles) {
            AfApprovalLogVO approvalLogVO = new AfApprovalLogVO();

            for (OrderUser orderUser : orderUsers) {
                if (orderUser.getRoleCode().equals(role.getRoleCode())) {
                    approvalLogVO.setUserId(orderUser.getUserId());
                    break;
                }
            }
            if (approvalLogVO.getUserId() == null) {
                LOGGER.error("未获取到用户编号，projectNo:{}，roleId：{}", projectNo, role.getRoleCode());
                throw new RuntimeException();
            }

            approvalLogVO.setRoleId(role.getRoleCode());
            approvalLogVO.setRoleName(role.getRoleName());
            approvalLogVO.setStatus(AfConstants.APPROVAL_OPTION_UNAPPROVAL);
            AfUserDTO userDTO = AfUtils.getUserInfo(httpLinks.getUserCenterGetUserMsg(), approvalLogVO.getUserId(), approvalLogVO.getRoleId());
            approvalLogVO.setUserName(userDTO.getUsername());
            approvalLogVO.setHeadPortrait(userDTO.getHeadPortrait());
            approvalLogVOs.add(approvalLogVO);
        }
        String customerId = project.getOwnerId();
        AfUserDTO customerInfo = AfUtils.getUserInfo(httpLinks.getUserCenterGetUserMsg(), customerId, Role.CC.id);
        AfConfig config = configService.findByNo(configNo);
        instanceDetailVO.setConfigNo(configNo);
        instanceDetailVO.setConfigName(config.getName());
        instanceDetailVO.setProjectNo(projectNo);
        instanceDetailVO.setCustomerId(customerId);
        instanceDetailVO.setCustomerName(customerInfo.getUsername());
        instanceDetailVO.setApprovalLogs(approvalLogVOs);
        return instanceDetailVO;
    }

    @Override
    public void submitStart(String projectNo, String userId, String configNo, Integer scheduleSort, String data, String remark) {
        // TODO 测试用
//        if (!verifyStartApproval(projectNo, configNo, scheduleSort)) {
//            LOGGER.error("无法发起审批");
//            throw new CommonException(500, "无法发起审批");
//        }
        List<UserRoleSet> allRoles = roleService.findAll();
        String configSchemeNo = configSchemeService.findByProjectNoAndConfigNoAndUserId(projectNo, configNo, userId);
        if (configSchemeNo == null) {
            LOGGER.error("在项目projectNo：{}中，当前用户userId：{}，不具有发起审批流：{}的权限", projectNo, userId, configNo, projectNo);
            throw new RuntimeException();
        }
        List<UserRoleSet> approvalRoles = approvalRoleService.findByConfigSchemeNo(configSchemeNo, allRoles);
        if (approvalRoles == null || approvalRoles.size() < 1) {
            LOGGER.error("未查询到审批角色信息，configSchemeNo：{}", configSchemeNo);
            throw new RuntimeException();
        }
        List<AfApprovalLog> approvalLogs = new ArrayList<>(approvalRoles.size());
        List<OrderUser> orderUsers = orderUserService.findByProjectNo(projectNo);
        if (orderUsers == null || orderUsers.isEmpty()) {
            LOGGER.error("未获取到项目用户信息，projectNo:{}", projectNo);
            throw new RuntimeException();
        }

        String instanceNo = UniqueCodeGenerator.AF_INSTANCE.getCode();
        for (int index = 0; index < approvalRoles.size(); index++) {
            UserRoleSet role = approvalRoles.get(index);
            AfApprovalLog approvalLog = null;
            for (OrderUser orderUser : orderUsers) {
                if (role.getRoleCode().equals(orderUser.getRoleCode())) {
                    approvalLog = new AfApprovalLog();
                    approvalLog.setRoleId(role.getRoleCode());
                    approvalLog.setUserId(orderUser.getUserId());
                    approvalLog.setInstanceNo(instanceNo);
                    approvalLog.setApprovalNo(UniqueCodeGenerator.AF_APPROVAL_LOG.getCode());
                    approvalLog.setIsApproval(0);
                    approvalLog.setSort(index);
                    approvalLog.setConfigNo(configNo);
                    approvalLog.setProjectNo(projectNo);
                    approvalLog.setScheduleSort(scheduleSort);
                    break;
                }
            }
            if (approvalLog == null) {
                LOGGER.error("未获取到用户编号，projectNo:{}，roleId：{}", projectNo, role.getRoleCode());
                throw new RuntimeException();
            }
            approvalLogs.add(approvalLog);
        }
        AfApprovalLog approvalLog = approvalLogs.get(0);
        approvalLog.setRemark(remark);
        approvalLog.setApprovalTime(new Date());
        approvalLog.setIsApproval(1);

        AfInstance instance = new AfInstance();
        instance.setConfigNo(configNo);
        instance.setCreateRoleId(approvalLog.getRoleId());
        instance.setCreateTime(new Date());
        instance.setData(data);
        instance.setInstanceNo(instanceNo);
        instance.setScheduleSort(scheduleSort);
        instance.setCreateUserId(userId);
        instance.setProjectNo(projectNo);
        instance.setConfigSchemeNo(configSchemeNo);
        instance.setRemark(remark);

        sendMessageToSub(configSchemeNo, projectNo, userId, orderUsers);

        if (approvalLogs.size() > 1) {
            instance.setCurrentApprovalLogNo(approvalLogs.get(1).getApprovalNo());
            instance.setStatus(AfConstants.APPROVAL_STATUS_START);
            sendMessageToNext(projectNo, userId, approvalLogs.get(1).getUserId());
        } else {
            instance.setStatus(AfConstants.APPROVAL_STATUS_SUCCESS);
            executeSuccessAction(instance);
        }

        insert(instance);
        approvalLogService.create(approvalLogs);
    }

    private boolean verifyStartApproval(String projectNo, String configNo, Integer scheduleSort) {
        boolean result = false;
        List<ProjectBigSchedulingDetailsVO> schedulingDetailsVOs = schedulingService.getScheduling(projectNo).getData();
        if (schedulingDetailsVOs != null && schedulingDetailsVOs.size() > 0) {
            int projectCompleteStatus = getProjectCompleteStatus(schedulingDetailsVOs, projectNo);
            if (projectCompleteStatus != AfConstants.APPROVAL_STATUS_SUCCESS && projectCompleteStatus != AfConstants.APPROVAL_STATUS_START) {
                if (AfConfigs.START_APPLICATION.configNo.equals(configNo)) {
                    int status = getInstanceStatus(configNo, projectNo);
                    if (status == AfConstants.APPROVAL_STATUS_BEFORE_START || status == AfConstants.APPROVAL_STATUS_FAIL){
                        result = true;
                    }
                } else if (AfConfigs.START_REPORT.configNo.equals(configNo)) {
                    int startApplicationStatus = getInstanceStatus(AfConfigs.START_APPLICATION.configNo, projectNo);
                    if (startApplicationStatus == AfConstants.APPROVAL_STATUS_SUCCESS) {
                        int status = getInstanceStatus(configNo, projectNo);
                        if (status == AfConstants.APPROVAL_STATUS_BEFORE_START || status == AfConstants.APPROVAL_STATUS_FAIL){
                            result = true;
                        }
                    }
                } else if (AfConfigs.CHECK_APPLICATION.configNo.equals(configNo)) {
                    int preScheduleSortCompleteStatus = getPreScheduleSortCompleteStatus(projectNo, schedulingDetailsVOs, scheduleSort);
                    if (preScheduleSortCompleteStatus == AfConstants.APPROVAL_STATUS_SUCCESS) {
                        int scheduleSortCompleteStatus = getScheduleSortCompleteStatus(projectNo, scheduleSort);
                        if (scheduleSortCompleteStatus == AfConstants.APPROVAL_STATUS_BEFORE_START || scheduleSortCompleteStatus == AfConstants.APPROVAL_STATUS_FAIL) {
                            result = true;
                        }
                    }
                } else if (AfConfigs.CHECK_REPORT.configNo.equals(configNo)) {
                    int preScheduleSortCompleteStatus = getPreScheduleSortCompleteStatus(projectNo, schedulingDetailsVOs, scheduleSort);
                    if (preScheduleSortCompleteStatus == AfConstants.APPROVAL_STATUS_SUCCESS) {
                        int scheduleSortCompleteStatus = getScheduleSortCompleteStatus(projectNo, scheduleSort);
                        if (scheduleSortCompleteStatus == AfConstants.APPROVAL_STATUS_BEFORE_START || scheduleSortCompleteStatus == AfConstants.APPROVAL_STATUS_FAIL) {
                            long checkApplicationCount = getCount(projectNo, AfConfigs.CHECK_APPLICATION.configNo, AfConstants.APPROVAL_STATUS_SUCCESS);
                            long checkReportSuccessCount = getCount(projectNo, AfConfigs.CHECK_REPORT.configNo, AfConstants.APPROVAL_STATUS_SUCCESS);
                            long checkReportStartCount = getCount(projectNo, AfConfigs.CHECK_REPORT.configNo, AfConstants.APPROVAL_STATUS_START);
                            if (checkApplicationCount > checkReportSuccessCount + checkReportStartCount) {
                                result = true;
                            }
                        }
                    }
                } else if (AfConfigs.PROBLEM_RECTIFICATION.configNo.equals(configNo)) {
                    result = true;
                } else if (AfConfigs.RECTIFICATION_COMPLETE.configNo.equals(configNo)) {
                    long checkApplicationCount = getCount(projectNo, AfConfigs.PROBLEM_RECTIFICATION.configNo, AfConstants.APPROVAL_STATUS_SUCCESS);
                    long checkReportSuccessCount = getCount(projectNo, AfConfigs.RECTIFICATION_COMPLETE.configNo, AfConstants.APPROVAL_STATUS_SUCCESS);
                    long checkReportStartCount = getCount(projectNo, AfConfigs.RECTIFICATION_COMPLETE.configNo, AfConstants.APPROVAL_STATUS_START);
                    if (checkApplicationCount > checkReportSuccessCount + checkReportStartCount) {
                        result = true;
                    }
                } else if (AfConfigs.CHANGE_ORDER.configNo.equals(configNo)) {
                    result = true;
                } else if (AfConfigs.CHANGE_COMPLETE.configNo.equals(configNo)) {
                    long checkApplicationCount = getCount(projectNo, AfConfigs.CHANGE_ORDER.configNo, AfConstants.APPROVAL_STATUS_SUCCESS);
                    long checkReportSuccessCount = getCount(projectNo, AfConfigs.CHANGE_COMPLETE.configNo, AfConstants.APPROVAL_STATUS_SUCCESS);
                    long checkReportStartCount = getCount(projectNo, AfConfigs.CHANGE_COMPLETE.configNo, AfConstants.APPROVAL_STATUS_START);
                    if (checkApplicationCount > checkReportSuccessCount + checkReportStartCount) {
                        result = true;
                    }
                } else if (AfConfigs.DELAY_ORDER.configNo.equals(configNo)) {
                    result = true;
                } else if (AfConfigs.COMPLETE_APPLICATION.configNo.equals(configNo)) {
                    int preScheduleSortCompleteStatus = getPreScheduleSortCompleteStatus(projectNo, schedulingDetailsVOs, scheduleSort);
                    if (preScheduleSortCompleteStatus == AfConstants.APPROVAL_STATUS_SUCCESS) {
                        int scheduleSortCompleteStatus = getScheduleSortCompleteStatus(projectNo, scheduleSort);
                        if (scheduleSortCompleteStatus == AfConstants.APPROVAL_STATUS_BEFORE_START || scheduleSortCompleteStatus == AfConstants.APPROVAL_STATUS_FAIL) {

                            if (getCount(projectNo, AfConstants.APPROVAL_STATUS_START) == 0
                                    && countEqual(projectNo, AfConfigs.CHECK_APPLICATION.configNo, AfConfigs.CHECK_REPORT.configNo, AfConstants.APPROVAL_STATUS_SUCCESS)
                                    && countEqual(projectNo, AfConfigs.PROBLEM_RECTIFICATION.configNo, AfConfigs.RECTIFICATION_COMPLETE.configNo, AfConstants.APPROVAL_STATUS_SUCCESS)
                                    && countEqual(projectNo, AfConfigs.CHANGE_ORDER.configNo, AfConfigs.CHANGE_COMPLETE.configNo, AfConstants.APPROVAL_STATUS_SUCCESS)) {
                                result = true;
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * 插入新数据
     * @param instance 审批流实例
     */
    private void insert(AfInstance instance) {
        instanceMapper.insertSelective(instance);
    }

    @Override
    public AfInstanceDetailVO detail(String instanceNo, String userId) {
        AfInstanceDetailVO instanceDetailVO = new AfInstanceDetailVO();
        instanceDetailVO.setIsShowButton(false);
        AfInstance instance = findByNo(instanceNo);
        if (instance == null) {
            LOGGER.error("未获取审批流信息，instanceNo:{}", instanceNo);
            throw new RuntimeException();
        }
        Project project = projectService.findByProjectNo(instance.getProjectNo());
        if (project == null) {
            LOGGER.error("未查询到项目编号为{}的项目", instance.getProjectNo());
            throw new RuntimeException();
        }
        String customerId = project.getOwnerId();
        AfUserDTO customer = AfUtils.getUserInfo(httpLinks.getUserCenterGetUserMsg(), customerId, Role.CC.id);
        List<UserRoleSet> roles = roleService.findAll();

        List<AfApprovalLogVO> approvalLogVOs = new ArrayList<>();
        List<AfApprovalLog> approvalLogs = approvalLogService.findByInstanceNo(instanceNo);
        if (approvalLogs == null || approvalLogs.size() < 1) {
            LOGGER.error("未查询审批日志，instanceNo:{}", instanceNo);
            throw new RuntimeException();
        }
        int wait = 0;
        Date approvalTime = null;
        for (AfApprovalLog approvalLog : approvalLogs) {
            AfApprovalLogVO approvalLogVO = new AfApprovalLogVO();
            approvalLogVO.setUserId(approvalLog.getUserId());
            approvalLogVO.setRoleId(approvalLog.getRoleId());
            for (UserRoleSet record : roles) {
                if (record.getRoleCode().equals(approvalLogVO.getRoleId())) {
                    approvalLogVO.setRoleName(record.getRoleName());
                    break;
                }
            }
            AfUserDTO userDTO = AfUtils.getUserInfo(httpLinks.getUserCenterGetUserMsg(), approvalLog.getUserId(), approvalLog.getRoleId());
            approvalLogVO.setUserName(userDTO.getUsername());
            approvalLogVO.setHeadPortrait(userDTO.getHeadPortrait());

            if (approvalLog.getIsApproval() == 1) {
                if (approvalLog.getSort() == 0) {
                    approvalLogVO.setStatus(AfConstants.APPROVAL_OPTION_START);
                } else if (approvalLog.getOption() == 1){
                    approvalLogVO.setStatus(AfConstants.APPROVAL_OPTION_AGREE);
                } else {
                    approvalLogVO.setStatus(AfConstants.APPROVAL_OPTION_REFUSAL);
                    instanceDetailVO.setRefusalReason(approvalLog.getRemark());
                }
                approvalLogVO.setApprovalTime(approvalLog.getApprovalTime());
                approvalTime = approvalLog.getApprovalTime();
                wait = 1;
            } else {
                approvalLogVO.setStatus(AfConstants.APPROVAL_OPTION_UNAPPROVAL);
                if (wait == 1) {
                    approvalLogVO.setWaitTip(getWaitTip(approvalTime));
                    wait++;
                }
            }
            if (approvalLog.getApprovalNo().equals(instance.getCurrentApprovalLogNo()) && approvalLog.getUserId().equals(userId)) {
                instanceDetailVO.setIsShowButton(true);
            }
            approvalLogVOs.add(approvalLogVO);
        }

        AfConfig config = configService.findByNo(instance.getConfigNo());
        if (config == null) {
            LOGGER.error("未查询到审批流配置信息，configNo:{}", instance.getConfigNo());
            throw new RuntimeException();
        }
        instanceDetailVO.setInstanceNo(instanceNo);
        instanceDetailVO.setAddress(project.getAddressDetail() + project.getAddressDetail());
        instanceDetailVO.setConfigNo(instance.getConfigNo());
        instanceDetailVO.setConfigName(config.getName());
        instanceDetailVO.setData(instance.getData());
        instanceDetailVO.setRemark(approvalLogs.get(0).getRemark());
        instanceDetailVO.setProjectNo(instance.getProjectNo());
        instanceDetailVO.setCustomerId(customerId);
        instanceDetailVO.setCustomerName(customer.getUsername());
        instanceDetailVO.setApprovalLogs(approvalLogVOs);
        instanceDetailVO.setStatus(instance.getStatus());
        return instanceDetailVO;
    }

    /**
     * 组装等待审批时间提示
     * @param approvalTime 上一个人的审批时间
     * @return 等待审批时间提示
     */
    private String getWaitTip(Date approvalTime) {
        Date currentTime = new Date();
        int days = DateUtil.differentDaysByMillisecond(approvalTime, currentTime);
        int hours = DateUtil.differentHoursByMillisecond(approvalTime, currentTime);
        return "已等待" + days + "天" + hours + "小时";
    }

    @Override
    public void approval(String instanceNo, String userId, Integer option, String remark) {
        if (option != AfConstants.OPTION_REFUSAL && option != AfConstants.OPTION_AGREE) {
            LOGGER.error("未传入正确的参数，option:{}", option);
            throw new CommonException(500, "未传入正确的参数");
        }
        if (option == AfConstants.OPTION_REFUSAL && StringUtils.isEmpty(remark)) {
            LOGGER.error("用户在拒绝的情况下必须写明原因");
            throw new RuntimeException();
        }
        AfInstance instance = findByNo(instanceNo);
        if (instance == null) {
            LOGGER.error("未获取审批流信息，instanceNo:{}", instanceNo);
            throw new RuntimeException();
        }

        if (instance.getCurrentApprovalLogNo() == null) {
            LOGGER.error("当前审批流记录编号为空，无法审批");
            throw new RuntimeException();
        }
        AfApprovalLog approvalLog = approvalLogService.findByNo(instance.getCurrentApprovalLogNo());
        if (approvalLog == null) {
            LOGGER.error("未查询到审批记录，approvalLogNo:{}", instance.getCurrentApprovalLogNo());
            throw new RuntimeException();
        }
        String recordUserId = orderUserService.findUserIdByProjectNoAndRoleId(instance.getProjectNo(), approvalLog.getRoleId());
        if (!userId.equals(recordUserId)) {
            LOGGER.error("当前用户不具有审批权限!");
            throw new RuntimeException();
        }

        approvalLog.setRemark(remark);
        approvalLog.setIsApproval(1);
        approvalLog.setApprovalTime(new Date());

        if (option == AfConstants.OPTION_AGREE) {
            // 同意
            approvalLog.setOption(option);
            AfApprovalLog nextApprovalLog = approvalLogService.findByInstanceNoAndSort(instanceNo, approvalLog.getSort() + 1);
            if (nextApprovalLog == null) {
                // 结束审批流
                instance.setStatus(AfConstants.APPROVAL_STATUS_SUCCESS);
                instance.setCurrentApprovalLogNo(null);
                executeSuccessAction(instance);
            } else {
                instance.setCurrentApprovalLogNo(nextApprovalLog.getApprovalNo());
                sendMessageToNext(instance.getProjectNo(), userId, nextApprovalLog.getUserId());
            }
        } else {
            // 不同意
            approvalLog.setOption(option);
            instance.setStatus(AfConstants.APPROVAL_STATUS_FAIL);
            instance.setCurrentApprovalLogNo(null);
        }

        updateByPrimaryKey(instance);
        approvalLogService.updateByPrimaryKey(approvalLog);
    }

    /**
     * 给下一个审批人发送审批消息
     * @param projectNo 项目编号
     * @param sendUserId 发送消息用户
     * @param subUserId 订阅消息用户
     */
    private void sendMessageToNext(String projectNo, String sendUserId, String subUserId) {
        sendMessage(projectNo, sendUserId, subUserId, "有新的表单需要您审批！");
    }

    /**
     * 给订阅者发布消息
     * @param projectNo 项目编号
     * @param configSchemeNo 配置方案编号
     * @param sendUserId 发送消息用户
     * @param orderUsers 项目角色关系
     */
    private void sendMessageToSub(String projectNo, String configSchemeNo, String sendUserId, List<OrderUser> orderUsers) {
        List<AfSubRole> subRoles = subRoleService.findByConfigSchemeNo(configSchemeNo);
        List<String> subUserIds = new ArrayList<>();
        if (subRoles != null && !subRoles.isEmpty()) {
            for (AfSubRole subRole : subRoles) {
                String userId = null;
                for (OrderUser orderUser : orderUsers) {
                    if (subRole.getRoleId().equals(orderUser.getRoleCode())) {
                        userId = orderUser.getUserId();
                        break;
                    }
                }
                if (userId == null) {
                    LOGGER.error("在项目，projectNo:{},中未查询到角色，roleId：{}", orderUsers.get(0), subRole.getRoleId());
                    throw new RuntimeException();
                }
                subUserIds.add(userId);
            }
            sendMessage(projectNo, sendUserId, subUserIds, "您有新的审批!");
        }
    }

    private void sendMessage(String projectNo, String sendUserId, String subUserId, String content) {
        List<String> subUserIds = new ArrayList<>(1);
        subUserIds.add(subUserId);
        sendMessage(projectNo, sendUserId, subUserIds, content);
    }

    private void sendMessage(String projectNo, String sendUserId, List<String> subUserIds, String content) {
        Map<String, String> requestMsg = new HashMap<>();
        requestMsg.put("projectNo", projectNo);
//        requestMsg.put("userNo", JSONUtil.bean2JsonStr(subUserIds));
//        requestMsg.put("senderId", sendUserId);
        requestMsg.put("userNo", "[123567]");
        requestMsg.put("senderId", "123456");
        requestMsg.put("content", content);
        requestMsg.put("dynamicId", "0");
        requestMsg.put("type", "3");
        LOGGER.info("发送审批消息：requestMsg：{}", requestMsg);
        HttpUtils.HttpRespMsg respMsg = HttpUtils.post(httpLinks.getMessageSave(), requestMsg);
        LOGGER.info("respMsg:{}", JSONUtil.bean2JsonStr(respMsg));
        // TODO 错误判断
    }

    /**
     * 发送审批成功消息
     * @param instance 审批流实例
     */
    private void executeSuccessAction(AfInstance instance) {
        if (AfConfigs.START_REPORT.configNo.equals(instance.getConfigNo())) {
            schedulingService.projectStart(instance.getProjectNo(), instance.getScheduleSort());
        } else if (AfConfigs.COMPLETE_APPLICATION.configNo.equals(instance.getProjectNo())) {
            schedulingService.completeBigScheduling(instance.getProjectNo(), instance.getScheduleSort());
        } else if (AfConfigs.CHANGE_COMPLETE.configNo.equals(instance.getConfigNo())) {
            // TODO 发送变更金额
//            sendChangeMoney(instance.getProjectNo(), instance.getData(), instance.getRemark());
        }

        createPdf(instance.getProjectNo(), instance.getConfigNo(), instance.getScheduleSort());
    }

    private void sendChangeMoney(String projectNo, String data, String remark) {
        ConstructionOrder constructionOrder = orderService.getConstructionOrder(projectNo);
        String orderNo = constructionOrder.getOrderNo();

        Map dataMap = JSONUtil.json2Bean(data, Map.class);
        String money = (String) dataMap.get("money");

        AfUtils.sendChangeMoney(httpLinks.getCreateFee(), orderNo, money, "+" + remark);
    }

    private void createPdf(String projectNo, String configNo, Integer scheduleSort) {
//        AfUtils.createPdf()
    }

    /**
     * 根据id更新数据
     * @param instance
     */
    private void updateByPrimaryKey(AfInstance instance) {
        instanceMapper.updateByPrimaryKey(instance);
    }

    /**
     * 根据审批流编号查询审批流实例
     * @param instanceNo 审批流编号
     * @return 审批流实例
     */
    private AfInstance findByNo(String instanceNo) {
        AfInstanceExample example = new AfInstanceExample();
        example.createCriteria().andInstanceNoEqualTo(instanceNo);
        List<AfInstance> instances = instanceMapper.selectByExample(example);
        return instances != null && instances.size() > 0 ? instances.get(0) : null;
    }

    @Override
    public AfInstanceListVO list(String userId, String projectNo, String approvalType, Integer scheduleSort) {
        AfInstanceListVO instanceListVO = new AfInstanceListVO();
        List<AfInstanceVO> instanceVOs = new ArrayList<>();
        List<AfStartMenuVO> startMenus = new ArrayList<>();

        List<ProjectBigSchedulingDetailsVO> schedulingDetailsVOs = schedulingService.getScheduling(projectNo).getData();
        // TODO
//        if (schedulingDetailsVOs != null && schedulingDetailsVOs.size() > 0) {
//            int projectCompleteStatus = getProjectCompleteStatus(schedulingDetailsVOs, projectNo);
//            if (projectCompleteStatus != AfConstants.APPROVAL_STATUS_SUCCESS && projectCompleteStatus != AfConstants.APPROVAL_STATUS_START) {
                if (AfConstants.APPROVAL_TYPE_SCHEDULE_APPROVAL.equals(approvalType)) {
                    // 进度验收
                    if (scheduleSort == null) {
                        // 开工准备:开工申请
                        getInstances(instanceVOs, AfConfigs.START_APPLICATION.configNo, userId, projectNo);
                        // 开工准备:开工报告
                        getInstances(instanceVOs, AfConfigs.START_REPORT.configNo, userId, projectNo);
                        // 获取开工准备阶段发起菜单
                        getStartStartMenus(startMenus, userId, projectNo);
                    } else {
                        // 验收申请
                        getInstances(instanceVOs, AfConfigs.CHECK_APPLICATION.configNo, userId, projectNo, scheduleSort);
                        // 验收报告
                        getInstances(instanceVOs, AfConfigs.CHECK_REPORT.configNo, userId, projectNo, scheduleSort);
                        // 完工审批
                        getInstances(instanceVOs, AfConfigs.COMPLETE_APPLICATION.configNo, userId, projectNo, scheduleSort);
//                        int preScheduleSortCompleteStatus = getPreScheduleSortCompleteStatus(projectNo, schedulingDetailsVOs, scheduleSort);
//                        if (preScheduleSortCompleteStatus == AfConstants.APPROVAL_STATUS_SUCCESS) {
//                            int scheduleSortCompleteStatus = getScheduleSortCompleteStatus(projectNo, scheduleSort);
//                            if (scheduleSortCompleteStatus != AfConstants.APPROVAL_STATUS_SUCCESS && scheduleSortCompleteStatus != AfConstants.APPROVAL_STATUS_START) {
//                                if (isNeedCheck(schedulingDetailsVOs, scheduleSort)) {
                                    // 获取验收、完工申请发起菜单
                                    getCheckAndCompleteStartMenus(startMenus, userId, projectNo, schedulingDetailsVOs, scheduleSort);
//                                }
//                            }
//                        }
                    }

                } else if (AfConstants.APPROVAL_TYPE_PROBLEM_RECTIFICATION.equals(approvalType)) {
                    // 问题整改：问题整改
                    getInstances(instanceVOs, AfConfigs.PROBLEM_RECTIFICATION.configNo, userId, projectNo);
                    // 问题整改：整改完成
                    getInstances(instanceVOs, AfConfigs.RECTIFICATION_COMPLETE.configNo, userId, projectNo);

                    getStartMenus(startMenus, userId, projectNo, AfConfigs.PROBLEM_RECTIFICATION.configNo, AfConfigs.RECTIFICATION_COMPLETE.configNo);
                } else if (AfConstants.APPROVAL_TYPE_CONSTRUCTION_CHANGE.equals(approvalType)) {
                    // 施工变更：变更单
                    getInstances(instanceVOs, AfConfigs.CHANGE_ORDER.configNo, userId, projectNo);
                    // 施工变更：变更完成
                    getInstances(instanceVOs, AfConfigs.CHANGE_COMPLETE.configNo, userId, projectNo);

                    getStartMenus(startMenus, userId, projectNo, AfConfigs.CHANGE_ORDER.configNo, AfConfigs.CHANGE_COMPLETE.configNo);
                } else if (AfConstants.APPROVAL_TYPE_DELAY_VERIFY.equals(approvalType)) {
                    // 延期确认
                    getInstances(instanceVOs, AfConfigs.DELAY_ORDER.configNo, userId, projectNo);
                    getDelayStartMenus(startMenus, AfConfigs.DELAY_ORDER.configNo, userId, projectNo);
//                }
//            }
        }
        instanceVOs.sort(Comparator.comparing(AfInstanceVO::getCreateTime).reversed());
        instanceListVO.setInstances(instanceVOs);
        instanceListVO.setStartMenus(startMenus);
        return instanceListVO;
    }

    /**
     * 获取项目的完成状态
     * @param schedulingDetailsVOs 项目排期信息
     * @param projectNo 项目编号
     * @return 完成状态
     */
    private int getProjectCompleteStatus(List<ProjectBigSchedulingDetailsVO> schedulingDetailsVOs, String projectNo) {
        Integer lastScheduleSort = schedulingDetailsVOs.get(schedulingDetailsVOs.size() - 1).getBigSort();
        return getInstanceStatus(AfConfigs.COMPLETE_APPLICATION.configNo, projectNo, lastScheduleSort);
    }

    /**
     * 获取上一个阶段的排期编号
     * @param schedulingDetailsVOs 排期信息
     * @param scheduleSort 当前排期编号
     * @return 上一个阶段的排期编号
     */
    private Integer getPreScheduleSort(List<ProjectBigSchedulingDetailsVO> schedulingDetailsVOs, Integer scheduleSort) {
        Integer preScheduleSort = null;
        for (ProjectBigSchedulingDetailsVO schedulingDetailsVO : schedulingDetailsVOs) {
            if (scheduleSort.equals(schedulingDetailsVO.getBigSort())) {
                return preScheduleSort;
            }
            preScheduleSort = schedulingDetailsVO.getBigSort();
        }
        LOGGER.error("当前节点不存在目标项目中，scheduleSort：{}", scheduleSort);
        throw new RuntimeException();
    }

    /**
     * 获取上一个阶段的完成情况
     * @param projectNo 项目编号
     * @param schedulingDetailsVOs 项目排期信息
     * @param scheduleSort 当前排期编号
     * @return 上一个阶段的完成情况
     */
    private int getPreScheduleSortCompleteStatus(String projectNo, List<ProjectBigSchedulingDetailsVO> schedulingDetailsVOs, Integer scheduleSort) {
        Integer preScheduleSort = getPreScheduleSort(schedulingDetailsVOs, scheduleSort);
        return getScheduleSortCompleteStatus(projectNo, preScheduleSort);
    }

    private int getScheduleSortCompleteStatus(String projectNo, Integer scheduleSort) {
        int status;
        if (scheduleSort == null) {
            status = getInstanceStatus(AfConfigs.START_REPORT.configNo, projectNo);
        } else {
            status = getInstanceStatus(AfConfigs.COMPLETE_APPLICATION.configNo, projectNo, scheduleSort);
        }
        return status;
    }

    /**
     * 当前节点是否需要验收
     * @param schedulingDetailsVOs 排期信息
     * @param scheduleSort 当前排期编号
     * @return 当前节点是否需要验收
     */
    private boolean isNeedCheck(List<ProjectBigSchedulingDetailsVO> schedulingDetailsVOs, Integer scheduleSort) {
        for (ProjectBigSchedulingDetailsVO schedulingDetailsVO : schedulingDetailsVOs) {
            if (scheduleSort.equals(schedulingDetailsVO.getBigSort())) {
                return schedulingDetailsVO.getIsNeedCheck() == 1;
            }
        }
        LOGGER.error("当前节点不存在目标项目中，scheduleSort：{}", scheduleSort);
        throw new RuntimeException();
    }

    /**
     * 获取发起菜单
     * @param startMenus 发起菜单
     * @param userId 用户编号
     * @param projectNo 项目编号
     * @param startConfigNo 开始审批编号
     * @param completeConfigNo 完成审批编号
     */
    private void getStartMenus(List<AfStartMenuVO> startMenus, String userId, String projectNo, String startConfigNo, String completeConfigNo) {
        // 发起菜单
        addStartMenu(startMenus, projectNo, startConfigNo, userId);
        List<AfInstance> startInstances = findByConfigNoAndProjectNo(startConfigNo, projectNo);
        List<AfInstance> completeInstances = findByConfigNoAndProjectNo(completeConfigNo, projectNo);
        int startCount = getSuccessCount(startInstances);
        int completeCount = getStartAndSuccessCount(completeInstances);
        if (startCount > completeCount) {
            // 发起完成菜单
            addStartMenu(startMenus, projectNo, completeConfigNo, userId);
        }
    }

    /**
     * 添加发起按钮
     * @param startMenus 发起按钮集合
     * @param projectNo 项目编号
     * @param configNo 审批流配置编号
     * @param userId 用户id
     */
    private void addStartMenu(List<AfStartMenuVO> startMenus, String projectNo, String configNo, String userId) {
        String configSchemeNo = configSchemeService.findByProjectNoAndConfigNoAndUserId(projectNo, configNo, userId);
        if (configSchemeNo != null) {
            AfStartMenuVO startMenuVO = createStartMenu(configNo);
            startMenus.add(startMenuVO);
        }
    }

    /**
     * 添加发起验收与完工菜单
     * @param startMenus 发起菜单
     * @param userId 用户编号
     * @param projectNo 项目编号
     * @param schedulingDetailsVOs 排期信息
     * @param scheduleSort 当前阶段编号
     */
    private void getCheckAndCompleteStartMenus(List<AfStartMenuVO> startMenus, String userId, String projectNo, List<ProjectBigSchedulingDetailsVO> schedulingDetailsVOs, Integer scheduleSort) {
        List<AfInstance> checkApplicationInstances = findByConfigNoAndProjectNoAndScheduleSort(AfConfigs.CHECK_APPLICATION.configNo, projectNo, scheduleSort);
        List<AfInstance> checkReportInstances = findByConfigNoAndProjectNoAndScheduleSort(AfConfigs.CHECK_REPORT.configNo, projectNo, scheduleSort);

        int checkApplicationStatus = getInstanceStatus(checkApplicationInstances);
        int checkReportStatus = getInstanceStatus(checkReportInstances);

        int checkApplicationCount = getSuccessCount(checkApplicationInstances);
        int checkReportCount = getStartAndSuccessCount(checkReportInstances);
        // TODO 测试用
//        if (isNeedCheck(schedulingDetailsVOs, scheduleSort)) {
            // 发起验收申请菜单
            addStartMenu(startMenus, projectNo, AfConfigs.CHECK_APPLICATION.configNo, userId);

            if (checkApplicationCount > checkReportCount) {
                // 如果验收申请数量大于验收报告数量，发起验收报告菜单
                addStartMenu(startMenus, projectNo, AfConfigs.START_APPLICATION.configNo, userId);

            }
//        }
        if (checkApplicationStatus != AfConstants.APPROVAL_STATUS_START && checkReportStatus != AfConstants.APPROVAL_STATUS_START) {
            if (checkApplicationCount == checkReportCount) {
                if (schedulingDetailsVOs.get(schedulingDetailsVOs.size() - 1).getBigSort().equals(scheduleSort)) {
                    // 当前阶段为最后一个阶段
                    if (getCount(projectNo, AfConstants.APPROVAL_STATUS_START) == 0
                            && countEqual(projectNo, AfConfigs.PROBLEM_RECTIFICATION.configNo, AfConfigs.RECTIFICATION_COMPLETE.configNo, AfConstants.APPROVAL_STATUS_SUCCESS)
                            && countEqual(projectNo, AfConfigs.CHANGE_ORDER.configNo, AfConfigs.CHANGE_COMPLETE.configNo, AfConstants.APPROVAL_STATUS_SUCCESS)) {
                        addStartMenu(startMenus, projectNo, AfConfigs.COMPLETE_APPLICATION.configNo, userId);
                    }
                } else {
                    // 当前节点不存在未完成的验收申请与验收报告，且验收申请与验收报告数量相等，发起完成申请菜单
                    addStartMenu(startMenus, projectNo, AfConfigs.COMPLETE_APPLICATION.configNo, userId);
                }
            }
        }
    }

    private long getCount(String projectNo, int status) {
        AfInstanceExample example = new AfInstanceExample();
        example.createCriteria().andProjectNoEqualTo(projectNo).andStatusEqualTo(status);
        return instanceMapper.countByExample(example);
    }

    private boolean countEqual(String projectNo, String startConfigNo, String completeConfigNo, int status) {
        return getCount(projectNo, startConfigNo, status) == getCount(projectNo, completeConfigNo, status);
    }

    private long getCount(String projectNo, String configNo, int status) {
        AfInstanceExample example = new AfInstanceExample();
        example.createCriteria().andProjectNoEqualTo(projectNo).andConfigNoEqualTo(configNo).andStatusEqualTo(status);
        return instanceMapper.countByExample(example);
    }

    /**
     * 统计审批流实例的成功数量
     * @param instances 审批流实例
     * @return 成功数量
     */
    private int getSuccessCount(List<AfInstance> instances) {
        int count = 0;
        for (AfInstance instance : instances) {
            if (instance.getStatus() == AfConstants.APPROVAL_STATUS_SUCCESS) {
                count++;
            }
        }
        return count;
    }

    /**
     * 统计审批流实例的进行中与成功数量
     * @param instances 审批流实例
     * @return 进行中与成功数量
     */
    private int getStartAndSuccessCount(List<AfInstance> instances) {
        int count = 0;
        for (AfInstance instance : instances) {
            if (instance.getStatus() == AfConstants.APPROVAL_STATUS_START || instance.getStatus() == AfConstants.APPROVAL_STATUS_SUCCESS) {
                count++;
            }
        }
        return count;
    }

    /**
     * 获取延期发起菜单
     * @param startMenus 发起菜单
     * @param configNo 审批流配置编号
     * @param userId 用户编号
     * @param projectNo 项目编号
     */
    private void getDelayStartMenus(List<AfStartMenuVO> startMenus, String configNo, String userId, String projectNo){
        addStartMenu(startMenus, projectNo, configNo, userId);
    }

    /**
     * 获取开工准备发起菜单
     * @param startMenus 发起菜单
     * @param userId 用户id
     * @param projectNo 项目编号
     */
    private void getStartStartMenus(List<AfStartMenuVO> startMenus, String userId, String projectNo) {
//        int startApplicationStatus = getInstanceStatus(AfConfigs.START_APPLICATION.configNo, projectNo);
//        if (startApplicationStatus == 0 || startApplicationStatus == AfConstants.APPROVAL_STATUS_FAIL) {
            addStartMenu(startMenus, projectNo, AfConfigs.START_APPLICATION.configNo, userId);
//        } else if (startApplicationStatus == AfConstants.APPROVAL_STATUS_SUCCESS ) {
//            int startReportStatus = getInstanceStatus(AfConfigs.START_REPORT.configNo, projectNo);
//            if (startReportStatus == 0 || startReportStatus == AfConstants.APPROVAL_STATUS_FAIL ) {
                addStartMenu(startMenus, projectNo, AfConfigs.START_REPORT.configNo, userId);
//            }
//        }
        // TODO 测试用
    }

    /**
     * 创建发起菜单
     * @param configNo 审批流配置编号
     * @return 发起菜单
     */
    private AfStartMenuVO createStartMenu(String configNo) {
        AfStartMenuVO startMenuVO = new AfStartMenuVO();
        AfConfig config = configService.findByNo(configNo);
        if (config == null) {
            LOGGER.error("未查询到审批流配，configNo：{}", configNo);
            throw new RuntimeException();
        }
        startMenuVO.setConfigNo(config.getConfigNo());
        startMenuVO.setConfigName(config.getName());
        return startMenuVO;
    }

    /**
     * 获取审批流实例状态
     * @param configNo 审批流配置编号
     * @param projectNo 项目编号
     * @return 审批流实例状态
     */
    private int getInstanceStatus(String configNo, String projectNo) {
        List<AfInstance> instances = findByConfigNoAndProjectNo(configNo, projectNo);
        return getInstanceStatus(instances);
    }

    /**
     * 获取审批流实例状态
     * @param configNo 审批流配置编号
     * @param projectNo 项目编号
     * @param scheduleSort 排期编号
     * @return 审批流实例状态
     */
    private int getInstanceStatus(String configNo, String projectNo, Integer scheduleSort) {
        List<AfInstance> instances = findByConfigNoAndProjectNoAndScheduleSort(configNo, projectNo, scheduleSort);
        return getInstanceStatus(instances);
    }

    /**
     * 获取审批流实例状态
     * @param instances 审批流实例
     * @return 审批流实例状态
     */
    private int getInstanceStatus(List<AfInstance> instances) {
        int status = 0;
        if (instances != null) {
            for (AfInstance instance : instances) {
                if (AfConstants.APPROVAL_STATUS_START == instance.getStatus()) {
                    status = instance.getStatus();
                    break;
                } else if (AfConstants.APPROVAL_STATUS_SUCCESS == instance.getStatus()) {
                    status = instance.getStatus();
                } else if (status != AfConstants.APPROVAL_STATUS_SUCCESS) {
                    status = instance.getStatus();
                }
            }
        }
        return status;
    }

    /**
     * 根据审批流配置编号与项目编号查询审批流实例
     * @param configNo 审批流配置编号
     * @param projectNo 项目编号
     * @return 审批流实例
     */
    private List<AfInstance> findByConfigNoAndProjectNo(String configNo, String projectNo) {
        AfInstanceExample example = new AfInstanceExample();
        example.createCriteria().andConfigNoEqualTo(configNo).andProjectNoEqualTo(projectNo);
        return instanceMapper.selectByExample(example);
    }

    /**
     * 根据审批流配置编号、项目编号、排期编号查询审批实例
     * @param configNo 审批流配置编号
     * @param projectNo 项目编号
     * @param scheduleSort 排期编号
     * @return 审批实例
     */
    private List<AfInstance> findByConfigNoAndProjectNoAndScheduleSort(String configNo, String projectNo, Integer scheduleSort) {
        AfInstanceExample example = new AfInstanceExample();
        example.createCriteria().andConfigNoEqualTo(configNo).andProjectNoEqualTo(projectNo).andScheduleSortEqualTo(scheduleSort);
        return instanceMapper.selectByExample(example);
    }

    /**
     * 获取审批流实例
     * @param instanceVOs 审批流实例
     * @param configNo 审批流配置编号
     * @param userId 用户编号
     * @param projectNo 项目编号
     */
    private void getInstances( List<AfInstanceVO> instanceVOs, String configNo, String userId, String projectNo) {
        List<AfApprovalLog> approvalLogs = approvalLogService.findByConfigNoAndProjectNoAndUserId(configNo, projectNo, userId);
        getInstances(instanceVOs, approvalLogs, configNo, userId);
    }

    /**
     * 获取审批流实例
     * @param instanceVOs 审批流实例
     * @param configNo 审批流配置编号
     * @param userId 用户编号
     * @param projectNo 项目编号
     * @param scheduleSort 排期编号
     */
    private void getInstances( List<AfInstanceVO> instanceVOs, String configNo, String userId, String projectNo, Integer scheduleSort) {
        List<AfApprovalLog> approvalLogs = approvalLogService.findByConfigNoAndProjectNoAndScheduleSortAndUserId(configNo, projectNo, scheduleSort, userId);
        getInstances(instanceVOs, approvalLogs, configNo, userId);
    }

    /**
     * 获取审批流实例
     * @param instanceVOs 审批流实例
     * @param approvalLogs 审批记录
     * @param configNo 审批流配置编号
     * @param userId 用户编号
     */
    private void getInstances( List<AfInstanceVO> instanceVOs, List<AfApprovalLog> approvalLogs, String configNo, String userId) {
        AfConfig config = configService.findByNo(configNo);
        if (config == null) {
            LOGGER.error("未查询到审批流配置信息，configNo:{}", configNo);
            throw new RuntimeException();
        }
        if (approvalLogs != null) {
            for (AfApprovalLog approvalLog : approvalLogs) {
                AfInstanceVO instanceVO = new AfInstanceVO();
                instanceVO.setIsShowButton(false);
                AfInstance instance = findByNo(approvalLog.getInstanceNo());
                if (instance == null) {
                    LOGGER.error("未查询到审批流信息，instanceNo:{}", approvalLog.getInstanceNo());
                    throw new RuntimeException();
                }
                UserRoleSet role = roleService.findById(approvalLog.getRoleId());
                if (role == null) {
                    LOGGER.error("未查询到角色信息，roleId:{}", approvalLog.getRoleId());
                    throw new RuntimeException();
                }
                if (instance.getStatus() == 1) {
                    AfApprovalLog currentApprovalLog = approvalLogService.findByNo(instance.getCurrentApprovalLogNo());
                    if (currentApprovalLog.getUserId().equals(userId)) {
                        // 待审批用户与当前用户相同
                        instanceVO.setIsShowButton(true);
                    }
                }

                instanceVO.setStatus(instance.getStatus());

                instanceVO.setInstanceNo(instance.getInstanceNo());
                instanceVO.setConfigName(config.getName());
                instanceVO.setCreateTime(instance.getCreateTime());
                instanceVO.setCreateUserId(instance.getCreateUserId());
                AfUserDTO userDTO = AfUtils.getUserInfo(httpLinks.getUserCenterGetUserMsg(), instance.getCreateUserId(), instance.getCreateRoleId());
                instanceVO.setCreateUsername(userDTO.getUsername());
                instanceVO.setScheduleSort(instance.getScheduleSort());
                instanceVO.setRemark(approvalLog.getRemark());
                instanceVO.setRoleId(role.getRoleCode());
                instanceVO.setRoleName(role.getRoleName());

                instanceVOs.add(instanceVO);
            }
        }
    }

    @Override
    public int getProjectCheckResult(String projectNo) {
        List<Integer> statuses = new ArrayList<>();
        statuses.add(AfConstants.APPROVAL_STATUS_SUCCESS);
        statuses.add(AfConstants.APPROVAL_STATUS_FAIL);
        Integer result = instanceMapper.getProjectCheckResult(projectNo, AfConfigs.CHECK_REPORT.configNo, statuses);
        return result != null ? result : 0;
    }

    @Override
    public Map<String, Integer> getProjectCheckResult(List<String> projectNos) {
        List<Integer> statuses = new ArrayList<>();
        statuses.add(AfConstants.APPROVAL_STATUS_SUCCESS);
        statuses.add(AfConstants.APPROVAL_STATUS_FAIL);
        List<AfInstance> instances =  instanceMapper.getProjectCheckResults(projectNos, AfConfigs.CHECK_REPORT.configNo, statuses);
        Map<String, Integer> checkResult = new HashMap<>();
        if (instances != null) {
            for (AfInstance instance : instances) {
                checkResult.put(instance.getProjectNo(), instance.getStatus());
            }
        }
        return checkResult;
    }
}
