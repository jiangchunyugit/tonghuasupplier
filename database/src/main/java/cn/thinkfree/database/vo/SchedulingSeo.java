package cn.thinkfree.database.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author gejiaming
 */
@ApiModel(value = "SchedulingSeo--排期入参分页实体", description = "SchedulingSeo--排期入参分页实体")
@Data
public class SchedulingSeo extends AbsPageSearchCriteria {
    @ApiModelProperty(name = "companyId",value = "公司编号")
    private String companyId;
    @ApiModelProperty(value = "该项目所使用的方案编号")
    private String schemeNo;
}
