package cn.thinkfree.database.vo;

import cn.thinkfree.core.model.BaseModel;

public class ActivationCodeVO extends BaseModel {

    private String code;
    private String body;

    public ActivationCodeVO(String code ,String file){
        this.code = code;
        this.body = file;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

}
