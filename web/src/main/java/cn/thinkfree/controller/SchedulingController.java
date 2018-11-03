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
@Api(tags = "APP/PC-排期相关")
@RestController
@RequestMapping(value = "scheduling")
public class SchedulingController extends AbsBaseController {

    @Autowired
    private NewSchedulingService schedulingService;

    @ApiOperation(value = "APP/PC-获取排期信息")
    @RequestMapping(value = "getScheduling", method = RequestMethod.POST)
    public MyRespBundle<List<ProjectBigSchedulingDetailsVO>> getScheduling(@RequestParam(name = "projectNo") @ApiParam(name = "projectNo", value = "项目编号 使用 111") String projectNo) {
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
    @ApiOperation(value = "APP-删除大排期")
    public MyRespBundle deleteProjectScheduling(@ApiParam(name = "projectBigSchedulingDetailsVO",value = "大排期信息") ProjectBigSchedulingDetailsVO projectBigSchedulingDetailsVO) {
        String result = schedulingService.deleteProjectScheduling(projectBigSchedulingDetailsVO);
        return sendSuccessMessage(result);
    }

    @RequestMapping(value = "updateProjectScheduling",method = RequestMethod.POST)
    @ApiOperation(value = "APP-确认排期")
    public MyRespBundle confirmProjectScheduling(@RequestBody@ApiParam(name = "projectBigSchedulingDetailsVO",value = "大排期信息") List<ProjectBigSchedulingDetailsVO> bigList){
        return schedulingService.confirmProjectScheduling(bigList);
    }

    @RequestMapping(value = "",method = RequestMethod.POST)
    @ApiOperation("APP-获取验收阶段")
    public MyRespBundle<List<ProjectBigSchedulingDetailsVO>> getCheckStage(@RequestParam(name = "projectNo") @ApiParam(name = "projectNo", value = "项目编号 111") String projectNo){
        return schedulingService.getCheckStage(projectNo);
    }

    @RequestMapping(value = "createScheduling",method = RequestMethod.POST)
    @ApiOperation("生成排期")
    public MyRespBundle createScheduling(
            @RequestParam(name = "projectNo") @ApiParam(name = "projectNo", value = "项目编号 使用 1223098338391") String projectNo,
            @RequestParam(name = "projectNo") @ApiParam(name = "companyId", value = "公司编号 使用 111") String companyId ){
        return schedulingService.createScheduling(projectNo,companyId);
    }





//    @ApiOperation(value = "获取甘特图")
//    @RequestMapping(value = "gantt", method = RequestMethod.POST)
//    public Meta<GanttDto> getGantt(@PathVariable(name = "projectNo") @ApiParam(name = "projectNo", value = "项目编号") String projectNo) {
//        return iGanttService.getGantt(projectNo);
//    }

}
