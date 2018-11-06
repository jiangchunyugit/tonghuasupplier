package cn.thinkfree.database.vo;

import cn.thinkfree.core.model.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 下拉框类型
 */
@ApiModel("键值对类型")
public class SelectItem extends BaseModel {

    /**
     * 键
     */
    @ApiModelProperty("键")
    private String label;

    /**
     * 值
     */
    @ApiModelProperty("值")
    private String val;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public SelectItem() {
    }

    public SelectItem(String key, String val) {
        this.label = key;
        this.val = val;
    }
}
