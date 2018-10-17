package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.core.utils.JSONUtil;
import cn.thinkfree.database.vo.ApprovalFlowConfigVO;
import cn.thinkfree.database.vo.ApprovalFlowOrderVO;
import cn.thinkfree.service.approvalflow.ApprovalFlowConfigService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/approvalFlowConfig")
@Api(value = "审批流",description = "审批流")
public class ApprovalFlowConfigController extends AbsBaseController{

    @Autowired
    private ApprovalFlowConfigService configService;


    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @MyRespBody
    @ApiOperation(value="查询所有审批流")
    public MyRespBundle list(){
        return sendJsonData(ResultMessage.SUCCESS, configService.list());
    }

    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @MyRespBody
    @ApiOperation(value="审批流节点信息")
    @ApiParam(name = "num", value= "审批流编号", required = true)
    public MyRespBundle detail(@RequestParam(name = "num") String num){
        return sendJsonData(ResultMessage.SUCCESS, configService.detail(num));
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @MyRespBody
    @ApiOperation(value="修改审批流")
    @ApiParam(name = "configLogDTO" ,value = "审批流信息", required = true)
    public MyRespBundle edit(@RequestBody ApprovalFlowConfigVO configLogDTO){
        printInfoMes("configLogDTO:{}", JSONUtil.bean2JsonStr(configLogDTO));
        configService.edit(configLogDTO);
        return sendSuccessMessage(ResultMessage.SUCCESS.message);
    }

    @ApiOperation("添加审批流")
    @ResponseBody
    @PostMapping(value = "add", produces = "application/json")
    public MyRespBundle add(@RequestBody ApprovalFlowConfigVO configLogDTO){
        printInfoMes("configLogDTO:{}", JSONUtil.bean2JsonStr(configLogDTO));
        configService.add(configLogDTO);
        return sendSuccessMessage(ResultMessage.SUCCESS.message);
    }

    @ApiOperation("审批流顺序")
    @ResponseBody
    @PostMapping(value = "order", produces = "application/json")
    public MyRespBundle order(){
        return sendJsonData(ResultMessage.SUCCESS, configService.order());
    }

    @ApiOperation("修改审批流顺序")
    @ResponseBody
    @PostMapping(value = "editOrder", produces = "application/json")
    public MyRespBundle editOrder(@RequestBody List<ApprovalFlowOrderVO> orderVOs){
        configService.editOrder(orderVOs);
        return sendSuccessMessage(ResultMessage.SUCCESS.message);
    }

    @ApiOperation("删除审批流")
    @ResponseBody
    @PostMapping(value = "delete")
    @ApiParam(name = "num" ,value = "审批流编号", required = true)
    public MyRespBundle delete(@RequestParam("num")String num){
        printInfoMes("num:{}", num);
        configService.delete(num);
        return sendSuccessMessage(ResultMessage.SUCCESS.message);
    }
}

