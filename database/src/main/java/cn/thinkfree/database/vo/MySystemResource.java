package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.PcSystemResource;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel("权限分级")
public class MySystemResource {

    @ApiModelProperty("模块下的属性")
    List<PcSystemResourceVo> pcSystemResourceVoList;

    @ApiModelProperty("模块信息")
    PcSystemResource pcSystemResource;

    public List<PcSystemResourceVo> getPcSystemResourceVoList() {
        return pcSystemResourceVoList;
    }

    public void setPcSystemResourceVoList(List<PcSystemResourceVo> pcSystemResourceVoList) {
        this.pcSystemResourceVoList = pcSystemResourceVoList;
    }

    public PcSystemResource getPcSystemResource() {
        return pcSystemResource;
    }

    public void setPcSystemResource(PcSystemResource pcSystemResource) {
        this.pcSystemResource = pcSystemResource;
    }
}
