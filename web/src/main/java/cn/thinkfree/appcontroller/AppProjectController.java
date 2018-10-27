package cn.thinkfree.appcontroller;

import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.base.RespData;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.appvo.*;
import cn.thinkfree.service.newproject.NewProjectService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author gejiaming
 */
@Api(tags = "项目相关")
@RestController
@RequestMapping(value = "project")
public class AppProjectController {
    @Autowired
    private NewProjectService newProjectService;

    @RequestMapping(value = "getAllProject", method = RequestMethod.POST)
    @ApiOperation(value = "项目列表")
    public MyRespBundle<PageInfo<ProjectVo>> getAllProject(@ApiParam(name = "appProjectSEO", value = "项目列表入参实体") AppProjectSEO appProjectSEO) {
        MyRespBundle<PageInfo<ProjectVo>> page = newProjectService.getAllProject(appProjectSEO);
        return page;
    }

    @RequestMapping(value = "getProjectDetail", method = RequestMethod.POST)
    @ApiOperation(value = "获取项目详情接口")
    public MyRespBundle<ProjectVo> getProjectDetail(@RequestParam(name = "projectNo") @ApiParam(name = "projectNo", value = "项目编号") String projectNo) {
        MyRespBundle<ProjectVo> projectVo = newProjectService.getProjectDetail(projectNo);
        return projectVo;
    }

    @RequestMapping(value = "getDesignData", method = RequestMethod.POST)
    @ApiOperation(value = "获取设计资料")
    public MyRespBundle<DataVo> getDesignData(@RequestParam(name = "projectNo") @ApiParam(name = "projectNo", value = "项目编号") String projectNo) {
        MyRespBundle<DataVo> dataVo = newProjectService.getDesignData(projectNo);
        return dataVo;
    }

    @RequestMapping(value = "getConstructionData", method = RequestMethod.POST)
    @ApiOperation(value = "获取施工资料")
    public MyRespBundle<List<UrlDetailVo>> getConstructionData(@RequestParam(name = "projectNo") @ApiParam(name = "projectNo", value = "项目编号") String projectNo) {
        MyRespBundle<List<UrlDetailVo>> dataDetailVo = newProjectService.getConstructionData(projectNo);
        return dataDetailVo;
    }

    @RequestMapping(value = "getQuotationData", method = RequestMethod.POST)
    @ApiOperation(value = "获取报价单资料")
    public MyRespBundle<List<UrlDetailVo>> getQuotationData(@RequestParam(name = "projectNo") @ApiParam(name = "projectNo", value = "项目编号") String projectNo) {
        MyRespBundle<List<UrlDetailVo>> dataDetailVo = newProjectService.getQuotationData(projectNo);
        return dataDetailVo;
    }

    @RequestMapping(value = "cancleOrder", method = RequestMethod.POST)
    @ApiOperation(value = "取消订单")
    public MyRespBundle cancleOrder(@ApiParam(name = "orderNo", value = "订单编号") String orderNo,
                                    @ApiParam(name = "cancelReason", value = "取消原因") String cancelReason) {
        return RespData.success();
    }

    @RequestMapping(value = "confirmVolumeRoomData", method = RequestMethod.POST)
    @ApiOperation(value = "确认资料")
    public MyRespBundle<String> confirmVolumeRoomData(@ApiParam(name = "dataDetailVo", value = "资料详情") DataDetailVo dataDetailVo) {
        MyRespBundle<String> result = newProjectService.confirmVolumeRoomData(dataDetailVo);
        return result;
    }

    @RequestMapping(value = "getProjectUsers", method = RequestMethod.POST)
    @ApiOperation("批量获取人员信息")
    public MyRespBundle<List<UserVo>> getProjectUsers(@RequestParam("projectNo") @ApiParam(name = "projectNo", value = "项目编号,测试请用 1223098338391") String projectNo) {
        return newProjectService.getProjectUsers(projectNo);
    }

    @RequestMapping(value = "getProjectStatus", method = RequestMethod.POST)
    @ApiOperation(value = "获取项目阶段")
    public MyRespBundle<Integer> getProjectStatus(@RequestParam("projectNo") @ApiParam(name = "projectNo", value = "项目编号,测试请用 1223098338391") String projectNo) {
        return newProjectService.getProjectStatus(projectNo);
    }

    @RequestMapping(value = "getListUserByUserIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量获取员工的信息")
    public MyRespBundle<Map<String, UserVo>> getListUserByUserIds(@RequestBody @ApiParam(name = "userIds", value = "用户id集合,测试用 123567和123456") List<String> userIds) {
        return newProjectService.getListUserByUserIds(userIds);
    }

//    @RequestMapping(value = "applyRefund",method = RequestMethod.POST)
//    @ApiOperation(value = "退款接口")
//    public MyRespBundle<String> applyRefund(@ApiParam(name = "",value = "") ){
//
//        return ;
//    }


//    @RequestMapping(value = "",method = RequestMethod.POST)
//    @ApiOperation(value = "")
//    public MyRespBundle<> (@ApiParam(name = "",value = "") ){
//
//        return sendJsonData(ResultMessage.SUCCESS,);
//    }


}

