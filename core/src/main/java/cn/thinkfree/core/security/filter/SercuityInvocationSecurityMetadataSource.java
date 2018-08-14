package cn.thinkfree.core.security.filter;

import cn.thinkfree.core.security.dao.SecurityResourceDao;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

import java.util.*;


/**
 * 资源加载器
 */
 public class SercuityInvocationSecurityMetadataSource
 implements FilterInvocationSecurityMetadataSource
 {
	
	private boolean rejectPublicInvocations = false;
	private static String prefix="ROLE_";

	 /**
	  * 数据库支持
	  */
	 SecurityResourceDao securityResourceDao;

	 /**
	  * 缓存所有资源
	  */
	private static Map<String, Integer> resources = new HashMap<String, Integer>();
	
	public SercuityInvocationSecurityMetadataSource(SecurityResourceDao securityResourceDao) {
		this.securityResourceDao = securityResourceDao;
		loadSecurityMetadataSource();
 	}

     /**
      * 匹配资源需要的权限
	  * @param object  资源
	  * @return
      * @throws IllegalArgumentException
      */
 	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
 
 		String url = ((FilterInvocation)object).getRequestUrl();
		if(resources.isEmpty()) return null;
 		Integer resourceId = resources.get(url);
		if(rejectPublicInvocations && resourceId == null) {
			throw new IllegalArgumentException("Secure object invocation " + object +
                    " was denied as public invocations are not allowed via this interceptor. ");
		}
		return getRolesByResouceId(resourceId);
	}

	private Collection<ConfigAttribute> getRolesByResouceId(Integer resourceId) {
		List<String>  roles = securityResourceDao.getRoleByResourceId(resourceId);
		
		Collection<ConfigAttribute> atts = null;
		if(roles != null) {
			atts = new ArrayList<ConfigAttribute>();
			for (String role : roles) {
				atts.add(new SecurityConfig(prefix+role));
			}
		}
		
		return atts;
	}

	 /**
	  * 加载所有资源
	  */
	private void loadSecurityMetadataSource() {
 		List<Map<String, Object>> resourceDtos = securityResourceDao.findAllResource();
 		if(resourceDtos != null) {
			resources.clear();
			for (Map<String, Object> dto : resourceDtos) {
				resources.put(dto.get("url").toString(), Integer.parseInt(dto.get("id").toString())); 
			}
		}
	}
	
	public boolean supports(Class<?> clazz) {
		return true;
	}
	
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return null;
	}

	 

	public void setRejectPublicInvocations(boolean rejectPublicInvocations) {
		this.rejectPublicInvocations = rejectPublicInvocations;
	}




}