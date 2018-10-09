package cn.thinkfree.service.approvalFlow.impl;

import cn.thinkfree.database.mapper.ApprovalFlowOptionMapper;
import cn.thinkfree.database.model.ApprovalFlowOption;
import cn.thinkfree.service.approvalFlow.ApprovalFlowOptionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 审批流节点操作项服务层
 */
@Service
public class ApprovalFlowOptionServiceImpl implements ApprovalFlowOptionService {

    @Resource
    private ApprovalFlowOptionMapper optionMapper;

    /**
     * 创建审批流节点操作项
     * @param nodeNum 审批流节点编号
     * @param options 审批流节点操作项
     */
    @Override
    public void create(String nodeNum, List<ApprovalFlowOption> options) {
        for (ApprovalFlowOption option : options) {
            option.setId(0);
            option.setExternalUniqueCode(nodeNum);
            optionMapper.insert(option);
        }
    }
}
