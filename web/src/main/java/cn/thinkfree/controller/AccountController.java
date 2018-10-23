package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.AppParameter;
import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.annotation.MySysLog;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.core.constants.SysConstants;
import cn.thinkfree.core.constants.SysLogAction;
import cn.thinkfree.core.constants.SysLogModule;
import cn.thinkfree.database.constants.UserEnabled;
import cn.thinkfree.database.model.SystemPermission;
import cn.thinkfree.database.model.SystemPermissionResource;
import cn.thinkfree.database.model.SystemResource;
import cn.thinkfree.database.model.SystemRole;
import cn.thinkfree.database.utils.BeanValidator;
import cn.thinkfree.database.vo.account.*;
import cn.thinkfree.service.account.PermissionResourceService;
import cn.thinkfree.service.account.PermissionService;
import cn.thinkfree.service.account.SystemResourceService;
import cn.thinkfree.service.account.SystemRoleService;
import cn.thinkfree.service.pcUser.PcUserInfoService;
import cn.thinkfree.service.userResource.PcSystemResourceService;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 账号相关
 */
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

    /**
     * 创建权限
     * @param permissionVO 权限
     * @return
     */
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
    @GetMapping("/permission/{id}/resource")
    @MyRespBody
    public MyRespBundle<List> resource(@PathVariable("id")Integer id){
        List<SystemResource> resources = systemResourceService.listResourceByPermissionID(id);
        Collections.sort(resources, Comparator.comparingInt(SystemResource::getId));
        return sendJsonData(ResultMessage.SUCCESS,resources);
    }


    /**
     * 为权限分配资源
     * @param id
     * @param resources
     * @return
     */
    @PostMapping("/permission/{id}/resource")
    @MyRespBody
    @MySysLog(action = SysLogAction.GRANT,module = SysLogModule.PC_PERMISSION,desc = "分配资源")
    public MyRespBundle<List> authorize(@PathVariable("id")Integer id,@RequestParam(value = "resource",required = false) Integer[] resources){

        String mes = permissionResourceService.updateSystemPermissionResource(id,resources);

        return sendJsonData(ResultMessage.SUCCESS,mes);
    }

    /**
     * 启用权限
     * @param id
     * @return
     */
    @PostMapping("/permission/{id}/enable")
    @MyRespBody
    @MySysLog(action = SysLogAction.CHANGE_STATE,module = SysLogModule.PC_PERMISSION,desc = "启用权限")
    public MyRespBundle<List> enablePermission(@PathVariable("id")Integer id ){

        String mes = permissionService.updatePermissionState(id, UserEnabled.Enabled_true.shortVal());

        return sendJsonData(ResultMessage.SUCCESS,mes);
    }

    /**
     * 禁用权限
     * @param id
     * @return
     */
    @PostMapping("/permission/{id}/disable")
    @MyRespBody
    @MySysLog(action = SysLogAction.CHANGE_STATE,module = SysLogModule.PC_PERMISSION,desc = "禁用权限")
    public MyRespBundle<List> disablePermission(@PathVariable("id")Integer id ){
        String mes = permissionService.updatePermissionState(id, UserEnabled.Disable.shortVal());
        return sendJsonData(ResultMessage.SUCCESS,mes);
    }

    /**
     * 删除权限
     * @param id
     * @return
     */
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
    @PostMapping("/role/{id}/permission")
    @MyRespBody
    @MySysLog(action = SysLogAction.GRANT,module = SysLogModule.PC_PERMISSION,desc = "授予权限")
    public MyRespBundle<String> grant(@PathVariable Integer id,@RequestParam(required = false,value = "permission") Integer[] permissions){

        String mes = systemRoleService.updateRoleByGrant(id,permissions);

        return sendSuccessMessage(mes);
    }

    /**
     * 停用角色
     * @param id
     * @return
     */
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
    @PostMapping("/role/{id}/enable")
    @MyRespBody
    @MySysLog(action = SysLogAction.CHANGE_STATE,module = SysLogModule.PC_PERMISSION,desc = "启用角色")
    public MyRespBundle<String>  enableRole(@PathVariable Integer id){
        String  mes = systemRoleService.updateRoleState(id, UserEnabled.Enabled_true.shortVal());
        return sendSuccessMessage(mes);
    }

    /**
     * 删除角色
     * @param id
     * @return
     */
    @DeleteMapping("/role/{id}")
    @MyRespBody
    @MySysLog(action = SysLogAction.DEL,module = SysLogModule.PC_PERMISSION,desc = "删除角色")
    public MyRespBundle<String> delRole(@PathVariable Integer id){
        String mes = systemRoleService.updateRoleForDel(id);
        return sendSuccessMessage(mes);
    }




    @PostMapping("/info")
    @MyRespBody
    @MySysLog(action = SysLogAction.SAVE,module = SysLogModule.PC_PERMISSION,desc = "新建账号")
    public MyRespBundle<AccountVO> account(AccountVO accountVO){


         AccountVO  result=pcUserInfoService.saveUserAccount(accountVO);
        return null;
    }



}
