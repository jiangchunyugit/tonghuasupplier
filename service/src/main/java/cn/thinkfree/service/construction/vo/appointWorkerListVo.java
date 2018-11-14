package cn.thinkfree.service.construction.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "指派施工人员列表-反参实体（装饰平台）")
public class appointWorkerListVo {

    @ApiModelProperty("ID")
    private String workerId;

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("角色类型")
    private String roleType;

    @ApiModelProperty("项目数量")
    private String projectNum;
}
