package cn.thinkfree.service.approvalFlow;

import cn.thinkfree.database.dto.ApprovalFlowConfigLogDTO;
import cn.thinkfree.database.model.ApprovalFlow;
import cn.thinkfree.database.vo.ApprovalFlowDetailVo;

import java.util.List;

public interface ApprovalFlowConfigService {

    ApprovalFlow findByNum(String approvalFlowNum);

    List<ApprovalFlow> list();

    void edit(ApprovalFlowConfigLogDTO configLogDTO);

}
