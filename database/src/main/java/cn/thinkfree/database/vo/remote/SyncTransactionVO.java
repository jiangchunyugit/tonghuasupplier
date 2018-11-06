package cn.thinkfree.database.vo.remote;

import cn.thinkfree.core.model.BaseModel;

/**
 * 推送消息 交易主体
 */
public class SyncTransactionVO extends BaseModel {


    /**
     * 完整详细的联系地址
     */
    private String address;
    /**
     * 交易方地点编码
     * 公司编号
     */
    private String code;
    /**
     * 公司（业务实体）
     * 经营主体
     */
    private int cwgsdm;
    /**
     * 税务登记证上编号
     */
    private String gssh;
    /**
     * 交易方地点名称（交易方简称）
     */
    private String jc;
    /**
     * 交易方注册名称（营业执照上的名称）
     */
    private String name;
    /**
     * 交易方编码
     */
    private String vendorCode;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getCwgsdm() {
        return cwgsdm;
    }

    public void setCwgsdm(int cwgsdm) {
        this.cwgsdm = cwgsdm;
    }

    public String getGssh() {
        return gssh;
    }

    public void setGssh(String gssh) {
        this.gssh = gssh;
    }

    public String getJc() {
        return jc;
    }

    public void setJc(String jc) {
        this.jc = jc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVendorCode() {
        return vendorCode;
    }

    public void setVendorCode(String vendorCode) {
        this.vendorCode = vendorCode;
    }
}
