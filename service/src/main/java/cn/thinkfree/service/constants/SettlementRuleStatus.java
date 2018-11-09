package cn.thinkfree.service.constants;

/**
 * 结算规则结算周期状态
 */
public enum SettlementRuleStatus {


    /**
     *     结算周期类型 0 自然月最后一天结算1每个自然月2次月3每周 4每多少天
     */

    LastDaySettlement(0,"自然月最后一天结算"),

    NaturalMonthSettlement(1,"每个自然月周期结算"),

    NextMonthSettlement(2,"次月周期结算"),

    WeekSettlement(3,"周结算"),

    DaySettlement(4,"日结算");

    public final Integer code;
    public final String mes;

    SettlementRuleStatus(Integer code , String mes){
        this.code = code;
        this.mes = mes;
    }
    public String getCode(){
         return code.toString();
    }

    /**
     * 字典翻译
     * @param value
     * @return
     */
    public static String getDesc(String value) {  
    	SettlementRuleStatus[] businessModeEnums = values();
        for (SettlementRuleStatus businessModeEnum : businessModeEnums) {
            if ((businessModeEnum.code+"").equals(value)) {  
                return businessModeEnum.mes;  
            }  
        }  
        return null;  
    }  
}
