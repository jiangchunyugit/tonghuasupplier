package cn.thinkfree.service.company;

import cn.thinkfree.database.mapper.CompanyInfoMapper;
import cn.thinkfree.database.model.CompanyInfo;
import cn.thinkfree.database.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
