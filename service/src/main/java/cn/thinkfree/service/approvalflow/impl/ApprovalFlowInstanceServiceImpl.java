package cn.thinkfree.service.approvalflow.impl;

import cn.thinkfree.core.base.MyLogger;
import cn.thinkfree.core.utils.ThreadManager;
import cn.thinkfree.core.utils.UniqueCodeGenerator;
import cn.thinkfree.database.mapper.ApprovalFlowInstanceMapper;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.vo.*;
import cn.thinkfree.service.approvalflow.*;
import cn.thinkfree.service.neworder.NewOrderUserService;
import cn.thinkfree.service.project.ProjectService;
import cn.thinkfree.service.utils.HttpUtils;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;


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
    private NewOrderUserService orderUserService;
    @Resource
    private ApprovalFlowNodeRoleService nodeRoleService;
    @Resource
    private ApprovalFlowOptionService optionService;
    @Resource
    private ApprovalFlowNoticeUrlService noticeUrlService;
    @Resource
    private ApprovalFlowMessageLogService messageLogService;

    @Override
    public ApprovalFlowInstanceDetailVO detail(String instanceNum, String configNum, String companyNo, String projectNo, String userId, Integer scheduleSort, Integer scheduleVersion) {
        ApprovalFlowInstanceDetailVO instanceVO = new ApprovalFlowInstanceDetailVO();
        // 获取并校验项目信息
        ApprovalFlowProjectVO projectVO = getAndVerifyProjectInfo(projectNo);
        // 判断该用户是否有审批当前节点的权限

        ApprovalFlowInstance instance = null;
        ApprovalFlowConfigLog configLog;
        ApprovalFlowNodeVO currentNodeVO = null;
        boolean editable = false;
        if (StringUtils.isEmpty(instanceNum)) {
            ApprovalFlowConfig config = configService.findByNum(configNum);
            configLog = configLogService.findByConfigNumAndVersion(configNum, config.getVersion());
            editable = true;
        } else {
            instance = findByNum(instanceNum);
            if (instance == null) {
                LOGGER.error("未查询到编号为{}的审批流实例！", instanceNum);
                throw new RuntimeException();
            }
            companyNo = instance.getCompanyNo();
            scheduleSort = instance.getScheduleSort();
            scheduleVersion = instance.getScheduleVersion();

            configLog = configLogService.findByNum(instance.getConfigLogNum());
        }
        // 获取审批节点配置信息
        List<ApprovalFlowNodeVO> nodeVOs = nodeService.findVoByConfigLogNum(configLog.getNum());
        // 获取项目用户角色信息
        List<OrderUser> orderUsers = orderUserService.findByOrderNo(projectNo);
        // 获取审批角色信息
        List<ApprovalFlowUserVO> userVOs = getUserVOs(nodeVOs, companyNo, scheduleSort, scheduleVersion);
        // 已经审批过，填充审批信息（审批节点是否审批过，审批时间）
        if (instance != null) {
            List<ApprovalFlowApprovalLog> approvalLogs = approvalLogService.findByInstanceNum(instance.getNum());
            fillApprovalMsg(approvalLogs, orderUsers, userVOs);

            if (StringUtils.isNotEmpty(instance.getCurrentNodeNum())) {
                currentNodeVO = getCurrentNode(nodeVOs, instance.getCurrentNodeNum());
                // 在已经存在审批的情况下，当前节点等于第一个节点说明审批流跳转回了第一步
                if (currentNodeVO.getNum().equals(nodeVOs.get(0).getNum())) {
                    editable = true;
                }
            }

            instanceVO.setData(instance.getData());
        } else {
            currentNodeVO = nodeVOs.get(0);
        }
        if (null != currentNodeVO) {
            boolean hasAuthority = verifyApprovalAuthority(userId, userVOs, currentNodeVO.getNum());
            if (hasAuthority) {
                instanceVO.setNodeNum(currentNodeVO.getNum());
                instanceVO.setNodeDescribe(currentNodeVO.getName());
                instanceVO.setOptions(currentNodeVO.getOptions());
            } else {
                editable = false;
            }

        }

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
    private boolean verifyApprovalAuthority(String userId, List<ApprovalFlowUserVO> userVOs, String nodeNum){
        boolean haveAuthority = false;
        for (ApprovalFlowUserVO userVO : userVOs) {
            if (userVO.getNodeNum().equals(nodeNum) && userVO.getUserId().equals(userId)) {
                haveAuthority = true;
                break;
            }
        }
        return haveAuthority;
    }

    private <T extends ApprovalFlowNode> T getCurrentNode(List<T> nodes, String currentNodeNum){
        for (T node : nodes) {
            if (node.getNum().equals(currentNodeNum)) {
                return node;
            }
        }
        LOGGER.error("未查询到编号为{}的审批流节点！", currentNodeNum);
        throw new RuntimeException();
    }

    /**
     * 填充审批信息：用户信息，对应节点是否已经审批过
     * @param approvalLogs 审批信息
     * @param orderUsers 项目用户关系
     * @param userVOs 部分审批信息
     */
    private void fillApprovalMsg(List<ApprovalFlowApprovalLog> approvalLogs, List<OrderUser> orderUsers, List<ApprovalFlowUserVO> userVOs) {
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
        }
    }

    /**
     * 查询审批用户角色信息
     * @param nodeVOs 审批节点信息
     * @param companyNo 公司编号
     * @param scheduleSort 项目排期编号
     * @param scheduleVersion 项目排期版本号
     * @return 审批用户角色信息
     */
    private List<ApprovalFlowUserVO> getUserVOs(List<ApprovalFlowNodeVO> nodeVOs, String companyNo, Integer scheduleSort, Integer scheduleVersion) {
        List<ApprovalFlowScheduleNodeRole> scheduleNodeRoles = scheduleNodeRoleService.findByNodesAndCompanyNoAndScheduleSortAndVersion(nodeVOs, companyNo, scheduleSort, scheduleVersion);
        List<ApprovalFlowUserVO> userVOs = new ArrayList<>();
        // 公司是否针对当前项目排期设置过审批顺序
        if (null != scheduleNodeRoles) {
            for (ApprovalFlowScheduleNodeRole scheduleNodeRole : scheduleNodeRoles) {
                ApprovalFlowUserVO userVO = new ApprovalFlowUserVO();
                userVO.setRoleId(scheduleNodeRole.getRoleId());
                userVO.setNodeNum(scheduleNodeRole.getNodeNum());
                userVOs.add(userVO);
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
    @Override
    public ApprovalFlowInstance findByNum(String num) {
        ApprovalFlowInstanceExample example = new ApprovalFlowInstanceExample();
        example.createCriteria().andNumEqualTo(num);
        List<ApprovalFlowInstance> instances = instanceMapper.selectByExample(example);
        return instances != null && instances.size() > 0 ? instances.get(0) : null;
    }

    @Override
    public String findConfigNameByInstanceNum(String instanceNum) {
        return instanceMapper.findConfigNameByInstanceNum(instanceNum);
    }

    /**
     * 获取项目信息
     * @param projectNo 项目编号
     * @return 项目信息
     */
    private ApprovalFlowProjectVO getAndVerifyProjectInfo(String projectNo){
        ApprovalFlowProjectVO projectVO = new ApprovalFlowProjectVO();
        Project project = projectService.findByProjectNo(projectNo);
        if (project == null) {
            LOGGER.error("未查询到项目编号为{}的项目！", projectNo);
            throw new RuntimeException();
        }

        String houseType = projectService.getHouseType(project);

        projectVO.setProjectNo(projectNo);
        projectVO.setHouseType(houseType);
        projectVO.setAddress(project.getAddress() + project.getAddressDetail());
        projectVO.setOwnerId(project.getOwnerId());
        return projectVO;
    }

    /**
     * @param approvalVO 审批信息
     */
    @Override
    public void approval(ApprovalFlowApprovalVO approvalVO) {
        ApprovalFlowInstance instance;

        ApprovalFlowOption option = optionService.findByNum(approvalVO.getOptionNum());
        if (option == null) {
            LOGGER.error("未查询到编号为[{}]的操作项", approvalVO.getOptionNum());
            throw new RuntimeException();
        }
        if (option.getIsNeedRemark() == 1 && StringUtils.isEmpty(approvalVO.getRemark())) {
            LOGGER.error("该操作项需要添加备注");
            throw new RuntimeException();
        }
        if (StringUtils.isNotEmpty(approvalVO.getInstanceNum())) {
            instance = findByNum(approvalVO.getInstanceNum());
            if (instance == null) {
                LOGGER.error("未查询到编号为{}的审批流实例", approvalVO.getInstanceNum());
                throw new RuntimeException();
            }
            if (!instance.getCurrentNodeNum().equals(approvalVO.getNodeNum())){
                LOGGER.error("审批流节点编号[{}]与instance表中的节点编号[{}]不相符", approvalVO.getNodeNum(), instance.getCurrentNodeNum());
                throw new RuntimeException();
            }
            approvalVO.setCompanyNo(instance.getCompanyNo());
            approvalVO.setScheduleSort(instance.getScheduleSort());
            approvalVO.setScheduleVersion(instance.getScheduleVersion());
            approvalVO.setProjectNo(instance.getProjectNo());
        }

        List<OrderUser> orderUsers = orderUserService.findByOrderNo(approvalVO.getProjectNo());
        // 获取当前用户的角色，并校验是否有审批权限
        String roleId = getRoleIdOfCurrentUser(orderUsers, approvalVO, approvalVO.getNodeNum());

        List<ApprovalFlowNode> nodes = nodeService.findByConfigLogNum(approvalVO.getConfigLogNum());
        ApprovalFlowNode currentNode = getCurrentNode(nodes, approvalVO.getNodeNum());
        // 如果审批流实例编码为空，说明未审批过，则创建审批流实例，否则继续审批
        if (StringUtils.isEmpty(approvalVO.getProjectNo())) {
            if (currentNode.getSort() != 1) {
                LOGGER.error("错误的开始审批节点编号：{}", approvalVO.getNodeNum());
                throw new RuntimeException();
            }
            instance = create(approvalVO, roleId);
            approvalVO.setInstanceNum(instance.getNum());
        } else {
            if (currentNode.getSort() == 1) {
                updateData(approvalVO.getInstanceNum(), approvalVO.getData());
            }
        }
        // 保存审批记录

        ApprovalFlowApprovalLog approvalLog = approvalLogService.create(approvalVO.getInstanceNum(), approvalVO.getNodeNum(), approvalVO.getUserId(), roleId, approvalVO.getOptionNum(), approvalVO.getRemark());

        sendNotice(approvalVO, approvalLog.getNum());

        String message = StringUtils.isEmpty(approvalVO.getRemark()) ? approvalVO.getRemark() : currentNode.getMessage();
        sendApprovalMessage(approvalVO, approvalLog.getNum(), message, orderUsers);

        executeNextNode(approvalVO, approvalLog.getNum(), nodes, currentNode, option, orderUsers);
    }

    /**
     * 更新审批数据
     * @param instanceNum 审批流实例编号
     * @param data 数据
     */
    private void updateData(String instanceNum, String data) {
        ApprovalFlowInstance instance = new ApprovalFlowInstance();
        instance.setData(data);

        ApprovalFlowInstanceExample example = new ApprovalFlowInstanceExample();
        example.createCriteria().andNumEqualTo(instanceNum);

        instanceMapper.updateByExampleSelective(instance, example);
    }

    /**
     * 发送订阅通知
     */
    private void sendNotice(ApprovalFlowApprovalVO approvalVO, String approvalLogNum){
        List<ApprovalFlowNoticeUrl> noticeUrls = noticeUrlService.findByNodeNum(approvalVO.getNodeNum());
        if (noticeUrls != null) {
            for (ApprovalFlowNoticeUrl noticeUrl : noticeUrls) {
                LOGGER.info("发送通知给：{},发送内容为：{}", JSONObject.toJSONString(noticeUrl), approvalVO.getData());

                String nodeNum = noticeUrl.getNodeNum();
                Map<String, String> requestMap = new HashMap<>(16);
                requestMap.put("instanceNum", approvalVO.getInstanceNum());
                requestMap.put("nodeNum", nodeNum);
                requestMap.put("projectNo", approvalVO.getProjectNo());
                requestMap.put("approvalLogNum", approvalLogNum);
                requestMap.put("userId", approvalVO.getUserId());
                requestMap.put("dataJson", approvalVO.getData());
                try {
                    ThreadManager.getThreadPollProxy().execute(() -> {
                        HttpUtils.HttpRespMsg respMsg = HttpUtils.post(noticeUrl.getNoticeUrl(), requestMap);
                        LOGGER.info("发送通知成功：url:{},requestMap:{},resp:{}", noticeUrl.getNoticeUrl(), JSONObject.toJSONString(requestMap), JSONObject.toJSONString(respMsg));
                    });
                } catch (Exception e) {
                    LOGGER.error("发送通知失败：url:{},requestMap:{}", noticeUrl.getNoticeUrl(), JSONObject.toJSONString(requestMap));
                }
            }
        }
    }

    /**
     * 发送推送消息
     */
    private void sendApprovalMessage(ApprovalFlowApprovalVO approvalVO, String approvalLogNum, String remark, List<OrderUser> orderUsers) {
        List<ApprovalFlowNodeRole> nodeRoles = nodeRoleService.findReceiveRoleByNodeNum(approvalVO.getNodeNum());
        if (nodeRoles != null) {
            for (ApprovalFlowNodeRole nodeRole : nodeRoles) {
                String userId = null;
                for (OrderUser orderUser : orderUsers) {
                    if (orderUser.getRoleId().equals(nodeRole.getRoleId())) {
                        userId = orderUser.getUserId();
                        break;
                    }
                }
                if (userId != null) {
                    saveAndSendApprovalMessage(approvalLogNum, approvalVO, userId, approvalVO.getUserId(), remark, orderUsers);
                } else {
                    LOGGER.error("在项目:[{}]中未查询到角色id为：[{}]的用户", approvalVO.getProjectNo(), nodeRole.getRoleId());
                    throw new RuntimeException("");
                }
            }
        }

    }
    /**
     * 保存发送的审批消息
     *
     * @param approvalLogNum 审批编号
     * @param userId                   发送给谁
     * @param sendUserId               提交审批人ID
     * @param result                   审批结果
     */
    private void saveAndSendApprovalMessage(String approvalLogNum, ApprovalFlowApprovalVO approvalVO, String userId, String sendUserId, String result, List<OrderUser> orderUsers) {
        ApprovalFlowMessageLog messageLog = new ApprovalFlowMessageLog();
        messageLog.setInstanceNum(approvalVO.getInstanceNum());
        messageLog.setMessage(result);
        messageLog.setProjectNo(approvalVO.getProjectNo());
        messageLog.setUserId(userId);
        messageLog.setSendUserId(sendUserId);
        messageLog.setScheduleSort(approvalVO.getScheduleSort());
        for (OrderUser orderUser : orderUsers) {
            if (userId.equals(orderUser.getUserId())) {
                messageLog.setRoleId(orderUser.getRoleId());
            }
            if (sendUserId.equals(orderUser.getUserId())) {
                messageLog.setRoleId(orderUser.getRoleId());
            }
        }
        messageLogService.create(messageLog);

        ThreadManager.getThreadPollProxy().execute(() -> {
//            try {
//                UAppApproveMessageDTO messageDTO = new UAppApproveMessageDTO();
//                messageDTO.setProjectNo(projectNo);
//                //接收人
//                messageDTO.setRecipientId(userId);
//                //报告ID
//                messageDTO.setReportId(approvalNum);
//                //发送人ID
//                messageDTO.setSenderId(sendUserId);
//                Meta<String> myRespBundle = uAppServiceClient.sendApprovalMsg(messageDTO);
//                logger.info("发送推送消息结果：{}", JSONObject.toJSONString(myRespBundle));
//            } catch (Exception e) {
//                logger.error("调用推送服务失败", e);
//            }
        });
    }

    /**
     * 更新当前要审批的节点编号
     * @param orderUsers 项目角色用户信息
     * @param approvalVO 审批信息
     * @param nodeNum 节点编号
     */
    private void updateCurrentNodeNumAndUserId(List<OrderUser> orderUsers, ApprovalFlowApprovalVO approvalVO, String nodeNum) {
        List<UserRoleDTO> userRoles = findApprovalUserRole(orderUsers, approvalVO, nodeNum);
        String userId = null;
        if (userRoles != null && userRoles.size() > 0) {
            userId = userRoles.get(0).getUserId();
        }
        if (userId != null) {

            ApprovalFlowInstance instance = new ApprovalFlowInstance();
            instance.setCurrentNodeNum(nodeNum);
            instance.setCreateUserId(userId);

            ApprovalFlowInstanceExample example = new ApprovalFlowInstanceExample();
            example.createCriteria().andNumEqualTo(approvalVO.getInstanceNum());

            instanceMapper.updateByExampleSelective(instance, example);
        } else {
            throw new RuntimeException();
        }
    }

    private ApprovalFlowNode getNextNode(String instanceNum, List<ApprovalFlowNode> nodes, ApprovalFlowNode currentNode, ApprovalFlowOption option) {
        int currentNodeIndex =currentNode.getSort();
        ApprovalFlowNode nextNode = null;
        if (option != null) {
            if (option.getBackStep() == -1) {
                // 执行下一步操作
                if (currentNodeIndex + 1 < nodes.size()) {
                    nextNode = nodes.get(currentNodeIndex + 1);
                }
            } else {
                // 执行跳转
                int backStep = option.getBackStep();
                nextNode = nodes.get(backStep);
                if (backStep < currentNodeIndex) {
                    // 向后跳转
                    List<String> nodeNums = new ArrayList<>(nodes.size());
                    for (int index = 0; index < nodes.size(); index++) {
                        if (index >= backStep) {
                            nodeNums.add(nodes.get(index).getNum());
                        }
                    }
                    approvalLogService.updateIsInvalidByInstanceNumAndNodeNums(instanceNum, nodeNums);
                }
            }
        }
        return nextNode;
    }

    private void remindNextNode(ApprovalFlowApprovalVO approvalVO, String approvalLogNum, ApprovalFlowNode currentNode, ApprovalFlowNode nextNode, List<OrderUser> orderUsers){
        if (currentNode.getIsPushMsg() == 1 && nextNode != null) {
            List<UserRoleDTO> userRoles = findApprovalUserRole(orderUsers, approvalVO, nextNode.getNum());
            if (null != userRoles) {
                String message = StringUtils.isEmpty(approvalVO.getRemark()) ? approvalVO.getRemark() : currentNode.getMessage();
                for (UserRoleDTO userRole : userRoles) {
                    saveAndSendApprovalMessage(approvalLogNum, approvalVO, userRole.getUserId(), approvalVO.getUserId(), message, orderUsers);
                }
            }
        }
    }

    private void executeNextNode(ApprovalFlowApprovalVO approvalVO, String approvalLogNum, List<ApprovalFlowNode> nodes, ApprovalFlowNode currentNode, ApprovalFlowOption option, List<OrderUser> orderUsers) {
        ApprovalFlowNode nextNode = getNextNode(approvalVO.getInstanceNum(), nodes, currentNode, option);
        if (nextNode != null) {
            approvalVO.setNodeNum(nextNode.getNum());

            if (currentNode.getIsPushMsg() == 1) {
                remindNextNode(approvalVO, approvalLogNum, currentNode, nextNode, orderUsers);
            }

            // 系统触发
            if (nextNode.getTriggerMode() == 0) {
                ApprovalFlowApprovalLog approvalLog = approvalLogService.create(approvalVO.getInstanceNum(), nextNode.getNum(), "system", "", "", "");
                sendNotice(approvalVO, approvalLog.getNum());
                sendApprovalMessage(approvalVO, approvalLog.getNum(), nextNode.getMessage(), orderUsers);

                ApprovalFlowOption nextNodeOption = null;
                List<ApprovalFlowOption> options = optionService.findByNodeNum(nextNode.getNum());
                if (options != null && options.size() > 0) {
                    nextNodeOption = options.get(0);
                }
                executeNextNode(approvalVO, approvalLog.getNum(), nodes, nextNode, nextNodeOption, orderUsers);
            } else {
                updateCurrentNodeNumAndUserId(orderUsers, approvalVO, nextNode.getNum());
            }
        } else {
            finishApproval(approvalVO.getInstanceNum());
        }
    }

    /**
     * 完成审批
     * @param instanceNum 审批流实例编号
     */
    private void finishApproval(String instanceNum) {
        ApprovalFlowInstance instance = findByNum(instanceNum);
        if (instance == null) {
            LOGGER.error("未查询到编号为：[{}]的审批流实例", instanceNum);
            throw new RuntimeException();
        }
        instance.setIsEnd(1);
        instance.setCurrentNodeNum(null);

        instanceMapper.updateByPrimaryKey(instance);
    }

    /**
     * 获取当前用户在审批流中的角色ID
     * @param orderUsers 项目用户角色关系
     * @param approvalVO 审批流信息
     * @return 用户角色ID
     */
    private String getRoleIdOfCurrentUser(List<OrderUser> orderUsers, ApprovalFlowApprovalVO approvalVO, String nodeNum) {
        List<UserRoleDTO> userRoles = findApprovalUserRole(orderUsers, approvalVO, nodeNum);
        String roleId = null;
        for (UserRoleDTO userRole : userRoles) {
            if (userRole.getUserId().equals(approvalVO.getUserId())) {
                roleId = userRole.getRoleId();
                break;
            }
        }
        if (roleId == null) {
            throw new RuntimeException("当前用户没有审批权限");
        }
        return roleId;
    }

    private List<UserRoleDTO> findApprovalUserRole(List<OrderUser> orderUsers, ApprovalFlowApprovalVO approvalVO, String nodeNum){
        List<String> roleIds = new ArrayList<>();
        List<ApprovalFlowScheduleNodeRole> scheduleNodeRoles = scheduleNodeRoleService.findByNodeNumAndCompanyNoAndScheduleSortAndVersion(nodeNum, approvalVO.getCompanyNo(), approvalVO.getScheduleSort(), approvalVO.getScheduleVersion());
        if (scheduleNodeRoles != null && scheduleNodeRoles.size() > 0) {
            for (ApprovalFlowScheduleNodeRole scheduleNodeRole : scheduleNodeRoles) {
                roleIds.add(scheduleNodeRole.getRoleId());
            }
        } else {
            List<ApprovalFlowNodeRole> nodeRoles = nodeRoleService.findSendRoleByNodeNum(nodeNum);
            for (ApprovalFlowNodeRole nodeRole : nodeRoles) {
                roleIds.add(nodeRole.getRoleId());
            }
        }
        List<UserRoleDTO> userRoles = new ArrayList<>(roleIds.size());
        for (String roleId : roleIds) {
            for (OrderUser orderUser : orderUsers) {
                if (orderUser.getRoleId().equals(roleId)) {
                    UserRoleDTO userRole = new UserRoleDTO();
                    userRole.setUserId(orderUser.getUserId());
                    userRole.setRoleId(orderUser.getRoleId());
                    userRoles.add(userRole);
                    break;
                }
            }
        }

        return userRoles;
    }


    /**
     * 创建审批流实例
     * @param approvalVO 审批信息
     * @param roleId 审批角色ID
     * @return 审批流实例
     */
    private ApprovalFlowInstance create(ApprovalFlowApprovalVO approvalVO, String roleId) {
        ApprovalFlowInstance instance = new ApprovalFlowInstance();
        instance.setConfigLogNum(approvalVO.getConfigLogNum());
        instance.setCreateRoleId(roleId);
        instance.setCompanyNo(approvalVO.getCompanyNo());
        instance.setCreateUserId(approvalVO.getUserId());
        instance.setCreateTime(new Date());
        instance.setData(approvalVO.getData());
        instance.setIsEnd(0);
        instance.setNum(UniqueCodeGenerator.AF_INSTANCE.getCode());
        instance.setProjectNo(approvalVO.getProjectNo());
        instance.setScheduleSort(approvalVO.getScheduleSort());
        instance.setScheduleVersion(approvalVO.getScheduleVersion());

        insert(instance);

        return instance;
    }

    private void insert(ApprovalFlowInstance instance) {
        instanceMapper.insertSelective(instance);
    }

    @Override
    public void clearApprovalLog() {

    }
}
