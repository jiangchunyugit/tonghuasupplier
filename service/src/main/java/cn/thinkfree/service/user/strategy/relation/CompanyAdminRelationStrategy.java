package cn.thinkfree.service.user.strategy.relation;

import cn.thinkfree.core.constants.SysConstants;
import cn.thinkfree.database.constants.UserLevel;
import cn.thinkfree.database.mapper.CityBranchMapper;
import cn.thinkfree.database.mapper.CompanyInfoMapper;
import cn.thinkfree.database.model.CityBranch;
import cn.thinkfree.database.model.CityBranchExample;
import cn.thinkfree.database.model.CompanyInfo;
import cn.thinkfree.database.model.HrOrganizationEntity;
import cn.thinkfree.database.vo.UserVO;
import cn.thinkfree.service.storeinfo.StoreInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * 管理员 查看所有数据
 */
@Component
public class CompanyAdminRelationStrategy implements RelationStrategy {


    @Autowired
    StoreInfoService storeInfoService;

    /**
     * 构建关系图
     *
     * @return
     * @param userVO
     */
    @Override
    public List<String> build(UserVO userVO) {
        List<HrOrganizationEntity> hrOrganizationEntities = storeInfoService.getHrOrganizationEntity();
        return hrOrganizationEntities.stream().map(HrOrganizationEntity::getOrganizationId).collect(toList());
    }
}
