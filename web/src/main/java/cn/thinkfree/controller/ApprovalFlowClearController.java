package cn.thinkfree.controller;

import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.service.approvalflow.ApprovalFlowConfigService;
import cn.thinkfree.service.approvalflow.ApprovalFlowInstanceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 初始化审批配置与审批记录（测试用）
 *
 * @author song
 * @version 1.0
 * @date 2018/10/23 17:41
 */
@RestController
@RequestMapping(value = "/approvalFlowClear")
@Api(value = "初始化审批配置与审批记录",description = "初始化审批配置与审批记录")
public class ApprovalFlowClearController extends AbsBaseController {

    @Resource
    private ApprovalFlowConfigService configService;
    @Resource
    private ApprovalFlowInstanceService instanceService;

    @ApiOperation("清除多余审批流配置")
    @PostMapping("clearConfig")
    public MyRespBundle clearConfig(){
        configService.clearConfig();
        return sendSuccessMessage(ResultMessage.SUCCESS.message);
    }

    @ApiOperation("清除审批记录")
    @PostMapping("clearApprovalLog")
    public MyRespBundle clearApprovalLog(){
        instanceService.clearApprovalLog();
        return sendSuccessMessage(ResultMessage.SUCCESS.message);
    }
}
