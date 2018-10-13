package cn.thinkfree.database.appVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@ApiModel(value = "DesignBaseVo,设计订单阶段基础信息")
@Data
@AllArgsConstructor
public class DesignBaseVo {
    @ApiModelProperty(name = "sort",value ="序号" )
    private Integer sort;
    @ApiModelProperty(name = "name",value ="名字" )
    private String name;
}
