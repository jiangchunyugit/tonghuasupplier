package cn.thinkfree.service.platform.build.impl;

import cn.thinkfree.database.mapper.BuildPayConfigMapper;
import cn.thinkfree.database.mapper.BuildSchemeCompanyRelMapper;
import cn.thinkfree.database.mapper.BuildSchemeConfigMapper;
import cn.thinkfree.database.mapper.ConstructionOrderMapper;
import cn.thinkfree.database.model.*;
import cn.thinkfree.service.platform.build.BuildConfigService;
import cn.thinkfree.service.platform.designer.DesignDispatchService;
import cn.thinkfree.service.platform.designer.DesignerService;
import cn.thinkfree.service.platform.vo.CompanySchemeVo;
import cn.thinkfree.service.platform.vo.PageVo;
import cn.thinkfree.service.utils.OrderNoUtils;
import cn.thinkfree.service.utils.ReflectUtils;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private ConstructionOrderMapper constructionOrderMapper;

    /**
     * 查询所有施工配置方案
     * @return
     */
    @Override
    public PageVo<List<BuildSchemeConfig>> allBuildScheme(String schemeNo, String schemeName, String companyId, String cityStation,
                                                          String storeNo, int isEnable, int pageSize, int pageIndex) {
        BuildSchemeConfigExample configExample = new BuildSchemeConfigExample();
        BuildSchemeConfigExample.Criteria criteria = configExample.createCriteria().andDelStateEqualTo(2);
        if(StringUtils.isNotBlank(schemeNo)){
            criteria.andSchemeNoLike("%" +schemeNo+ "%");
        }
        if(StringUtils.isNotBlank(schemeName)){
            criteria.andSchemeNameLike("%" +schemeName+ "%");
        }
        if(StringUtils.isNotBlank(companyId)){
            criteria.andCompanyIdEqualTo(companyId);
        }
        if(StringUtils.isNotBlank(cityStation)){
            criteria.andCityStationEqualTo(cityStation);
        }
        if(StringUtils.isNotBlank(storeNo)){
            criteria.andStoreNoEqualTo(storeNo);
        }
        if(isEnable == 1 || isEnable == 2){
            criteria.andIsEnableEqualTo(isEnable);
        }
        long total = schemeConfigMapper.countByExample(configExample);
        PageHelper.startPage(pageIndex, pageSize);
        List<BuildSchemeConfig> schemeConfigs = schemeConfigMapper.selectByExample(configExample);
        PageVo<List<BuildSchemeConfig>> pageVo = new PageVo<>();
        pageVo.setTotal(total);
        pageVo.setData(schemeConfigs);
        pageVo.setPageSize(pageSize);
        pageVo.setPageIndex(pageIndex);
        return pageVo;
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
    public PageVo<List<BuildPayConfig>> payConfigBySchemeNo(String schemeNo, int pageSize, int pageIndex) {
        checkScheme(schemeNo);
        BuildPayConfigExample configExample = new BuildPayConfigExample();
        configExample.createCriteria().andSchemeNoEqualTo(schemeNo).andDeleteStateEqualTo(2);
        long total = payConfigMapper.countByExample(configExample);
        PageHelper.startPage(pageIndex - 1, pageSize);
        List<BuildPayConfig> payConfigs = payConfigMapper.selectByExample(configExample);
        PageVo<List<BuildPayConfig>> pageVo = new PageVo<>();
        pageVo.setPageSize(pageSize);
        pageVo.setPageIndex(pageIndex);
        pageVo.setTotal(total);
        pageVo.setData(payConfigs);
        return pageVo;
    }

    @Override
    public void savePayConfig(String paySchemeNo, String schemeNo, String progressName, String stageNo, BigDecimal payPercentum, int time, String remark) {
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
        if(payPercentum == null || payPercentum.doubleValue() <= 0){
            throw new RuntimeException("支付百分比不能小于0");
        }
        BuildPayConfig payConfig = new BuildPayConfig();
        payConfig.setSchemeNo(schemeNo);
        payConfig.setProgressName(progressName);
        payConfig.setPayTimeOut(time);
        payConfig.setStageCode(stageNo);
        payConfig.setRemark(remark);
        payConfig.setPayPercentum(payPercentum);
        if(StringUtils.isNotBlank(paySchemeNo)){
            payConfig.setPaySchemeNo(paySchemeNo);
            payConfigMapper.updateByPrimaryKeySelective(payConfig);
        }else{
            payConfig.setCreateTime(new Date());
            payConfig.setPaySchemeNo(OrderNoUtils.getNo("BUC"));
            payConfig.setDeleteState(2);
            payConfigMapper.insertSelective(payConfig);
        }
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
        BuildSchemeCompanyRelExample relExample = new BuildSchemeCompanyRelExample();
        relExample.createCriteria().andCompanyIdEqualTo(companyId).andBuildSchemeNoEqualTo(schemeNo).andDelStateEqualTo(2);
        List<BuildSchemeCompanyRel> rels = companyRelMapper.selectByExample(relExample);
        if(!rels.isEmpty()){
            throw new RuntimeException("该方案已存在,不可添加");
        }
        BuildSchemeCompanyRel companyRel = new BuildSchemeCompanyRel();
        companyRel.setBuildSchemeNo(schemeNo);
        companyRel.setCompanyId(companyId);
        companyRel.setOptionUserId(optionUserId);
        companyRel.setOptionUserName(optionUserName);
        companyRel.setCreateTime(new Date());
        companyRel.setIsEable(2);
        companyRel.setDelState(2);
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
    public void companyDelScheme(String companyId, String optionUserId, String optionUserName, String schemeNo) {
        if(StringUtils.isBlank(companyId)){
            throw new RuntimeException("公司ID不能为空");
        }
        if(StringUtils.isBlank(optionUserId)){
            throw new RuntimeException("操作人ID不能为空");
        }
        if(StringUtils.isBlank(optionUserName)){
            throw new RuntimeException("操作人名称不能为空");
        }
        if(StringUtils.isBlank(schemeNo)){
            throw new RuntimeException("方案编号不能为空");
        }
        BuildSchemeCompanyRelExample companyRelExample = new BuildSchemeCompanyRelExample();
        companyRelExample.createCriteria().andCompanyIdEqualTo(companyId).andBuildSchemeNoEqualTo(schemeNo);
        List<BuildSchemeCompanyRel> companyRels = companyRelMapper.selectByExample(companyRelExample);
        if(companyRels.isEmpty()) {
            throw new RuntimeException("没有查询到该关联关系");
        }
        for(BuildSchemeCompanyRel companyRel : companyRels){
            if(companyRel.getIsEable() == 1){
                throw new RuntimeException("该方案正在启用中，不可删除");
            }
        }
        BuildSchemeCompanyRel schemeCompanyRel = new BuildSchemeCompanyRel();
        schemeCompanyRel.setDelState(1);
        companyRelMapper.updateByExampleSelective(schemeCompanyRel,companyRelExample);
    }

    @Override
    public String getSchemeNoByCompanyId(String companyId) {
        BuildSchemeCompanyRelExample relExample = new BuildSchemeCompanyRelExample();
        relExample.createCriteria().andCompanyIdEqualTo(companyId);
        List<BuildSchemeCompanyRel> companyRels = companyRelMapper.selectByExample(relExample);
        if(companyRels.isEmpty()){
            return null;
        }
        return companyRels.get(0).getBuildSchemeNo();
    }

    @Override
    public List<BuildSchemeConfig> queryScheme(String searchKey, String companyId, String cityStation, String storeNo) {
//        if(StringUtils.isBlank(companyId)){
//            throw new RuntimeException("companyId不能为空");
//        }
//        if(StringUtils.isBlank(cityStation)){
//            throw new RuntimeException("cityStation不能为空");
//        }
//        if(StringUtils.isBlank(storeNo)){
//            throw new RuntimeException("storeNo不能为空");
//        }
        BuildSchemeConfigExample configExample = new BuildSchemeConfigExample();
        BuildSchemeConfigExample.Criteria criteria = configExample.createCriteria();
//        criteria
//                .andCompanyIdEqualTo(companyId).andCityStationEqualTo(cityStation).andStoreNoEqualTo(storeNo)
        if(StringUtils.isNotBlank(searchKey)){
            configExample.or().andSchemeNameLike("%" + searchKey + "%").andDelStateEqualTo(2).andIsEnableEqualTo(1);
            configExample.or().andSchemeNoLike("%" + searchKey + "%").andDelStateEqualTo(2).andIsEnableEqualTo(1);
        }else{
            criteria.andDelStateEqualTo(2).andIsEnableEqualTo(1);
        }
        return schemeConfigMapper.selectByExample(configExample);
    }

    @Override
    public List<BuildPayConfig> queryPayScheme(String projectNo) {
        ConstructionOrderExample orderExample = new ConstructionOrderExample();
        orderExample.createCriteria().andProjectNoEqualTo(projectNo);
        List<ConstructionOrder> constructionOrders = constructionOrderMapper.selectByExample(orderExample);
        if(constructionOrders.isEmpty()){
            throw new RuntimeException("没有查询到该项目");
        }
        ConstructionOrder constructionOrder = constructionOrders.get(0);
        String schemeNo = constructionOrder.getSchemeNo();
        BuildPayConfigExample configExample = new BuildPayConfigExample();
        configExample.createCriteria().andSchemeNoEqualTo(schemeNo);
        return payConfigMapper.selectByExample(configExample);
    }
    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void companyEnableScheme(String companyId, String schemeNo, String optionUserId, String optionUserName) {
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

        companyRelExample = new BuildSchemeCompanyRelExample();
        companyRelExample.createCriteria().andCompanyIdEqualTo(companyId).andBuildSchemeNoEqualTo(schemeNo);
        schemeCompanyRel = new BuildSchemeCompanyRel();
        schemeCompanyRel.setIsEable(1);
        companyRelMapper.updateByExampleSelective(schemeCompanyRel,companyRelExample);
    }

    @Override
    public PageVo<List<CompanySchemeVo>> queryByCompanyId(String companyId, int pageSize, int pageIndex) {
        if(StringUtils.isBlank(companyId)){
            throw new RuntimeException("公司ID不能为空");
        }
        BuildSchemeCompanyRelExample companyRelExample = new BuildSchemeCompanyRelExample();
        companyRelExample.createCriteria().andCompanyIdEqualTo(companyId).andDelStateEqualTo(2);
        long total = companyRelMapper.countByExample(companyRelExample);
        PageHelper.startPage(pageIndex,pageSize);
        List<BuildSchemeCompanyRel> companyRels = companyRelMapper.selectByExample(companyRelExample);
        if(companyRels.isEmpty()){
            return PageVo.def(new ArrayList<>());
        }
        List<String> schemeNos = ReflectUtils.getList(companyRels,"buildSchemeNo");
        BuildSchemeConfigExample configExample = new BuildSchemeConfigExample();
        configExample.createCriteria().andSchemeNoIn(schemeNos);
        List<BuildSchemeConfig> schemeConfigs = schemeConfigMapper.selectByExample(configExample);
        Map<String,BuildSchemeConfig> schemeConfigMap = ReflectUtils.listToMap(schemeConfigs,"schemeNo");
        List<CompanySchemeVo> schemeVos = new ArrayList<>();
        for(BuildSchemeCompanyRel companyRel : companyRels){
            BuildSchemeConfig schemeConfig = schemeConfigMap.get(companyRel.getBuildSchemeNo());
            if(schemeConfig == null){
                continue;
            }
            CompanySchemeVo schemeVo = new CompanySchemeVo();
            schemeVo.setCityStation(schemeConfig.getCityStation());
            schemeVo.setCompanyId(schemeConfig.getCompanyId());
            schemeVo.setIsEnable(companyRel.getIsEable());
            schemeVo.setSchemeName(schemeConfig.getSchemeName());
            schemeVo.setSchemeNo(schemeConfig.getSchemeNo());
            schemeVo.setRemark(schemeConfig.getRemark());
            schemeVo.setStoreNo(schemeConfig.getStoreNo());
            schemeVos.add(schemeVo);
        }
        PageVo<List<CompanySchemeVo>> pageVo = new PageVo<>();
        pageVo.setData(schemeVos);
        pageVo.setTotal(total);
        pageVo.setPageIndex(pageIndex);
        pageVo.setPageSize(pageSize);
        return pageVo;
    }
}
