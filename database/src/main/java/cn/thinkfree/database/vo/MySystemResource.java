package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.PcSystemResource;

import java.util.List;

public class MySystemResource {
    List<PcSystemResourceVo> pcSystemResourceVoList;
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
