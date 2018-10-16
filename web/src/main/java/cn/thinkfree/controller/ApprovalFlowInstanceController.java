package cn.thinkfree.controller;

import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.service.approvalflow.ApprovalFlowInstanceService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 审批流实例控制层
 * @author song
 * @date 2018/10/12 17:28
 * @version 1.0
 */
@RestController
@RequestMapping(value = "/approvalFlowInstance")
@Api(value = "审批流实例",description = "审批流实例")
public class ApprovalFlowInstanceController extends AbsBaseController {

    @Resource
    private ApprovalFlowInstanceService instanceService;


}
