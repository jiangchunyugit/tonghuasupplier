package cn.thinkfree.core.security.filter;

import cn.thinkfree.core.base.MyLogger;
import cn.thinkfree.core.security.dao.SecurityResourceDao;
import cn.thinkfree.core.security.filter.util.SecurityUrlTrustHolder;
import cn.thinkfree.core.security.model.SecurityResource;
import cn.thinkfree.core.security.utils.UrlUtils;
import cn.thinkfree.core.utils.LogUtil;
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

 	 private MyLogger logger = LogUtil.getLogger(SecurityInvocationSecurityMetadataSource.class);
	 private boolean rejectPublicInvocations = false;
	 private boolean hotDeployment = false;
	 /**
	  * 数据库支持
	  */
	 SecurityResourceDao securityResourceDao;
	 /**
	  * 缓存所有资源
	  */
	 private static Multimap<ResourceKey, String> resources = ArrayListMultimap.create();
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
	 @Override
	 public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
		 String url = UrlUtils.getRealURL((FilterInvocation) object);
		 String method = UrlUtils.getRequestMethod((FilterInvocation) object);
		 if(SecurityUrlTrustHolder.isTrust(UrlUtils.getRealURL((FilterInvocation) object))|| resources.isEmpty()){
			 return null;
		 }

		 Collection<String> codes = resources.get(new ResourceKey(url,method));
		 if(codes.isEmpty()){
		 	logger.warn("匿名资源:{}",object);
//		 	throw new IllegalArgumentException("Secure object invocation " + object +
//						 " was denied as public invocations are not allowed via this interceptor. ");
		 }
		 if(codes.size() == 1){
			 String permissionCode = codes.iterator().next();
			 if(StringUtils.isBlank(permissionCode)){
				 return null;

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
	  * 加载所有资源
	  */
	 private synchronized void loadSecurityMetadataSource() {
		 List<? extends SecurityResource> allResource = securityResourceDao.findAllResource();
		 if(allResource != null) {
			 resources.clear();
			 for (SecurityResource dto : allResource) {
				 resources.put(new ResourceKey(dto.getResource(),dto.getAccessMode()),dto.getRoleCode());
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

	/**
	 * 资源主键
	 */
	public class ResourceKey{

	 	String url;
	 	String method;

		 public ResourceKey(String url, String method) {
			 this.url = url;
			 this.method = method;
		 }

		 @Override
		 public boolean equals(Object o) {
			 if (this == o) return true;
			 if (o == null || getClass() != o.getClass()) return false;

			 ResourceKey that = (ResourceKey) o;

			 if (url != null ? !url.equals(that.url) : that.url != null) return false;
			 return method != null ? method.equals(that.method) : that.method == null;
		 }

		 @Override
		 public int hashCode() {
			 int result = url != null ? url.hashCode() : 0;
			 result = 31 * result + (method != null ? method.hashCode() : 0);
			 return result;
		 }

		 @Override
		 public String toString() {
			 return "ResourceKey{" +
					 "url='" + url + '\'' +
					 ", method='" + method + '\'' +
					 '}';
		 }
	 }



}