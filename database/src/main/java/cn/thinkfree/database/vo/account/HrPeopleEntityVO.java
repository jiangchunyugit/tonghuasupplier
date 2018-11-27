package cn.thinkfree.database.vo.account;

import cn.thinkfree.database.model.HrPeopleEntity;

public class HrPeopleEntityVO extends HrPeopleEntity {

    /**
     * 分公司名称
     */
    private String branchCompanyName;

    public String getBranchCompanyName() {
        return branchCompanyName;
    }

    public void setBranchCompanyName(String branchCompanyName) {
        this.branchCompanyName = branchCompanyName;
    }
}
