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
    public MyRespBundle<List<ProjectBigSchedulingDetailsVO>> getScheduling(
            @RequestParam(name = "projectNo") @ApiParam(name = "projectNo", value = "项目编号 使用 1223098338391", required = true) String projectNo) {
        return schedulingService.getScheduling(projectNo);
    }

    @RequestMapping(value = "saveProjectScheduling", method = RequestMethod.POST)
    @ApiOperation(value = "添加大排期")
    public MyRespBundle saveProjectScheduling(@ApiParam(name = "projectBigSchedulingDetailsVO", value = "大排期信息") ProjectBigSchedulingDetailsVO projectBigSchedulingDetailsVO) {
        String result = schedulingService.saveProjectScheduling(projectBigSchedulingDetailsVO);
        return sendSuccessMessage(result);
    }

    @RequestMapping(value = "deleteProjectScheduling", method = RequestMethod.POST)
    @ApiOperation(value = "APP-删除大排期")
    public MyRespBundle deleteProjectScheduling(@ApiParam(name = "projectBigSchedulingDetailsVO", value = "大排期信息") ProjectBigSchedulingDetailsVO projectBigSchedulingDetailsVO) {
        String result = schedulingService.deleteProjectScheduling(projectBigSchedulingDetailsVO);
        return sendSuccessMessage(result);
    }


    @RequestMapping(value = "updateProjectScheduling", method = RequestMethod.POST)
    @ApiOperation(value = "APP-确认排期")
    public MyRespBundle confirmProjectScheduling(@RequestBody @ApiParam(name = "projectBigSchedulingDetailsVO", value = "大排期信息") List<ProjectBigSchedulingDetailsVO> bigList) {
        return schedulingService.confirmProjectScheduling(bigList);
    }

    @RequestMapping(value = "getCheckStage", method = RequestMethod.POST)
    @ApiOperation("APP-获取验收阶段")
    public MyRespBundle<List<ProjectBigSchedulingDetailsVO>> getCheckStage(@RequestParam(name = "projectNo") @ApiParam(name = "projectNo", value = "项目编号 111", required = true) String projectNo) {
        return schedulingService.getCheckStage(projectNo);
    }

    @RequestMapping(value = "createScheduling", method = RequestMethod.POST)
    @ApiOperation("生成排期")
    public MyRespBundle createScheduling(
            @RequestParam(name = "orderNo") @ApiParam(name = "orderNo", value = "订单编号 使用 1223081", required = true) String orderNo) {
        return schedulingService.createScheduling(orderNo);
    }

    @RequestMapping(value = "getPcCheckStage", method = RequestMethod.POST)
    @ApiOperation("提供PC合同处获取验收阶段")
    public MyRespBundle<List<String>> getPcCheckStage(
            @RequestParam(name = "orderNo") @ApiParam(name = "orderNo", value = "订单编号 测试用 1223081", required = true) String orderNo,
            @RequestParam(name = "type") @ApiParam(name = "type", value = "订单类型 1,设计订单  2,施工订单  测试用2", required = true) Integer type) {
        return schedulingService.getPcCheckStage(orderNo, type);

    }


}
