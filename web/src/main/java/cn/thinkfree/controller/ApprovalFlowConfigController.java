package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.core.utils.JSONUtil;
import cn.thinkfree.database.dto.ApprovalFlowConfigLogDTO;
import cn.thinkfree.service.approvalFlow.ApprovalFlowConfigLogService;
import cn.thinkfree.service.approvalFlow.ApprovalFlowConfigService;
import cn.thinkfree.service.approvalFlow.ApprovalFlowNodeService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/approvalFlowConfig")
@Api(value = "审批流",description = "审批流")
public class ApprovalFlowConfigController extends AbsBaseController{

    @Autowired
    private ApprovalFlowConfigService configService;
    @Autowired
    private ApprovalFlowConfigLogService configLogService;
    @Resource
    private ApprovalFlowNodeService nodeService;


    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @MyRespBody
    @ApiOperation(value="查询所有审批流")
    public MyRespBundle list(){
        return sendJsonData(ResultMessage.SUCCESS, configService.list());
    }

    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @MyRespBody
    @ApiOperation(value="审批流节点信息")
    @ApiParam(name = "approvalFlowNum", value= "审批流编号", required = true)
    public MyRespBundle detail(@RequestParam(name = "approvalFlowNum") String approvalFlowNum){
        return sendJsonData(ResultMessage.SUCCESS, configLogService.detail(approvalFlowNum));
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @MyRespBody
    @ApiOperation(value="修改审批流")
    @ApiParam(name = "configLogDTO" ,value = "审批流信息", required = true)
    public MyRespBundle edit(@RequestBody ApprovalFlowConfigLogDTO configLogDTO){
        printInfoMes("configLogDTO:{}", JSONUtil.bean2JsonStr(configLogDTO));
        configService.edit(configLogDTO);
        return sendSuccessMessage(ResultMessage.SUCCESS.message);
    }

    @ApiOperation("添加审批流")
    @ResponseBody
    @PostMapping(value = "add", produces = "application/json")
    public MyRespBundle add(@RequestBody ApprovalFlowConfigLogDTO configLogDTO){
        printInfoMes("configLogDTO:{}", JSONUtil.bean2JsonStr(configLogDTO));
        configService.add(configLogDTO);
        return sendSuccessMessage(ResultMessage.SUCCESS.message);
    }

    @ApiOperation("删除审批流")
    @ResponseBody
    @PostMapping(value = "delete")
    @ApiParam(name = "approvalFlowNum" ,value = "审批流编号", required = true)
    public MyRespBundle delete(@RequestParam("approvalFlowNum")String approvalFlowNum){
        printInfoMes("approvalFlowNum:{}", approvalFlowNum);
        configService.delete(approvalFlowNum);
        return sendSuccessMessage(ResultMessage.SUCCESS.message);
    }

    @ApiOperation("查询审批流审批角色顺序")
    @ResponseBody
    @PostMapping(value = "findNodeRoleSequence")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "approvalFlowNum", value = "审批流编号"),
            @ApiImplicitParam(name = "companyId", value = "公司编号"),
            @ApiImplicitParam(name = "projectBigSchedulingId", value = "项目节点编号")
            })
    public MyRespBundle findNodeRoleSequence(@RequestParam("approvalFlowNum")String approvalFlowNum,
                                             @RequestParam("companyId")String companyId,
                                             @RequestParam("projectBigSchedulingId")long projectBigSchedulingId){
        printInfoMes("approvalFlowNum:{},companyId:{},projectBigSchedulingId:{}", approvalFlowNum, companyId, projectBigSchedulingId);
        return sendJsonData(ResultMessage.SUCCESS, nodeService.findNodeRoleSequence(approvalFlowNum, companyId, projectBigSchedulingId));
    }
}

