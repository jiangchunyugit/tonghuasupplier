package cn.tonghua.service.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * PDF配置
 *
 * @author song
 * @version 1.0
 * @date 2018/11/11 15:13
 */
@Data
@Component
@ConfigurationProperties(prefix = "pdf")
public class PdfConfig {

    /**
     * 模板文件夹
     */
    private String templateDir;
    /**
     * 导出文件夹
     */
    private String exportDir;
    /**
     * 字体文件夹
     */
    private String fontDir;

    @Value("${custom.cloud.fileUpload}")
    private String fileUploadUrl;

}
