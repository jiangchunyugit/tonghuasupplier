package cn.thinkfree.service.branchcompany;

import cn.thinkfree.core.security.filter.util.SessionUserDetailsUtil;
import cn.thinkfree.database.constants.OneTrue;
import cn.thinkfree.database.constants.UserEnabled;
import cn.thinkfree.database.constants.UserLevel;
import cn.thinkfree.database.mapper.*;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.vo.*;
import cn.thinkfree.service.citybranch.CityBranchService;
import cn.thinkfree.service.utils.AccountHelper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author jiangchunyu(后台)
 * @date 2018
 * @Description 分公司（省分站）
 */
@Service
public class BranchCompanyServiceImpl implements BranchCompanyService {

    @Autowired
    BranchCompanyMapper branchCompanyMapper;

    @Autowired
    CityBranchMapper cityBranchMapper;

    @Autowired
    CityBranchService cityBranchService;

    @Autowired
    BusinessEntityMapper businessEntityMapper;

    @Autowired
    CompanyInfoMapper companyInfoMapper;

    @Autowired
    StoreInfoMapper storeInfoMapper;

    @Autowired
    HrOrganizationEntityMapper hrOrganizationEntityMapper;

    @Autowired
    PcUserInfoMapper pcUserInfoMapper;

    @Autowired
    BranchCompanyProvinceMapper branchCompanyProvinceMapper;

    @Autowired
    BusinessEntityStoreMapper businessEntityStoreMapper;

    /**
     * 判断查询还是创建
     */
    private final int searchFlag = 1;

    /**
     * list非空判断
     */
    private final int flagZero = 0;

    @Override
    public boolean checkRepeat(BranchCompanyVO branchCompanyVO) {
        if (StringUtils.isNotBlank(branchCompanyVO.getPointName())) {
            BranchCompanyExample branchCompanyExample = new BranchCompanyExample();
            BranchCompanyExample.Criteria criteria = branchCompanyExample.createCriteria();
            criteria.andPointNameEqualTo(branchCompanyVO.getPointName());
            if (branchCompanyVO.getId() != null) {
                criteria.andIdNotEqualTo(branchCompanyVO.getId());
            }
            return branchCompanyMapper.countByExample(branchCompanyExample) >flagZero?true:false;
        }
        return false;
    }

    @Override
    public List<HrOrganizationEntity> ebsBranchCompanylist() {

        HrOrganizationEntityExample hrOrganizationEntityExample = new HrOrganizationEntityExample();
        // 类型type2 ：02省公司
        hrOrganizationEntityExample.createCriteria().andZbizType2EqualTo("02");
        return hrOrganizationEntityMapper.selectByExample(hrOrganizationEntityExample);
    }

    @Override
    public boolean addBranchCompany(BranchCompanyVO branchCompanyVO) {

        // 二级分站信息
        branchCompanyVO.setCreateTime(new Date());
        branchCompanyVO.setIsDel(OneTrue.YesOrNo.NO.shortVal());
        branchCompanyVO.setIsEnable(UserEnabled.Enabled_false.shortVal());
        branchCompanyVO.setBranchCompanyCode(AccountHelper.createUserNo("B"));

        // 省份字表信息增加
        this.branchCompanyProvinceInsert(branchCompanyVO);
        return branchCompanyMapper.insertSelective(branchCompanyVO)>flagZero?true:false;
    }

    /**
     * 省份循环插入
     * @param branchCompanyVO
     */
    private void branchCompanyProvinceInsert (BranchCompanyVO branchCompanyVO) {

        Optional.ofNullable(branchCompanyVO).map(BranchCompanyVO::getProvinceList).ifPresent(u->{
            u.forEach(e->{
                BranchCompanyProvince branchCompanyProvince = new BranchCompanyProvince();
                branchCompanyProvince.setBranchCompanyCode(branchCompanyVO.getBranchCompanyCode());
                branchCompanyProvince.setProvinceCode(e.getProvinceCode());
                branchCompanyProvinceMapper.insertSelective(branchCompanyProvince);
            });
        });
    }

    @Override
    public boolean updateBranchCompany(BranchCompanyVO branchCompanyVO) {

        // 删除原来省份数据
        if (branchCompanyVO != null && branchCompanyVO.getId() != null) {
            BranchCompany branchCompany = branchCompanyMapper.selectByPrimaryKey(branchCompanyVO.getId());
            if (branchCompany == null) {
                return false;
            }
            branchCompanyVO.setBranchCompanyCode(branchCompany.getBranchCompanyCode());
            Optional.ofNullable(branchCompany).map(c->c.getBranchCompanyCode()).ifPresent(d->{
                BranchCompanyProvinceExample branchCompanyProvinceExample = new BranchCompanyProvinceExample();
                branchCompanyProvinceExample.createCriteria().andBranchCompanyCodeEqualTo(d);
                if (branchCompanyProvinceMapper.countByExample(branchCompanyProvinceExample)>0) {
                    branchCompanyProvinceMapper.deleteByExample(branchCompanyProvinceExample);
                }
            });
        }
        this.branchCompanyProvinceInsert(branchCompanyVO);
        return branchCompanyMapper.updateByPrimaryKeySelective(branchCompanyVO)>flagZero?true:false;
    }

    @Override
    public boolean enableBranchCompany(BranchCompany branchCompany) {
        return branchCompanyMapper.updateByPrimaryKeySelective(branchCompany)>flagZero?true:false;
    }

    @Override
    public BranchCompanyVO branchCompanyDetails(Integer id) {

        // 分公司信息
        BranchCompanyVO branchCompanyVO = branchCompanyMapper.selectBranchCompanyWithPro(id);
        return branchCompanyVO;
    }

    @Override
    public PageInfo<BranchCompanyVO> branchCompanyList(BranchCompanySEO branchCompanySEO) {

        // 负责人姓名电话不为空拼接模糊查询条件
        if(StringUtils.isNotBlank(branchCompanySEO.getLegalName())) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("%");
            stringBuffer.append(branchCompanySEO.getLegalName());
            stringBuffer.append("%");
            branchCompanySEO.setLegalName(stringBuffer.toString());
        }
        PageHelper.startPage(branchCompanySEO.getPage(),branchCompanySEO.getRows());
        List<BranchCompanyVO> branchCompanies = branchCompanyMapper.selectBranchCompanyByParam(branchCompanySEO);
        return new PageInfo<>(branchCompanies);
    }

    @Override
    public List<BranchCompany> branchCompanies(Integer flag) {

        BranchCompanyExample branchCompanyExample = new BranchCompanyExample();
        BranchCompanyExample.Criteria criteria = branchCompanyExample.createCriteria();
        // 查询创建分公司按照启用权限（1创建，0查询）
        if (flag==searchFlag) {
            criteria.andIsEnableEqualTo(UserEnabled.Enabled_true.code.shortValue());
        }
        criteria.andIsDelNotEqualTo(OneTrue.YesOrNo.YES.shortVal());
        return branchCompanyMapper.selectByExample(branchCompanyExample);
    }

    @Override
    public List<BranchCompanyVO> addCitybranchCompany() {
        return branchCompanyMapper.selectBranchCompanyForCityBranchCreat();
    }

    @Override
    public List<CompanyRelationVO> companyRelationList() {
        return branchCompanyMapper.selectCompanyRelation();
    }

    @Override
    public SiteInfo getSiteInfo() {

        UserVO userVO = (UserVO) SessionUserDetailsUtil.getUserDetails();
        if (userVO == null && userVO.getPcUserInfo() == null && userVO.getPcUserInfo().getLevel() == null) {
            return null;
        }
        // 权限等级
        short level = userVO.getPcUserInfo().getLevel();
        SiteInfo siteInfo = new SiteInfo();

        if (userVO.getCompanyInfo() != null) {
            siteInfo.setSiteNm(userVO.getCompanyInfo().getCompanyName());
        }
        // 跟账号
        if (UserLevel.Company_Admin.code == level && userVO.getCompanyInfo() != null) {
            siteInfo.setLegalName(userVO.getCompanyInfo().getLegalName());
            siteInfo.setLegalPhone(userVO.getCompanyInfo().getLegalPhone());
        } else {
            // 分公司信息，城市分站信息
            if (userVO.getBranchCompany() != null
                    && StringUtils.isNotBlank(userVO.getBranchCompany().getBranchCompanyCode())) {
                String branchCompanyCode = userVO.getBranchCompany().getBranchCompanyCode();
                // 分公司信息
                BranchCompanyExample branchCompanyExample = new BranchCompanyExample();
                branchCompanyExample.createCriteria().andBranchCompanyCodeEqualTo(branchCompanyCode);
                List<BranchCompany> branchCompanies = branchCompanyMapper.selectByExample(branchCompanyExample);
                if (branchCompanies != null && branchCompanies.size() > flagZero ) {
                    BranchCompany branchCompany = branchCompanies.get(0);
                    // 分公司名称
                    siteInfo.setEbsBranchCompany(branchCompany.getCompanyName());
                    // 省账号
                    if (UserLevel.Company_Province.code == level) {
                        siteInfo.setLegalName(branchCompany.getLegalName());
                        siteInfo.setLegalPhone(branchCompany.getLegalPhone());
                        siteInfo.setMail(branchCompany.getMail());
                        siteInfo.setMark(branchCompany.getMark());
                    }else if (UserLevel.Company_City.code == level&& userVO.getCityBranch() != null
                            && StringUtils.isNotBlank(userVO.getCityBranch().getCityBranchCode())) {
                        // 市账号
                        CityBranchExample cityBranchExample = new CityBranchExample();
                        cityBranchExample.createCriteria().andCityBranchCodeEqualTo(userVO.getCityBranch().getCityBranchCode());
                        List<CityBranch> cityBranches = cityBranchMapper.selectByExample(cityBranchExample);
                        if (cityBranches.size() >flagZero) {
                            CityBranch cityBranch = cityBranches.get(0);
                            siteInfo.setEbsCityBranch(cityBranch.getCityBranchName());
                            siteInfo.setLegalName(cityBranch.getLegalName());
                            siteInfo.setLegalPhone(cityBranch.getLegalPhone());
                            siteInfo.setMail(cityBranch.getMail());
                            siteInfo.setMark(cityBranch.getMark());
                        }
                    }
                }
            }
        }
        return  siteInfo;
    }

    @Override
    public List<BranchCompany> getBranchCompanyByIdList() {

        // 分公司查询条件
        BranchCompanyExample branchCompanyExample = new BranchCompanyExample();
        BranchCompanyExample.Criteria criteria = branchCompanyExample.createCriteria();
        criteria.andIsEnableEqualTo(UserEnabled.Enabled_true.code.shortValue());
        criteria.andIsDelEqualTo(OneTrue.YesOrNo.NO.shortVal());
        UserVO userVO = (UserVO) SessionUserDetailsUtil.getUserDetails();
        if (userVO != null && userVO.getPcUserInfo() != null && userVO.getPcUserInfo().getLevel() != null) {
            // 权限等级
            short level = userVO.getPcUserInfo().getLevel();

            // 省账号或者市账号
            if ((UserLevel.Company_Admin.code != level)
                    && userVO.getBranchCompany() != null
                    && StringUtils.isNotBlank(userVO.getBranchCompany().getBranchCompanyCode())) {
                criteria.andBranchCompanyCodeEqualTo(userVO.getBranchCompany().getBranchCompanyCode());
            }
        }
        return branchCompanyMapper.selectByExample(branchCompanyExample);
    }

    @Override
    public List<CompanyInfo> getCompanyOrganizationByUser(String userId) {

        // 入驻公司表条件
        CompanyInfoExample companyInfoExample = new CompanyInfoExample();
        CompanyInfoExample.Criteria criteria = companyInfoExample.createCriteria();

        PcUserInfoExample userInfoExample = new PcUserInfoExample();
        userInfoExample.createCriteria().andIdEqualTo(userId);
        PcUserInfo pcUserInfo = pcUserInfoMapper.selectByPrimaryKey(userId);

        if (pcUserInfo != null) {
            StoreInfoExample storeInfoExample = new StoreInfoExample();
            StoreInfoExample.Criteria storeCriteria = storeInfoExample.createCriteria();
            // 市条件
            if (StringUtils.isNotBlank(pcUserInfo.getCityBranchCompanyId())) {
                storeCriteria.andCityBranchCodeEqualTo(pcUserInfo.getCityBranchCompanyId());
            }
            // 省条件
            else if (StringUtils.isNotBlank(pcUserInfo.getBranchCompanyId())) {
                storeCriteria.andBranchCompanyCodeEqualTo(pcUserInfo.getBranchCompanyId());
            }
            List<StoreInfo> storeInfoList = storeInfoMapper.selectByExample(storeInfoExample);
            if (storeInfoList.size() >flagZero ) {
                List<String> storeIds = storeInfoList.stream().filter(e->StringUtils.isNotBlank(e.getStoreId())).map(e->e.getStoreId()).collect(Collectors.toList());
                if (storeIds.size() >flagZero ) {
                    criteria.andSiteCompanyIdIn(storeIds);
                }
            }
        }

        return companyInfoMapper.selectByExample(companyInfoExample);
    }

    @Override
    public EnterCompanyOrganizationVO getCompanyOrganizationByCompanyId(String companyId) {

        // 组织架构
        EnterCompanyOrganizationVO enterCompanyOrganizationVO = new EnterCompanyOrganizationVO();
        CompanyInfo companyInfo = companyInfoMapper.selectByCompanyId(companyId);

        // 入驻公司信息
        if (companyInfo != null) {
            enterCompanyOrganizationVO.setCompanyNm(companyInfo.getCompanyName());
            enterCompanyOrganizationVO.setCompanyType(companyInfo.getRoleId());

            // 门店信息，经营主体信息，门店信息，分公司信息
            if (StringUtils.isNotBlank(companyInfo.getSiteCompanyId())) {

                StoreInfoExample storeInfoExample = new StoreInfoExample();
                storeInfoExample.createCriteria().andStoreIdEqualTo(companyInfo.getSiteCompanyId());
                List<StoreInfo> storeInfoList = storeInfoMapper.selectByExample(storeInfoExample);
                // 门店信息
                if (storeInfoList.size() >flagZero) {
                    StoreInfo storeInfo = storeInfoList.get(0);
                    enterCompanyOrganizationVO.setStoreId(companyInfo.getSiteCompanyId());
                    // 分公司
                    if (StringUtils.isNotBlank(storeInfo.getBranchCompanyCode())) {
                        BranchCompanyExample branchCompanyExample = new BranchCompanyExample();
                        branchCompanyExample.createCriteria().andBranchCompanyCodeEqualTo(storeInfo.getBranchCompanyCode())
                        .andIsEnableEqualTo(UserEnabled.Enabled_true.code.shortValue());
                        List<BranchCompany> branchCompanies = branchCompanyMapper.selectByExample(branchCompanyExample);
                        if (branchCompanies.size()>flagZero) {
                            BranchCompany branchCompany = branchCompanies.get(0);
                            enterCompanyOrganizationVO.setBranchCompanyCode(branchCompany.getBranchCompanyCode());
                            enterCompanyOrganizationVO.setBranchCompanyNm(branchCompany.getCompanyName());
                        }
                    }
                    // 城市分站
                    if (StringUtils.isNotBlank(storeInfo.getCityBranchCode())) {
                        CityBranchExample cityBranchExample = new CityBranchExample();
                        cityBranchExample.createCriteria().andCityBranchCodeEqualTo(storeInfo.getCityBranchCode())
                        .andIsEnableEqualTo(UserEnabled.Enabled_true.code.shortValue());
                        List<CityBranch> cityBranches = cityBranchMapper.selectByExample(cityBranchExample);
                        if (cityBranches.size()>flagZero) {
                            CityBranch cityBranch = cityBranches.get(0);
                            enterCompanyOrganizationVO.setCityBranchCode(cityBranch.getCityBranchCode());
                            enterCompanyOrganizationVO.setCityBranchNm(cityBranch.getCityBranchName());
                        }
                    }
                    // 经营主体
                    BusinessEntityStoreExample businessEntityStoreExample = new BusinessEntityStoreExample();
                    businessEntityStoreExample.createCriteria().andStoreIdEqualTo(storeInfo.getStoreId());
                    List<BusinessEntityStore> businessEntityStores = businessEntityStoreMapper.selectByExample(businessEntityStoreExample);
                    if (businessEntityStores.size() > flagZero) {
                        BusinessEntityExample businessEntityExample = new BusinessEntityExample();
                        businessEntityExample.createCriteria().andBusinessEntityCodeEqualTo(businessEntityStores.get(0).getBusinessEntityCode())
                                .andIsEnableEqualTo(UserEnabled.Enabled_true.code.shortValue());
                        List<BusinessEntity> businessEntices = businessEntityMapper.selectByExample(businessEntityExample);
                        if (businessEntices.size()>flagZero) {
                            BusinessEntity businessEntity = businessEntices.get(0);
                            enterCompanyOrganizationVO.setBusinessEntityCode(businessEntity.getBusinessEntityCode());
                            enterCompanyOrganizationVO.setBusinessEntityNm(businessEntity.getEntityName());
                        }
                    }
                    // 门店名称
                    HrOrganizationEntityExample hrOrganizationEntityExample = new HrOrganizationEntityExample();
                    hrOrganizationEntityExample.createCriteria().andOrganizationIdEqualTo(companyInfo.getSiteCompanyId());
                    List<HrOrganizationEntity> hrOrganizationEntities = hrOrganizationEntityMapper.selectByExample(hrOrganizationEntityExample);
                    if (hrOrganizationEntities.size() > flagZero) {
                        HrOrganizationEntity hrOrganizationEntity = hrOrganizationEntities.get(0);
                        enterCompanyOrganizationVO.setStoreNm(hrOrganizationEntity.getOrganizationText());
                    }
                }
            }
        }
        return enterCompanyOrganizationVO;
    }
}
