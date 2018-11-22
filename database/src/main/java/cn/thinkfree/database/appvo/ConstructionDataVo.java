package cn.thinkfree.database.appvo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("施工资料实体")
@Data
public class ConstructionDataVo {
    @ApiModelProperty(value = "图片地址")
    private List<UrlDetailVo> urlList;
    @ApiModelProperty(value = "是否确认 0,未确认 1,确认")
    private Integer confirm;
}
