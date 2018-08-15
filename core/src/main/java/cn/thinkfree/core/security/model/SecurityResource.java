package cn.thinkfree.core.security.model;

import org.springframework.security.core.GrantedAuthority;

/**
 * Created by lenovo on 2017/2/24.
 */
public abstract class SecurityResource implements GrantedAuthority {

    abstract public String getResouce();
    abstract public String getRoleCode();



}
