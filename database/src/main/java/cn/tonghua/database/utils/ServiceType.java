package cn.tonghua.database.utils;

public enum ServiceType {

    Machie(0,"电脑"),
    Program(1,"程序"),
    Video(2,"视频"),
    Projector(3,"投影仪");

    public final Integer code;
    public final String desc;
    ServiceType(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public Short shortVal(){
        return code.shortValue();
    }
}
