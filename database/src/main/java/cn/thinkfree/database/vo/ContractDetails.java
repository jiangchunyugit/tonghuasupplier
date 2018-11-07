package cn.thinkfree.database.vo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.thinkfree.database.model.PcAuditInfo;

public class ContractDetails  {
	
	
	private String signedTime;//合同签订时间
	
	private String startEime;//合同开始时间
	
	private String endEime;//合同开始时间
	
	private String auditName;//当前审批状态
	
	private String companyLocation;
	
	private String auditStatus;
	
	private String companyName;
	
	private String companyId;
	
	private String legalName;
	
	private String legalIdCard;

    
	
	public String getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getLegalName() {
		return legalName;
	}

	public void setLegalName(String legalName) {
		this.legalName = legalName;
	}

	public String getLegalIdCard() {
		return legalIdCard;
	}

	public void setLegalIdCard(String legalIdCard) {
		this.legalIdCard = legalIdCard;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLefalCardUpUrl() {
		return lefalCardUpUrl;
	}

	public void setLefalCardUpUrl(String lefalCardUpUrl) {
		this.lefalCardUpUrl = lefalCardUpUrl;
	}

	public String getLefalCardDownUrl() {
		return lefalCardDownUrl;
	}

	public void setLefalCardDownUrl(String lefalCardDownUrl) {
		this.lefalCardDownUrl = lefalCardDownUrl;
	}

	public String getCompanyType() {
		return companyType;
	}

	public void setCompanyType(String companyType) {
		this.companyType = companyType;
	}

	public String getComapnyNature() {
		return comapnyNature;
	}

	public void setComapnyNature(String comapnyNature) {
		this.comapnyNature = comapnyNature;
	}

	public String getIsThreeToOne() {
		return isThreeToOne;
	}

	public void setIsThreeToOne(String isThreeToOne) {
		this.isThreeToOne = isThreeToOne;
	}

	public String getBusinessCode() {
		return businessCode;
	}

	public void setBusinessCode(String businessCode) {
		this.businessCode = businessCode;
	}

	public String getLegalPhone() {
		return legalPhone;
	}

	public void setLegalPhone(String legalPhone) {
		this.legalPhone = legalPhone;
	}

	public String getOrganizationCode() {
		return organizationCode;
	}

	public void setOrganizationCode(String organizationCode) {
		this.organizationCode = organizationCode;
	}

	public String getIsTaxpayer() {
		return isTaxpayer;
	}

	public void setIsTaxpayer(String isTaxpayer) {
		this.isTaxpayer = isTaxpayer;
	}

	public String getTaxCodePhotoUrl() {
		return taxCodePhotoUrl;
	}

	public void setTaxCodePhotoUrl(String taxCodePhotoUrl) {
		this.taxCodePhotoUrl = taxCodePhotoUrl;
	}

	public String getBusinessPhotoUrl() {
		return businessPhotoUrl;
	}

	public void setBusinessPhotoUrl(String businessPhotoUrl) {
		this.businessPhotoUrl = businessPhotoUrl;
	}

	public String getWorkCardFrom() {
		return workCardFrom;
	}

	public void setWorkCardFrom(String workCardFrom) {
		this.workCardFrom = workCardFrom;
	}

	public String getWorkCardIndate() {
		return workCardIndate;
	}

	public void setWorkCardIndate(String workCardIndate) {
		this.workCardIndate = workCardIndate;
	}

	public String getWorkPhotoUrl() {
		return workPhotoUrl;
	}

	public void setWorkPhotoUrl(String workPhotoUrl) {
		this.workPhotoUrl = workPhotoUrl;
	}

	public String getWorkCode() {
		return workCode;
	}

	public void setWorkCode(String workCode) {
		this.workCode = workCode;
	}

	private String postcode;
	
	private String email;
	
	private String lefalCardUpUrl;
	
	private String lefalCardDownUrl;
	
	private String companyType;
	
	private String comapnyNature;
	
	private String isThreeToOne;
	
	private String businessCode;
	
	private String legalPhone;
	
	private String organizationCode;
	
	private String isTaxpayer;
	
	private String taxCodePhotoUrl;
	
	private String businessPhotoUrl;
	
	private String workCardFrom;
	
	private String workCardIndate;
	
	private String workPhotoUrl;
	
	private String workCode;
	
	
	
	private List<LinkedHashMap<String,String>> settlementInfo = new ArrayList<>();//结算信息
	
    private List<PcAuditInfo> auditInfo = new ArrayList<>();//审核信息

	public String getSignedTime() {
		return signedTime;
	}

	public void setSignedTime(String signedTime) {
		this.signedTime = signedTime;
	}

	public String getStartEime() {
		return startEime;
	}

	public void setStartEime(String startEime) {
		this.startEime = startEime;
	}

	public String getEndEime() {
		return endEime;
	}

	public void setEndEime(String endEime) {
		this.endEime = endEime;
	}
	
	

	public String getAuditName() {
		return auditName;
	}

	public void setAuditName(String auditName) {
		this.auditName = auditName;
	}

	public List<LinkedHashMap<String,String>> getSettlementInfo() {
		return settlementInfo;
	}

	public void setSettlementInfo(List<LinkedHashMap<String,String>> settlementInfo) {
		this.settlementInfo = settlementInfo;
	}

	public String getCompanyLocation() {
		return companyLocation;
	}

	public void setCompanyLocation(String companyLocation) {
		this.companyLocation = companyLocation;
	}

	public List<PcAuditInfo> getAuditInfo() {
		return auditInfo;
	}

	public void setAuditInfo(List<PcAuditInfo> auditInfo) {
		this.auditInfo = auditInfo;
	}





}
