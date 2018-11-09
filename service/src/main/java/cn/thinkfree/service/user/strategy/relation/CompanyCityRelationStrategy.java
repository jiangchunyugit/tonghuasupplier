package cn.thinkfree.service.user.strategy.relation;

import cn.thinkfree.core.exception.ForbiddenException;
import cn.thinkfree.database.constants.UserEnabled;
import cn.thinkfree.database.mapper.BranchCompanyMapper;
import cn.thinkfree.database.mapper.CityBranchMapper;
import cn.thinkfree.database.model.BranchCompany;
import cn.thinkfree.database.model.CityBranch;
import cn.thinkfree.database.vo.UserVO;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CompanyCityRelationStrategy implements RelationStrategy {

    @Autowired
    BranchCompanyMapper branchCompanyMapper;
    @Autowired
    CityBranchMapper cityBranchMapper;

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
        CityBranch cityBranch = cityBranchMapper.selectByPrimaryKey(Integer.valueOf(userVO.getPcUserInfo().getCityBranchCompanyId()));
        userVO.setCityBranch(cityBranch);
        return Lists.newArrayList(userVO.getPcUserInfo().getCityBranchCompanyId());
    }
}
