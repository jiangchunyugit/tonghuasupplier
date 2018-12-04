package cn.thinkfree.database.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;


@ApiModel(value="每个分站下经营主体门店详情")
public class BusinessEntityStoreVO {

    @ApiModelProperty(value="经营主体编号")
    @NotBlank(message = "选择门店时，经营主体编号不可传空",groups = {Severitys.Insert.class})
    private String businessEntityCode;

    @ApiModelProperty("经营主体门店")
    @NotEmpty(message = "选择门店时，门店",groups = {Severitys.Insert.class})
    private List<BusinessEntityRelationVO> businessEntityRelationVOS;

    public String getBusinessEntityCode() {
        return businessEntityCode;
    }

    public void setBusinessEntityCode(String businessEntityCode) {
        this.businessEntityCode = businessEntityCode;
    }

    public List<BusinessEntityRelationVO> getBusinessEntityRelationVOS() {
        return businessEntityRelationVOS;
    }

    public void setBusinessEntityRelationVOS(List<BusinessEntityRelationVO> businessEntityRelationVOS) {
        this.businessEntityRelationVOS = businessEntityRelationVOS;
    }
}