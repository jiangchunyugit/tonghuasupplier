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
 * @date 2018/10/27 16:18
 */
@Data
@ApiModel("审批流实例信息")
public class AfInstanceListVO {
    @ApiModelProperty("审批实例")
    List<AfInstanceVO> instances;
    @ApiModelProperty("发起菜单")
    List<AfStartMenuVO> startMenus;
}
