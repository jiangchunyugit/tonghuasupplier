package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.core.security.filter.util.SessionUserDetailsUtil;
import cn.thinkfree.database.model.CompanyUserSet;
import cn.thinkfree.database.model.PreProjectGuide;
import cn.thinkfree.database.model.PreProjectUserRole;
import cn.thinkfree.database.model.UserInfo;
import cn.thinkfree.database.vo.ProjectSEO;
import cn.thinkfree.database.vo.ProjectVO;
import cn.thinkfree.database.vo.UserVO;
import cn.thinkfree.service.project.ProjectService;
import cn.thinkfree.service.staff.StaffService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;


@RequestMapping("/staff")
@RestController
public class StaffController extends AbsBaseController{

    @Autowired
    private StaffService staffService;

    @Autowired
    ProjectService projectService;
    /*
    * 员工列表，以及条件查询
    * */
    @RequestMapping(value = "/findList", method = RequestMethod.POST)
    @MyRespBody
    @ApiOperation(value="查询员工列表", notes="根据员工姓名和电话和状态查询员工列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "page", value = "当前页码", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType="query", name = "rows", value = "每页展示条数", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType="query", name = "name", value = "员工", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "phone", value = "电话", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "isBind", value = "状态", required = true, dataType = "Integer")
    })
    public MyRespBundle<List<UserInfo>> queryStaffList(@RequestParam("page") Integer page,@RequestParam("rows") Integer rows,
                                                       @RequestParam("name") String name, @RequestParam("phone") String phone,
                                                       @RequestParam("isBind") Integer isBind){
        if (page == null){
            page = 0;
        }
        if (rows == null){
            rows = 15;
        }
        /*UserVO uservo = (UserVO) SessionUserDetailsUtil.getUserDetails();
        if(uservo.getPcUserInfo() == null){
            return sendJsonData(ResultMessage.FAIL, "用户为空");
        }*/
        List<CompanyUserSet> companyUserSetList = this.staffService.queryStaffList(page,rows,name,phone,isBind);
        PageInfo<CompanyUserSet> pageInfo = new PageInfo<>(companyUserSetList);
        return sendJsonData(ResultMessage.SUCCESS,pageInfo);
    }

    /**
     * 查询员工信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/queryCompanyUser",method = RequestMethod.POST)
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

    @ApiOperation("项目列表")
    @GetMapping("/companyList")
    @MyRespBody
    public MyRespBundle<PageInfo<PreProjectGuide>> list(ProjectSEO projectSEO){
        PageInfo<ProjectVO> pageInfo = projectService.pageProjectBySEO(projectSEO);
        return sendJsonData(ResultMessage.SUCCESS,pageInfo);
    }


    /**
     *
     * 查询岗位
     * @return
     */
    @GetMapping("/queryUserRole")
    @MyRespBody
    public MyRespBundle<PreProjectUserRole> queryRole(){
        List<PreProjectUserRole> userRoleList = this.staffService.queryRole();
        return sendJsonData(ResultMessage.SUCCESS,userRoleList);
    }

    /**
     * 删除员工逻辑删除
     * @param id
     * @return
     */
    @DeleteMapping("/updateDelCompanyUser")
    @MyRespBody
    public MyRespBundle<String> updateDelCompanyUser(){
        Integer id = 4;
        Integer mes = this.staffService.updateDelCompanyUser(id);
        if(mes > 0){
            return sendJsonData(ResultMessage.SUCCESS, "删除成功!");
        }
        return sendJsonData(ResultMessage.FAIL, mes);
    }

    /**
     * 删除员工
     * @param id
     * @return
     */
    @DeleteMapping("/delCompanyUser")
    @MyRespBody
    public MyRespBundle<String> delCompanyUser(@RequestParam("id")Integer id){

        Integer mes = this.staffService.deletCompanyByNo(id);
        if(mes > 0){
            return sendJsonData(ResultMessage.SUCCESS, mes);
        }
        return sendJsonData(ResultMessage.FAIL, mes);
    }

    //TODO 待实现
    @PostMapping("/updateCompanyWei")
    @MyRespBody
    public MyRespBundle<String> updateCompanyWei(@RequestParam("id")Integer id,
                                                 @RequestParam("roleName")String roleName){
        Integer mes = this.staffService.updateCompanyWei(id,roleName);
        if(mes > 0){
            return sendJsonData(ResultMessage.SUCCESS, mes);
        }
        return sendJsonData(ResultMessage.FAIL, mes);
    }


    /**
     * 邀请员工
     * @param companyUserSet
     * @return
     */
    @PostMapping("/insetCompanyUser")
    @MyRespBody
    public MyRespBundle<String> insertCompanyUser(CompanyUserSet companyUserSet){
        UserVO uservo = (UserVO) SessionUserDetailsUtil.getUserDetails();
        Integer mes = this.staffService.insetCompanyUser(companyUserSet);
        if(mes > 0){
            return sendJsonData(ResultMessage.SUCCESS, mes);
        }
        return sendJsonData(ResultMessage.FAIL, mes);
    }

    /**
     * 再次邀请员工
     * @param id
     * @return
     */
    @RequestMapping("/toInsertCompanyUser")
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
}


