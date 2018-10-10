package cn.thinkfree.service.approvalFlow.impl;

import cn.thinkfree.core.utils.UniqueCodeGenerator;
import cn.thinkfree.database.mapper.ApprovalFlowNodeMapper;
import cn.thinkfree.database.model.ApprovalFlowNodeExample;
import cn.thinkfree.database.vo.ApprovalFlowNodeVo;
import cn.thinkfree.service.approvalFlow.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 审批流节点服务层
 */
@Service
@Transactional
public class ApprovalFlowNodeServiceImpl implements ApprovalFlowNodeService {

    @Resource
    private ApprovalFlowNodeMapper nodeMapper;
    @Resource
    private ApprovalFlowFormService formDataService;
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
     * @param configLogNum 节点编号
     * @return 审批流节点信息
     */
    @Override
    public List<ApprovalFlowNodeVo> findByConfigLogNum(String configLogNum) {
        return nodeMapper.findByConfigLogNum(configLogNum);
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
                nodeVo.setConfigLogNum(configLogNum);
                nodeVo.setNum(UniqueCodeGenerator.AF_NODE.getCode());

                formDataService.create(nodeVo.getNum(), nodeVo.getFormVos());
                noticeUrlService.create(nodeVo.getNum(),  nodeVo.getNoticeUrls());
                optionService.create(nodeVo.getNum(), nodeVo.getOptions());
                nodeRoleService.create(nodeVo.getNum(), nodeVo.getNodeRoles());
                timeoutNoticeService.create(nodeVo.getNum(), nodeVo.getTimeoutNotices());

                nodeMapper.insert(nodeVo);
            }
        }
    }

    @Override
    public void deleteByConfigLogNum(String configLogNum) {
        ApprovalFlowNodeExample nodeExample = new ApprovalFlowNodeExample();
        nodeExample.createCriteria().andNumEqualTo(configLogNum);
        nodeMapper.deleteByExample(nodeExample);
    }
}
