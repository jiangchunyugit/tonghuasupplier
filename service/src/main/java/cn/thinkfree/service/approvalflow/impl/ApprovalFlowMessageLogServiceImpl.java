package cn.thinkfree.service.approvalflow.impl;

import cn.thinkfree.core.base.MyLogger;
import cn.thinkfree.core.utils.JSONUtil;
import cn.thinkfree.database.mapper.ApprovalFlowMessageLogMapper;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.vo.ApprovalFlowMessageLogVO;
import cn.thinkfree.service.approvalflow.*;
import cn.thinkfree.service.config.UserCenterConfig;
import cn.thinkfree.service.constants.HttpLinks;
import cn.thinkfree.service.utils.HttpUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * 审批流消息记录服务层
 *
 * @author song
 * @version 1.0
 * @date 2018/10/22 17:50
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class ApprovalFlowMessageLogServiceImpl implements ApprovalFlowMessageLogService {

    private static final MyLogger LOGGER = new MyLogger(ApprovalFlowMessageLogService.class);

    @Resource
    private ApprovalFlowMessageLogMapper messageLogMapper;
    @Resource
    private ApprovalFlowInstanceService instanceService;
    @Resource
    private RoleService roleService;
    @Resource
    private UserCenterConfig userCenterConfig;

    @Override
    public void create(ApprovalFlowMessageLog messageLog) {
        messageLog.setCreateTime(new Date());
        messageLog.setReadState(0);

        messageLogMapper.insertSelective(messageLog);
    }

    @Override
    public List<ApprovalFlowMessageLogVO> findByUserId(String userId) {
        ApprovalFlowMessageLogExample example = new ApprovalFlowMessageLogExample();
        example.createCriteria().andUserIdEqualTo(userId);
        example.setOrderByClause("read_state asc, create_time desc");

        List<ApprovalFlowMessageLog> messageLogs = messageLogMapper.selectByExample(example);

        List<ApprovalFlowMessageLogVO> messageLogVOs = new ArrayList<>();
        if (messageLogs != null ) {
            for (ApprovalFlowMessageLog messageLog : messageLogs) {
                ApprovalFlowMessageLogVO messageLogVO = new ApprovalFlowMessageLogVO();
                String configName = instanceService.findConfigNameByInstanceNum(messageLog.getInstanceNum());
                Map result = getUserName(messageLog.getSendUserId(), messageLog.getSendRoleId());
                String sendUserName = (String) result.get("nickName");
                String headPortrait = (String) result.get("headPortraits");

                messageLogVO.setConfigName(configName);

                UserRoleSet role = roleService.findById(messageLog.getSendRoleId());
                if (role == null) {
                    LOGGER.error("未查询到id为[{}]的角色", messageLog.getSendRoleId());
                    throw new RuntimeException();
                }
                messageLogVO.setSendRoleName(role.getRoleName());
                messageLogVO.setSendUserName(sendUserName);
                messageLogVO.setHeadPortrait(headPortrait);
            }
        }
        LOGGER.debug("result:{}", getUserName("CC18101915471200000", "CC"));
        return messageLogVOs;
    }

    private Map getUserName(String userId, String roleId) {
        Map<String, String> requestMap = new HashMap<>(2);
        requestMap.put("userId", userId);
        requestMap.put("roleId", roleId);
        HttpUtils.HttpRespMsg httpRespMsg = HttpUtils.post(userCenterConfig.getGetUserMsgUrl(), requestMap);
        Map responseMap = JSONUtil.json2Bean(httpRespMsg.getContent(), Map.class);
        return (Map) responseMap.get("data");
    }

    @Override
    public Long countOfUnread(String userId) {
        ApprovalFlowMessageLogExample example = new ApprovalFlowMessageLogExample();
        example.createCriteria().andUserIdEqualTo(userId);
        return messageLogMapper.countByExample(example);
    }

}
