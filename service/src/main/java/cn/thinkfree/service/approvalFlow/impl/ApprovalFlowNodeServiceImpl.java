package cn.thinkfree.service.approvalFlow.impl;

import cn.thinkfree.database.mapper.ApprovalFlowNodeMapper;
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
}
