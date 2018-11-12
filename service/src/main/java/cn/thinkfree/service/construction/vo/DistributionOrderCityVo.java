package cn.thinkfree.service.construction.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;



/**
 * 施工派单--公司/城市实体
 */
@Getter
@Setter
@ApiModel(value = "施工派单--公司/城市实体（运营后台）")
public class DistributionOrderCityVo {

    @ApiModelProperty("城市")
    private String city;

    @ApiModelProperty("公司名称")
    private String companyName;

    @ApiModelProperty("公司ID")
    private String companyId;
}
