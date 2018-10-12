package cn.thinkfree.service.index;

import cn.thinkfree.core.security.model.SecurityResource;
import cn.thinkfree.database.mapper.SystemResourceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
