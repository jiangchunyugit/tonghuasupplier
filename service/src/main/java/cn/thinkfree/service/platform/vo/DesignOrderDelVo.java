package cn.thinkfree.service.platform.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author xusonghui
 * 设计阶段，订单详情
 */
public class DesignOrderDelVo extends DesignerOrderVo {
    @ApiModelProperty("订单阶段")
    private int state;
    @ApiModelProperty("当前状态")
    private String stageName;
    @ApiModelProperty("状态提示语")
    private String reminder;
    @ApiModelProperty("小区名称")
    private String communityName;
    @ApiModelProperty("常住人口")
    private String peopleNo;
    @ApiModelProperty("量房时间")
    private String volumeRoomDate;
    @ApiModelProperty("合同名称")
    private String contractName;
    @ApiModelProperty("合同查看地址")
    private String contractUrl;

    public DesignOrderDelVo(DesignerOrderVo designerOrderVo, int state,
                            String stageName, String reminder, String communityName, String peopleNo, String volumeRoomDate, String contractName, String contractUrl) {
        super(designerOrderVo.getProjectNo(), designerOrderVo.getDesignOrderNo(), designerOrderVo.getOwnerName(), designerOrderVo.getOwnerPhone(), designerOrderVo.getAddress(),
                designerOrderVo.getOrderSource(), designerOrderVo.getCreateTime(), designerOrderVo.getStyleName(), designerOrderVo.getBudget(), designerOrderVo.getArea(),
                designerOrderVo.getCompanyName(), designerOrderVo.getCompanyState(), designerOrderVo.getDesignerName(), designerOrderVo.getOrderStateName(),
                designerOrderVo.getOptionUserName(), designerOrderVo.getOptionTime(), designerOrderVo.getOrderState(), designerOrderVo.getProjectMoney());
        this.state = state;
        this.stageName = stageName;
        this.reminder = reminder;
        this.communityName = communityName;
        this.peopleNo = peopleNo;
        this.volumeRoomDate = volumeRoomDate;
        this.contractName = contractName;
        this.contractUrl = contractUrl;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public String getReminder() {
        return reminder;
    }

    public void setReminder(String reminder) {
        this.reminder = reminder;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getPeopleNo() {
        return peopleNo;
    }

    public void setPeopleNo(String peopleNo) {
        this.peopleNo = peopleNo;
    }

    public String getVolumeRoomDate() {
        return volumeRoomDate;
    }

    public void setVolumeRoomDate(String volumeRoomDate) {
        this.volumeRoomDate = volumeRoomDate;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getContractUrl() {
        return contractUrl;
    }

    public void setContractUrl(String contractUrl) {
        this.contractUrl = contractUrl;
    }
}
