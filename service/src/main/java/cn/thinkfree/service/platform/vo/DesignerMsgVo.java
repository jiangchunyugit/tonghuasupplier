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
