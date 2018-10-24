package cn.thinkfree.controller;

import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.vo.ApprovalFlowApprovalVO;
import cn.thinkfree.service.approvalflow.ApprovalFlowInstanceService;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
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
     * @param instanceNum 审批流实例编号
     * @param configNum 审批流配置编号
     * @param companyNo 公司编号
     * @param projectNo 项目编号
     * @param userId 用户Id
     * @param scheduleSort 排期编号
     * @param scheduleVersion 排期版本
     * @return 审批流实例详情
     */
    @ApiOperation("获取审批流实例详情")
    @ResponseBody
    @PostMapping("detail")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "instanceNum", value = "审批流实例编号"),
            @ApiImplicitParam(name = "configNum", value = "审批流配置编号"),
            @ApiImplicitParam(name = "companyNo", value = "项目编号", required = true),
            @ApiImplicitParam(name = "projectNo", value = "项目编号", required = true),
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true),
            @ApiImplicitParam(name = "scheduleSort", value = "项目排期编号"),
            @ApiImplicitParam(name = "scheduleVersion", value = "项目排期版本")
            })
    public MyRespBundle detail(@RequestParam(name = "instanceNum", required = false) String instanceNum,
                               @RequestParam(name = "configNum", required = false) String configNum,
                               @RequestParam(name = "companyNo") String companyNo,
                               @RequestParam(name = "projectNo") String projectNo,
                               @RequestParam(name = "userId") String userId,
                               @RequestParam(name = "scheduleSort", required = false) Integer scheduleSort,
                               @RequestParam(name = "scheduleVersion", required = false) Integer scheduleVersion) {
        if(StringUtils.isEmpty(instanceNum)){
            if (StringUtils.isEmpty(configNum)) {
                throw new RuntimeException("审批流配置编号不能为空！");
            }
            if (StringUtils.isEmpty(companyNo)) {
                throw new RuntimeException("公司编号不能为空！");
            }
            if (StringUtils.isEmpty(projectNo)) {
                throw new RuntimeException("项目编号不能为空！");
            }
            if (null == scheduleSort) {
                throw new RuntimeException("排期编号不能为空！");
            }
        }
        if (StringUtils.isEmpty(userId)) {
            throw new RuntimeException("用户ID不能为空！");
        }
        return sendJsonData(ResultMessage.SUCCESS, instanceService.detail(instanceNum, configNum, companyNo, projectNo, userId, scheduleSort, scheduleVersion));
    }

    /**
     * 执行审批操作
     * @param approvalVO 审批信息
     * @return 成功信息
     */
    @ApiOperation("执行审批操作")
    @ResponseBody
    @PostMapping("approval")
    public MyRespBundle approval(@RequestBody ApprovalFlowApprovalVO approvalVO) {
        instanceService.approval(approvalVO);
        return sendSuccessMessage(ResultMessage.SUCCESS.message);
    }
}
