package cn.thinkfree.database.vo.contract;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.Map;

@ApiModel(description = "合同录入实体参数")

public class ContractParam {

  public Date getStartDt() {
    return startDt;
  }

  public void setStartDt(Date startDt) {
    this.startDt = startDt;
  }

  public Date getEndDt() {
    return endDt;
  }

  public void setEndDt(Date endDt) {
    this.endDt = endDt;
  }

  public Map<String, String> getParamMap() {
    return paramMap;
  }

  public void setParamMap(Map<String, String> paramMap) {
    this.paramMap = paramMap;
  }

  @ApiModelProperty(value = "合同开始时间", required = true)
  private Date startDt;

  @ApiModelProperty(value = "合同结束时间", required = true)
  private Date endDt;

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  @ApiModelProperty("设计公司 0新增1修改")
  private String type;

  @ApiModelProperty("合同信息")
  Map<String, String> paramMap;
}
