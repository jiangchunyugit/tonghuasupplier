package cn.thinkfree.core.convert;


import cn.thinkfree.core.annotation.AppParameter;
import cn.thinkfree.core.annotation.MyRespBody;
import org.springframework.core.Conventions;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMessageConverterMethodProcessor;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

/**
 *  自定义数据转换
 *  输入为了版本号等信息
 *  输出为了加密和时间戳等信息
 */
public class MySpringProcessor extends AbstractMessageConverterMethodProcessor {

    public MySpringProcessor(List<HttpMessageConverter<?>> converters) {
        super(converters);
    }

    public MySpringProcessor(List<HttpMessageConverter<?>> converters, ContentNegotiationManager contentNegotiationManager) {
        super(converters, contentNegotiationManager);
    }

    public MySpringProcessor(List<HttpMessageConverter<?>> converters, ContentNegotiationManager manager, List<Object> requestResponseBodyAdvice) {
        super(converters, manager, requestResponseBodyAdvice);
    }

    /**
     * 是否支持参数
     * @param methodParameter 方法传递的参数
     * @return
     */
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(AppParameter.class);
    }

    /**
     * 如果支持参数 如何处理
     * @param methodParameter     方法传递的参数
     * @param modelAndViewContainer  容器
     * @param nativeWebRequest      request请求
     * @param webDataBinderFactory  数据转换工厂
     * @return
     * @throws Exception
     */
    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        methodParameter = methodParameter.nestedIfOptional();
        Object arg = this.readWithMessageConverters(nativeWebRequest, methodParameter, methodParameter.getNestedGenericParameterType());
        String name = Conventions.getVariableNameForParameter(methodParameter);
        WebDataBinder binder = webDataBinderFactory.createBinder(nativeWebRequest, arg, name);
        if(arg != null) {
            this.validateIfApplicable(binder, methodParameter);
            if(binder.getBindingResult().hasErrors() && this.isBindExceptionRequired(binder, methodParameter)) {
                throw new MethodArgumentNotValidException(methodParameter, binder.getBindingResult());
            }
        }

        modelAndViewContainer.addAttribute(BindingResult.MODEL_KEY_PREFIX + name, binder.getBindingResult());
        return this.adaptArgumentIfNecessary(arg, methodParameter);
    }
    protected <T> Object readWithMessageConverters(NativeWebRequest webRequest, MethodParameter parameter, Type paramType) throws IOException, HttpMediaTypeNotSupportedException, HttpMessageNotReadableException {
        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        ServletServerHttpRequest inputMessage = new ServletServerHttpRequest(servletRequest);
        Object arg = this.readWithMessageConverters(inputMessage, parameter, paramType);
        if(arg == null && this.checkRequired(parameter)) {
            throw new HttpMessageNotReadableException("Required request body is missing: " + parameter.getMethod().toGenericString());
        } else {
            return arg;
        }
    }
    protected boolean checkRequired(MethodParameter parameter) {
        return (parameter.getParameterAnnotation(AppParameter.class)).required() && !parameter.isOptional();
    }

    /**
     * 是否支持的返回类型
     * @param methodParameter  方法返回类型
     * @return
     */
    @Override
    public boolean supportsReturnType(MethodParameter methodParameter) {
        return AnnotatedElementUtils.hasAnnotation(methodParameter.getContainingClass(), MyRespBody.class) || methodParameter.hasMethodAnnotation(MyRespBody.class);
    }


    /**
     * 如何处理返回类型
     * @param returnVal   返回数据
     * @param returnType  返回类型
     * @param modelAndViewContainer 容器
     * @param nativeWebRequest  本地请求
     * @throws Exception
     */
    @Override
    public void handleReturnValue(Object returnVal, MethodParameter returnType, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest) throws Exception {
        modelAndViewContainer.setRequestHandled(true);
        ServletServerHttpRequest inputMessage = this.createInputMessage(nativeWebRequest);
        ServletServerHttpResponse outputMessage = this.createOutputMessage(nativeWebRequest);
        this.writeWithMessageConverters(returnVal, returnType, inputMessage, outputMessage);
    }
}
