package cn.thinkfree.service.approvalFlow;


import cn.thinkfree.database.vo.ApprovalFlowFormVo;

import java.util.List;

public interface ApprovalFlowFormService {

    void create(String nodeNum, List<ApprovalFlowFormVo> formVos);

}
