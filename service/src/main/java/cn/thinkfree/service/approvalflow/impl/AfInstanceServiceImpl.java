package cn.thinkfree.service.approvalflow.impl;

import cn.thinkfree.core.base.MyLogger;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.AfConfigs;
import cn.thinkfree.core.constants.AfConstants;
import cn.thinkfree.core.constants.Role;
import cn.thinkfree.core.utils.UniqueCodeGenerator;
import cn.thinkfree.database.mapper.AfInstanceMapper;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.vo.*;
import cn.thinkfree.service.approvalflow.*;
import cn.thinkfree.service.constants.HttpLinks;
import cn.thinkfree.service.neworder.NewOrderUserService;
import cn.thinkfree.service.newscheduling.NewSchedulingService;
import cn.thinkfree.service.project.ProjectService;
import cn.thinkfree.service.utils.AfUtils;
import cn.thinkfree.service.utils.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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

    private static final MyLogger LOGGER = new MyLogger(AfInstanceService.class);

    @Resource
    private AfInstanceMapper instanceMapper;
    @Resource
    private AfApprovalLogService approvalLogService;
    @Resource
    private NewOrderUserService orderUserService;
    @Resource
    private ProjectService projectService;
    @Resource
    private AfApprovalRoleService approvalRoleService;
    @Resource
    private RoleService roleService;
    @Resource
    private AfApprovalOrderService approvalOrderService;
    @Resource
    private AfConfigService configService;
    @Resource
    private NewSchedulingService schedulingService;
    @Resource
    private HttpLinks httpLinks;

    @Override
    public AfInstanceDetailVO start(String projectNo, String userId, String configNo, Integer scheduleSort) {
        AfInstanceDetailVO instanceDetailVO = new AfInstanceDetailVO();
        Project project = projectService.findByProjectNo(projectNo);
        if (project == null) {
            LOGGER.error("未查询到项目编号为{}的项目！", projectNo);
            throw new RuntimeException();
        }

        String customerId = project.getOwnerId();

        AfUserDTO customerInfo = AfUtils.getUserInfo(httpLinks.getUserCenterGetUserMsgUrl(), customerId, Role.CC.id);

        List<UserRoleSet> roles = roleService.findAll();
        AfApprovalOrder approvalOrder = approvalOrderService.findByProjectNoAndConfigNoAndUserId(projectNo, configNo, userId);
        if (approvalOrder == null) {
            LOGGER.error("在项目projectNo：{}中，当前用户userId：{}，不具有发起审批流：{}的权限！", projectNo, userId, configNo, projectNo);
            throw new RuntimeException();
        }
        List<UserRoleSet> approvalRoles = approvalRoleService.findByApprovalOrderNo(approvalOrder.getApprovalOrderNo(), roles);
        if (approvalRoles == null) {
            LOGGER.error("获取审批顺序出错approvalOrderNo:{}！", approvalOrder.getApprovalOrderNo());
            throw new RuntimeException();
        }

        List<OrderUser> orderUsers = orderUserService.findByProjectNo(projectNo);
        List<AfApprovalLogVO> approvalLogVOs = new ArrayList<>();
        for (UserRoleSet role : approvalRoles) {
            AfApprovalLogVO approvalLogVO = new AfApprovalLogVO();

            for (OrderUser orderUser : orderUsers) {
                if (orderUser.getRoleId().equals(role.getRoleCode())) {
                    approvalLogVO.setUserId(orderUser.getUserId());
                    break;
                }
            }
            if (approvalLogVO.getUserId() == null) {
                LOGGER.error("未获取到用户编号，projectNo:{}，roleId：{}！", projectNo, role.getRoleCode());
                throw new RuntimeException();
            }

            approvalLogVO.setRoleId(role.getRoleCode());
            approvalLogVO.setRoleName(role.getRoleName());
            approvalLogVO.setStatus(AfConstants.APPROVAL_OPTION_UNAPPROVAL);
            AfUserDTO userDTO = AfUtils.getUserInfo(httpLinks.getUserCenterGetUserMsgUrl(), approvalLogVO.getUserId(), approvalLogVO.getRoleId());
            approvalLogVO.setUserName(userDTO.getUsername());
            approvalLogVO.setHeadPortrait(userDTO.getHeadPortrait());
            approvalLogVOs.add(approvalLogVO);
        }
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

        List<UserRoleSet> roles = roleService.findAll();
        AfApprovalOrder approvalOrder = approvalOrderService.findByProjectNoAndConfigNoAndUserId(projectNo, configNo, userId);
        if (approvalOrder == null) {
            LOGGER.error("在项目projectNo：{}中，当前用户userId：{}，不具有发起审批流：{}的权限！", projectNo, userId, configNo, projectNo);
            throw new RuntimeException();
        }
        List<UserRoleSet> approvalRoles = approvalRoleService.findByApprovalOrderNo(approvalOrder.getApprovalOrderNo(), roles);
        if (approvalRoles == null || approvalRoles.size() < 1) {
            LOGGER.error("获取审批顺序出错approvalOrderNo:{}！", approvalOrder.getApprovalOrderNo());
            throw new RuntimeException();
        }
        List<AfApprovalLog> approvalLogs = new ArrayList<>(approvalRoles.size());
        List<OrderUser> orderUsers = orderUserService.findByProjectNo(projectNo);

        String instanceNo = UniqueCodeGenerator.AF_INSTANCE.getCode();
        for (int index = 0; index < approvalRoles.size(); index++) {
            UserRoleSet role = approvalRoles.get(index);
            AfApprovalLog approvalLog = null;
            for (OrderUser orderUser : orderUsers) {
                if (role.getRoleCode().equals(orderUser.getRoleId())) {
                    approvalLog = new AfApprovalLog();
                    approvalLog.setRoleId(role.getRoleCode());
                    approvalLog.setUserId(orderUser.getUserId());
                    approvalLog.setInstanceNo(instanceNo);
                    approvalLog.setApprovalNo(UniqueCodeGenerator.AF_APPROVAL_LOG.getCode());
                    approvalLog.setIsApproval(0);
                    approvalLog.setSort(index + 1);
                    approvalLog.setConfigNo(configNo);
                    approvalLog.setProjectNo(projectNo);
                    approvalLog.setScheduleSort(scheduleSort);
                    break;
                }
            }
            if (approvalLog == null) {
                LOGGER.error("未获取到用户编号，projectNo:{}，roleId：{}！", projectNo, role.getRoleCode());
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
        instance.setApprovalOrderNo(approvalOrder.getApprovalOrderNo());
        instance.setCreateUserId(userId);
        instance.setProjectNo(projectNo);

        if (approvalLogs.size() > 1) {
            instance.setCurrentApprovalLogNo(approvalLogs.get(1).getApprovalNo());
            instance.setStatus(1);
        } else {
            instance.setStatus(2);
            sendMessage();
        }

        insert(instance);
        approvalLogService.create(approvalLogs);
    }

    private void insert(AfInstance instance) {
        instanceMapper.insertSelective(instance);
    }

    private void sendMessage(){

    }

    @Override
    public AfInstanceDetailVO detail(String instanceNo, String userId) {
        AfInstanceDetailVO instanceDetailVO = new AfInstanceDetailVO();
        instanceDetailVO.setIsShowButton(false);
        AfInstance instance = findByNo(instanceNo);
        if (instance == null) {
            LOGGER.error("未获取审批流信息，instanceNo:{}！", instanceNo);
            throw new RuntimeException();
        }

        Project project = projectService.findByProjectNo(instance.getProjectNo());
        if (project == null) {
            LOGGER.error("未查询到项目编号为{}的项目！", instance.getProjectNo());
            throw new RuntimeException();
        }
        String customerId = project.getOwnerId();
        AfUserDTO customer = AfUtils.getUserInfo(httpLinks.getUserCenterGetUserMsgUrl(), customerId, Role.CC.id);
        List<UserRoleSet> roles = roleService.findAll();

        List<AfApprovalLogVO> approvalLogVOs = new ArrayList<>();
        List<AfApprovalLog> approvalLogs = approvalLogService.findByInstanceNo(instanceNo);
        if (approvalLogs == null || approvalLogs.size() < 1) {
            LOGGER.error("未查询审批日志，instanceNo:{}！", instanceNo);
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
            AfUserDTO userDTO = AfUtils.getUserInfo(httpLinks.getUserCenterGetUserMsgUrl(), approvalLog.getUserId(), approvalLog.getRoleId());
            approvalLogVO.setUserName(userDTO.getUsername());
            approvalLogVO.setHeadPortrait(userDTO.getHeadPortrait());

            if (approvalLog.getIsApproval() == 1) {
                if (approvalLog.getSort() == 1) {
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
            LOGGER.error("未查询到审批流配置信息，configNo:{}！", instance.getConfigNo());
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

    private String getWaitTip(Date approvalTime) {
        Date currentTime = new Date();
        int days = DateUtil.differentDaysByMillisecond(approvalTime, currentTime);
        int hours = DateUtil.differentHoursByMillisecond(approvalTime, currentTime);
        return "已等待" + days + "天" + hours + "小时";
    }

    @Override
    public void approval(String instanceNo, String userId, Integer option, String remark) {
        if (option != AfConstants.OPTION_REFUSAL && option != AfConstants.OPTION_AGREE) {
            LOGGER.error("未传入正确的参数，option:{}！", option);
            throw new RuntimeException();
        }
        if (option == AfConstants.OPTION_REFUSAL && StringUtils.isEmpty(remark)) {
            LOGGER.error("用户在拒绝的情况下必须写明原因！");
            throw new RuntimeException();
        }
        AfInstance instance = findByNo(instanceNo);
        if (instance == null) {
            LOGGER.error("未获取审批流信息，instanceNo:{}！", instanceNo);
            throw new RuntimeException();
        }

        if (instance.getCurrentApprovalLogNo() == null) {
            LOGGER.error("当前审批流记录编号为空，无法审批！");
            throw new RuntimeException();
        }
        AfApprovalLog approvalLog = approvalLogService.findByNo(instance.getCurrentApprovalLogNo());
        if (approvalLog == null) {
            LOGGER.error("未查询到审批记录，approvalLogNo:{}！", instance.getCurrentApprovalLogNo());
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
                sendSuccessMessage();
            } else {
                instance.setCurrentApprovalLogNo(nextApprovalLog.getApprovalNo());
                sendMessageToNext();
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

    private void sendMessageToNext() {

    }

    private void sendSuccessMessage() {

    }

    private void updateByPrimaryKey(AfInstance instance) {
        instanceMapper.updateByPrimaryKey(instance);
    }

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

        MyRespBundle<List<ProjectBigSchedulingDetailsVO>> schedulingDetailsVOs = schedulingService.getScheduling(projectNo);
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
                                    getCheckAndCompleteStartMenus(startMenus, userId, projectNo, schedulingDetailsVOs.getData(), scheduleSort);
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

    private int getProjectCompleteStatus(List<ProjectBigSchedulingDetailsVO> schedulingDetailsVOs, String projectNo) {
        Integer lastScheduleSort = schedulingDetailsVOs.get(schedulingDetailsVOs.size() - 1).getBigSort();
        return getInstanceStatus(AfConfigs.COMPLETE_APPLICATION.configNo, projectNo, lastScheduleSort);
    }

    private Integer getPreScheduleSort(List<ProjectBigSchedulingDetailsVO> schedulingDetailsVOs, Integer scheduleSort) {
        Integer preScheduleSort = null;
        for (ProjectBigSchedulingDetailsVO schedulingDetailsVO : schedulingDetailsVOs) {
            if (scheduleSort.equals(schedulingDetailsVO.getBigSort())) {
                return preScheduleSort;
            }
            preScheduleSort = schedulingDetailsVO.getBigSort();
        }
        LOGGER.error("当前节点不存在目标项目中，scheduleSort：{}！", scheduleSort);
        throw new RuntimeException();
    }

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

    private boolean isNeedCheck(List<ProjectBigSchedulingDetailsVO> schedulingDetailsVOs, Integer scheduleSort) {
        for (ProjectBigSchedulingDetailsVO schedulingDetailsVO : schedulingDetailsVOs) {
            if (scheduleSort.equals(schedulingDetailsVO.getBigSort())) {
                return schedulingDetailsVO.getIsNeedCheck() == 1;
            }
        }
        LOGGER.error("当前节点不存在目标项目中，scheduleSort：{}！", scheduleSort);
        throw new RuntimeException();
    }

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

    private void addStartMenu(List<AfStartMenuVO> startMenus, String projectNo, String configNo, String userId) {
        AfApprovalOrder approvalOrder = approvalOrderService.findByProjectNoAndConfigNoAndUserId(projectNo, configNo, userId);
        if (approvalOrder != null) {
            AfStartMenuVO startMenuVO = createStartMenu(configNo);
            startMenus.add(startMenuVO);
        }
    }

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
                // 当前节点不存在未完成的验收申请与验收报告，且验收申请与验收报告数量相等，发起完成申请菜单
                addStartMenu(startMenus, projectNo, AfConfigs.COMPLETE_APPLICATION.configNo, userId);
            }
        }
    }

    private int getSuccessCount(List<AfInstance> instances) {
        int count = 0;
        for (AfInstance instance : instances) {
            if (instance.getStatus() == AfConstants.APPROVAL_STATUS_SUCCESS) {
                count++;
            }
        }
        return count;
    }

    private int getStartAndSuccessCount(List<AfInstance> instances) {
        int count = 0;
        for (AfInstance instance : instances) {
            if (instance.getStatus() == AfConstants.APPROVAL_STATUS_START || instance.getStatus() == AfConstants.APPROVAL_STATUS_SUCCESS) {
                count++;
            }
        }
        return count;
    }

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

    private AfStartMenuVO createStartMenu(String configNo) {
        AfStartMenuVO startMenuVO = new AfStartMenuVO();
        AfConfig config = configService.findByNo(configNo);
        startMenuVO.setConfigNo(config.getConfigNo());
        startMenuVO.setConfigName(config.getName());
        return startMenuVO;
    }

    private int getInstanceStatus(String configNo, String projectNo) {
        List<AfInstance> instances = findByConfigNoAndProjectNo(configNo, projectNo);
        return getInstanceStatus(instances);
    }

    private int getInstanceStatus(String configNo, String projectNo, Integer scheduleSort) {
        List<AfInstance> instances = findByConfigNoAndProjectNoAndScheduleSort(configNo, projectNo, scheduleSort);
        return getInstanceStatus(instances);
    }

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

    private void getInstances( List<AfInstanceVO> instanceVOs, String configNo, String userId, String projectNo) {
        List<AfApprovalLog> approvalLogs = approvalLogService.findByConfigNoAndProjectNoAndUserId(configNo, projectNo, userId);
        getInstances(instanceVOs, approvalLogs, configNo, userId);
    }

    private void getInstances( List<AfInstanceVO> instanceVOs, String configNo, String userId, String projectNo, Integer scheduleSort) {
        List<AfApprovalLog> approvalLogs = approvalLogService.findByConfigNoAndProjectNoAndScheduleSortAndUserId(configNo, projectNo, scheduleSort, userId);
        getInstances(instanceVOs, approvalLogs, configNo, userId);
    }

    private void getInstances( List<AfInstanceVO> instanceVOs, List<AfApprovalLog> approvalLogs, String configNo, String userId) {
        AfConfig config = configService.findByNo(configNo);
        if (config == null) {
            LOGGER.error("未查询到审批流配置信息，configNo:{}！", configNo);
            throw new RuntimeException();
        }
        if (approvalLogs != null) {
            for (AfApprovalLog approvalLog : approvalLogs) {
                AfInstanceVO instanceVO = new AfInstanceVO();
                instanceVO.setIsShowButton(false);
                AfInstance instance = findByNo(approvalLog.getInstanceNo());
                if (instance == null) {
                    LOGGER.error("未查询到审批流信息，instanceNo:{}！", approvalLog.getInstanceNo());
                    throw new RuntimeException();
                }
                UserRoleSet role = roleService.findById(approvalLog.getRoleId());
                if (role == null) {
                    LOGGER.error("未查询到角色信息，roleId:{}！", approvalLog.getRoleId());
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
                AfUserDTO userDTO = AfUtils.getUserInfo(httpLinks.getUserCenterGetUserMsgUrl(), instance.getCreateUserId(), instance.getCreateRoleId());
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

    @Override
    public AfInstanceDetailVO Mstart(String projectNo, String userId, String configNo, Integer scheduleSort) {
        AfInstanceDetailVO instanceDetailVO = new AfInstanceDetailVO();
        AfConfig config = configService.findByNo(configNo);
        instanceDetailVO.setConfigName(config.getName());
        instanceDetailVO.setConfigNo(configNo);
        instanceDetailVO.setAddress("北京市朝阳区美立方4号楼");
        instanceDetailVO.setIsShowButton(true);
        instanceDetailVO.setProjectNo("aaaaaaaaaaaaaaaaaaaaaaaaaa");
        instanceDetailVO.setCustomerId("CC123456");
        instanceDetailVO.setCustomerName("金庸");
        instanceDetailVO.setScheduleSort(scheduleSort);

        List<AfApprovalLogVO> approvalLogs = new ArrayList<>(4);
        for (int i = 0; i < 4; i++) {
            AfApprovalLogVO afApprovalLogVO = new AfApprovalLogVO();
            afApprovalLogVO.setRoleId("CC");
            afApprovalLogVO.setRoleName("业主" + i);
            afApprovalLogVO.setUserId("aaaaaa" + i);
            afApprovalLogVO.setUserName("张三" + i);
            approvalLogs.add(afApprovalLogVO);
        }
        instanceDetailVO.setApprovalLogs(approvalLogs);
        return instanceDetailVO;
    }

    @Override
    public void MsubmitStart(String projectNo, String userId, String configNo, Integer scheduleSort, String data, String remark) {
        if (StringUtils.isEmpty(projectNo) || StringUtils.isEmpty(userId) || StringUtils.isEmpty(configNo)) {
            throw new RuntimeException();
        }
    }

    @Override
    public AfInstanceDetailVO Mdetail(String instanceNo, String userId) {

        String configNo = AfConfigs.START_APPLICATION.configNo;

        AfInstanceDetailVO instanceDetailVO = new AfInstanceDetailVO();
        AfConfig config = configService.findByNo(configNo);
        instanceDetailVO.setConfigName(config.getName());
        instanceDetailVO.setConfigNo(configNo);
        instanceDetailVO.setAddress("北京市朝阳区美立方4号楼");
        instanceDetailVO.setIsShowButton(true);
        instanceDetailVO.setProjectNo("aaaaaaaaaaaaaaaaaaaaaaaaaa");
        instanceDetailVO.setCustomerId("CC123456");
        instanceDetailVO.setCustomerName("金庸");

        List<AfApprovalLogVO> approvalLogs = new ArrayList<>(4);
        for (int i = 0; i < 4; i++) {
            AfApprovalLogVO afApprovalLogVO = new AfApprovalLogVO();
            afApprovalLogVO.setRoleId("CC");
            afApprovalLogVO.setRoleName("业主" + i);
            afApprovalLogVO.setUserId("aaaaaa" + i);
            afApprovalLogVO.setUserName("张三" + i);
            if (i < 1) {
                afApprovalLogVO.setStatus(1);
                afApprovalLogVO.setApprovalTime(new Date());
            } else if (i == 1){
                afApprovalLogVO.setStatus(2);
            }
            approvalLogs.add(afApprovalLogVO);
        }
        instanceDetailVO.setApprovalLogs(approvalLogs);

        return instanceDetailVO;
    }

    @Override
    public void Mapproval(String instanceNo, String userId, Integer option, String remark) {
        if (StringUtils.isEmpty(instanceNo) || StringUtils.isEmpty(userId) || option == null) {
            throw new RuntimeException();
        }
        if (option == 0 && StringUtils.isEmpty(remark)) {
            throw new RuntimeException("拒绝必须写原因");
        }
    }

    @Override
    public AfInstanceListVO Mlist(String userId, String projectNo, String approvalType, Integer scheduleSort) {
        AfInstanceListVO instanceListVO = new AfInstanceListVO();

        List<AfInstanceVO> instanceVOS = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            AfInstanceVO instanceVO = new AfInstanceVO();
            instanceVO.setInstanceNo(UniqueCodeGenerator.AF_INSTANCE.getCode());
            instanceVO.setCreateUsername("张三" + i);
            instanceVO.setRoleId("CC");
            instanceVO.setRoleName("业主" + i);
            instanceVO.setCreateTime(new Date());
            instanceVO.setConfigName("开工申请" + i);
            instanceVO.setRemark("啦啦啦啦啦啦啦啦啦");
            if (i % 3 == 0) {
                instanceVO.setIsShowButton(true);
                instanceVO.setStatus(AfConstants.APPROVAL_STATUS_START);
            } else {
                instanceVO.setIsShowButton(false);
                if (i % 2 == 0) {
                    instanceVO.setStatus(AfConstants.APPROVAL_STATUS_FAIL);
                } else if (i == 1) {
                    instanceVO.setStatus(AfConstants.APPROVAL_STATUS_START);
                } else {
                    instanceVO.setStatus(AfConstants.APPROVAL_STATUS_SUCCESS);
                }
            }
            instanceVOS.add(instanceVO);
        }

        List<AfStartMenuVO> startMenuVOS = new ArrayList<>();
        {
            AfStartMenuVO startMenuVO = new AfStartMenuVO();
            startMenuVO.setConfigNo(AfConfigs.START_APPLICATION.configNo);
            startMenuVO.setConfigName(AfConfigs.START_APPLICATION.name);
            startMenuVOS.add(startMenuVO);
            startMenuVO = new AfStartMenuVO();
            startMenuVO.setConfigNo(AfConfigs.COMPLETE_APPLICATION.configNo);
            startMenuVO.setConfigName(AfConfigs.COMPLETE_APPLICATION.name);
            startMenuVOS.add(startMenuVO);
        }
        instanceListVO.setInstances(instanceVOS);
        instanceListVO.setStartMenus(startMenuVOS);
        return instanceListVO;
    }
}
