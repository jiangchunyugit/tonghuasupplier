package cn.thinkfree.database.vo;

import cn.thinkfree.core.model.BaseModel;
import cn.thinkfree.database.model.PreProjectMaterial;

import java.util.List;

public class PreProjectMaterialVO extends BaseModel {

    private String projectNo;

    private List<PreProjectMaterial> preProjectMaterials;

    public List<PreProjectMaterial> getPreProjectMaterials() {
        return preProjectMaterials;
    }

    public void setPreProjectMaterials(List<PreProjectMaterial> preProjectMaterials) {
        this.preProjectMaterials = preProjectMaterials;
    }

    public String getProjectNo() {
        return projectNo;
    }

    public void setProjectNo(String projectNo) {
        this.projectNo = projectNo;
    }
}
