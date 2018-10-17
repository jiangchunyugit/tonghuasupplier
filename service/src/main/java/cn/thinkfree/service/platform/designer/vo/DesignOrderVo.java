package cn.thinkfree.service.platform.designer.vo;

import cn.thinkfree.database.model.ConstructionOrder;
import cn.thinkfree.database.model.DesignOrder;
import cn.thinkfree.database.model.Project;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author xusonghui
 * 设计订单列表
 */
public class DesignOrderVo {
    @ApiModelProperty("设计订单")
    private DesignOrder designOrder;
    @ApiModelProperty("项目信息")
    private Project project;
    @ApiModelProperty("施工订单")
    private ConstructionOrder constructionOrder;

    public DesignOrder getDesignOrder() {
        return designOrder;
    }

    public void setDesignOrder(DesignOrder designOrder) {
        this.designOrder = designOrder;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public ConstructionOrder getConstructionOrder() {
        return constructionOrder;
    }

    public void setConstructionOrder(ConstructionOrder constructionOrder) {
        this.constructionOrder = constructionOrder;
    }
}
