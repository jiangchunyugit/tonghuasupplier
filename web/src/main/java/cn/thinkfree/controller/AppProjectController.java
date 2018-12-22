package cn.thinkfree.controller;

import cn.thinkfree.core.base.RespData;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.database.appvo.*;
import cn.thinkfree.database.pcvo.PcProjectDetailVo;
import cn.thinkfree.service.newproject.NewProjectService;
import cn.thinkfree.service.platform.vo.PageVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.models.auth.In;
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

    @RequestMapping(value = "getConstructionAllProject", method = RequestMethod.POST)
    @ApiOperation(value = "项目列表--施工端")
    public MyRespBundle<PageVo<List<ConstructionProjectVo>>> getConstructionAllProject(
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") @ApiParam(name = "pageSize", required = false, value = "每页条数") int pageSize,
            @RequestParam(name = "pageNum", required = false, defaultValue = "1") @ApiParam(name = "pageNum", required = false, value = "第几页") int pageNum,
            @RequestParam(name = "userId", required = false) @ApiParam(name = "userId", value = "用户编号", required = false) String userId,
            @RequestParam(name = "projectType", required = false) @ApiParam(name = "projectType", value = "项目分类 1,全部 2,待签约 3,待开工 4,施工中 5,已竣工", required = false) Integer projectType,
            @RequestParam(name = "inputData", required = false) @ApiParam(name = "inputData", value = "筛选输入值", required = false) String inputData) {
        return newProjectService.getConstructionAllProject(pageSize, pageNum, userId, inputData, projectType);
    }

    @ApiOperation(value = "获取施工端项目搜索项(进度阶段+验收阶段)")
    @RequestMapping(value = "getProjectScreen", method = {RequestMethod.GET, RequestMethod.POST})
    public MyRespBundle<ProjectScreenVo> getProjectScreen(
            @RequestParam(name = "userId", required = false) @ApiParam(name = "userId", value = "用户编号", required = false) String userId,
            @RequestParam(name = "projectNo", required = false) @ApiParam(name = "projectNo", value = "项目编号", required = false) String projectNo) {
        return newProjectService.getProjectScreen(userId, projectNo);
    }

    @ApiOperation("施工端项目列表--筛选")
    @RequestMapping(value = "getProjectByScreen", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<PageVo<List<ConstructionProjectVo>>> getProjectByScreen(
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") @ApiParam(name = "pageSize", required = false, value = "每页条数") int pageSize,
            @RequestParam(name = "pageNum", required = false, defaultValue = "1") @ApiParam(name = "pageNum", required = false, value = "第几页") int pageNum,
            @RequestParam(name = "userId", required = false) @ApiParam(name = "userId", value = "用户编号", required = false) String userId,
            @RequestParam(name = "delayBegin", required = false) @ApiParam(name = "delayBegin", value = "逾期天数 开始天数", required = false) Integer delayBegin,
            @RequestParam(name = "delayEnd", required = false) @ApiParam(name = "delayEnd", value = "逾期天数 结束天数", required = false) Integer delayEnd,
            @RequestParam(name = "schedulingSort", required = false) @ApiParam(name = "schedulingSort", value = "进度阶段 sort值", required = false) Integer schedulingSort,
            @RequestParam(name = "checkSort", required = false) @ApiParam(name = "checkSort", value = "验收阶段 sort值", required = false) Integer checkSort,
            @RequestParam(name = "checkComplete", required = false) @ApiParam(name = "checkComplete", value = "是否完成验收 1,是 2,否", required = false) Integer checkComplete,
            @RequestParam(name = "projectNo", required = false) @ApiParam(name = "projectNo", value = "项目编号", required = false) String projectNo) {
        return newProjectService.getProjectByScreen(pageSize, pageNum, userId, delayBegin, delayEnd, schedulingSort, checkSort, checkComplete, projectNo);
    }


    @RequestMapping(value = "getProjectNum", method = RequestMethod.POST)
    @ApiOperation(value = "C/B-项目个数")
    public MyRespBundle<Integer> getAllProject(
            @RequestParam("userId") @ApiParam(name = "userId", value = "用户编号", required = true) String userId) {
        return newProjectService.getProjectNum(userId);
    }

    @RequestMapping(value = "getAppProjectDetail", method = RequestMethod.POST)
    @ApiOperation(value = "APP-获取项目详情接口")
    public MyRespBundle<ProjectVo> getAppProjectDetail(
            @RequestParam("userId") @ApiParam(name = "userId", value = "用户编号", required = true) String userId,
            @RequestParam(name = "projectNo") @ApiParam(name = "projectNo", value = "项目编号", required = true) String projectNo) {
        MyRespBundle<ProjectVo> projectVo = newProjectService.getAppProjectDetail(userId, projectNo);
        return projectVo;
    }

    @RequestMapping(value = "getAppProjectTitleDetail", method = RequestMethod.POST)
    @ApiOperation(value = "APP-获取项目详情头接口")
    public MyRespBundle<ProjectTitleVo> getAppProjectTitleDetail(
            @RequestParam(name = "projectNo") @ApiParam(name = "projectNo", value = "项目编号", required = true) String projectNo) {
        return newProjectService.getAppProjectTitleDetail(projectNo);
    }

    @RequestMapping(value = "getDesignData", method = RequestMethod.POST)
    @ApiOperation(value = "APP-获取设计资料")
    public MyRespBundle<DataVo> getDesignData(
            @RequestParam(name = "projectNo") @ApiParam(name = "projectNo", value = "项目编号", required = true) String projectNo) {
        return newProjectService.getDesignData(projectNo);
    }

    @RequestMapping(value = "getConstructionData", method = RequestMethod.POST)
    @ApiOperation(value = "APP/PC-获取施工资料")
    public MyRespBundle<ConstructionDataVo> getConstructionData(
            @RequestParam(name = "projectNo") @ApiParam(name = "projectNo", value = "项目编号", required = true) String projectNo) {
        return newProjectService.getConstructionData(projectNo);
    }

    @RequestMapping(value = "getQuotationData", method = RequestMethod.POST)
    @ApiOperation(value = "APP-获取报价单资料")
    public MyRespBundle<List<UrlDetailVo>> getQuotationData(
            @RequestParam(name = "projectNo") @ApiParam(name = "projectNo", value = "项目编号", required = true) String projectNo) {
        MyRespBundle<List<UrlDetailVo>> dataDetailVo = newProjectService.getQuotationData(projectNo);
        return dataDetailVo;
    }

    @RequestMapping(value = "cancleOrder", method = RequestMethod.POST)
    @ApiOperation(value = "APP-取消订单")
    public MyRespBundle cancleOrder(@RequestParam("orderNo") @ApiParam(name = "orderNo", value = "订单编号", required = true) String orderNo,
                                    @RequestParam("projectNo") @ApiParam(name = "projectNo", value = "项目编号", required = true) String projectNo,
                                    @RequestParam("userId") @ApiParam(name = "userId", value = "用户编号", required = true) String userId,
                                    @RequestParam("cancelReason") @ApiParam(name = "cancelReason", value = "取消原因", required = true) String cancelReason) {
        return newProjectService.cancleOrder(orderNo, projectNo, userId, cancelReason);
    }

    @RequestMapping(value = "confirmVolumeRoomData", method = RequestMethod.POST)
    @ApiOperation(value = "APP-确认资料")
    public MyRespBundle<String> confirmVolumeRoomData(@RequestBody @ApiParam(name = "dataVo", value = "确认资料 ") CaseDataVo dataVo) {
        try {
            return newProjectService.confirmVolumeRoomData(dataVo);
        } catch (Exception e) {
            e.printStackTrace();
            return RespData.error(e.getMessage());
        }
    }

    @RequestMapping(value = "confirmVolumeRoomDataUser", method = RequestMethod.POST)
    @ApiOperation(value = "C端确认资料")
    public MyRespBundle<String> confirmVolumeRoomDataUser(
            @RequestParam(name = "projectNo", required = false) @ApiParam(name = "projectNo", value = "项目编号", required = false) String projectNo,
            @RequestParam(name = "result", required = false) @ApiParam(name = "result", value = "结果 1,同意  2,不同意", required = false) Integer result,
            @RequestParam(name = "category", required = false) @ApiParam(name = "category", value = "项目编号", required = false) Integer category) {
        try {
            return newProjectService.confirmVolumeRoomDataUser(projectNo, category, result);
        } catch (Exception e) {
            e.printStackTrace();
            return RespData.error(e.getMessage());
        }
    }

    @RequestMapping(value = "getProjectStatus", method = RequestMethod.POST)
    @ApiOperation(value = "APP-获取项目阶段")
    public MyRespBundle<Integer> getProjectStatus(
            @RequestParam("projectNo") @ApiParam(name = "projectNo", value = "项目编号", required = true) String projectNo) {
        return newProjectService.getProjectStatus(projectNo);
    }

    @RequestMapping(value = "applyRefund", method = RequestMethod.POST)
    @ApiOperation(value = "APP-退款接口")
    public MyRespBundle<String> applyRefund(
            @RequestParam("OrderNo") @ApiParam(name = "OrderNo", value = "订单号", required = true) String orderNo,
            @RequestParam("payOrderNo") @ApiParam(name = "payOrderNo", value = "支付订单号", required = true) String payOrderNo,
            @RequestParam(value = "otherReason", required = false) @ApiParam(name = "otherReason", value = "其他原因", required = true) String otherReason,
            @RequestParam("money") @ApiParam(name = "money", value = "金额", required = true) Integer money,
            @RequestParam("moneyName") @ApiParam(name = "moneyName", value = "退款项目名", required = true) String moneyName,
            @RequestParam("userId") @ApiParam(name = "userId", value = "用户id", required = true) String userId,
            @RequestParam("cancelReason") @ApiParam(name = "cancelReason", value = "取消原因", required = true) String cancelReason) {
        return newProjectService.applyRefund(orderNo, payOrderNo, otherReason, money, moneyName, userId, cancelReason);
    }

    @RequestMapping(value = "getDesignOrderData", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation("根据设计师ID获取设计信息")
    public MyRespBundle<PageVo<List<DesignOrderVo>>> getDesignOrderData(
            @RequestParam("designerId") @ApiParam(name = "designerId", value = "设计师ID") String designerId,
            @RequestParam(value = "ownerMsg", required = false) @ApiParam(name = "ownerMsg", value = "业主信息，手机号/姓名") String ownerMsg,
            @RequestParam(value = "projectNo", required = false) @ApiParam(name = "projectNo", value = "项目编号") String projectNo,
            @RequestParam(value = "state", required = false) @ApiParam(name = "state", value = "订单状态", defaultValue = "-1") int state,
            @RequestParam(value = "pageIndex",required = false, defaultValue = "1") @ApiParam(name = "pageIndex", value = "第几页") int pageIndex,
            @RequestParam(value = "pageSize",required = false, defaultValue = "50") @ApiParam(name = "pageSize", value = "每页多少条")int pageSize) {
        try{
            return RespData.success(newProjectService.getDesignOrderData(designerId, ownerMsg, projectNo, state, pageIndex, pageSize));
        }catch (Exception e){
            e.printStackTrace();
            return RespData.error(e.getMessage());
        }
    }

}

