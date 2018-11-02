package cn.thinkfree.database.pcvo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;

@ApiModel("施工订单")
@Data
public class ConstructionOrderVO {
    String orderNo;
    Date createTime;
    Date closeTime;
    Integer orderStage;
    String orderSource;
    String ownerId;
    String addressDetail;
    Integer houseType;
    Integer decorationBudget;
    Integer houseRoom;
    Integer houseOffice;
    Integer houseToilet;
    Integer area;
    String style;
    String remark;

}
