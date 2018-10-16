package cn.thinkfree.service.approvalflow.impl;

import cn.thinkfree.database.mapper.ApprovalFlowConfigSuperMapper;
import cn.thinkfree.database.model.ApprovalFlowConfigSuper;
import cn.thinkfree.database.model.ApprovalFlowConfigSuperExample;
import cn.thinkfree.database.vo.ApprovalFlowOrderVO;
import cn.thinkfree.service.approvalflow.ApprovalFlowConfigSuperService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author song
 * @date 2018/10/16 10:54
 * @version 1.0
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class ApprovalFlowConfigSuperServiceImpl implements ApprovalFlowConfigSuperService {

    @Resource
    private ApprovalFlowConfigSuperMapper configSuperMapper;

    @Override
    public List<ApprovalFlowConfigSuper> findByConfigAlias(String configAlias) {
        ApprovalFlowConfigSuperExample example = new ApprovalFlowConfigSuperExample();
        example.createCriteria().andConfigAliasEqualTo(configAlias).andUsableEqualTo(1);
        return configSuperMapper.selectByExample(example);
    }

    @Override
    public void create(String configAlias, List<ApprovalFlowConfigSuper> configSupers) {
        deleteByConfigAlias(configAlias);
        Date createTime = new Date();
        for (ApprovalFlowConfigSuper configSuper : configSupers) {
            configSuper.setUsable(1);
            configSuper.setConfigAlias(configAlias);
            configSuper.setCreateTime(createTime);
            configSuperMapper.insertSelective(configSuper);
        }
    }

    @Override
    public void create(List<ApprovalFlowOrderVO> orderVOs) {
        deleteAll();
        Date createTime = new Date();
        if (null != orderVOs) {
            for (ApprovalFlowOrderVO orderVO : orderVOs) {
                for (ApprovalFlowConfigSuper configSuper : orderVO.getConfigSupers()) {
                    configSuper.setUsable(1);
                    configSuper.setConfigAlias(orderVO.getConfig().getAlias());
                    configSuper.setCreateTime(createTime);
                    configSuperMapper.insertSelective(configSuper);
                }
            }
        }
    }

    private void deleteAll() {
        ApprovalFlowConfigSuperExample example = new ApprovalFlowConfigSuperExample();
        ApprovalFlowConfigSuper configSuper = new ApprovalFlowConfigSuper();
        configSuper.setUsable(0);
        configSuperMapper.updateByExampleSelective(configSuper, example);
    }

    private void deleteByConfigAlias(String configAlias) {
        ApprovalFlowConfigSuperExample example = new ApprovalFlowConfigSuperExample();
        example.createCriteria().andConfigAliasEqualTo(configAlias);
        ApprovalFlowConfigSuper configSuper = new ApprovalFlowConfigSuper();
        configSuper.setUsable(0);
        configSuperMapper.updateByExampleSelective(configSuper, example);
    }

    @Override
    public List<ApprovalFlowConfigSuper> findAllUsable() {
        ApprovalFlowConfigSuperExample example = new ApprovalFlowConfigSuperExample();
        example.createCriteria().andUsableEqualTo(1);
        return configSuperMapper.selectByExample(example);
    }
}
