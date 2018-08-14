package cn.thinkfree.service.user;

import cn.thinkfree.database.model.User;
import cn.thinkfree.database.vo.IndexUserReportVO;


public interface UserService {
    User findById(String id);
    void save(User user);

    /**
     * 汇总公司用户
     * @param companyID 公司ID
     * @return
     */
    IndexUserReportVO countCompanyUser(String companyID);




}
