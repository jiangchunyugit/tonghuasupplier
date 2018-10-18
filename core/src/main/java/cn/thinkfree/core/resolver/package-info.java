/**
 * @RequestBody 不支持多个字符串参数 
 * 自定义注解 封装注解  MultiRequestBody 
 * 使用自定义 MultiRequestBodyArgumentResolver 解析器
 */
package cn.thinkfree.core.resolver;

import org.springframework.web.bind.annotation.RequestBody;
