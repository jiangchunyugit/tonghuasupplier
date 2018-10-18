package cn.thinkfree.service.approvalflow;

import cn.thinkfree.database.model.ApprovalFlowConfig;
import cn.thinkfree.database.model.ApprovalFlowConfigLog;
import cn.thinkfree.database.vo.ApprovalFlowNodeVO;

import java.util.List;


public interface ApprovalFlowConfigLogService {

    void create(ApprovalFlowConfig config, List<ApprovalFlowNodeVO> nodeVos);

    void deleteByApprovalFlowNum(String approvalFlowNum);

    ApprovalFlowConfigLog findByConfigNumAndVersion(String configNum, int version);

    ApprovalFlowConfigLog findLastVersionByApprovalFlowNum(String approvalFlowNum);

    /**
     * 根据审批流编号查询所有版本的审批流信息，并以版本号正序排序
     * @param approvalFlowNum 审批流编号
     * @return 审批流信息
     */
    List<ApprovalFlowConfigLog> findByApprovalFlowNumOrderByVersionAsc(String approvalFlowNum);

    /**
     * 根据审批流配置日志编号查询日志信息
     * @param num 审批流配置日志编号
     * @return 审批流配置日志信息
     */
    ApprovalFlowConfigLog findByNum(String num);
}
