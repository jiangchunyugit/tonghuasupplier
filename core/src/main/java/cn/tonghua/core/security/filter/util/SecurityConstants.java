package cn.tonghua.core.security.filter.util;

/**
 * SecurityConstants
 * 
 */
 public class SecurityConstants {

	/**
	 * 登陆链接
	 * j_spring_security_check
	 */
	public static final String LOGIN_URL = "/login";
	/**
	 * 登录页面
	 */
	public static final String LOGIN_PAGE = "/loginPage";
	/**
	 * 鉴权成功默认页面
	 */
	public static final String LOGIN_SUCCESS_PAGE = "/index";
	/**
	 * 登录传递用户名title
	 */
	public static final String LOGIN_USERNAME = "username";
	/**
	 * 登录传递密码title
	 */
	public static final String LOGIN_PASSWORD = "password";

	/**
	 * 登出链接
	 */
	public static final String LOGIN_OUT = "/logout";

	/**
	 * 记住我 cookie值
	 */
	public static final String REMEMBERME_KEY = "webmvc#FD637E6D9C0F1A5A67082AF56CE32485";

	/**
	 * 未授权返回URL
	 */
	public static final String BAN_URL = "/error/403.jsp";
	
	/**
	 * 请求不接受返回URL
	 */
	public static final String NOT_ACCEPTABLE = "/error/406.jsp";

	/**
	 * 操作错误
	 */
	public static final String ERROR_PAGE = "/error/500.jsp";

	/**
	 * 混淆
	 */
	public static final String SALT = "username";
}
