package cn.thinkfree.controller;


import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.service.construction.ConstructionOrderOperate;
import cn.thinkfree.service.construction.ConstructionStateService;
import cn.thinkfree.service.construction.ConstructionStateServiceB;
import cn.thinkfree.service.construction.vo.ConstructionOrderListVo;
import cn.thinkfree.service.construction.vo.ConstructionOrderManageVo;
import cn.thinkfree.service.construction.vo.ConstructionStateVo;
import cn.thinkfree.service.construction.vo.SiteDetailsVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *  施工状态相关接口
 */
@Api(value = "施工订单状态接口API---->刘博/佳明/../..", tags = "施工订单状态接口API")
@Controller
@RequestMapping("constructionState")
public class ConstructionStateController extends AbsBaseController {

    @Autowired
    ConstructionStateServiceB constructionStateServiceB;


    @ApiOperation("查询当前状态")
    @MyRespBody
    @RequestMapping(value = "getState", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<String> getState(@RequestParam @ApiParam(value = "订单编号",required = true) String orderNo,
                                         @RequestParam @ApiParam(value = "1获取平台状态，2获取装饰公司状态，3获取施工人员状态，4获取消费者状态",required = true) int type) {

        return constructionStateServiceB.getStateInfo(orderNo,type);
    }


    @ApiOperation("运营平台-派单给装饰公司")
    @MyRespBody
    @RequestMapping(value = "upOperateDispatch", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<String> operateDispatchToConstruction(@RequestParam @ApiParam(value = "订单编号",required = true) String orderNo) {

        return constructionStateServiceB.operateDispatchToConstruction(orderNo);
    }

    @ApiOperation("装饰公司-派单给服务人员")
    @MyRespBody
    @RequestMapping(value = "constructionForEmployee", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<String> constructionStateForEmployee(@RequestParam @ApiParam(value = "订单编号",required = true) String orderNo) {

        return constructionStateServiceB.constructionState(orderNo,1);
    }

    @ApiOperation("装饰公司-施工报价完成")
    @MyRespBody
    @RequestMapping(value = "constructionPriceComplete", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<String> constructionPriceComplete(@RequestParam @ApiParam(value = "订单编号",required = true) String orderNo) {

        return constructionStateServiceB.constructionState(orderNo,2);
    }

    @ApiOperation("装饰公司-审核完成")
    @MyRespBody
    @RequestMapping(value = "constructionExamineComplete", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<String> constructionExamineComplete(@RequestParam @ApiParam(value = "订单编号",required = true) String orderNo) {

        return constructionStateServiceB.constructionState(orderNo,3);
    }

    @ApiOperation("装饰公司-合同录入")
    @MyRespBody
    @RequestMapping(value = "constructionContractEntry", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<String> constructionContractEntry(@RequestParam @ApiParam(value = "订单编号",required = true) String orderNo) {

        return constructionStateServiceB.constructionState(orderNo,4);
    }

    @ApiOperation("装饰公司-确认线下签约完成（自动创建工地项目）")
    @MyRespBody
    @RequestMapping(value = "constructionSignComplete", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<String> constructionSignComplete(@RequestParam @ApiParam(value = "订单编号",required = true) String orderNo) {

        return constructionStateServiceB.constructionState(orderNo,5);
    }

    /**
     *  订单支付
     */
    @ApiOperation("消费者-首期款支付")
    @MyRespBody
    @RequestMapping(value = "constructionFirstPay", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<String> constructionFirstPay(@RequestParam @ApiParam(value = "订单编号",required = true) String orderNo) {

        return constructionStateServiceB.customerPay(orderNo,1);
    }

    @ApiOperation("施工人员-开工报告")
    @MyRespBody
    @RequestMapping(value = "commencementReport", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<String> commencementReport(@RequestParam @ApiParam(value = "订单编号",required = true) String orderNo) {

        return constructionStateServiceB.customerPay(orderNo,2);
    }

    @ApiOperation("施工人员-阶段验收通过")
    @MyRespBody
    @RequestMapping(value = "constructionStageCheck", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<String> constructionStageCheck(@RequestParam @ApiParam(value = "订单编号",required = true) String orderNo) {

        return constructionStateServiceB.customerPay(orderNo,3);
    }

    @ApiOperation("消费者-支付阶段款")
    @MyRespBody
    @RequestMapping(value = "constructionPayStage", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<String> constructionPayStage(@RequestParam @ApiParam(value = "订单编号",required = true) String orderNo) {

        return constructionStateServiceB.customerPay(orderNo,4);
    }


    @ApiOperation("施工人员-等待尾款支付（验收通过）")
    @MyRespBody
    @RequestMapping(value = "waitForTheTail", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<String> waitForTheTail(@RequestParam @ApiParam(value = "订单编号",required = true) String orderNo) {

        return constructionStateServiceB.customerPay(orderNo,5);
    }

    @ApiOperation("订单完成-支付尾款后")
    @MyRespBody
    @RequestMapping(value = "orderComplete", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<String> orderComplete(@RequestParam @ApiParam(value = "订单编号",required = true) String orderNo) {

        return constructionStateServiceB.orderComplete(orderNo);
    }

    @ApiOperation("消费者-取消订单(签约阶段逆向)")
    @MyRespBody
    @RequestMapping(value = "customerCancelOrder", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<String> customerCancelOrder(@RequestParam @ApiParam(value = "订单编号",required = true) String orderNo) {

        return constructionStateServiceB.customerCancelOrder(orderNo);
    }

    @ApiOperation("消费者-取消订单(支付未开工逆向)")
    @MyRespBody
    @RequestMapping(value = "customerCancelOrderForPay", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<String> customerCancelOrderForPay(@RequestParam @ApiParam(value = "订单编号",required = true) String orderNo) {

        return constructionStateServiceB.customerCancelOrder(orderNo);
    }


}
