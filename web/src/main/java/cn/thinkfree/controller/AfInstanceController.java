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
    @ApiOperation(value="访问审批开始页面")
    public MyRespBundle start(String projectNo, String userId, String configNo){
        return sendJsonData(ResultMessage.SUCCESS, instanceService.start(projectNo, userId, configNo));
    }

    @RequestMapping(value = "/submitStart", method = RequestMethod.POST)
    @MyRespBody
    @ApiOperation(value="发起审批")
    public MyRespBundle submitStart(String projectNo, String userId, String planNo, Integer scheduleSort, String data, String remark){
        instanceService.submitStart(projectNo, userId, planNo, scheduleSort, data, remark);
        return sendSuccessMessage(ResultMessage.SUCCESS.message);
    }

    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @MyRespBody
    @ApiOperation(value="查询所有审批流")
    public MyRespBundle detail(String instanceNo, String userId){
        return sendJsonData(ResultMessage.SUCCESS, instanceService.detail(instanceNo, userId));
    }

    @RequestMapping(value = "/approval", method = RequestMethod.POST)
    @MyRespBody
    @ApiOperation(value="审批流节点信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "instanceNo", value = "审批流实例编码", required = true),
            @ApiImplicitParam(name = "userId", value = "用户编码", required = true),
            @ApiImplicitParam(name = "option", value = "用户选择", required = true),
            @ApiImplicitParam(name = "remark", value = "备注")
    })
    public MyRespBundle approval(@RequestParam(name = "instanceNo") String instanceNo,
                                 @RequestParam(name = "userId") String userId,
                                 @RequestParam(name = "option") Integer option,
                                 @RequestParam(name = "remark", required = false) String remark){
        instanceService.approval(instanceNo, userId, option, remark);
        return sendSuccessMessage(ResultMessage.SUCCESS.message);
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @MyRespBody
    @ApiOperation(value="审批记录信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户编码", required = true),
            @ApiImplicitParam(name = "projectNo", value = "用户选择", required = true),
            @ApiImplicitParam(name = "scheduleSort", value = "scheduleSort", required = true)
    })
    public MyRespBundle list(@RequestParam(name = "userId") String userId,
                                 @RequestParam(name = "projectNo") String projectNo,
                                 @RequestParam(name = "scheduleSort", required = false) Integer scheduleSort){
        return sendJsonData(ResultMessage.SUCCESS, instanceService.list(userId, projectNo, scheduleSort));
    }
}
