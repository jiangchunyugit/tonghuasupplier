package cn.thinkfree.database.appvo;

import cn.thinkfree.database.vo.ProjectBigSchedulingDetailsVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("施工端项目搜索项详情")
@Data
public class ProjectScreenVo {
    @ApiModelProperty(value = "进度阶段集合")
    private List<ProjectBigSchedulingDetailsVO> schedulingList;
    @ApiModelProperty(value = "验收阶段集合")
    private List<ProjectBigSchedulingDetailsVO> checkList;
}
