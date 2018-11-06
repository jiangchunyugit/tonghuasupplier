package cn.thinkfree.service.branchcompany;

import cn.thinkfree.core.utils.SpringBeanUtil;
import cn.thinkfree.database.constants.OneTrue;
import cn.thinkfree.database.constants.UserEnabled;
import cn.thinkfree.database.mapper.BranchCompanyMapper;
import cn.thinkfree.database.mapper.CityBranchMapper;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.vo.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BranchCompanyServiceImpl implements BranchCompanyService {

    @Autowired
    BranchCompanyMapper branchCompanyMapper;

    @Autowired
    CityBranchMapper cityBranchMapper;

    @Override
    public int addBranchCompany(BranchCompany branchCompany) {

        if (branchCompany.getId() != null) {
            return branchCompanyMapper.updateByPrimaryKeySelective(branchCompany);
        } else {

            branchCompany.setCreateTime(new Date());
            branchCompany.setIsDel(OneTrue.YesOrNo.NO.shortVal());
            branchCompany.setIsEnable(UserEnabled.Enabled_false.shortVal());
            // todo 埃森哲获取省份信息(moke 跟ebsid一致)
            branchCompany.setProvinceCode(branchCompany.getBranchCompEbsid().shortValue());

            return branchCompanyMapper.insertSelective(branchCompany);
        }
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
    public BranchCompanyVO branchCompanyById(Integer id) {

        BranchCompanyVO branchCompanyVO = new BranchCompanyVO();
        SpringBeanUtil.copy(branchCompanyMapper.selectByPrimaryKey(id),branchCompanyVO);
        return branchCompanyVO;
    }

    @Override
    public List<CompanyRelationVO> companyRelationList() {
        return branchCompanyMapper.selectCompanyRelation();
    }
}
