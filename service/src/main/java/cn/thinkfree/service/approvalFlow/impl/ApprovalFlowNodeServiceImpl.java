package cn.thinkfree.service.approvalFlow.impl;

import cn.thinkfree.core.utils.UniqueCodeGenerator;
import cn.thinkfree.database.mapper.ApprovalFlowNodeMapper;
import cn.thinkfree.database.model.ApprovalFlowNode;
import cn.thinkfree.database.vo.ApprovalFlowNodeVo;
import cn.thinkfree.service.approvalFlow.ApprovalFlowNodeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ApprovalFlowNodeServiceImpl implements ApprovalFlowNodeService {

    @Resource
    private ApprovalFlowNodeMapper nodeMapper;

    @Override
    public List<ApprovalFlowNodeVo> findByExternalUniqueCode(String externalUniqueCode) {
        return nodeMapper.findByExternalUniqueCode(externalUniqueCode);
    }

    @Override
    public void create(String configLogNum, List<ApprovalFlowNodeVo> nodeVos) {
        if (nodeVos != null){
            for (ApprovalFlowNodeVo nodeVo : nodeVos){
                nodeVo.setExternalUniqueCode(configLogNum);
                nodeVo.setRecordUniqueCode(UniqueCodeGenerator.AF_NODE.getCode());
                nodeVo.getFormDatas();
                nodeVo.getNoticeUrls();
                nodeVo.getOptions();
                nodeVo.getNodeRoles();
                nodeVo.getTimeoutNotices();
                nodeMapper.insert(nodeVo);
            }
        }
    }
}
