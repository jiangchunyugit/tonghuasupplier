package cn.thinkfree.service.user.strategy;

import cn.thinkfree.database.vo.UserVO;
import cn.thinkfree.service.user.strategy.relation.RelationStrategy;
import cn.thinkfree.service.user.strategy.build.UserBuildStrategy;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public class Context {

    private RelationStrategy relationStrategy;

    private UserBuildStrategy userBuildStrategy;

    public Context(RelationStrategy relationStrategy){
        this.relationStrategy = relationStrategy;
    }

    public Context(UserBuildStrategy userBuildStrategy){
        this.userBuildStrategy = userBuildStrategy;
    }
    public List<String> builder(UserVO userVO){
        return relationStrategy.build(userVO);
    }

    public UserDetails loadUser(String userName){
       return userBuildStrategy.build(userName);
    }
}
