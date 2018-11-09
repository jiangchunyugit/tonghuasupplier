package cn.thinkfree.database.appvo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author gejiaming
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderPlayVo {
    @ApiModelProperty(name = "constructionCompany",value = "承接公司")
    private String constructionCompany;
    @ApiModelProperty(name = "companyUrl",value = "公司个人主页地址")
    private String companyUrl;
    @ApiModelProperty(name = "persionList",value = "人员集合")
    private List<PersionVo> persionList;
    @ApiModelProperty(name = "taskNum",value = "任务数")
    private Integer taskNum;
    @ApiModelProperty(name = "cost",value = "费用")
    private Integer cost;
    @ApiModelProperty(name = "schedule",value = "工期")
    private Integer schedule;
    @ApiModelProperty(name = "delay",value = "延迟天数")
    private Integer delay;
    @ApiModelProperty(value = "排期是否可以编辑(0,不可以 1,可以)")
    private Integer isConfirm;
}
