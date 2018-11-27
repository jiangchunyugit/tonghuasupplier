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
import org.springframework.validation.annotation.Validated;
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
    public MyRespBundle<PageInfo<ProjectVo>> getAllProject(
            @ApiParam(name = "appProjectSEO", value = "项目列表入参实体") AppProjectSEO appProjectSEO) {
        MyRespBundle<PageInfo<ProjectVo>> page = newProjectService.getAllProject(appProjectSEO);
        return page;
    }

    @RequestMapping(value = "getProjectNum", method = RequestMethod.POST)
    @ApiOperation(value = "C/B-项目个数")
    public MyRespBundle<Integer> getAllProject(
            @RequestParam("userId") @ApiParam(name = "userId", value = "用户编号",required = true) String userId) {
        return newProjectService.getProjectNum(userId);
    }

    @RequestMapping(value = "getAppProjectDetail", method = RequestMethod.POST)
    @ApiOperation(value = "APP-获取项目详情接口")
    public MyRespBundle<ProjectVo> getAppProjectDetail(
            @RequestParam(name = "projectNo") @ApiParam(name = "projectNo", value = "项目编号",required = true) String projectNo) {
        MyRespBundle<ProjectVo> projectVo = newProjectService.getAppProjectDetail(projectNo);
        return projectVo;
    }

    @RequestMapping(value = "getAppProjectTitleDetail", method = RequestMethod.POST)
    @ApiOperation(value = "APP-获取项目详情头接口")
    public MyRespBundle<ProjectTitleVo> getAppProjectTitleDetail(
            @RequestParam(name = "projectNo") @ApiParam(name = "projectNo", value = "项目编号",required = true) String projectNo) {
        return newProjectService.getAppProjectTitleDetail(projectNo);
    }

    @RequestMapping(value = "getDesignData", method = RequestMethod.POST)
    @ApiOperation(value = "APP-获取设计资料")
    public MyRespBundle<DataVo> getDesignData(
            @RequestParam(name = "projectNo") @ApiParam(name = "projectNo", value = "项目编号",required = true) String projectNo) {
        return newProjectService.getDesignData(projectNo);
    }

    @RequestMapping(value = "getConstructionData", method = RequestMethod.POST)
    @ApiOperation(value = "APP/PC-获取施工资料")
    public MyRespBundle<ConstructionDataVo> getConstructionData(
            @RequestParam(name = "projectNo") @ApiParam(name = "projectNo", value = "项目编号",required = true) String projectNo) {
        return newProjectService.getConstructionData(projectNo);
    }

    @RequestMapping(value = "getQuotationData", method = RequestMethod.POST)
    @ApiOperation(value = "APP-获取报价单资料")
    public MyRespBundle<List<UrlDetailVo>> getQuotationData(
            @RequestParam(name = "projectNo") @ApiParam(name = "projectNo", value = "项目编号",required = true) String projectNo) {
        MyRespBundle<List<UrlDetailVo>> dataDetailVo = newProjectService.getQuotationData(projectNo);
        return dataDetailVo;
    }

    @RequestMapping(value = "cancleOrder", method = RequestMethod.POST)
    @ApiOperation(value = "APP-取消订单")
    public MyRespBundle cancleOrder(@RequestParam("orderNo")@ApiParam(name = "orderNo", value = "订单编号",required = true) String orderNo,
                                    @RequestParam("projectNo")@ApiParam(name = "projectNo", value = "项目编号",required = true) String projectNo,
                                    @RequestParam("userId")@ApiParam(name = "userId", value = "用户编号",required = true) String userId,
                                    @RequestParam("cancelReason")@ApiParam(name = "cancelReason", value = "取消原因",required = true) String cancelReason) {
        return newProjectService.cancleOrder(orderNo, projectNo, userId, cancelReason);
    }

    @RequestMapping(value = "confirmVolumeRoomData", method = RequestMethod.POST)
    @ApiOperation(value = "APP-确认资料")
    public MyRespBundle<String> confirmVolumeRoomData( @RequestBody  @ApiParam(name = "dataVo", value = "确认资料 ") CaseDataVo dataVo) {
        try{
            return newProjectService.confirmVolumeRoomData(dataVo);
        } catch (Exception e) {
            e.printStackTrace();
            return RespData.error("此阶段无法提交资料信息!");
        }
    }

    @RequestMapping(value = "confirmVolumeRoomDataUser", method = RequestMethod.POST)
    @ApiOperation(value = "C端确认资料")
    public MyRespBundle<String> confirmVolumeRoomDataUser(
            @RequestParam(name = "projectNo") @ApiParam(name = "projectNo", value = "项目编号")String projectNo,
            @RequestParam(name = "category")@ApiParam(name = "category", value = "项目编号")Integer category) {
        try{
            return newProjectService.confirmVolumeRoomDataUser(projectNo,category);
        } catch (Exception e) {
            e.printStackTrace();
            return RespData.error(e.getMessage());
        }
    }

    @RequestMapping(value = "getProjectStatus", method = RequestMethod.POST)
    @ApiOperation(value = "APP-获取项目阶段")
    public MyRespBundle<Integer> getProjectStatus(
            @RequestParam("projectNo") @ApiParam(name = "projectNo", value = "项目编号",required = true) String projectNo) {
        return newProjectService.getProjectStatus(projectNo);
    }

    @RequestMapping(value = "applyRefund", method = RequestMethod.POST)
    @ApiOperation(value = "APP-退款接口")
    public MyRespBundle<String> applyRefund(
            @RequestParam("OrderNo") @ApiParam(name = "OrderNo", value = "订单号",required = true) String orderNo,
            @RequestParam("payOrderNo") @ApiParam(name = "payOrderNo", value = "支付订单号",required = true) String payOrderNo,
            @RequestParam(value = "otherReason",required = false) @ApiParam(name = "otherReason", value = "其他原因",required = true) String otherReason,
            @RequestParam("money") @ApiParam(name = "money", value = "金额",required = true) Integer money,
            @RequestParam("moneyName") @ApiParam(name = "moneyName", value = "退款项目名",required = true) String moneyName,
            @RequestParam("userId") @ApiParam(name = "userId", value = "用户id",required = true) String userId,
            @RequestParam("cancelReason") @ApiParam(name = "cancelReason", value = "取消原因",required = true) String cancelReason) {
        return newProjectService.applyRefund(orderNo, payOrderNo, otherReason, money, moneyName, userId, cancelReason);
    }

    @RequestMapping(value = "getDesignOrderData",method = {RequestMethod.GET,RequestMethod.POST})
    @ApiOperation("根据设计师ID获取设计信息")
    public MyRespBundle<List<DesignOrderVo>> getDesignOrderData(
            @RequestParam("designerId")@ApiParam(name = "designerId",value = "设计师ID")String designerId){
        return newProjectService.getDesignOrderData(designerId);
    }

}

