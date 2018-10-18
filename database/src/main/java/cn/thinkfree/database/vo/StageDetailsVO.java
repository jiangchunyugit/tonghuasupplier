package cn.thinkfree.database.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


import java.util.Date;

/**
 * @Auther: jiang
 * @Date: 2018/10/18 15:33
 * @Description: 施工详情
 */
@ApiModel("施工详情")
public class StageDetailsVO {
    @ApiModelProperty("阶段")
    private Integer stage;
    @ApiModelProperty("订单类型(1,设计订单 2,施工订单 3,项目)")
    private Integer type;
    @ApiModelProperty("时间")
    private Date createTime;

    public Integer getStage() {
        return stage;
    }

    public void setStage(Integer stage) {
        this.stage = stage;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
