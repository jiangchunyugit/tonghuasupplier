package cn.thinkfree.service.user.strategy.relation;

import cn.thinkfree.core.constants.SysConstants;
import cn.thinkfree.core.exception.ForbiddenException;
import cn.thinkfree.database.constants.UserEnabled;
import cn.thinkfree.database.mapper.BranchCompanyMapper;
import cn.thinkfree.database.mapper.CityBranchMapper;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.vo.UserVO;
import cn.thinkfree.service.storeinfo.StoreInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.security.auth.login.LoginException;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class CompanyProvinceRelationStrategy implements RelationStrategy {

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

        // todo jiagn
         return storeInfoService.storeInfoListByCompanyId(userVO.getPcUserInfo().getBranchCompanyId())
                 .stream()
                 .map(StoreInfo::getStoreId)
                 .collect(toList());

    }
}
