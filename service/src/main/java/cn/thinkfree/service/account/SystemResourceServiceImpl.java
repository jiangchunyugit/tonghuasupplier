package cn.thinkfree.service.account;

import cn.thinkfree.core.constants.SysConstants;
import cn.thinkfree.database.constants.ResourceType;
import cn.thinkfree.database.mapper.SystemResourceMapper;
import cn.thinkfree.database.model.SystemResource;
import cn.thinkfree.database.model.SystemResourceExample;
import cn.thinkfree.database.vo.account.ResourceSEO;
import cn.thinkfree.database.vo.account.SystemResourceTreeVO;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class SystemResourceServiceImpl implements SystemResourceService {

    @Autowired
    SystemResourceMapper systemResourceMapper;


    /**
     * 查询当前权限的资源情况
     *
     * @param id
     * @return
     */
    @Override
    public List<SystemResource> listResourceByPermissionID(Integer id) {
        ResourceSEO condition = new ResourceSEO.Builder()
                .permissionID(id)
                .type(ResourceType.MENU.code)
                .platform(SysConstants.PlatformType.Platform.code)
                .build();
        List<SystemResource> systemResources = systemResourceMapper.selectResourceForAuthorize(condition);

        return convertTreeVO(systemResources);

    }

    /**
     * 查询当前企业角色资源状况
     *
     * @param id
     * @return
     */
    @Override
    public List<SystemResource> listResourceByEnterPriseRoleID(Integer id) {
        return systemResourceMapper.selectEnterPriseResourceForAuthorize(id);
    }

    /**
     * 获取权限下资源详情
     *
     * @param id  权限ID
     * @param rid 资源ID
     * @return
     */
    @Override
    public List<SystemResource> listResourceDetails(Integer id, Integer rid) {

        ResourceSEO condition = new ResourceSEO.Builder()
                .permissionID(id)
                .type(ResourceType.FUNCTION.code)
                .resourceID(rid)
                .platform(SysConstants.PlatformType.Platform.code)
                .build();
        List<SystemResource> systemResources = systemResourceMapper.selectResourceForAuthorize(condition);
        return systemResources;
    }


    /**
     * 转换树形结构
     * @param systemResources
     * @return
     */
    private List<SystemResource> convertTreeVO(List<SystemResource> systemResources) {
        if(systemResources.isEmpty()){
            return Collections.EMPTY_LIST;
        }

        Multimap<Integer,SystemResource> multimap = ArrayListMultimap.create(systemResources.size(),10);

        // 标记层级
        systemResources.forEach(a->{
            multimap.put(a.getPid(),a);
        });
        // 拼装树形
        multimap.forEach((a,v)->{
            ((SystemResourceTreeVO) v).setChild(((ArrayListMultimap<Integer, SystemResource>) multimap).get(v.getId()));
        });

        return  Lists.newArrayList(multimap.get(ResourceType.ROOT.code));
    }


}
