package cn.thinkfree.service.approvalflow.impl;

import cn.thinkfree.core.base.MyLogger;
import cn.thinkfree.database.mapper.ApprovalFlowInstanceMapper;
import cn.thinkfree.database.mapper.PreProjectHouseTypeMapper;
import cn.thinkfree.database.mapper.UserRegisterMapper;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.vo.*;
import cn.thinkfree.service.approvalflow.*;
import cn.thinkfree.service.project.ProjectService;
import cn.thinkfree.service.scheduling.OrderUserService;
import javafx.scene.Scene;
import jxl.biff.BaseCellFeatures;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
 * 审批流实例服务层
 * @author song
 * @date 2018/10/12 17:35
 * @version 1.0
 */
@Service
@Transactional(rollbackFor = {RuntimeException.class})
public class ApprovalFlowInstanceServiceImpl implements ApprovalFlowInstanceService {

    private static final MyLogger LOGGER = new MyLogger(ApprovalFlowInstanceServiceImpl.class);

    @Resource
    private ApprovalFlowInstanceMapper instanceMapper;
    @Resource
    private ProjectService projectService;
    @Resource
    private ApprovalFlowConfigService configService;
    @Resource
    private ApprovalFlowConfigLogServiceImpl configLogService;
    @Resource
    private ApprovalFlowNodeService nodeService;
    @Resource
    private ApprovalFlowScheduleNodeRoleService scheduleNodeRoleService;
    @Resource
    private ApprovalFlowApprovalLogService approvalLogService;
    @Resource
    private RoleService roleService;
    @Resource
    private OrderUserService orderUserService;
    @Resource
    private ApprovalFlowNodeRoleService nodeRoleService;

    @Override
    public ApprovalFlowInstanceDetailVO detail(String num, String configNum, String projectNo, String userId, Integer scheduleSort, Integer scheduleVersion) {
        ApprovalFlowInstanceDetailVO instanceVO = new ApprovalFlowInstanceDetailVO();
        // 校验并获取项目信息
        ApprovalFlowProjectVO projectVO = getProjectInfo(projectNo);
        // 判断该用户是否有审批当前节点的权限

        ApprovalFlowInstance instance = null;
        ApprovalFlowConfigLog configLog;
        ApprovalFlowNodeVO currentNodeVO = null;
        boolean editable = false;
        if (StringUtils.isEmpty(num)) {
            ApprovalFlowConfig config = configService.findByNum(configNum);
            configLog = configLogService.findByConfigNumAndVersion(configNum, config.getVersion());
            editable = true;
        } else {
            instance = findByNum(num);
            configLog = configLogService.findByNum(instance.getConfigLogNum());
        }
        List<ApprovalFlowNodeVO> nodeVOs = nodeService.findVoByConfigLogNum(configLog.getNum());
        List<OrderUser> orderUsers = orderUserService.findByOrderNo(projectNo);
        List<ApprovalFlowUserVO> userVOs = getUserVOs(nodeVOs, scheduleSort, scheduleVersion);
        // 已经审批过
        if (instance != null) {
            List<ApprovalFlowApprovalLog> approvalLogs = approvalLogService.findByInstanceNum(instance.getNum());
            fillApprovalMsg(approvalLogs, orderUsers, userVOs);
            currentNodeVO = getCurrentNode(nodeVOs, instance.getCurrentNodeNum());

            instanceVO.setData(instance.getData());
        }
        if (currentNodeVO == null) {
            currentNodeVO = nodeVOs.get(0);
        }
        verifyApprovalAuthority(userId, userVOs, currentNodeVO.getNum());
        List<ApprovalFlowOption> options = currentNodeVO.getOptions();

        instanceVO.setNodeNum(currentNodeVO.getNum());
        instanceVO.setNodeDescribe(currentNodeVO.getNodeName());
        instanceVO.setOptions(options);

        instanceVO.setEditable(editable);
        instanceVO.setUserVOs(userVOs);
        instanceVO.setConfigLogNum(configLog.getNum());
        instanceVO.setProject(projectVO);
        return instanceVO;
    }

    /**
     * 校验该用户是否有当前节点的审批权限
     * @param userId 当前用户ID
     * @param userVOs 用户审批信息
     * @param nodeNum 当前节点编号
     */
    private void verifyApprovalAuthority(String userId, List<ApprovalFlowUserVO> userVOs, String nodeNum){
        boolean haveAuthority = false;
        for (ApprovalFlowUserVO userVO : userVOs) {
            if (userVO.getNodeNum().equals(nodeNum) && userVO.getUserId().equals(userId)) {
                haveAuthority = true;
                break;
            }
        }
        if (!haveAuthority) {
            throw new RuntimeException("该用户没有当前节点的审批权限！");
        }
    }

    private ApprovalFlowNodeVO getCurrentNode(List<ApprovalFlowNodeVO> nodeVOs, String currentNodeNum){
        for (ApprovalFlowNodeVO nodeVO : nodeVOs) {
            if (nodeVO.getNum().equals(currentNodeNum)) {
                return nodeVO;
            }
        }
        LOGGER.error("通过审批流实例中记录的节点编号获取节点信息出错！");
        return null;
    }

    /**
     * 填充审批信息：用户信息，对应节点是否已经审批过
     * @param approvalLogs 审批信息
     * @param orderUsers 项目用户关系
     * @param userVOs 部分审批信息
     */
    private void fillApprovalMsg(List<ApprovalFlowApprovalLog> approvalLogs, List<OrderUser> orderUsers, List<ApprovalFlowUserVO> userVOs) {
        List<String> userIds = new ArrayList<>(userVOs.size());
        for (ApprovalFlowUserVO userVO : userVOs) {
            for (OrderUser orderUser : orderUsers) {
                if (userVO.getRoleId().equals(orderUser.getRoleId())) {
                    userVO.setUserId(orderUser.getUserId());
                }
            }
            for (ApprovalFlowApprovalLog approvalLog : approvalLogs) {
                if (approvalLog.getRoleId().equals(userVO.getRoleId())) {
                    userVO.setIsOperated(true);
                    userVO.setApprovalTime(approvalLog.getCreateTime());
                }
            }
            userIds.add(userVO.getUserId());
        }

    }

    /**
     * 查询审批用户角色信息
     * @param nodeVOs 审批节点信息
     * @param scheduleSort 项目排期编号
     * @param scheduleVersion 项目排期版本号
     * @return 审批用户角色信息
     */
    private List<ApprovalFlowUserVO> getUserVOs(List<ApprovalFlowNodeVO> nodeVOs, Integer scheduleSort, Integer scheduleVersion) {
        List<List<ApprovalFlowScheduleNodeRole>> scheduleNodeRoles = scheduleNodeRoleService.findByNodesAndScheduleSortAndVersion(nodeVOs, scheduleSort, scheduleVersion);
        List<ApprovalFlowUserVO> userVOs = new ArrayList<>();
        // 公司是否针对当前项目排期设置过审批顺序
        if (null != scheduleNodeRoles) {
            for (List<ApprovalFlowScheduleNodeRole> scheduleNodeRoleList : scheduleNodeRoles) {
                for (ApprovalFlowScheduleNodeRole scheduleNodeRole : scheduleNodeRoleList) {
                    ApprovalFlowUserVO userVO = new ApprovalFlowUserVO();
                    userVO.setRoleId(scheduleNodeRole.getRoleId());
                    userVO.setNodeNum(scheduleNodeRole.getNodeNum());
                    userVOs.add(userVO);
                }
            }
        } else {
            for (ApprovalFlowNodeVO nodeVO : nodeVOs) {
                if (nodeVO.getNodeRoles() != null) {
                    for (ApprovalFlowNodeRole nodeRole : nodeVO.getNodeRoles()) {
                        ApprovalFlowUserVO userVO = new ApprovalFlowUserVO();
                        userVO.setRoleId(nodeRole.getRoleId());
                        userVO.setNodeNum(nodeVO.getNum());
                        userVOs.add(userVO);
                    }
                }
            }
        }

        List<UserRoleSet> roles = roleService.findAll();
        for (ApprovalFlowUserVO userVO : userVOs) {
            for (UserRoleSet role : roles) {
                if (userVO.getRoleId().equals(role.getRoleCode())) {
                    userVO.setRoleName(role.getRoleName());
                }
            }
        }
        return userVOs;
    }

    /**
     * 根据编号查询审批流实例
     * @param num 编号
     * @return 审批流实例
     */
    private ApprovalFlowInstance findByNum(String num) {
        ApprovalFlowInstanceExample example = new ApprovalFlowInstanceExample();
        example.createCriteria().andNumEqualTo(num);
        List<ApprovalFlowInstance> instances = instanceMapper.selectByExample(example);
        return instances != null && instances.size() > 0 ? instances.get(0) : null;
    }
    /**
     * 获取项目信息
     * @param projectNo 项目编号
     * @return 项目信息
     */
    private ApprovalFlowProjectVO getProjectInfo(String projectNo){
        ApprovalFlowProjectVO projectVO = new ApprovalFlowProjectVO();
        Project project = projectService.findByProjectNo(projectNo);
        if (project == null) {
            throw new RuntimeException("");
        }

        String houseType = projectService.getHouseType(project);

        projectVO.setProjectNo(projectNo);
        projectVO.setHouseType(houseType);
        projectVO.setAddress(project.getAddress() + project.getAddressDetail());
        projectVO.setOwnerId(project.getOwnerId());
        return projectVO;
    }

    @Override
    public void approval(ApprovalFlowApprovalVO approvalVO) {
        verifyApprovalAuthority(approvalVO.getProjectNo(), approvalVO.getNodeNum(), approvalVO.getUserId(), approvalVO.getScheduleSort(), approvalVO.getScheduleVersion());

        String instanceNum = approvalVO.getInstanceNum();
        String configLogNum;
        ApprovalFlowInstance instance = null;
        // 如果审批流实例编码为空，说明未审批过，则创建审批流实例，否则继续审批
        if (StringUtils.isEmpty(instanceNum)) {
            configLogNum = approvalVO.getConfigLogNum();
            instance = new ApprovalFlowInstance();
//            instance.setCompanyNum(null);
//            instance.setConfigLogNum(configLogNum);
//            instance.setConfigNum();
//            instance.setCreateRoleId();
//            instance.setCreateUserId();
//            instance.setCreateTime();
//            instance.setData();
//            instance.setIsEnd();
//            instance.setNum();
//            instance.setCurrentNodeNum();
//            instance.setProjectNum();
//            instance.setScheduleSort();
//            instance.setScheduleVersion();
        } else {
            instance = findByNum(instanceNum);
            configLogNum = instance.getConfigLogNum();

        }



        /**
         * 校验当前用户是否有审批权限
         * 判断下一步执行节点
         */
    }

    /**
     * 校验当前用户是否有审批权限
     * @param projectNo 项目编号
     * @param nodeNum 节点编号
     * @param userId 用户编号
     * @param scheduleSort 项目排期编号
     * @param scheduleVersion 项目排期版本号
     */
    private void verifyApprovalAuthority(String projectNo, String nodeNum, String userId, Integer scheduleSort, Integer scheduleVersion){
        List<UserRoleSet> roles = new ArrayList<>();
        List<ApprovalFlowScheduleNodeRole> scheduleNodeRoles = scheduleNodeRoleService.findByNodeNumAndScheduleSortAndVersion(nodeNum, scheduleSort, scheduleVersion);
        if (scheduleNodeRoles != null && scheduleNodeRoles.size() > 0) {
            for (ApprovalFlowScheduleNodeRole scheduleNodeRole : scheduleNodeRoles) {
                UserRoleSet role = new UserRoleSet();
                role.setRoleCode(scheduleNodeRole.getRoleId());
                roles.add(role);
            }
        } else {
            List<ApprovalFlowNodeRole> nodeRoles = nodeRoleService.findByNodeNum(nodeNum);
            for (ApprovalFlowNodeRole nodeRole : nodeRoles) {
                UserRoleSet role = new UserRoleSet();
                role.setRoleCode(nodeRole.getRoleId());
                roles.add(role);
            }
        }
        List<OrderUser> orderUsers = orderUserService.findByOrderNoAndUserId(projectNo, userId);
        boolean haveAuthority = false;
        if (orderUsers != null) {
            for (UserRoleSet role : roles) {
                for (OrderUser orderUser : orderUsers) {
                    if (role.getRoleCode().equals(orderUser.getRoleId())) {
                        haveAuthority = true;
                        break;
                    }
                }
            }
        }
        if (!haveAuthority) {
            throw new RuntimeException("该用户没有当前节点的审批权限");
        }
    }

}
