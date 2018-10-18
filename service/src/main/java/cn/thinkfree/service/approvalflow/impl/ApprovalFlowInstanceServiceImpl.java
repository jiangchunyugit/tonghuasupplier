package cn.thinkfree.service.approvalflow.impl;

import cn.thinkfree.database.mapper.ApprovalFlowInstanceMapper;
import cn.thinkfree.database.mapper.PreProjectHouseTypeMapper;
import cn.thinkfree.database.mapper.UserRegisterMapper;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.vo.*;
import cn.thinkfree.service.approvalflow.*;
import cn.thinkfree.service.project.ProjectService;
import cn.thinkfree.service.scheduling.OrderUserService;
import javafx.scene.Scene;
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

    @Override
    public ApprovalFlowInstanceDetailVO detail(String num, String configNum, String projectNo, String userId, Integer scheduleSort, Integer scheduleVersion) {
        ApprovalFlowInstanceDetailVO instanceVO = new ApprovalFlowInstanceDetailVO();
        ApprovalFlowProjectVO projectVO = getProjectInfo(projectNo);

        ApprovalFlowInstance instance = null;
        ApprovalFlowConfigLog configLog;
        if (StringUtils.isEmpty(num) && StringUtils.isNotEmpty(configNum)) {
            ApprovalFlowConfig config = configService.findByNum(configNum);
            configLog = configLogService.findByConfigNumAndVersion(configNum, config.getVersion());
        } else if (StringUtils.isNotEmpty(num)){
            instance = findByNum(num);
            configLog = configLogService.findByNum(instance.getConfigLogNum());

        } else {
            throw new RuntimeException("");
        }
        List<ApprovalFlowNodeVO> nodeVOs = nodeService.findVoByConfigLogNum(configLog.getNum());
        List<OrderUser> orderUsers = orderUserService.findByOrderNo(projectNo);
        List<ApprovalFlowUserVO> userVOs = getUserVOs(nodeVOs, scheduleSort, scheduleVersion);
        // 已经审批过
        if (instance != null) {
            List<ApprovalFlowApprovalLog> approvalLogs = approvalLogService.findByInstanceNum(instance.getNum());
            fillApprovalMsg(approvalLogs, orderUsers, userVOs);
        }

        instanceVO.setUserVOs(userVOs);
        instanceVO.setConfigLogNum(configLog.getNum());
        instanceVO.setProject(projectVO);
        return instanceVO;
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
                    userVOs.add(userVO);
                }
            }
        } else {
            for (ApprovalFlowNodeVO nodeVO : nodeVOs) {
                if (nodeVO.getNodeRoles() != null) {
                    for (ApprovalFlowNodeRole nodeRole : nodeVO.getNodeRoles()) {
                        ApprovalFlowUserVO userVO = new ApprovalFlowUserVO();
                        userVO.setRoleId(nodeRole.getRoleId());
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
        projectVO.setOwnerId(Long.parseLong(project.getOwnerId()));
        return projectVO;
    }

}
