package cn.thinkfree.service.user.strategy.relation;

import cn.thinkfree.database.model.StoreInfo;
import cn.thinkfree.database.vo.UserVO;
import cn.thinkfree.service.storeinfo.StoreInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CompanyCityRelationStrategy implements RelationStrategy {

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
        return storeInfoService.storeInfoListByCityId(userVO.getPcUserInfo().getCityBranchCompanyId())
                .stream()
                .map(StoreInfo::getStoreId)
                .collect(Collectors.toList());
    }
}
