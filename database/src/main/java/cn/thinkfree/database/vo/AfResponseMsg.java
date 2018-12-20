package cn.thinkfree.database.vo;

import lombok.Data;

/**
 * http请求响应数据
 *
 * @author song
 * @version 1.0
 * @date 2018/12/6 17:55
 */
@Data
public class AfResponseMsg {
    private int code;
    private String msg;
    private String data;
}
