package cn.thinkfree.service.user;

import cn.thinkfree.core.logger.AbsLogPrinter;
import cn.thinkfree.core.security.dao.SecurityUserDao;
import cn.thinkfree.database.mapper.*;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.vo.IndexUserReportVO;
import cn.thinkfree.service.constants.UserRegisterType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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




    @Override
    public IndexUserReportVO countCompanyUser(String companyID) {
        System.out.println(companyUserSetMapper);
        List<CompanyUserSet> rs = companyUserSetMapper.selectByExample(null);
        System.out.println(rs);
        companyUserSetMapper.countCompanyUser(companyID);


        return null;
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

        UserRegisterExample userRegisterExample = new UserRegisterExample();
        userRegisterExample.createCriteria().andPhoneEqualTo(phone).andTypeEqualTo(UserRegisterType.Staff.shortVal());
        List<UserRegister> users = userRegisterMapper.selectByExample(userRegisterExample);
        if(users.isEmpty()|| users.size() > 1){
            printErrorMes("用户账号信息错误",phone);
            throw  new UsernameNotFoundException("用户账号信息错误");
        }
        UserRegister user = users.get(0);
        printDebugMes("获取到用户账号信息:{},准备获取用户详情信息",user);
        PcUserInfoExample pcUserInfoExample = new PcUserInfoExample();
        pcUserInfoExample.createCriteria().andIdEqualTo(user.getUserId());
        List<PcUserInfo> pcUserInfos = pcUserInfoMapper.selectByExample(pcUserInfoExample);
        if(pcUserInfos.isEmpty() || pcUserInfos.size() > 1){
            printErrorMes("用户详情信息错误",phone);
            throw  new UsernameNotFoundException("用户详情信息错误");
        }
        user.setPcUserInfo(pcUserInfos.get(0));
        printDebugMes("拼接用户权限");

        PcUserResourceExample pcUserResourceExample = new PcUserResourceExample();
        pcUserResourceExample.createCriteria().andUserIdEqualTo(user.getUserId());
        List<PcUserResource> pus = pcUserResourceMapper.selectByExample(pcUserResourceExample);

        if(pus.isEmpty()){
            printErrorMes("用户无权限");
            user.setResources(Collections.emptyList());

        }else{
            SystemResourceExample systemResourceExample = new SystemResourceExample();
            systemResourceExample.createCriteria().andIdIn(
                    pus.stream()
                            .map(PcUserResource::getResourceId).collect(Collectors.toList())
                    );
            user.setResources(systemResourceMapper.selectByExample(systemResourceExample));

        }

        return user;
    }
}
