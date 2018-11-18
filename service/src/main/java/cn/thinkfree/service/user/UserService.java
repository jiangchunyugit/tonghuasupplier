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

    /**
     * 检查用户是否存在
     * @param email
     * @return
     */
    Boolean checkUserExist(String email);

    /**
     * 忘记密码
     * @param email
     * @return
     */
    String forgetPwd(String email);

    /**
     * 忘记密码 -- 重置密码
     * @param email
     * @param pwd
     * @param code
     * @return
     */
    String updatePassWordOnForget(String email, String pwd, String code);
}
