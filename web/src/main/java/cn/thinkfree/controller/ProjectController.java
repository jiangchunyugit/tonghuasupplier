package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.annotation.MySysLog;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.core.constants.SysLogAction;
import cn.thinkfree.core.constants.SysLogModule;
import cn.thinkfree.database.model.PreProjectGuide;
import cn.thinkfree.database.vo.ProjectDetailsVO;
import cn.thinkfree.database.vo.ProjectSEO;
import cn.thinkfree.database.vo.ProjectVO;
import cn.thinkfree.service.project.ProjectService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
     * @param projectID
     * @return
     */
    @DeleteMapping("/del")
    @MyRespBody
    @MySysLog(action = SysLogAction.DEL,module = SysLogModule.PC_PROJECT,desc = "删除项目信息")
    public MyRespBundle<String> del(@RequestParam String projectID){
        String mes = projectService.deleteProjectByProjectNo(projectID);
        return sendSuccessMessage(mes);
    }

    @PostMapping("/stop")
    @MyRespBody
    @MySysLog(action = SysLogAction.CHANGE_STATE,module = SysLogModule.PC_PROJECT,desc = "停工")
    public MyRespBundle<String> stop(@RequestParam String projectNo){
        String mes = projectService.updateProjectStateForStop(projectNo);
        return sendSuccessMessage(mes);
    }

    @GetMapping("/details")
    @MyRespBody
    public MyRespBundle<ProjectDetailsVO> details(@RequestParam String projectNo){

        ProjectDetailsVO projectDetailsVO = projectService.selectProjectDetailsVOByProjectNo(projectNo);


        return null;
    }



}
