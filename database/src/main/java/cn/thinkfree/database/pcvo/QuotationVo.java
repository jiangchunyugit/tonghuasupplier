package cn.thinkfree.database.pcvo;

import cn.thinkfree.database.model.ProjectQuotationCheck;
import cn.thinkfree.database.model.ProjectQuotationRoomsConstruct;
import cn.thinkfree.database.model.ProjectQuotationRoomsHardDecoration;
import cn.thinkfree.database.model.ProjectQuotationRoomsSoftDecoration;
import cn.thinkfree.database.vo.BasisConstructionVO;
import cn.thinkfree.database.vo.HardQuoteVO;
import cn.thinkfree.database.vo.SoftQuoteVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@ApiModel("精准报价实体")
@Data
public class QuotationVo {
    @ApiModelProperty(value = "房屋名称")
    private String name;
    @ApiModelProperty(value = "房屋编号")
    private String value;
    @ApiModelProperty(value = "基础施工项实体")
    private List<BasisConstructionVO> BasicsTableData;
    @ApiModelProperty(value = "基础施工项总价")
    private BigDecimal basicsSumPrice;
    @ApiModelProperty(value = "软装实体")
    private List<SoftQuoteVO> MaterialTableData;
    @ApiModelProperty(value = "软装总价")
    private BigDecimal materialSumPrice;
    @ApiModelProperty(value = "硬装实体")
    private List<HardQuoteVO> furnitureTableData;
    @ApiModelProperty(value = "硬装总价")
    private BigDecimal furnitureSumPrice;
}
