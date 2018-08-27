package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.annotation.MySysLog;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.core.constants.SysLogAction;
import cn.thinkfree.core.constants.SysLogModule;
import cn.thinkfree.core.security.filter.util.SessionUserDetailsUtil;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.vo.*;
import cn.thinkfree.service.project.ProjectService;
import cn.thinkfree.service.staff.StaffService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import cn.thinkfree.database.mapper.PreProjectUserRoleMapper;



@RequestMapping("/staff")
@RestController
@Api(value = "员工管理",description = "员工管理")
public class StaffController extends AbsBaseController{

    @Autowired
    private StaffService staffService;

    @Autowired
    ProjectService projectService;

    @Autowired
    private PreProjectUserRoleMapper preProjectUserRoleMapper;
    /**
     * 员工列表，以及条件查询
     * @param staffSEO
     * @return
     */

    @RequestMapping(value = "/findList", method = RequestMethod.POST)
    @MyRespBody
    @ApiOperation(value="查询员工列表(子公司：员工信息)", notes="根据员工姓名和电话和状态查询员工列表")
    public MyRespBundle<PageInfo<StaffsVO>> queryStaffList(@ApiParam("参数信息") StaffSEO staffSEO){
        /*UserVO uservo = (UserVO) SessionUserDetailsUtil.getUserDetails();
        if(uservo.getPcUserInfo() == null){
            return sendJsonData(ResultMessage.FAIL, "用户为空");
        }*/
        PageInfo<StaffsVO> staffsVOPageInfo = staffService.queryStaffList(staffSEO);

        return sendJsonData(ResultMessage.SUCCESS,staffsVOPageInfo);
    }

    /**
     * 查询员工信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/queryCompanyUser",method = RequestMethod.GET)
    public MyRespBundle<PageInfo<CompanyUserSet>> queryCompanyUser(
            @RequestParam("id")Integer id,@RequestParam("userID") String userID,@RequestParam("status") Integer status,@RequestParam("rows")Integer rows,@RequestParam("page")Integer page){
        if(StringUtils.isEmpty(id.toString())){
            return sendJsonData(ResultMessage.FAIL, "参数有误");
        }
        projectService.selectProjectVOForPerson(userID,status,rows,page);
        projectService.countProjectForPerson(userID);
        CompanyUserSet companyUserSet = this.staffService.queryCompanyUser(id);
        return sendJsonData(ResultMessage.SUCCESS,companyUserSet);
    }

    /*@PostMapping("/save-user")
    @MyRespBody
    @SuppressWarnings("unchecked")
    public MyRespBundle saveUserInfo(@RequestBody SaveUserInfo userInfo){
        userService.saveUserInfo(userInfo);
    }*/

    @ApiOperation("员工管理--->详情-->项目列表")
    @GetMapping("/companyList")
    @MyRespBody
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "userId", value = "userId", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "status", value = "status", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "userId", value = "rows", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType="query", name = "roleId", value = "page", required = true, dataType = "Integer")
    })
    public MyRespBundle<PageInfo<ProjectVO>> list(String userId,Integer status,Integer rows,Integer page){
        PageInfo<ProjectVO> pageInfo = projectService.selectProjectVOForPerson(userId, status, rows, page);
        return sendJsonData(ResultMessage.SUCCESS,pageInfo);
    }

    @ApiOperation("员工管理--->详情-->项目情况")
    @GetMapping("/projectNum")
    @MyRespBody
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "userId", value = "userId", required = true, dataType = "String")
    })
    public MyRespBundle<IndexProjectReportVO> projectNum(String userId){
        IndexProjectReportVO indexProjectReportVO = projectService.countProjectForPerson(userId);
        return sendJsonData(ResultMessage.SUCCESS,indexProjectReportVO);
    }

    /**
     * 邀请员工
     * @param companyUserSet
     * @return
     */
    @PostMapping("/insetCompanyUser")
    @MyRespBody
    @MySysLog(action = SysLogAction.SAVE,module = SysLogModule.PC_STAFFS,desc = "邀请员工")
    public MyRespBundle<String> insertCompanyUser(CompanyUserSet companyUserSet){

        String mes = this.staffService.insetCompanyUser(companyUserSet);

        return sendJsonData(ResultMessage.SUCCESS, mes);

    }

    /**
     * 再次邀请员工
     * @param id
     * @return
     */
    @RequestMapping(value = "/toInsertCompanyUser",method = RequestMethod.POST)
    @MyRespBody
    public MyRespBundle<String> toInsertCompanyUser(@RequestParam("id") Integer id){
        CompanyUserSet companyUserSet = this.staffService.queryCompanyUser(id);
        Date date = new Date();
        if(false){
            return sendJsonData(ResultMessage.ERROR,"请五分钟之后在再次邀请!!!");
        }else{
            UserInfo userVO = (UserInfo) SessionUserDetailsUtil.getUserDetails();
            companyUserSet.setUserId(userVO.getUserId());
            companyUserSet.setBindTime(new Date());
            companyUserSet.setUpdateTime(new Date());
            this.staffService.insetCompanyUser(companyUserSet);
            return sendJsonData(ResultMessage.SUCCESS,"邀请成功!!!");
        }
    }

    /**
     * 再次邀请员工
     * @param userID
     * @return
     */
    @ApiOperation(value = "再次邀请员工",notes = "再次邀请员工")
    @PostMapping("/reInvitation")
    @MyRespBody
    public MyRespBundle<String> reInvitation(@RequestParam String userID){
        String mes = staffService.reInvitation(userID);
        return sendSuccessMessage(mes);
    }

    /**
     * 判断员工是否可以移除
     * @param
     * @return
     */
    @GetMapping("/isDel")
    @MyRespBody
    @ApiOperation(value = "员工详情--->判断是否移除员工")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "userId", value = "userId", required = true, dataType = "String")
    })
    public MyRespBundle<String> isDel(@RequestParam(value = "userId") String userId){

        boolean flag = staffService.updateDelCompanyUser(userId);

        return sendJsonData(ResultMessage.SUCCESS, flag);
    }

    /**
     * 移除员工  修改字段
     * @param
     * @return
     */
    @PostMapping("/delete")
    @MyRespBody
    @ApiOperation(value = "员工详情--->移除员工")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="String", name = "userId", value = "userId", required = true, dataType = "String")
    })
    public MyRespBundle<String> delete(@RequestParam(value = "userId",defaultValue = "") String  userId){

        int line = staffService.updateIsJob(userId);
        System.out.println(line);
        System.out.println(line);

        return sendJsonData(ResultMessage.SUCCESS, line);

    }

    /**
     * 修改岗位
     * @return
     */
    @PostMapping("/updateRole")
    @MyRespBody
    @ApiOperation(value = "员工详情--->修改岗位")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "userId", value = "userId", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "roleId", value = "roleId", required = true, dataType = "String")
    })
    public MyRespBundle<String> updateRole(@RequestParam("userId")String userId,
                                           @RequestParam("roleId")String roleId){
        String mes = staffService.updateRole(userId,roleId);

        return sendJsonData(ResultMessage.SUCCESS, mes);
    }

    /**
     * 查看详细信息
     * @return
     */
    @GetMapping("/details")
    @MyRespBody
    @ApiOperation(value = "员工列表--->查看")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "userId", value = "userId", required = true, dataType = "String"),
    })
    public MyRespBundle<CompanyUserSetVo> details(@RequestParam("userId")String userId){
        StaffsVO staffsVO = staffService.detail(userId);

        return sendJsonData(ResultMessage.SUCCESS, staffsVO);
    }

}


