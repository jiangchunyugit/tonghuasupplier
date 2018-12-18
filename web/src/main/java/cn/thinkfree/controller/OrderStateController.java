package cn.thinkfree.controller;

import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.service.platform.order.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @author xusonghui
 */
@Api(value = "查询所有订单状态", description = "查询所有订单状态")
@Controller
@RequestMapping("order")
public class OrderStateController extends AbsBaseController {

    @Autowired
    private OrderService orderStateService;

    @ApiOperation("查询所有订单状态")
    @ResponseBody
    @RequestMapping(value = "allState", method = {RequestMethod.GET})
    public MyRespBundle<List<Map<String, Object>>> allState() {
        return sendJsonData(ResultMessage.SUCCESS, orderStateService.allState());
    }
}
