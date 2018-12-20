package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.DealerBrandInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="经销商品牌品类条目")
public class BrandItemsVO extends DealerBrandInfo{

    @ApiModelProperty(value="大于0则是经销商有品牌品类需要被审批")
    private String count;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
