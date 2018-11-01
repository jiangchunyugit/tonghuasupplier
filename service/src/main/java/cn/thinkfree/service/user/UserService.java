package cn.thinkfree.service.user;

import cn.thinkfree.database.model.UserLoginLog;
import cn.thinkfree.database.vo.IndexUserReportVO;
import cn.thinkfree.database.vo.account.ChangeMeVO;

import java.util.List;


public interface UserService {
 

    /**
     * 汇总公司用户
     * @param companyRelationMap 公司关系图
     * @return
     */
    IndexUserReportVO countCompanyUser(List<String> companyRelationMap);

    /**
     * 用户登陆后
     * @param userLoginLog
     * @return
     */
    String userLoginAfter(UserLoginLog userLoginLog);


    /**
     * 是否首次登陆
     * @return
     */
    String isFirstLogin();

    /**
     * 更新个人信息
     * @param changeMeVO
     * @return
     */
    String updateUserInfo(ChangeMeVO changeMeVO);
}
