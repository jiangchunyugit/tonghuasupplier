package cn.thinkfree.controller;

import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.service.platform.employee.ProjectUserService;
import cn.thinkfree.service.platform.order.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @author xusonghui
 */
@Api(value = "订单相关操作", description = "订单相关操作")
@Controller
@RequestMapping("order")
public class OrderController extends AbsBaseController {

    @Autowired
    private OrderService orderStateService;
    @Autowired
    private ProjectUserService projectUserService;

    @ApiOperation("查询所有订单状态")
    @ResponseBody
    @RequestMapping(value = "allState", method = {RequestMethod.GET})
    public MyRespBundle<List<Map<String, Object>>> allState() {
        return sendJsonData(ResultMessage.SUCCESS, orderStateService.allState());
    }

    @ApiOperation("项目移交")
    @ResponseBody
    @RequestMapping(value = "transferEmployee", method = {RequestMethod.GET, RequestMethod.POST})
    public MyRespBundle<String> transferEmployee(
            @ApiParam(name = "transferUserId", required = false, value = "移交人ID") @RequestParam(name = "transferUserId", required = false) String transferUserId,
            @ApiParam(name = "beTransferUserId", required = false, value = "被移交人ID") @RequestParam(name = "beTransferUserId", required = false) String beTransferUserId,
            @ApiParam(name = "projectNo", required = false, value = "项目编号") @RequestParam(name = "projectNo", required = false) String projectNo,
            @ApiParam(name = "roleCode", required = false, value = "角色编码") @RequestParam(name = "roleCode", required = false) String roleCode) {
        try {
            projectUserService.transferEmployee(transferUserId, beTransferUserId, projectNo, roleCode);
            return sendSuccessMessage(null);
        } catch (Exception e) {
            e.printStackTrace();
            return sendFailMessage(e.getMessage());
        }
    }
}
