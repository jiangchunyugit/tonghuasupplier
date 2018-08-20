package cn.thinkfree.service.user.builder;

import cn.thinkfree.database.vo.UserVO;

import java.util.List;

public class Context {

    private Strategy strategy;

    public Context(Strategy strategy){
        this.strategy = strategy;
    }

    public List<String> builder(UserVO userVO){
        return strategy.builder(userVO);
    }

}
