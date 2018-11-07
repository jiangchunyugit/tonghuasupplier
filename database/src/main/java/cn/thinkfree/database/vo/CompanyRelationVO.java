package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.BranchCompany;
import cn.thinkfree.database.model.CityBranch;

import java.util.List;

public class CompanyRelationVO extends BranchCompany {

    private List<CityBranch> cityBranchList;

    public List<CityBranch> getCityBranchList() {
        return cityBranchList;
    }

    public void setCityBranchList(List<CityBranch> cityBranchList) {
        this.cityBranchList = cityBranchList;
    }
}
