package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.DealerBrandInfo;
import cn.thinkfree.database.model.DealerCategory;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(description = "品牌品类信息")
public class DealerBrandInfoVO extends DealerBrandInfo {

    @ApiModelProperty("品类信息 ")
    List<DealerCategory> categoryList;

    public List<DealerCategory> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<DealerCategory> categoryList) {
        this.categoryList = categoryList;
    }
}
