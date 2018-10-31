package cn.thinkfree.service.construction.vo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;


/**
 * 施工状态返回实体List（查询）
 */
@Getter
@Setter
@Api(tags = "施工状态返回实体List（查询）")
public class ConstructionStateListVo {

    @ApiModelProperty("状态码")
    private Integer stateCode;

    @ApiModelProperty("状态说明")
    private String stateInfo;

}
