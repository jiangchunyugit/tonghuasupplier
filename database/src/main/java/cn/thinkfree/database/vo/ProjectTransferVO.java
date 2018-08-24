package cn.thinkfree.database.vo;

import cn.thinkfree.core.model.BaseModel;
import cn.thinkfree.database.model.PreProjectUserRole;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class ProjectTransferVO extends BaseModel {

    /**
     * 项目编号
     */
    private String projectNo;
    /**
     * 项目缩略图
     */
    private MultipartFile thumbnail;
    /**
     * 项目员工信息
     */
    private List<ProjectUserRoleVO> staffs;


    public String getProjectNo() {
        return projectNo;
    }

    public void setProjectNo(String projectNo) {
        this.projectNo = projectNo;
    }

    public MultipartFile getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(MultipartFile thumbnail) {
        this.thumbnail = thumbnail;
    }

    public List<ProjectUserRoleVO> getStaffs() {
        return staffs;
    }

    public void setStaffs(List<ProjectUserRoleVO> staffs) {
        this.staffs = staffs;
    }
}
