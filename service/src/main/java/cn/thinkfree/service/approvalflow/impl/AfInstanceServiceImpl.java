package cn.thinkfree.service.approvalflow.impl;

import cn.thinkfree.core.base.MyLogger;
import cn.thinkfree.core.constants.AFAlias;
import cn.thinkfree.core.constants.AfConstants;
import cn.thinkfree.core.utils.UniqueCodeGenerator;
import cn.thinkfree.database.mapper.AfInstanceMapper;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.vo.*;
import cn.thinkfree.service.approvalflow.*;
import cn.thinkfree.service.neworder.NewOrderUserService;
import cn.thinkfree.service.project.ProjectService;
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
    private AfPlanService planService;
    @Resource
    private AfConfigService configService;

    @Override
    public AfInstanceDetailVO start(String projectNo, String userId, String configNo) {
        AfInstanceDetailVO instanceDetailVO = new AfInstanceDetailVO();
        Project project = projectService.findByProjectNo(projectNo);
        if (project == null) {
            LOGGER.error("未查询到项目编号为{}的项目！", projectNo);
            throw new RuntimeException();
        }

        String customerId = project.getOwnerId();
        String customerName = getCustomerNameById(customerId);

        String planNo = getPlanNo();
        List<UserRoleSet> roles = roleService.findAll();

        List<UserRoleSet> approvalRoles = approvalRoleService.findByPlanNo(planNo, roles);

        List<OrderUser> orderUsers = orderUserService.findByOrderNo(projectNo);
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

                for (UserRoleSet record : roles) {
                    if (record.getRoleCode().equals(role.getRoleCode())) {
                        approvalLogVO.setRoleId(role.getRoleCode());
                        approvalLogVO.setRoleName(role.getRoleName());
                        break;
                    }
                }
                if (approvalLogVO.getRoleId() == null) {
                    // TODO
                }
                approvalLogVO.setIsApproval(false);
                approvalLogVO.setUserName(getUserNameByUserIdAndRoleId(approvalLogVO.getUserId(), approvalLogVO.getRoleId()));
                approvalLogVOs.add(approvalLogVO);
            }
        }

        instanceDetailVO.setProjectNo(projectNo);
        instanceDetailVO.setCustomerId(customerId);
        instanceDetailVO.setCustomerName(customerName);
        instanceDetailVO.setApprovalLogs(approvalLogVOs);
        return instanceDetailVO;
    }

    private String getUserNameByUserIdAndRoleId(String userId, String roleId) {
        return "";
    }

    private String getPlanNo() {
        // TODO
        return "";
    }

    private String getCustomerNameById(String customerId) {
        // TODO
        return "";
    }

    @Override
    public void submitStart(String projectNo, String userId, String planNo, Integer scheduleSort, String data, String remark) {
        AfPlanVO planVO = planService.findByPlanNo(planNo);
        if (planVO == null) {
            // TODO
        }
        List<UserRoleSet> roles = planVO.getRoles();
        if (roles == null || roles.size() == 0) {
            // TODO
        }
        List<AfApprovalLog> approvalLogs = new ArrayList<>(roles.size());
        List<OrderUser> orderUsers = orderUserService.findByOrderNo(projectNo);
        for (UserRoleSet role : roles) {
            AfApprovalLog approvalLog = null;
            for (OrderUser orderUser : orderUsers) {
                if (role.getRoleCode().equals(orderUser.getRoleId())) {
                    approvalLog = new AfApprovalLog();
                    approvalLog.setRoleId(role.getRoleCode());
                    approvalLog.setUserId(orderUser.getUserId());
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
        if (!approvalLog.getUserId().equals(userId)) {
            // TODO
        }
        AfInstance instance = new AfInstance();
        instance.setCompanyNo(null);
        instance.setConfigLogNo(planVO.getPlan().getConfigLogNo());
        instance.setCreateRoleId(approvalLog.getRoleId());
        instance.setCreateTime(new Date());
        instance.setData(data);
        instance.setInstanceNo(UniqueCodeGenerator.AF_INSTANCE.getCode());
        instance.setPlanNo(planNo);
        instance.setScheduleSort(scheduleSort);
        if (approvalLogs.size() > 1) {
            instance.setCurrentApprovalLogNo(approvalLogs.get(1).getApprovalNo());
            instance.setStatus(1);
        } else {
            instance.setStatus(2);
            sendMessage();
        }
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
        String customerName = getCustomerNameById(customerId);

        String planNo = getPlanNo();
        List<UserRoleSet> roles = roleService.findAll();

        List<AfApprovalLogVO> approvalLogVOs = new ArrayList<>();
        List<AfApprovalLog> approvalLogs = approvalLogService.findByInstanceNo(instanceNo);
        if (approvalLogs == null || approvalLogs.size() < 1) {
            // TODO
        }
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
            approvalLogVO.setUserName(getUserNameByUserIdAndRoleId(approvalLogVO.getUserId(), approvalLogVO.getRoleId()));

            if (approvalLog.getIsApproval() == 1) {
                approvalLogVO.setIsApproval(true);
                approvalLogVO.setApprovalTime(approvalLog.getApprovalTime());
                approvalLogVO.setRemark(approvalLog.getRemark());
            } else {
                approvalLogVO.setIsApproval(false);
            }

            if (approvalLog.getApprovalNo().equals(instance.getCurrentApprovalLogNo())) {
                instanceDetailVO.setEditable(true);
            } else {
                instanceDetailVO.setEditable(false);
            }

            approvalLogVOs.add(approvalLogVO);
        }

        AfConfig config = configService.findByConfigNo(instance.getConfigNo());
        if (config == null) {

        }
        instanceDetailVO.setInstanceNo(instanceNo);
        instanceDetailVO.setAddress(project.getAddress() + project.getAddressDetail());
        instanceDetailVO.setConfigNo(instance.getConfigNo());
        instanceDetailVO.setConfigName(config.getName());
        instanceDetailVO.setPlanNo(planNo);
        instanceDetailVO.setData(instance.getData());
        instanceDetailVO.setRemark(approvalLogs.get(0).getRemark());
        instanceDetailVO.setProjectNo(instance.getProjectNo());
        instanceDetailVO.setCustomerId(customerId);
        instanceDetailVO.setCustomerName(customerName);
        instanceDetailVO.setApprovalLogs(approvalLogVOs);
        return instanceDetailVO;
    }

    @Override
    public void approval(String instanceNo, String userId, Integer option, String remark) {
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
        String recordUserId = orderUserService.findUserIdByOrderNoAndRoleId(instance.getProjectNo(), approvalLog.getRoleId());
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
                sendMessage();
            } else {
                instance.setCurrentApprovalLogNo(nextApprovalLog.getApprovalNo());
            }
        } else {
            // 不同意
            instance.setStatus(3);
            instance.setCurrentApprovalLogNo(null);
        }

        updateByPrimaryKey(instance);
        approvalLogService.updateByPrimaryKey(approvalLog);
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
    public AfInstanceListVO list(String userId, String projectNo, Integer scheduleSort) {

        List<AfInstanceVO> instanceVOs = getInstances(userId, projectNo, scheduleSort);
        List<AfConfigPlanVO> configPlanVOs = getConfigPlans(userId, projectNo, scheduleSort);
        AfInstanceListVO instanceListVO = new AfInstanceListVO();
        instanceListVO.setInstances(instanceVOs);
        instanceListVO.setConfigPlans(configPlanVOs);
        return instanceListVO;
    }

    /**
     * 获取发起列表
     * @param userId 用户ID
     * @param projectNo 项目编号
     * @param scheduleSort 排期编号
     * @return 发起列表
     */
    private List<AfConfigPlanVO> getConfigPlans(String userId, String projectNo, Integer scheduleSort){
        List<AfConfigPlanVO> configPlanVOs = new ArrayList<>();
        // 获取上一个排期编号
        Integer preScheduleSort = getPreScheduleSort(scheduleSort);

        String companyNo = getCompanyNo();
        String roleId = orderUserService.findRoleIdByOrderNoAndUserId(projectNo, userId);
        if (scheduleSort == -1) {
            startApplication(configPlanVOs, companyNo, roleId, projectNo, scheduleSort);
        } else {
            boolean preSuccess = scheduleCompleteApplicationSuccess(projectNo, preScheduleSort);
            // 上一个节点完工
            if (preSuccess) {
                AfConfig config = configService.findByAlias(AFAlias.COMPLETE_APPLICATION.alias);
                if (config == null) {
                    // TODO
                }
                int completeApplicationStatus = scheduleCompleteApplicationStatus(config.getConfigNo(), projectNo, scheduleSort);
                if (completeApplicationStatus != AfConstants.APPROVAL_STATUS_SUCCESS && completeApplicationStatus != AfConstants.APPROVAL_STATUS_START) {
                    // 当前节点未完工，且未发起完工审批
                    checkApplication(configPlanVOs, companyNo, roleId, projectNo, scheduleSort);
                    problemRectification(configPlanVOs, companyNo, roleId, projectNo, scheduleSort);
                    changeOrder(configPlanVOs, companyNo, roleId, projectNo, scheduleSort);
                    delayOrder(configPlanVOs, companyNo, roleId, projectNo, scheduleSort);
                    completeApplication(configPlanVOs, config, companyNo, roleId, projectNo, scheduleSort);
                }
            }
        }

        return configPlanVOs;
    }

    /**
     * 是否能发起完工申请
     * @param configPlanVOs
     * @param config
     * @param companyNo
     * @param roleId
     * @param projectNo
     * @param scheduleSort
     */
    private void completeApplication(List<AfConfigPlanVO> configPlanVOs, AfConfig config, String companyNo, String roleId, String projectNo, Integer scheduleSort){
        List<AfInstance> instances = findByProjectNoAndScheduleSort(projectNo, scheduleSort);
        boolean complete = true;
        if (instances != null) {
            int checkApplicationCount = 0, checkReportCount = 0, problemRectificationCount = 0, rectificationCompleteCount = 0, changeOrderCount = 0, changeCompleteCount = 0;
            String checkApplicationConfigNo = configService.findConfigNoByAlias(AFAlias.CHECK_APPLICATION.alias);
            String checkReportConfigNo = configService.findConfigNoByAlias(AFAlias.CHECK_REPORT.alias);
            String problemRectificationConfigNo = configService.findConfigNoByAlias(AFAlias.PROBLEM_RECTIFICATION.alias);
            String rectificationCompleteConfigNo = configService.findConfigNoByAlias(AFAlias.RECTIFICATION_COMPLETE.alias);
            String changeOrderConfigNo = configService.findConfigNoByAlias(AFAlias.CHANGE_ORDER.alias);
            String changeCompleteConfigNo = configService.findConfigNoByAlias(AFAlias.CHANGE_COMPLETE.alias);

            for (AfInstance instance : instances) {
                if (instance.getStatus() == AfConstants.APPROVAL_STATUS_START) {
                    complete = false;
                    break;
                } else if (instance.getStatus() == AfConstants.APPROVAL_STATUS_SUCCESS){
                    if (instance.getConfigNo().equals(checkApplicationConfigNo)) {
                        checkApplicationCount++;
                    } else if (instance.getConfigNo().equals(checkReportConfigNo)) {
                        checkReportCount++;
                    } else if (instance.getConfigNo().equals(problemRectificationConfigNo)) {
                        problemRectificationCount++;
                    } else if (instance.getConfigNo().equals(rectificationCompleteConfigNo)) {
                        rectificationCompleteCount++;
                    } else if (instance.getConfigNo().equals(changeOrderConfigNo)) {
                        changeOrderCount++;
                    } else if (instance.getConfigNo().equals(changeCompleteConfigNo)) {
                        changeCompleteCount++;
                    }
                }
            }
            if (complete) {
                if (checkApplicationCount != checkReportCount
                        || problemRectificationCount != rectificationCompleteCount
                        || changeOrderCount != changeCompleteCount) {
                    complete = false;
                }
            }
        }
        if (complete) {
            AfPlan plan = planService.findByConfigNoAndCompanyNoAndRoleId(config.getConfigNo(), companyNo, roleId);
            if (plan != null) {
                // 当前用户为发起用户
                AfConfigPlanVO configPlanVO = createConfigPlanVO(plan.getPlanNo(), config.getName(), config.getConfigNo());
                configPlanVOs.add(configPlanVO);
            }
        }
    }

    private int scheduleCompleteApplicationStatus(String configNo, String projectNo, Integer scheduleSort) {

        List<AfInstance> instances = findByConfigNoAndProjectNoAndScheduleSort(configNo, projectNo, scheduleSort);
        int status = 0;
        if (instances != null) {
            for (AfInstance instance : instances) {
                if (instance.getStatus() == AfConstants.APPROVAL_STATUS_SUCCESS || instance.getStatus() == AfConstants.APPROVAL_STATUS_START) {
                    status = instance.getStatus();
                    break;
                } else {
                    status = instance.getStatus();
                }
            }
        }
        return status;
    }

    private void startApplication(List<AfConfigPlanVO> configPlanVOs, String companyNo, String roleId, String projectNo, Integer scheduleSort){
        // 开工申请
        AfConfig config = configService.findByAlias(AFAlias.START_APPLICATION.alias);
        if (config == null) {
            // TODO
        }
        int status = startStatus(config.getConfigNo(), projectNo);
        if (status == AfConstants.APPROVAL_STATUS_SUCCESS) {
            // 开工申请完成，判断开工报告
            config = configService.findByAlias(AFAlias.START_REPORT.alias);
            if (config == null) {
                // TODO
            }
            status = startStatus(config.getConfigNo(), projectNo);
        }
        if (status != AfConstants.APPROVAL_STATUS_START && status != AfConstants.APPROVAL_STATUS_SUCCESS) {
            AfPlan plan = planService.findByConfigNoAndCompanyNoAndRoleId(config.getConfigNo(), companyNo, roleId);
            if (plan != null) {
                // 当前用户为发起用户
                AfConfigPlanVO configPlanVO = createConfigPlanVO(plan.getPlanNo(), config.getName(), config.getConfigNo());
                configPlanVOs.add(configPlanVO);
            }
        }
    }

    /**
     * 项目开工申请或开工报告是否成功，因一个项目只能有一个开工申请或开工报告成功，所以不考虑排期
     * @param startApplicationConfigNo 开工申请审批配置编号
     * @param projectNo 项目编号
     * @return 开工申请是否成功
     */
    private int startStatus(String startApplicationConfigNo, String projectNo) {
        int status = 0;
        List<AfInstance> instances = findByConfigNoAndProjectNo(startApplicationConfigNo, projectNo);
        if (instances != null && instances.size() > 0) {
            for (AfInstance instance : instances) {
                if (instance.getStatus() == AfConstants.APPROVAL_STATUS_START || instance.getStatus() == AfConstants.APPROVAL_STATUS_SUCCESS) {
                    status = instance.getStatus();
                    break;
                } else {
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
     * 当前节点完工申请是否成功
     * @param projectNo 项目编号
     * @param scheduleSort 排期编号
     * @return 完工申请是否成功
     */
    private boolean scheduleCompleteApplicationSuccess(String projectNo, Integer scheduleSort) {
        String configAlias;
        if (scheduleSort == -1) {
            configAlias = AFAlias.START_REPORT.alias;
        } else {
            configAlias = AFAlias.COMPLETE_APPLICATION.alias;
        }
        AfConfig config = configService.findByAlias(configAlias);
        if (config == null) {
            // TODO
        }
        List<AfInstance> instances = findByConfigNoAndProjectNoAndScheduleSort(config.getConfigNo(), projectNo, scheduleSort);
        boolean success = false;
        if (instances != null) {
            for (AfInstance instance : instances) {
                if (instance.getStatus() == 2) {
                    success = true;
                    break;
                }
            }
        }
        return success;
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

    private AfConfigPlanVO createConfigPlanVO(String planNo, String configName, String configNo) {
        AfConfigPlanVO configPlanVO = new AfConfigPlanVO();
        configPlanVO.setPlanNo(planNo);
        configPlanVO.setConfigName(configName);
        configPlanVO.setConfigName(configNo);
        return configPlanVO;
    }

    private void checkApplication(List<AfConfigPlanVO> configPlanVOs, String companyNo, String roleId, String projectNo, Integer scheduleSort) {
        AfConfig config = configService.findByAlias(AFAlias.CHECK_APPLICATION.alias);
        AfPlan plan = planService.findByConfigNoAndCompanyNoAndRoleId(config.getConfigNo(), companyNo, roleId);
        if (plan != null) {
            // 当前用户为发起用户
            AfConfigPlanVO configPlanVO = createConfigPlanVO(plan.getPlanNo(), config.getName(), config.getConfigNo());
            configPlanVOs.add(configPlanVO);
        }
        checkReport(configPlanVOs, companyNo, roleId, projectNo, scheduleSort);
    }

    private void checkReport(List<AfConfigPlanVO> configPlanVOs, String companyNo, String roleId, String projectNo, Integer scheduleSort) {
        int miss = compareInstanceSuccessCount(AFAlias.CHECK_APPLICATION.alias, AFAlias.CHECK_REPORT.alias, projectNo, scheduleSort);
        if (miss > 0) {
            AfConfig config = configService.findByAlias(AFAlias.CHECK_REPORT.alias);
            AfPlan plan = planService.findByConfigNoAndCompanyNoAndRoleId(config.getConfigNo(), companyNo, roleId);
            if (plan != null) {
                // 当前用户为发起用户
                AfConfigPlanVO configPlanVO = createConfigPlanVO(plan.getPlanNo(), config.getName(), config.getConfigNo());
                configPlanVOs.add(configPlanVO);
            }
        }
    }

    private int compareInstanceSuccessCount(String aAlias, String bAlias, String projectNo, Integer scheduleSort){
        int aCount = 0, bCount = 0;

        AfConfig aConfig = configService.findByAlias(aAlias);
        List<AfInstance> aInstances = findByConfigNoAndProjectNoAndScheduleSort(aConfig.getConfigNo(), projectNo, scheduleSort);
        if (aInstances != null) {
            for (AfInstance instance : aInstances) {
                if (instance.getStatus() == AfConstants.APPROVAL_STATUS_SUCCESS) {
                    aCount++;
                }
            }
        }

        AfConfig bConfig = configService.findByAlias(bAlias);
        List<AfInstance> bInstances = findByConfigNoAndProjectNoAndScheduleSort(bConfig.getConfigNo(), projectNo, scheduleSort);
        if (bInstances != null) {
            for (AfInstance instance : bInstances) {
                if (instance.getStatus() == AfConstants.APPROVAL_STATUS_SUCCESS || instance.getStatus() == AfConstants.APPROVAL_STATUS_START) {
                    bCount++;
                }
            }
        }
        return aCount - bCount;
    }



    private void problemRectification(List<AfConfigPlanVO> configPlanVOs, String companyNo, String roleId, String projectNo, Integer scheduleSort) {
        AfConfig config = configService.findByAlias(AFAlias.PROBLEM_RECTIFICATION.alias);
        AfPlan plan = planService.findByConfigNoAndCompanyNoAndRoleId(config.getConfigNo(), companyNo, roleId);
        if (plan != null) {
            // 当前用户为发起用户
            AfConfigPlanVO configPlanVO = createConfigPlanVO(plan.getPlanNo(), config.getName(), config.getConfigNo());
            configPlanVOs.add(configPlanVO);
        }
    }

    private void changeOrder(List<AfConfigPlanVO> configPlanVOs, String companyNo, String roleId, String projectNo, Integer scheduleSort) {
        AfConfig config = configService.findByAlias(AFAlias.CHANGE_ORDER.alias);
        AfPlan plan = planService.findByConfigNoAndCompanyNoAndRoleId(config.getConfigNo(), companyNo, roleId);
        if (plan != null) {
            // 当前用户为发起用户
            AfConfigPlanVO configPlanVO = createConfigPlanVO(plan.getPlanNo(), config.getName(), config.getConfigNo());
            configPlanVOs.add(configPlanVO);
        }
    }

    private void delayOrder(List<AfConfigPlanVO> configPlanVOs, String companyNo, String roleId, String projectNo, Integer scheduleSort) {
        AfConfig config = configService.findByAlias(AFAlias.DELAY_ORDER.alias);
        AfPlan plan = planService.findByConfigNoAndCompanyNoAndRoleId(config.getConfigNo(), companyNo, roleId);
        if (plan != null) {
            // 当前用户为发起用户
            AfConfigPlanVO configPlanVO = createConfigPlanVO(plan.getPlanNo(), config.getName(), config.getConfigNo());
            configPlanVOs.add(configPlanVO);
        }
    }
    private String getCompanyNo(){
        // TODO
        return "";
    }

    private List<AfInstanceVO> getInstances(String userId, String projectNo, Integer scheduleSort) {
        List<AfInstanceVO> instanceVOs = new ArrayList<>();

        List<AfInstance> instances = findByProjectNoAndScheduleSort(projectNo, scheduleSort);
        if (instances != null) {
            for (AfInstance instance : instances) {
                AfInstanceVO instanceVO = new AfInstanceVO();
                AfConfig config = configService.findByConfigNo(instance.getConfigNo());
                if (config == null) {
                    // TODO
                }
                AfApprovalLog approvalLog = approvalLogService.findByInstanceNoAndSort(instance.getInstanceNo(), 1);
                if (approvalLog == null) {
                    // TODO
                }
                UserRoleSet role = roleService.findById(approvalLog.getRoleId());
                if (role == null) {
                    // TODO
                }
                String username = getUserNameByUserIdAndRoleId(approvalLog.getUserId(), role.getRoleCode());

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
                instanceVO.setCreateUsername(username);
                instanceVO.setScheduleSort(instance.getScheduleSort());
                instanceVO.setRemark(approvalLog.getRemark());
                instanceVO.setRoleId(role.getRoleCode());
                instanceVO.setRoleName(role.getRoleName());
            }
        }
        return instanceVOs;
    }

    private List<AfInstance> findByProjectNoAndScheduleSort(String projectNo, Integer scheduleSort) {
        AfInstanceExample example = new AfInstanceExample();
        example.createCriteria().andProjectNoEqualTo(projectNo).andScheduleSortEqualTo(scheduleSort);
        example.setOrderByClause("create_time desc");
        return instanceMapper.selectByExample(example);
    }
}
