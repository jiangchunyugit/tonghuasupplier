package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.PcApplyInfo;

/**
 * @author ying007
 * 申请记录完全字段
 */
public class PcApplyInfoVo extends PcApplyInfo {
    /**
     * 公司类型：装饰，设计。。。。
     */
    private String roleName;

    private String provinceName;

    private String cityName;

    private String areaName;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    @Override
    public String toString() {
        return "PcApplyInfoVo{" +
                "roleName='" + roleName + '\'' +
                ", provinceName='" + provinceName + '\'' +
                ", cityName='" + cityName + '\'' +
                ", areaName='" + areaName + '\'' +
                '}';
    }
}
