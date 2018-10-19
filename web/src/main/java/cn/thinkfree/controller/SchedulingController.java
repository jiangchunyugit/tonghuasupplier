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

    @ApiOperation(value = "施工配置列表", notes = "")
    @PostMapping("/list")
    @MyRespBody
    public MyRespBundle<ProjectBigSchedulingVO> list(@RequestParam(name = "companyId") @ApiParam(value = "公司编号", name = "companyId") String companyId) {
        if ("".equals(companyId) || null == companyId) {
            return sendJsonData(ResultMessage.ERROR, "公司编号为空");
        }
        ProjectBigSchedulingVO projectBigSchedulingVO = schedulingService.selectProjectBigSchedulingByCompanyId(companyId);
        return sendJsonData(ResultMessage.SUCCESS, projectBigSchedulingVO);
    }

    @RequestMapping(value = "saveProjectScheduling", method = RequestMethod.POST)
    @ApiOperation(value = "添加公司施工节点")
    public MyRespBundle saveProjectScheduling(@ApiParam(value = "施工节点信息") ProjectBigSchedulingVO projectBigSchedulingVO) {
        String result = schedulingService.saveProjectScheduling(projectBigSchedulingVO);
        return sendSuccessMessage(result);
    }

    @RequestMapping(value = "deleteProjectScheduling", method = RequestMethod.POST)
    @ApiOperation(value = "删除公司施工节点")
    public MyRespBundle deleteProjectScheduling(@ApiParam(value = "施工节点信息") ProjectBigSchedulingVO projectBigSchedulingVO) {
        String result = schedulingService.deleteProjectScheduling(projectBigSchedulingVO);
        return sendSuccessMessage(result);
    }

    @ApiOperation(value = "获取排期信息")
    @RequestMapping(value = "getScheduling", method = RequestMethod.POST)
    public MyRespBundle<List<ProjectBigSchedulingDetailsVO>> getScheduling(@RequestParam(name = "projectNo") @ApiParam(name = "projectNo", value = "项目编号") String projectNo) {
        List<ProjectBigSchedulingDetailsVO> bigSchedulingDetailsVoList = schedulingService.getScheduling(projectNo);
        return sendJsonData(ResultMessage.SUCCESS, bigSchedulingDetailsVoList);
    }

//    @ApiOperation(value = "获取甘特图")
//    @RequestMapping(value = "gantt", method = RequestMethod.POST)
//    public Meta<GanttDto> getGantt(@PathVariable(name = "projectNo") @ApiParam(name = "projectNo", value = "项目编号") String projectNo) {
//        return iGanttService.getGantt(projectNo);
//    }

}
