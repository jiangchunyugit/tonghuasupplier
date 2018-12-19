package cn.thinkfree.service.platform.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author xusonghui
 * 项目概述信息
 */
public class ProjectSummaryMsgVo {

    @ApiModelProperty("项目编号")
    private String projectNo;
    @ApiModelProperty("项目地址")
    private String address;
    @ApiModelProperty("项目创建时间")
    private long createTime;

    public String getProjectNo() {
        return projectNo;
    }

    public void setProjectNo(String projectNo) {
        this.projectNo = projectNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}
