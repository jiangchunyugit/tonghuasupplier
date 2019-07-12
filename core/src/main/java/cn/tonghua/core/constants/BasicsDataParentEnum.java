package cn.tonghua.core.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xusonghui
 * 基础枚举配置类
 */

public enum BasicsDataParentEnum {
    /**
     * 平台拒绝接单编码
     */
    CANCEL_PLATFORM("CANCEL_PLATFORM", "平台拒绝接单编码"),
    /**
     * 证件类型编码
     */
    ID_CARD_TYPE("ID_CARD_TYPE", "证件类型编码"),
    /**
     * 国家类型编码
     */
    COUNTRY_TYPE("COUNTRY_TYPE", "国家类型编码"),
    /**
     * 设计师拒绝接单编码
     */
    CANCEL_DESIGNER("CANCEL_DESIGNER", "设计师拒绝接单编码"),
    /**
     * 公司拒绝接单编码
     */
    CANCEL_DESIGN_COMPANY("CANCEL_DESIGN_COMPANY", "公司拒绝接单编码"),
    /**
     * 退款原因编码
     */
    REFUND("REFUND", "退款原因编码"),
    /**
     * 取消施工原因编码
     */
    CANCEL_CONS("CANCEL_CONS", "取消施工原因编码"),
    /**
     * 取消设计原因编码
     */
    CANCEL_DESIGN("CANCEL_DESIGN", "取消设计原因编码"),
    /**
     * 房屋类型
     */
    HOUSE_TYPE("HOUSE_TYPE", "房屋类型"),
    /**
     * 户型结构
     */
    HOUSE_STRUCTURE("HOUSE_STRUCTURE", "户型结构"),
    /**
     * 房屋属性
     */
    HOUSE_ATTR("HOUSE_ATTR", "房屋属性"),
    /**
     * 计费项目
     */
    CHARGING_PROJECT("CHARGING_PROJECT", "计费项目"),
    /**
     * 设计风格
     */
    DESIGN_STYLE("DESIGN_STYLE", "设计风格"),
    /**
     * 订单来源设置
     */
    PROJECT_SOURCE("PROJECT_SOURCE", "订单来源设置"),
    ;
    private String code;
    private String name;

    BasicsDataParentEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static List<Map<String,String>> allTypes(){
        List<Map<String,String>> datas = new ArrayList<>();
        BasicsDataParentEnum [] parentEnums = BasicsDataParentEnum.values();
        for (BasicsDataParentEnum parentEnum : parentEnums){
            Map<String,String> map = new HashMap<>();
            map.put("code",parentEnum.code);
            map.put("name",parentEnum.name);
            datas.add(map);
        }
        return datas;
    }

    public static List<String> allCodes(){
        List<String> datas = new ArrayList<>();
        BasicsDataParentEnum [] parentEnums = BasicsDataParentEnum.values();
        for (BasicsDataParentEnum parentEnum : parentEnums){
            datas.add(parentEnum.code);
        }
        return datas;
    }


}
