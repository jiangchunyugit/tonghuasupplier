package cn.thinkfree.core.security.model;

import java.io.Serializable;


import org.springframework.security.core.GrantedAuthority;



public class Role implements Serializable,GrantedAuthority{

	
	private String pkRole;
	private String roleName;
	private String roleDesc;
	private String createTime;
	private String createUser;
	

	public String getPkRole() {
 		return pkRole;
	}
	public void setPkRole(String pkRole) {
		this.pkRole = pkRole;
	}

	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String pkName) {
		this.roleName = pkName;
	}
	

	public String getRoleDesc() {
		return roleDesc;
	}
	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}

	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getAuthority() {
		return this.roleName;
 	}
	
	public Role() {
		 
	}
	public Role(String pkRole, String pkName) {
		super();
		this.pkRole = pkRole;
		this.roleName = pkName;
	}
	public Role(String pkRole, String roleName, String roleDesc,
			String createTime, String createUser) {
		super();
		this.pkRole = pkRole;
		this.roleName = roleName;
		this.roleDesc = roleDesc;
		this.createTime = createTime;
		this.createUser = createUser;
	}
	
	
}
