package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.annotation.MySysLog;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.core.constants.SysLogAction;
import cn.thinkfree.core.constants.SysLogModule;
import cn.thinkfree.core.security.filter.util.SessionUserDetailsUtil;
import cn.thinkfree.database.constants.UserEnabled;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.utils.BeanValidator;
import cn.thinkfree.database.vo.UserVO;
import cn.thinkfree.database.vo.account.*;
import cn.thinkfree.service.account.*;
import cn.thinkfree.service.companyuser.CompanyUserService;
import cn.thinkfree.service.pcUser.PcUserInfoService;
import cn.thinkfree.service.user.UserService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 账号相关
 */
@Api(description="账号,角色,权限,资源")
@RestController
@RequestMapping("/account")
public class AccountController extends AbsBaseController {

    @Autowired
    PermissionService permissionService;

    @Autowired
    SystemResourceService systemResourceService;

    @Autowired
    PermissionResourceService permissionResourceService;

    @Autowired
    SystemRoleService systemRoleService;

    @Autowired
    PcUserInfoService pcUserInfoService;

    @Autowired
    UserRoleSetService userRoleSetService;

    @Autowired
    CompanyUserService CompanyUserService;

    @Autowired
    UserService userService;

    /**
     * 创建权限
     * @param permissionVO 权限
     * @return
     */

    @ApiOperation(value="前端-运营平台-权限管理-创建权限", notes="新增权限")
    @PostMapping("/permission")
    @MyRespBody
    @MySysLog(action = SysLogAction.SAVE,module = SysLogModule.PC_PERMISSION,desc = "新增权限信息")
    public MyRespBundle<SystemPermission> permission(PermissionVO permissionVO){

        BeanValidator.validate(permissionVO);

        SystemPermission result = permissionService.save(permissionVO);

        return sendJsonData(ResultMessage.SUCCESS,result);
    }

    /**
     * 权限列表
     * @param permissionSEO
     * @return
     */
    @ApiResponses({
            @ApiResponse(code = 200,message = "操作成功",response = PermissionVO.class)
    })
    @ApiOperation(value="前端-运营平台-权限管理", notes="权限列表")
    @GetMapping("/permission")
    @MyRespBody
    public MyRespBundle<PageInfo<SystemPermission>> permissions(PermissionSEO permissionSEO){
        PageInfo<SystemPermission> pageInfo = permissionService.page(permissionSEO);
        return sendJsonData(ResultMessage.SUCCESS,pageInfo);
    }


    /**
     * 权限详情
     * @param id 主键
     * @return
     */
    @ApiResponses({
            @ApiResponse(code = 200,message = "操作成功",response = PermissionVO.class)
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "权限主键",paramType = "path",dataType = "String"),
            @ApiImplicitParam(name = "token",value = "用户令牌",paramType = "header",dataType = "String")

    })
    @ApiResponse(code = 200,message = "操作成功",response = PermissionVO.class)
    @ApiOperation(value="前端-运营平台-权限管理-权限详情", notes="权限详情")
    @GetMapping("/permission/{id}")
    @MyRespBody
    public MyRespBundle<SystemPermission> findPermission(@PathVariable("id") Integer id ){

        SystemPermission systemPermission = permissionService.detail(id);
        return sendJsonData(ResultMessage.SUCCESS,systemPermission);
    }


    /**
     * 编辑权限
     * @param id  主键
     * @param permissionVO 权限信息
     * @return
     */
    @ApiOperation(value="前端-运营平台-权限管理-权限编辑", notes="权限编辑")
    @PostMapping("/permission/{id}")
    @MyRespBody
    @MySysLog(action = SysLogAction.EDIT,module = SysLogModule.PC_PERMISSION,desc = "编辑权限信息")
    public MyRespBundle<SystemPermission> editPermission(@PathVariable("id") Integer id, PermissionVO permissionVO){

        BeanValidator.validate(permissionVO);
        permissionVO.setId(id);

        SystemPermission systemPermission = permissionService.edit(permissionVO);

        return sendJsonData(ResultMessage.SUCCESS,systemPermission);
    }

    /**
     * 查看权限的资源信息
     * @param id
     * @return
     */
    @ApiResponses({
            @ApiResponse(code = 200,message = "操作成功",response = SystemResourceTreeVO.class)
    })
    @ApiOperation(value="前端-运营平台-权限管理-功能分配-详情", notes="权限资源状况")
    @GetMapping("/permission/{id}/resource")
    @MyRespBody
    public MyRespBundle< List<SystemResource>> resource(@PathVariable("id")Integer id){
        List<SystemResource> resources = systemResourceService.listResourceByPermissionID(id);
        Collections.sort(resources, Comparator.comparingInt(SystemResource::getSortNum));
        return sendJsonData(ResultMessage.SUCCESS,resources);
    }


    /**
     * 为权限分配资源
     * @param id
     * @param resources
     * @return
     */
    @ApiOperation(value="前端-运营平台-权限管理-功能分配", notes="权限授权")
    @PostMapping("/permission/{id}/resource")
    @MyRespBody
    @MySysLog(action = SysLogAction.GRANT,module = SysLogModule.PC_PERMISSION,desc = "分配资源")
    public MyRespBundle<String> authorize(@PathVariable("id")Integer id,@ApiParam("资源主键集合")@RequestParam(value = "resource",required = false) Integer[] resources){

        String mes = permissionResourceService.updateSystemPermissionResource(id,resources);

        return sendJsonData(ResultMessage.SUCCESS,mes);
    }

    /**
     * 启用权限
     * @param id
     * @return
     */
    @ApiOperation(value="前端-运营平台-权限管理-启用权限", notes="启用权限")
    @PostMapping("/permission/{id}/enable")
    @MyRespBody
    @MySysLog(action = SysLogAction.CHANGE_STATE,module = SysLogModule.PC_PERMISSION,desc = "启用权限")
    public MyRespBundle<String> enablePermission(@PathVariable("id")Integer id ){

        String mes = permissionService.updatePermissionState(id, UserEnabled.Enabled_true.shortVal());

        return sendJsonData(ResultMessage.SUCCESS,mes);
    }

    /**
     * 禁用权限
     * @param id
     * @return
     */
    @ApiOperation(value="前端-运营平台-权限管理-禁用权限", notes="禁用权限")
    @PostMapping("/permission/{id}/disable")
    @MyRespBody
    @MySysLog(action = SysLogAction.CHANGE_STATE,module = SysLogModule.PC_PERMISSION,desc = "禁用权限")
    public MyRespBundle<String> disablePermission(@PathVariable("id")Integer id ){
        String mes = permissionService.updatePermissionState(id, UserEnabled.Disable.shortVal());
        return sendJsonData(ResultMessage.SUCCESS,mes);
    }

    /**
     * 删除权限
     * @param id
     * @return
     */
    @ApiOperation(value="前端-运营平台-权限管理-删除权限", notes="删除权限")
    @DeleteMapping("/permission/{id}")
    @MyRespBody
    @MySysLog(action = SysLogAction.DEL,module = SysLogModule.PC_PERMISSION,desc = "删除权限")
    public MyRespBundle<String> deletePermission(@PathVariable Integer id){

        String mes = permissionService.updatePermissionForDel(id);

        return sendSuccessMessage(mes);
    }


    /**
     * 新增角色
     * @param systemRoleVO
     * @return
     */
    @ApiOperation(value="前端-运营平台-角色管理-新增角色", notes="新增角色")
    @PostMapping("/role")
    @MyRespBody
    @MySysLog(action = SysLogAction.SAVE,module = SysLogModule.PC_PERMISSION,desc = "创建角色")
    public MyRespBundle<SystemRole> role(SystemRoleVO systemRoleVO){

        BeanValidator.validate(systemRoleVO);

        SystemRole result = systemRoleService.save(systemRoleVO);

        return sendJsonData(ResultMessage.SUCCESS,result);
    }

    /**
     * 分页查询角色
     * @param systemRoleSEO
     * @return
     */
    @ApiResponses({
            @ApiResponse(code = 200,message = "操作成功",response = SystemRoleVO.class)
    })
    @ApiOperation(value="前端-运营平台-角色管理", notes="角色列表")
    @GetMapping("/role")
    @MyRespBody
    public MyRespBundle<PageInfo<SystemRole>> roles(SystemRoleSEO systemRoleSEO){

        PageInfo<SystemRole> result = systemRoleService.page(systemRoleSEO);

        return sendJsonData(ResultMessage.SUCCESS,result);
    }


    /**
     * 角色详情
     * @param id
     * @return
     */
    @ApiResponses({
            @ApiResponse(code = 200,message = "操作成功",response = SystemRoleVO.class)
    })
    @ApiOperation(value="前端-运营平台-角色管理-角色详情", notes="角色详情")
    @GetMapping("/role/{id}")
    @MyRespBody
    public MyRespBundle<SystemRole> detail(@PathVariable("id")Integer id){
        SystemRole result = systemRoleService.detail(id);
        return sendJsonData(ResultMessage.SUCCESS,result);
    }

    /**
     * 编辑角色
     * @param id
     * @param systemRoleVO
     * @return
     */
    @ApiOperation(value="前端-运营平台-角色管理-编辑角色", notes="编辑角色")
    @PostMapping("/role/{id}")
    @MyRespBody
    @MySysLog(action = SysLogAction.EDIT,module = SysLogModule.PC_PERMISSION,desc = "编辑角色")
    public MyRespBundle<SystemRole> editRole(@PathVariable("id") Integer id ,SystemRoleVO systemRoleVO){
        BeanValidator.validate(systemRoleVO);
        systemRoleVO.setId(id);
        SystemRole result = systemRoleService.edit(systemRoleVO);
        return sendJsonData(ResultMessage.SUCCESS,result);
    }

    /**
     * 查看角色 权限状况
     * @param id
     * @return
     */
    @ApiResponses({
            @ApiResponse(code = 200,message = "操作成功",response = PermissionVO.class)
    })
    @ApiOperation(value="前端-运营平台-角色管理-角色权限状况", notes="角色权限状况")
    @GetMapping("/role/{id}/permission")
    @MyRespBody
    public MyRespBundle<List<SystemPermission>> rolePermission(@PathVariable Integer id ){

        List<SystemPermission> permissions = permissionService.listPermissionByRoleID(id);
        Collections.sort(permissions, Comparator.comparingInt(SystemPermission::getId));

        return sendJsonData(ResultMessage.SUCCESS,permissions);
    }

    /**
     * 角色授权
     * @param id
     * @param permissions
     * @return
     */
    @ApiOperation(value="前端-运营平台-角色管理-角色授权", notes="角色授权")
    @PostMapping("/role/{id}/permission")
    @MyRespBody
    @MySysLog(action = SysLogAction.GRANT,module = SysLogModule.PC_PERMISSION,desc = "授予权限")
    public MyRespBundle<String> grant(@PathVariable Integer id,@ApiParam("权限主键集合")@RequestParam(required = false,value = "permission") Integer[] permissions){

        String mes = systemRoleService.updateRoleByGrant(id,permissions);

        return sendSuccessMessage(mes);
    }

    /**
     * 停用角色
     * @param id
     * @return
     */
    @ApiOperation(value="前端-运营平台-角色管理-停用角色", notes="停用角色")
    @PostMapping("/role/{id}/disable")
    @MyRespBody
    @MySysLog(action = SysLogAction.CHANGE_STATE,module = SysLogModule.PC_PERMISSION,desc = "停用角色")
    public MyRespBundle<String>  disableRole(@PathVariable Integer id){
        String  mes = systemRoleService.updateRoleState(id, UserEnabled.Disable.shortVal());
        return sendSuccessMessage(mes);
    }

    /**
     * 启用角色
     * @param id
     * @return
     */
    @ApiOperation(value="前端-运营平台-角色管理-启用角色", notes="启用角色")
    @PostMapping("/role/{id}/enable")
    @MyRespBody
    @MySysLog(action = SysLogAction.CHANGE_STATE,module = SysLogModule.PC_PERMISSION,desc = "启用角色")
    public MyRespBundle<String> enableRole(@PathVariable Integer id){
        String  mes = systemRoleService.updateRoleState(id, UserEnabled.Enabled_true.shortVal());
        return sendSuccessMessage(mes);
    }

    /**
     * 删除角色
     * @param id
     * @return
     */
    @ApiOperation(value="前端-运营平台-角色管理-删除角色", notes="删除角色")
    @DeleteMapping("/role/{id}")
    @MyRespBody
    @MySysLog(action = SysLogAction.DEL,module = SysLogModule.PC_PERMISSION,desc = "删除角色")
    public MyRespBundle<String> delRole(@PathVariable Integer id){
        String mes = systemRoleService.updateRoleForDel(id);
        return sendSuccessMessage(mes);
    }


    /**
     * 账号管理--列表
     * @param accountSEO
     * @return
     */

    @ApiOperation(value="前端-运营平台-账号管理", notes="账号管理列表")
    @GetMapping("/info")
    @MyRespBody
    public MyRespBundle<PageInfo<AccountListVO>> accountList(AccountSEO accountSEO){
        PageInfo page = pcUserInfoService.pageAccountVO(accountSEO);
        return sendJsonData(ResultMessage.SUCCESS,page);
    }

    /**
     * 创建账号
     * @param accountVO
     * @return
     */
    @ApiOperation(value="前端-运营平台-账号管理-创建账号", notes="创建账号")
    @PostMapping("/info")
    @MyRespBody
    @MySysLog(action = SysLogAction.SAVE,module = SysLogModule.PC_PERMISSION,desc = "新建账号")
    public MyRespBundle<AccountVO> account(@RequestBody  AccountVO accountVO){
        AccountVO result = pcUserInfoService.saveUserAccount(accountVO);
        return sendJsonData(ResultMessage.SUCCESS,result);
    }

    /**
     * 查询账号详情
     * @param id
     * @return
     */
    @ApiOperation(value="前端-运营平台-账号管理-账号详情", notes="账号详情")
    @GetMapping("/info/{id}")
    @MyRespBody
    public MyRespBundle<AccountVO> details(@PathVariable String id){
        AccountVO result = pcUserInfoService.findAccountVOByID(id);
        return sendJsonData(ResultMessage.SUCCESS,result);
    }

    /**
     * 修改账号信息
     * @param id
     * @param accountVO
     * @return
     */
    @ApiOperation(value="前端-运营平台-账号管理-账号编辑", notes="账号编辑")
    @PostMapping("/info/{id}")
    @MyRespBody
    @MySysLog(action = SysLogAction.EDIT,module = SysLogModule.PC_PERMISSION,desc = "修改账号信息")
    public MyRespBundle<String> editInfo(@PathVariable String id,@RequestBody AccountVO accountVO){
        String mes = pcUserInfoService.updateAccountVO(id,accountVO);
        return sendSuccessMessage(mes);
    }

    /**
     * 重置密码
     * @param id
     * @return
     */
    @ApiOperation(value="前端-运营平台-账号管理-账号编辑-重置密码", notes="重置密码")
    @PostMapping("/info/{id}/reset")
    @MyRespBody
    @MySysLog(action = SysLogAction.EDIT,module = SysLogModule.PC_PERMISSION,desc = "重置密码")
    public MyRespBundle<String> resetPassWord(@PathVariable String id){
        String mes = pcUserInfoService.updateForResetPassWord(id);
        return sendSuccessMessage(mes);
    }


    /**
     * 删除账号
     * @param id
     * @return
     */
    @ApiOperation(value="前端-运营平台-账号管理-删除账号", notes="删除账号")
    @DeleteMapping("/info/{id}")
    @MyRespBody
    @MySysLog(action = SysLogAction.DEL,module = SysLogModule.PC_PERMISSION,desc = "删除账号")
    public MyRespBundle<String> delAccount(@PathVariable String id){
        String mes = pcUserInfoService.delAccountByID(id);
        return sendSuccessMessage(mes);
    }


    /**
     * 启用账号
     * @param id
     * @return
     */
    @ApiOperation(value="前端-运营平台-账号管理-启用账号", notes="启用账号")
    @PostMapping("/info/{id}/enable")
    @MyRespBody
    @MySysLog(action = SysLogAction.CHANGE_STATE,module = SysLogModule.PC_PERMISSION,desc = "启用账号")
    public MyRespBundle<String> enableAccount(@PathVariable String id){
        String mes = pcUserInfoService.updateAccountState(id,UserEnabled.Enabled_true.code);
        return sendSuccessMessage(SUCCESS.message);
    }

    /**
     * 禁用账号
     * @param id
     * @return
     */
    @ApiOperation(value="前端-运营平台-账号管理-禁用账号", notes="禁用账号")
    @ApiResponses({
            @ApiResponse(code = 200,message = "操作成功"),
            @ApiResponse(code = 500,message = "异常")
    })
    @PostMapping("/info/{id}/disable")
    @MyRespBody
    @MySysLog(action = SysLogAction.CHANGE_STATE,module = SysLogModule.PC_PERMISSION,desc = "停用账号")
    public MyRespBundle<String> disableAccount(@PathVariable String id){
        String mes = pcUserInfoService.updateAccountState(id,UserEnabled.Disable.code);
        return sendSuccessMessage(SUCCESS.message);
    }
    /**
     * 初次登录初始化密码
     * @param id
     * @return
     */
    @ApiOperation(value="前端-运营平台-初次登录", notes="初次登录重置密码")
    @PostMapping("/info/{id}/init")
    @MyRespBody
    @MySysLog(action = SysLogAction.EDIT,module = SysLogModule.PC_PERMISSION,desc = "初次登录重置密码")
    public MyRespBundle<String> initPassWord(@PathVariable String id,@ApiParam("密码") String passWord){
        String mes = pcUserInfoService.updatePassWordForInit(id,passWord);
        return sendSuccessMessage(SUCCESS.message);
    }


    /**
     * 创建企业角色类型
     * @param userRoleSet
     * @return
     */
    @ApiOperation(value="前端-运营平台-创建企业角色类型", notes="创建企业角色类型")
    @PostMapping("/enterprise/role")
    @MyRespBody
    @MySysLog(action = SysLogAction.SAVE,module = SysLogModule.PC_PERMISSION,desc = "创建企业角色")
    public MyRespBundle<String> enterpriseRole(UserRoleSet userRoleSet){
        String mes = userRoleSetService.saveEnterPriseRole(userRoleSet);
        return sendJsonData(ResultMessage.SUCCESS,mes);
    }

    /**
     * 查询企业角色类型
     * @param id
     * @return
     */
    @ApiOperation(value="前端-运营平台-查询企业角色类型", notes="企业角色类型详情")
    @GetMapping("/enterprise/role/{id}")
    @MyRespBody
    public MyRespBundle<UserRoleSet> detailEnterPriseRole(@PathVariable Integer id){
        UserRoleSet result = userRoleSetService.findUserRoleSetVO(id);
        return sendJsonData(ResultMessage.SUCCESS,result);
    }

    /**
     * 企业角色类型列表
     * @param userRoleSetSEO
     * @return
     */
    @ApiOperation(value="前端-运营平台-企业角色类型列表", notes="企业角色类型列表")
    @GetMapping("/enterprise/role")
    @MyRespBody
    public MyRespBundle<PageInfo<UserRoleSet>>  enterpriseRoles(UserRoleSetSEO userRoleSetSEO){
        PageInfo<UserRoleSet> result = userRoleSetService.pageEnterPriseRole(userRoleSetSEO);
        return sendJsonData(ResultMessage.SUCCESS,result);
    }

    /**
     * 企业角色编辑
     * @param id
     * @param userRoleSet
     * @return
     */
    @ApiOperation(value="前端-运营平台-企业角色类型编辑", notes="企业角色类型编辑")
    @PostMapping("/enterprise/role/{id}")
    @MyRespBody
    @MySysLog(action = SysLogAction.EDIT,module = SysLogModule.PC_PERMISSION,desc = "企业角色编辑")
    public MyRespBundle<String> enterPriseRole(@PathVariable Integer id,UserRoleSet userRoleSet){
        String mes = userRoleSetService.updateEnterPriseRole(id,userRoleSet);
        return sendSuccessMessage(mes);
    }

    /**
     * 获取企业角色资源
     * @param id
     * @return
     */
    @ApiOperation(value="前端-运营平台-企业角色类型资源", notes="企业角色类型资源")
    @GetMapping("/enterprise/role/{id}/resource")
    @MyRespBody
    public MyRespBundle<List<SystemResource>> enterPriseRoleResource(@PathVariable Integer id){
        List<SystemResource> resources = systemResourceService.listResourceByEnterPriseRoleID(id);
        Collections.sort(resources, Comparator.comparingInt(SystemResource::getSortNum));
        return sendJsonData(ResultMessage.SUCCESS,resources);
    }

    /**
     * 编辑企业角色资源
     * @param id
     * @param resources
     * @return
     */
    @ApiOperation(value="前端-运营平台-企业角色类型资源分配", notes="企业角色类型资源分配")
    @PostMapping("/enterprise/role/{id}/resource")
    @MyRespBody
    public MyRespBundle<String> enterPriseRoleGrantResource(@PathVariable Integer id,@RequestParam(value = "resource",required = false) Integer[] resources){

        String mes = CompanyUserService.updateEnterPriseRoleResource(id,resources);

        return sendJsonData(ResultMessage.SUCCESS,mes);
    }


    /**
     * 变更个人信息
     * @param changeMeVO
     * @return
     */
    @ApiOperation(value="前端-运营平台-修改个人信息", notes="修改个人信息")
    @PostMapping("/me")
    @MyRespBody
    @MySysLog(action = SysLogAction.EDIT,module = SysLogModule.PC_PERMISSION,desc = "修改个人信息")
    public MyRespBundle<String> changeMe(ChangeMeVO changeMeVO){
        String mes = userService.updateUserInfo(changeMeVO);
        return sendJsonData(ResultMessage.SUCCESS,mes);
    }


    /**
     * 获取个人信息
     * @return
     */
    @ApiOperation(value="前端-运营平台-获取个人信息", notes="获取个人信息 first是否第一次登陆 companyName公司名称")
    @GetMapping("/me")
    @MyRespBody
    public MyRespBundle<Map<String,String>> me(){
        UserVO userVO = (UserVO) SessionUserDetailsUtil.getUserDetails();
        Map<String,String> result = new HashMap<>(20);
        result.put("userName",userVO.getUserRegister().getPhone());
        result.put("companyName",userVO.getCompanyName());
        result.put("face",userVO.getUserRegister().getHeadPortraits());
        result.put("name",userVO.getName());
        result.put("first",userService.isFirstLogin());
        result.put("companyId", userVO.getCompanyID());
        return sendJsonData(ResultMessage.SUCCESS,result);
    }




}
