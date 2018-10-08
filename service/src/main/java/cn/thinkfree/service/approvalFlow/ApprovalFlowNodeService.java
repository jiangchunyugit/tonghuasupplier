package cn.thinkfree.service.approvalFlow;


import cn.thinkfree.database.vo.ApprovalFlowNodeVo;

import java.util.List;

public interface ApprovalFlowNodeService {

    List<ApprovalFlowNodeVo> findByExternalUniqueCode(String externalUniqueCode);
}
