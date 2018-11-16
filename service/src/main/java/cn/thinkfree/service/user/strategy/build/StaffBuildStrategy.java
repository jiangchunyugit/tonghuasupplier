package cn.thinkfree.service.user.strategy.build;

import cn.thinkfree.core.base.MyLogger;
import cn.thinkfree.core.constants.SysConstants;
import cn.thinkfree.core.security.model.SecurityUser;
import cn.thinkfree.core.utils.LogUtil;
import cn.thinkfree.database.constants.UserRegisterType;
import cn.thinkfree.database.mapper.*;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.vo.EnterPriseUserVO;
import cn.thinkfree.database.vo.UserVO;
import cn.thinkfree.service.utils.ThreadLocalHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * 企业员工登录
 */
@Component
public class StaffBuildStrategy implements UserBuildStrategy {

    MyLogger logger = LogUtil.getLogger(StaffBuildStrategy.class);


    @Autowired
    CompanyInfoMapper companyInfoMapper;

    @Autowired
    UserRegisterMapper userRegisterMapper;

    @Autowired
    CompanyUserMapper companyUserMapper;

    @Autowired
    CompanyUserRoleMapper companyUserRoleMapper;

    @Autowired
    CompanyRoleResourceMapper companyRoleResourceMapper;

    @Autowired
    SystemResourceMapper systemResourceMapper;

    @Override
    public SecurityUser build(String userID) {
        UserVO userVO = new EnterPriseUserVO();
        userVO.setType(UserRegisterType.Staff);
        UserRegister userRegister = (UserRegister) ThreadLocalHolder.get();
        userVO.setUserRegister(userRegister);
        completionDetailInfo(userVO,userID);

        completionUserRole(userVO);

        ThreadLocalHolder.clear();
        return userVO;
    }

    /**
     * 补充用户权限
     * @param userVO
     */
    private void completionUserRole(UserVO userVO ) {
        CompanyUserRoleExample companyUserRoleExample = new CompanyUserRoleExample();
        companyUserRoleExample.createCriteria().andUserIdEqualTo(userVO.getCompanyUser().getEmpNumber());
        List<CompanyUserRole> companyUserRoles = companyUserRoleMapper.selectByExample(companyUserRoleExample);

        if(companyUserRoles.isEmpty() ){
            userVO.setResources(Collections.emptyList());
            return;
        }

        CompanyRoleResourceExample roleResourceExample = new CompanyRoleResourceExample();
        roleResourceExample.createCriteria().andRoleIdIn(companyUserRoles.stream().map(c->Integer.valueOf(c.getRoleId())).collect(toList()));
        List<CompanyRoleResource> companyRoleResources = companyRoleResourceMapper.selectByExample(roleResourceExample);
        if(companyRoleResources.isEmpty() ){
            userVO.setResources(Collections.emptyList());
            return;
        }
        SystemResourceExample systemResourceExample = new SystemResourceExample();
        systemResourceExample.createCriteria().andPlatformEqualTo(SysConstants.PlatformType.Enterprise.code)
                .andIdIn(companyRoleResources.stream().map(CompanyRoleResource::getId).collect(toList()));
        List<SystemResource> systemResources = systemResourceMapper.selectByExample(systemResourceExample);
        userVO.setResources(systemResources);


    }

    /**
     * 补充用户信息
     * @param userVO
     * @param userID
     */
    private void completionDetailInfo(UserVO userVO, String userID) {
        CompanyUserExample condition = new CompanyUserExample();
        condition.createCriteria().andEmpNumberEqualTo(userID).andStatusEqualTo(SysConstants.YesOrNo.YES.shortVal().toString());
        List<CompanyUser> users = companyUserMapper.selectByExample(condition);
        if(users.isEmpty() || users.size() > 1){
            throw  new UsernameNotFoundException("用户信息异常");
        }
        CompanyUser companyUser = users.get(0);
        userVO.setCompanyUser(companyUser);

        CompanyInfoExample companyInfoExample = new CompanyInfoExample();
        companyInfoExample.createCriteria().andCompanyIdEqualTo(companyUser.getCompanyId());
        List<CompanyInfo> companyInfoList = companyInfoMapper.selectByExample(companyInfoExample);
        if(companyInfoList.isEmpty() || companyInfoList.size() > 1){
            throw  new UsernameNotFoundException("公司信息异常");
        }
        userVO.setCompanyInfo(companyInfoList.get(0));

    }
}
