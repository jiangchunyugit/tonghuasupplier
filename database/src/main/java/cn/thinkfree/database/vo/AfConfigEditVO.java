package cn.thinkfree.database.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 修改审批流配置
 *
 * @author song
 * @version 1.0
 * @date 2018/10/30 17:44
 */
@Data
@ApiModel("修改审批流配置")
public class AfConfigEditVO {
    @ApiModelProperty("审批流配置")
    List<AfConfigVO> configVOs;
    @ApiModelProperty("方案编号")
    String schemeNo;
    @ApiModelProperty("用户编号")
    String userId;
}
