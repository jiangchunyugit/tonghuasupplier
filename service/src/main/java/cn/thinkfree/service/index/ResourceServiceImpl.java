package cn.thinkfree.service.index;

import cn.thinkfree.core.security.model.SecurityResource;
import cn.thinkfree.database.mapper.SystemResourceMapper;
import cn.thinkfree.database.model.SystemResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Service
public class ResourceServiceImpl implements ResourceService {

    @Autowired
    SystemResourceMapper systemResourceMapper;


    @Override
    public List<? extends SecurityResource> findAllResource() {
        return systemResourceMapper.selectByExample(null);
    }

    @Override
    public List<String> getRoleByResourceId(Integer resourceId) {
        return null;
    }
}
