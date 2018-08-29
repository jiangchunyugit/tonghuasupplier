package cn.thinkfree.service.user;

import cn.thinkfree.core.constants.SysConstants;
import cn.thinkfree.core.logger.AbsLogPrinter;
import cn.thinkfree.core.security.dao.SecurityUserDao;
import cn.thinkfree.database.constants.UserLevel;
import cn.thinkfree.database.mapper.*;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.vo.IndexUserReportVO;
import cn.thinkfree.database.vo.UserVO;
import cn.thinkfree.service.constants.UserRegisterType;
import cn.thinkfree.service.user.builder.Context;
import cn.thinkfree.service.user.builder.StrategyFactory;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl extends AbsLogPrinter implements UserService, SecurityUserDao{


    @Autowired
    UserRegisterMapper userRegisterMapper;

    @Autowired
    PcUserInfoMapper pcUserInfoMapper;

    @Autowired
    SystemResourceMapper systemResourceMapper;

    @Autowired
    PcUserResourceMapper pcUserResourceMapper;

    @Autowired
    CompanyUserSetMapper companyUserSetMapper;

    @Autowired
    CompanyInfoMapper companyInfoMapper;

    @Autowired
    StrategyFactory strategyFactory;

    @Autowired
    UserLoginLogMapper userLoginLogMapper;

    /**
     * 汇总
     * @param companyRelationMap 公司ID
     * @return
     */
    @Override
    public IndexUserReportVO countCompanyUser(List<String> companyRelationMap) {
        IndexUserReportVO i = companyUserSetMapper.countCompanyUser(companyRelationMap);
        return i == null ? new IndexUserReportVO():i;
    }

    @Transactional
    @Override
    public String userLoginAfter(UserLoginLog userLoginLog) {
        userLoginLogMapper.insertSelective(userLoginLog);
        return "SUCCESS";
    }


    /**
     * 权限验证
     * 1.获取账号信息
     * 2.补全账号信息
     * 3.拉取权限信息
     * @param phone
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {

        UserVO userVO = new UserVO();
        printDebugMes("用户登录:{}",phone);
        UserRegisterExample userRegisterExample = new UserRegisterExample();
        userRegisterExample.createCriteria().andPhoneEqualTo(phone)
                .andTypeEqualTo(UserRegisterType.Staff.shortVal())
                .andIsDeleteEqualTo(SysConstants.YesOrNo.NO.shortVal());
        List<UserRegister> users = userRegisterMapper.selectByExample(userRegisterExample);
        if(users.isEmpty()|| users.size() > 1){
            printErrorMes("用户账号信息错误",phone);
            throw  new UsernameNotFoundException("用户账号信息错误");
        }
        UserRegister user = users.get(0);
        userVO.setUserRegister(user);

        completionDetailInfo(userVO,user);
        completionUserRole(userVO,user);

        return userVO;
    }

    /**
     * 补全用户角色
     * @param userVO
     * @param user
     */
    private void completionUserRole(UserVO userVO, UserRegister user) {
        printDebugMes("拼接用户权限");
        PcUserResourceExample pcUserResourceExample = new PcUserResourceExample();
        pcUserResourceExample.createCriteria().andUserIdEqualTo(user.getUserId());
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
     * @param user
     */
    private void completionDetailInfo(UserVO userVO, UserRegister user) {

        printDebugMes("获取到用户账号信息:{},准备获取用户详情信息",user);
        PcUserInfoExample pcUserInfoExample = new PcUserInfoExample();
        pcUserInfoExample.createCriteria().andIdEqualTo(user.getUserId());
        List<PcUserInfo> pcUserInfos = pcUserInfoMapper.selectByExample(pcUserInfoExample);
        if(pcUserInfos.isEmpty() || pcUserInfos.size() > 1){
            throw  new UsernameNotFoundException("用户详情信息错误");
        }
        PcUserInfo pcUserInfo = pcUserInfos.get(0);
        userVO.setPcUserInfo(pcUserInfo);
        userVO.setRelationMap(strategyFactory.getStrategy(userVO.getPcUserInfo().getLevel()).builder(userVO));


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
