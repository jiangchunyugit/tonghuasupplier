package cn.thinkfree.core.security.filter;

import cn.thinkfree.core.security.dao.SecurityResourceDao;
import cn.thinkfree.core.security.filter.util.SecurityUrlTrustHolder;
import cn.thinkfree.core.security.filter.util.SecurityUserTrustListHolder;
import cn.thinkfree.core.security.model.SecurityResource;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

import java.util.*;
import java.util.stream.Collectors;


/**
 * 资源加载器
 */
 public class SecurityInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

	 private boolean rejectPublicInvocations = false;
	 private boolean hotDeployment = false;

	 private static String prefix="ROLE_";

	 /**
	  * 数据库支持
	  */
	 SecurityResourceDao securityResourceDao;

	 /**
	  * 缓存所有资源
	  */
	 private static Multimap<String, String> resources = ArrayListMultimap.create();
	 public SecurityInvocationSecurityMetadataSource(SecurityResourceDao securityResourceDao) {
		 this.securityResourceDao = securityResourceDao;
		 loadSecurityMetadataSource();

	 }


	 /**
	  * 匹配资源需要的权限
	  * TODO 开发结束后补充资源
	  * @param object  资源
	  * @return
	  * @throws IllegalArgumentException
	  */
	 public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
		 String url = ((FilterInvocation)object).getRequestUrl();
		 url = url.replaceAll("/\\d{1,}","/{id}");
// 		if(StringUtils.isNotBlank(url)&& url.indexOf("?") > 0){
// 			url = url.substring(0,url.indexOf("?"));
//		}
		 if(SecurityUrlTrustHolder.isTrust(url)|| resources.isEmpty()){
			 return null;
		 }

// 		Integer resourceId = resources.get(url);
// 		if(resourceId == null && hotDeployment){
// 			loadSecurityMetadataSource();
// 			resourceId = resources.get(url);
//		}
		 Collection<String> codes = resources.get(url);
		 if(codes.size() == 1){
//			String permissionCode = resources.get(url);
			 String permissionCode = codes.iterator().next();
			 //		if( resourceId == null) {
			 if(StringUtils.isBlank(permissionCode)){
				 return null;
//				 throw new IllegalArgumentException("Secure object invocation " + object +
//						 " was denied as public invocations are not allowed via this interceptor. ");
			 }
			 return buildPermissionByCode(permissionCode);
		 }else{
			 return buildPermissionByCode(codes);
		 }
//		return getPermissionByResourceId(resourceId);
	 }



	 /**
	  * 根据资源的CODE进行重新组建
	  * @param permissionCode
	  * @return
	  */
	 private Collection<ConfigAttribute> buildPermissionByCode(String permissionCode) {
		 return Lists.newArrayList(new SecurityConfig(permissionCode));
	 }
	 private Collection<ConfigAttribute> buildPermissionByCode(Collection<String> permissionCode) {

		 return permissionCode.stream()
				 .filter(p->StringUtils.isNotBlank(p))
				 .map(p->new SecurityConfig(p))
				 .collect(Collectors.toList());
	 }

	 /**
	  * @TODO 开发阶段 关闭权限验证
	  */
	 private Collection<ConfigAttribute> getPermissionByResourceId(Integer resourceId) {
		 List<String>  roles = null;//securityResourceDao.selectPermissionsByResourceID(resourceId);

		 Collection<ConfigAttribute> atts = null;
//		atts = Lists.newArrayList(new SecurityConfig(prefix+"USER"));
		 if(!roles.isEmpty()) {
			 atts = new ArrayList<>();
			 for (String role : roles) {
				 atts.add(new SecurityConfig(role));
			 }
		 }
		 return atts == null ? Lists.newArrayList(new SecurityConfig("ROLE_ANONYMOUSLY")) :atts ;
	 }

	 /**
	  * 加载所有资源
	  */
	 private synchronized void loadSecurityMetadataSource() {
		 List<? extends SecurityResource> allResource = securityResourceDao.findAllResource();
		 if(allResource != null) {
			 resources.clear();
			 for (SecurityResource dto : allResource) {
				 resources.put(dto.getResouce(),dto.getRoleCode());
			 }
		 }
	 }

	 public boolean supports(Class<?> clazz) {
		 return true;
	 }

	 /**
	  * 初始化全部权限
	  * @return
	  */
	 @Override
	 public Collection<ConfigAttribute> getAllConfigAttributes() {
		 return null;
	 }


	 public void setHotDeployment(boolean hotDeployment) {
		 this.hotDeployment = hotDeployment;
	 }

	 public void setRejectPublicInvocations(boolean rejectPublicInvocations) {
		 this.rejectPublicInvocations = rejectPublicInvocations;
	 }




}