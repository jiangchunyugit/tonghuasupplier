package cn.thinkfree.service.user.builder;

import cn.thinkfree.database.vo.UserVO;

import java.util.List;

public interface Strategy {

    /**
     * 构建关系图
     * @return
     * @param userVO
     */
    List<String> builder(UserVO userVO);

}
