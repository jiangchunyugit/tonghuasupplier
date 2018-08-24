package cn.thinkfree.service.staff;

import cn.thinkfree.database.model.CompanyUserSet;
import cn.thinkfree.database.model.PreProjectUserRole;
import cn.thinkfree.database.model.UserRoleSet;
import cn.thinkfree.database.vo.CompanyUserSetVo;
import cn.thinkfree.database.vo.UserVO;
import cn.thinkfree.database.vo.StaffSEO;



import java.util.List;


public interface StaffService {

    /**
     *查询员工列表
     * @param page
     * @return
     */
    List<CompanyUserSet> queryStaffList(Integer page, Integer rows,
                                        String name, String phone, Integer isBind,StaffSEO staffSEO);

    /**
     *查询员工信息
     * @param id
     * @return
     */
    CompanyUserSet queryCompanyUser(Integer id);
    /**
     *查询岗位
     * @param
     * @return
     */
    List<PreProjectUserRole> queryRole();

    Integer deletCompanyByNo(Integer id);

    Integer updateCompanyWei(Integer id,String roleName);

    /**
     * 邀请员工
     * @param companyUserSet
     * @return
     */
    String insetCompanyUser(CompanyUserSet companyUserSet);


    /**
     * 再次邀请用户
     * @param userID
     * @return
     */
    String reInvitation(String userID);

    /**
     * 判断员工是否可以移除
     * @param userId
     * @return
     */
    String updateDelCompanyUser(String userId);

    /**
     * 移除员工
     * @param userId
     * @return
     */
    int updateIsJob(String userId);

    /**
     * 查询员工详情
     * @param id
     * @return
     */
    CompanyUserSetVo detail(Integer id);

    /**
     * 获取岗位信息
     * @return
     */
    List<UserRoleSet> getRole();

    /**
     * 修改岗位
     * @param userId
     * @param roleId
     * @return
     */
    String updateRole(String userId,String roleId);
}

