package cn.thinkfree.database.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ying007
 * 公司入驻审批状态 0待激活1已激活2财务审核中3财务审核成功4财务审核失败5待交保证金6已交保证金 7入驻成功
 */
public enum CompanyAuditStatus {
    /**
     * 待激活
     */
//    NOTACTIVATION(0,"待激活"),
    /**
     * 已激活
     */
    JOINING(0,"入驻中"),
    /**
     * 资质待审核中
     */
    AUDITING(1,"资质待审核"),


    /**
     * 资质审核通过
     */
    SUCCESSAUDIT(2,"资质审核通过"),


    /**
     * 资质审核不通过
     */
    FAILAUDIT(3,"资质审核不通过"),
    /**
     * 财务审核中
     */
    CHECKING(4,"财务审核中"),
    /**
     * 财务审核成功
     */
    SUCCESSCHECK(5,"财务审核成功"),
    /**
     * 财务审核失败
     */
    FAILCHECK(6,"财务审核失败"),
    /**
     * 待交保证金
     */
    NOTPAYBAIL(7,"待交保证金"),

    /**
     * 入驻成功
     */
    SUCCESSJOIN(8,"入驻成功");

    /**
     * 9待签约
     */
//    SIGNING(9, "待签约");


    public final Integer code;
    public final String mes;

    CompanyAuditStatus(Integer code , String mes){
        this.code = code;
        this.mes = mes;
    }
    public String stringVal(){
        return code.toString();
    }
    
    public static String getDesc(Integer value) {  
    	CompanyAuditStatus[] businessModeEnums = values();  
        for (CompanyAuditStatus businessModeEnum : businessModeEnums) {  
            if (businessModeEnum.code == value) {  
                return businessModeEnum.mes;  
            }  
        }  
        return null;  
    }

    public static List<Map<String, String>> map(){
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        CompanyAuditStatus[] businessModeEnums = values();
        for(CompanyAuditStatus businessModeEnum : businessModeEnums){
            Map<String, String> map = new HashMap<>();
            map.put("id",businessModeEnum.code.toString());
            map.put("name", businessModeEnum.mes);
            list.add(map);
        }
        return list;
    }

}
