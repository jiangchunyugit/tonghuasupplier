package cn.thinkfree.service.construction.vo;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@ApiModel(value = "施工订单管理列表-反参实体（运营后台）--孙宇专用")
public class ConstructionOrderManageVo {

    @ApiModelProperty("待审核")
    private Integer waitExamine;

    @ApiModelProperty("待签约")
    private Integer waitSign;

    @ApiModelProperty("待支付")
    private Integer waitPay;

    @ApiModelProperty("订单个数")
    private Integer orderNum;

    @ApiModelProperty("总页数")
    private Integer countPageNum;

    @ApiModelProperty("城市列表")
    private List<ConstructionCityVo> cityList;

    @ApiModelProperty("订单列表")
    private List<ConstructionOrderListVo> orderList;


}
