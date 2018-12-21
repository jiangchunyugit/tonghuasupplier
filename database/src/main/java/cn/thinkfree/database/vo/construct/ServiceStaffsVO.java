package cn.thinkfree.database.vo.construct;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 服务人员信息
 *
 * @author song
 * @version 1.0
 * @date 2018/12/21 11:38
 */
@ApiModel("服务人员信息")
@Data
public class ServiceStaffsVO {
    @ApiModelProperty("项目经理")
    private String projectManager;
    @ApiModelProperty("工长")
    private String foreman;
    @ApiModelProperty("管家")
    private String steward;
    @ApiModelProperty("设计师")
    private String designer;
}
