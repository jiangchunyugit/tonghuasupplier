package cn.thinkfree.controller;

import ch.qos.logback.classic.net.SyslogAppender;
import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.annotation.MySysLog;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.core.constants.SysLogAction;
import cn.thinkfree.core.constants.SysLogModule;
import cn.thinkfree.database.model.PreProjectGuide;
import cn.thinkfree.database.model.PreProjectMaterial;
import cn.thinkfree.database.vo.*;
import cn.thinkfree.service.constants.ProjectStatus;
import cn.thinkfree.service.project.ProjectService;
import cn.thinkfree.service.remote.CloudService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Api(value = "项目相关信息",description = "项目相关信息描述")
@RestController
@RequestMapping("/project")
public class ProjectController extends AbsBaseController {


    @Autowired
    ProjectService projectService;

    @Autowired
    CloudService cloudService;

    /**
     * 项目列表
     * @param projectSEO
     * @return
     */
    @ApiOperation(value = "项目列表", notes = "根据一定条件获取分页项目记录")
    @GetMapping("/list")
    @MyRespBody
    public MyRespBundle<PageInfo<PreProjectGuide>> list(@ApiParam("项目搜索条件") ProjectSEO projectSEO){

        PageInfo<ProjectVO> pageInfo = projectService.pageProjectBySEO(projectSEO);
        return sendJsonData(ResultMessage.SUCCESS,pageInfo);
    }

    /**
     * 删除项目
     * @param projectNo
     * @return
     */
    @ApiOperation(value = "删除项目", notes = "删除未上线项目")
    @DeleteMapping("/del")
    @MyRespBody
    @MySysLog(action = SysLogAction.DEL,module = SysLogModule.PC_PROJECT,desc = "删除项目信息")
    public MyRespBundle<String> del(@ApiParam("项目编号")@RequestParam String projectNo){
        String mes = projectService.deleteProjectByProjectNo(projectNo);
        return sendSuccessMessage(mes);
    }

    /**
     * 项目停工
     * @param projectNo
     * @return
     */
    @ApiOperation(value = "项目停工", notes = "对已上线项目进行停工")
    @PostMapping("/stop")
    @MyRespBody
    @MySysLog(action = SysLogAction.CHANGE_STATE,module = SysLogModule.PC_PROJECT,desc = "停工")
    public MyRespBundle<String> stop(@RequestParam String projectNo){
        String mes = projectService.updateProjectStateForStop(projectNo);
        return sendSuccessMessage(mes);
    }

    /**
     * 项目详情
     * @param projectNo
     * @return
     */
    @ApiOperation(value = "项目详情", notes = "根据项目编号查询项目详情")
    @GetMapping("/details")
    @MyRespBody
    public MyRespBundle<ProjectDetailsVO> details(@RequestParam String projectNo){
        ProjectDetailsVO projectDetailsVO = projectService.selectProjectDetailsVOByProjectNo(projectNo);
        return sendJsonData(ResultMessage.SUCCESS,projectDetailsVO);
    }

    /**
     * 项目报价单
     * @param projectNo
     * @return
     */
    @ApiOperation(value = "项目报价单", notes = "根据项目编号查询项目报价单")
    @GetMapping("/quotation")
    @MyRespBody
    public MyRespBundle<ProjectQuotationVO> quotation(@RequestParam String projectNo){

        ProjectQuotationVO projectQuotationVO = projectService.selectProjectQuotationVoByProjectNo(projectNo);

        return sendJsonData(ResultMessage.SUCCESS,projectQuotationVO);
    }


    /**
     * 获取公司下相应职位员工
     * @param job
     * @return
     */
    @ApiOperation(value = "获取公司下相应职位员工", notes = "根据职位编号获取公司里相应职位员工")
    @GetMapping("/staffs")
    @MyRespBody
    public MyRespBundle<List<StaffsVO>> staffs(@ApiParam("职位编号 CS,PM之流")@RequestParam String job,@ApiParam("过滤条件") String filter){
        List<StaffsVO> staffsVOS = projectService.selectStaffsByJob(job,filter);
        return sendJsonData(ResultMessage.SUCCESS,staffsVOS);
    }


    /**
     * 编辑报价单
     * @param projectQuotationVO
     * @return
     */
    @ApiOperation(value = "项目报价单保存", notes = "保存项目报价单")
    @PostMapping("/edit")
    @MyRespBody
    @MySysLog(action = SysLogAction.EDIT,module = SysLogModule.PC_PROJECT)
    public MyRespBundle<String> editQuotation(@ApiParam ProjectQuotationVO projectQuotationVO){

        String mes = projectService.editQuotation(projectQuotationVO);

        return sendSuccessMessage(mes);
    }

    /**
     * 上线项目
     * @return
     */
    @ApiOperation(value = "项目上线", notes = "项目上线")
    @PostMapping("/up")
    @MyRespBody
    @MySysLog(action = SysLogAction.CHANGE_STATE,module = SysLogModule.PC_PROJECT)
    public MyRespBundle<String> up(@ApiParam ProjectDetailsVO projectDetailsVO){
        System.out.println(projectDetailsVO);
        String mes = projectService.updateProjectForUpOnline(projectDetailsVO);
        return sendSuccessMessage(mes);
    }


    /**
     * 移交项目
     * @param projectTransferVO
     * @return
     */
    @ApiOperation(value = "移交项目", notes = "移交项目")
    @PostMapping("/transfer")
    @MyRespBody
    @MySysLog(action = SysLogAction.EDIT,module = SysLogModule.PC_PROJECT,desc = "移交项目")
    public MyRespBundle<String> transfer(@ApiParam  ProjectTransferVO projectTransferVO){

        String mes = projectService.updateProjectByTransfer(projectTransferVO);

        return sendSuccessMessage(mes);
    }


    /**
     * 获取项目主材
     * @param projectNo
     * @return
     */
    @ApiOperation(value = "项目主材信息", notes = "获取项目主材信息")
    @GetMapping("/material")
    @MyRespBody
    public MyRespBundle<List<PreProjectMaterial>> material(@RequestParam String projectNo){
        List<PreProjectMaterial> materials=projectService.selectProjectMaterilsByProjectNo(projectNo);
        return sendJsonData(ResultMessage.SUCCESS,materials);
    }


    /**
     * 编辑项目主材信息
     * @param projectNo
     * @param preProjectMaterials
     * @return
     */
    @ApiOperation(value = "编辑项目主材信息", notes = "编辑项目主材信息")
    @GetMapping("/editMaterial")
    @MyRespBody
    public MyRespBundle<String> editMaterial(String projectNo,List<PreProjectMaterial> preProjectMaterials){
       String mes=projectService.editMaterials(projectNo,preProjectMaterials);
        return sendJsonData(ResultMessage.SUCCESS,mes);
    }


    @ApiOperation(value = "邀请业主",notes = "邀请业主")
    @PostMapping("/notifyOwner")
    public MyRespBundle<String> notifyOwner(String phone){
        // TODO 后续
        String mes = projectService.notifyOwner(phone);
        return sendSuccessMessage(mes);
    }




}
