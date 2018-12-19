package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.core.constants.RoleFunctionEnum;
import cn.thinkfree.database.model.EmployeeMsg;
import cn.thinkfree.service.platform.basics.RoleFunctionService;
import cn.thinkfree.service.platform.employee.EmployeeService;
import cn.thinkfree.service.platform.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author xusonghui
 * 员工管理
 */
@Api(value = "员工管理API接口", tags = "员工管理API接口--->app后台公用===>徐松辉")
@Controller
@RequestMapping("employee")
public class EmployeeController extends AbsBaseController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private EmployeeService employeeService;

    @ApiOperation("员工实名认证审核--->王玲组专用")
    @MyRespBody
    @RequestMapping(value = "review", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle reviewDesigner(
            @ApiParam(name = "userId", required = false, value = "员工ID") @RequestParam(name = "userId", required = false) String userId,
            @ApiParam(name = "authState", required = false, value = "审核状态,2审核通过，4审核不通过") @RequestParam(name = "authState", required = false) int authState,
            @ApiParam(name = "reason", required = false, value = "审核不通过的原因") @RequestParam(name = "reason", required = false) String reason) {
        try {
            employeeService.reviewEmployee(userId, authState, reason);
        } catch (Exception e) {
            return sendFailMessage(e.getMessage());
        }
        return sendJsonData(ResultMessage.SUCCESS, null);
    }

    @ApiOperation("提交证件信息--->app专用")
    @MyRespBody
    @RequestMapping(value = "submitCardMsg", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle submitCardMsg(
            @ApiParam(name = "userId", required = false, value = "员工ID") @RequestParam(name = "userId", required = false) String userId,
            @ApiParam(name = "cardType", required = false, value = "证件类型") @RequestParam(name = "cardType", required = false) int cardType,
            @ApiParam(name = "cardNo", required = false, value = "证件号码") @RequestParam(name = "cardNo", required = false) String cardNo,
            @ApiParam(name = "realName", required = false, value = "真实姓名") @RequestParam(name = "realName", required = false) String realName,
            @ApiParam(name = "country", required = false, value = "所属国家编码") @RequestParam(name = "country", required = false) String country,
            @ApiParam(name = "idCardUrl1", required = false, value = "证件正面") @RequestParam(name = "idCardUrl1", required = false) String idCardUrl1,
            @ApiParam(name = "idCardUrl2", required = false, value = "证件反面") @RequestParam(name = "idCardUrl2", required = false) String idCardUrl2,
            @ApiParam(name = "idCardUrl3", required = false, value = "手持证件") @RequestParam(name = "idCardUrl3", required = false) String idCardUrl3) {
        try {
            employeeService.submitCardMsg(userId, cardType, cardNo, realName, country, idCardUrl1, idCardUrl2, idCardUrl3);
        } catch (Exception e) {
            return sendFailMessage(e.getMessage());
        }
        return sendJsonData(ResultMessage.SUCCESS, null);
    }

    @ApiOperation("员工绑定公司申请--->app专用")
    @MyRespBody
    @RequestMapping(value = "apply", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle apply(
            @ApiParam(name = "userId", required = false, value = "员工ID") @RequestParam(name = "userId", required = false) String userId,
            @ApiParam(name = "employeeApplyState", required = false, value = "员工申请状态1入驻待审核，4解约待审核") @RequestParam(name = "employeeApplyState", required = false) int employeeApplyState,
            @ApiParam(name = "companyId", required = false, value = "公司ID") @RequestParam(name = "companyId", required = false) String companyId,
            @ApiParam(name = "reason", required = false, value = "解约原因") @RequestParam(name = "reason", required = false) String reason) {
        try {
            employeeService.employeeApply(userId, employeeApplyState, companyId, reason);
        } catch (Exception e) {
            return sendFailMessage(e.getMessage());
        }
        return sendJsonData(ResultMessage.SUCCESS, null);
    }

    @ApiOperation("处理员工申请--->王玲组专用")
    @MyRespBody
    @RequestMapping(value = "dealApply", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle dealApply(
            @ApiParam(name = "userId", required = false, value = "员工ID") @RequestParam(name = "userId", required = false) String userId,
            @ApiParam(name = "employeeApplyState", required = false, value = "员工申请状态，1入驻待审核，2入驻不通过，3已入驻，4解约待审核，5解约不通过，6已解约") @RequestParam(name = "employeeApplyState", required = false) int employeeApplyState,
            @ApiParam(name = "dealExplain", required = false, value = "处理结果") @RequestParam(name = "dealExplain", required = false) String dealExplain,
            @ApiParam(name = "dealUserId", required = false, value = "处理人ID") @RequestParam(name = "dealUserId", required = false) String dealUserId,
            @ApiParam(name = "roleCode", required = false, value = "该员工所属角色编码") @RequestParam(name = "roleCode", required = false) String roleCode,
            @ApiParam(name = "companyId", required = false, value = "公司ID") @RequestParam(name = "companyId", required = false) String companyId) {
        try {
            employeeService.dealApply(userId, employeeApplyState, dealExplain, dealUserId, roleCode, companyId);
        } catch (Exception e) {
            return sendFailMessage(e.getMessage());
        }
        return sendJsonData(ResultMessage.SUCCESS, null);
    }

    @ApiOperation("查询所有角色--->谁用谁掉接口，无参数")
    @MyRespBody
    @RequestMapping(value = "queryRoles", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<PageVo<List<RoleVo>>> queryRoles(
            @ApiParam(name = "searchKey", required = false, value = "搜索关键字") @RequestParam(name = "searchKey", required = false) String searchKey,
            @ApiParam(name = "state", required = false, value = "状态，-1全部，1启用，2未启用，3禁用") @RequestParam(name = "state", required = false, defaultValue = "-1") int state,
            @ApiParam(name = "pageSize", required = false, value = "每页多少条") @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
            @ApiParam(name = "pageIndex", required = false, value = "第几页，从1开始") @RequestParam(name = "pageIndex", required = false, defaultValue = "1") int pageIndex) {
        PageVo<List<RoleVo>> userRoleSets = employeeService.queryRoles(searchKey, state, pageSize, pageIndex);
        return sendJsonData(ResultMessage.SUCCESS, userRoleSets);
    }

    @ApiOperation("创建角色--->王玲组专用")
    @MyRespBody
    @RequestMapping(value = "createRole", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle createRole(
            @ApiParam(name = "roleName", required = false, value = "角色名称") @RequestParam(name = "roleName", required = false) String roleName,
            @ApiParam(name = "remark", required = false, value = "备注") @RequestParam(name = "remark", required = false) String remark) {
        try {
            employeeService.createRole(roleName, remark);
        } catch (Exception e) {
            return sendFailMessage(e.getMessage());
        }
        return sendJsonData(ResultMessage.SUCCESS, null);
    }

    @ApiOperation("编辑角色--->王玲组专用")
    @MyRespBody
    @RequestMapping(value = "editRole", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle editRole(
            @ApiParam(name = "roleCode", required = false, value = "角色编码") @RequestParam(name = "roleCode", required = false) String roleCode,
            @ApiParam(name = "roleName", required = false, value = "角色名称") @RequestParam(name = "roleName", required = false) String roleName,
            @ApiParam(name = "remark", required = false, value = "备注") @RequestParam(name = "remark", required = false) String remark) {
        try {
            employeeService.editRole(roleCode, roleName, remark);
        } catch (Exception e) {
            return sendFailMessage(e.getMessage());
        }
        return sendJsonData(ResultMessage.SUCCESS, null);
    }

    @ApiOperation("删除角色--->王玲组专用")
    @MyRespBody
    @RequestMapping(value = "delRole", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle delRole(
            @ApiParam(name = "roleCode", required = false, value = "角色编码") @RequestParam(name = "roleCode", required = false) String roleCode) {
        try {
            employeeService.delRole(roleCode);
        } catch (Exception e) {
            return sendFailMessage(e.getMessage());
        }
        return sendJsonData(ResultMessage.SUCCESS, null);
    }

    @ApiOperation("启用/禁用角色--->王玲组专用")
    @MyRespBody
    @RequestMapping(value = "enableRole", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle enableRole(
            @ApiParam(name = "roleCode", required = false, value = "角色编码") @RequestParam(name = "roleCode", required = false) String roleCode,
            @ApiParam(name = "state", required = false, value = "状态，1启用，2未启用，3禁用") @RequestParam(name = "state", required = false, defaultValue = "-1") int state) {
        try {
            employeeService.enableRole(roleCode, state);
        } catch (Exception e) {
            return sendFailMessage(e.getMessage());
        }
        return sendJsonData(ResultMessage.SUCCESS, null);
    }

    @ApiOperation("设置用户角色--->王玲组专用")
    @MyRespBody
    @RequestMapping(value = "setUserRole", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle setUserRole(
            @ApiParam(name = "userId", required = false, value = "员工ID") @RequestParam(name = "userId", required = false) String userId,
            @ApiParam(name = "roleCode", required = false, value = "角色编码") @RequestParam(name = "roleCode", required = false) String roleCode,
            @ApiParam(name = "companyId", required = false, value = "公司ID") @RequestParam(name = "companyId", required = false) String companyId) {
        try {
            employeeService.setUserRole(userId, roleCode, companyId);
        } catch (Exception e) {
            return sendFailMessage(e.getMessage());
        }
        return sendJsonData(ResultMessage.SUCCESS, null);
    }

    @ApiOperation("根据用户ID获取用户信息--->公用")
    @MyRespBody
    @RequestMapping(value = "msg", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<EmployeeMsgVo> msg(
            @ApiParam(name = "userId", required = false, value = "员工ID") @RequestParam(name = "userId", required = false) String userId) {
        try {
            EmployeeMsgVo employeeMsg = employeeService.employeeMsgById(userId);
            return sendJsonData(ResultMessage.SUCCESS, employeeMsg);
        } catch (Exception e) {
            return sendFailMessage(e.getMessage());
        }
    }


    @ApiOperation("根据角色和公司ID查询员工信息--->装饰公司/设计公司用")
    @MyRespBody
    @RequestMapping(value = "queryStaff", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<PageVo<List<EmployeeMsgVo>>> queryStaff(
            @ApiParam(name = "companyId", required = false, value = "公司ID") @RequestParam(name = "companyId", required = false) String companyId,
            @ApiParam(name = "roleCode", required = false, value = "角色编码") @RequestParam(name = "roleCode", required = false) String roleCode,
            @ApiParam(name = "searchKey", required = false, value = "搜索关键字") @RequestParam(name = "searchKey", required = false) String searchKey,
            @ApiParam(name = "pageSize", required = false, value = "每页多少条") @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
            @ApiParam(name = "pageIndex", required = false, value = "第几页，从1开始") @RequestParam(name = "pageIndex", required = false, defaultValue = "1") int pageIndex) {
        try {
            PageVo<List<EmployeeMsgVo>> pageVo = employeeService.queryEmployee(companyId, roleCode, searchKey, pageSize, pageIndex);
            return sendJsonData(ResultMessage.SUCCESS, pageVo);
        } catch (Exception e) {
            return sendFailMessage(e.getMessage());
        }
    }

    @Autowired
    private RoleFunctionService roleFunctionService;

    @ApiOperation("根据角色和公司ID查询员工信息--->设计公司设计师列表用")
    @MyRespBody
    @RequestMapping(value = "queryStaff/designCompanyId", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<PageVo<List<EmployeeMsgVo>>> queryStaffByDesignCompanyId(
            @ApiParam(name = "companyId", required = false, value = "公司ID") @RequestParam(name = "companyId", required = false) String companyId,
            @ApiParam(name = "searchKey", required = false, value = "搜索关键字") @RequestParam(name = "searchKey", required = false) String searchKey,
            @ApiParam(name = "pageSize", required = false, value = "每页多少条") @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
            @ApiParam(name = "pageIndex", required = false, value = "第几页，从1开始") @RequestParam(name = "pageIndex", required = false, defaultValue = "1") int pageIndex) {
        try {
            String roleCode = roleFunctionService.queryRoleCode(RoleFunctionEnum.DESIGN_POWER);
            if(StringUtils.isBlank(roleCode)){
                throw new RuntimeException("没有查询到设计师编码");
            }
            PageVo<List<EmployeeMsgVo>> pageVo = employeeService.queryStaffByDesignCompanyId(companyId, roleCode, searchKey, pageSize, pageIndex);
            return sendJsonData(ResultMessage.SUCCESS, pageVo);
        } catch (Exception e) {
            return sendFailMessage(e.getMessage());
        }
    }

    @ApiOperation("根据角色和公司ID查询设计师列表--->指派设计师，获取设计师列表===》王玲")
    @MyRespBody
    @RequestMapping(value = "designer/companyId", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<List<EmployeeMsg>> queryDesignerByCompanyId(
            @ApiParam(name = "companyId", required = false, value = "公司ID") @RequestParam(name = "companyId", required = false) String companyId) {
        try {
            String roleCode = roleFunctionService.queryRoleCode(RoleFunctionEnum.DESIGN_POWER);
            if(StringUtils.isBlank(roleCode)){
                throw new RuntimeException("没有查询到设计师编码");
            }
            List<EmployeeMsg> msgVos = employeeService.queryDesignerByCompanyId(companyId, roleCode);
            return sendJsonData(ResultMessage.SUCCESS, msgVos);
        } catch (Exception e) {
            return sendFailMessage(e.getMessage());
        }
    }

    @ApiOperation("根据角色查询员工信息--->运营后台用")
    @MyRespBody
    @RequestMapping(value = "queryStaffByPlatform", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<PageVo<List<EmployeeMsgVo>>> queryStaffByPlatform(
            @ApiParam(name = "roleCode", required = false, value = "角色编码") @RequestParam(name = "roleCode", required = false) String roleCode,
            @ApiParam(name = "searchKey", required = false, value = "搜索关键字") @RequestParam(name = "searchKey", required = false) String searchKey,
            @ApiParam(name = "city", required = false, value = "城市编码") @RequestParam(name = "city", required = false) String city,
            @ApiParam(name = "pageSize", required = false, value = "每页多少条") @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
            @ApiParam(name = "pageIndex", required = false, value = "第几页，从1开始") @RequestParam(name = "pageIndex", required = false, defaultValue = "1") int pageIndex) {
        try {
            PageVo<List<EmployeeMsgVo>> pageVo = employeeService.queryStaffByPlatform(roleCode, searchKey, city, pageSize, pageIndex);
            return sendJsonData(ResultMessage.SUCCESS, pageVo);
        } catch (Exception e) {
            logger.error("e:", e);
            return sendFailMessage(e.getMessage());
        }
    }

    @ApiOperation("根据角色查询员工信息--->运营后台用")
    @MyRespBody
    @RequestMapping(value = "waitDealList", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<PageVo<List<EmployeeApplyVo>>> waitDealList(
            @ApiParam(name = "searchKey", required = false, value = "搜索关键字（证件号，姓名，手机号）") @RequestParam(name = "searchKey", required = false) String searchKey,
            @ApiParam(name = "companyId", required = false, value = "公司ID") @RequestParam(name = "companyId", required = false) String companyId,
            @ApiParam(name = "companyType", required = false, value = "公司类型，1装饰，2设计") @RequestParam(name = "companyType", required = false) int companyType,
            @ApiParam(name = "pageSize", required = false, value = "每页多少条") @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
            @ApiParam(name = "pageIndex", required = false, value = "第几页，从1开始") @RequestParam(name = "pageIndex", required = false, defaultValue = "1") int pageIndex) {
        try {
            PageVo<List<EmployeeApplyVo>> pageVo = employeeService.waitDealList(searchKey, companyId, companyType, pageSize, pageIndex);
            return sendJsonData(ResultMessage.SUCCESS, pageVo);
        } catch (Exception e) {
            logger.error("e:", e);
            return sendFailMessage(e.getMessage());
        }
    }

    @ApiOperation("查询所有已提交实名认证审核的用户数据--->运营后台用")
    @MyRespBody
    @RequestMapping(value = "queryAllEmployee", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<PageVo<List<EmployeeMsgVo>>> queryAllEmployee(
            @ApiParam(name = "phone", required = false, value = "手机号（模糊）") @RequestParam(name = "phone", required = false) String phone,
            @ApiParam(name = "name", required = false, value = "员工姓名（模糊）") @RequestParam(name = "name", required = false) String name,
            @ApiParam(name = "cardNo", required = false, value = "证件号码（模糊）") @RequestParam(name = "cardNo", required = false) String cardNo,
            @ApiParam(name = "pageSize", required = false, value = "每页多少条") @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
            @ApiParam(name = "pageIndex", required = false, value = "第几页，从1开始") @RequestParam(name = "pageIndex", required = false, defaultValue = "1") int pageIndex) {
        try {
            PageVo<List<EmployeeMsgVo>> pageVo = employeeService.queryAllEmployee(phone, name, cardNo, pageSize, pageIndex);
            return sendJsonData(ResultMessage.SUCCESS, pageVo);
        } catch (Exception e) {
            logger.error("e:", e);
            return sendFailMessage(e.getMessage());
        }
    }

    @ApiOperation("查询员工和项目的关联关系")
    @MyRespBody
    @RequestMapping(value = "queryRelationProject", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<EmployeeAndProjectMsgVo> queryRelationProject(
            @ApiParam(name = "userId", required = false, value = "用户ID") @RequestParam(name = "userId", required = false) String userId){
        try {
            EmployeeAndProjectMsgVo msgVo = employeeService.queryRelationProject(userId);
            return sendJsonData(ResultMessage.SUCCESS, msgVo);
        } catch (Exception e) {
            logger.error("e:", e);
            return sendFailMessage(e.getMessage());
        }
    }

    @ApiOperation("移除员工")
    @MyRespBody
    @RequestMapping(value = "removeEmployee", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<String> removeEmployee(
            @ApiParam(name = "employeeId", value = "员工ID") @RequestParam(name = "employeeId", required = false) String employeeId,
            @ApiParam(name = "dealExplain", required = false, value = "处理结果") @RequestParam(name = "dealExplain", required = false) String dealExplain,
            @ApiParam(name = "dealUserId", required = false, value = "处理人ID") @RequestParam(name = "dealUserId", required = false) String dealUserId,
            @ApiParam(name = "roleCode", required = false, value = "该员工所属角色编码") @RequestParam(name = "roleCode", required = false) String roleCode,
            @ApiParam(name = "companyId", required = false, value = "公司ID") @RequestParam(name = "companyId", required = false) String companyId){
        try {
            employeeService.removeEmployee(employeeId, dealExplain, dealUserId, roleCode, companyId);
            return sendSuccessMessage(null);
        } catch (Exception e) {
            logger.error("e:", e);
            return sendFailMessage(e.getMessage());
        }
    }

}
