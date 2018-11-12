package cn.thinkfree.database.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Auther: jiang
 * @Date: 2018/11/12 14:01
 * @Description: 工地管理施工阶段数量
 */
@Getter
@Setter
@ApiModel(value = "工地管理施工阶段数量")
public class ConstructionStageNunVO {
    //待开工
    @ApiModelProperty("待开工")
    private Integer waitStart ;
    //施工中
    @ApiModelProperty("施工中")
    private Integer underConstruction  ;
    //已完工
    @ApiModelProperty("已完工")
    private Integer completed ;
}
