package cn.thinkfree.service.approvalflow.impl;

import cn.thinkfree.database.mapper.AfConfigLogMapper;
import cn.thinkfree.database.model.AfConfig;
import cn.thinkfree.database.model.AfConfigLog;
import cn.thinkfree.service.approvalflow.AfConfigLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * TODO
 *
 * @author song
 * @version 1.0
 * @date 2018/10/25 17:27
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class AfConfigLogServiceImpl implements AfConfigLogService {

    @Resource
    private AfConfigLogMapper configLogMapper;

    @Override
    public void create(AfConfig config, String configLogNo, String createUserId) {
        AfConfigLog configLog = new AfConfigLog();
        configLog.setConfigLogNo(configLogNo);
        configLog.setConfigNo(config.getConfigNo());
        configLog.setCreateTime(new Date());
        configLog.setCreateUserId(createUserId);
        configLog.setVersion(config.getVersion());
        configLog.setName(config.getName());
        configLog.setAlias(config.getAlias());
        configLog.setDescribe(config.getDescribe());

        insert(configLog);
    }

    private void insert(AfConfigLog configLog){
        configLogMapper.insertSelective(configLog);
    }
}
