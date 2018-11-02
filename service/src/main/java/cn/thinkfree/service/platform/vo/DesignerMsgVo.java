package cn.thinkfree.service.platform.vo;

import cn.thinkfree.database.model.DesignerMsg;
import cn.thinkfree.database.model.DesignerStyleConfig;
import cn.thinkfree.database.model.EmployeeMsg;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author xusonghui
 * 设计师信息
 */
public class DesignerMsgVo {

    @ApiModelProperty("真实姓名")
    private String realName;
    @ApiModelProperty("性别，1男，2女")
    private int sex;
    @ApiModelProperty("出生年月")
    private String birthday;
    @ApiModelProperty("用户名")
    private String userName;
    @ApiModelProperty("所在地")
    private String address;
    @ApiModelProperty("手机号")
    private String phone;
    @ApiModelProperty("邮箱")
    private String email;
    @ApiModelProperty("证件号")
    private String certificate;
    @ApiModelProperty("证件正面")
    private String certificateUrl1;
    @ApiModelProperty("证件反面")
    private String certificateUrl2;
    @ApiModelProperty("手持证件照")
    private String certificateUrl3;
    @ApiModelProperty("实名认证状态，1未认证，2已认证")
    private int authState;
    @ApiModelProperty("来源，1无来源，2用户注册，3后台创建")
    private int source;
    @ApiModelProperty("注册时间")
    private String registerTime;
    @ApiModelProperty("设计师标签")
    private String designTag;
    @ApiModelProperty("设计师等级")
    private int level;
    @ApiModelProperty("设计师身份,1社会化设计师")
    private String identity;
    @ApiModelProperty("公司名称")
    private String companyName;
    @ApiModelProperty("从业年限")
    private int workingTime;
    @ApiModelProperty("量房费")
    private String volumeRoomMoney;
    @ApiModelProperty("设计费最低")
    private String designerMoneyLow;
    @ApiModelProperty("设计费最高")
    private String designerMoneyHigh;
    @ApiModelProperty("擅长风格")
    private List<String> designerStyles;



    @ApiModelProperty("设计师信息")
    private DesignerMsg designerMsg;
    @ApiModelProperty("设计师擅长风格")
    private List<DesignerStyleConfig> designerStyleConfigs;
    @ApiModelProperty("员工信息")
    private EmployeeMsg employeeMsg;

    public DesignerMsg getDesignerMsg() {
        return designerMsg;
    }

    public void setDesignerMsg(DesignerMsg designerMsg) {
        this.designerMsg = designerMsg;
    }

    public List<DesignerStyleConfig> getDesignerStyleConfigs() {
        return designerStyleConfigs;
    }

    public void setDesignerStyleConfigs(List<DesignerStyleConfig> designerStyleConfigs) {
        this.designerStyleConfigs = designerStyleConfigs;
    }

    public void setEmployeeMsg(EmployeeMsg employeeMsg) {
        this.employeeMsg = employeeMsg;
    }

    public EmployeeMsg getEmployeeMsg() {
        return employeeMsg;
    }
}
