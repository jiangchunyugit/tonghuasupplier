package cn.thinkfree.core.security.model;

import cn.thinkfree.core.model.BaseModel;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

/**
 * Created by lenovo on 2017/2/24.
 */
public abstract class SecurityUser extends BaseModel implements UserDetails {

    public abstract String getName();

    public abstract String getPhone();

    public abstract String getCompanyName();

    public abstract Date getCreateTime();

    public abstract Short getType();

    public abstract String getUserID();

}

