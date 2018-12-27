package cn.thinkfree.service.companysubmit;

import cn.thinkfree.core.constants.SysConstants;
import cn.thinkfree.core.security.filter.util.SessionUserDetailsUtil;
import cn.thinkfree.core.utils.SpringBeanUtil;
import cn.thinkfree.database.mapper.DealerBrandInfoMapper;
import cn.thinkfree.database.mapper.PcAuditInfoMapper;
import cn.thinkfree.database.model.DealerBrandInfo;
import cn.thinkfree.database.model.DealerBrandInfoExample;
import cn.thinkfree.database.model.PcAuditInfo;
import cn.thinkfree.database.vo.*;
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> saveBrand(DealerBrandInfo dealerBrandInfo) {
        Date date = new Date();
        Map<String, Object> map = new HashMap<>();
        if(StringUtils.isBlank(dealerBrandInfo.getCompanyId()) || StringUtils.isBlank(dealerBrandInfo.getAgencyCode())){
            map.put("isSuccess", false);
            map.put("msg", "公司编号不能为空");
            return map;
        }
        boolean brandflag = getBrandList(dealerBrandInfo);
        if(brandflag){
             map.put("isSuccess", false);
             map.put("msg", "此品牌品类审核已通过，请重新选择品牌品类");
             return map;
        }
        boolean flag = saveBrandInfo(dealerBrandInfo, SysConstants.YesOrNo.YES.shortVal(), BrandConstants.AuditStatus.AUDITING.code);
        if(flag){
            map.put("isSuccess", flag);
            map.put("msg", "经销商品牌添加成功");
        }else{
            map.put("isSuccess", flag);
            map.put("msg", "经销商品牌添加失败");
        }
        return map;
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
    public boolean updateBrand(DealerBrandInfo dealerBrandInfo) {
        return saveBrandInfo(dealerBrandInfo, SysConstants.YesOrNo.YES.shortVal(), BrandConstants.AuditStatus.UPDATEING.code);
    }

    /**
     * 审批：1：如果成功：变更原来数据状态为变更成功，最新数据变更为审批通过，
     *       2：如果失败：则直接变更最新数据为变更不通过
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

        boolean brandFlag = false;
        boolean changeFlag = false;
        //审核通过：如果成功：变更原来数据状态为变更成功，最新数据变更为审批通过
        if(AuditStatus.AuditPass.shortVal().equals(pcAuditInfoVO.getAuditStatus())){
            //1.修改品牌表变更状态---->审批通过
            brandFlag = updateBrandStatus(BrandConstants.AuditStatus.AUDITSUCCESS.code, brandId);
            //2.修改：变更前数据status为变更通过
            changeFlag = updateChangeStatus(pcAuditInfoVO);

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

    private boolean updateChangeStatus(PcAuditInfoVO pcAuditInfoVO) {
        DealerBrandInfoExample example = new DealerBrandInfoExample();
        example.createCriteria().andCompanyIdEqualTo(pcAuditInfoVO.getCompanyId())
                .andIsValidEqualTo(Integer.valueOf(SysConstants.YesOrNo.YES.shortVal()))
                .andAuditStatusEqualTo(BrandConstants.AuditStatus.AUDITSUCCESS.code)
                .andBrandNoEqualTo(pcAuditInfoVO.getBrandNo())
                .andCategoryNoEqualTo(pcAuditInfoVO.getCategoryNo());
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
     * 查询新增品牌时候是审核成功的品牌  true：是
     * @return
     */
    public boolean getBrandList(DealerBrandInfo dealerBrandInfo){
        DealerBrandInfoExample example = new DealerBrandInfoExample();
        List<Integer> list = new ArrayList<>();
        list.add(BrandConstants.AuditStatus.AUDITSUCCESS.code);
        list.add(BrandConstants.AuditStatus.DISCARD.code);
        example.createCriteria()
                .andIsValidEqualTo(Integer.parseInt(SysConstants.YesOrNo.YES.val.toString()))
                .andCompanyIdEqualTo(dealerBrandInfo.getCompanyId())
                .andAgencyCodeEqualTo(dealerBrandInfo.getAgencyCode())
                .andBrandNoEqualTo(dealerBrandInfo.getBrandNo())
                .andCategoryNoEqualTo(dealerBrandInfo.getCategoryNo())
                .andAuditStatusNotIn(list);
        List<DealerBrandInfo> dealerBrandInfos =  dealerBrandInfoMapper.selectByExample(example);
        if(dealerBrandInfos.size() == 1){
            return true;
        }else {
            return false;
        }
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
