package cn.thinkfree.service.staff;

import cn.thinkfree.database.model.CompanyUserSet;
import cn.thinkfree.database.model.PreProjectUserRole;
import cn.thinkfree.database.vo.UserVO;

import java.util.List;


public interface StaffService {

    List<CompanyUserSet> queryStaffList(Integer page, Integer rows, String name, String phone, Integer isBind);

    CompanyUserSet queryCompanyUser(Integer id);

    List<PreProjectUserRole> queryRole();

    Integer deletCompanyByNo(Integer id);

    Integer updateCompanyWei(Integer id,String roleName);

    /**
     * 邀请员工
     * @param companyUserSet
     * @return
     */
    String insetCompanyUser(CompanyUserSet companyUserSet);

    /*
    * 删除员工逻辑删除
    * */
    Integer updateDelCompanyUser(Integer id);

    /**
     * 再次邀请用户
     * @param userID
     * @return
     */
    String reInvitation(String userID);
}

