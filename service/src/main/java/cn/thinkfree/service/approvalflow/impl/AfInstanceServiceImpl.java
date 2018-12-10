package cn.thinkfree.service.approvalflow.impl;

import cn.thinkfree.core.constants.AfConfigs;
import cn.thinkfree.core.constants.AfConstants;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.core.constants.Role;
import cn.thinkfree.core.exception.CommonException;
import cn.thinkfree.core.utils.JSONUtil;
import cn.thinkfree.core.utils.ThreadManager;
import cn.thinkfree.core.utils.UniqueCodeGenerator;
import cn.thinkfree.database.mapper.AfInstanceMapper;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.vo.*;
import cn.thinkfree.service.approvalflow.*;
import cn.thinkfree.service.config.HttpLinks;
import cn.thinkfree.service.construction.ConstructionAndPayStateService;
import cn.thinkfree.service.construction.ConstructionStateService;
import cn.thinkfree.service.neworder.NewOrderService;
import cn.thinkfree.service.neworder.NewOrderUserService;
import cn.thinkfree.service.newscheduling.NewSchedulingBaseService;
import cn.thinkfree.service.newscheduling.NewSchedulingService;
import cn.thinkfree.service.platform.employee.EmployeeService;
import cn.thinkfree.service.platform.vo.EmployeeMsgVo;
import cn.thinkfree.service.project.ProjectService;
import cn.thinkfree.service.utils.AfUtils;
import cn.thinkfree.service.utils.DateUtil;
import cn.thinkfree.service.utils.HttpUtils;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
    @Autowired
    private AfInstancePdfUrlService instancePdfUrlService;
    @Autowired
    private ConstructionStateService constructionStateService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private ConstructionAndPayStateService constructionAndPayStateService;
    @Autowired
    private NewSchedulingBaseService schedulingBaseService;
    @Autowired
    private AfInstanceRelevanceService instanceRelevancyService;

    @Override
    public AfInstanceDetailVO start(String projectNo, String userId, String configNo, Integer scheduleSort) {
//        if (!verifyStartApproval(projectNo, configNo, scheduleSort)) {
//            LOGGER.error("无法发起审批");
//            throw new RuntimeException();
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
            AfUserDTO userDTO = getUserInfo(approvalLogVO.getUserId(), approvalLogVO.getRoleId());
            approvalLogVO.setUserName(userDTO.getUsername());
            approvalLogVO.setHeadPortrait(userDTO.getHeadPortrait());
            approvalLogVOs.add(approvalLogVO);
        }

        if (AfConfigs.CHECK_APPLICATION.configNo.equals(configNo)) {
//            List<AfCheckItemVO> checkItems = getCheckApplicationCheckItems(projectNo, scheduleSort);
            // TODO
            List<AfCheckItemVO> checkItems = new ArrayList<>();
            AfCheckItemVO checkItemVO = new AfCheckItemVO();
            checkItemVO.setType(1);
            checkItemVO.setName("阶段验收");
            checkItems.add(checkItemVO);

            checkItemVO = new AfCheckItemVO();
            checkItemVO.setType(2);
            checkItemVO.setName("避水验收");
            checkItems.add(checkItemVO);

            instanceDetailVO.setCheckItems(checkItems);
        } else if (AfConfigs.CHECK_REPORT.configNo.equals(configNo)){
//            List<AfCheckItemVO> checkItems = getCheckReportCheckItems(projectNo, scheduleSort);
            // TODO
            List<AfCheckItemVO> checkItems = new ArrayList<>();
            AfCheckItemVO checkItemVO = new AfCheckItemVO();
            checkItemVO.setType(1);
            checkItemVO.setName("阶段验收");
            checkItems.add(checkItemVO);

            checkItemVO = new AfCheckItemVO();
            checkItemVO.setType(2);
            checkItemVO.setName("避水验收");
            checkItems.add(checkItemVO);
            instanceDetailVO.setCheckItems(checkItems);
        } else if (AfConfigs.CHANGE_COMPLETE.configNo.equals(configNo)){
            AfInstance instance = getRelevanceChangeOrderInstance(projectNo);

            if (instance == null) {
                LOGGER.error("未查询到已完成的{}, projectNo:{}", AfConfigs.CHANGE_ORDER.name, projectNo);
                throw new RuntimeException();
            }
            instanceDetailVO.setRelevancyDate(instance.getData());
        }
        String customerId = project.getOwnerId();
        AfUserDTO customerInfo = getUserInfo(customerId, Role.CC.id);
        AfConfig config = configService.findByNo(configNo);
        instanceDetailVO.setConfigNo(configNo);
        instanceDetailVO.setConfigName(config.getName());
        instanceDetailVO.setProjectNo(projectNo);
        instanceDetailVO.setCustomerId(customerId);
        instanceDetailVO.setCustomerName(customerInfo.getUsername());
        instanceDetailVO.setApprovalLogs(approvalLogVOs);
        instanceDetailVO.setAddress(project.getAddressDetail());
        return instanceDetailVO;
    }

    /**
     * 获取验收申请验收项
     * @param projectNo 项目编号
     * @param scheduleSort 排期编号
     * @return 验收项
     */
    private List<AfCheckItemVO> getCheckApplicationCheckItems(String projectNo, Integer scheduleSort) {
        List<AfCheckItemVO> checkItems = getCheckItems(projectNo, scheduleSort);
        if (checkItems.size() > 1) {
            List<AfCheckItemVO> checkApplicationCheckItems = new ArrayList<>();
            List<AfInstance> checkApplicationInstances = findByConfigNoAndProjectNoAndScheduleSortAndStatus(AfConfigs.CHECK_APPLICATION.configNo, projectNo, scheduleSort, AfConstants.APPROVAL_STATUS_SUCCESS);
            if (checkApplicationInstances != null) {
                for (AfInstance checkApplicationInstance : checkApplicationInstances) {
                    AfCheckItemVO checkItem =  getCheckItem(checkApplicationInstance.getData());
                    checkApplicationCheckItems.add(checkItem);
                }
            }
            if (checkApplicationCheckItems.size() > 0) {
                for (AfCheckItemVO checkApplicationCheckItem : checkApplicationCheckItems) {
                    for (AfCheckItemVO checkItemVO : checkItems) {
                        if (checkItemVO.getType().equals(checkApplicationCheckItem.getType())) {
                            checkItems.remove(checkItemVO);
                            break;
                        }
                    }
                }
            }
        }
        return checkItems;
    }

    /**
     * 获取验收报告验收项
     * @param projectNo 项目编号
     * @param scheduleSort 排期编号
     * @return 验收项
     */
    private List<AfCheckItemVO> getCheckReportCheckItems(String projectNo, Integer scheduleSort) {
        List<AfCheckItemVO> checkItems = getCheckItems(projectNo, scheduleSort);
        if (checkItems.size() > 1) {
            List<AfCheckItemVO> checkApplicationCheckItems = new ArrayList<>();
            List<AfInstance> checkApplicationInstances = findByConfigNoAndProjectNoAndScheduleSortAndStatus(AfConfigs.CHECK_APPLICATION.configNo, projectNo, scheduleSort, AfConstants.APPROVAL_STATUS_SUCCESS);
            if (checkApplicationInstances != null) {
                for (AfInstance checkApplicationInstance : checkApplicationInstances) {
                    AfCheckItemVO checkItem =  getCheckItem(checkApplicationInstance.getData());
                    checkApplicationCheckItems.add(checkItem);
                }
            }
            if (checkApplicationCheckItems.size() > 1) {
                List<AfCheckItemVO> checkReportCheckItems = new ArrayList<>();
                List<AfInstance> checkReportInstances = findByConfigNoAndProjectNoAndScheduleSortAndStatus(AfConfigs.CHECK_REPORT.configNo, projectNo, scheduleSort, AfConstants.APPROVAL_STATUS_SUCCESS);
                if (checkReportInstances != null) {
                    for (AfInstance checkReportInstance : checkReportInstances) {
                        AfCheckItemVO checkItem =  getCheckItem(checkReportInstance.getData());
                        checkReportCheckItems.add(checkItem);
                    }
                }
                if (checkReportCheckItems.size() > 0) {
                    for (AfCheckItemVO checkReportCheckItem : checkReportCheckItems) {
                        for (AfCheckItemVO checkApplicationCheckItem : checkApplicationCheckItems) {
                            if (checkApplicationCheckItem.getType().equals(checkReportCheckItem.getType())) {
                                checkApplicationCheckItems.remove(checkApplicationCheckItem);
                                break;
                            }
                        }
                    }
                }
            }
            checkItems = checkApplicationCheckItems;
        }
        return checkItems;
    }

    private AfCheckItemVO getCheckItem(String data) {
        return JSONUtil.json2Bean(data, AfCheckItemVO.class);
    }

    /**
     * 获取要关联的变更单
     * @param projectNo 项目编号
     * @return 变更单实例
     */
    private AfInstance getRelevanceChangeOrderInstance(String projectNo) {
        return findByConfigNoAndProjectNoAndStatus(AfConfigs.CHANGE_ORDER.configNo, projectNo, AfConstants.APPROVAL_STATUS_SUCCESS);
    }

    private AfInstance findByConfigNoAndProjectNoAndStatus(String configNo, String projectNo, Integer status) {
        PageHelper.startPage(1, 1);
        AfInstanceExample example = new AfInstanceExample();
        // TODO status
//        example.createCriteria().andConfigNoEqualTo(configNo).andProjectNoEqualTo(projectNo).andStatusEqualTo(status);
        example.createCriteria().andConfigNoEqualTo(configNo).andProjectNoEqualTo(projectNo);
        example.setOrderByClause("create_time desc");
        List<AfInstance> instances = instanceMapper.selectByExample(example);
        return instances != null && instances.size() > 0 ? instances.get(0) : null;
    }

    private List<AfCheckItemVO> getCheckItems(String projectNo, Integer scheduleSort) {
        List<AfCheckItemVO> checkItems = new ArrayList<>(2);
        ConstructionOrder constructionOrder = orderService.getConstructionOrder(projectNo);
        if (constructionOrder == null) {
            LOGGER.error("未查询到订单信息，projectNo:{}", projectNo);
            throw new RuntimeException();
        }
        String schemeNo = constructionOrder.getSchemeNo();
        if (StringUtils.isEmpty(schemeNo)) {
            LOGGER.error("订单未配置方案信息，constructionOrderNo:{}", constructionOrder.getOrderNo());
            throw new RuntimeException();
        }
        ProjectBigScheduling projectBigScheduling = schedulingBaseService.findBySchemeNoAndSort(schemeNo, scheduleSort);
        if (projectBigScheduling == null) {
            LOGGER.error("未查询到排期信息，schemeNo:{}, scheduleSort:{}", schemeNo, scheduleSort);
            throw new RuntimeException();
        }
        if (projectBigScheduling.getIsNeedCheck() == 0) {
            LOGGER.error("该项目当前排期不需要验收，projectNo:{},scheduleSort:{}", projectNo, scheduleSort);
            throw new RuntimeException();
        }
        AfCheckItemVO checkItemVO = new AfCheckItemVO();
        checkItemVO.setType(1);
        checkItemVO.setName(projectBigScheduling.getRename());
        checkItems.add(checkItemVO);
        if (projectBigScheduling.getIsWaterTest() == 1) {
            checkItemVO = new AfCheckItemVO();
            checkItemVO.setType(2);
            checkItemVO.setName("避水验收");
            checkItems.add(checkItemVO);
        }
        return checkItems;
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

    @Override
    public void submitStart(String projectNo, String userId, String configNo, Integer scheduleSort, String data, String remark) {
//        if (!verifyStartApproval(projectNo, configNo, scheduleSort)) {
//            LOGGER.error("无法发起审批");
//            throw new RuntimeException();
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
        if (AfConfigs.CHANGE_ORDER.configNo.equals(configNo)) {
            // 变更单校验变更数据
            verifyChangeOrderData(data);
        } else if (AfConfigs.CHANGE_COMPLETE.configNo.equals(configNo)) {
            AfInstance instance = findByConfigNoAndProjectNoAndStatus(AfConfigs.CHANGE_ORDER.configNo, projectNo, AfConstants.APPROVAL_STATUS_SUCCESS);
            if (instance == null) {
                LOGGER.error("未查询到已完成的{}, projectNo:{}", AfConfigs.CHANGE_ORDER.name, projectNo);
                throw new RuntimeException();
            }
            instanceRelevancyService.create(instance.getInstanceNo(), instanceNo, instance.getData());
        }
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

    private void verifyChangeOrderData(String data) {
        AfChangeOrderVO changeOrderVO = JSONUtil.json2Bean(data, AfChangeOrderVO.class);
        if (changeOrderVO == null) {
            LOGGER.error("变更单没有输入变更费用，data:{}", data);
            throw new RuntimeException();
        }
        if (StringUtils.isBlank(changeOrderVO.getChangeCause())) {
            LOGGER.error("变更单没有输入变更原因，data:{}", data);
            throw new RuntimeException();
        }
        List<AfChangeOrder> changeOrders = changeOrderVO.getChangeOrders();
        if (changeOrders != null) {
            for (AfChangeOrder changeOrder : changeOrders) {
                Integer changeType = changeOrder.getChangeType();
                if (changeType == null) {
                    LOGGER.error("变更类型（增减项）为空，data:{}", data);
                    throw new RuntimeException();
                }
                if (changeType != -1 && changeType != 1) {
                    LOGGER.error("错误的变更类型（增减项），changeType：{}", changeType);
                    throw new RuntimeException();
                }
                if (StringUtils.isBlank(changeOrder.getConstructionName())) {
                    LOGGER.error("变更单没有输入施工项名称，data:{}", data);
                    throw new RuntimeException();
                }
                if (StringUtils.isBlank(changeOrder.getConstructionNo())) {
                    LOGGER.error("变更单没有输入施工项编号，data:{}", data);
                    throw new RuntimeException();
                }
                Integer count = changeOrder.getCount();
                if (count == null || count <= 0) {
                    LOGGER.error("变更单没有输入变更数量，data:{}", data);
                    throw new RuntimeException();
                }
                verifyChangeOrderAmount(changeOrder.getUnitPrice());
            }
        }
        List<AfOtherChange> otherChanges = changeOrderVO.getOtherChanges();
        if (otherChanges != null) {
            for (AfOtherChange otherChange : otherChanges) {
                verifyChangeOrderAmount(otherChange.getAmount());
                Integer changeType = otherChange.getChangeType();
                if (changeType == null) {
                    LOGGER.error("变更类型（增减项）为空，data:{}", data);
                    throw new RuntimeException();
                }
                if (changeType != -1 && changeType != 1) {
                    LOGGER.error("错误的变更类型（增减项），changeType：{}", changeType);
                    throw new RuntimeException();
                }
                if (StringUtils.isBlank(otherChange.getExpenseName())) {
                    LOGGER.error("变更单没有输入费用名称，data:{}", data);
                    throw new RuntimeException();
                }
                if (StringUtils.isBlank(otherChange.getDescribe())) {
                    LOGGER.error("变更单没有输入费用说明，data:{}", data);
                    throw new RuntimeException();
                }
            }
        }
        if ((changeOrders == null || changeOrders.isEmpty()) && (otherChanges == null || otherChanges.isEmpty())) {
            LOGGER.error("变更单没有输入变更信息，data:{}", data);
            throw new RuntimeException();
        }

    }

    private void verifyChangeOrderAmount(String amount) {
        if (amount == null) {
            LOGGER.error("变更单没有输入费用");
            throw new RuntimeException();
        }
        int index = amount.indexOf(".");
        if (index > 0 && amount.length() - index > 3) {
            LOGGER.error("变更单费用格式错误，amount:{}", amount);
            throw new RuntimeException();
        }
        try {
            new BigDecimal(amount);
        } catch (NumberFormatException e) {
            LOGGER.error("变更单费用格式错误，amount:{}", amount);
            throw new RuntimeException();
        }
    }

    private boolean verifyStartApproval(String projectNo, String configNo, Integer scheduleSort) {
        List<ProjectBigSchedulingDetailsVO> schedulingDetailsVOs = schedulingService.getScheduling(projectNo).getData();
        if (schedulingDetailsVOs == null || schedulingDetailsVOs.isEmpty()) {
            LOGGER.error("未查询到正确的排期信息，projectNo:{}", projectNo);
            throw new RuntimeException();
        }
        schedulingDetailsVOs.sort(Comparator.comparing(ProjectBigSchedulingDetailsVO::getBigSort));
        if (AfConfigs.START_APPLICATION.configNo.equals(configNo)) {
            int verify = verifyStartApplicationAndStartReport(schedulingDetailsVOs, projectNo, configNo);
            return verify == 1 || verify == 3;
        } else if (AfConfigs.START_REPORT.configNo.equals(configNo)) {
            int verify = verifyStartApplicationAndStartReport(schedulingDetailsVOs, projectNo, configNo);
            return verify == 2 || verify == 3;
        } else if (AfConfigs.CHECK_APPLICATION.configNo.equals(configNo)) {
            int verify = verifyCheckAndComplete(schedulingDetailsVOs, projectNo, scheduleSort, configNo);
            return verify == 1 || verify == 3 || verify == 5 || verify == 7;
        } else if (AfConfigs.CHECK_REPORT.configNo.equals(configNo)) {
            int verify = verifyCheckAndComplete(schedulingDetailsVOs, projectNo, scheduleSort, configNo);
            return verify == 2 || verify == 3 || verify == 6 || verify == 7;
        } else if (AfConfigs.PROBLEM_RECTIFICATION.configNo.equals(configNo)) {
            int verify = verifyProjectRectificationAndRectificationComplete(schedulingDetailsVOs, projectNo, configNo);
            return verify == 1 || verify == 3;
        } else if (AfConfigs.RECTIFICATION_COMPLETE.configNo.equals(configNo)) {
            int verify = verifyProjectRectificationAndRectificationComplete(schedulingDetailsVOs, projectNo, configNo);
            return verify == 2 || verify == 3;
        } else if (AfConfigs.CHANGE_ORDER.configNo.equals(configNo)) {
            int verify = verifyChangeOrderAndChangeComplete(schedulingDetailsVOs, projectNo, configNo);
            return verify == 1 || verify == 3;
        } else if (AfConfigs.CHANGE_COMPLETE.configNo.equals(configNo)) {
            int verify = verifyChangeOrderAndChangeComplete(schedulingDetailsVOs, projectNo, configNo);
            return verify == 2 || verify == 3;
        } else if (AfConfigs.DELAY_ORDER.configNo.equals(configNo)) {
            int verify = verifyDelay(schedulingDetailsVOs, projectNo, configNo);
            return verify == 1;
        } else if (AfConfigs.COMPLETE_APPLICATION.configNo.equals(configNo)) {
            int verify = verifyCheckAndComplete(schedulingDetailsVOs, projectNo, scheduleSort, configNo);
            return verify == 4 || verify == 5 || verify == 6 || verify == 7;
        }
        return false;
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
        AfUserDTO customer = getUserInfo(customerId, Role.CC.id);
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
            AfUserDTO userDTO = getUserInfo(approvalLog.getUserId(), approvalLog.getRoleId());
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

        if (AfConfigs.CHANGE_COMPLETE.configNo.equals(instance.getConfigNo())) {
            AfInstanceRelevance instanceRelevance = instanceRelevancyService.findByRelevanceInstanceNo(instance.getInstanceNo());
            instanceDetailVO.setRelevancyDate(instanceRelevance.getData());
        }

        AfConfig config = configService.findByNo(instance.getConfigNo());
        if (config == null) {
            LOGGER.error("未查询到审批流配置信息，configNo:{}", instance.getConfigNo());
            throw new RuntimeException();
        }
        instanceDetailVO.setInstanceNo(instanceNo);
        instanceDetailVO.setAddress(project.getAddressDetail());
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
    public Integer approval(String instanceNo, String userId, Integer option, String remark) {

        Integer instanceStatus;

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
                instanceStatus = AfConstants.APPROVAL_STATUS_SUCCESS;
            } else {
                instance.setCurrentApprovalLogNo(nextApprovalLog.getApprovalNo());
                sendMessageToNext(instance.getProjectNo(), userId, nextApprovalLog.getUserId());
                instanceStatus = AfConstants.APPROVAL_STATUS_START;
            }
        } else {
            // 不同意
            approvalLog.setOption(option);
            instance.setStatus(AfConstants.APPROVAL_STATUS_FAIL);
            instance.setCurrentApprovalLogNo(null);
            instanceStatus = AfConstants.APPROVAL_STATUS_FAIL;
        }

        updateByPrimaryKey(instance);
        approvalLogService.updateByPrimaryKey(approvalLog);
        return instanceStatus;
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
        requestMsg.put("userNo", JSONUtil.bean2JsonStr(subUserIds));
        requestMsg.put("senderId", sendUserId);
        requestMsg.put("content", content);
        requestMsg.put("dynamicId", "0");
        requestMsg.put("type", "3");
        LOGGER.info("发送审批消息：requestMsg：{}", requestMsg);
        try {
            ThreadManager.getThreadPollProxy().execute(()->{
                HttpUtils.HttpRespMsg respMsg = HttpUtils.post(httpLinks.getMessageSave(), requestMsg);
                LOGGER.info("respMsg:{}", JSONUtil.bean2JsonStr(respMsg));
            });
        } catch (Exception e) {
            LOGGER.error("发送审批消息出错", e);
        }
    }

    /**
     * 发送审批成功消息
     * @param instance 审批流实例
     */
    private void executeSuccessAction(AfInstance instance) {
        if (AfConfigs.START_REPORT.configNo.equals(instance.getConfigNo())) {
            schedulingService.projectStart(instance.getProjectNo(), 1);
            constructionStateService.constructionPlan(instance.getProjectNo(), 0);
        } else if (AfConfigs.COMPLETE_APPLICATION.configNo.equals(instance.getConfigNo())) {
            schedulingService.completeBigScheduling(instance.getProjectNo(), instance.getScheduleSort());
            constructionStateService.constructionPlan(instance.getProjectNo(), instance.getScheduleSort());
        } else if (AfConfigs.CHANGE_COMPLETE.configNo.equals(instance.getConfigNo())) {
            // TODO 发送变更金额
            sendChangeMoney(instance.getProjectNo(), instance.getData());
        }

        createPdf(instance);
    }

    private void sendChangeMoney(String projectNo, String data) {
        ConstructionOrder constructionOrder = orderService.getConstructionOrder(projectNo);
        String orderNo = constructionOrder.getOrderNo();

        List<AfChangeOrderDTO> changeOrderDTOS = new ArrayList<>();

        AfChangeOrderVO changeOrderVO = JSONUtil.json2Bean(data, AfChangeOrderVO.class);
        List<AfChangeOrder> changeOrders = changeOrderVO.getChangeOrders();
        if (changeOrders != null) {
            BigDecimal amount;
            for (AfChangeOrder changeOrder : changeOrders) {
                BigDecimal unitPrice = new BigDecimal(changeOrder.getUnitPrice());
                BigDecimal count = new BigDecimal(changeOrder.getCount());
                BigDecimal changeType = new BigDecimal(changeOrder.getChangeType());
                amount = unitPrice.multiply(count).multiply(changeType);

                AfChangeOrderDTO changeOrderDTO = new AfChangeOrderDTO();
                changeOrderDTO.setOrderId(orderNo);
                changeOrderDTO.setFeeAmount(amount.setScale(2, RoundingMode.HALF_UP).toString());
                changeOrderDTO.setFeeName(changeOrder.getConstructionName());
                changeOrderDTO.setProjectNo(projectNo);

                changeOrderDTOS.add(changeOrderDTO);
            }
        }
        List<AfOtherChange> otherChanges = changeOrderVO.getOtherChanges();
        if (otherChanges != null) {
            BigDecimal amount;
            for (AfOtherChange otherChange : otherChanges) {
                amount = new BigDecimal(otherChange.getAmount()).multiply(new BigDecimal(otherChange.getChangeType()));

                AfChangeOrderDTO changeOrderDTO = new AfChangeOrderDTO();
                changeOrderDTO.setOrderId(orderNo);
                changeOrderDTO.setFeeAmount(amount.setScale(2, RoundingMode.HALF_UP).toString());
                changeOrderDTO.setFeeName(otherChange.getExpenseName());
                changeOrderDTO.setProjectNo(projectNo);
                changeOrderDTO.setRemark(otherChange.getDescribe());

                changeOrderDTOS.add(changeOrderDTO);
            }
        }
        String httpData = JSONUtil.bean2JsonStr(changeOrderDTOS);
        int code = AfUtils.postJson(httpLinks.getCreateFee(), httpData);
        if (code != ResultMessage.SUCCESS.code) {
            LOGGER.error("变更单变更金额失败， url:{}, data:{}", httpLinks.getCreateFee(), httpData);
            throw new RuntimeException();
        }
    }

    private void createPdf(AfInstance instance) {
        try {
            ThreadManager.getThreadPollProxy().execute(()-> {
                instancePdfUrlService.create(instance);
            });
        } catch (Exception e) {
            LOGGER.error("审批记录导出为PDF出错， instance:{}", instance.getInstanceNo());
        }
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

        List<ProjectBigSchedulingDetailsVO> schedulingDetailsVOs = null;
//        List<ProjectBigSchedulingDetailsVO> schedulingDetailsVOs = schedulingService.getScheduling(projectNo).getData();
//        if (schedulingDetailsVOs == null || schedulingDetailsVOs.isEmpty()) {
//            LOGGER.error("未查询到正确的排期信息，projectNo:{}", projectNo);
//            throw new RuntimeException();
//        }
//        schedulingDetailsVOs.sort(Comparator.comparing(ProjectBigSchedulingDetailsVO::getBigSort));
        if (AfConstants.APPROVAL_TYPE_SCHEDULE_APPROVAL.equals(approvalType)) {
            if (scheduleSort == null) {
                LOGGER.error("没有传入相应的排期编号！");
                throw new RuntimeException();
            }
            // 进度验收
            if (scheduleSort == 0) {
                // 开工准备:开工申请
                getInstances(instanceVOs, AfConfigs.START_APPLICATION.configNo, userId, projectNo);
                // 开工准备:开工报告
                getInstances(instanceVOs, AfConfigs.START_REPORT.configNo, userId, projectNo);
                // 获取开工准备阶段发起菜单
                getStartStartMenus(startMenus, schedulingDetailsVOs, projectNo, userId);
            } else {
                // 验收申请
                getInstances(instanceVOs, AfConfigs.CHECK_APPLICATION.configNo, userId, projectNo, scheduleSort);
                // 验收报告
                getInstances(instanceVOs, AfConfigs.CHECK_REPORT.configNo, userId, projectNo, scheduleSort);
                // 完工审批
                getInstances(instanceVOs, AfConfigs.COMPLETE_APPLICATION.configNo, userId, projectNo, scheduleSort);
                            // 获取验收、完工申请发起菜单
                getCheckAndCompleteStartMenus(startMenus, userId, projectNo, schedulingDetailsVOs, scheduleSort);
            }

        } else if (AfConstants.APPROVAL_TYPE_PROBLEM_RECTIFICATION.equals(approvalType)) {
            // 问题整改：问题整改
            getInstances(instanceVOs, AfConfigs.PROBLEM_RECTIFICATION.configNo, userId, projectNo);
            // 问题整改：整改完成
            getInstances(instanceVOs, AfConfigs.RECTIFICATION_COMPLETE.configNo, userId, projectNo);
            getProblemStartMenus(startMenus, schedulingDetailsVOs, userId, projectNo);
        } else if (AfConstants.APPROVAL_TYPE_CONSTRUCTION_CHANGE.equals(approvalType)) {
            // 施工变更：变更单
            getInstances(instanceVOs, AfConfigs.CHANGE_ORDER.configNo, userId, projectNo);
            // 施工变更：变更完成
            getInstances(instanceVOs, AfConfigs.CHANGE_COMPLETE.configNo, userId, projectNo);
            getChangeStartMenus(startMenus, schedulingDetailsVOs, userId, projectNo);
        } else if (AfConstants.APPROVAL_TYPE_DELAY_VERIFY.equals(approvalType)) {
            // 延期确认
            getInstances(instanceVOs, AfConfigs.DELAY_ORDER.configNo, userId, projectNo);
            getDelayStartMenus(startMenus, schedulingDetailsVOs, userId, projectNo);
        }

        instanceVOs.sort(Comparator.comparing(AfInstanceVO::getCreateTime).reversed());
        instanceListVO.setInstances(instanceVOs);
        instanceListVO.setStartMenus(startMenus);
        return instanceListVO;
    }

    private int verifyStartApplicationAndStartReport(List<ProjectBigSchedulingDetailsVO> schedulingDetailsVOs, String projectNo, String configNo) {
        int result = 0;
        if (!projectCompleted(schedulingDetailsVOs, projectNo)) {
            long startApplicationStartCount = getCount(projectNo, AfConfigs.START_APPLICATION.configNo, AfConstants.APPROVAL_STATUS_START);
            long startApplicationSuccessCount = getSuccessCount(projectNo, AfConfigs.START_APPLICATION.configNo);
            if (startApplicationStartCount == 0 && startApplicationSuccessCount == 0) {
                result += 1;
                if (configNo != null && configNo.equals(AfConfigs.START_APPLICATION.configNo)) {
                    return result;
                }
            } else if (startApplicationSuccessCount > 0){
                long startReportStartAndSuccessCount = getStartAndSuccessCount(projectNo, AfConfigs.START_REPORT.configNo);
                if (startReportStartAndSuccessCount == 0) {
                    if (constructionAndPayStateService.isBeComplete(projectNo, 0)) {
                        result += 2;
                    }
                }
            }
        }
        return result;
    }

    private int verifyCheckAndComplete(List<ProjectBigSchedulingDetailsVO> schedulingDetailsVOs, String projectNo, Integer scheduleSort, String configNo) {
        int result = 0;
        if (!projectCompleted(schedulingDetailsVOs, projectNo)) {
            if (getPreScheduleSortSuccceed(projectNo, schedulingDetailsVOs, scheduleSort)) {
                if (!getScheduleSortSucceed(projectNo, scheduleSort)) {

                    long checkApplicationCount = getSuccessCount(projectNo, AfConfigs.CHECK_APPLICATION.configNo, scheduleSort);
                    long checkReportCount = getStartAndSuccessCount(projectNo, AfConfigs.CHECK_REPORT.configNo, scheduleSort);


                    boolean needCheck = isNeedCheck(schedulingDetailsVOs, scheduleSort);
                    if (needCheck) {
                        List<AfCheckItemVO> checkItems = getCheckItems(projectNo, scheduleSort);
                        if (checkItems.size() > checkApplicationCount) {
                            result += 1;
                        }
                        if (configNo != null && configNo.equals(AfConfigs.CHECK_APPLICATION.configNo)) {
                            return result;
                        }
                        if (checkApplicationCount > checkReportCount && checkItems.size() > checkReportCount) {
                            // 如果验收申请数量大于验收报告数量，发起验收报告菜单
                            result += 2;
                            if (configNo != null && configNo.equals(AfConfigs.CHECK_REPORT.configNo)) {
                                return result;
                            }
                        }
                    }
                    if (constructionAndPayStateService.isBeComplete(projectNo, scheduleSort)) {
                        if (checkApplicationCount == checkReportCount) {
                            long checkApplicationStartCount = getCount(projectNo, scheduleSort, AfConfigs.CHECK_APPLICATION.configNo, AfConstants.APPROVAL_STATUS_START);
                            long checkReportStartCount = getCount(projectNo, scheduleSort, AfConfigs.CHECK_REPORT.configNo, AfConstants.APPROVAL_STATUS_START);
                            if (checkApplicationStartCount == 0 && checkReportStartCount == 0) {
                                if (needCheck && checkApplicationCount == 0) {
                                    return result;
                                }
                                if (getLargestScheduleSort(schedulingDetailsVOs).equals(scheduleSort)) {
                                    // 当前阶段为最后一个阶段
                                    if (getCount(projectNo, AfConstants.APPROVAL_STATUS_START) == 0
                                            && countEqual(projectNo, AfConfigs.PROBLEM_RECTIFICATION.configNo, AfConfigs.RECTIFICATION_COMPLETE.configNo, AfConstants.APPROVAL_STATUS_SUCCESS)
                                            && countEqual(projectNo, AfConfigs.CHANGE_ORDER.configNo, AfConfigs.CHANGE_COMPLETE.configNo, AfConstants.APPROVAL_STATUS_SUCCESS)) {
                                        result += 4;
                                    }
                                } else {
                                    // 当前节点不存在未完成的验收申请与验收报告，且验收申请与验收报告数量相等，发起完成申请菜单
                                    result += 4;
                                }
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    private int verifyProjectRectificationAndRectificationComplete(List<ProjectBigSchedulingDetailsVO> schedulingDetailsVOs, String projectNo, String configNo) {
        int result = 0;
        if (!projectCompleted(schedulingDetailsVOs, projectNo)) {
            if (startReportSuccess(projectNo)) {
                result += 1;
                if (configNo != null && configNo.equals(AfConfigs.PROBLEM_RECTIFICATION.configNo)) {
                    return result;
                }
                long problemRectificationSuccessCount = getSuccessCount(projectNo, AfConfigs.PROBLEM_RECTIFICATION.configNo);
                long rectificationCompleteStartAndSuccessCount = getStartAndSuccessCount(projectNo, AfConfigs.RECTIFICATION_COMPLETE.configNo);
                if (problemRectificationSuccessCount > rectificationCompleteStartAndSuccessCount) {
                    result += 2;
                }
            }
        }

        return result;
    }

    private int verifyChangeOrderAndChangeComplete(List<ProjectBigSchedulingDetailsVO> schedulingDetailsVOs, String projectNo, String configNo) {
        int result = 0;
        if (!projectCompleted(schedulingDetailsVOs, projectNo)) {
            if (startReportSuccess(projectNo)) {
                if (getCount(projectNo, AfConfigs.CHANGE_ORDER.configNo, AfConstants.APPROVAL_STATUS_START) == 0
                        && getCount(projectNo, AfConfigs.CHANGE_COMPLETE.configNo, AfConstants.APPROVAL_STATUS_START) == 0) {
                    long changeOrderSuccessCount = getCount(projectNo, AfConfigs.CHANGE_ORDER.configNo, AfConstants.APPROVAL_STATUS_SUCCESS);
                    long changeCompleteSuccessCount = getCount(projectNo, AfConfigs.CHANGE_COMPLETE.configNo, AfConstants.APPROVAL_STATUS_SUCCESS);
                    if (changeOrderSuccessCount > changeCompleteSuccessCount) {
                        result += 2;
                    } else {
                        result += 1;
                    }
                }
            }
        }

        return result;
    }

    private int verifyDelay(List<ProjectBigSchedulingDetailsVO> schedulingDetailsVOs, String projectNo, String configNo) {
        int result = 0;
        if (!projectCompleted(schedulingDetailsVOs, projectNo)) {
            if (startReportSuccess(projectNo)) {
                result += 1;
            }
        }
        return result;
    }

    private boolean projectCompleted(List<ProjectBigSchedulingDetailsVO> schedulingDetailsVOs, String projectNo) {
        Integer lastScheduleSort = getLargestScheduleSort(schedulingDetailsVOs);
        long startAndSuccessCount = getStartAndSuccessCount(projectNo, AfConfigs.COMPLETE_APPLICATION.configNo, lastScheduleSort);
        return startAndSuccessCount > 0;
    }

    private Integer getLargestScheduleSort(List<ProjectBigSchedulingDetailsVO> schedulingDetailsVOs) {
        Integer largestScheduleSort = 0;

        if (schedulingDetailsVOs != null) {
            for (ProjectBigSchedulingDetailsVO schedulingDetailsVO : schedulingDetailsVOs) {
                if (schedulingDetailsVO.getBigSort() > largestScheduleSort) {
                    largestScheduleSort = schedulingDetailsVO.getBigSort();
                }
            }
        }

        return largestScheduleSort;
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
    private boolean getPreScheduleSortSuccceed(String projectNo, List<ProjectBigSchedulingDetailsVO> schedulingDetailsVOs, Integer scheduleSort) {
        Integer preScheduleSort = getPreScheduleSort(schedulingDetailsVOs, scheduleSort);
        return getScheduleSortSucceed(projectNo, preScheduleSort);
    }

    private boolean getScheduleSortSucceed(String projectNo, Integer scheduleSort) {
        long successCount;
        if (scheduleSort == null) {
            successCount = getSuccessCount(projectNo, AfConfigs.START_REPORT.configNo);
        } else {
            successCount = getSuccessCount(projectNo, AfConfigs.COMPLETE_APPLICATION.configNo, scheduleSort);

        }
        return successCount > 0;
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
     */
    private void getChangeStartMenus(List<AfStartMenuVO> startMenus, List<ProjectBigSchedulingDetailsVO> schedulingDetailsVOs, String userId, String projectNo) {
//        int result = verifyChangeOrderAndChangeComplete(schedulingDetailsVOs, projectNo, null);
//        if (result == 1 || result == 3) {
            addStartMenu(startMenus, projectNo, AfConfigs.CHANGE_ORDER.configNo, userId);
//        }
//        if (result == 2 || result == 3) {
            addStartMenu(startMenus, projectNo, AfConfigs.CHANGE_COMPLETE.configNo, userId);
//        }
    }

    /**
     * 获取发起菜单
     * @param startMenus 发起菜单
     * @param userId 用户编号
     * @param projectNo 项目编号
     */
    private void getProblemStartMenus(List<AfStartMenuVO> startMenus, List<ProjectBigSchedulingDetailsVO> schedulingDetailsVOs, String userId, String projectNo) {
        int result = verifyProjectRectificationAndRectificationComplete(schedulingDetailsVOs, projectNo, null);
        if (result == 1 || result == 3) {
            addStartMenu(startMenus, projectNo, AfConfigs.PROBLEM_RECTIFICATION.configNo, userId);
        }
        if (result == 2 || result == 3) {
            addStartMenu(startMenus, projectNo, AfConfigs.RECTIFICATION_COMPLETE.configNo, userId);
        }
    }

    private boolean startReportSuccess(String projectNo) {
        long successCount = getSuccessCount(projectNo, AfConfigs.START_REPORT.configNo);
        return successCount > 0;
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
        int result = verifyCheckAndComplete(schedulingDetailsVOs, projectNo, scheduleSort, null);
        if (result == 1 || result == 3 || result == 5 || result == 7) {
            addStartMenu(startMenus, projectNo, AfConfigs.CHECK_APPLICATION.configNo, userId);
        }
        if (result == 2 || result == 3 || result == 6 || result == 7) {
            addStartMenu(startMenus, projectNo, AfConfigs.CHECK_REPORT.configNo, userId);
        }
        if (result == 4 || result == 5 || result == 6 || result == 7) {
            addStartMenu(startMenus, projectNo, AfConfigs.COMPLETE_APPLICATION.configNo, userId);
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

    private long getCount(String projectNo, Integer scheduleSort, String configNo, int status) {
        AfInstanceExample example = new AfInstanceExample();
        example.createCriteria().andProjectNoEqualTo(projectNo).andScheduleSortEqualTo(scheduleSort).andConfigNoEqualTo(configNo).andStatusEqualTo(status);
        return instanceMapper.countByExample(example);
    }

    private long getSuccessCount(String projectNo, String configNo) {
        AfInstanceExample example = new AfInstanceExample();
        example.createCriteria().andProjectNoEqualTo(projectNo).andConfigNoEqualTo(configNo).andStatusEqualTo(AfConstants.APPROVAL_STATUS_SUCCESS);
        return instanceMapper.countByExample(example);
    }

    private long getStartAndSuccessCount(String projectNo, String configNo) {
        AfInstanceExample example = new AfInstanceExample();
        List<Integer> statuses = new ArrayList<>();
        statuses.add(AfConstants.APPROVAL_STATUS_START);
        statuses.add(AfConstants.APPROVAL_STATUS_SUCCESS);
        example.createCriteria().andProjectNoEqualTo(projectNo).andConfigNoEqualTo(configNo).andStatusIn(statuses);
        return instanceMapper.countByExample(example);
    }

    private long getSuccessCount(String projectNo, String configNo, Integer scheduleSort) {
        AfInstanceExample example = new AfInstanceExample();
        example.createCriteria().andProjectNoEqualTo(projectNo).andConfigNoEqualTo(configNo).andStatusEqualTo(AfConstants.APPROVAL_STATUS_SUCCESS).andScheduleSortEqualTo(scheduleSort);
        return instanceMapper.countByExample(example);
    }

    private long getStartAndSuccessCount(String projectNo, String configNo, Integer scheduleSort) {
        AfInstanceExample example = new AfInstanceExample();
        List<Integer> statuses = new ArrayList<>();
        statuses.add(AfConstants.APPROVAL_STATUS_START);
        statuses.add(AfConstants.APPROVAL_STATUS_SUCCESS);
        example.createCriteria().andProjectNoEqualTo(projectNo).andConfigNoEqualTo(configNo).andStatusIn(statuses).andScheduleSortEqualTo(scheduleSort);
        return instanceMapper.countByExample(example);
    }

    /**
     * 获取延期发起菜单
     * @param startMenus 发起菜单
     * @param userId 用户编号
     * @param projectNo 项目编号
     */
    private void getDelayStartMenus(List<AfStartMenuVO> startMenus, List<ProjectBigSchedulingDetailsVO> schedulingDetailsVOs, String userId, String projectNo){
        int result = verifyDelay(schedulingDetailsVOs, projectNo, null);
        if (result == 1) {
            addStartMenu(startMenus, projectNo, AfConfigs.DELAY_ORDER.configNo, userId);
        }
    }

    /**
     * 获取开工准备发起菜单
     * @param startMenus 发起菜单
     * @param userId 用户id
     * @param projectNo 项目编号
     */
    private void getStartStartMenus(List<AfStartMenuVO> startMenus, List<ProjectBigSchedulingDetailsVO> schedulingDetailsVOs, String projectNo, String userId) {
        int result = verifyStartApplicationAndStartReport(schedulingDetailsVOs, projectNo, null);
        if (result == 1 || result == 3) {
            addStartMenu(startMenus, projectNo, AfConfigs.START_APPLICATION.configNo, userId);
        }
        if (result == 2 || result == 3) {
            addStartMenu(startMenus, projectNo, AfConfigs.START_REPORT.configNo, userId);
        }
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
     * 根据审批流配置编号、项目编号、排期编号查询审批实例
     * @param configNo 审批流配置编号
     * @param projectNo 项目编号
     * @param scheduleSort 排期编号
     * @param status 实例状态
     * @return 审批实例
     */
    private List<AfInstance> findByConfigNoAndProjectNoAndScheduleSortAndStatus(String configNo, String projectNo, Integer scheduleSort, int status) {
        AfInstanceExample example = new AfInstanceExample();
        example.createCriteria().andConfigNoEqualTo(configNo).andProjectNoEqualTo(projectNo).andScheduleSortEqualTo(scheduleSort).andStatusEqualTo(status);
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
                UserRoleSet role = roleService.findById(instance.getCreateRoleId());
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
                AfUserDTO userDTO = getUserInfo(instance.getCreateUserId(), instance.getCreateRoleId());
                instanceVO.setCreateUsername(userDTO.getUsername());
                instanceVO.setScheduleSort(instance.getScheduleSort());
                instanceVO.setRemark(instance.getRemark());
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
    public List<String> projectApprovalList(String projectNo) {
        List<String> pdfUrls = new ArrayList<>();
        List<AfInstancePdfUrl> instancePdfUrls= instancePdfUrlService.findByProjectNo(projectNo);
        if (instancePdfUrls != null) {
            for (AfInstancePdfUrl instancePdfUrl : instancePdfUrls) {
                pdfUrls.add(instancePdfUrl.getPdfUrl());
            }
        }
        return pdfUrls;
    }

    @Override
    public boolean getStartReportSucceed(String projectNo) {
        long successCount = getSuccessCount(projectNo, AfConfigs.START_REPORT.configNo);
        return successCount > 0;
    }

    @Override
    public int getScheduleEditable(String projectNo) {
        long startAndSuccessCount = getStartAndSuccessCount(projectNo, AfConfigs.START_APPLICATION.configNo);
        if (startAndSuccessCount > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public boolean getCheckReportSuccess(String projectNo, Integer scheduleSort) {
        List<AfInstance> instances = findByConfigNoAndProjectNoAndScheduleSortAndStatus(AfConfigs.CHECK_REPORT.configNo, projectNo, scheduleSort, AfConstants.APPROVAL_STATUS_SUCCESS);
        if (instances != null) {
            for (AfInstance instance : instances) {
                AfCheckItemVO checkItem = getCheckItem(instance.getData());
                if (checkItem.getType() == 1) {
                    return true;
                }
            }
        }
        return false;
    }
}
