package cn.thinkfree.database.vo.account;

import cn.thinkfree.core.model.TreeStructure;
import cn.thinkfree.database.model.SystemResource;

import java.util.List;

public class SystemResourceTreeVO extends SystemResource  {

    private Integer isAuth;

    private List<SystemResource> child;

    public Integer getIsAuth() {
        return isAuth;
    }

    public void setIsAuth(Integer isAuth) {
        this.isAuth = isAuth;
    }

    public List<SystemResource> getChild() {
        return child;
    }

    public void setChild(List<SystemResource> child) {
        this.child = child;
    }



}
