package cn.thinkfree.database.constants;

/**
 * 首页项目折线图单位
 */
public enum  IndexChartUnit {

    Week(1,"周"),
    Month(2,"月"),
    Range(3,"区间");

    public final Integer code;
    public final String desc;
    IndexChartUnit(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
