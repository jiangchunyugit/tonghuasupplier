package cn.thinkfree.core.security.dao;

import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2017/2/24.
 */
public interface SecurityResourceDao {
    List<Map<String, Object>> findAllResource();

    List<String> getRoleByResourceId(Integer resourceId);
}
