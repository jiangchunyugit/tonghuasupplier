package cn.thinkfree.service.approvalflow.impl;

import cn.thinkfree.core.base.MyLogger;
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

    /**
     * 首次审批：项目编号、审批流配置日志编号、操作项编号、数据、备注、用户编号、排期编号、排期版本
     * 非首次审批：审批流实例编号、操作项编号、”数据“、备注、用户编号
     *
     * 校验当前用户是否有审批权限、判断操作项是否属于当前节点
     *
     * 首次审批
     *   创建审批实例记录
     *   创建审批记录
     *
     * 非首次审批
     *   创建审批记录
     *
     * 判断下一步执行节点，记录下一步节点编号
     * @param approvalVO 审批信息
     */
    /**
     // TODO 检查该该操作是否必须填写原因
     */
    @Override
    public void approval(ApprovalFlowApprovalVO approvalVO) {
        String instanceNum = approvalVO.getInstanceNum();
        Integer scheduleSort = approvalVO.getScheduleSort();
        Integer scheduleVersion = approvalVO.getScheduleVersion();
        String configLogNum = approvalVO.getConfigLogNum();
        String projectNo = approvalVO.getProjectNo();
        String userId = approvalVO.getUserId();

        ApprovalFlowInstance instance;
        if (StringUtils.isNotEmpty(instanceNum)) {
            instance = findByNum(instanceNum);
            scheduleSort = instance.getScheduleSort();
            scheduleVersion = instance.getScheduleVersion();
            projectNo = instance.getProjectNum();
        }

        String roleId = getRoleIdByProjectNoAndUserId(projectNo, userId);
        verifyApprovalAuthority(projectNo, approvalVO.getNodeNum(), userId, scheduleSort, scheduleVersion);

        // 如果审批流实例编码为空，说明未审批过，则创建审批流实例，否则继续审批
        if (StringUtils.isEmpty(instanceNum)) {
            instance = create(configLogNum, userId, roleId, approvalVO.getData(), projectNo, scheduleSort, scheduleVersion);
            instanceNum = instance.getNum();
        }
        ApprovalFlowOption option = optionService.findByNum(approvalVO.getOptionNum());
        ApprovalFlowApprovalLog approvalLog = approvalLogService.create(instanceNum, approvalVO.getNodeNum(), userId, roleId, approvalVO.getOptionNum(), option.getBtnExplain(), approvalVO.getRemark());

        sendNotice(projectNo, instanceNum, approvalLog.getNum(), approvalVO.getNodeNum(), userId, approvalVO.getData());
        sendPushMsg(instanceNum, projectNo, approvalLog.getNum(), approvalVO.getNodeNum(), approvalVO.getRemark(), userId);

        ApprovalFlowNode nextNode = getNextNode(configLogNum, instanceNum, approvalVO.getNodeNum(), option);
        if (nextNode != null) {
            updateCurrentNodeNum(instanceNum, nextNode.getNum());
        }
    }

    /**
     * 发送订阅通知
     */
    private void sendNotice(String projectNo, String instanceNum, String approvalLogNum, String nodeNum, String userId, String data){
        List<ApprovalFlowNoticeUrl> noticeUrls = noticeUrlService.findByNodeNum(nodeNum);
        if (noticeUrls != null) {
            for (ApprovalFlowNoticeUrl noticeUrl : noticeUrls) {
                LOGGER.info("发送通知给：{},发送内容为：{}", JSONObject.toJSONString(noticeUrl), data);
                sendHttp(instanceNum, projectNo, approvalLogNum, noticeUrl, data, userId);
            }
        }
    }

    /**
     * 执行http请求，发送通知，无论发送成功或者失败，都不抛出异常信息
     * @param instanceNum 审批流实例编码
     * @param projectNo 项目编码
     * @param approvalLogNum 审批记录编码
     * @param noticeUrl  推送地址
     * @param dataJson 发送数据
     * @param userId 用户ID
     */
    private void sendHttp(String instanceNum, String projectNo, String approvalLogNum, ApprovalFlowNoticeUrl noticeUrl, String dataJson, String userId) {
        String nodeNum = noticeUrl.getNodeNum();
        Map<String, String> requestMap = new HashMap<>(16);
        requestMap.put("instanceNum", instanceNum);
        requestMap.put("nodeNum", nodeNum);
        requestMap.put("projectNo", projectNo);
        requestMap.put("approvalLogNum", approvalLogNum);
        requestMap.put("userId", userId);
        requestMap.put(noticeUrl.getDataKey(), dataJson);
        new Thread(() -> {
            try {
                //当前审批节点编号
                HttpUtils.HttpRespMsg respMsg = HttpUtils.post(noticeUrl.getNoticeUrl(), requestMap);
                LOGGER.info("发送通知成功：url:{},requestMap:{},resp:{}", noticeUrl.getNoticeUrl(), JSONObject.toJSONString(requestMap), JSONObject.toJSONString(respMsg));
            } catch (Exception e) {
                LOGGER.error("发送通知失败：url:{},requestMap:{}", noticeUrl.getNoticeUrl(), JSONObject.toJSONString(requestMap), e);
            }
        }).start();
    }

    /**
     * 发送推送消息
     */
    private void sendPushMsg(String instanceNum, String projectNo, String approvalLogNum, String nodeNum, String result, String userId) {
        List<ApprovalFlowNodeRole> nodeRoles = nodeRoleService.findReceiveRoleByNodeNum(nodeNum);
        for (ApprovalFlowNodeRole nodeRole : nodeRoles) {

            OrderUser orderUser = orderUserService.findByOrderNoAndRoleId(projectNo, nodeRole.getRoleId());
            if (orderUser == null) {
                LOGGER.error("没有发现角色编号为：{}，的用户", orderUser.getRoleId());
                continue;
            }
            LOGGER.info("发送消息给[{}]，msg:[{}]", orderUser.getUserId(), result);
            savePushMsg(approvalLogNum, instanceNum, projectNo, orderUser.getUserId(), userId, result);
        }
    }
    /**
     * 保存发送的审批消息
     *
     * @param approvalNum 审批编号
     * @param instanceNum 审批记录编号
     * @param projectNo                项目编号
     * @param userId                   发送给谁
     * @param sendUserId               提交审批人ID
     * @param result                   审批结果
     */
    private void savePushMsg(String approvalNum, String instanceNum, String projectNo, String userId, String sendUserId, String result) {
//        UserInfoEntity userInfo = queryUserName(userId);
//        if (userInfo == null) {
//            return;
//        }
//        UserInfoEntity sendUserInfo = queryUserName(sendUserId);
//        if (sendUserInfo == null) {
//            return;
//        }
//        ApprovalFlowEntity flowEntity = approvalFlowDao.findByApprovalFlowNum(approvalNum);
//        if (flowEntity == null) {
//            logger.error("没有查询到审批流编号为[{}]的审批流信息", approvalNum);
//            return;
//        }
//        UserRoleSetEntity roleSetEntity = userRoleSetDao.findByRoleCode(userInfo.getRoleId());
//        if (roleSetEntity == null) {
//            logger.error("没有查询到角色编码为[{}]的角色信息", userInfo.getRoleId());
//            return;
//        }
//        UserRoleSetEntity sendRole = userRoleSetDao.findByRoleCode(sendUserInfo.getRoleId());
//        if (sendRole == null) {
//            logger.error("没有查询到角色编码为[{}]的角色信息", sendUserInfo.getRoleId());
//            return;
//        }
//        ApprovalFlowPushMsg pushMsgEntity = new ApprovalFlowPushMsgEntity();
//        pushMsgEntity.setApprovalNum(approvalFlowCreateLogNum);
//        pushMsgEntity.setCreateDate(new Date());
//        pushMsgEntity.setH5Link(flowEntity.getH5Link());
//        pushMsgEntity.setProjectNo(projectNo);
//        //未读状态
//        pushMsgEntity.setReadState(1);
//        pushMsgEntity.setRoleName(roleSetEntity.getRoleName());
//        pushMsgEntity.setTitleName("居然装饰");
//        pushMsgEntity.setTypeName(flowEntity.getApprovalFlowName());
//        pushMsgEntity.setUserName(userInfo.getName());
//        pushMsgEntity.setUserId(userId);
//        pushMsgEntity.setReason(reason);
//        pushMsgEntity.setSendUserId(sendUserId);
//        pushMsgEntity.setSendUserName(sendUserInfo.getName());
//        pushMsgEntity.setSendUserRoleName(sendRole.getRoleName());
//        flowPushMsgDao.saveAndFlush(pushMsgEntity);
//        new Thread(() -> {
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
//        }).start();
    }


    /**
     * 更新当前要审批的节点编号
     * @param instanceNum 审批流实例编号
     * @param nodeNum 审批流节点编号
     */
    private void updateCurrentNodeNum(String instanceNum, String nodeNum) {
        ApprovalFlowInstance instance = new ApprovalFlowInstance();
        instance.setCurrentNodeNum(nodeNum);

        ApprovalFlowInstanceExample example = new ApprovalFlowInstanceExample();
        example.createCriteria().andNumEqualTo(instanceNum);

        instanceMapper.updateByExampleSelective(instance, example);
    }

    private ApprovalFlowNode getNextNode(String configLogNum, String instanceNum, String nodeNum, ApprovalFlowOption option) {
        List<ApprovalFlowNode> nodes = nodeService.findByConfigLogNum(configLogNum);
        int currentNodeIndex = -1;
        for (ApprovalFlowNode node : nodes) {
            if (node.getNum().equals(nodeNum)) {
                currentNodeIndex = node.getSort();
            }
        }
        ApprovalFlowNode nextNode;
        if (option.getBackStep() == -1) {
            // 执行下一步操作
            if (currentNodeIndex + 1 < nodes.size()) {
                nextNode = nodes.get(currentNodeIndex + 1);
            } else {
                nextNode = null;
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
        if (nextNode != null) {
            nextNode = tryExecuteNextNode(instanceNum, nodes, nextNode);
        }
        return nextNode;
    }

    private ApprovalFlowNode tryExecuteNextNode(String instanceNum, List<ApprovalFlowNode> nodes, ApprovalFlowNode nextNode) {
        // 系统触发
        if (nextNode.getTriggerMode() == 0) {

        }
        return nextNode;
    }

    private String getRoleIdByProjectNoAndUserId(String projectNo, String userId) {

        return "";
    }


    /**
     * 创建审批流实例
     * @param configLogNum 审批流配置记录编号
     * @param userId 创建用户
     * @param roleId 创建用户角色
     * @param data 审批数据
     * @param projectNo 项目编号
     * @param scheduleSort 排期编号
     * @param scheduleVersion 排期版本号
     * @return 审批流实例
     */
    private ApprovalFlowInstance create(String configLogNum, String userId, String roleId, String data, String projectNo, Integer scheduleSort, Integer scheduleVersion) {
        ApprovalFlowInstance instance = new ApprovalFlowInstance();
        instance.setConfigLogNum(configLogNum);
        instance.setCreateRoleId(roleId);
        instance.setCreateUserId(userId);
        instance.setCreateTime(new Date());
        instance.setData(data);
        instance.setIsEnd(0);
        instance.setNum(UniqueCodeGenerator.AF_INSTANCE.getCode());
        instance.setProjectNum(projectNo);
        instance.setScheduleSort(scheduleSort);
        instance.setScheduleVersion(scheduleVersion);

        insert(instance);

        return instance;
    }

    private void insert(ApprovalFlowInstance instance) {
        instanceMapper.insertSelective(instance);
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
            List<ApprovalFlowNodeRole> nodeRoles = nodeRoleService.findSendRoleByNodeNum(nodeNum);
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
