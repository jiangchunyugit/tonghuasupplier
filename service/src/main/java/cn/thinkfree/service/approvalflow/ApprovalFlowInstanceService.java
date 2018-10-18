package cn.thinkfree.service.approvalflow;

import cn.thinkfree.database.vo.ApprovalFlowInstanceDetailVO;

/**
 * 审批流实例服务层
 * @author song
 * @date 2018/10/12 17:34
 * @version 1.0
 */
public interface ApprovalFlowInstanceService {

    /**
     * 审批流实例详情
     * @param num 审批流实例编号
     * @param configNum 审批流配置编号
     * @param projectNo 项目编号
     * @param userId 用户id
     * @param scheduleSort 项目排期编号
     * @param scheduleVersion 项目排期版本
     * @return 审批流实例详情
     */
    ApprovalFlowInstanceDetailVO detail(String num, String configNum, String projectNo, String userId, Integer scheduleSort, Integer scheduleVersion);

}
