package cn.thinkfree.database.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Range;

import java.util.Date;

@ApiModel(description = "公告信息表")
public class SystemMessage {

    @ApiModelProperty("主键")
    private Integer id;

    @ApiModelProperty("公告内容")
    private String content;

    @ApiModelProperty("发送时间")
    private Date sendTime;

    @ApiModelProperty("标题")
    @Range(min = 2, max = 20, message = "标题输入2-20个字(数字、字母、汉字、符号)")
    private String title;

    @ApiModelProperty("发送对象：管家，工长。。")
    private String receiveRole;

    @ApiModelProperty("发送账户")
    private String sendUser;

    @ApiModelProperty("发送账户")
    private String sendUserId;

    @ApiModelProperty("发送账户所属公司")
    private String companyId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getReceiveRole() {
        return receiveRole;
    }

    public void setReceiveRole(String receiveRole) {
        this.receiveRole = receiveRole == null ? null : receiveRole.trim();
    }

    public String getSendUser() {
        return sendUser;
    }

    public void setSendUser(String sendUser) {
        this.sendUser = sendUser == null ? null : sendUser.trim();
    }

    public String getSendUserId() {
        return sendUserId;
    }

    public void setSendUserId(String sendUserId) {
        this.sendUserId = sendUserId == null ? null : sendUserId.trim();
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId == null ? null : companyId.trim();
    }
}