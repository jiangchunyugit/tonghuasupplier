package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.PreProjectInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
@ApiModel("项目报价单")
public class ProjectQuotationVO extends PreProjectInfo {
    @ApiModelProperty("报价单条目")
    private List<ProjectQuotationItemVO> items;

    public List<ProjectQuotationItemVO> getItems() {
        return items;
    }

    public void setItems(List<ProjectQuotationItemVO> items) {
        this.items = items;
    }
}
