package cn.thinkfree.service.approvalFlow.impl;

import cn.thinkfree.core.utils.UniqueCodeGenerator;
import cn.thinkfree.database.mapper.ApprovalFlowNodeMapper;
import cn.thinkfree.database.model.ApprovalFlowNode;
import cn.thinkfree.database.vo.ApprovalFlowNodeVo;
import cn.thinkfree.service.approvalFlow.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 审批流节点服务层
 */
@Service
public class ApprovalFlowNodeServiceImpl implements ApprovalFlowNodeService {

    @Resource
    private ApprovalFlowNodeMapper nodeMapper;
    @Resource
    private ApprovalFlowFormDataService formDataService;
    @Resource
    private ApprovalFlowNoticeUrlService noticeUrlService;
    @Resource
    private ApprovalFlowOptionService optionService;
    @Resource
    private ApprovalFlowNodeRoleService nodeRoleService;
    @Resource
    private ApprovalFlowTimeoutNoticeService timeoutNoticeService;

    /**
     * 根据节点编号查询节点信息
     * @param externalUniqueCode 节点编号
     * @return 审批流节点信息
     */
    @Override
    public List<ApprovalFlowNodeVo> findByExternalUniqueCode(String externalUniqueCode) {
        return nodeMapper.findByExternalUniqueCode(externalUniqueCode);
    }

    /**
     * 创建审批流节点
     * @param configLogNum 审批流配置记录编号
     * @param nodeVos 审批流节点信息
     */
    @Override
    public void create(String configLogNum, List<ApprovalFlowNodeVo> nodeVos) {
        if (nodeVos != null){
            for (ApprovalFlowNodeVo nodeVo : nodeVos){
                nodeVo.setExternalUniqueCode(configLogNum);
                nodeVo.setRecordUniqueCode(UniqueCodeGenerator.AF_NODE.getCode());

                formDataService.create(nodeVo.getRecordUniqueCode(), nodeVo.getFormDataVos());
                noticeUrlService.create(nodeVo.getRecordUniqueCode(),  nodeVo.getNoticeUrls());
                optionService.create(nodeVo.getRecordUniqueCode(), nodeVo.getOptions());
                nodeRoleService.create(nodeVo.getRecordUniqueCode(), nodeVo.getNodeRoles());
                timeoutNoticeService.create(nodeVo.getRecordUniqueCode(), nodeVo.getTimeoutNotices());

                nodeMapper.insert(nodeVo);
            }
        }
    }
}
