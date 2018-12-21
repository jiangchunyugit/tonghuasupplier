package cn.thinkfree.service.platform.vo;

import cn.thinkfree.database.model.Project;
import cn.thinkfree.database.model.ProjectData;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * @author xusonghui
 * 运营后台设计订单详情
 */
@Getter
@Setter
public class PcDesignOrderMsgVo {
    @ApiModelProperty("设计订单状态相关信息")
    private List<Map<String, Object>> stateMaps;
    @ApiModelProperty("订单当前状态")
    private int orderState;
    @ApiModelProperty("设计订单相关信息")
    private DesignOrderDelVo designerOrderVo;
    @ApiModelProperty("量房时间")
    private long volumeRoomDate;
    @ApiModelProperty("量房费")
    private String volumeRoomMoney;
    @ApiModelProperty("合同名称")
    private String contractName;
    @ApiModelProperty("合同地址")
    private String contractUrl;
    @ApiModelProperty("费用信息")
    private List<DesignOrderPayMsgVo> payMsgVos;
    @ApiModelProperty("量房资料")
    private List<ProjectData> dataVolume;
    @ApiModelProperty("3D资料")
    private List<ProjectData> data3D;
    @ApiModelProperty("施工资料")
    private List<ProjectData> dataCons;
}
