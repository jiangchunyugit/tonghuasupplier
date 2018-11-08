package cn.thinkfree.service.platform.employee.impl;

import cn.thinkfree.core.constants.RoleFunctionEnum;
import cn.thinkfree.database.mapper.OrderUserMapper;
import cn.thinkfree.database.model.OrderUser;
import cn.thinkfree.database.model.OrderUserExample;
import cn.thinkfree.service.platform.basics.RoleFunctionService;
import cn.thinkfree.service.platform.employee.ProjectUserService;
import cn.thinkfree.service.utils.ReflectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author xusonghui
 */
@Service
public class ProjectUserServiceImpl implements ProjectUserService {

    @Autowired
    private OrderUserMapper orderUserMapper;
    @Autowired
    private RoleFunctionService functionService;

    @Override
    public List<String> queryUserId(String projectNo, RoleFunctionEnum functionEnum) {
        List<String> roleCodes = functionService.queryRoleCodes(functionEnum);
        if(roleCodes.isEmpty()){
            return new ArrayList<>();
        }
        OrderUserExample userExample = new OrderUserExample();
        userExample.createCriteria().andProjectNoEqualTo(projectNo).andRoleCodeIn(roleCodes);
        List<OrderUser> orderUsers = orderUserMapper.selectByExample(userExample);
        return ReflectUtils.getList(orderUsers,"userId");
    }

    @Override
    public String queryUserIdOne(String projectNo, RoleFunctionEnum functionEnum) {
        List<String> strings = queryUserId(projectNo,functionEnum);
        if(strings.isEmpty()){
            return null;
        }
        return strings.get(0);
    }

    @Override
    public void addUserId(String orderNo, String projectNo, String userId, RoleFunctionEnum functionEnum) {
        String roleCode = functionService.queryRoleCode(functionEnum);
        OrderUser orderUser = new OrderUser();
        orderUser.setCreateTime(new Date());
        orderUser.setProjectNo(projectNo);
        orderUser.setUserId(userId);
        orderUser.setRoleCode(roleCode);
        orderUser.setOrderNo(orderNo);
        orderUserMapper.insertSelective(orderUser);
    }

    @Override
    public void delUserRel(String orderNo, String projectNo, String userId, RoleFunctionEnum functionEnum) {
        String roleCode = functionService.queryRoleCode(functionEnum);
        OrderUserExample userExample = new OrderUserExample();
        userExample.createCriteria().andOrderNoEqualTo(orderNo).andProjectNoEqualTo(projectNo).andUserIdEqualTo(userId).andRoleCodeEqualTo(roleCode);
        List<OrderUser> orderUsers = orderUserMapper.selectByExample(userExample);
        if(orderUsers.isEmpty()){
            throw new RuntimeException("关联关系不存在");
        }
        orderUserMapper.deleteByExample(userExample);
    }
}
