package cn.thinkfree.service.approvalflow;

import cn.thinkfree.database.vo.ApprovalFlowConfigVO;
import cn.thinkfree.database.model.ApprovalFlowConfig;
import cn.thinkfree.database.vo.ApprovalFlowConfigLogVO;
import cn.thinkfree.database.vo.ScheduleApprovalFlowConfigVo;

import java.util.List;


public interface ApprovalFlowConfigService {

    ApprovalFlowConfigLogVO detail(String num);

    ApprovalFlowConfig findByNum(String approvalFlowNum);

    List<ApprovalFlowConfig> list();

    void edit(ApprovalFlowConfigVO configLogDTO);

    void add(ApprovalFlowConfigVO configLogDTO);

    void delete(String approvalFlowNum);

    /**
     * 根据公司id与节点编号获取审批流配置
     * @param companyNum 公司id
     * @param projectBigScheduleSort 节点编号
     * @return 审批流配置
     */
    ScheduleApprovalFlowConfigVo findScheduleApprovalFlowConfigVo(String companyNum, Integer projectBigScheduleSort);

    /**
     * 保存公司id、项目节点、审批流配置
     * @param companyNum 公司id
     * @param projectBigScheduleSort 项目节点
     * @param scheduleApprovalFlowConfigVo 审批流配置
     */
    void saveScheduleApprovalFlowConfigVo(String companyNum, Integer projectBigScheduleSort, ScheduleApprovalFlowConfigVo scheduleApprovalFlowConfigVo);

}
