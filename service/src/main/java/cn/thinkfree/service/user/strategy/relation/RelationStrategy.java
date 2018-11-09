package cn.thinkfree.service.user.strategy.relation;

import cn.thinkfree.database.vo.UserVO;

import java.util.List;

public interface RelationStrategy {

    /**
     * 构建关系图
     * @return
     * @param userVO
     */
     List<String> build(UserVO userVO);


}
