package cn.thinkfree.service.approvalFlow.impl;

import cn.thinkfree.core.utils.UniqueCodeGenerator;
import cn.thinkfree.database.mapper.ApprovalFlowFormDataMapper;
import cn.thinkfree.database.vo.ApprovalFlowFormDataVo;
import cn.thinkfree.service.approvalFlow.ApprovalFlowFormDataService;
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
public class ApprovalFlowFormDataServiceImpl implements ApprovalFlowFormDataService {

    @Resource
    private ApprovalFlowFormDataMapper formDataMapper;
    @Resource
    private ApprovalFlowFormElementService flowFormElementService;

    /**
     * 创建新的审批流节点表单
     * @param nodeNum 节点编号
     * @param formDataVos 表单信息
     */
    @Override
    public void create(String nodeNum, List<ApprovalFlowFormDataVo> formDataVos) {
        if (null != formDataVos) {
            for (ApprovalFlowFormDataVo formDataVo : formDataVos) {
                formDataVo.setId(0);
                formDataVo.setExternalUniqueCode(nodeNum);
                formDataVo.setRecordUniqueCode(UniqueCodeGenerator.AF_FORM_DATA.getCode());

                flowFormElementService.create(formDataVo.getRecordUniqueCode(), formDataVo.getFormElements());

                formDataMapper.insert(formDataVo);
            }
        }
    }
}
