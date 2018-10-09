package cn.thinkfree.service.approvalFlow.impl;

import cn.thinkfree.database.mapper.ApprovalFlowFormElementMapper;
import cn.thinkfree.database.model.ApprovalFlowFormElement;
import cn.thinkfree.service.approvalFlow.ApprovalFlowFormElementService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 审批流节点表单元素服务层
 */
@Service
public class ApprovalFlowFormElementServiceImpl implements ApprovalFlowFormElementService {

    @Resource
    private ApprovalFlowFormElementMapper formElementMapper;

    /**
     * 创建新的审批流节点表单元素
     * @param formDataNum 表单编号
     * @param formElements 表单元素信息
     */
    @Override
    public void create(String formDataNum, List<ApprovalFlowFormElement> formElements) {
        if (null != formElements) {
            for (ApprovalFlowFormElement formElement : formElements) {
                formElement.setId(0);
                formElement.setExternalUniqueCode(formDataNum);
                formElementMapper.insert(formElement);
            }
        }
    }
}
