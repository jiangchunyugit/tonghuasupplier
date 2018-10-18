package cn.thinkfree.controller;

import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.service.approvalflow.ApprovalFlowInstanceService;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 获取审批流实例详情
     * @param num 审批流实例编号
     * @param projectNo 项目编号
     * @param userId 用户ID
     * @return 审批流实例详情
     */
    @ApiOperation("获取审批流实例详情")
    @ResponseBody
    @PostMapping("detail")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "num", value = "审批流实例编号"),
            @ApiImplicitParam(name = "configNum", value = "审批流配置编号"),
            @ApiImplicitParam(name = "projectNo", value = "项目编号", required = true),
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true),
            @ApiImplicitParam(name = "scheduleSort", value = "项目排期编号", required = true),
            @ApiImplicitParam(name = "scheduleVersion", value = "项目排期版本", required = true)
            })
    public MyRespBundle detail(@RequestParam(name = "num", required = false) String num,
                               @RequestParam(name = "configNum", required = false) String configNum,
                               @RequestParam(name = "projectNo") String projectNo,
                               @RequestParam(name = "userId") String userId,
                               @RequestParam(name = "scheduleSort") Integer scheduleSort,
                               @RequestParam(name = "scheduleVersion") Integer scheduleVersion
    ) {
//        if(StringUtils.isEmpty(approvalNum)){
//            throw new RuntimeException(1901,"审批流编号不能为空");
//        }
//        if(StringUtils.isEmpty(projectNo)){
//            throw new CommonException(1901,"项目编号不能为空");
//        }
//        if(StringUtils.isEmpty(userId)){
//            throw new CommonException(1901,"用户ID不能为空");
//        }
        return sendJsonData(ResultMessage.SUCCESS, instanceService.detail(num, configNum, projectNo, userId, scheduleSort, scheduleVersion));
    }

    /**
     * 执行审批操作
     * @param userId
     * @return 成功信息
     */
    @ApiOperation("执行审批操作")
    @ResponseBody
    @PostMapping("approval")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "configNum", value = "审批流配置编号"),
            @ApiImplicitParam(name = "num", value = "审批流实例编号"),
            @ApiImplicitParam(name = "projectNo", value = "项目编号", required = true),
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true),
            @ApiImplicitParam(name = "scheduleSort", value = "项目排期编号", required = true),
            @ApiImplicitParam(name = "scheduleVersion", value = "项目排期版本", required = true)
    })
    public MyRespBundle approval(String configLogNum, String instanceNum, String projectNo, String scheduleSort, Integer scheduleVersion, String data, Integer optionSort, String userId) {

        return sendSuccessMessage(ResultMessage.SUCCESS.message);
    }
}
