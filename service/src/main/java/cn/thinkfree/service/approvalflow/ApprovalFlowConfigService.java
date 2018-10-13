package cn.thinkfree.service.approvalflow;

import cn.thinkfree.database.dto.ApprovalFlowConfigLogDTO;
import cn.thinkfree.database.model.ApprovalFlowConfig;
import cn.thinkfree.database.vo.ScheduleApprovalFlowConfigVo;

import java.util.List;


public interface ApprovalFlowConfigService {

    ApprovalFlowConfig findByNum(String approvalFlowNum);

    List<ApprovalFlowConfig> list();

    void edit(ApprovalFlowConfigLogDTO configLogDTO);

    void add(ApprovalFlowConfigLogDTO configLogDTO);

    void delete(String approvalFlowNum);

    ScheduleApprovalFlowConfigVo findScheduleApprovalFlowConfigVo(String companyNum, Integer ProjectBigScheduleSort);

    void saveScheduleApprovalFlowConfigVo(String companyNum, Integer ProjectBigScheduleSort, ScheduleApprovalFlowConfigVo scheduleApprovalFlowConfigVo);

}
