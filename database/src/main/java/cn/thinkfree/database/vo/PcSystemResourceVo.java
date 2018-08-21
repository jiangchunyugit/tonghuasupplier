package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.PcSystemResource;

public class PcSystemResourceVo extends PcSystemResource {
    private boolean isAuthResource = false;

    public boolean isAuthResource() {
        return isAuthResource;
    }

    public void setAuthResource(boolean authResource) {
        isAuthResource = authResource;
    }
}
