package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.PreProjectGuide;
import cn.thinkfree.database.model.PreProjectInfo;
import cn.thinkfree.database.model.PreProjectUserRole;
import io.swagger.annotations.ApiModel;

import java.util.List;
@ApiModel
public class ProjectDetailsVO extends PreProjectGuide {

    /**
     * 项目基本信息
     */
    private PreProjectInfo info;

    /**
     * 项目员工信息
     */
    private List<PreProjectUserRole> staffs;

    public PreProjectInfo getInfo() {
        return info;
    }

    public void setInfo(PreProjectInfo info) {
        this.info = info;
    }

    public List<PreProjectUserRole> getStaffs() {
        return staffs;
    }

    public void setStaffs(List<PreProjectUserRole> staffs) {
        this.staffs = staffs;
    }
}
