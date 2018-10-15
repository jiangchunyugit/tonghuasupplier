package cn.thinkfree.database.appvo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("设计资料详情")
public class DataVo {
    @ApiModelProperty(name = "parlourList",value = "资料集合")
    private List<DataDetailVo> dataList;

}
