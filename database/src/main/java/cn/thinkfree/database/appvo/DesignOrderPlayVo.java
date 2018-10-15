package cn.thinkfree.database.appvo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author gejiaming
 */
@Data
public class DesignOrderPlayVo {
    @ApiModelProperty(name = "constructionCompany",value = "承接公司")
    private String constructionCompany;
    @ApiModelProperty(name = "persionList",value = "人员集合")
    private List<PersionVo> persionList;

}
