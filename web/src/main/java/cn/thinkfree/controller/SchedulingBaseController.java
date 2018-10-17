package cn.thinkfree.controller;

import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.model.ProjectBigScheduling;
import cn.thinkfree.database.model.ProjectSmallScheduling;
import cn.thinkfree.database.vo.ProjectBigSchedulingVO;
import cn.thinkfree.database.vo.ProjectSmallSchedulingVO;
import cn.thinkfree.database.vo.SchedulingSeo;
import cn.thinkfree.service.scheduling.SchedulingBaseService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "排期基础信息操作")
@RestController
@RequestMapping(value = "schedulingBase")
public class SchedulingBaseController extends AbsBaseController {
    @Autowired
    private SchedulingBaseService schedulingBaseService;

    @RequestMapping(value = "listSmallScheduling", method = RequestMethod.POST)
    @ApiOperation(value = "获取本地基础小排期信息")
    public MyRespBundle<PageInfo<ProjectSmallSchedulingVO>> listSmallScheduling(@ApiParam(name = "schedulingSeo", value = "排期入参分页实体") SchedulingSeo schedulingSeo) {
        PageInfo<ProjectSmallSchedulingVO> page = schedulingBaseService.listSmallScheduling(schedulingSeo);
        return sendJsonData(ResultMessage.SUCCESS, page);
    }

    @RequestMapping(value = "addBigScheduling",method = RequestMethod.POST)
    @ApiOperation("添加基础大排期")
    public MyRespBundle<String> addBigScheduling(@ApiParam(name = "projectBigSchedulingVO",value = "基础大排期信息")ProjectBigSchedulingVO projectBigSchedulingVO){
        String result = schedulingBaseService.addBigScheduling(projectBigSchedulingVO);
        return sendSuccessMessage(result);
    }

    @RequestMapping(value = "listBigScheduling",method = RequestMethod.POST)
    @ApiOperation("获取基础大排期")
    public MyRespBundle<PageInfo<ProjectBigScheduling>> listBigScheduling(@ApiParam(name = "schedulingSeo", value = "排期入参分页实体") SchedulingSeo schedulingSeo){
        PageInfo<ProjectBigScheduling> page = schedulingBaseService.listBigScheduling(schedulingSeo);
        return sendJsonData(ResultMessage.SUCCESS, page);
    }

    @RequestMapping(value = "updateSmallScheduling", method = RequestMethod.POST)
    @ApiOperation(value = "关联小排期与大排期")
    public MyRespBundle<String> updateSmallScheduling(@ApiParam(name = "projectSmallSchedulingVO", value = "小排期实体") ProjectSmallSchedulingVO projectSmallSchedulingVO) {
        String result = schedulingBaseService.updateSmallScheduling(projectSmallSchedulingVO);
        return sendSuccessMessage(result);
    }


}
