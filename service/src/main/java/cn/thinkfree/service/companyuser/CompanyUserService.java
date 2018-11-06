package cn.thinkfree.service.companyuser;

import java.util.List;

import com.github.pagehelper.PageInfo;

import cn.thinkfree.database.model.CompanyRole;
import cn.thinkfree.database.model.CompanyUser;
import cn.thinkfree.database.vo.CompanyUserSEO;
import cn.thinkfree.database.vo.CompanyUserVo;

public interface CompanyUserService {

	/**
	 * 查询 入驻公司 用户列表
	 * @author lvqidong
	 */
    PageInfo<CompanyUser> queryCompanyUserList(CompanyUserSEO companyUser);
    
    
    /**
     * 添加或者修改入驻公司用户
     * @author lvqidong
     */
    boolean insertOrUpdateCompanyUser(CompanyUserVo companyUser);


    /**
     * 添加或者修改入驻公司用户
     * @author lvqidong
     */
    boolean  updateUserStatus(String userId,String status);
    
    
    /**
     * 根据员工编号查询 员工信息
     * @author lvqidong
     */
    CompanyUser getCompanyUserInfo(String empNumber);
    
    
    /**
     * 根据当前登陆公司编号查询
     *插入角色列表
     *@author lvqidong
     */
    List<CompanyRole> getRoleList();
    
    
    
    /**
     * 添加公司角色
     * @author lvqidong
     */
    boolean inserRoleInfo(CompanyRole user);

    /**
     * 更新企业角色资源
     * @param id
     * @param resources
     * @return
     */
    String updateEnterPriseRoleResource(Integer id, Integer[] resources);
}
