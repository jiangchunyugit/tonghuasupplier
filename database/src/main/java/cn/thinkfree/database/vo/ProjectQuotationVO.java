package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.PreProjectInfo;

import java.util.List;

public class ProjectQuotationVO extends PreProjectInfo {

    private List<ProjectQuotationItemVO> items;

    public List<ProjectQuotationItemVO> getItems() {
        return items;
    }

    public void setItems(List<ProjectQuotationItemVO> items) {
        this.items = items;
    }
}
