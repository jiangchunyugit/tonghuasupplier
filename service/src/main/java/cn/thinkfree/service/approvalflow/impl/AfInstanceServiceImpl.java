package cn.thinkfree.service.approvalflow.impl;

import cn.thinkfree.core.base.MyLogger;
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
import cn.thinkfree.service.project.ProjectService;
import cn.thinkfree.service.utils.AfUtils;
import cn.thinkfree.service.utils.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * TODO
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
    private AfConfigSchemeService configPlanService;
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
            // TODO
            // 不具有审批资格
        }
        List<UserRoleSet> approvalRoles = approvalRoleService.findByApprovalOrderNo(approvalOrder.getApprovalOrderNo(), roles);
        if (approvalRoles == null) {
            // TODO
        }

        List<OrderUser> orderUsers = orderUserService.findByProjectNo(projectNo);
        List<AfApprovalLogVO> approvalLogVOs = new ArrayList<>();
        if (approvalRoles != null) {
            for (UserRoleSet role : approvalRoles) {
                AfApprovalLogVO approvalLogVO = new AfApprovalLogVO();

                for (OrderUser orderUser : orderUsers) {
                    if (orderUser.getRoleId().equals(role.getRoleCode())) {
                        approvalLogVO.setUserId(orderUser.getUserId());
                        break;
                    }
                }
                if (approvalLogVO.getUserId() == null) {
                    // TODO
                }

                approvalLogVO.setRoleId(role.getRoleCode());
                approvalLogVO.setRoleName(role.getRoleName());
                approvalLogVO.setIsApproval(false);
                AfUserDTO userDTO = AfUtils.getUserInfo(httpLinks.getUserCenterGetUserMsgUrl(), userId, approvalLogVO.getRoleId());
                approvalLogVO.setUserName(userDTO.getUsername());
                approvalLogVO.setHeadPortrait(userDTO.getHeadPortrait());
                approvalLogVOs.add(approvalLogVO);
            }
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

    private AfUserDTO getUserNameByUserIdAndRoleId(String userId, String roleId) {
        return AfUtils.getUserInfo(httpLinks.getUserCenterGetUserMsgUrl(), userId, roleId);
    }

    private AfUserDTO getCustomerNameById(String customerId) {
        return getUserNameByUserIdAndRoleId(customerId, Role.CC.id);
    }

    @Override
    public void submitStart(String projectNo, String userId, String configNo, Integer scheduleSort, String data, String remark) {

        List<UserRoleSet> roles = roleService.findAll();
        AfApprovalOrder approvalOrder = approvalOrderService.findByProjectNoAndConfigNoAndUserId(projectNo, configNo, userId);
        if (approvalOrder == null) {
            // TODO
            // 不具有审批资格
        }
        List<UserRoleSet> approvalRoles = approvalRoleService.findByApprovalOrderNo(approvalOrder.getApprovalOrderNo(), roles);
        if (approvalRoles == null || approvalRoles.size() < 1) {
            // TODO
        }
        List<AfApprovalLog> approvalLogs = new ArrayList<>(approvalRoles.size());
        List<OrderUser> orderUsers = orderUserService.findByProjectNo(projectNo);

        String instanceNo = UniqueCodeGenerator.AF_INSTANCE.getCode();
        for (UserRoleSet role : approvalRoles) {
            AfApprovalLog approvalLog = null;
            for (OrderUser orderUser : orderUsers) {
                if (role.getRoleCode().equals(orderUser.getRoleId())) {
                    approvalLog = new AfApprovalLog();
                    approvalLog.setRoleId(role.getRoleCode());
                    approvalLog.setUserId(orderUser.getUserId());
                    approvalLog.setInstanceNo(instanceNo);
                    approvalLog.setApprovalNo(UniqueCodeGenerator.AF_APPROVAL_LOG.getCode());
                    break;
                }
            }
            if (approvalLog == null) {
                // TODO
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
        AfInstance instance = findByNo(instanceNo);
        if (instance == null) {
            // TODO
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
            // TODO
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
            AfUserDTO userDTO = AfUtils.getUserInfo(httpLinks.getUserCenterGetUserMsgUrl(), approvalLogVO.getUserId(), approvalLogVO.getRoleId());
            approvalLogVO.setUserName(userDTO.getUsername());
            approvalLogVO.setHeadPortrait(userDTO.getHeadPortrait());

            if (approvalLog.getIsApproval() == 1) {
                approvalLogVO.setIsApproval(true);
                approvalLogVO.setApprovalTime(approvalLog.getApprovalTime());
                approvalLogVO.setRemark(approvalLog.getRemark());
                approvalTime = approvalLog.getApprovalTime();
                wait = 1;
            } else {
                approvalLogVO.setIsApproval(false);
                if (wait == 1) {
                    approvalLogVO.setWaitTip(getWaitTip(approvalTime));
                    wait++;
                }
            }

            if (approvalLog.getApprovalNo().equals(instance.getCurrentApprovalLogNo()) && approvalLog.getUserId().equals(userId)) {
                instanceDetailVO.setEditable(true);
            } else {
                instanceDetailVO.setEditable(false);
            }

            approvalLogVOs.add(approvalLogVO);
        }

        AfConfig config = configService.findByNo(instance.getConfigNo());
        if (config == null) {
            // TODO
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
        if (option == 0 && StringUtils.isEmpty(remark)) {
            // TODO
        }
        AfInstance instance = findByNo(instanceNo);
        if (instance == null) {
            // TODO
        }

        if (instance.getCurrentApprovalLogNo() == null) {
            // TODO
        }
        AfApprovalLog approvalLog = approvalLogService.findByNo(instance.getCurrentApprovalLogNo());
        if (approvalLog == null) {
            // TODO
        }
        String recordUserId = orderUserService.findUserIdByProjectNoAndRoleId(instance.getProjectNo(), approvalLog.getRoleId());
        if (!userId.equals(recordUserId)) {
            // TODO
        }

        approvalLog.setRemark(remark);

        if (option == 1) {
            // 同意
            approvalLog.setApprovalTime(new Date());
            approvalLog.setIsApproval(1);
            AfApprovalLog nextApprovalLog = approvalLogService.findByInstanceNoAndSort(instanceNo, approvalLog.getSort() + 1);
            if (nextApprovalLog == null) {
                // 结束审批流
                instance.setStatus(2);
                instance.setCurrentApprovalLogNo(null);
                sendSuccessMessage();
            } else {
                instance.setCurrentApprovalLogNo(nextApprovalLog.getApprovalNo());
                sendMessageToNext();
            }
        } else {
            // 不同意
            instance.setStatus(3);
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
        if (AfConstants.APPROVAL_TYPE_CONSTRUCTION_CHANGE.equals(approvalType)) {
            // 进度验收
            if (scheduleSort == null) {
                // 开工准备:开工申请
                getInstances(instanceVOs, AfConfigs.START_APPLICATION.configNo, userId, projectNo);
                // 开工准备:开工报告
                getInstances(instanceVOs, AfConfigs.START_REPORT.configNo, userId, projectNo);
                // 获取开工准备阶段发起菜单
                getStartStartMenus(startMenus, userId, projectNo);
            } else {
                // 完工审批
                getInstances(instanceVOs, AfConfigs.COMPLETE_APPLICATION.configNo, userId, projectNo, scheduleSort);
                // 获取完工申请发起菜单
                getCompleteStartMenus(startMenus, AfConfigs.COMPLETE_APPLICATION.configNo, userId, projectNo, scheduleSort);
                // 验收申请
                getInstances(instanceVOs, AfConfigs.CHECK_APPLICATION.configNo, userId, projectNo, scheduleSort);
                // 验收报告
                getInstances(instanceVOs, AfConfigs.CHECK_REPORT.configNo, userId, projectNo, scheduleSort);
                getStartMenus(startMenus, userId, projectNo, scheduleSort);
            }

        } else if (AfConstants.APPROVAL_TYPE_PROBLEM_RECTIFICATION.equals(approvalType)) {
            // 问题整改：问题整改
            getInstances(instanceVOs, AfConfigs.PROBLEM_RECTIFICATION.configNo, userId, projectNo);
            // 问题整改：整改完成
            getInstances(instanceVOs, AfConfigs.RECTIFICATION_COMPLETE.configNo, userId, projectNo);

            getStartMenus(startMenus, userId, projectNo, AfConfigs.PROBLEM_RECTIFICATION.configNo, AfConfigs.RECTIFICATION_COMPLETE.configNo);
        } else if (AfConstants.APPROVAL_TYPE_SCHEDULE_APPROVAL.equals(approvalType)) {
            // 施工变更：变更单
            getInstances(instanceVOs, AfConfigs.CHANGE_ORDER.configNo, userId, projectNo);
            // 施工变更：变更完成
            getInstances(instanceVOs, AfConfigs.CHANGE_COMPLETE.configNo, userId, projectNo);
        } else if (AfConstants.APPROVAL_TYPE_DELAY_VERIFY.equals(approvalType)) {
            // 延期确认
            getInstances(instanceVOs, AfConfigs.DELAY_ORDER.configNo, userId, projectNo);
        }
        instanceListVO.setInstances(instanceVOs);
        instanceListVO.setStartMenus(startMenus);
        return instanceListVO;
    }

    private void getStartMenus(List<AfStartMenuVO> startMenus, String userId, String projectNo, String startConfigNo, String completeConfigNo) {
        boolean projectComplete = projectComplete(projectNo);
        if (!projectComplete) {
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
    }

    private void addStartMenu(List<AfStartMenuVO> startMenus, String projectNo, String configNo, String userId) {
        AfApprovalOrder approvalOrder = approvalOrderService.findByProjectNoAndConfigNoAndUserId(projectNo, configNo, userId);
        if (approvalOrder != null) {
            AfStartMenuVO startMenuVO = createStartMenu(configNo);
            startMenus.add(startMenuVO);
        }
    }

    private boolean projectComplete(String projectNo) {
        return false;
    }

    private void getStartMenus(List<AfStartMenuVO> startMenus, String userId, String projectNo, Integer scheduleSort) {
        Integer preScheduleSort = getPreScheduleSort(scheduleSort);
        int preStatus = getInstanceStatus(AfConfigs.COMPLETE_APPLICATION.configNo, projectNo, preScheduleSort);
        if (preStatus == AfConstants.APPROVAL_STATUS_SUCCESS) {
            // 上一个节点完成
            int status = getInstanceStatus(AfConfigs.COMPLETE_APPLICATION.configNo, projectNo, scheduleSort);
            if (status == 0 || status == AfConstants.APPROVAL_STATUS_FAIL) {
                // 当前节点未完成
                List<AfInstance> checkApplicationInstances = findByConfigNoAndProjectNoAndScheduleSort(AfConfigs.CHECK_APPLICATION.configNo, projectNo, scheduleSort);
                List<AfInstance> checkReportInstances = findByConfigNoAndProjectNoAndScheduleSort(AfConfigs.CHECK_REPORT.configNo, projectNo, scheduleSort);

                int checkApplicationStatus = getInstanceStatus(checkApplicationInstances);
                int checkReportStatus = getInstanceStatus(checkReportInstances);

                int checkApplicationCount = getSuccessCount(checkApplicationInstances);
                int checkReportCount = getStartAndSuccessCount(checkReportInstances);
                // 发起验收申请菜单
                addStartMenu(startMenus, projectNo, AfConfigs.CHECK_APPLICATION.configNo, userId);

                if (checkApplicationCount > checkReportCount) {
                    // 如果验收申请数量大于验收报告数量，发起验收报告菜单
                    addStartMenu(startMenus, projectNo, AfConfigs.START_APPLICATION.configNo, userId);

                }
                if (checkApplicationStatus != AfConstants.APPROVAL_STATUS_START && checkReportStatus != AfConstants.APPROVAL_STATUS_START) {
                    if (checkApplicationCount == checkReportCount) {
                        // 当前节点不存在未完成的验收申请与验收报告，且验收申请与验收报告数量相等，发起完成申请菜单
                        addStartMenu(startMenus, projectNo, AfConfigs.COMPLETE_APPLICATION.configNo, userId);
                    }
                }
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

    private void getCompleteStartMenus(List<AfStartMenuVO> startMenus, String configNo, String userId, String projectNo, Integer scheduleSort){
        Integer preScheduleSort = getPreScheduleSort(scheduleSort);
        int preStatus = getInstanceStatus(configNo, projectNo, preScheduleSort);
        if (preStatus == AfConstants.APPROVAL_STATUS_SUCCESS) {
            int status = getInstanceStatus(configNo, projectNo, scheduleSort);
            if (status == 0 || status == AfConstants.APPROVAL_STATUS_FAIL) {
                addStartMenu(startMenus, projectNo, configNo, userId);
            }
        }
    }

    /**
     * 获取开工准备发起菜单
     * @param startMenus 发起菜单
     * @param userId 用户id
     * @param projectNo
     */
    private void getStartStartMenus(List<AfStartMenuVO> startMenus, String userId, String projectNo) {
        int startApplicationStatus = getInstanceStatus(AfConfigs.START_APPLICATION.configNo, projectNo);
        if (startApplicationStatus == 0 || startApplicationStatus == AfConstants.APPROVAL_STATUS_FAIL) {
            addStartMenu(startMenus, projectNo, AfConfigs.START_APPLICATION.configNo, userId);
        } else if (startApplicationStatus == AfConstants.APPROVAL_STATUS_SUCCESS ) {
            int startReportStatus = getInstanceStatus(AfConfigs.START_REPORT.configNo, projectNo);
            if (startReportStatus == 0 || startReportStatus == AfConstants.APPROVAL_STATUS_FAIL ) {
                addStartMenu(startMenus, projectNo, AfConfigs.START_REPORT.configNo, userId);
            }
        }
    }

    private AfStartMenuVO createStartMenu(String configNo) {
        AfStartMenuVO startMenuVO = new AfStartMenuVO();
        AfConfig config = configService.findByNo(configNo);
        startMenuVO.setConfigNo(config.getConfigNo());
        startMenuVO.setConfigName(config.getName());
        return startMenuVO;
    }

    private int getInstanceStatus(String config, String projectNo) {
        List<AfInstance> instances = findByConfigNoAndProjectNo(config, projectNo);
        return getInstanceStatus(instances);
    }

    private int getInstanceStatus(String config, String projectNo, Integer scheduleSort) {
        List<AfInstance> instances = findByConfigNoAndProjectNoAndScheduleSort(config, projectNo, scheduleSort);
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

    private Integer getPreScheduleSort(Integer scheduleSort) {
        return scheduleSort - 1;
    }

    private void getInstances( List<AfInstanceVO> instanceVOs, String configNo, String userId, String projectNo) {
        List<AfInstance> instances = findByConfigNoAndProjectNo(configNo, projectNo);
        getInstances(instanceVOs, instances, configNo, userId);
    }

    private void getInstances( List<AfInstanceVO> instanceVOs, String configNo, String userId, String projectNo, Integer scheduleSort) {
        List<AfInstance> instances = findByConfigNoAndProjectNoAndScheduleSort(configNo, projectNo, scheduleSort);
        getInstances(instanceVOs, instances, configNo, userId);
    }

    private void getInstances( List<AfInstanceVO> instanceVOs, List<AfInstance> instances, String configNo, String userId) {
        AfConfig config = configService.findByNo(configNo);
        if (config == null) {
            // TODO
        }
        if (instances != null) {
            for (AfInstance instance : instances) {
                AfInstanceVO instanceVO = new AfInstanceVO();
                AfApprovalLog approvalLog = approvalLogService.findByInstanceNoAndSort(instance.getInstanceNo(), 1);
                if (approvalLog == null) {
                    // TODO
                }
                UserRoleSet role = roleService.findById(approvalLog.getRoleId());
                if (role == null) {
                    // TODO
                }
                if (instance.getStatus() == 1) {
                    AfApprovalLog currentApprovalLog = approvalLogService.findByNo(instance.getCurrentApprovalLogNo());

                    if (currentApprovalLog.getUserId().equals(userId)) {
                        // 待审批用户与当前用户相同
                        instanceVO.setIsShowButton(true);
                    } else {
                        // 待审批用户与当前用户不同
                        instanceVO.setIsShowButton(false);
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
    public AfInstanceDetailVO Mstart(String projectNo, String userId, String configNo, Integer scheduleSort) {
        AfInstanceDetailVO instanceDetailVO = new AfInstanceDetailVO();
        AfConfig config = configService.findByNo(configNo);
        instanceDetailVO.setConfigName(config.getName());
        instanceDetailVO.setConfigNo(configNo);
        instanceDetailVO.setAddress("北京市朝阳区美立方4号楼");
        instanceDetailVO.setEditable(true);
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
        instanceDetailVO.setEditable(true);
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
                afApprovalLogVO.setIsApproval(true);
                afApprovalLogVO.setRemark("啦啦啦啦");
                afApprovalLogVO.setApprovalTime(new Date());
            } else {
                afApprovalLogVO.setIsApproval(false);
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
