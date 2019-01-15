package cn.thinkfree.service.companysubmit;

import cn.thinkfree.core.constants.SysConstants;
import cn.thinkfree.core.security.filter.util.SessionUserDetailsUtil;
import cn.thinkfree.core.utils.SpringBeanUtil;
import cn.thinkfree.database.mapper.AgencyContractMapper;
import cn.thinkfree.database.mapper.DealerBrandInfoMapper;
import cn.thinkfree.database.mapper.DealerCategoryMapper;
import cn.thinkfree.database.mapper.PcAuditInfoMapper;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.vo.*;
import cn.thinkfree.service.constants.AgencyConstants;
import cn.thinkfree.service.constants.AuditStatus;
import cn.thinkfree.service.constants.BrandConstants;
import cn.thinkfree.service.constants.CompanyConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class DealerBrandServiceImpl implements DealerBrandService{

    @Autowired
    DealerBrandInfoMapper dealerBrandInfoMapper;

    @Autowired
    PcAuditInfoMapper pcAuditInfoMapper;

    @Autowired
    DealerCategoryMapper dealerCategoryMapper;

    @Autowired
    AgencyContractMapper agencyContractMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> saveBrand(DealerBrandInfoVO dealerBrandInfoVO) {
        Map<String, Object> map = new HashMap<>();
        DealerBrandInfo dealerBrandInfo = new DealerBrandInfo();
        SpringBeanUtil.copy(dealerBrandInfoVO, dealerBrandInfo);

        if(StringUtils.isBlank(dealerBrandInfo.getCompanyId()) || StringUtils.isBlank(dealerBrandInfo.getAgencyCode())){
            map.put("isSuccess", false);
            map.put("msg", "公司编号不能为空");
            return map;
        }
        //判断添加的品牌品类是否是审批中，变更中，或者审核成功的
        Map<String, Object> brandMap = getBrandList(dealerBrandInfo);
        if(brandMap.get("isSuccess").toString().equals("false")){
             return brandMap;
        }
        //品牌添加
        boolean flag = saveBrandInfo(dealerBrandInfo, SysConstants.YesOrNo.YES.shortVal(), BrandConstants.AuditStatus.AUDITING.code);
        //品类添加
        List<DealerCategory> dealerCategories = dealerBrandInfoVO.getCategoryList();
        int line = 0;
        int cateLine = 0;
        if(dealerCategories!= null){
            cateLine = dealerCategories.size();
        }
        if(cateLine > 0){
            line = saveCategory(dealerBrandInfo, dealerCategories, line);
        }else {
            map.put("isSuccess", false);
            map.put("msg", "经销商品类不能为空");
        }

        if(flag && line > 0 && line == cateLine){
            map.put("isSuccess", flag);
            map.put("msg", "经销商品牌添加成功");
        }else{
            map.put("isSuccess", flag);
            map.put("msg", "经销商品牌添加失败");
            throw new RuntimeException("经销商品牌添加失败");
        }
        return map;
    }

    private int saveCategory(DealerBrandInfo dealerBrandInfo, List<DealerCategory> dealerCategories, int line) {
        Date date = new Date();
        for(DealerCategory dealerCategory: dealerCategories){
            dealerCategory.setCreateTime(date);
            dealerCategory.setUpdateTime(date);
            dealerCategory.setBrandNo(dealerBrandInfo.getBrandNo());
            dealerCategory.setBrandId(dealerBrandInfo.getId());
            int category = dealerCategoryMapper.insertSelective(dealerCategory);
            if(category > 0){
                line++;
            }
        }
        return line;
    }

    private boolean saveBrandInfo(DealerBrandInfo dealerBrandInfo, Short isValid, Integer status) {
        Date date = new Date();
        dealerBrandInfo.setCreateTime(date);
        dealerBrandInfo.setUpdateTime(date);
        dealerBrandInfo.setIsValid(Integer.valueOf(isValid));
        dealerBrandInfo.setAuditStatus(status);
        int line = dealerBrandInfoMapper.insertSelective(dealerBrandInfo);
        if(line > 0){
            return true;
        }
        return false;
    }

    @Override
    public List<AuditBrandInfoVO> showBrandDetail(BrandDetailVO brandDetailVO) {
        List<AuditBrandInfoVO> auditBrandInfoVOS = dealerBrandInfoMapper.showBrandDetail(brandDetailVO);
        return auditBrandInfoVOS;
    }

    @Override
    public List<BrandItemsVO> showBrandItems(String companyId, String agencyCode) {
        //根据公司编号，经销商编号，经销商公司有效，统计品牌，品类信息  count：审核中的品牌数量
        List<BrandItemsVO> brandItemsVOS = dealerBrandInfoMapper.showBrandItems(companyId, agencyCode);
        return brandItemsVOS;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> auditBrand(PcAuditInfoVO pcAuditInfoVO) {
        Date date = new Date();

        UserVO userVO = (UserVO) SessionUserDetailsUtil.getUserDetails();

        PcAuditInfo pcAuditInfo = new PcAuditInfo();
        SpringBeanUtil.copy(pcAuditInfoVO, pcAuditInfo);

        Map<String,Object> map = new HashMap<>();

        String brandId = pcAuditInfoVO.getBrandId();
        if(StringUtils.isBlank(brandId)){
            map.put("isSuccess", false);
            map.put("msg", "品牌编号不能为空");
            return map;
        }
        int auditFlag = 0;
        boolean brandFlag = false;
        //审核通过
        if(AuditStatus.AuditPass.shortVal().equals(pcAuditInfoVO.getAuditStatus())){
            //修改品牌表状态---->审批通过
            brandFlag = updateBrandStatus(BrandConstants.AuditStatus.AUDITSUCCESS.code, brandId);
        }else{//审核失败
            brandFlag = updateBrandStatus(BrandConstants.AuditStatus.AUDITFAIL.code, brandId);
        }

        //添加审核记录表
        auditFlag = saveAuditInfo(CompanyConstants.AuditType.BRANDAUDIT.stringVal(), pcAuditInfo, userVO, date, brandId);

        if(brandFlag && auditFlag  > 0 ){
            //todo 经销商需要调取事件同步埃森哲吗？
            map.put("isSuccess", true);
            map.put("msg", "品牌审核成功");
            return map;
        }else{
            map.put("isSuccess", false);
            map.put("msg", "品牌审核失败");
            return map;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String,Object> updateBrand(DealerBrandInfoVO dealerBrandInfoVO) {
        Map<String,Object> map = new HashMap<>();
        DealerBrandInfo dealerBrandInfo = new DealerBrandInfo();
        SpringBeanUtil.copy(dealerBrandInfoVO, dealerBrandInfo);

        //判断添加的品牌品类是否是审批中，变更中，或者审核成功的
//        Map<String, Object> brandMap = getBrandList(dealerBrandInfo);
//        if(brandMap.get("isSuccess").toString().equals("false")){
//            return brandMap;
//        }

        //品牌添加
        boolean brandFlag = saveBrandInfo(dealerBrandInfo, SysConstants.YesOrNo.YES.shortVal(), BrandConstants.AuditStatus.UPDATEING.code);
        //品类添加
        List<DealerCategory> dealerCategories = dealerBrandInfoVO.getCategoryList();
        int line = 0;
        int cateLine = 0;
        if(dealerCategories!= null){
            cateLine = dealerCategories.size();
        }
        if(cateLine > 0){
            line = saveCategory(dealerBrandInfo, dealerCategories, line);
        }
        if(brandFlag && line >0 && line == cateLine){
            map.put("isSuccess", true);
            return map;
        }
        map.put("isSuccess", false);
        map.put("msg", "品牌品类变更失败！");
        return map;
    }

    /**
     * 审批：1：如果成功：变更原来数据状态为变更成功，最新数据变更为审批通过，
     *       2：如果失败：则直接变更最新数据为变更不通过
     *       注：需要校验变更后的 品牌的品类是否减少并且减少的品类是否已签约 是则不可以审批
     * @param pcAuditInfoVO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> auditChangeBrand(PcAuditInfoVO pcAuditInfoVO) {
        Date date = new Date();
        UserVO userVO = (UserVO) SessionUserDetailsUtil.getUserDetails();

        PcAuditInfo pcAuditInfo = new PcAuditInfo();
        SpringBeanUtil.copy(pcAuditInfoVO, pcAuditInfo);

        Map<String,Object> map = new HashMap<>();

        String brandId = pcAuditInfoVO.getBrandId();
        if(StringUtils.isBlank(brandId)){
            map.put("isSuccess", false);
            map.put("msg", "品牌编号不能为空");
            return map;
        }

        //品类信息
        DealerCategoryExample dealerCategoryExample = new DealerCategoryExample();
        dealerCategoryExample.createCriteria().andBrandIdEqualTo(Integer.valueOf(pcAuditInfoVO.getBrandId()));
        List<DealerCategory> dealerCategories = dealerCategoryMapper.selectByExample(dealerCategoryExample);

        if(dealerCategories.size() <= 0){
            map.put("isSuccess", false);
            map.put("msg", "操作失败");
            return map;
        }

        //查询合同表信息
        List<String> status = new ArrayList<>(2);
        status.add(AgencyConstants.AgencyType.OVERDUE_ING.code.toString());
        status.add(AgencyConstants.AgencyType.INVALID_ING.code.toString());

        AgencyContractExample agencyContractExample = new AgencyContractExample();
        agencyContractExample.createCriteria().andCompanyIdEqualTo(pcAuditInfoVO.getCompanyId())
                .andBrandNoEqualTo(dealerCategories.get(0).getBrandNo())
                .andStatusNotIn(status);
         List<AgencyContract> agencyContracts = agencyContractMapper.selectByExample(agencyContractExample);

        Set<String> list = new HashSet<>();
        for(AgencyContract agencyContract: agencyContracts){
//            if(list.contains(agencyContract.getCategoryNo())){
//                map.put("isSuccess", false);
//                map.put("msg", "有相同品类");
//                return map;
//            }
            list.add(agencyContract.getCategoryNo());
        }

        boolean brandFlag = false;
        boolean changeFlag = false;
        //审核通过：如果成功：变更原来数据状态为变更成功，最新数据变更为审批通过
        if(AuditStatus.AuditPass.shortVal().equals(pcAuditInfoVO.getAuditStatus())){
            //如果变更品类信息里不包含合同的品类信息则不可以审批通过
            for(AgencyContract agencyContract: agencyContracts){
                if(!list.contains(agencyContract.getCategoryNo())){
                    map.put("isSuccess", false);
                    map.put("msg", agencyContract.getBrandName() + "品牌" + agencyContract.getCategoryName() + "品类已签约，请先作废此合同！");
                    return map;
                }
            }

            //1.修改：变更前数据status为变更通过   1,2顺序不可以改变，不然两条数据都刷成6：变更成功
            changeFlag = updateChangeStatus(pcAuditInfoVO);
            //2.修改品牌表变更状态---->审批通过
            brandFlag = updateBrandStatus(BrandConstants.AuditStatus.AUDITSUCCESS.code, brandId);

        }else{//审核失败
            //1.修改最新变更品牌状态为--->变更不通过  原来品牌信息不变
            brandFlag = updateBrandStatus(BrandConstants.AuditStatus.UPDATE_FAIL.code, brandId);
            changeFlag = true;
        }
        //添加审核记录表
        int auditFlag = saveAuditInfo(CompanyConstants.AuditType.BRANDAUDIT.stringVal(), pcAuditInfo, userVO, date, brandId);

        if(!changeFlag || !brandFlag || auditFlag <= 0){
            throw new RuntimeException("变更审批失败");
        }

        if(changeFlag && brandFlag && auditFlag  > 0 ){
            //todo 经销商需要调取事件同步埃森哲吗？
            map.put("isSuccess", true);
            map.put("msg", "品牌变更成功");
            return map;
        }else{
            map.put("isSuccess", false);
            map.put("msg", "品牌变更失败");
            return map;
        }
    }

    @Override
    public List<DealerBrandInfo> showSignBrand(String companyId) {
        if(StringUtils.isBlank(companyId)){
            return null;
        }
        return dealerBrandInfoMapper.showSignBrand(companyId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> editBrand(DealerBrandInfoVO dealerBrandInfoVO) {
        Map<String, Object>  map = new HashMap<>();

        DealerBrandInfo dealerBrandInfo = new DealerBrandInfo();
        SpringBeanUtil.copy(dealerBrandInfoVO, dealerBrandInfo);

        //判断添加的品牌品类是否是审批中，变更中，或者审核成功的
        Map<String, Object> brandMap = getBrandList(dealerBrandInfo);
        if(brandMap.get("isSuccess").toString().equals("false")){
            return brandMap;
        }

        //1.删除品类信息  2.更新品牌信息   3.再新增品类信息
        //1.删除品类信息
        DealerCategoryExample categoryExample = new DealerCategoryExample();
        categoryExample.createCriteria().andBrandIdEqualTo(dealerBrandInfoVO.getId());
        dealerCategoryMapper.deleteByExample(categoryExample);

        //更新品牌信息
        DealerBrandInfoExample brandInfoExample = new DealerBrandInfoExample();
        brandInfoExample.createCriteria().andIdEqualTo(dealerBrandInfoVO.getId());
        dealerBrandInfo.setUpdateTime(new Date());
        dealerBrandInfo.setAuditStatus(BrandConstants.AuditStatus.AUDITING.code);
        int brandLine = dealerBrandInfoMapper.updateByExampleSelective(dealerBrandInfo, brandInfoExample);
//        boolean flag = saveBrandInfo(dealerBrandInfo, SysConstants.YesOrNo.YES.shortVal(), BrandConstants.AuditStatus.AUDITING.code);

        //品类添加
        List<DealerCategory> dealerCategories = dealerBrandInfoVO.getCategoryList();
        int line = 0;
        int cateLine = 0;
        if(dealerCategories!= null){
            cateLine = dealerCategories.size();
        }
        if(cateLine > 0){
            line = saveCategory(dealerBrandInfo, dealerCategories, line);
        }
        if(brandLine > 0 && line > 0 && line == cateLine){
            map.put("isSuccess", true);
            map.put("msg", "重新提交成功");
            return map;
        }
        map.put("isSuccess", false);
        map.put("msg", "重新提交失败");
        return map;
    }

    @Override
    public Map<String, Object> isSignChange(String companyId, String agencyCode, String brandNo) {
        Map<String, Object> map = new HashMap<>();

        DealerBrandInfoExample example = new DealerBrandInfoExample();
        example.createCriteria().andCompanyIdEqualTo(companyId)
                .andAgencyCodeEqualTo(agencyCode)
                .andBrandNoEqualTo(brandNo)
                .andAuditStatusEqualTo(BrandConstants.AuditStatus.UPDATEING.code);
        List<DealerBrandInfo> dealerBrandInfos = dealerBrandInfoMapper.selectByExample(example);
        if(dealerBrandInfos.size() > 0){
            map.put("isSuccess", false);
            map.put("msg","品牌变更中，不可变更");
        }else{
            map.put("isSuccess", true);
            map.put("msg","没毛病");
        }
        return map;
    }

    @Override
    public List<PcAuditInfo> findAuditList(String brandId, String companyId) {
        PcAuditInfoExample example = new PcAuditInfoExample();
        example.createCriteria().andContractNumberEqualTo(brandId)
        .andAuditTypeEqualTo(CompanyConstants.AuditType.BRANDAUDIT.stringVal())
        .andAuditLevelEqualTo(CompanyConstants.auditLevel.JOINON.stringVal())
        .andCompanyIdEqualTo(companyId);
        return pcAuditInfoMapper.selectByExample(example);
    }

//    @Override
//    public List<AuditBrandInfoVO> applyBrandDetail(BrandDetailVO brandDetailVO) {
//        List<AuditBrandInfoVO> dealerBrandInfos = dealerBrandInfoMapper.applyBrandDetail(brandDetailVO);
//        return dealerBrandInfos;
//    }

    private boolean updateChangeStatus(PcAuditInfoVO pcAuditInfoVO) {
        DealerBrandInfoExample example = new DealerBrandInfoExample();
        example.createCriteria().andCompanyIdEqualTo(pcAuditInfoVO.getCompanyId())
                .andIsValidEqualTo(Integer.valueOf(SysConstants.YesOrNo.YES.shortVal()))
                .andAuditStatusEqualTo(BrandConstants.AuditStatus.AUDITSUCCESS.code)
                .andBrandNoEqualTo(pcAuditInfoVO.getBrandNo());
        DealerBrandInfo dealerBrandInfo = new DealerBrandInfo();
        dealerBrandInfo.setAuditStatus(BrandConstants.AuditStatus.UPDATE_SUCCESS.code);
        dealerBrandInfo.setUpdateTime(new Date());
        int line = dealerBrandInfoMapper.updateByExampleSelective(dealerBrandInfo, example);
        if(line > 0){
            return true;
        }
        return false;
    }

    /**
     * 查询新增品牌时候是审核成功的品牌
     * @return
     */
    public Map<String, Object> getBrandList(DealerBrandInfo dealerBrandInfo){
        Map<String, Object>  map = new HashMap<>();
        DealerBrandInfoExample example = new DealerBrandInfoExample();
        List<Integer> list = new ArrayList<>();
        list.add(BrandConstants.AuditStatus.UPDATEING.code);
        list.add(BrandConstants.AuditStatus.AUDITING.code);
        list.add(BrandConstants.AuditStatus.AUDITSUCCESS.code);

//        list.add(BrandConstants.AuditStatus.DISCARD.code);
        example.createCriteria()
                .andIsValidEqualTo(Integer.parseInt(SysConstants.YesOrNo.YES.val.toString()))
                .andCompanyIdEqualTo(dealerBrandInfo.getCompanyId())
                .andAgencyCodeEqualTo(dealerBrandInfo.getAgencyCode())
                .andBrandNoEqualTo(dealerBrandInfo.getBrandNo())
                .andAuditStatusIn(list);
        List<DealerBrandInfo> dealerBrandInfos =  dealerBrandInfoMapper.selectByExample(example);
        String[] str = dealerBrandInfo.getCategoryCode().split(",");
        for(DealerBrandInfo dealerBrandInfo1 : dealerBrandInfos){
            for(int i = 0; i < str.length; i++){
                if(dealerBrandInfo1.getCategoryCode().contains(str[i])){
                    if(BrandConstants.AuditStatus.AUDITSUCCESS.code == dealerBrandInfo1.getAuditStatus()){
                        map.put("isSuccess", false);
                        map.put("msg", "此品牌品类已通过审核");
                        return map;
                    }
                    if(BrandConstants.AuditStatus.AUDITING.code == dealerBrandInfo1.getAuditStatus() || BrandConstants.AuditStatus.UPDATEING.code == dealerBrandInfo1.getAuditStatus()){
                        map.put("isSuccess", false);
                        map.put("msg", "此品牌品类正在审核中");
                        return map;
                    }
                }
            }
        }
        map.put("isSuccess", true);
        map.put("msg", "操作成功！");
        return map;
    }

    public boolean updateBrandStatus(Integer status, String id){
        Date date = new Date();
        DealerBrandInfo dealerBrandInfo = new DealerBrandInfo();
        dealerBrandInfo.setAuditStatus(status);
        dealerBrandInfo.setId(Integer.valueOf(id));
        dealerBrandInfo.setUpdateTime(date);
        DealerBrandInfoExample example = new DealerBrandInfoExample();
        example.createCriteria().andIdEqualTo(Integer.valueOf(id));
        int flag = dealerBrandInfoMapper.updateByExampleSelective(dealerBrandInfo, example);
        if(flag > 0){
            return true;
        }else {
            return false;
        }
    }

    private int saveAuditInfo(String auditType, PcAuditInfo pcAuditInfo, UserVO userVO, Date date, String contractNumber) {

        String auditPersion = userVO == null ? "" : userVO.getName();
        String auditAccount = userVO == null ? "" : userVO.getUsername();
        //添加审核记录表
        PcAuditInfo record = new PcAuditInfo(auditType, pcAuditInfo.getAuditLevel(), auditPersion, pcAuditInfo.getAuditStatus(), date,
                pcAuditInfo.getCompanyId(), pcAuditInfo.getAuditCase(), contractNumber, date, auditAccount);
        return pcAuditInfoMapper.insertSelective(record);
    }
}
