package cn.thinkfree.database.appvo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 灵活展示实体
 * @author gejiaming
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlexibleOrderPlayVo {
    private String key;
    private String value;
    private String url;
    private Boolean display;
    private String phone;

    public FlexibleOrderPlayVo(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public FlexibleOrderPlayVo(String key, String value, String url, Boolean display) {
        this.key = key;
        this.value = value;
        this.url = url;
        this.display = display;
    }

    public FlexibleOrderPlayVo(String key, String value, Boolean display, String phone) {
        this.key = key;
        this.value = value;
        this.display = display;
        this.phone = phone;
    }
}
