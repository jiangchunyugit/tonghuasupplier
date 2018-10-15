package cn.thinkfree.database.appvo;

import cn.thinkfree.database.vo.AbsPageSearchCriteria;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * @author gejiaming
 */
@ApiModel(value = "AppProjectSEO--项目列表入参实体",description = "AppProjectSEO--项目列表入参实体")
@Data
public class AppProjectSEO extends AbsPageSearchCriteria {
    @ApiModelProperty(name = "userId",value = "用户编号")
    private String userId;
}
