package cn.thinkfree.service.user;

import cn.thinkfree.core.constants.SysConstants;
import cn.thinkfree.core.exception.MyException;
import cn.thinkfree.core.logger.AbsLogPrinter;
import cn.thinkfree.core.security.dao.SecurityUserDao;
import cn.thinkfree.core.security.filter.util.SessionUserDetailsUtil;
import cn.thinkfree.core.security.model.SecurityUser;
import cn.thinkfree.core.security.token.MyCustomUserDetailToken;
import cn.thinkfree.core.security.utils.MultipleMd5;
import cn.thinkfree.database.mapper.*;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.vo.IndexUserReportVO;
import cn.thinkfree.database.vo.UserVO;
import cn.thinkfree.database.vo.account.ChangeMeVO;
import cn.thinkfree.database.constants.UserRegisterType;
import cn.thinkfree.service.user.strategy.StrategyFactory;
import cn.thinkfree.service.utils.ThreadLocalHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


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

    @Autowired
    CompanyUserMapper companyUserMapper;


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
     * 是否首次登陆
     *
     * @return
     */
    @Override
    public String isFirstLogin() {
        UserVO userVO = (UserVO) SessionUserDetailsUtil.getUserDetails();
        String userID = userVO.getUsername();
        UserLoginLogExample loginLogExample = new UserLoginLogExample();
        loginLogExample.createCriteria().andUserIdEqualTo(userID);
        List<UserLoginLog> logs = userLoginLogMapper.selectByExample(loginLogExample);
        return logs.isEmpty() ? "Y":"N";
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

        printDebugMes("用户登录:{}",phone);
        UserRegisterExample userRegisterExample = new UserRegisterExample();
        userRegisterExample.createCriteria().andPhoneEqualTo(phone)
                .andIsDeleteEqualTo(SysConstants.YesOrNo.NO.shortVal());
        List<UserRegister> users = userRegisterMapper.selectByExample(userRegisterExample);
        if(users.isEmpty()|| users.size() > 1){
            printErrorMes("用户账号信息错误",phone);
            throw  new UsernameNotFoundException("用户账号信息错误");
        }
        UserRegister user = users.get(0);
        // 省去一次查询
        ThreadLocalHolder.set(user);

        UserRegisterType type = UserRegisterType.values()[Integer.valueOf(user.getType())];
        SecurityUser userDetails = strategyFactory.getStrategy(type).build(user.getUserId());

        if(userDetails == null){
            throw  new UsernameNotFoundException("用户账号信息错误");
        }

        return userDetails;
    }



    @Override
    public UserDetails loadPlatformUser(MyCustomUserDetailToken.TokenDetail detail) throws UsernameNotFoundException {

        UserRegisterExample userRegisterExample = new UserRegisterExample();
        userRegisterExample.createCriteria().andPhoneEqualTo(detail.getUserName()).andIsDeleteEqualTo(SysConstants.YesOrNo.NO.shortVal());
        List<UserRegister> users = userRegisterMapper.selectByExample(userRegisterExample);
        if(users.isEmpty() || users.size() > 1){
            throw new UsernameNotFoundException("用户信息异常");
        }
        UserRegisterType type = UserRegisterType.values()[Integer.valueOf(detail.getType())];
        UserDetails userDetails = strategyFactory.getStrategy(type).build(detail.getUserName());


        return userDetails;
    }

    /**
     * 更新个人信息
     *
     * @param changeMeVO
     * @return
     */
    @Transactional
    @Override
    public String updateUserInfo(ChangeMeVO changeMeVO) {
        UserVO userVO = (UserVO) SessionUserDetailsUtil.getUserDetails();
        PasswordEncoder passwordEncoder = new MultipleMd5();
        Boolean changePwd = StringUtils.isNotBlank(changeMeVO.getOldPwd());
        if( changePwd && !passwordEncoder.encode(changeMeVO.getOldPwd()).equals(userVO.getUserRegister().getPassword())){
            throw new MyException("旧密码不正确!") ;
        }
        if(changePwd && StringUtils.isBlank(changeMeVO.getNewPwd())){
            throw new MyException("新密码不能为空!");
        }
        if(!StringUtils.equals(userVO.getUserRegister().getHeadPortraits(),changeMeVO.getFace())){
            UserRegister update = new UserRegister();
            update.setHeadPortraits(changeMeVO.getFace());
            if(changePwd){
                update.setPassword(passwordEncoder.encode(changeMeVO.getNewPwd()));
            }
            UserRegisterExample condition = new UserRegisterExample();
            condition.createCriteria().andUserIdEqualTo(userVO.getUserRegister().getUserId());
            userRegisterMapper.updateByExampleSelective(update,condition);
        }

        if(UserRegisterType.Platform.shortVal().equals(userVO.getType())){
            PcUserInfo update = new PcUserInfo();
            update.setName(changeMeVO.getName());
            update.setId(userVO.getPcUserInfo().getId());
            pcUserInfoMapper.updateByPrimaryKeySelective(update);
        }else if(UserRegisterType.Enterprise.shortVal().equals(userVO.getType())){
            CompanyUser update = new CompanyUser();
            update.setEmpName(changeMeVO.getName());
            CompanyUserExample condition = new CompanyUserExample();
            condition.createCriteria().andIdEqualTo(userVO.getCompanyUser().getId());
            companyUserMapper.updateByExampleSelective(update,condition);
        }
        return "操作成功!";
    }
}
