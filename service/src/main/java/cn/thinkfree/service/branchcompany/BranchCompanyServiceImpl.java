package cn.thinkfree.service.branchcompany;

import cn.thinkfree.core.security.filter.util.SessionUserDetailsUtil;
import cn.thinkfree.database.constants.OneTrue;
import cn.thinkfree.database.constants.UserEnabled;
import cn.thinkfree.database.constants.UserLevel;
import cn.thinkfree.database.mapper.BranchCompanyMapper;
import cn.thinkfree.database.mapper.CityBranchMapper;
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

        if (userVO.getPcUserInfo() == null) {
//            return new SiteInfo();
            return null;
        }
        short level = userVO.getPcUserInfo().getLevel();
        SiteInfo siteInfo = new SiteInfo();
        Integer companyId = userVO.getBranchCompany().getId();
        if (null != companyId) {
            BranchCompany branchCompany = new BranchCompany();
            branchCompany = branchCompanyMapper.selectByPrimaryKey(companyId);
            if (null != branchCompany) {
                siteInfo.setSiteNm(branchCompany.getCompanyName());
                if (UserLevel.Company_Admin.code == level || UserLevel.Company_Province.code == level ) {
                    siteInfo.setLegalName(branchCompany.getLegalName());
                    siteInfo.setLegalPhone(branchCompany.getLegalPhone());
                    siteInfo.setMail(branchCompany.getMail());
                    siteInfo.setMark(branchCompany.getMark());
                } else {
                    Integer cityId = userVO.getCityBranch().getId();
                    if (null != cityId) {
                        CityBranch cityBranch = cityBranchMapper.selectByPrimaryKey(cityId);
                        if (cityBranch != null) {
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
}
