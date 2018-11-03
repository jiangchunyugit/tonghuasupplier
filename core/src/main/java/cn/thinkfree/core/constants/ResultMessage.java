package cn.thinkfree.core.constants;

public enum ResultMessage {
        SUCCESS(1000,"操作成功!"),
        FAIL(400,"参数错误"),
        ERROR(500,"操作失败!");

        public final Integer code ;
        public final String message;
        ResultMessage(Integer code , String message) {
            this.code = code;
            this.message = message;
        }
    }