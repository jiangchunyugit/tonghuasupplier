package cn.thinkfree.service.approvalflow.impl;

import cn.thinkfree.database.mapper.AfInstanceRelevanceMapper;
import cn.thinkfree.database.model.AfInstanceRelevance;
import cn.thinkfree.database.model.AfInstanceRelevanceExample;
import cn.thinkfree.service.approvalflow.AfInstanceRelevanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 审批流实例关联服务层
 *
 * @author song
 * @version 1.0
 * @date 2018/12/6 18:38
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class AfInstanceRelevanceServiceImpl implements AfInstanceRelevanceService {

    @Autowired
    private AfInstanceRelevanceMapper instanceRelevanceMapper;


    @Override
    public void create(String sourceInstanceNo, String relevancyInstanceNo, String data) {
        AfInstanceRelevance instanceRelevance = new AfInstanceRelevance();
        instanceRelevance.setSourceInstanceNo(sourceInstanceNo);
        instanceRelevance.setRelevanceInstanceNo(relevancyInstanceNo);
        instanceRelevance.setData(data);

        insert(instanceRelevance);
    }

    private void insert(AfInstanceRelevance instanceRelevance) {
        instanceRelevanceMapper.insertSelective(instanceRelevance);
    }

    @Override
    public AfInstanceRelevance findByRelevanceInstanceNo(String relevancyInstanceNo) {
        AfInstanceRelevanceExample example = new AfInstanceRelevanceExample();
        example.createCriteria().andRelevanceInstanceNoEqualTo(relevancyInstanceNo);
        List<AfInstanceRelevance> instanceRelevances = instanceRelevanceMapper.selectByExample(example);
        return instanceRelevances != null && instanceRelevances.size() > 0 ? instanceRelevances.get(0) : null;
    }
}
