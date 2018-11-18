package cn.thinkfree.database.vo;

import cn.thinkfree.core.constants.SysConstants;
import cn.thinkfree.database.model.CompanyUser;

public class EnterPriseUserVO extends UserVO {



    @Override
    public boolean isEnabled() {

        if(getCompanyUser() != null && SysConstants.YesOrNo.NO.shortVal().equals(getCompanyUser().getIsJob())){
            return false;
        }
        if(SysConstants.YesOrNo.YES.shortVal().equals(getCompanyInfo().getPlatformType().shortValue())){
            return false;
        }
        return true;
    }
}
