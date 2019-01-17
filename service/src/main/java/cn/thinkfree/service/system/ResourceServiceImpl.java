package cn.thinkfree.service.system;

import cn.thinkfree.core.security.model.SecurityResource;
import cn.thinkfree.database.constants.ResourceType;
import cn.thinkfree.database.mapper.SystemResourceMapper;
import cn.thinkfree.database.model.SystemResourceExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourceServiceImpl implements ResourceService {

    @Autowired
    SystemResourceMapper systemResourceMapper;


    @Override
    public List<? extends SecurityResource> findAllResource() {
        SystemResourceExample systemResourceExample = new SystemResourceExample();
        systemResourceExample.createCriteria().andTypeNotEqualTo(ResourceType.ROOT.code.toString());
        return systemResourceMapper.selectByExample(systemResourceExample);
    }

    @Override
    public List<String> getRoleByResourceId(Integer resourceId) {
        return null;
    }
}
