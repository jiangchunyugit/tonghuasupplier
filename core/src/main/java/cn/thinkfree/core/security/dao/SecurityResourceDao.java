package cn.thinkfree.core.security.dao;

import cn.thinkfree.core.security.model.SecurityResource;

import java.util.List;

/**
 * Created by lenovo on 2017/2/24.
 */
public interface SecurityResourceDao {
    List<? extends SecurityResource> findAllResource();

    List<String> getRoleByResourceId(Integer resourceId);
}
