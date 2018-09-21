package cn.thinkfree.database.vo;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author ying007
 * 公司资质上传文件信息
 */
public class CompanySubmitFileVo {
    /**
     * 营业执照
     */
    private MultipartFile businessPhotoUrl;

    /**
     * 企业税务登记证
     */
    private MultipartFile taxCodePhotoUrl;

    /**
     * 装修施工资质证书
     */
    private MultipartFile workPhotoUrl;

    /**
     * 法人身份证正面
     */
    private MultipartFile lefalCardUpUrl;

    /**
     * 法人身份证背面
     */
    private MultipartFile lefalCardDownUrl;

    /**
     * 开户许可证
     */
    private MultipartFile licenseUrl;

    public MultipartFile getBusinessPhotoUrl() {
        return businessPhotoUrl;
    }

    public void setBusinessPhotoUrl(MultipartFile businessPhotoUrl) {
        this.businessPhotoUrl = businessPhotoUrl;
    }

    public MultipartFile getTaxCodePhotoUrl() {
        return taxCodePhotoUrl;
    }

    public void setTaxCodePhotoUrl(MultipartFile taxCodePhotoUrl) {
        this.taxCodePhotoUrl = taxCodePhotoUrl;
    }

    public MultipartFile getWorkPhotoUrl() {
        return workPhotoUrl;
    }

    public void setWorkPhotoUrl(MultipartFile workPhotoUrl) {
        this.workPhotoUrl = workPhotoUrl;
    }

    public MultipartFile getLefalCardUpUrl() {
        return lefalCardUpUrl;
    }

    public void setLefalCardUpUrl(MultipartFile lefalCardUpUrl) {
        this.lefalCardUpUrl = lefalCardUpUrl;
    }

    public MultipartFile getLefalCardDownUrl() {
        return lefalCardDownUrl;
    }

    public void setLefalCardDownUrl(MultipartFile lefalCardDownUrl) {
        this.lefalCardDownUrl = lefalCardDownUrl;
    }

    public MultipartFile getLicenseUrl() {
        return licenseUrl;
    }

    public void setLicenseUrl(MultipartFile licenseUrl) {
        this.licenseUrl = licenseUrl;
    }

    @Override
    public String toString() {
        return "CompanySubmitFileVo{" +
                "businessPhotoUrl=" + businessPhotoUrl +
                ", taxCodePhotoUrl=" + taxCodePhotoUrl +
                ", workPhotoUrl=" + workPhotoUrl +
                ", lefalCardUpUrl=" + lefalCardUpUrl +
                ", lefalCardDownUrl=" + lefalCardDownUrl +
                ", licenseUrl=" + licenseUrl +
                '}';
    }
}
