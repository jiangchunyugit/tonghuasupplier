package cn.thinkfree.database.appvo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author gejiaming
 */
@Data
@AllArgsConstructor
public class DataDetailVo {
    /**
     * 图片地址
     */
    private List<String> urlList;
    /**
     * 上传时间
     */
    private Date uploadTime;
    /**
     * 类型
     */
    private String type;
    /**
     * 展示类型(1,图片 2,PDF)
     */
    private Integer playType;
    /**
     * 是否确认
     */
    private Boolean confirm;
    /**
     * 3D图URL
     */
    private String thirdUrl;
}
