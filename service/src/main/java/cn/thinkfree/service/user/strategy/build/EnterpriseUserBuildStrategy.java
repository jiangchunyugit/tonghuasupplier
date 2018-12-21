package cn.thinkfree.service.user.strategy.build;

import cn.thinkfree.core.base.MyLogger;
import cn.thinkfree.core.constants.SysConstants;
import cn.thinkfree.core.exception.MyException;
import cn.thinkfree.core.security.model.SecurityUser;
import cn.thinkfree.core.utils.LogUtil;
import cn.thinkfree.database.constants.UserRegisterType;
import cn.thinkfree.database.mapper.*;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.vo.EnterPriseUserVO;
import cn.thinkfree.database.vo.UserVO;
import cn.thinkfree.service.company.CompanyInfoService;
import cn.thinkfree.service.companyuser.CompanyUserService;
import cn.thinkfree.service.utils.ThreadLocalHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * 入驻平台
 *
 */
@Component
public class EnterpriseUserBuildStrategy  implements UserBuildStrategy {


    MyLogger logger = LogUtil.getLogger(EnterpriseUserBuildStrategy.class);


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
        userVO.setType(UserRegisterType.Enterprise);
        UserRegister userRegister = (UserRegister) ThreadLocalHolder.get();
        userVO.setUserRegister(userRegister);
        completionDetailInfo(userVO,userID);

        completionUserRole(userVO,userID);

        ThreadLocalHolder.clear();
        return userVO;
    }

    /**
     * 补充用户权限
     * @param userVO
     */
    private void completionUserRole(UserVO userVO ,String userID) {
        CompanyUserRoleExample companyUserRoleExample = new CompanyUserRoleExample();
        companyUserRoleExample.createCriteria().andUserIdEqualTo(userID);
        List<CompanyUserRole> companyUserRoles = companyUserRoleMapper.selectByExample(companyUserRoleExample);

        if(companyUserRoles.isEmpty() ){
            userVO.setResources(defaultUserRole());
            return;
        }

        CompanyRoleResourceExample roleResourceExample = new CompanyRoleResourceExample();
        roleResourceExample.createCriteria().andRoleIdIn(companyUserRoles.stream().map(c->Integer.valueOf(c.getRoleId())).collect(toList()));
        List<CompanyRoleResource> companyRoleResources = companyRoleResourceMapper.selectByExample(roleResourceExample);
        if(companyRoleResources.isEmpty() ){
            userVO.setResources(defaultUserRole());
            return;
        }
        SystemResourceExample systemResourceExample = new SystemResourceExample();
        systemResourceExample.createCriteria().andPlatformEqualTo(SysConstants.PlatformType.Enterprise.code)
                .andIdIn(companyRoleResources.stream().map(CompanyRoleResource::getResourceId).collect(toList()));
        List<SystemResource> systemResources = systemResourceMapper.selectByExample(systemResourceExample);
        systemResources.addAll(defaultUserRole());
        userVO.setResources(systemResources);

    }

    /**
     * 补充用户信息
     * @param userVO
     * @param userID
     */
    private void completionDetailInfo(UserVO userVO, String userID) {

        CompanyInfoExample companyInfoExample = new CompanyInfoExample();
        companyInfoExample.createCriteria().andCompanyIdEqualTo(userID);
        List<CompanyInfo> companyInfoList = companyInfoMapper.selectByExample(companyInfoExample);
        if(companyInfoList.isEmpty() || companyInfoList.size() > 1){
            throw  new UsernameNotFoundException("公司信息异常");
        }
        userVO.setCompanyInfo(companyInfoList.get(0));

    }
}
