package cn.thinkfree.service.construction.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@ApiModel(value = "施工订单管理列表-项目派单-数据统计-反参实体（运营后台）--孙宇专用")
public class ConstructionOrderDistributionNumVo {

    @ApiModelProperty("待派单")
    private Integer waitDistributionOrder;

    @ApiModelProperty("待接单")
    private Integer waitReceipt;

    @ApiModelProperty("已接单")
    private Integer alreadyReceipt;

    @ApiModelProperty("订单个数")
    private Integer orderNum;

    @ApiModelProperty("城市列表")
    private List<ConstructionCityVo> cityList;

}
