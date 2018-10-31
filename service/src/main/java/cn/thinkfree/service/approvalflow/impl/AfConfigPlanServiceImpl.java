package cn.thinkfree.service.approvalflow.impl;

import cn.thinkfree.database.mapper.AfConfigPlanMapper;
import cn.thinkfree.database.model.AfConfigPlan;
import cn.thinkfree.database.model.AfConfigPlanExample;
import cn.thinkfree.service.approvalflow.AfConfigPlanService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * TODO
 *
 * @author song
 * @version 1.0
 * @date 2018/10/30 17:02
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class AfConfigPlanServiceImpl implements AfConfigPlanService {

    @Resource
    private AfConfigPlanMapper configPlanMapper;


    @Override
    public List<AfConfigPlan> findByPlanNo(String planNo) {
        AfConfigPlanExample example = new AfConfigPlanExample();
        example.createCriteria().andPlanNoEqualTo(planNo).andUsableEqualTo(1);
        return configPlanMapper.selectByExample(example);
    }

    @Override
    public AfConfigPlan findByConfigNoAndPlanNo(String configNo, String planNo) {
        AfConfigPlanExample example = new AfConfigPlanExample();
        example.createCriteria().andConfigNoEqualTo(configNo).andPlanNoEqualTo(planNo).andUsableEqualTo(1);
        List<AfConfigPlan> configPlans = configPlanMapper.selectByExample(example);
        return configPlans != null && configPlans.size() > 0 ? configPlans.get(0) : null;
    }

    @Override
    public void create(String configPlanNo, String configNo, String planNo, String describe, String userId) {
        AfConfigPlan configPlan = new AfConfigPlan();
        configPlan.setConfigNo(configNo);
        configPlan.setConfigPlanNo(configPlanNo);
        configPlan.setCreateTime(new Date());
        configPlan.setCreateUserId(userId);
        configPlan.setPlanNo(planNo);
        configPlan.setUsable(1);
        configPlan.setDescribe(describe);

        delete(configNo, planNo);

        insert(configPlan);
    }

    private void delete(String configNo, String planNo) {
        AfConfigPlan configPlan = new AfConfigPlan();
        configPlan.setUsable(0);

        AfConfigPlanExample example = new AfConfigPlanExample();
        example.createCriteria().andConfigNoEqualTo(configNo).andPlanNoEqualTo(planNo);

        configPlanMapper.updateByExampleSelective(configPlan, example);
    }

    private void insert(AfConfigPlan configPlan) {
        configPlanMapper.insertSelective(configPlan);
    }
}
