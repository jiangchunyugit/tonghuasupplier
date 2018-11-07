package cn.thinkfree.service.platform.basics.impl;

import cn.thinkfree.core.constants.RoleFunctionEnum;
import cn.thinkfree.database.mapper.UserRoleFunRelMapper;
import cn.thinkfree.database.mapper.UserRoleSetMapper;
import cn.thinkfree.database.model.UserRoleFunRel;
import cn.thinkfree.database.model.UserRoleFunRelExample;
import cn.thinkfree.database.model.UserRoleSet;
import cn.thinkfree.database.model.UserRoleSetExample;
import cn.thinkfree.service.platform.basics.RoleFunctionService;
import cn.thinkfree.service.utils.ReflectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xusonghui
 */
@Service
public class RoleFunctionServiceImpl implements RoleFunctionService {

    @Autowired
    private UserRoleFunRelMapper roleFunRelMapper;
    @Autowired
    private UserRoleSetMapper userRoleSetMapper;

    @Override
    public boolean checkFun(String roleCode, RoleFunctionEnum functionEnum) {
        UserRoleFunRelExample roleFunRelExample = new UserRoleFunRelExample();
        roleFunRelExample.createCriteria().andRoleCodeEqualTo(roleCode).andFunctionItemNoEqualTo(functionEnum.getFunctionCode());
        List<UserRoleFunRel> funRels = roleFunRelMapper.selectByExample(roleFunRelExample);
        return !funRels.isEmpty();
    }

    @Override
    public List<UserRoleSet> queryUserRoleByFun(RoleFunctionEnum functionEnum) {
        UserRoleFunRelExample roleFunRelExample = new UserRoleFunRelExample();
        roleFunRelExample.createCriteria().andFunctionItemNoEqualTo(functionEnum.getFunctionCode());
        List<UserRoleFunRel> funRels = roleFunRelMapper.selectByExample(roleFunRelExample);
        if (funRels.isEmpty()) {
            return new ArrayList<>();
        }
        List<String> roleCodes = ReflectUtils.getList(funRels, "roleCode");
        UserRoleSetExample setExample = new UserRoleSetExample();
        setExample.createCriteria().andRoleCodeIn(roleCodes);
        return userRoleSetMapper.selectByExample(setExample);
    }

    @Override
    public List<String> queryRoleCode(RoleFunctionEnum functionEnum) {
        UserRoleFunRelExample roleFunRelExample = new UserRoleFunRelExample();
        roleFunRelExample.createCriteria().andFunctionItemNoEqualTo(functionEnum.getFunctionCode());
        List<UserRoleFunRel> funRels = roleFunRelMapper.selectByExample(roleFunRelExample);
        if (funRels.isEmpty()) {
            return new ArrayList<>();
        }
        List<String> roleCodes = ReflectUtils.getList(funRels, "roleCode");
        return roleCodes;
    }

    @Override
    public void addRel(String roleCode, String functionCode) {
        UserRoleFunRel roleFunRel = new UserRoleFunRel();
        roleFunRel.setFunctionItemNo(functionCode);
        roleFunRel.setRoleCode(roleCode);
        roleFunRelMapper.insertSelective(roleFunRel);
    }

    @Override
    public void delRel(String roleCode, String functionCode) {
        UserRoleFunRelExample relExample = new UserRoleFunRelExample();
        relExample.createCriteria().andRoleCodeEqualTo(roleCode).andFunctionItemNoEqualTo(functionCode);
        roleFunRelMapper.deleteByExample(relExample);
    }
}
