package cn.thinkfree.core.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xusonghui
 * 功能项常量枚举类
 */

public enum RoleFunctionEnum {
    /**
     * 编辑排期计划功能
     */
    EDIT_SCHEDULING("edit_scheduling","编辑排期计划功能"),
    /**
     * 拥有设计师的权力
     */
    DESIGN_POWER("design_power","拥有设计师的权力"),
    /**
     * 拥有业主的权力
     */
    OWNER_POWER("owner_power","拥有业主的权力"),
    ;

    private String functionCode;

    private String describe;

    RoleFunctionEnum(String functionCode, String describe) {
        this.functionCode = functionCode;
        this.describe = describe;
    }

    public String getFunctionCode() {
        return functionCode;
    }

    public String getDescribe() {
        return describe;
    }

    /**
     * 获取所有功能项
     * @return
     */
    public List<Map<String,String>> allFunction(){
        RoleFunctionEnum[] functionEnums = RoleFunctionEnum.values();
        List<Map<String,String>> data = new ArrayList<>();
        for (RoleFunctionEnum functionEnum : functionEnums){
            Map<String,String> map = new HashMap<>();
            map.put("functionCode",functionEnum.functionCode);
            map.put("describe",functionEnum.describe);
            data.add(map);
        }
        return data;
    }

    /**
     * 获取所有功能项编码
     * @return
     */
    public List<String> allFunctionCode(){
        RoleFunctionEnum[] functionEnums = RoleFunctionEnum.values();
        List<String> data = new ArrayList<>();
        for (RoleFunctionEnum functionEnum : functionEnums){
            data.add(functionEnum.functionCode);
        }
        return data;
    }
}
