package cn.thinkfree.service.approvalflow.impl;

import cn.thinkfree.database.mapper.ApprovalFlowOptionMapper;
import cn.thinkfree.database.model.ApprovalFlowNoticeUrl;
import cn.thinkfree.database.model.ApprovalFlowOption;
import cn.thinkfree.database.model.ApprovalFlowOptionExample;
import cn.thinkfree.service.approvalflow.ApprovalFlowOptionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 审批流节点操作项服务层
 */
@Service
@Transactional
public class ApprovalFlowOptionServiceImpl implements ApprovalFlowOptionService {

    @Resource
    private ApprovalFlowOptionMapper optionMapper;


    @Override
    public void create(String nodeNum, List<ApprovalFlowOption> options) {
        for (ApprovalFlowOption option : options) {
            option.setId(null);
            option.setNodeNum(nodeNum);
            optionMapper.insertSelective(option);
        }
    }

    @Override
    public void deleteByNodeNums(List<String> nodeNums) {
        ApprovalFlowOptionExample example = new ApprovalFlowOptionExample();
        example.createCriteria().andNodeNumIn(nodeNums);
        optionMapper.deleteByExample(example);
    }
}
