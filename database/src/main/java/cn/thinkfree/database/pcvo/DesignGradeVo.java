package cn.thinkfree.database.pcvo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("设计师等级")
public class DesignGradeVo {
    @ApiModelProperty(value = "序号")
    private Integer sort;
    @ApiModelProperty(value = "设计师等级")
    private String grade;
    @ApiModelProperty(value = "升级所需成长值")
    private Integer growthValue;

}
