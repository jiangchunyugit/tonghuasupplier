package cn.thinkfree.core.security.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class User  implements Serializable, UserDetails {

	 
	// Fields

		private String pkUser;
		private String username;
		private String password;
		private String phone;
		private String name;
		private Short domain;
		private String hospital;
		private String department;
		private String title;
		private Short usertype;
		private String image;
		private String certificate;
		private String registerdate;
		private String lastlogindate;
		private String comment;
		private String pk_other;
		private String othertype;
		private String dr;
		private Short review;
		private String registertype;
		

		private List<? extends GrantedAuthority> roles;

		// Constructors



		/** default constructor */
		public User() {
		}

		/** minimal constructor */
		public User(String pkUser) {
			this.pkUser = pkUser;
		}

		/** full constructor */
		public User(String pkUser, String username, String password, String phone,
				String name, Short domain, String hospital, String department,
				String title, Short usertype, String image, String certificate,
				String registerdate, String lastlogindate, String comment,
				String pk_other,String othertype,String dr,Short review,
				String registertype) {
			this.pkUser = pkUser;
			this.username = username;
			this.password = password;
			this.phone = phone;
			this.name = name;
			this.domain = domain;
			this.hospital = hospital;
			this.department = department;
			this.title = title;
			this.usertype = usertype;
			this.image = image;
			this.certificate = certificate;
			this.registerdate = registerdate;
			this.lastlogindate = lastlogindate;
			this.comment = comment;
			this.pk_other = pk_other;
			this.othertype = othertype;
			this.dr = dr;
			this.review = review;
			this.registertype = registertype;
		}

		// Property accessors
		public String getPkUser() {
			return this.pkUser;
		}

		public void setPkUser(String pkUser) {
			this.pkUser = pkUser;
		}

		public String getUsername() {
			return this.username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPassword() {
			return this.password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getPhone() {
			return this.phone;
		}

		public void setPhone(String phone) {
			this.phone = phone;
		}

		public String getName() {
			return this.name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Short getDomain() {
			return this.domain;
		}

		public void setDomain(Short domain) {
			this.domain = domain;
		}

		public String getHospital() {
			return this.hospital;
		}

		public void setHospital(String hospital) {
			this.hospital = hospital;
		}

		public String getDepartment() {
			return this.department;
		}

		public void setDepartment(String department) {
			this.department = department;
		}

		public String getTitle() {
			return this.title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public Short getUsertype() {
			return this.usertype;
		}

		public void setUsertype(Short usertype) {
			this.usertype = usertype;
		}

		public String getImage() {
			return this.image;
		}

		public void setImage(String image) {
			this.image = image;
		}

		public String getCertificate() {
			return this.certificate;
		}

		public void setCertificate(String certificate) {
			this.certificate = certificate;
		}

		public String getRegisterdate() {
			return this.registerdate;
		}

		public void setRegisterdate(String registerdate) {
			this.registerdate = registerdate;
		}

		public String getLastlogindate() {
			return this.lastlogindate;
		}

		public void setLastlogindate(String lastlogindate) {
			this.lastlogindate = lastlogindate;
		}

		public String getComment() {
			return this.comment;
		}
		public void setComment(String comment) {
 			this.comment = comment;
		}
		
		public String getPk_other() {
			return this.pk_other;
		}

		public void setPk_other(String pk_other) {
			this.pk_other = pk_other;
		}
		
		public String getOthertype() {
			return this.othertype;
		}

		public void setOthertype(String othertype) {
			this.othertype = othertype;
		}

		public String getDr() {
			return this.dr;
		}

		public void setDr(String dr) {
			this.dr = dr;
		}

		public Short getReview() {
			return review;
		}

		public void setReview(Short review) {
			this.review = review;
		}

		public String getRegistertype() {
			return registertype;
		}

		public void setRegistertype(String registertype) {
			this.registertype = registertype;
		}

	public Collection<? extends GrantedAuthority> getAuthorities() {
//		List tmpRoles=new ArrayList();
//		GrantedAuthority tmpAuthority=new SimpleGrantedAuthority("ROLE_USER");
 //			tmpRoles.add(tmpAuthority);
//		for(GrantedAuthority r : roles){
//			tmpRoles.add(r);
//		}
		 
		return roles;
 	}
 

	public boolean isAccountNonExpired() {
		return true;
	}

	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isEnabled() {
 		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String toString() {
		return "User [pkUser=" + pkUser + ", username=" + username
				+ ", password=" + password + ", phone=" + phone + ", name="
				+ name + ", domain=" + domain + ", hospital=" + hospital
				+ ", department=" + department + ", title=" + title
				+ ", usertype=" + usertype + ", image=" + image
				+ ", certificate=" + certificate + ", registerdate="
				+ registerdate + ", lastlogindate=" + lastlogindate
				+ ", comment=" + comment + ", pk_other=" + pk_other
				+ ", othertype=" + othertype +", dr=" + dr 
				+ ", review=" + review +"]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((certificate == null) ? 0 : certificate.hashCode());
		result = prime * result + ((comment == null) ? 0 : comment.hashCode());
		result = prime * result
				+ ((department == null) ? 0 : department.hashCode());
		result = prime * result + ((domain == null) ? 0 : domain.hashCode());
		result = prime * result
				+ ((hospital == null) ? 0 : hospital.hashCode());
		result = prime * result + ((image == null) ? 0 : image.hashCode());
		result = prime * result
				+ ((lastlogindate == null) ? 0 : lastlogindate.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((othertype == null) ? 0 : othertype.hashCode());
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((phone == null) ? 0 : phone.hashCode());
		result = prime * result + ((pkUser == null) ? 0 : pkUser.hashCode());
		result = prime * result
				+ ((pk_other == null) ? 0 : pk_other.hashCode());
		result = prime * result
				+ ((registerdate == null) ? 0 : registerdate.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
		result = prime * result
				+ ((usertype == null) ? 0 : usertype.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
 		if (this == obj) return true;
		if (getClass() != obj.getClass()) return false;
		User other = (User) obj;
		if(other.getPkUser() == this.getPkUser()){
			return true;
		}
 
		return false;
 	}

	 
	
	 private void writeObject(final ObjectOutputStream out) throws IOException  
	   {  
		 	out.writeUTF(this.pkUser);  
		 	out.writeUTF(this.name);
//		 	out.writeUTF(this.username);
		 	out.writeUTF(this.hospital);
		 	out.writeUTF(this.department);
		 	out.writeUTF(this.title);
		 	out.writeUTF(this.image);
		 	out.writeUTF(this.certificate);
		 	out.writeUTF(this.comment);
	   }  
 	    
	 
	   private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException  
	   {  
		   this.pkUser=in.readUTF();
		   this.name=in.readUTF();
		   this.hospital=in.readUTF();
		   this.department=in.readUTF();
		   this.title =in.readUTF();
		   this.image = in.readUTF();
		   this.certificate = in.readUTF();
		   this.comment = in.readUTF();
	   }

	public List<? extends GrantedAuthority> getRoles() {
		return roles;
	}

	public void setRoles(List<? extends GrantedAuthority> roles) {
		this.roles = roles;
	}  
   
}
