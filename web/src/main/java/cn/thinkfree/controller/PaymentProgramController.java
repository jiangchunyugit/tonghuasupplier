package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.utils.BeanValidator;
import cn.thinkfree.database.vo.PaymentProgramVO;
import cn.thinkfree.database.vo.Severitys;
import cn.thinkfree.service.paymentprogram.PaymentProgramService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/paymentProgramte")
@Api(value = "编辑支付方案接口",description = "支付方案信息")
public class PaymentProgramController extends AbsBaseController{


    @Autowired
    PaymentProgramService paymentProgramService;
    /**
     * 新增支付方案
     */
    @PostMapping(value = "/savePaymentProgram")
    @MyRespBody
    @ApiOperation(value="编辑支付方案接口：新增支付方案")
    public MyRespBundle<String> savePaymentProgram(@ApiParam("支付方案信息")@RequestBody PaymentProgramVO paymentProgramVO){

        BeanValidator.validate(paymentProgramVO, Severitys.Update.class);
        if (paymentProgramService.addPaymentProgram(paymentProgramVO)) {

            return sendJsonData(ResultMessage.SUCCESS,"操作成功");
        }
        return sendJsonData(ResultMessage.SUCCESS,"操作失败");
    }

    /**
     * 支付方案查询
     */
    @GetMapping(value = "/getPaymentProgram")
    @MyRespBody
    @ApiOperation(value="编辑支付方案接口：支付方案查询")
    public MyRespBundle<PaymentProgramVO> getPaymentProgram(@ApiParam("支付方案查询")PaymentProgramVO paymentProgramVO){

        BeanValidator.validate(paymentProgramVO, Severitys.Insert.class);
        return sendJsonData(ResultMessage.SUCCESS,paymentProgramService.getPaymentPrograms(paymentProgramVO));
    }

}

