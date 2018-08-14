package cn.thinkfree.core.security.dao;

 
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

 
//@Component("securityUserDao")
public class UserAuthityDao   implements UserDetailsService {

	 
	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
//		User user = (User) getEntityManager().createQuery("From User where (username = :name or pk_other = :name) and dr = 0   ")
//   				.setParameter("name", username).getSingleResult();
//		user.setLastlogindate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
//		user.setRoles(findRoleByPkUser(user.getPkUser()));
		return null;
	}


}
