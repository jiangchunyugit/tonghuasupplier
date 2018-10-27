package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.service.approvalflow.AfInstanceService;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * TODO
 *
 * @author song
 * @version 1.0
 * @date 2018/10/25 18:22
 */
@RestController
@Api(value = "审批流实例")
@RequestMapping("af-instance")
public class AfInstanceController extends AbsBaseController {

    @Resource
    private AfInstanceService instanceService;


    @RequestMapping(value = "/start", method = RequestMethod.POST)
    @MyRespBody
    @ApiOperation(value="查询所有审批流")
    public MyRespBundle start(){
        instanceService.start();
        return sendSuccessMessage(ResultMessage.SUCCESS.message);
    }

    @RequestMapping(value = "/approval", method = RequestMethod.POST)
    @MyRespBody
    @ApiOperation(value="审批流节点信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "instanceNo", value = "审批流实例编码", required = true),
            @ApiImplicitParam(name = "userId", value = "用户编码", required = true),
            @ApiImplicitParam(name = "option", value = "用户选择", required = true)
    })
    @ApiParam(name = "num", value= "审批流编号", required = true)
    public MyRespBundle approval(@RequestParam(name = "instanceNo") String instanceNo, @RequestParam(name = "userId") String userId, @RequestParam(name = "option") Integer option){
        instanceService.approval(instanceNo, userId, option);
        return sendSuccessMessage(ResultMessage.SUCCESS.message);
    }
}
