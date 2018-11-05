package cn.thinkfree.controller;

import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.database.appvo.OrderTaskSortVo;
import cn.thinkfree.database.pcvo.*;
import cn.thinkfree.service.newproject.NewPcProjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@Api(tags = "PC-项目相关")
@RequestMapping("pcProject")
@RestController
public class PcProjectController {
    @Autowired
    private NewPcProjectService newProjectService;

    @RequestMapping(value = "getPcProjectTask", method = RequestMethod.POST)
    @ApiOperation(value = "PC获取项目详情接口--项目阶段")
    public MyRespBundle<List<OrderTaskSortVo>> getPcProjectTask(@RequestParam(name = "projectNo") @ApiParam(name = "projectNo", value = "项目编号 1223098338391") String projectNo) {
        return newProjectService.getPcProjectTask(projectNo);
    }

    @RequestMapping(value = "getPcProjectConstructionOrder", method = RequestMethod.POST)
    @ApiOperation(value = "PC获取项目详情接口--施工订单")
    public MyRespBundle<ConstructionOrderVO> getPcProjectConstructionOrder(@RequestParam(name = "projectNo") @ApiParam(name = "projectNo", value = "项目编号 1223098338391") String projectNo) {
        return newProjectService.getPcProjectConstructionOrder(projectNo);
    }

    @RequestMapping(value = "getPcProjectDesigner", method = RequestMethod.POST)
    @ApiOperation(value = "PC获取项目详情接口--设计信息")
    public MyRespBundle<DesignerOrderVo> getPcProjectDesigner(@RequestParam(name = "projectNo") @ApiParam(name = "projectNo", value = "项目编号 1223098338391") String projectNo) {
        return newProjectService.getPcProjectDesigner(projectNo);
    }

    @RequestMapping(value = "getPcProjectPreview", method = RequestMethod.POST)
    @ApiOperation(value = "PC获取项目详情接口--预交底信息")
    public MyRespBundle<PreviewVo> getPcProjectPreview(@RequestParam(name = "projectNo") @ApiParam(name = "projectNo", value = "项目编号 1223098338391") String projectNo) {
        return newProjectService.getPcProjectPreview(projectNo);
    }

    @RequestMapping(value = "getPcProjectOffer", method = RequestMethod.POST)
    @ApiOperation(value = "PC获取项目详情接口--报价信息")
    public MyRespBundle<OfferVo> getPcProjectOffer(@RequestParam(name = "projectNo") @ApiParam(name = "projectNo", value = "项目编号 1223098338391") String projectNo) {
        return newProjectService.getPcProjectOffer(projectNo);
    }

    @RequestMapping(value = "getPcProjectContract", method = RequestMethod.POST)
    @ApiOperation(value = "PC获取项目详情接口--合同信息")
    public MyRespBundle<ContractVo> getPcProjectContract(@RequestParam(name = "projectNo") @ApiParam(name = "projectNo", value = "项目编号 1223098338391") String projectNo) {
        return newProjectService.getPcProjectContract(projectNo);
    }

    @RequestMapping(value = "getPcProjectScheduling", method = RequestMethod.POST)
    @ApiOperation(value = "PC获取项目详情接口--施工信息")
    public MyRespBundle<SchedulingVo> getPcProjectScheduling(@RequestParam(name = "projectNo") @ApiParam(name = "projectNo", value = "项目编号 1223098338391") String projectNo) {
        return newProjectService.getPcProjectScheduling(projectNo);
    }

    @RequestMapping(value = "getPcProjectSettlement", method = RequestMethod.POST)
    @ApiOperation(value = "PC获取项目详情接口--结算管理")
    public MyRespBundle<SettlementVo> getPcProjectSettlement(@RequestParam(name = "projectNo") @ApiParam(name = "projectNo", value = "项目编号 1223098338391") String projectNo) {
        return newProjectService.getPcProjectSettlement(projectNo);
    }

/*    @RequestMapping(value = "getPcProjectPayment", method = RequestMethod.POST)
    @ApiOperation(value = "PC获取项目详情接口--支付信息")
    public MyRespBundle<PaymentVo> getPcProjectPayment(@RequestParam(name = "projectNo") @ApiParam(name = "projectNo", value = "项目编号 1223098338391") String projectNo) {
        return newProjectService.getPcProjectPayment(projectNo);
    }*/

    @RequestMapping(value = "getPcProjectEvaluate", method = RequestMethod.POST)
    @ApiOperation(value = "PC获取项目详情接口--评价管理")
    public MyRespBundle<EvaluateVo> getPcProjectEvaluate(@RequestParam(name = "projectNo") @ApiParam(name = "projectNo", value = "项目编号 1223098338391") String projectNo) {
        return newProjectService.getPcProjectEvaluate(projectNo);
    }

    @RequestMapping(value = "getPcProjectInvoice", method = RequestMethod.POST)
    @ApiOperation(value = "PC获取项目详情接口--发票管理")
    public MyRespBundle<InvoiceVo> getPcProjectInvoice(@RequestParam(name = "projectNo") @ApiParam(name = "projectNo", value = "项目编号 1223098338391") String projectNo) {
        return newProjectService.getPcProjectInvoice(projectNo);
    }

}
