package cn.thinkfree.service.approvalflow.impl;

import cn.thinkfree.database.mapper.RebateNodeMapper;
import cn.thinkfree.database.model.RebateNode;
import cn.thinkfree.database.model.RebateNodeExample;
import cn.thinkfree.service.approvalflow.RebateNodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 返款节点服务层
 *
 * @author song
 * @version 1.0
 * @date 2018/12/18 18:01
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class RebateNodeServiceImpl implements RebateNodeService {

    @Autowired
    private RebateNodeMapper rebateNodeMapper;

    @Override
    public List<RebateNode> findByType(Integer nodeType) {
        RebateNodeExample example = new RebateNodeExample();
        example.createCriteria().andNodeTypeEqualTo(nodeType);
        return rebateNodeMapper.selectByExample(example);
    }
}
