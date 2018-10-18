package cn.thinkfree.database.appvo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author gejiaming
 */
@ApiModel(value = "DesignBaseVo,设计订单阶段基础信息")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderTaskSortVo {
    @ApiModelProperty(name = "sort",value ="序号" )
    private Integer sort;
    @ApiModelProperty(name = "name",value ="名字" )
    private String name;
}
