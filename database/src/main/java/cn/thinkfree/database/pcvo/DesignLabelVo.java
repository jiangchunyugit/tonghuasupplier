package cn.thinkfree.database.pcvo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("设计师标签")
@Data
public class DesignLabelVo {
    @ApiModelProperty(value = "序号")
    private Integer sort;
    @ApiModelProperty(value = "标签名称")
    private String name;
    @ApiModelProperty(value = "所需成长值")
    private Integer needGrowthValue;

}
