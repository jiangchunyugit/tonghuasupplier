package cn.thinkfree.core.constants;

public enum ResultMessage {
        SUCCESS(200,"操作成功!"),
        FAIL(400,"参数错误"),
        ERROR(500,"操作失败!");

        public Integer code ;
        public String message;
        ResultMessage(Integer code , String message) {
            this.code = code;
            this.message = message;
        }
    }