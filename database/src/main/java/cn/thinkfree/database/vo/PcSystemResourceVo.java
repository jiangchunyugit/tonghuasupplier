package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.PcSystemResource;
import io.swagger.annotations.ApiModelProperty;


public class PcSystemResourceVo extends PcSystemResource {
    @ApiModelProperty("用户是否有该权限")
    private boolean isAuthResource = false;

    public boolean isAuthResource() {
        return isAuthResource;
    }

    public void setAuthResource(boolean authResource) {
        isAuthResource = authResource;
    }
}
