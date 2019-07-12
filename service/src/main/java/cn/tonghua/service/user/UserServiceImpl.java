package cn.tonghua.service.user;


import cn.tonghua.core.logger.AbsLogPrinter;
import cn.tonghua.core.security.dao.SecurityUserDao;
import cn.tonghua.core.security.token.MyCustomUserDetailToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl extends AbsLogPrinter implements SecurityUserDao {


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return null;
    }

    @Value("${custom.userService.useCache}")
    Boolean useCache;

    @Override
    public UserDetails loadPlatformUser(MyCustomUserDetailToken.TokenDetail detail) throws UsernameNotFoundException {

//        UserRegisterExample userRegisterExample = new UserRegisterExample();
//        userRegisterExample.createCriteria().andPhoneEqualTo(detail.getUserName()).andIsDeleteEqualTo(SysConstants.YesOrNo.NO.shortVal());
//        List<UserRegister> users = userRegisterMapper.selectByExample(userRegisterExample);
//        if(users.isEmpty() || users.size() > 1){
//            throw new UsernameNotFoundException("用户信息异常");
//        }
//
//        UserRegister user = users.get(0);
//        if(!Short.valueOf(detail.getType()).equals(user.getType())){
//            throw new UsernameNotFoundException("用户账号信息错误");
//        }
//        // 省去一次查询
//        ThreadLocalHolder.set(user);
//        UserRegisterType type = UserRegisterType.values()[Integer.valueOf(user.getType())];
//        SecurityUser userDetails = strategyFactory.getStrategy(type).build(user.getUserId());
//        if(userDetails == null){
//            throw  new UsernameNotFoundException("用户账号信息错误");
//        }
//        return userDetails;
        return null;
    }

}
