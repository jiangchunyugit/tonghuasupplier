package cn.tonghua.service.system;

import cn.tonghua.core.security.model.SecurityResource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourceServiceImpl implements ResourceService {

    @Override
    public List<? extends SecurityResource> findAllResource() {
        return null;
    }

    @Override
    public List<String> getRoleByResourceId(Integer resourceId) {
        return null;
    }
}
