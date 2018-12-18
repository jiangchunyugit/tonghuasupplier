package cn.thinkfree.core.security.filter.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * 登陆用户信息工具类
 * 
 */
@Component
public class SessionUserDetailsUtil {





	/**
	 * 得到当前session中的用户，如果没有返回null
	 *
	 * 
	 * @return UserDetails
	 */
	public static UserDetails getUserDetails() {
		UserDetails userDetails = null;
		SecurityContext sc = SecurityContextHolder.getContext();
		Authentication ac = sc.getAuthentication();
		if (ac != null && !"anonymousUser".equals( ac.getPrincipal())) {
			userDetails = (UserDetails) ac.getPrincipal();
		}

		return userDetails;
	}

	/**
	 * 得到当前登录用户，如果没有返回null
	 * 
	 * @return loginId
	 */
	public static String getLoginUserName() {
		String loginId = null;
		UserDetails userDetails = getUserDetails();
		if (userDetails != null) {
			loginId = userDetails.getUsername();
		}
		return loginId;
	}

//	/**
//	 * 获取登录用户主键
//	 * @return
//	 */
//	public static Integer getLoginUserID(){
//		User userDetails = (User) getUserDetails();
//		return userDetails == null ? null:userDetails.getId();
//	}

	/**
	 * 判断用户是否登陆
	 *
	 */
	public static boolean isLogined() {
		boolean flag = false;
		if(getLoginUserName() != null) flag = true;
		return flag;
	}



}
