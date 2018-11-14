package cn.thinkfree.service.construction.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@ApiModel(value = "施工订单管理列表-反参实体（运营后台）")
public class ConstructionOrderCommonVo {

    @ApiModelProperty("总页数")
    private Integer countPageNum;

    @ApiModelProperty("订单列表")
    private List<ConstructionOrderListVo> orderList;


}
