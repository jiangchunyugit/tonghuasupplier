package cn.thinkfree.database.constants;

public enum  JobState {

    job(1,"在职"),
    quit(0,"离职");

    public Integer code;
    public String mes;
    JobState(Integer code, String mes) {
        this.code = code;
        this.mes = mes;
    }
}
