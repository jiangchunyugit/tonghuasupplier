package cn.thinkfree.database.appvo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * @author gejiaming
 */
@ApiModel(value = "SchedulingBaseBigVo--大排期基础信息")
@Data
public class SchedulingBaseBigVo {
    @ApiModelProperty(name = "sort",value ="序号" )
    private Integer sort;
    @ApiModelProperty(name = "name",value ="名字" )
    private String name;
}
