package cn.thinkfree.database.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * TODO
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
    String planNo;
    @ApiModelProperty("用户编号")
    String userId;
}
