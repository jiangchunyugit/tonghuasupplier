package cn.thinkfree.service.user.strategy.build;

import cn.thinkfree.core.constants.SysConstants;
import cn.thinkfree.core.logger.AbsLogPrinter;
import cn.thinkfree.core.security.model.SecurityUser;
import cn.thinkfree.database.constants.UserRegisterType;
import cn.thinkfree.database.mapper.*;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.vo.UserVO;
import cn.thinkfree.service.user.strategy.StrategyFactory;
import cn.thinkfree.service.utils.ThreadLocalHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 运营人员
 */
@Component
public class PlatformUserBuildStrategy extends AbsLogPrinter implements UserBuildStrategy {


    @Autowired
    PcUserResourceMapper pcUserResourceMapper;
    @Autowired
    SystemResourceMapper systemResourceMapper;
    @Autowired
    PcUserInfoMapper pcUserInfoMapper;
    @Autowired
    CompanyInfoMapper companyInfoMapper;
    @Autowired
    StrategyFactory strategyFactory;
    @Autowired
    SystemPermissionMapper systemPermissionMapper;
    @Autowired
    SystemPermissionResourceMapper systemPermissionResourceMapper;
    @Autowired
    SystemRoleMapper systemRoleMapper;
    @Autowired
    BranchCompanyMapper branchCompanyMapper;
    @Autowired
    CityBranchMapper cityBranchMapper;


    /**
     * 运营平台人员登录
     * 1.查询账号信息
     * 2.查询用户信息
     * 3.查询所属分店信息
     * 4.查询角色权限信息
     * 5.构建公司关系图
     * @param userID
     * @return
     */
    @Override
    public SecurityUser build(String userID) {
         UserVO userVO = new UserVO();
         userVO.setType(UserRegisterType.Platform);
         UserRegister userRegister = (UserRegister) ThreadLocalHolder.get();
         userVO.setUserRegister(userRegister);

         completionDetailInfo(userVO,userID);

         completionUserRole(userVO,userID);

         ThreadLocalHolder.clear();
         return userVO;
    }


    /**
     * 补全用户角色
     * @param userVO
     * @param userID
     */
    private void completionUserRole(UserVO userVO, String userID) {
        printDebugMes("拼接用户权限");

        List<SystemRole> userRoles = systemRoleMapper.selectEffectiveRoleByUserID(userID);
        if(userRoles.isEmpty()){
            userVO.setResources(defaultUserRole());
            return;
        }
        List<SystemPermission>  rolePermissions = systemPermissionMapper.selectEffectivePermission(userRoles.stream()
                                                                                            .map(SystemRole::getId)
                                                                                            .collect(Collectors.toList()));
        if(rolePermissions.isEmpty()){
            userVO.setResources(defaultUserRole());
            return;
        }

        SystemPermissionResourceExample permissionResourceCondition = new SystemPermissionResourceExample();
        permissionResourceCondition.createCriteria()
                .andPermissionIdIn(rolePermissions.stream().map(SystemPermission::getId).collect(Collectors.toList()));
        List<SystemPermissionResource> permissionResources = systemPermissionResourceMapper.selectByExample(permissionResourceCondition);
        if(permissionResources.isEmpty()){
            userVO.setResources(defaultUserRole());
            return;
        }

        SystemResourceExample resourceCondition = new SystemResourceExample();
        resourceCondition.createCriteria().andPlatformEqualTo(SysConstants.PlatformType.Platform.code)
                .andIdIn(permissionResources.stream().map(SystemPermissionResource::getResourceId).collect(Collectors.toList()));
        List<SystemResource> resources = systemResourceMapper.selectByExample(resourceCondition);
        resources.addAll(defaultUserRole());
        userVO.setResources(resources);
    }

    /**
     * 补全详细信息
     * @param userVO
     * @param userID
     */
    private void completionDetailInfo(UserVO userVO, String userID) {

        printDebugMes("获取到用户账号信息:{},准备获取用户详情信息",userID);
        PcUserInfoExample pcUserInfoExample = new PcUserInfoExample();
        pcUserInfoExample.createCriteria().andIdEqualTo(userID);
        List<PcUserInfo> users = pcUserInfoMapper.selectByExample(pcUserInfoExample);
        if(users.isEmpty() || users.size() > 1){
            throw  new UsernameNotFoundException("用户详情信息错误");
        }
        PcUserInfo pcUserInfo = users.get(0);
        userVO.setPcUserInfo(pcUserInfo);
        userVO.setRelationMap(strategyFactory.getStrategy(userVO.getPcUserInfo().getLevel()).build(userVO));

        CompanyInfoExample companyInfoExample = new CompanyInfoExample();
        companyInfoExample.createCriteria().andCompanyIdEqualTo(userVO.getPcUserInfo().getRootCompanyId());
        List<CompanyInfo> companyInfos = companyInfoMapper.selectByExample(companyInfoExample);
        if(companyInfos.isEmpty() || companyInfos.size() > 1){
            throw  new UsernameNotFoundException("用户企业信息异常");
        }
        CompanyInfo companyInfo = companyInfos.get(0);
        userVO.setCompanyInfo(companyInfo);

    }
}
