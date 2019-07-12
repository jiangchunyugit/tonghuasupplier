package cn.tonghua.core.security.filter;

import cn.tonghua.core.security.filter.util.SecurityUrlTrustHolder;
import cn.tonghua.core.security.utils.UrlUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;

import java.util.Collection;
import java.util.Iterator;

/**
 * 安全资源（URL）权限决策器
 * 
 */
 public class SecurityAccessDecisionManager implements AccessDecisionManager {

	private boolean allowIfAllAbstainDecisions = false;


	public boolean isAllowIfAllAbstainDecisions() {
		return allowIfAllAbstainDecisions;
	}

	public void setAllowIfAllAbstainDecisions(boolean allowIfAllAbstainDecisions) {
		this.allowIfAllAbstainDecisions = allowIfAllAbstainDecisions;
	}


	protected final void checkAllowIfAllAbstainDecisions() {
		if (!this.isAllowIfAllAbstainDecisions()) {
			throw new AccessDeniedException("Access is denied");
		}
	}

	//这里的三个参数可以片面的理解为： 用户登录后的凭证(其中包含了用户名和角色的对应关系)、请求的URL、请求的URL对应角色(这些角色可以访问这些URL)
	public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes)
			throws AccessDeniedException, InsufficientAuthenticationException {

		// 跳过白名单
		if(SecurityUrlTrustHolder.isTrust(UrlUtils.getRealURL((FilterInvocation)object)) || configAttributes == null){
			return;
		}
		setAllowIfAllAbstainDecisions(false);

		String needRole = null;
		Iterator<ConfigAttribute> ite = configAttributes.iterator();
		while(ite.hasNext()){
			ConfigAttribute ca = ite.next();
			needRole = ca.getAttribute();

			for(GrantedAuthority role:authentication.getAuthorities()){
				if(StringUtils.equals(role.getAuthority(),needRole) ){
					setAllowIfAllAbstainDecisions(true);
					return;
				}
			}
		}
		checkAllowIfAllAbstainDecisions();
	}

	@Override
	public boolean supports(ConfigAttribute attribute) {
		return true;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}
}
