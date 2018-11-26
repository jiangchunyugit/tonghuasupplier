package cn.thinkfree.controller;

import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.service.platform.order.OrderStateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

/**
 * @author xusonghui
 */
@Api(value = "查询所有订单状态",description = "查询所有订单状态")
@Controller
@RequestMapping("order")
public class OrderStateController extends AbsBaseController{

    @Autowired
    private OrderStateService orderStateService;
    @ApiOperation("查询所有订单状态")
    @GetMapping("allState")
    public MyRespBundle<List<Map<String,String>>> allState(){
        return sendJsonData(ResultMessage.SUCCESS,orderStateService.allState());
    }
}
