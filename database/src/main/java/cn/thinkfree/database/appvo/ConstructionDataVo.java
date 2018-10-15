package cn.thinkfree.database.appvo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "ConstructionDataVo,施工资料实体")
public class ConstructionDataVo {
    @ApiModelProperty(name = "constructionDataList",value = "施工资料集合")
    private List<DataDetailVo> constructionDataList;
}
