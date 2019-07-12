/**
 * Security 需知
 * 1：安全链的入口是 extends AbstractSecurityInterceptor implements Filter
 * 2：安全链的资源加载器是需要在入口里声明的  implements FilterInvocationSecurityMetadataSource
 * 3：会通过 implements AccessDecisionManager来判定是否有资格访问权限
 * Created by lenovo on 2017/2/24.
 */
package cn.tonghua.core.security;