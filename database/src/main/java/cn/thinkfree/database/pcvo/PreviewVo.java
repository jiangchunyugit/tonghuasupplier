package cn.thinkfree.database.pcvo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@ApiModel("预交底实体")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PreviewVo {
    @ApiModelProperty(value = "预交底发起时间")
    private Date launchTime;
    @ApiModelProperty(value = "预交底状态")
    private String launchStatus;
    @ApiModelProperty(value = "项目经理")
    private String projectManager;
    @ApiModelProperty(value = "工长")
    private String foreman;
    @ApiModelProperty(value = "管家")
    private String housekeeper;
}
