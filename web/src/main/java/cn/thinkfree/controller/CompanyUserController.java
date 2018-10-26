package cn.thinkfree.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.model.CompanyRole;
import cn.thinkfree.database.model.CompanyUser;
import cn.thinkfree.database.vo.CompanyUserSEO;
import cn.thinkfree.database.vo.CompanyUserVo;
import cn.thinkfree.service.companyuser.CompanyUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping(value = "/companyUser")
@Api(value = "入驻公司员工和权限管理",description = "入驻公司员工和权限管理")
public class CompanyUserController extends AbsBaseController{

    @Autowired
    CompanyUserService companyInfoService;

    /**
     * 查询入驻公司员工
     * @author lqd
     * @param ContractSEO
     * @return pageList
     */
    @ApiOperation(value = "入驻公司员工列表", notes = "根据一定条件获取分页入驻公司员工")
    @PostMapping("/listPage")
    @MyRespBody

    public MyRespBundle<PageInfo<CompanyUser>> listPage(@ApiParam("项目搜索条件")CompanyUserSEO companyUser){

        PageInfo<CompanyUser> pageInfo = companyInfoService.queryCompanyUserList(companyUser);

        return sendJsonData(ResultMessage.SUCCESS,pageInfo);
    }

    /**
     * 新增入驻公司员工
     * @author lqd
     * @param ContractSEO
     * @return pageList
     */
    @ApiOperation(value = "新增或者添加公司信息", notes = "新增入驻公司员工")
    @PostMapping("/insertInfo")
    @MyRespBody
    public MyRespBundle<String> insertInfo(@ApiParam("添加/修改用户信息")CompanyUserVo companyUser){

        boolean  falg = companyInfoService.inserOrUpdateCompanyUser(companyUser);

        return sendJsonData(ResultMessage.SUCCESS,falg);
    }

    /**
     * 停用/启用
     * @author lqd
     * @param ContractSEO
     * @return pageList
     */
    @ApiOperation(value = "停用/启用公司员工", notes = "1 代表启用  0代表停用")
    @PostMapping("/updateUserStatus")
    @MyRespBody
    public MyRespBundle<String> updateUserStatus(@ApiParam("用户编号")String userId,@ApiParam("状态")String status){

        boolean  falg = companyInfoService.updateUserStatus(userId, status);

        return sendJsonData(ResultMessage.SUCCESS,falg);
    }

    /**
     * 根据用户编号查询
     * @author lqd
     * @param ContractSEO
     * @return pageList
     */
    @ApiOperation(value = "新增或者添加公司信息", notes = "根据一定条件获取分页入驻公司员工（通过id是否有值来区分）")
    @PostMapping("/getCompanyUser")
    @MyRespBody
    public MyRespBundle<PageInfo<CompanyUser>> getCompanyUser(@ApiParam("添加/修改用户信息")String empNumber){

    	CompanyUser  companyUser = companyInfoService.getCompanyUserInfo(empNumber);

        return sendJsonData(ResultMessage.SUCCESS,companyUser);
    }

    
    /**
     * 查询公司角色列表
     * @author lqd
     * @param ContractSEO
     * @return pageList
     */
    @ApiOperation(value = "入驻公角色列表", notes = "入驻公角色列表")
    @PostMapping("/getCompanyRoleList")
    @MyRespBody
    public MyRespBundle<PageInfo<CompanyRole>> getCompanyRoleList(){

    	List<CompanyRole> list = companyInfoService.getRoleList();

        return sendJsonData(ResultMessage.SUCCESS,list);
    }
    
    
    /**
     *新增公司角色列表
     * @author lqd
     * @param ContractSEO
     * @return pageList
     */
    @ApiOperation(value = "新增角色列表", notes = "新增公角色列表")
    @PostMapping("/insertCompanyRole")
    @MyRespBody
    public MyRespBundle<PageInfo<CompanyRole>> insertCompanyRole(@ApiParam("角色信息")CompanyRole user){

    	boolean  flag = companyInfoService.inserRoleInfo(user);

        return sendJsonData(ResultMessage.SUCCESS,flag);
    }
    
}

