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
import com.sun.tools.javac.comp.Enter;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Branch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
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

    @Override
    public int addBranchCompany(BranchCompany branchCompany) {

        branchCompany.setCreateTime(new Date());
        branchCompany.setIsDel(OneTrue.YesOrNo.NO.shortVal());
        branchCompany.setIsEnable(UserEnabled.Enabled_false.shortVal());
        branchCompany.setBranchCompanyCode(AccountHelper.createUserNo("B"));

        return branchCompanyMapper.insertSelective(branchCompany);
    }

    @Override
    public int updateBranchCompany(BranchCompany branchCompany) {
        return branchCompanyMapper.updateByPrimaryKeySelective(branchCompany);
    }

    @Override
    public BranchCompanyVO branchCompanyDetails(Integer id) {

        // 分公司信息
        BranchCompanyVO branchCompanyVO = new BranchCompanyVO();
        branchCompanyVO = branchCompanyMapper.selectBranchCompanyWithPro(id);
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
    public List<BranchCompany> branchCompanys() {

        BranchCompanyExample branchCompanyExample = new BranchCompanyExample();
        BranchCompanyExample.Criteria criteria = branchCompanyExample.createCriteria();
        criteria.andIsDelNotEqualTo(OneTrue.YesOrNo.YES.shortVal());
        return branchCompanyMapper.selectByExample(branchCompanyExample);
    }

    @Override
    public BranchCompany branchCompanyById(Integer id) {

        return branchCompanyMapper.selectByPrimaryKey(id);
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
                List<BranchCompany> branchCompanys= branchCompanyMapper.selectByExample(branchCompanyExample);
                if (branchCompanys.size() > 0 && userVO.getCityBranch() != null
                        && StringUtils.isNotBlank(userVO.getCityBranch().getCityBranchCode())) {
                    BranchCompany branchCompany = branchCompanys.get(0);
                    // 分公司名称
                    siteInfo.setEbsBranchCompany(branchCompany.getCompanyName());
                    CityBranchExample cityBranchExample = new CityBranchExample();
                    cityBranchExample.createCriteria().andCityBranchCodeEqualTo(userVO.getCityBranch().getCityBranchCode());
                    List<CityBranch> cityBranches = cityBranchMapper.selectByExample(cityBranchExample);

                    if (cityBranches.size() >0) {
                        CityBranch cityBranch = cityBranches.get(0);
                        siteInfo.setEbsCityBranch(cityBranch.getCityBranchName());
                        // 市账号
                        if (UserLevel.Company_City.code == level) {
                            siteInfo.setLegalName(cityBranch.getLegalName());
                            siteInfo.setLegalPhone(cityBranch.getLegalPhone());
                            siteInfo.setMail(cityBranch.getMail());
                            siteInfo.setMark(cityBranch.getMark());
                        }
                    }
                    // 省账号
                    if (UserLevel.Company_Province.code == level) {
                        siteInfo.setLegalName(branchCompany.getLegalName());
                        siteInfo.setLegalPhone(branchCompany.getLegalPhone());
                        siteInfo.setMail(branchCompany.getMail());
                        siteInfo.setMark(branchCompany.getMark());
                    }
                }
            }
        }
        return  siteInfo;
    }

    @Override
    public List<BranchCompany> getBranchCompanyByIdList(String provicecode ,String cityCode) {

        List<CityBranch> cityBranchList = cityBranchService.selectByProCit(provicecode!=null?Integer.valueOf(provicecode):null,cityCode!=null?Integer.valueOf(cityCode):null);
        BranchCompanyExample branchCompanyExample = new BranchCompanyExample();
        BranchCompanyExample.Criteria criteria = branchCompanyExample.createCriteria();
        if (StringUtils.isNotBlank(provicecode)) {
            criteria.andProvinceCodeEqualTo(Integer.valueOf(provicecode).shortValue());
        }
        if (cityBranchList.size()>0) {
            List<String> strings = cityBranchList.stream().map(e->e.getBranchCompanyCode()).collect(Collectors.toList());
            if (strings.size()>0) {
                criteria.andBranchCompanyCodeIn(strings);
            }
        }
        return branchCompanyMapper.selectByExample(branchCompanyExample);
    }

    @Override
    public CompanyOrganizationVO getCompanyOrganizationByUser() {

        UserVO userVO = (UserVO) SessionUserDetailsUtil.getUserDetails();
        CompanyOrganizationVO companyOrganizationVO = new CompanyOrganizationVO();
        if (null != userVO && userVO.getBranchCompany() != null) {

            // 分公司信息获取
            if (StringUtils.isNotBlank(userVO.getBranchCompany().getBranchCompanyCode())) {
                BranchCompanyExample branchCompanyExample = new BranchCompanyExample();
                branchCompanyExample.createCriteria().andBranchCompanyCodeEqualTo(userVO.getBranchCompany().getBranchCompanyCode());
                List<BranchCompany> branchCompanys= branchCompanyMapper.selectByExample(branchCompanyExample);
                if (branchCompanys.size() > 0) {
                    companyOrganizationVO.setBranchCompany(branchCompanys.get(0));
                    // 城市分站信息获取
                    if (userVO.getCityBranch() !=null && StringUtils.isNotBlank(userVO.getCityBranch().getCityBranchCode())) {

                        CityBranchExample cityBranchExample = new CityBranchExample();
                        cityBranchExample.createCriteria().andCityBranchCodeEqualTo(userVO.getCityBranch().getCityBranchCode());
                        List<CityBranch> cityBranches = cityBranchMapper.selectByExample(cityBranchExample);
                        if (cityBranches.size() >0 ) {
                            companyOrganizationVO.setCityBranch(cityBranches.get(0));

                            // 获取经营主体信息
                            companyOrganizationVO.setBusinessEntityVOList(businessEntityMapper.selectWithCityBranchCode(userVO.getCityBranch().getCityBranchCode()));
                        }
                    }

                }
            }
        }
        return companyOrganizationVO;
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
                List<StoreInfo> storeInfos = storeInfoMapper.selectByExample(storeInfoExample);
                // 门店信息
                if (storeInfos.size() >0) {
                    StoreInfo storeInfo = storeInfos.get(0);
                    enterCompanyOrganizationVO.setStoreId(companyInfo.getSiteCompanyId());
                    // 分公司
                    if (StringUtils.isNotBlank(storeInfo.getBranchCompanyCode())) {

                    }
                    // 城市分站
                    if (StringUtils.isNotBlank(storeInfo.getCityBranchCode())) {

                    }
                    // 经营主体
                    if (StringUtils.isNotBlank(storeInfo.getBusinessEntityCode())) {

                    }
                    // 门店名称
                    HrOrganizationEntityExample hrOrganizationEntityExample = new HrOrganizationEntityExample();
//                    hrOrganizationEntityExample.createCriteria()
                }
            }
        }
        return null;
    }
}
