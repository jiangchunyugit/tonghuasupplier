package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.constants.OneTrue;
import cn.thinkfree.database.model.PaymentPlan;
import cn.thinkfree.database.model.PaymentProgram;
import cn.thinkfree.database.vo.PaymentPlanVO;
import cn.thinkfree.service.paymentplan.PaymentPlanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/paymentPlan")
@Api(value = "前端使用---支付方案列表---蒋春雨",description = "支付方案信息")
public class PaymentPlanController extends AbsBaseController{


    @Autowired
    PaymentPlanService paymentPlanService;

    /**
     * 支付方案查询
     */
    @GetMapping(value = "/getPaymentPlans")
    @MyRespBody
    @ApiOperation(value="支付方案接口：支付方案列表")
    public MyRespBundle<PaymentPlan> getPaymentPlans(){

        return sendJsonData(ResultMessage.SUCCESS,paymentPlanService.paymentPlans());
    }

    @GetMapping(value = "/paymentPlanDetails")
    @MyRespBody
    @ApiOperation(value="支付方案：详情")
    public MyRespBundle<PaymentPlanVO> paymentPlanDetails(@ApiParam("方案编码")@RequestParam(value = "paymentCode") String paymentCode){

        return sendJsonData(ResultMessage.SUCCESS, paymentPlanService.paymentPlanDetails(paymentCode));
    }
}

