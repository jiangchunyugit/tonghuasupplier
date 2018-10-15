package cn.thinkfree.service.user.strategy.build;

import cn.thinkfree.core.base.MyLogger;
import cn.thinkfree.core.constants.SysConstants;
import cn.thinkfree.core.security.model.SecurityUser;
import cn.thinkfree.core.utils.LogUtil;
import cn.thinkfree.database.mapper.UserRegisterMapper;
import cn.thinkfree.database.model.CompanyInfo;
import cn.thinkfree.database.model.UserRegister;
import cn.thinkfree.database.model.UserRegisterExample;
import cn.thinkfree.service.company.CompanyInfoService;
import cn.thinkfree.service.utils.ThreadLocalHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * 入驻平台
 */
@Component
public class EnterpriseUserBuildStrategy  implements UserBuildStrategy {

    MyLogger logger = LogUtil.getLogger(EnterpriseUserBuildStrategy.class);


    @Autowired
    CompanyInfoService companyInfoService;

    @Autowired
    UserRegisterMapper userRegisterMapper;

    @Override
    public SecurityUser build(String userID) {

        UserRegister userRegister = (UserRegister) ThreadLocalHolder.get();

//        CompanyInfo c = companyInfoService.findCompayByCompayID(userID);
        return null;
    }
}
