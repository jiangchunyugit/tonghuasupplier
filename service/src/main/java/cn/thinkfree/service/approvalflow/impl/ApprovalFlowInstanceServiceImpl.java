package cn.thinkfree.service.approvalflow.impl;

import cn.thinkfree.database.mapper.ApprovalFlowInstanceMapper;
import cn.thinkfree.database.mapper.PreProjectHouseTypeMapper;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.vo.ApprovalFlowConfigLogVO;
import cn.thinkfree.database.vo.ApprovalFlowInstanceDetailVO;
import cn.thinkfree.database.vo.ApprovalFlowNodeVO;
import cn.thinkfree.database.vo.ApprovalFlowProjectVO;
import cn.thinkfree.service.approvalflow.*;
import cn.thinkfree.service.project.ProjectService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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

    @Override
    public ApprovalFlowInstanceDetailVO detail(String num, String configNum, String projectNo, String userId, Integer scheduleSort, Integer scheduleVersion) {
        ApprovalFlowInstanceDetailVO instanceVO = new ApprovalFlowInstanceDetailVO();
        ApprovalFlowProjectVO projectVO = getProjectInfo(projectNo);

        if (StringUtils.isEmpty(num) && StringUtils.isNotEmpty(configNum)) {
            ApprovalFlowConfig config = configService.findByNum(configNum);
            ApprovalFlowConfigLog configLog = configLogService.findByConfigNumAndVersion(configNum, config.getVersion());
            List<ApprovalFlowNodeVO> nodeVOs = nodeService.findVoByConfigLogNum(configLog.getNum());
            List<List<ApprovalFlowScheduleNodeRole>> scheduleNodeRoles = scheduleNodeRoleService.findByNodesAndScheduleSortAndVersion(nodeVOs, scheduleSort, scheduleVersion);
        } else if (StringUtils.isNotEmpty(num)){
            ApprovalFlowInstance instance = findByNum(num);

            List<ApprovalFlowApprovalLog> approvalLogs = approvalLogService.findByInstanceNum(instance.getNum());

        } else {
            throw new RuntimeException("");
        }
        instanceVO.setProject(projectVO);
        return instanceVO;
    }

    private ApprovalFlowInstance findByNum(String num) {
        ApprovalFlowInstanceExample example = new ApprovalFlowInstanceExample();
        example.createCriteria().andNumEqualTo(num);
        List<ApprovalFlowInstance> instances = instanceMapper.selectByExample(example);
        return instances != null && instances.size() > 0 ? instances.get(0) : null;
    }

    /**
     * 获取项目信息
     * @param projectNo 项目编号
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
        projectVO.setAddress(project.getAddressDetail());
        projectVO.setOwnerId(project.getOwnerId());
        return projectVO;
    }

}
