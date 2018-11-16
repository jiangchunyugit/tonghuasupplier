package cn.thinkfree.core.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xusonghui
 * 订单来源
 */

public enum ProjectSource {

    STATE_1(1, "天猫"),
    STATE_10(10, "下线"),
    STATE_20(20, "运营平台创建"),
    STATE_30(30, "运营平台导入"),
    STATE_40(40, "设计公司创建"),
    STATE_50(50, "设计公司导入"),;

    private int source;

    private String sourceName;

    ProjectSource(int source, String sourceName) {
        this.source = source;
        this.sourceName = sourceName;
    }

    public static ProjectSource queryByState(int state) {
        ProjectSource[] orderSources = ProjectSource.values();
        for (ProjectSource orderSource : orderSources) {
            if (state == orderSource.source) {
                return orderSource;
            }
        }
        throw new RuntimeException("无效的来源");
    }

    public static List<Map<String, Object>> queryAllState() {
        ProjectSource[] orderSources = ProjectSource.values();
        List<Map<String, Object>> sources = new ArrayList<>();
        for (ProjectSource orderSource : orderSources) {
            Map<String, Object> stringMap = new HashMap<>();
            stringMap.put("code", orderSource.source);
            stringMap.put("value", orderSource.sourceName);
            sources.add(stringMap);
        }
        return sources;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }
}
