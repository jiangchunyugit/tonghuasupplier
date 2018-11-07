package cn.thinkfree.service.account;

import cn.thinkfree.core.logger.AbsLogPrinter;
import cn.thinkfree.database.mapper.SystemPermissionResourceMapper;
import cn.thinkfree.database.model.SystemPermissionResource;
import cn.thinkfree.database.model.SystemPermissionResourceExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
public class PermissionResourceServiceImpl extends AbsLogPrinter implements PermissionResourceService{

    @Autowired
    SystemPermissionResourceMapper systemPermissionResourceMapper;

    /**
     * 为权限分配资源
     *
     * @param id
     * @param resources
     * @return
     */
    @Transactional
    @Override
    public String updateSystemPermissionResource(Integer id, Integer[] resources) {
        printInfoMes("权限分配,清空旧权限");
        SystemPermissionResourceExample condition = new SystemPermissionResourceExample();
        condition.createCriteria().andPermissionIdEqualTo(id);
        systemPermissionResourceMapper.deleteByExample(condition);

        printInfoMes("权限分配,写入新权限");
        if(resources == null || resources.length == 0 ){
            printInfoMes("权限分配,清空权限");
            return "操作成功!";
        }

        for(Integer rid : resources){
            SystemPermissionResource saveObj = new SystemPermissionResource();
            saveObj.setPermissionId(id);
            saveObj.setResourceId(rid);
            systemPermissionResourceMapper.insertSelective(saveObj);
        }

        return "操作成功!";
    }
}
