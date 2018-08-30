package cn.thinkfree.service.designer.vo;

import cn.thinkfree.core.model.BaseModel;

import java.util.List;

public class HomeStylerVO extends BaseModel {

    /**
     * 项目编号
     */
    private String projectNo;

    /**
     * 房屋集
     */
    private List<SpaceDetailsBean> spaceDetailsBeans;

    public String getProjectNo() {
        return projectNo;
    }

    public void setProjectNo(String projectNo) {
        this.projectNo = projectNo;
    }

    public List<SpaceDetailsBean> getSpaceDetailsBeans() {
        return spaceDetailsBeans;
    }

    public void setSpaceDetailsBeans(List<SpaceDetailsBean> spaceDetailsBeans) {
        this.spaceDetailsBeans = spaceDetailsBeans;
    }
}
