package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.annotation.MySysLog;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.core.constants.SysLogAction;
import cn.thinkfree.core.constants.SysLogModule;
import cn.thinkfree.database.model.PreProjectMaterial;
import cn.thinkfree.database.model.ProjectDocument;
import cn.thinkfree.database.utils.BeanValidator;
import cn.thinkfree.database.vo.*;
import cn.thinkfree.service.designer.service.HomeStylerService;
import cn.thinkfree.service.designer.vo.HomeStyler;
import cn.thinkfree.service.designer.vo.HomeStylerVO;
import cn.thinkfree.service.project.ProjectService;
import cn.thinkfree.service.remote.CloudService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author gejiaming
 */
@Api(value = "项目相关信息",description = "项目相关信息描述")
@RestController
@RequestMapping("/project")
public class ProjectController extends AbsBaseController {


    @Autowired
    ProjectService projectService;

    @Autowired
    CloudService cloudService;

    @Autowired
    HomeStylerService homeStylerService;

    /**
     * 项目列表
     * @param projectSEO
     * @return
     */
    @ApiOperation(value = "项目列表", notes = "根据一定条件获取分页项目记录")
    @GetMapping("/list")
    @MyRespBody
    public MyRespBundle<PageInfo<ProjectVO>> list(@ApiParam("项目搜索条件")   ProjectSEO projectSEO){

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
    public MyRespBundle<String> editQuotation(@ApiParam @RequestBody ProjectQuotationVO projectQuotationVO){

        String mes = projectService.editQuotation(projectQuotationVO);

        return sendSuccessMessage(mes);
    }

    /**
     * 保存项目
     * @return
     */
    @ApiOperation(value = "项目上线", notes = "项目上线")
    @PostMapping("/up")
    @MyRespBody
    @MySysLog(action = SysLogAction.CHANGE_STATE,module = SysLogModule.PC_PROJECT)
    public MyRespBundle<String> up(@ApiParam ProjectDetailsVO projectDetailsVO){
        BeanValidator.validate(projectDetailsVO);
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
     * @param preProjectMaterialVO
     * @return
     */
    @ApiOperation(value = "编辑项目主材信息", notes = "编辑项目主材信息")
    @PostMapping("/editMaterial")
    @MyRespBody
    public MyRespBundle<String> editMaterial(@RequestBody PreProjectMaterialVO preProjectMaterialVO){
       String mes=projectService.editMaterials(preProjectMaterialVO.getProjectNo(),preProjectMaterialVO.getPreProjectMaterials());
        return sendJsonData(ResultMessage.SUCCESS,mes);
    }


    @ApiOperation(value = "邀请业主",notes = "邀请业主")
    @PostMapping("/notifyOwner")
    public MyRespBundle<String> notifyOwner(String phone){

        String mes = projectService.notifyOwner(phone);
        return sendSuccessMessage(mes);
    }

    @GetMapping("/homeStyler")
    public MyRespBundle<HomeStylerVO> homeStyler(@RequestParam("id") String projectNo){
        HomeStyler homeStyler = homeStylerService.findDataByProjectNo(projectNo);
        HomeStylerVO homeStylerVO = new HomeStylerVO();
        homeStylerVO.setSpaceDetailsBeans(homeStyler.getSpaceDetails());
        homeStylerVO.setProjectDocuments(projectService.listProjectDocuments(projectNo));
        return sendJsonData(ResultMessage.SUCCESS,homeStylerVO);
    }

    /**
     * 判断业主是否激活
     * @param projectNo
     * @return
     */
    @ApiOperation(value = "判断业主是否激活",notes = "判断业主是否激活 传递项目编号 ")
    @GetMapping("/isActivated")
    public MyRespBundle<Boolean> isActivated(String projectNo){
        Boolean flag = projectService.selectOwnerIsActivatByProjectNo(projectNo);
        return sendJsonData(ResultMessage.SUCCESS,flag);
    }

    /**
     * 项目资料包上传

     * @param projectDocumentContainer 项目资料容器
     * @return
     */
    @ApiOperation(value = "项目资料包上传",notes = "项目资料包上传")
    @PostMapping("/document")
    @MyRespBody
    public MyRespBundle<String> document(  ProjectDocumentContainer projectDocumentContainer){
        if(projectDocumentContainer == null || projectDocumentContainer.getProjectDocuments() == null){
            return sendFailMessage("参数不全");
        }
        String mes =  projectService.uploadProjectDocuments( projectDocumentContainer);
        return sendSuccessMessage(mes);

    }


    @ApiOperation(value = "查询项目资料包",notes = "查询项目资料包")
    @GetMapping("/document")
    @MyRespBody
    public MyRespBundle<List<ProjectDocument>> getDocument(String projectNo){
        List<ProjectDocument> documents = projectService.listProjectDocuments(projectNo);
       return sendJsonData(ResultMessage.SUCCESS,documents);
    }



}
