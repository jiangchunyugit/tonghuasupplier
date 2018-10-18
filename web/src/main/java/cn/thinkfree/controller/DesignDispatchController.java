package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.service.platform.designer.DesignDispatchService;
import cn.thinkfree.service.platform.designer.vo.DesignOrderVo;
import cn.thinkfree.service.platform.designer.vo.PageVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author xusonghui
 * 设计订单派单相关接口
 */
@Api(value = "设计订单派单相关接口", tags = "设计订单派单相关接口")
@Controller
@RequestMapping("designerOrder")
public class DesignDispatchController extends AbsBaseController {
    /**
     * try{
     * }catch (Exception e){
     * return sendFailMessage(e.getMessage());
     * }
     */
    @Autowired
    private DesignDispatchService designDispatchService;

    @ApiOperation("设计师派单管理列表")
    @MyRespBody
    @RequestMapping(value = "orderList", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<PageVo<List<DesignOrderVo>>> queryDesignerOrder(
            @ApiParam(name = "companyId", required = false, value = "公司ID") @RequestParam(name = "companyId", required = false) String companyId,
            @ApiParam(name = "projectNo", required = false, value = "订单编号") @RequestParam(name = "projectNo", required = false) String projectNo,
            @ApiParam(name = "userMsg", required = false, value = "业主姓名或电话") @RequestParam(name = "userMsg", required = false) String userMsg,
            @ApiParam(name = "orderSource", required = false, value = "订单来源") @RequestParam(name = "orderSource", required = false) String orderSource,
            @ApiParam(name = "createTimeStart", required = false, value = "创建时间开始") @RequestParam(name = "createTimeStart", required = false) String createTimeStart,
            @ApiParam(name = "createTimeEnd", required = false, value = "创建时间结束") @RequestParam(name = "createTimeEnd", required = false) String createTimeEnd,
            @ApiParam(name = "styleCode", required = false, value = "装饰风格") @RequestParam(name = "styleCode", required = false) String styleCode,
            @ApiParam(name = "money", required = false, value = "装修预算") @RequestParam(name = "money", required = false) String money,
            @ApiParam(name = "acreage", required = false, value = "建筑面积") @RequestParam(name = "acreage", required = false) String acreage,
            @ApiParam(name = "designerOrderState", required = false, value = "订单状态") @RequestParam(name = "designerOrderState", required = false, defaultValue = "-1") int designerOrderState,
            @ApiParam(name = "companyState", required = false, value = "公司状态") @RequestParam(name = "companyState", required = false) String companyState,
            @ApiParam(name = "optionUserName", required = false, value = "操作人姓名") @RequestParam(name = "optionUserName", required = false) String optionUserName,
            @ApiParam(name = "optionTimeStart", required = false, value = "操作时间开始") @RequestParam(name = "optionTimeStart", required = false) String optionTimeStart,
            @ApiParam(name = "optionTimeEnd", required = false, value = "操作时间结束") @RequestParam(name = "optionTimeEnd", required = false) String optionTimeEnd,
            @ApiParam(name = "pageSize", required = false, value = "每页多少条") @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
            @ApiParam(name = "pageIndex", required = false, value = "第几页，从1开始") @RequestParam(name = "pageIndex", required = false, defaultValue = "1") int pageIndex) {
        PageVo<List<DesignOrderVo>> pageVo = designDispatchService.queryDesignerOrder(companyId, projectNo, userMsg, orderSource, createTimeStart, createTimeEnd, styleCode,
                money, acreage, designerOrderState, companyState, optionUserName, optionTimeStart, optionTimeEnd, pageSize, pageIndex);
        return sendJsonData(ResultMessage.SUCCESS, pageVo);
    }

    @ApiOperation("设计订单不派单")
    @MyRespBody
    @RequestMapping(value = "notDispatch", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle notDispatch(
            @ApiParam(name = "projectNo", required = false, value = "订单编号") @RequestParam(name = "projectNo", required = false) String projectNo,
            @ApiParam(name = "reason", required = false, value = "不派单原因") @RequestParam(name = "reason", required = false) String reason,
            @ApiParam(name = "companyId", required = false, value = "公司ID") @RequestParam(name = "companyId", required = false) String companyId,
            @ApiParam(name = "optionUserId", required = false, value = "操作人员ID") @RequestParam(name = "optionUserId", required = false) String optionUserId,
            @ApiParam(name = "optionUserName", required = false, value = "操作人员姓名") @RequestParam(name = "optionUserName", required = false) String optionUserName) {
        try {
            designDispatchService.notDispatch(projectNo, reason, companyId, optionUserId, optionUserName);
        } catch (Exception e) {
            return sendFailMessage(e.getMessage());
        }
        return sendSuccessMessage(null);
    }

    @ApiOperation("设计订单派单")
    @MyRespBody
    @RequestMapping(value = "dispatch", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle dispatch(
            @ApiParam(name = "projectNo", required = false, value = "订单编号") @RequestParam(name = "projectNo", required = false) String projectNo,
            @ApiParam(name = "companyId", required = false, value = "公司ID") @RequestParam(name = "companyId", required = false) String companyId,
            @ApiParam(name = "optionUserId", required = false, value = "操作人员ID") @RequestParam(name = "optionUserId", required = false) String optionUserId,
            @ApiParam(name = "optionUserName", required = false, value = "操作人员姓名") @RequestParam(name = "optionUserName", required = false) String optionUserName) {
        try {
            designDispatchService.dispatch(projectNo, companyId, optionUserId, optionUserName);
        } catch (Exception e) {
            return sendFailMessage(e.getMessage());
        }
        return sendSuccessMessage(null);
    }

    @ApiOperation("根据项目编号查询设计订单详情")
    @MyRespBody
    @RequestMapping(value = "designDel", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<DesignOrderVo> queryDesignDel(
            @ApiParam(name = "projectNo", required = false, value = "订单编号") @RequestParam(name = "projectNo", required = false) String projectNo) {
        try {
            return sendJsonData(ResultMessage.SUCCESS, designDispatchService.queryDesignOrderVoByProjectNo(projectNo));
        } catch (Exception e) {
            return sendFailMessage(e.getMessage());
        }
    }

    @ApiOperation("设计公司拒绝接单")
    @MyRespBody
    @RequestMapping(value = "refuseOrder", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle refuseOrder(
            @ApiParam(name = "projectNo", required = false, value = "订单编号") @RequestParam(name = "projectNo", required = false) String projectNo,
            @ApiParam(name = "companyId", required = false, value = "公司ID") @RequestParam(name = "companyId", required = false) String companyId,
            @ApiParam(name = "reason", required = false, value = "拒绝原因") @RequestParam(name = "reason", required = false) String reason,
            @ApiParam(name = "optionUserId", required = false, value = "操作人员ID") @RequestParam(name = "optionUserId", required = false) String optionUserId,
            @ApiParam(name = "optionUserName", required = false, value = "操作人员姓名") @RequestParam(name = "optionUserName", required = false) String optionUserName) {
        try {
            designDispatchService.refuseOrder(projectNo,companyId,reason,optionUserId,optionUserName);
        } catch (Exception e) {
            return sendFailMessage(e.getMessage());
        }
        return sendSuccessMessage(null);
    }

    @ApiOperation("设计公司指派设计师")
    @MyRespBody
    @RequestMapping(value = "assignDesigner", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle assignDesigner(
            @ApiParam(name = "projectNo", required = false, value = "订单编号") @RequestParam(name = "projectNo", required = false) String projectNo,
            @ApiParam(name = "companyId", required = false, value = "公司ID") @RequestParam(name = "companyId", required = false) String companyId,
            @ApiParam(name = "designerUserId", required = false, value = "设计师ID") @RequestParam(name = "designerUserId", required = false) String designerUserId,
            @ApiParam(name = "optionUserId", required = false, value = "操作人员ID") @RequestParam(name = "optionUserId", required = false) String optionUserId,
            @ApiParam(name = "optionUserName", required = false, value = "操作人员姓名") @RequestParam(name = "optionUserName", required = false) String optionUserName){
        try {
            designDispatchService.assignDesigner(projectNo,companyId,designerUserId,optionUserId,optionUserName);
        } catch (Exception e) {
            return sendFailMessage(e.getMessage());
        }
        return sendSuccessMessage(null);
    }

    @ApiOperation("设计师拒绝接单")
    @MyRespBody
    @RequestMapping(value = "designerRefuse", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle designerRefuse(
            @ApiParam(name = "projectNo", required = false, value = "订单编号") @RequestParam(name = "projectNo", required = false) String projectNo,
            @ApiParam(name = "reason", required = false, value = "拒绝原因") @RequestParam(name = "reason", required = false) String reason,
            @ApiParam(name = "designerUserId", required = false, value = "设计师ID") @RequestParam(name = "designerUserId", required = false) String designerUserId,
            @ApiParam(name = "optionUserName", required = false, value = "操作人员姓名") @RequestParam(name = "optionUserName", required = false) String optionUserName){
        try {
            designDispatchService.designerRefuse(projectNo,reason,designerUserId,optionUserName);
        } catch (Exception e) {
            return sendFailMessage(e.getMessage());
        }
        return sendSuccessMessage(null);
    }

}
