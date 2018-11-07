package cn.thinkfree.database.vo;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import cn.thinkfree.database.model.CompanyUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
/**
 * 添加入驻公司信息
 * @author lvqidong
 *
 */
@Api(value = "公司员工信息")
public class CompanyUserVo {

    @ApiModelProperty("公司员工信息")
    private CompanyUser companyUser;
    
    @ApiModelProperty("公司员工信息")
    private List<String> roleList;

    @ApiModelProperty("用户头像")
    private MultipartFile photoUrl;

	

	public List<String> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<String> roleList) {
		this.roleList = roleList;
	}

	public MultipartFile getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(MultipartFile photoUrl) {
		this.photoUrl = photoUrl;
	}

	public CompanyUser getCompanyUser() {
		return companyUser;
	}

	public void setCompanyUser(CompanyUser companyUser) {
		this.companyUser = companyUser;
	}

	

   
}
