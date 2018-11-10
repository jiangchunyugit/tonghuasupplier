package cn.thinkfree.database.vo.remote;

import cn.thinkfree.core.model.BaseModel;

/**
 * 同步订单
 */
public class SyncOrderVO extends BaseModel {
    /**
     * 实际支付费用：总费用扣除<积分优惠券抵扣>剩下的金额
     */
    private String actualAmount;
    /**
     * 公司id
     */
    private String companyId;
    /**
     * 公司名称
     */
    private String companyName;
    /**
     * 施工阶段(水电工程)
     */
    private String constructionStage;
    /**
     * 业主姓名
     */
    private String consumerName;
    /**
     * 合同类型：1全款，2分期
     */
    private String contractType;
    /**
     * 设计案例id
     */
    private String designId;
    /**
     * 设计师id
     */
    private String designUserId;
    /**
     * 合同结束时间
     */
    private String endTime;
    /**
     * 订单号：设计单号、施工单号、合同编号 等
     */
    private String fromOrderid;
    /**
     * 是否是合同尾款:是1,否2
     */
    private String isEnd;
    /**
     * 项目地址
     */
    private String projectAddr;
    /**
     * 项目编号
     */
    private String projectNo;
    /**
     * 签约时间
     */
    private String signedTime;
    /**
     * 合同开始时间
     */
    private String startTime;
    /**
     * 装修类型(个性化)
     */
    private String styleType;
    /**
     * 订单类型：设计1、施工2、合同3
     */
    private String type;
    /**
     * 费用类型：量房费1、入住保证金5、合同一期31,二期32,三期33、施工一期21,二期22,三期23
     */
    private String typeSub;
    /**
     * 业主id
     */
    private String userId;

    public SyncOrderVO() {
        this.actualAmount = "";
        this.companyId = "";
        this.companyName = "";
        this.constructionStage = "";
        this.consumerName = "";
        this.contractType = "";
        this.designId = "";
        this.designUserId = "";
        this.endTime = "";
        this.fromOrderid = "";
        this.isEnd = "";
        this.projectAddr = "";
        this.projectNo = "";
        this.signedTime = "";
        this.startTime = "";
        this.styleType = "";
        this.type = "";
        this.typeSub = "";
        this.userId = "";
    }

    public SyncOrderVO(String actualAmount, String companyId, String companyName, String constructionStage, String consumerName, String contractType, String designId, String designUserId, String endTime, String fromOrderid, String isEnd, String projectAddr, String projectNo, String signedTime, String startTime, String styleType, String type, String typeSub, String userId) {
        this.actualAmount = actualAmount;
        this.companyId = companyId;
        this.companyName = companyName;
        this.constructionStage = constructionStage;
        this.consumerName = consumerName;
        this.contractType = contractType;
        this.designId = designId;
        this.designUserId = designUserId;
        this.endTime = endTime;
        this.fromOrderid = fromOrderid;
        this.isEnd = isEnd;
        this.projectAddr = projectAddr;
        this.projectNo = projectNo;
        this.signedTime = signedTime;
        this.startTime = startTime;
        this.styleType = styleType;
        this.type = type;
        this.typeSub = typeSub;
        this.userId = userId;
    }

    public String getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(String actualAmount) {
        this.actualAmount = actualAmount;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getConstructionStage() {
        return constructionStage;
    }

    public void setConstructionStage(String constructionStage) {
        this.constructionStage = constructionStage;
    }

    public String getConsumerName() {
        return consumerName;
    }

    public void setConsumerName(String consumerName) {
        this.consumerName = consumerName;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public String getDesignId() {
        return designId;
    }

    public void setDesignId(String designId) {
        this.designId = designId;
    }

    public String getDesignUserId() {
        return designUserId;
    }

    public void setDesignUserId(String designUserId) {
        this.designUserId = designUserId;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getFromOrderid() {
        return fromOrderid;
    }

    public void setFromOrderid(String fromOrderid) {
        this.fromOrderid = fromOrderid;
    }

    public String getIsEnd() {
        return isEnd;
    }

    public void setIsEnd(String isEnd) {
        this.isEnd = isEnd;
    }

    public String getProjectAddr() {
        return projectAddr;
    }

    public void setProjectAddr(String projectAddr) {
        this.projectAddr = projectAddr;
    }

    public String getProjectNo() {
        return projectNo;
    }

    public void setProjectNo(String projectNo) {
        this.projectNo = projectNo;
    }

    public String getSignedTime() {
        return signedTime;
    }

    public void setSignedTime(String signedTime) {
        this.signedTime = signedTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStyleType() {
        return styleType;
    }

    public void setStyleType(String styleType) {
        this.styleType = styleType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeSub() {
        return typeSub;
    }

    public void setTypeSub(String typeSub) {
        this.typeSub = typeSub;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
