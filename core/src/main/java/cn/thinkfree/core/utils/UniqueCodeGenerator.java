package cn.thinkfree.core.utils;

import java.util.UUID;

/**
 * 唯一编码生成器
 * @author songchuanrang
 */
public enum UniqueCodeGenerator {
    /**
     * 创建审批记录编码
     */
    AFL("AFL-"),
    /**
     * 审批流编码
     */
    AF("AF-"),
    /**
     * 审批流节点编码前缀
     */
    AF_NODE("AF_NODE-"),
    /**
     * 审批流配置记录log表前缀
     */
    AF_LOG("AF_LOG-");

    private String prefix;
    UniqueCodeGenerator(String prefix){
        this.prefix = prefix;
    }

    public String getCode(){
        return (prefix + UUID.randomUUID().toString().replaceAll("-",""));
    }
}
