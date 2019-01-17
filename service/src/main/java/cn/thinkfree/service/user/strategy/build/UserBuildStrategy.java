package cn.thinkfree.service.user.strategy.build;

import cn.thinkfree.core.security.model.SecurityUser;
import cn.thinkfree.database.model.SystemResource;
import com.google.common.collect.Lists;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserBuildStrategy {

    SecurityUser build(String userID);

    default List<SystemResource> defaultUserRole(){

        SystemResource defaultGetRole = new SystemResource();
        defaultGetRole.setRequestMethod("GET");
        defaultGetRole.setCode("user");
        SystemResource defaultPostRole = new SystemResource();
        defaultPostRole.setRequestMethod("POST");
        defaultPostRole.setCode("user");
        SystemResource defaultDeleteRole = new SystemResource();
        defaultDeleteRole.setRequestMethod("DELETE");
        defaultDeleteRole.setCode("user");
        SystemResource defaultPutRole = new SystemResource();
        defaultPutRole.setRequestMethod("PUT");
        defaultPutRole.setCode("user");


        List<SystemResource> groupDefaultRole = Lists.newArrayList(defaultGetRole,defaultPostRole,defaultPutRole,defaultDeleteRole);
        return groupDefaultRole;
    }

}
