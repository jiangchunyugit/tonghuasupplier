package cn.thinkfree.service.approvalflow.impl;

import cn.thinkfree.database.mapper.AfInstanceMapper;
import cn.thinkfree.database.model.AfApprovalLog;
import cn.thinkfree.database.model.AfInstance;
import cn.thinkfree.database.model.AfInstanceExample;
import cn.thinkfree.service.approvalflow.AfApprovalLogService;
import cn.thinkfree.service.approvalflow.AfInstanceService;
import cn.thinkfree.service.neworder.NewOrderUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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

    @Resource
    private AfInstanceMapper instanceMapper;
    @Resource
    private AfApprovalLogService approvalLogService;
    @Resource
    private NewOrderUserService orderUserService;

    @Override
    public void start() {

    }

    @Override
    public void approval(String instanceNo, String userId, Integer option) {
        AfInstance instance = findByNo(instanceNo);
        if (instance == null) {
            // TODO
        }
        AfApprovalLog approvalLog = approvalLogService.findByNo(instance.getCurrentApprovalLogNo());
        if (approvalLog == null) {
            // TODO
        }
        String roleId = approvalLog.getRoleId();
        instance.getProjectNo();
        String recordUserId = orderUserService.findUserIdByOrderNoAndRoleId(instance.getProjectNo(), approvalLog.getRoleId());
        if (!userId.equals(recordUserId)) {
            // TODO
        }

        if (option == 1) {
            // 同意
            approvalLog.setApprovalTime(new Date());
            approvalLog.setIsApproval(1);
//            approvalLog.set

        } else {
            // 不同意

        }

    }

    private AfInstance findByNo(String instanceNo) {
        AfInstanceExample example = new AfInstanceExample();
        example.createCriteria().andInstanceNoEqualTo(instanceNo);
        List<AfInstance> instances = instanceMapper.selectByExample(example);
        return instances != null && instances.size() > 0 ? instances.get(0) : null;
    }
}
