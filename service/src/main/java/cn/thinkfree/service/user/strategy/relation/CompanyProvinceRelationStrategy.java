package cn.thinkfree.service.user.strategy.relation;

import cn.thinkfree.database.model.StoreInfo;
import cn.thinkfree.database.vo.UserVO;
import cn.thinkfree.service.storeinfo.StoreInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

         return storeInfoService.storeInfoListByCompanyId(userVO.getPcUserInfo().getBranchCompanyId())
                 .stream()
                 .map(StoreInfo::getStoreId)
                 .collect(toList());

    }
}
