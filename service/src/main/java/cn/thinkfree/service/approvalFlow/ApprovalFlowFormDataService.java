package cn.thinkfree.service.approvalFlow;


import cn.thinkfree.database.vo.ApprovalFlowFormDataVo;

import java.util.List;

public interface ApprovalFlowFormDataService {

    void create(String nodeNum, List<ApprovalFlowFormDataVo> formDataVos);

}
