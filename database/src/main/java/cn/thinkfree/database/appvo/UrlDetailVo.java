package cn.thinkfree.database.appvo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

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
    @ApiModelProperty(value = "效果图地址")
    private String imgUrl;
    @ApiModelProperty(value = "全景图地址")
    private String photo360Url;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "效果图时间--上传时间")
    private Date uploadTime;
    @ApiModelProperty(value = "资料名字")
    private String name;
}
