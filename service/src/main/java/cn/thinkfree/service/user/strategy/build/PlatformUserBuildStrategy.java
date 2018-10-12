package cn.thinkfree.service.user.strategy.build;

import cn.thinkfree.core.logger.AbsLogPrinter;
import cn.thinkfree.core.security.model.SecurityUser;
import cn.thinkfree.database.mapper.CompanyInfoMapper;
import cn.thinkfree.database.mapper.PcUserInfoMapper;
import cn.thinkfree.database.mapper.PcUserResourceMapper;
import cn.thinkfree.database.mapper.SystemResourceMapper;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.vo.UserVO;
import cn.thinkfree.service.user.strategy.StrategyFactory;
import cn.thinkfree.service.utils.ThreadLocalHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
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


    @Override
    public SecurityUser build(String userID) {
         UserVO userVO = new UserVO();
         UserRegister userRegister = (UserRegister) ThreadLocalHolder.get();
         userVO.setUserRegister(userRegister);
         completionDetailInfo(userVO,userID);
         completionUserRole(userVO,userID);
         completionThirdUserInfo(userVO,userID);
         ThreadLocalHolder.clear();
         return userVO;
    }

    /**
     * 补全第三方用户信息
     * @param userVO
     * @param userID
     */
    private void completionThirdUserInfo(UserVO userVO, String userID) {

    }

    /**
     * 补全用户角色
     * @param userVO
     * @param userID
     */
    private void completionUserRole(UserVO userVO, String userID) {
        printDebugMes("拼接用户权限");
        PcUserResourceExample pcUserResourceExample = new PcUserResourceExample();
        pcUserResourceExample.createCriteria().andUserIdEqualTo(userID);
        List<PcUserResource> pus = pcUserResourceMapper.selectByExample(pcUserResourceExample);
        if(pus.isEmpty()){
            printErrorMes("用户无权限");
            userVO.setResources(Collections.emptyList());
        }else{
            SystemResourceExample systemResourceExample = new SystemResourceExample();
            systemResourceExample.createCriteria().andIdIn(
                    pus.stream()
                            .map(PcUserResource::getResourceId).collect(Collectors.toList())
            );
            userVO.setResources(systemResourceMapper.selectByExample(systemResourceExample));
        }
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
        List<PcUserInfo> pcUserInfos = pcUserInfoMapper.selectByExample(pcUserInfoExample);
        if(pcUserInfos.isEmpty() || pcUserInfos.size() > 1){
            throw  new UsernameNotFoundException("用户详情信息错误");
        }
        PcUserInfo pcUserInfo = pcUserInfos.get(0);
        userVO.setPcUserInfo(pcUserInfo);
        userVO.setRelationMap(strategyFactory.getStrategy(userVO.getPcUserInfo().getLevel()).build(userVO));


        CompanyInfoExample companyInfoExample = new CompanyInfoExample();
        companyInfoExample.createCriteria().andCompanyIdEqualTo(userVO.getPcUserInfo().getCompanyId());
        List<CompanyInfo> companyInfos = companyInfoMapper.selectByExample(companyInfoExample);
        if(companyInfos.isEmpty() || companyInfos.size() >1){
            throw  new UsernameNotFoundException("用户企业信息异常");
        }
        CompanyInfo companyInfo = companyInfos.get(0);
        userVO.setCompanyInfo(companyInfo);

    }
}
