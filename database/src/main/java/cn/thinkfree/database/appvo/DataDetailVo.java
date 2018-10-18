package cn.thinkfree.database.appvo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @author gejiaming
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataDetailVo {
    /**
     * 图片地址
     */
    private List<UrlDetailVo> urlList;
    /**
     * pdf地址
     */
    private String pdfUrl;
    /**
     * 上传时间
     */
    private Date uploadTime;
    /**
     * 类型
     */
    private String type;
    /**
     * 是否确认
     */
    private Boolean confirm;
    /**
     * 3D图URL
     */
    private String thirdUrl;
}
