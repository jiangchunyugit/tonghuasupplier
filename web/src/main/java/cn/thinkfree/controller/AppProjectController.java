package cn.thinkfree.controller;

import cn.thinkfree.core.base.RespData;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.database.appvo.*;
import cn.thinkfree.database.pcvo.PcProjectDetailVo;
import cn.thinkfree.service.newproject.NewProjectService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author gejiaming
 */
@Api(tags = "APP/PC-项目相关")
@RestController
@RequestMapping(value = "project")
public class AppProjectController {
    @Autowired
    private NewProjectService newProjectService;

    @RequestMapping(value = "getAllProject", method = RequestMethod.POST)
    @ApiOperation(value = "C/B-项目列表")
    public MyRespBundle<PageInfo<ProjectVo>> getAllProject(@ApiParam(name = "appProjectSEO", value = "项目列表入参实体") AppProjectSEO appProjectSEO) {
        MyRespBundle<PageInfo<ProjectVo>> page = newProjectService.getAllProject(appProjectSEO);
        return page;
    }

    @RequestMapping(value = "getProjectNum", method = RequestMethod.POST)
    @ApiOperation(value = "C/B-项目个数")
    public MyRespBundle<Integer> getAllProject(@RequestParam("userId") @ApiParam(name = "userId", value = "用户编号,默认先写123456") String userId) {
        return newProjectService.getProjectNum(userId);
    }

    @RequestMapping(value = "getAppProjectDetail", method = RequestMethod.POST)
    @ApiOperation(value = "APP-获取项目详情接口")
    public MyRespBundle<ProjectVo> getAppProjectDetail(@RequestParam(name = "projectNo") @ApiParam(name = "projectNo", value = "项目编号 1223098338391") String projectNo) {
        MyRespBundle<ProjectVo> projectVo = newProjectService.getAppProjectDetail(projectNo);
        return projectVo;
    }

    @RequestMapping(value = "getAppProjectTitleDetail", method = RequestMethod.POST)
    @ApiOperation(value = "APP-获取项目详情头接口")
    public MyRespBundle<ProjectTitleVo> getAppProjectTitleDetail(@RequestParam(name = "projectNo") @ApiParam(name = "projectNo", value = "项目编号 1223098338391") String projectNo) {
        return newProjectService.getAppProjectTitleDetail(projectNo);
    }

    @RequestMapping(value = "getDesignData", method = RequestMethod.POST)
    @ApiOperation(value = "APP-获取设计资料")
    public MyRespBundle<DataVo> getDesignData(@RequestParam(name = "projectNo") @ApiParam(name = "projectNo", value = "项目编号 1223098338391") String projectNo) {
        MyRespBundle<DataVo> dataVo = newProjectService.getDesignData(projectNo);
        return dataVo;
    }

    @RequestMapping(value = "getConstructionData", method = RequestMethod.POST)
    @ApiOperation(value = "APP/PC-获取施工资料")
    public MyRespBundle<List<UrlDetailVo>> getConstructionData(@RequestParam(name = "projectNo") @ApiParam(name = "projectNo", value = "项目编号") String projectNo) {
        MyRespBundle<List<UrlDetailVo>> dataDetailVo = newProjectService.getConstructionData(projectNo);
        return dataDetailVo;
    }

    @RequestMapping(value = "getQuotationData", method = RequestMethod.POST)
    @ApiOperation(value = "APP-获取报价单资料")
    public MyRespBundle<List<UrlDetailVo>> getQuotationData(@RequestParam(name = "projectNo") @ApiParam(name = "projectNo", value = "项目编号") String projectNo) {
        MyRespBundle<List<UrlDetailVo>> dataDetailVo = newProjectService.getQuotationData(projectNo);
        return dataDetailVo;
    }

    @RequestMapping(value = "cancleOrder", method = RequestMethod.POST)
    @ApiOperation(value = "APP-取消订单")
    public MyRespBundle cancleOrder(@ApiParam(name = "orderNo", value = "订单编号") String orderNo,
                                    @ApiParam(name = "projectNo", value = "项目编号") String projectNo,
                                    @ApiParam(name = "userId", value = "用户编号") String userId,
                                    @ApiParam(name = "cancelReason", value = "取消原因") String cancelReason) {
        return newProjectService.cancleOrder(orderNo, projectNo, userId, cancelReason);
    }

    @RequestMapping(value = "confirmVolumeRoomData", method = RequestMethod.POST)
    @ApiOperation(value = "APP-确认资料")
    public MyRespBundle<String> confirmVolumeRoomData(
            @RequestParam(name = "projectNo") @ApiParam(name = "projectNo", value = "项目编号,测试请用 ") String projectNo,
            @RequestParam(name = "category") @ApiParam(name = "category", value = "项目编号,测试请用 ") Integer category) {
        return newProjectService.confirmVolumeRoomData(projectNo, category);
    }

    @RequestMapping(value = "getProjectUsers", method = RequestMethod.POST)
    @ApiOperation("批量获取人员信息")
    public MyRespBundle<List<UserVo>> getProjectUsers(@RequestParam("projectNo") @ApiParam(name = "projectNo", value = "项目编号,测试请用 1223098338391") String projectNo) {
        return newProjectService.getProjectUsers(projectNo);
    }

    @RequestMapping(value = "getProjectStatus", method = RequestMethod.POST)
    @ApiOperation(value = "APP-获取项目阶段")
    public MyRespBundle<Integer> getProjectStatus(@RequestParam("projectNo") @ApiParam(name = "projectNo", value = "项目编号,测试请用 1223098338391") String projectNo) {
        return newProjectService.getProjectStatus(projectNo);
    }

    @RequestMapping(value = "getListUserByUserIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量获取员工的信息")
    public MyRespBundle<Map<String, UserVo>> getListUserByUserIds(@RequestBody @ApiParam(name = "userIds", value = "用户id集合,测试用 123567和123456") List<String> userIds) {
        return newProjectService.getListUserByUserIds(userIds);
    }

    @RequestMapping(value = "applyRefund", method = RequestMethod.POST)
    @ApiOperation(value = "APP-退款接口")
    public MyRespBundle<String> applyRefund(
            @RequestParam("OrderNo") @ApiParam(name = "OrderNo", value = "订单号") String orderNo,
            @RequestParam("payOrderNo") @ApiParam(name = "payOrderNo", value = "支付订单号") String payOrderNo,
            @RequestParam("otherReason") @ApiParam(name = "otherReason", value = "其他原因") String otherReason,
            @RequestParam("money") @ApiParam(name = "money", value = "金额") Integer money,
            @RequestParam("moneyName") @ApiParam(name = "moneyName", value = "退款项目名") String moneyName,
            @RequestParam("userId") @ApiParam(name = "userId", value = "用户id") String userId,
            @RequestParam("cancelReason") @ApiParam(name = "cancelReason", value = "取消原因") String cancelReason) {
        return newProjectService.applyRefund(orderNo, payOrderNo, otherReason, money, moneyName, userId, cancelReason);
    }


//    @RequestMapping(value = "",method = RequestMethod.POST)
//    @ApiOperation(value = "")
//    public MyRespBundle<> (@ApiParam(name = "",value = "") ){
//
//        return sendJsonData(ResultMessage.SUCCESS,);
//    }


}

