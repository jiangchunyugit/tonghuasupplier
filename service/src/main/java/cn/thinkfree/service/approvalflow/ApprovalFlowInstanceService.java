package cn.thinkfree.service.approvalflow;

import cn.thinkfree.database.model.ApprovalFlowApprovalLog;
import cn.thinkfree.database.model.ApprovalFlowInstance;
import cn.thinkfree.database.vo.ApprovalFlowApprovalVO;
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
     * @param instanceNum 审批流实例编号
     * @param configNum 审批流配置编号
     * @param companyNo 公司编号
     * @param projectNo 项目编号
     * @param userId 用户id
     * @param scheduleSort 项目排期编号
     * @param scheduleVersion 项目排期版本
     * @return 审批流实例详情
     */
    ApprovalFlowInstanceDetailVO detail(String instanceNum, String configNum, String companyNo, String projectNo, String userId, Integer scheduleSort, Integer scheduleVersion);

    /**
     * 执行审批操作
     * @param approvalVO 审批信息
     */
    void approval(ApprovalFlowApprovalVO approvalVO);

    /**
     * 根据审批流实例编号查询审批流实例
     * @param num 审批流实例
     * @return 审批流实例
     */
    ApprovalFlowInstance findByNum(String num);

    /**
     * 根据审批流实例编号查询审批流配置名称
     * @param instanceNum 审批流实例编号
     * @return 审批流配置名称
     */
    String findConfigNameByInstanceNum(String instanceNum);

    /**
     * 清除审批记录（测试用）
     */
    void clearApprovalLog();
}
