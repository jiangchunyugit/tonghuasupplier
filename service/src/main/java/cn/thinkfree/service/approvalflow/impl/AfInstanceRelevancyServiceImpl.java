package cn.thinkfree.service.approvalflow.impl;

import cn.thinkfree.database.mapper.AfInstanceRelevancyMapper;
import cn.thinkfree.database.model.AfInstanceRelevancy;
import cn.thinkfree.database.model.AfInstanceRelevancyExample;
import cn.thinkfree.service.approvalflow.AfInstanceRelevancyService;
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
public class AfInstanceRelevancyServiceImpl implements AfInstanceRelevancyService {

    @Autowired
    private AfInstanceRelevancyMapper instanceRelevancyMapper;


    @Override
    public void create(String sourceInstanceNo, String relevancyInstanceNo) {
        AfInstanceRelevancy instanceRelevancy = new AfInstanceRelevancy();
        instanceRelevancy.setSourceInstanceNo(sourceInstanceNo);
        instanceRelevancy.setRelevancyInstanceNo(relevancyInstanceNo);

        insert(instanceRelevancy);
    }

    private void insert(AfInstanceRelevancy instanceRelevancy) {
        instanceRelevancyMapper.insertSelective(instanceRelevancy);
    }

    @Override
    public AfInstanceRelevancy findByRelevancyInstanceNo(String relevancyInstanceNo) {
        AfInstanceRelevancyExample example = new AfInstanceRelevancyExample();
        example.createCriteria().andRelevancyInstanceNoEqualTo(relevancyInstanceNo);
        List<AfInstanceRelevancy> instanceRelevancies = instanceRelevancyMapper.selectByExample(example);
        return instanceRelevancies != null && instanceRelevancies.size() > 0 ? instanceRelevancies.get(0) : null;
    }
}
