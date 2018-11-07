package cn.thinkfree.database.vo.remote;

import cn.thinkfree.core.model.BaseModel;

import java.util.Date;

/**
 * 同步合同信息
 */
public class SyncContractVO extends BaseModel {


    /**
     * 所属城市
     * 汉字
     */
    private String city;

    /**
     * 甲方信息（门店HR编号）
     */
    private String fddm;
    private String fkkhh;
    private int fkkhhbh;
    private String fkyhhm;
    private String fkyhzh;
    private String ghdwdm;
    private String ghdwmc;
    private int gsdm;
    private String hth;
    private String hth_OLD;
    private Date htyxq_END;
    private Date htyxq_START;
    private String jrdzzh;
    private String province;
    private String status;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getFddm() {
        return fddm;
    }

    public void setFddm(String fddm) {
        this.fddm = fddm;
    }

    public String getFkkhh() {
        return fkkhh;
    }

    public void setFkkhh(String fkkhh) {
        this.fkkhh = fkkhh;
    }

    public int getFkkhhbh() {
        return fkkhhbh;
    }

    public void setFkkhhbh(int fkkhhbh) {
        this.fkkhhbh = fkkhhbh;
    }

    public String getFkyhhm() {
        return fkyhhm;
    }

    public void setFkyhhm(String fkyhhm) {
        this.fkyhhm = fkyhhm;
    }

    public String getFkyhzh() {
        return fkyhzh;
    }

    public void setFkyhzh(String fkyhzh) {
        this.fkyhzh = fkyhzh;
    }

    public String getGhdwdm() {
        return ghdwdm;
    }

    public void setGhdwdm(String ghdwdm) {
        this.ghdwdm = ghdwdm;
    }

    public String getGhdwmc() {
        return ghdwmc;
    }

    public void setGhdwmc(String ghdwmc) {
        this.ghdwmc = ghdwmc;
    }

    public int getGsdm() {
        return gsdm;
    }

    public void setGsdm(int gsdm) {
        this.gsdm = gsdm;
    }

    public String getHth() {
        return hth;
    }

    public void setHth(String hth) {
        this.hth = hth;
    }

    public String getHth_OLD() {
        return hth_OLD;
    }

    public void setHth_OLD(String hth_OLD) {
        this.hth_OLD = hth_OLD;
    }

    public Date getHtyxq_END() {
        return htyxq_END;
    }

    public void setHtyxq_END(Date htyxq_END) {
        this.htyxq_END = htyxq_END;
    }

    public Date getHtyxq_START() {
        return htyxq_START;
    }

    public void setHtyxq_START(Date htyxq_START) {
        this.htyxq_START = htyxq_START;
    }

    public String getJrdzzh() {
        return jrdzzh;
    }

    public void setJrdzzh(String jrdzzh) {
        this.jrdzzh = jrdzzh;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
