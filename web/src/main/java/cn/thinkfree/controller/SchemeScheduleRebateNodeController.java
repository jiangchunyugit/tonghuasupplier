package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.vo.rebate.SchemeScheduleRebateNodeVO;
import cn.thinkfree.service.rebate.SchemeScheduleRebateNodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 方案返款节点服务层控制层
 *
 * @author song
 * @version 1.0
 * @date 2018/12/19 15:11
 */
@RestController
@Api(description = "排期返款节点关联")
@RequestMapping("schedule-rebate-node")
public class SchemeScheduleRebateNodeController extends AbsBaseController {

    @Autowired
    private SchemeScheduleRebateNodeService schemeScheduleRebateNodeService;


    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @MyRespBody
    @ApiOperation(value="前端-展示排期返款节点关联")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "schemeNo", value = "方案编号", required = true)
    })
    public MyRespBundle<SchemeScheduleRebateNodeVO> list(@RequestParam(name = "schemeNo") String schemeNo){
        return sendJsonData(ResultMessage.SUCCESS, schemeScheduleRebateNodeService.list(schemeNo));
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @MyRespBody
    @ApiOperation(value="前端-修改排期返款节点关联")
    public MyRespBundle edit(@RequestBody SchemeScheduleRebateNodeVO schemeScheduleRebateNodeVO){
        schemeScheduleRebateNodeService.edit(schemeScheduleRebateNodeVO);
        return sendSuccessMessage(ResultMessage.SUCCESS.message);
    }
}
