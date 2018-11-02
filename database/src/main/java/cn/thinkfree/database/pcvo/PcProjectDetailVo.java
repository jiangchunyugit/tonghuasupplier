package cn.thinkfree.database.pcvo;

import cn.thinkfree.database.appvo.OrderTaskSortVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("pc端项目详情")
@Data
public class PcProjectDetailVo {
    @ApiModelProperty(name = "orderTaskSortVoList", value = "订单任务")
    private List<OrderTaskSortVo> orderTaskSortVoList;
    @ApiModelProperty(name = "taskStage", value = "任务阶段")
    private Integer taskStage;
    @ApiModelProperty("施工订单")
    private ConstructionOrderVO constructionOrderVO;
    @ApiModelProperty("设计订单")
    private DesignerOrderVo designerOrderVo;
    @ApiModelProperty("报价信息")
    private OfferVo offerVo;
    @ApiModelProperty("合同信息")
    private ContractVo contractVo;
    @ApiModelProperty("施工信息")
    private SchedulingVo schedulingVo;
    @ApiModelProperty("结算信息")
    private SettlementVo settlementVo;
    @ApiModelProperty("评价管理")
    private EvaluateVo evaluateVo;
    @ApiModelProperty("发票管理")
    private InvoiceVo invoiceVo;
}
