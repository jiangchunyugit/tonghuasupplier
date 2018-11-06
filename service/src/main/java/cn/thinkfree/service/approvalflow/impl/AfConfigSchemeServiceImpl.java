package cn.thinkfree.service.approvalflow.impl;

import cn.thinkfree.database.mapper.AfConfigSchemeMapper;
import cn.thinkfree.database.model.AfConfigScheme;
import cn.thinkfree.database.model.AfConfigSchemeExample;
import cn.thinkfree.service.approvalflow.AfConfigSchemeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 审批流配置方案关系服务层
 *
 * @author song
 * @version 1.0
 * @date 2018/10/30 17:02
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class AfConfigSchemeServiceImpl implements AfConfigSchemeService {

    @Resource
    private AfConfigSchemeMapper configSchemeMapper;


    @Override
    public List<AfConfigScheme> findBySchemeNo(String schemeNo) {
        AfConfigSchemeExample example = new AfConfigSchemeExample();
        example.createCriteria().andSchemeNoEqualTo(schemeNo).andUsableEqualTo(1);
        return configSchemeMapper.selectByExample(example);
    }

    @Override
    public AfConfigScheme findByConfigNoAndSchemeNo(String configNo, String schemeNo) {
        AfConfigSchemeExample example = new AfConfigSchemeExample();
        example.createCriteria().andConfigNoEqualTo(configNo).andSchemeNoEqualTo(schemeNo).andUsableEqualTo(1);
        List<AfConfigScheme> configSchemes = configSchemeMapper.selectByExample(example);
        return configSchemes != null && configSchemes.size() > 0 ? configSchemes.get(0) : null;
    }

    @Override
    public void create(String configSchemeNo, String configNo, String schemeNo, String describe, String userId) {
        AfConfigScheme configScheme = new AfConfigScheme();
        configScheme.setConfigNo(configNo);
        configScheme.setConfigSchemeNo(configSchemeNo);
        configScheme.setCreateTime(new Date());
        configScheme.setCreateUserId(userId);
        configScheme.setSchemeNo(schemeNo);
        configScheme.setUsable(1);
        configScheme.setDescribe(describe);

        delete(configNo, schemeNo);

        insert(configScheme);
    }

    /**
     * 旧数据标志位usable置为0：不可用
     * @param configNo 审批流配置编号
     * @param schemeNo 方案编号
     */
    private void delete(String configNo, String schemeNo) {
        AfConfigScheme configScheme = new AfConfigScheme();
        configScheme.setUsable(0);

        AfConfigSchemeExample example = new AfConfigSchemeExample();
        example.createCriteria().andConfigNoEqualTo(configNo).andSchemeNoEqualTo(schemeNo);

        configSchemeMapper.updateByExampleSelective(configScheme, example);
    }

    private void insert(AfConfigScheme configScheme) {
        configSchemeMapper.insertSelective(configScheme);
    }
}
