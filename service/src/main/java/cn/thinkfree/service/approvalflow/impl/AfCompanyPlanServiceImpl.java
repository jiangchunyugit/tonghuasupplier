package cn.thinkfree.service.approvalflow.impl;

import cn.thinkfree.database.mapper.AfCompanyPlanMapper;
import cn.thinkfree.database.model.AfCompanyPlan;
import cn.thinkfree.database.model.AfCompanyPlanExample;
import cn.thinkfree.service.approvalflow.AfCompanyPlanService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * TODO
 *
 * @author song
 * @version 1.0
 * @date 2018/10/29 10:17
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class AfCompanyPlanServiceImpl implements AfCompanyPlanService {

    @Resource
    private AfCompanyPlanMapper companyPlanMapper;


    @Override
    public List<AfCompanyPlan> findByCompanyNoAndConfigNo(String companyNo, String configNo) {
        AfCompanyPlanExample example = new AfCompanyPlanExample();
        example.createCriteria().andCompanyNoEqualTo(companyNo).andConfigNoEqualTo(companyNo).andUsableEqualTo(1);
        return companyPlanMapper.selectByExample(example);
    }
}
