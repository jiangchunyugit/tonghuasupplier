package cn.tonghua.core.security.filter.util;

import javax.servlet.http.HttpServletRequest;
import java.util.StringTokenizer;

/**
 * HttpServletRequest工具类
 * 
 */
 public class SecurityRequestUtil {
	/**
	 * 取得用户的ip地址
	 * 
	 * @param request
	 * @return ip地址，没得到是 null
	 */
	public static String getRequestIp(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("x-client-ip");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}

		// 多级反向代理
		if (null != ip && !"".equals(ip.trim())) {
			StringTokenizer st = new StringTokenizer(ip, ",");
			if (st.countTokens() > 1) {
				return st.nextToken();
			}
		}
		return ip;

	}
	/**
	 * 是否AJAX请求
	 * @param request
	 * @return
	 */
	public static Boolean isAjax(HttpServletRequest request){
		String isAjax = request.getHeader("X-Requested-With");
		return "XMLHttpRequest".equals(isAjax) ? true : false;
	}
}
