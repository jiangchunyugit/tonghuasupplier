package cn.thinkfree.service.account;

import cn.thinkfree.core.model.TreeStructure;
import cn.thinkfree.database.constants.MenuType;
import cn.thinkfree.database.mapper.SystemResourceMapper;
import cn.thinkfree.database.model.SystemResource;
import cn.thinkfree.database.vo.account.SystemResourceTreeVO;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

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

        List<SystemResource> systemResources = systemResourceMapper.selectResourceForAuthorize(id);

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

        return  Lists.newArrayList(multimap.get(MenuType.ROOT.code));
    }


}
