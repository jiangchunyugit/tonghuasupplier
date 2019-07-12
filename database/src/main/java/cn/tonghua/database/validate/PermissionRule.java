package cn.tonghua.database.validate;

import javax.validation.Payload;

public class PermissionRule {

    /**
     * 新增 新增时需要验证字段组
     */
    public interface Form extends Payload { }
}
