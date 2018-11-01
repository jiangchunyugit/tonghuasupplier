package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.model.EmployeeMsg;
import cn.thinkfree.database.model.UserRoleSet;
import cn.thinkfree.service.platform.employee.EmployeeService;
import cn.thinkfree.service.platform.vo.EmployeeMsgVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
@Api(value = "员工管理API接口", tags = "员工管理API接口")
@Controller
@RequestMapping("employee")
public class EmployeeController extends AbsBaseController {
    @Autowired
    private EmployeeService employeeService;

    @ApiOperation("员工实名认证审核")
    @MyRespBody
    @RequestMapping(value = "review", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle reviewDesigner(
            @ApiParam(name = "userId", required = false, value = "员工ID") @RequestParam(name = "userId", required = false) String userId,
            @ApiParam(name = "authState", required = false, value = "审核状态") @RequestParam(name = "authState", required = false) int authState,
            @ApiParam(name = "companyId", required = false, value = "公司ID") @RequestParam(name = "companyId", required = false) String companyId) {
        try{
            employeeService.reviewEmployee(userId, authState, companyId);
        }catch (Exception e){
            return sendFailMessage(e.getMessage());
        }
        return sendJsonData(ResultMessage.SUCCESS, null);
    }

    @ApiOperation("提交证件信息")
    @MyRespBody
    @RequestMapping(value = "submitCardMsg", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle submitCardMsg(
            @ApiParam(name = "userId", required = false, value = "员工ID") @RequestParam(name = "userId", required = false) String userId,
            @ApiParam(name = "cardType", required = false, value = "证件类型") @RequestParam(name = "cardType", required = false) int cardType,
            @ApiParam(name = "cardNo", required = false, value = "证件号码") @RequestParam(name = "cardNo", required = false) String cardNo,
            @ApiParam(name = "realName", required = false, value = "真实姓名") @RequestParam(name = "realName", required = false) String realName,
            @ApiParam(name = "idCardUrl1", required = false, value = "证件正面") @RequestParam(name = "idCardUrl1", required = false) String idCardUrl1,
            @ApiParam(name = "idCardUrl2", required = false, value = "证件反面") @RequestParam(name = "idCardUrl2", required = false) String idCardUrl2,
            @ApiParam(name = "idCardUrl3", required = false, value = "手持证件") @RequestParam(name = "idCardUrl3", required = false) String idCardUrl3) {
        try{
            employeeService.submitCardMsg(userId, cardType, cardNo, realName, idCardUrl1, idCardUrl2, idCardUrl3);
        }catch (Exception e){
            return sendFailMessage(e.getMessage());
        }
        return sendJsonData(ResultMessage.SUCCESS, null);
    }

    @ApiOperation("员工申请")
    @MyRespBody
    @RequestMapping(value = "apply", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle apply(
            @ApiParam(name = "userId", required = false, value = "员工ID") @RequestParam(name = "userId", required = false) String userId,
            @ApiParam(name = "employeeApplyState", required = false, value = "员工申请状态") @RequestParam(name = "employeeApplyState", required = false) int employeeApplyState,
            @ApiParam(name = "companyId", required = false, value = "公司ID") @RequestParam(name = "companyId", required = false) String companyId) {
        try{
            employeeService.employeeApply(userId, employeeApplyState, companyId);
        }catch (Exception e){
            return sendFailMessage(e.getMessage());
        }
        return sendJsonData(ResultMessage.SUCCESS, null);
    }

    @ApiOperation("处理员工申请")
    @MyRespBody
    @RequestMapping(value = "dealApply", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle dealApply(
            @ApiParam(name = "userId", required = false, value = "员工ID") @RequestParam(name = "userId", required = false) String userId,
            @ApiParam(name = "employeeApplyState", required = false, value = "员工申请状态") @RequestParam(name = "employeeApplyState", required = false) int employeeApplyState,
            @ApiParam(name = "dealExplain", required = false, value = "处理结果") @RequestParam(name = "dealExplain", required = false) String dealExplain,
            @ApiParam(name = "dealUserId", required = false, value = "处理人ID") @RequestParam(name = "dealUserId", required = false) String dealUserId,
            @ApiParam(name = "companyId", required = false, value = "公司ID") @RequestParam(name = "companyId", required = false) String companyId) {
        try{
            employeeService.dealApply(userId, employeeApplyState, dealExplain, dealUserId, companyId);
        }catch (Exception e){
            return sendFailMessage(e.getMessage());
        }
        return sendJsonData(ResultMessage.SUCCESS, null);
    }

    @ApiOperation("查询所有角色")
    @MyRespBody
    @RequestMapping(value = "queryRoles", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<List<UserRoleSet>> queryRoles() {
        List<UserRoleSet> userRoleSets = employeeService.queryRoles();
        return sendJsonData(ResultMessage.SUCCESS, userRoleSets);
    }

    @ApiOperation("设置用户角色")
    @MyRespBody
    @RequestMapping(value = "setUserRole", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle setUserRole(
            @ApiParam(name = "userId", required = false, value = "员工ID") @RequestParam(name = "userId", required = false) String userId,
            @ApiParam(name = "roleCode", required = false, value = "角色编码") @RequestParam(name = "roleCode", required = false) String roleCode,
            @ApiParam(name = "companyId", required = false, value = "公司ID") @RequestParam(name = "companyId", required = false) String companyId) {
        try{
            employeeService.setUserRole(userId, roleCode, companyId);
        }catch (Exception e){
            return sendFailMessage(e.getMessage());
        }
        return sendJsonData(ResultMessage.SUCCESS, null);
    }

    @ApiOperation("根据用户ID获取用户信息")
    @MyRespBody
    @RequestMapping(value = "msg", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<EmployeeMsgVo> msg(
            @ApiParam(name = "userId", required = false, value = "员工ID") @RequestParam(name = "userId", required = false) String userId){
        try{
            EmployeeMsgVo employeeMsg = employeeService.employeeMsgById(userId);
            return sendJsonData(ResultMessage.SUCCESS, employeeMsg);
        }catch (Exception e){
            return sendFailMessage(e.getMessage());
        }
    }

}
