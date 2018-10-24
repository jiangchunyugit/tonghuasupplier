package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.model.ProjectBigSchedulingDetails;
import cn.thinkfree.database.vo.ProjectBigSchedulingDetailsVO;
import cn.thinkfree.database.vo.ProjectBigSchedulingVO;
import cn.thinkfree.service.newscheduling.NewSchedulingService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author gejiaming
 */
@Api(tags = "排期相关")
@RestController
@RequestMapping(value = "scheduling")
public class SchedulingController extends AbsBaseController {

    @Autowired
    private NewSchedulingService schedulingService;

    @ApiOperation(value = "获取排期信息")
    @RequestMapping(value = "getScheduling", method = RequestMethod.POST)
    public MyRespBundle<List<ProjectBigSchedulingDetailsVO>> getScheduling(@RequestParam(name = "projectNo") @ApiParam(name = "projectNo", value = "项目编号") String projectNo) {
        List<ProjectBigSchedulingDetailsVO> bigSchedulingDetailsVoList = schedulingService.getScheduling(projectNo);
        return sendJsonData(ResultMessage.SUCCESS, bigSchedulingDetailsVoList);
    }

    @RequestMapping(value = "saveProjectScheduling", method = RequestMethod.POST)
    @ApiOperation(value = "添加大排期")
    public MyRespBundle saveProjectScheduling(@ApiParam(name = "projectBigSchedulingDetailsVO",value = "大排期信息") ProjectBigSchedulingDetailsVO projectBigSchedulingDetailsVO) {
        String result = schedulingService.saveProjectScheduling(projectBigSchedulingDetailsVO);
        return sendSuccessMessage(result);
    }

    @RequestMapping(value = "deleteProjectScheduling", method = RequestMethod.POST)
    @ApiOperation(value = "删除大排期")
    public MyRespBundle deleteProjectScheduling(@ApiParam(name = "projectBigSchedulingDetailsVO",value = "大排期信息") ProjectBigSchedulingDetailsVO projectBigSchedulingDetailsVO) {
        String result = schedulingService.deleteProjectScheduling(projectBigSchedulingDetailsVO);
        return sendSuccessMessage(result);
    }

    @RequestMapping(value = "updateProjectScheduling",method = RequestMethod.POST)
    @ApiOperation(value = "确认排期")
    public MyRespBundle updateProjectScheduling(@ApiParam(name = "projectBigSchedulingDetailsVO",value = "大排期信息") ProjectBigSchedulingDetailsVO projectBigSchedulingDetailsVO){
//        String result = schedulingService.updateProjectScheduling(projectBigSchedulingDetailsVO);
//        return sendSuccessMessage(result);
        return sendSuccessMessage("");
    }

    @RequestMapping(value = "",method = RequestMethod.POST)
    @ApiOperation("获取验收阶段")
    public MyRespBundle<List<ProjectBigSchedulingDetailsVO>> getCheckStage(@RequestParam(name = "projectNo") @ApiParam(name = "projectNo", value = "项目编号") String projectNo){
        return schedulingService.getCheckStage(projectNo);
    }





//    @ApiOperation(value = "获取甘特图")
//    @RequestMapping(value = "gantt", method = RequestMethod.POST)
//    public Meta<GanttDto> getGantt(@PathVariable(name = "projectNo") @ApiParam(name = "projectNo", value = "项目编号") String projectNo) {
//        return iGanttService.getGantt(projectNo);
//    }

}
