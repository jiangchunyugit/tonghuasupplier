package cn.thinkfree.service.approvalFlow;


import cn.thinkfree.database.model.ApprovalFlowFormElementType;

import java.util.List;

public interface ApprovalFlowFormElementTypeService {

    List<ApprovalFlowFormElementType> findAll();

    ApprovalFlowFormElementType findByNum(String num);

    void add(ApprovalFlowFormElementType formElementType);

    void save(ApprovalFlowFormElementType formElementType);

    void deleteByNum(String num);

}
