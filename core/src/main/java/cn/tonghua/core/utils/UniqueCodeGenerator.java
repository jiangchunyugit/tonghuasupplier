package cn.tonghua.core.utils;

import java.util.UUID;

/**
 * 唯一编码生成器
 * @author songchuanrang
 */
public enum UniqueCodeGenerator {

    /**
     * 审批流配置编码前缀
     */
    AF_CONFIG("AF_CONFIG_"),
    /**
     * 审批流配置记录编码前缀
     */
    AF_CONFIG_SCHEME("AF_CONFIG_SCHEME_"),
    /**
     * 审批流实例编码前缀
     */
    AF_INSTANCE("AF_INSTANCE_"),
    /**
     * 审批流审批日志编码前缀
     */
    AF_APPROVAL_LOG("AF_APPROVAL_LOG_"),
    /**
     * 审批流审批顺序
     */
    AF_APPROVAL_ORDER("AF_APPROVAL_ORDER_");

    private String prefix;

    UniqueCodeGenerator(String prefix){
        this.prefix = prefix;
    }

    public String getCode(){
        return (prefix + UUID.randomUUID().toString().replaceAll("-",""));
    }
}
