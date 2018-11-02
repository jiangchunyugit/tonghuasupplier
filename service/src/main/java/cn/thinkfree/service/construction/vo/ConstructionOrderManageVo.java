package cn.thinkfree.service.construction.vo;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Api(tags = "施工订单管理-页面-反参实体（运营后台）")
public class ConstructionOrderManageVo {

    @ApiModelProperty("待审核")
    private String waitExamine;

    @ApiModelProperty("待签约")
    private String waitSign;

    @ApiModelProperty("待支付")
    private String waitPay;

    @ApiModelProperty("订单个数")
    private Integer orderNum;

    @ApiModelProperty("总页数")
    private Integer countPageNum;

    @ApiModelProperty("城市列表")
    private List<ConstructionCityVo> cityList;

    @ApiModelProperty("订单列表")
    private List<ConstructionOrderListVo> orderList;


}
