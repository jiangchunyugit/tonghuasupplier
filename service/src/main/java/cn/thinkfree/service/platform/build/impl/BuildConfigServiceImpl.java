package cn.thinkfree.service.platform.build.impl;

import cn.thinkfree.database.mapper.BuildPayConfigMapper;
import cn.thinkfree.database.mapper.BuildSchemeCompanyRelMapper;
import cn.thinkfree.database.mapper.BuildSchemeConfigMapper;
import cn.thinkfree.database.model.*;
import cn.thinkfree.service.platform.build.BuildConfigService;
import cn.thinkfree.service.utils.OrderNoUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author xusonghui
 * 施工配置服务
 */
@Service
public class BuildConfigServiceImpl implements BuildConfigService {

    @Autowired
    private BuildSchemeConfigMapper schemeConfigMapper;
    @Autowired
    private BuildPayConfigMapper payConfigMapper;
    @Autowired
    private BuildSchemeCompanyRelMapper companyRelMapper;

    /**
     * 查询所有施工配置方案
     * @return
     */
    @Override
    public List<BuildSchemeConfig> allBuildScheme() {
        BuildSchemeConfigExample configExample = new BuildSchemeConfigExample();
        configExample.createCriteria().andDelStateEqualTo(2);
        return schemeConfigMapper.selectByExample(configExample);
    }

    @Override
    public String createScheme(String schemeName, String companyId, String cityStation, String storeNo, String remark) {
        if(StringUtils.isBlank(schemeName)){
            throw new RuntimeException("方案名称不能为空");
        }
        if(StringUtils.isBlank(companyId)){
            throw new RuntimeException("公司ID不能为空");
        }
        if(StringUtils.isBlank(cityStation)){
            throw new RuntimeException("城市站不能为空");
        }
        if(StringUtils.isBlank(storeNo)){
            throw new RuntimeException("门店编号不能为空");
        }
        if(StringUtils.isBlank(remark)){
            throw new RuntimeException("请输入备注");
        }
        String schemeNo = OrderNoUtils.getNo("SN");
        BuildSchemeConfig schemeConfig = new BuildSchemeConfig();
        schemeConfig.setSchemeName(schemeName);
        schemeConfig.setCompanyId(companyId);
        schemeConfig.setCreateTime(new Date());
        schemeConfig.setCityStation(cityStation);
        schemeConfig.setStoreNo(storeNo);
        schemeConfig.setIsEnable(2);
        schemeConfig.setSchemeNo(schemeNo);
        schemeConfig.setDelState(2);
        schemeConfigMapper.insertSelective(schemeConfig);
        return schemeNo;
    }

    @Override
    public void enableScheme(String schemeNo) {
        BuildSchemeConfig schemeConfig = new BuildSchemeConfig();
        schemeConfig.setIsEnable(1);
        schemeConfig.setSchemeNo(schemeNo);
        schemeConfigMapper.updateByPrimaryKeySelective(schemeConfig);
    }

    @Override
    public void delScheme(String schemeNo) {
        BuildSchemeConfig schemeConfig = new BuildSchemeConfig();
        schemeConfig.setDelState(1);
        schemeConfig.setSchemeNo(schemeNo);
        schemeConfigMapper.updateByPrimaryKeySelective(schemeConfig);
    }

    @Override
    public BuildSchemeConfig bySchemeNo(String schemeNo) {
        return checkScheme(schemeNo);
    }

    @Override
    public List<BuildPayConfig> payConfigBySchemeNo(String schemeNo) {
        checkScheme(schemeNo);
        BuildPayConfigExample configExample = new BuildPayConfigExample();
        configExample.createCriteria().andSchemeNoEqualTo(schemeNo).andDeleteStateEqualTo(2);
        return payConfigMapper.selectByExample(configExample);
    }

    @Override
    public void savePayConfig(String schemeNo, String progressName, String stageNo, int time, String remark) {
        checkScheme(schemeNo);
        if(StringUtils.isBlank(progressName)){
            throw new RuntimeException("请输入工程进度名");
        }
        if(StringUtils.isBlank(stageNo)){
            throw new RuntimeException("请选择验收阶段");
        }
        if(time <= 0){
            throw new RuntimeException("无效的超时提醒");
        }
        if(StringUtils.isBlank(remark)){
            throw new RuntimeException("请输入备注");
        }
        BuildPayConfig payConfig = new BuildPayConfig();
        payConfig.setSchemeNo(schemeNo);
        payConfig.setCreateTime(new Date());
        payConfig.setProgressName(progressName);
        payConfig.setPayTimeOut(time);
        payConfig.setStageCode(stageNo);
        payConfig.setRemark(remark);
        payConfig.setPaySchemeNo(OrderNoUtils.getNo("BUC"));
        payConfig.setDeleteState(2);
        payConfigMapper.insertSelective(payConfig);
    }

    @Override
    public void delPayConfig(String paySchemeNo) {
        BuildPayConfig payConfig = new BuildPayConfig();
        payConfig.setPaySchemeNo(paySchemeNo);
        payConfig.setDeleteState(1);
        payConfigMapper.updateByPrimaryKeySelective(payConfig);
    }

    private BuildSchemeConfig checkScheme(String schemeNo){
        BuildSchemeConfig schemeConfig = schemeConfigMapper.selectByPrimaryKey(schemeNo);
        if(schemeConfig == null){
            throw new RuntimeException("无效的方案编号");
        }
        return schemeConfig;
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void chooseScheme(String companyId, String schemeNo, String optionUserId, String optionUserName) {
        if(StringUtils.isBlank(companyId)){
            throw new RuntimeException("公司ID不能为空");
        }
        if(StringUtils.isBlank(schemeNo)){
            throw new RuntimeException("方案编号不能为空");
        }
        if(StringUtils.isBlank(optionUserId)){
            throw new RuntimeException("操作人ID不能为空");
        }
        if(StringUtils.isBlank(optionUserName)){
            throw new RuntimeException("操作人名称不能为空");
        }
        BuildSchemeCompanyRelExample companyRelExample = new BuildSchemeCompanyRelExample();
        companyRelExample.createCriteria().andCompanyIdEqualTo(companyId);
        BuildSchemeCompanyRel schemeCompanyRel = new BuildSchemeCompanyRel();
        schemeCompanyRel.setIsEable(2);
        companyRelMapper.updateByExampleSelective(schemeCompanyRel,companyRelExample);
        BuildSchemeCompanyRel companyRel = new BuildSchemeCompanyRel();
        companyRel.setBuildSchemeNo(schemeNo);
        companyRel.setCompanyId(companyId);
        companyRel.setOptionUserId(optionUserId);
        companyRel.setOptionUserName(optionUserName);
        companyRel.setCreateTime(new Date());
        companyRel.setIsEable(1);
        companyRelMapper.insertSelective(companyRel);
    }

    @Override
    public void stopScheme(String companyId, String optionUserId, String optionUserName) {
        if(StringUtils.isBlank(companyId)){
            throw new RuntimeException("公司ID不能为空");
        }
        if(StringUtils.isBlank(optionUserId)){
            throw new RuntimeException("操作人ID不能为空");
        }
        if(StringUtils.isBlank(optionUserName)){
            throw new RuntimeException("操作人名称不能为空");
        }
        BuildSchemeCompanyRelExample companyRelExample = new BuildSchemeCompanyRelExample();
        companyRelExample.createCriteria().andCompanyIdEqualTo(companyId);
        BuildSchemeCompanyRel schemeCompanyRel = new BuildSchemeCompanyRel();
        schemeCompanyRel.setIsEable(2);
        companyRelMapper.updateByExampleSelective(schemeCompanyRel,companyRelExample);
    }

    @Override
    public List<BuildSchemeConfig> queryScheme(String searchKey, String companyId, String cityStation, String storeNo) {
        BuildSchemeConfigExample configExample = new BuildSchemeConfigExample();
        BuildSchemeConfigExample.Criteria criteria = configExample.createCriteria();
        criteria.andCompanyIdEqualTo(companyId).andCityStationEqualTo(cityStation).andStoreNoEqualTo(storeNo).andDelStateEqualTo(2).andIsEnableEqualTo(1);
        if(StringUtils.isBlank(searchKey)){
            criteria.andSchemeNameLike("%" + searchKey + "%");
            criteria.andSchemeNoLike("%" + searchKey + "%");
        }
        return schemeConfigMapper.selectByExample(configExample);
    }
}
