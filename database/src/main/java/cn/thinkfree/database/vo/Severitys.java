package cn.thinkfree.database.vo;

import javax.validation.Payload;

/**
 * 接口参数验证：注释分组--->1.添加分组，2.更新分组，3.查询分组
 */
public class Severitys {

    /**
     * 新增 新增时需要验证字段组
     */
    public interface Insert extends Payload{

    }
    /**
     * 修改 修改时需要验证字段组
     */
    public interface Update extends Payload{

    }
    /**
     * 条件查询  查询时需要验证字段组
     */
    public interface Select extends Payload{

    }
}
