package cn.thinkfree.controller;

import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.service.approvalflow.ApprovalFlowMessageLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 审批流消息
 *
 * @author song
 * @version 1.0
 * @date 2018/10/23 10:22
 */
@RestController
@RequestMapping(value = "/approvalFlowMessage")
@Api(value = "审批流消息",description = "审批流消息")
public class ApprovalFlowMessageController extends AbsBaseController {

    @Resource
    private ApprovalFlowMessageLogService messageLogService;

    @ApiOperation("获取用户的消息")
    @PostMapping("findByUserId")
    @ApiParam(name = "userId", value = "用户Id", required = true)
    public MyRespBundle findByUserId( @RequestParam(name = "userId") String userId){
        return sendJsonData(ResultMessage.SUCCESS, messageLogService.findByUserId(userId));
    }

    @ApiOperation("获取用户的消息总数")
    @PostMapping("countOfUnread")
    @ApiParam(name = "userId", value = "用户Id", required = true)
    public MyRespBundle countOfUnread( @RequestParam(name = "userId") String userId){
        return sendJsonData(ResultMessage.SUCCESS, messageLogService.countOfUnread(userId));
    }
}
