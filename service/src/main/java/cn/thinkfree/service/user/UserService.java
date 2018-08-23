package cn.thinkfree.service.user;

import cn.thinkfree.database.model.UserLoginLog;
import cn.thinkfree.database.vo.IndexUserReportVO;

import java.util.List;


public interface UserService {
 

    /**
     * 汇总公司用户
     * @param companyRelationMap 公司关系图
     * @return
     */
    IndexUserReportVO countCompanyUser(List<String> companyRelationMap);

    String userLoginAfter(UserLoginLog userLoginLog);



}
