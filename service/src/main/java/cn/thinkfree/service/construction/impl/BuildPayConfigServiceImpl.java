package cn.thinkfree.service.construction.impl;

import cn.thinkfree.database.mapper.BuildPayConfigMapper;
import cn.thinkfree.database.model.BuildPayConfig;
import cn.thinkfree.database.model.BuildPayConfigExample;
import cn.thinkfree.service.construction.BuildPayConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * 施工支付方案配置
 *
 * @author song
 * @version 1.0
 * @date 2018/12/22 19:00
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class BuildPayConfigServiceImpl implements BuildPayConfigService {

    @Autowired
    private BuildPayConfigMapper buildPayConfigMapper;


    @Override
    public List<BuildPayConfig> findBySchemeNo(String schemeNo) {
        BuildPayConfigExample example = new BuildPayConfigExample();
        example.createCriteria().andSchemeNoEqualTo(schemeNo).andDeleteStateIn(Arrays.asList(2, 3));
        return buildPayConfigMapper.selectByExample(example);
    }
}
