package cn.tonghua.service.remote;


import cn.tonghua.core.model.BaseModel;

import java.util.List;

public class RemoteResult<T> extends BaseModel {


    private int ret;
    private String session_id;
    private List<String> cand;
    private List<String> asso;

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public List<String> getCand() {
        return cand;
    }

    public void setCand(List<String> cand) {
        this.cand = cand;
    }

    public List<String> getAsso() {
        return asso;
    }

    public void setAsso(List<String> asso) {
        this.asso = asso;
    }
        private Boolean isComplete;

    private Integer code;
    private String msg;
//    private Date timestamp;
    private T data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

//    public Date getTimestamp() {
//        return timestamp;
//    }
//
//    public void setTimestamp(Date timestamp) {
//        this.timestamp = timestamp;
//    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


    public Boolean isComplete() {
        return isComplete;
    }

    public void setIsComplete(Boolean complete) {
        isComplete = complete;
    }
}
