package cn.thinkfree.service.construction.vo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 施工状态返回实体（查询）
 */
@Getter
@Setter
@ApiModel(value = "施工订单管理列表-反参实体（运营后台）--孙宇专用")
public class ConstructionStateVo {

    @ApiModelProperty("当前状态")
    private String stateInfo;
    @ApiModelProperty("下一个状态")
    private List<ConstructionStateListVo> nextState;
    @ApiModelProperty("下一个状态变更权限")
    private boolean isState;
}
