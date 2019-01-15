package cn.thinkfree.service.platform.employee.impl;

import cn.thinkfree.core.constants.RoleFunctionEnum;
import cn.thinkfree.database.mapper.OrderUserMapper;
import cn.thinkfree.database.model.OrderUser;
import cn.thinkfree.database.model.OrderUserExample;
import cn.thinkfree.service.platform.basics.RoleFunctionService;
import cn.thinkfree.service.platform.employee.ProjectUserService;
import cn.thinkfree.service.utils.ReflectUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        List<OrderUser> orderUsers = queryOrderUser(projectNo, functionEnum);
        return ReflectUtils.getList(orderUsers, "userId");
    }

    @Override
    public List<OrderUser> queryOrderUser(String projectNo, RoleFunctionEnum functionEnum) {
        List<String> roleCodes = functionService.queryRoleCodes(functionEnum);
        if (roleCodes.isEmpty()) {
            return new ArrayList<>();
        }
        OrderUserExample userExample = new OrderUserExample();
        userExample.createCriteria().andProjectNoEqualTo(projectNo).andRoleCodeIn(roleCodes);
        List<OrderUser> orderUsers = orderUserMapper.selectByExample(userExample);
        if(orderUsers.isEmpty()){
            return new ArrayList<OrderUser>();
        }
        return orderUsers;
    }

    @Override
    public String queryUserIdOne(String projectNo, RoleFunctionEnum functionEnum) {
        List<String> strings = queryUserId(projectNo, functionEnum);
        if (strings.isEmpty()) {
            return null;
        }
        return strings.get(0);
    }

    @Override
    public OrderUser queryOrderUserOne(String projectNo, RoleFunctionEnum functionEnum) {
        List<OrderUser> orderUsers = queryOrderUser(projectNo,functionEnum);
        if (orderUsers.isEmpty()) {
            return null;
        }
        return orderUsers.get(0);
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
        orderUser.setUpdateTime(new Date());
        orderUserMapper.insertSelective(orderUser);
    }

    @Override
    public void delUserRel(String orderNo, String projectNo, String userId, RoleFunctionEnum functionEnum) {
        String roleCode = functionService.queryRoleCode(functionEnum);
        OrderUserExample userExample = new OrderUserExample();
        userExample.createCriteria().andOrderNoEqualTo(orderNo).andProjectNoEqualTo(projectNo).andUserIdEqualTo(userId).andRoleCodeEqualTo(roleCode);
        List<OrderUser> orderUsers = orderUserMapper.selectByExample(userExample);
        if (orderUsers.isEmpty()) {
            throw new RuntimeException("关联关系不存在");
        }
        orderUserMapper.deleteByExample(userExample);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void transferEmployee(String transferUserId, String beTransferUserId, String projectNo, String roleCode) {
        if(StringUtils.isBlank(transferUserId)){
            throw new RuntimeException("移交人ID不能为空");
        }
        if(StringUtils.isBlank(beTransferUserId)){
            throw new RuntimeException("被移交人ID不能为空");
        }
        if(StringUtils.isBlank(projectNo)){
            throw new RuntimeException("项目编号不能为空");
        }
        if(StringUtils.isBlank(roleCode)){
            throw new RuntimeException("角色编码不能为空");
        }
        if(transferUserId.equals(beTransferUserId)){
            throw new RuntimeException("被移交人和移交人重复");
        }
        OrderUserExample orderUserExample = new OrderUserExample();
        orderUserExample.createCriteria().andUserIdEqualTo(transferUserId).andRoleCodeEqualTo(roleCode)
                .andIsTransferEqualTo(Short.parseShort("0")).andProjectNoEqualTo(projectNo);
        List<OrderUser> orderUsers = orderUserMapper.selectByExample(orderUserExample);
        if(orderUsers.isEmpty()){
            throw new RuntimeException("没有查询到移交人和该项目的关系");
        }
        String orderNo = orderUsers.get(0).getOrderNo();
        OrderUser orderUser = new OrderUser();
        orderUser.setTransferTime(new Date());
        orderUser.setIsTransfer(Short.parseShort("1"));
        orderUser.setTransferUserId(beTransferUserId);
        orderUser.setUpdateTime(new Date());
        orderUserMapper.updateByExampleSelective(orderUser,orderUserExample);
        orderUser = new OrderUser();
        orderUser.setCreateTime(new Date());
        orderUser.setProjectNo(projectNo);
        orderUser.setUserId(beTransferUserId);
        orderUser.setRoleCode(roleCode);
        orderUser.setOrderNo(orderNo);
        orderUser.setUpdateTime(new Date());
        orderUserMapper.insertSelective(orderUser);
    }

    @Override
    public long countByUserId(String userId) {
        OrderUserExample example = new OrderUserExample();
        example.createCriteria().andUserIdEqualTo(userId);
        return orderUserMapper.countByExample(example);
    }
}
