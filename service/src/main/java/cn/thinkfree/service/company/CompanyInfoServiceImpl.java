package cn.thinkfree.service.company;

import cn.thinkfree.core.security.filter.util.SessionUserDetailsUtil;
import cn.thinkfree.database.mapper.CompanyInfoMapper;
import cn.thinkfree.database.model.CompanyInfo;
import cn.thinkfree.database.model.CompanyInfoExample;
import cn.thinkfree.database.vo.CompanyInfoSEO;
import cn.thinkfree.database.vo.CompanyInfoVo;
import cn.thinkfree.database.vo.StaffsVO;
import cn.thinkfree.database.vo.UserVO;
import cn.thinkfree.service.utils.UserNoUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.thinkfree.database.mapper.CompanyUserSetMapper;

import java.util.Date;
import java.util.List;

@Service
public class CompanyInfoServiceImpl implements CompanyInfoService {

    @Autowired
    CompanyInfoMapper companyInfoMapper;

    @Autowired
    CompanyUserSetMapper companyUserSetMapper;
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
    @Transactional(rollbackFor = Exception.class)
    public int addCompanyInfo(CompanyInfo companyInfo) {
        UserVO userVO = (UserVO) SessionUserDetailsUtil.getUserDetails();
        companyInfo.setCompanyId(UserNoUtils.getUserNo("BD"));
        companyInfo.setCreateTime(new Date());
        companyInfo.setPhone(companyInfo.getLegalPhone());
        companyInfo.setRoleId("BD");
        /*companyInfo.setRootCompanyId(userVO.getPcUserInfo().getRootCompanyId());
        companyInfo.setParentCompanyId(userVO.getCompanyID());*/
        return companyInfoMapper.insertSelective(companyInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateCompanyInfo(CompanyInfo companyInfo) {
        CompanyInfoExample example = new CompanyInfoExample();
        example.createCriteria().andCompanyIdEqualTo(companyInfo.getCompanyId());

        return companyInfoMapper.updateByExampleSelective(companyInfo,example);
    }

    @Override
    public PageInfo<CompanyInfo> list(CompanyInfoSEO companyInfoSEO) {
        if(!StringUtils.isBlank(companyInfoSEO.getLegalName())){
            String name = companyInfoSEO.getLegalName();
            companyInfoSEO.setLegalName("%"+name+"%");
        }
        if(!StringUtils.isBlank(companyInfoSEO.getLegalPhone())){
            String phone = companyInfoSEO.getLegalPhone();
            companyInfoSEO.setLegalPhone("%"+phone+"%");
        }
        if(!StringUtils.isBlank(companyInfoSEO.getCompanyName())){
            String companyName = companyInfoSEO.getCompanyName();
            companyInfoSEO.setCompanyName("%"+companyName+"%");
        }
        PageHelper.startPage(companyInfoSEO.getPage(),companyInfoSEO.getRows());
        List<CompanyInfo> companyInfos = companyInfoMapper.selectCompanyByParam(companyInfoSEO);
        return new PageInfo<>(companyInfos);
    }
    @Override
    public PageInfo<StaffsVO> staffMessage(String companyId, Integer page, Integer rows) {

        PageHelper.startPage(page, rows);
        List<StaffsVO> companyUserSets = companyUserSetMapper.staffByCompanyID(companyId);
        return new PageInfo<>(companyUserSets);
    }

    @Override
    public CompanyInfoVo companyDetails(String companyId) {

        return companyInfoMapper.selectByCompanyId(companyId);
    }
}
