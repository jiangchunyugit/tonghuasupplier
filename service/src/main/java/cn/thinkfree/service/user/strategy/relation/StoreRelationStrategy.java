package cn.thinkfree.service.user.strategy.relation;

import cn.thinkfree.database.mapper.SystemUserStoreMapper;
import cn.thinkfree.database.model.SystemUserStore;
import cn.thinkfree.database.model.SystemUserStoreExample;
import cn.thinkfree.database.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * 门店构建策略
 */
@Component
public class StoreRelationStrategy  implements  RelationStrategy{

    @Autowired
    SystemUserStoreMapper systemUserStoreMapper;

    /**
     * 构建关系图
     *
     * @param userVO
     * @return
     */
    @Override
    public List<String> build(UserVO userVO) {

        SystemUserStoreExample condition = new SystemUserStoreExample();
        condition.createCriteria().andUserIdEqualTo(userVO.getUserID());
        List<SystemUserStore> stores = systemUserStoreMapper.selectByExample(condition);
        // TODO 过滤门店
        return Collections.emptyList();
    }
}
