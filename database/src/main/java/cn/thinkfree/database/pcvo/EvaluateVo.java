package cn.thinkfree.database.pcvo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@ApiModel("评价管理")
@AllArgsConstructor
@NoArgsConstructor
public class EvaluateVo {
    @ApiModelProperty(value = "设计服务")
    private Integer design;
    @ApiModelProperty(value = "施工质量")
    private Integer construction;
    @ApiModelProperty(value = "服务态度")
    private Integer attitude;
}
