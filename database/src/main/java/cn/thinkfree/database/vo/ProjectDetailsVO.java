package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@ApiModel(description = "项目详情")
public class ProjectDetailsVO extends PreProjectGuide {

    /**
     * 项目基本信息
     */
    @ApiModelProperty("项目基本信息")
    private PreProjectInfo info;

    /**
     * 公司信息
     */
    @ApiModelProperty("公司信息")
    private CompanyInfo companyInfo;


    /**
     * 项目员工信息
     */
    @ApiModelProperty("项目员工信息")
    private List<PreProjectUserRole> staffs;

    /**
     * 报价单
     */
    @ApiModelProperty("项目报价单")
    private ProjectQuotationVO projectQuotationVO;

    /**
     * 二维码文件
     */
    @ApiModelProperty("二维码文件")
    private MultipartFile QRCodeFile;

    /**
     * 项目缩略图
     */
    @ApiModelProperty("项目缩略图")
    private MultipartFile thumbnail;


    public MultipartFile getQRCodeFile() {
        return QRCodeFile;
    }

    public void setQRCodeFile(MultipartFile QRCodeFile) {
        this.QRCodeFile = QRCodeFile;
    }

    public MultipartFile getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(MultipartFile thumbnail) {
        this.thumbnail = thumbnail;
    }

    public ProjectQuotationVO getProjectQuotationVO() {
        return projectQuotationVO;
    }

    public void setProjectQuotationVO(ProjectQuotationVO projectQuotationVO) {
        this.projectQuotationVO = projectQuotationVO;
    }

    public CompanyInfo getCompanyInfo() {
        return companyInfo;
    }

    public void setCompanyInfo(CompanyInfo companyInfo) {
        this.companyInfo = companyInfo;
    }

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
