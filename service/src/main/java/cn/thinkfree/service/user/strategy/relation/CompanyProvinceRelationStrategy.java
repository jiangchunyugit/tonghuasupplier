package cn.thinkfree.service.user.strategy.relation;

import cn.thinkfree.core.constants.SysConstants;
import cn.thinkfree.core.exception.ForbiddenException;
import cn.thinkfree.database.constants.UserEnabled;
import cn.thinkfree.database.mapper.BranchCompanyMapper;
import cn.thinkfree.database.mapper.CityBranchMapper;
import cn.thinkfree.database.model.BranchCompany;
import cn.thinkfree.database.model.BranchCompanyExample;
import cn.thinkfree.database.model.CityBranch;
import cn.thinkfree.database.model.CityBranchExample;
import cn.thinkfree.database.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.security.auth.login.LoginException;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class CompanyProvinceRelationStrategy implements RelationStrategy {

    @Autowired
    CityBranchMapper cityBranchMapper;

    @Autowired
    BranchCompanyMapper branchCompanyMapper;
    /**
     * 构建关系图
     *
     * @return
     * @param userVO
     */
    @Override
    public List<String> build(UserVO userVO) {

        BranchCompany branchCompany = branchCompanyMapper.selectByPrimaryKey(Integer.valueOf(userVO.getPcUserInfo().getBranchCompanyId()));
        userVO.setBranchCompany(branchCompany);

        CityBranchExample condition = new CityBranchExample();
        condition.createCriteria().andIsDelEqualTo(SysConstants.YesOrNo.NO.shortVal())
                .andBranchCompIdEqualTo(Integer.valueOf(userVO.getPcUserInfo().getBranchCompanyId()));
        List<CityBranch> cityBranches = cityBranchMapper.selectByExample(condition);
        return cityBranches.stream().map(c->String.valueOf(c.getId()) ).collect(toList());

    }
}
