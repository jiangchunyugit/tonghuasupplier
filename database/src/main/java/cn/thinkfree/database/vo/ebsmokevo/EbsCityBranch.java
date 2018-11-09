package cn.thinkfree.database.vo.ebsmokevo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author jiangchunyu 埃森哲城市分站接口
 */
@ApiModel("埃森哲城市分站")
public class EbsCityBranch {

    /**
     * 埃森哲id
     */
    @ApiModelProperty("id")
    private Integer id;

    /**
     * 埃森哲名称
     */
    @ApiModelProperty("名称")
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
