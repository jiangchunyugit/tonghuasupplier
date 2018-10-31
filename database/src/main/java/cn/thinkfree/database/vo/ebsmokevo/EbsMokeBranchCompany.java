package cn.thinkfree.database.vo.ebsmokevo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author jiangchunyu 埃森哲分公司信息
 */
@ApiModel("埃森哲分公司信息")
public class EbsMokeBranchCompany {

    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("名称")
    private String nm;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNm() {
        return nm;
    }

    public void setNm(String nm) {
        this.nm = nm;
    }
}
