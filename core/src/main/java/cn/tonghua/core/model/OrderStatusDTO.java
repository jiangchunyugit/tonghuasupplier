package cn.tonghua.core.model;

import lombok.Data;

/**
 * 订单状态model
 *
 * @author song
 * @version 1.0
 * @date 2018/12/7 11:18
 */
@Data
public class OrderStatusDTO {
    /**
     * 订单状态码
     */
    private Integer status;
    /**
     * 状态名称
     */
    private String name;
}
