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
@RequestMapping("designerDispatch")
public class DesignDispatchController extends AbsBaseController {
    @Autowired
    private DesignDispatchService designDispatchService;

    @ApiOperation("设计师派单管理列表")
    @MyRespBody
    @RequestMapping(value = "orderList",method = {RequestMethod.POST,RequestMethod.GET})
    public MyRespBundle<PageVo<List<DesignOrderVo>>> queryDesignerOrder(
            @ApiParam(name = "projectNo", required = false, value = "订单编号") @RequestParam(name = "projectNo", required = false) String projectNo,
            @ApiParam(name = "userMsg", required = false, value = "业主姓名或电话") @RequestParam(name = "userMsg", required = false) String userMsg,
            @ApiParam(name = "orderSource", required = false, value = "订单来源") @RequestParam(name = "orderSource", required = false) String orderSource,
            @ApiParam(name = "createTimeStart", required = false, value = "创建时间开始") @RequestParam(name = "createTimeStart", required = false) String createTimeStart,
            @ApiParam(name = "createTimeEnd", required = false, value = "创建时间结束") @RequestParam(name = "createTimeEnd", required = false) String createTimeEnd,
            @ApiParam(name = "styleCode", required = false, value = "装饰风格") @RequestParam(name = "styleCode", required = false) String styleCode,
            @ApiParam(name = "money", required = false, value = "装修预算") @RequestParam(name = "money", required = false) String money,
            @ApiParam(name = "acreage", required = false, value = "建筑面积") @RequestParam(name = "acreage", required = false) String acreage,
            @ApiParam(name = "designerOrderState", required = false, value = "订单状态") @RequestParam(name = "designerOrderState", required = false,defaultValue = "-1") int designerOrderState,
            @ApiParam(name = "companyState", required = false, value = "公司状态") @RequestParam(name = "companyState", required = false) String companyState,
            @ApiParam(name = "optionUserName", required = false, value = "操作人姓名") @RequestParam(name = "optionUserName", required = false) String optionUserName,
            @ApiParam(name = "optionTimeStart", required = false, value = "操作时间开始") @RequestParam(name = "optionTimeStart", required = false) String optionTimeStart,
            @ApiParam(name = "optionTimeEnd", required = false, value = "操作时间结束") @RequestParam(name = "optionTimeEnd", required = false) String optionTimeEnd,
            @ApiParam(name = "pageSize", required = false, value = "每页多少条") @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
            @ApiParam(name = "pageIndex", required = false, value = "第几页，从1开始") @RequestParam(name = "pageIndex", required = false, defaultValue = "1") int pageIndex){
        PageVo<List<DesignOrderVo>> pageVo = designDispatchService.queryDesignerOrder(projectNo,userMsg,orderSource,createTimeStart,createTimeEnd,styleCode,
                money,acreage,designerOrderState,companyState,optionUserName,optionTimeStart,optionTimeEnd,pageSize,pageIndex);
        return sendJsonData(ResultMessage.SUCCESS, pageVo);
    }

    @ApiOperation("不派单原因")
    @MyRespBody
    @RequestMapping(value = "notDispatch",method = {RequestMethod.POST,RequestMethod.GET})
    public MyRespBundle notDispatch(
            @ApiParam(name = "projectNo", required = false, value = "订单编号") @RequestParam(name = "projectNo", required = false) String projectNo,
            @ApiParam(name = "reason", required = false, value = "不派单原因") @RequestParam(name = "reason", required = false) String reason){
        return sendSuccessMessage(null);
    }
}
