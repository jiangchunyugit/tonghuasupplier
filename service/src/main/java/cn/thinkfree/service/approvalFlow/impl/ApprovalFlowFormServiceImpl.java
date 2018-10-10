package cn.thinkfree.service.approvalFlow.impl;

import cn.thinkfree.core.utils.UniqueCodeGenerator;
import cn.thinkfree.database.mapper.ApprovalFlowFormMapper;
import cn.thinkfree.database.vo.ApprovalFlowFormVo;
import cn.thinkfree.service.approvalFlow.ApprovalFlowFormService;
import cn.thinkfree.service.approvalFlow.ApprovalFlowFormElementService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 审批流节点表单服务层
 */
@Service
@Transactional
public class ApprovalFlowFormServiceImpl implements ApprovalFlowFormService {

    @Resource
    private ApprovalFlowFormMapper formMapper;
    @Resource
    private ApprovalFlowFormElementService flowFormElementService;

    /**
     * 创建新的审批流节点表单
     * @param nodeNum 节点编号
     * @param formDataVos 表单信息
     */
    @Override
    public void create(String nodeNum, List<ApprovalFlowFormVo> formDataVos) {
        if (null != formDataVos) {
            for (ApprovalFlowFormVo formDataVo : formDataVos) {
                formDataVo.setId(0);
                formDataVo.setNodeNum(nodeNum);
                formDataVo.setNum(UniqueCodeGenerator.AF_FORM.getCode());

                flowFormElementService.create(formDataVo.getNum(), formDataVo.getFormElements());

                formMapper.insert(formDataVo);
            }
        }
    }
}
