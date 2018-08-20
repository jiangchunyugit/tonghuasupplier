package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.annotation.MySysLog;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.core.constants.SysLogAction;
import cn.thinkfree.core.constants.SysLogModule;
import cn.thinkfree.database.model.PreProjectGuide;
import cn.thinkfree.database.vo.*;
import cn.thinkfree.service.project.ProjectService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/project")
public class ProjectController extends AbsBaseController {


    @Autowired
    ProjectService projectService;


    /**
     * 项目列表
     * @param projectSEO
     * @return
     */
    @ApiOperation("项目列表")
    @GetMapping("/list")
    @MyRespBody
    public MyRespBundle<PageInfo<PreProjectGuide>> list(ProjectSEO projectSEO){

        PageInfo<ProjectVO> pageInfo = projectService.pageProjectBySEO(projectSEO);
        return sendJsonData(ResultMessage.SUCCESS,pageInfo);
    }

    /**
     * 删除项目
     * @param projectNo
     * @return
     */
    @DeleteMapping("/del")
    @MyRespBody
    @MySysLog(action = SysLogAction.DEL,module = SysLogModule.PC_PROJECT,desc = "删除项目信息")
    public MyRespBundle<String> del(@RequestParam String projectNo){
        String mes = projectService.deleteProjectByProjectNo(projectNo);
        return sendSuccessMessage(mes);
    }

    /**
     * 项目停工
     * @param projectNo
     * @return
     */
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
    @GetMapping("/staffs")
    @MyRespBody
    public MyRespBundle<List<StaffsVO>> staffs(@RequestParam String job){
        List<StaffsVO> staffsVOS = projectService.selectStaffsByJob(job);
        return sendJsonData(ResultMessage.SUCCESS,staffsVOS);
    }


    /**
     * 编辑报价单
     * @param projectQuotationVO
     * @return
     */
    @PostMapping("/edit")
    @MyRespBody
    @MySysLog(action = SysLogAction.EDIT,module = SysLogModule.PC_PROJECT)
    public MyRespBundle<String> editQuotation(ProjectQuotationVO projectQuotationVO){

        String mes = projectService.editQuotation(projectQuotationVO);

        return sendSuccessMessage(mes);
    }

    /**
     * 上线项目
     * @return
     */
    @PostMapping("/up")
    @MyRespBody
    @MySysLog(action = SysLogAction.CHANGE_STATE,module = SysLogModule.PC_PROJECT)
    public MyRespBundle<String> up(ProjectDetailsVO projectDetailsVO){
        System.out.println(projectDetailsVO);
        String mes = projectService.updateProjectForUpOnline(projectDetailsVO);
        return sendSuccessMessage(mes);
    }


    @PostMapping("/transfer")
    @MyRespBody
    @MySysLog(action = SysLogAction.EDIT,module = SysLogModule.PC_PROJECT,desc = "移交项目")
    public MyRespBundle<String> transfer(ProjectTransferVO projectTransferVO){

        String mes = projectService.updateProjectByTransfer(projectTransferVO);

        return sendSuccessMessage(mes);
    }


}
