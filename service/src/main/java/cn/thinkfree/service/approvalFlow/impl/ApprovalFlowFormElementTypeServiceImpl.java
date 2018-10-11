package cn.thinkfree.service.approvalFlow.impl;


import cn.thinkfree.core.utils.UniqueCodeGenerator;
import cn.thinkfree.database.mapper.ApprovalFlowFormElementTypeMapper;
import cn.thinkfree.database.model.ApprovalFlowFormElementType;
import cn.thinkfree.database.model.ApprovalFlowFormElementTypeExample;
import cn.thinkfree.service.approvalFlow.ApprovalFlowFormElementTypeService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 审批流节点表单元素类型服务层
 */
@Service
@Transactional
public class ApprovalFlowFormElementTypeServiceImpl implements ApprovalFlowFormElementTypeService {

    @Resource
    private ApprovalFlowFormElementTypeMapper formElementTypeMapper;

    @Override
    public List<ApprovalFlowFormElementType> findAll() {
        ApprovalFlowFormElementTypeExample formElementTypeExample = new ApprovalFlowFormElementTypeExample();
        formElementTypeExample.createCriteria().andUsableEqualTo(1);
        return formElementTypeMapper.selectByExample(formElementTypeExample);
    }

    @Override
    public ApprovalFlowFormElementType findByNum(String num) {
        ApprovalFlowFormElementTypeExample formElementTypeExample = new ApprovalFlowFormElementTypeExample();
        formElementTypeExample.createCriteria().andUsableEqualTo(1).andNumEqualTo(num);
        List<ApprovalFlowFormElementType> formElementTypes = formElementTypeMapper.selectByExample(formElementTypeExample);
        return formElementTypes != null && formElementTypes.size() > 0 ? formElementTypes.get(0) : null;
    }

    @Override
    public void add(ApprovalFlowFormElementType formElementType) {
        formElementType.setId(0);
        formElementType.setUsable(1);
        formElementType.setNum(UniqueCodeGenerator.AF_FORM_ELEMENT_TYPE.getCode());
        formElementTypeMapper.insert(formElementType);
    }

    @Override
    public void save(ApprovalFlowFormElementType formElementType) {
        ApprovalFlowFormElementTypeExample formElementTypeExample = new ApprovalFlowFormElementTypeExample();
        formElementTypeExample.createCriteria().andNumEqualTo(formElementType.getNum());
        formElementTypeMapper.updateByExampleSelective(formElementType, formElementTypeExample);
    }

    @Override
    public void deleteByNum(String num) {
        ApprovalFlowFormElementType formElementType = new ApprovalFlowFormElementType();
        formElementType.setUsable(1);
        ApprovalFlowFormElementTypeExample formElementTypeExample = new ApprovalFlowFormElementTypeExample();
        formElementTypeExample.createCriteria().andNumEqualTo(num);
        formElementTypeMapper.updateByExampleSelective(formElementType, formElementTypeExample);
    }
}
