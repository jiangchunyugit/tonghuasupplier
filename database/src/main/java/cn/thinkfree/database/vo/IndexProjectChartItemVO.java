package cn.thinkfree.database.vo;

/**
 * 首页项目统计图
 */
public class IndexProjectChartItemVO {

    private String dateLine;
    private String num;

    public String getDateLine() {
        return dateLine;
    }

    public void setDateLine(String dateLine) {
        this.dateLine = dateLine;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public IndexProjectChartItemVO() {
    }

    public IndexProjectChartItemVO(String dateLine, String num) {
        this.dateLine = dateLine;
        this.num = num;
    }
}
