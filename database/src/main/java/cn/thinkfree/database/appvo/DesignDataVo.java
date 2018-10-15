package cn.thinkfree.database.appvo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("设计资料详情")
public class DesignDataVo {
    @ApiModelProperty(name = "parlourList",value = "设计资料")
    private List<DataDetailVo> designDataList;

}
