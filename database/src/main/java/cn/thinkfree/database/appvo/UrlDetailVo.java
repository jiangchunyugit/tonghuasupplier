package cn.thinkfree.database.appvo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 资料详情
 * @author gejiaming
 */
@ApiModel("图片+pdf资料详情")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UrlDetailVo {
    /**
     * 地址
     */
    @ApiModelProperty(value = "地址")
    private String url;
    /**
     * 上传时间
     */
    @ApiModelProperty(value = "上传时间")
    private Date uploadTime;
    /**
     * 名字
     */
    @ApiModelProperty(value = "名字")
    private String name;
    /**
     * 文件类型
     */
    @ApiModelProperty(value = "文件类型")
    private Integer fileType;
}
