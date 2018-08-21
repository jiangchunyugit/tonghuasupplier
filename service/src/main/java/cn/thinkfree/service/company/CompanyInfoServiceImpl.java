package cn.thinkfree.service.company;

import cn.thinkfree.database.mapper.CompanyInfoMapper;
import cn.thinkfree.database.model.CompanyInfo;
import cn.thinkfree.database.model.CompanyInfoExample;
import cn.thinkfree.database.vo.UserVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springfox.documentation.schema.Example;

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
    public PageInfo<CompanyInfo> list(CompanyInfo companyInfo) {
        if(null != companyInfo.getLegalName() || !"".equals(companyInfo.getLegalName())){
            String name = companyInfo.getLegalName();
            companyInfo.setLegalName("%"+name+"%");
        }
        if(null != companyInfo.getLegalPhone() || !"".equals(companyInfo.getLegalPhone())){
            String phone = companyInfo.getLegalPhone();
            companyInfo.setLegalPhone("%"+phone+"%");
        }
        PageHelper.startPage(0,15);
        List<CompanyInfo> companyInfos = companyInfoMapper.selectCompanyByParam(companyInfo);
        return new PageInfo<>(companyInfos);
    }
}
