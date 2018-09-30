package cn.thinkfree.service.user.strategy.build;

import cn.thinkfree.core.base.MyLogger;
import cn.thinkfree.core.constants.SysConstants;
import cn.thinkfree.core.utils.LogUtil;
import cn.thinkfree.database.mapper.UserRegisterMapper;
import cn.thinkfree.database.model.UserRegisterExample;
import cn.thinkfree.service.company.CompanyInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class EnterpriseUserBuildStrategy implements UserBuildStrategy {

    MyLogger logger = LogUtil.getLogger(EnterpriseUserBuildStrategy.class);


    @Autowired
    CompanyInfoService companyInfoService;

    @Autowired
    UserRegisterMapper userRegisterMapper;

    @Override
    public UserDetails build(String userID) {

        UserRegisterExample userRegisterExample = new UserRegisterExample();

        userRegisterExample.createCriteria().andPhoneEqualTo(userID).andIsDeleteEqualTo(SysConstants.YesOrNo.NO.shortVal());

        userRegisterMapper.selectByExample(userRegisterExample);

//        companyInfoService


        return null;
    }
}
