package cn.thinkfree.database.appvo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 资料详情(新)
 * @author gejiaming
 */
@ApiModel("图片+pdf资料详情")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewUrlDetailVo {
    @ApiModelProperty(value = "图片集合")
    private List<UrlDetailVo> imgs;
    @ApiModelProperty(value = "房间名称")
    private String roomName;
}
