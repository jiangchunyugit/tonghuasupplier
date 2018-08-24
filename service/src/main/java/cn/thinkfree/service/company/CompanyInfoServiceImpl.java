package cn.thinkfree.service.company;

import cn.thinkfree.database.mapper.CompanyInfoMapper;
import cn.thinkfree.database.model.CompanyInfo;
import cn.thinkfree.database.model.CompanyInfoExample;
import cn.thinkfree.database.vo.CompanyInfoSEO;
import cn.thinkfree.database.vo.UserVO;
import cn.thinkfree.service.utils.UserNoUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springfox.documentation.schema.Example;

import java.util.Date;
import java.util.List;

@Service
public class CompanyInfoServiceImpl implements CompanyInfoService {

    @Autowired
    CompanyInfoMapper companyInfoMapper;

    /**
     * 根据相关公司id查询公司信息
     * @param userVO
     * @return
     */
    @Override
    public List<CompanyInfo> selectByCompany(UserVO userVO) {
        return  companyInfoMapper.selectByCompany(userVO.getRelationMap());
    }

    @Override
    @Transactional
    public int addCompanyInfo(CompanyInfo companyInfo) {
        companyInfo.setCompanyId(UserNoUtils.getUserNo(""));
        // TODO 以后再改吧
        companyInfo.setRoleId("BD");
        companyInfo.setCreateTime(new Date());
        companyInfo.setPhone(companyInfo.getLegalPhone());
        return companyInfoMapper.insertSelective(companyInfo);
    }

    @Override
    @Transactional
    public int updateCompanyInfo(CompanyInfo companyInfo) {
        CompanyInfoExample example = new CompanyInfoExample();
        example.createCriteria().andIdEqualTo(companyInfo.getId());

        return companyInfoMapper.updateByExampleSelective(companyInfo,example);
    }

    @Override
    public PageInfo<CompanyInfo> list(CompanyInfoSEO companyInfoSEO) {
        if(null != companyInfoSEO.getLegalName() || !"".equals(companyInfoSEO.getLegalName())){
            String name = companyInfoSEO.getLegalName();
            companyInfoSEO.setLegalName("%"+name+"%");
        }
        if(null != companyInfoSEO.getLegalPhone() || !"".equals(companyInfoSEO.getLegalPhone())){
            String phone = companyInfoSEO.getLegalPhone();
            companyInfoSEO.setLegalPhone("%"+phone+"%");
        }
        PageHelper.startPage(companyInfoSEO.getPage(),companyInfoSEO.getRows());
        List<CompanyInfo> companyInfos = companyInfoMapper.selectCompanyByParam(companyInfoSEO);
        return new PageInfo<>(companyInfos);
    }
}
