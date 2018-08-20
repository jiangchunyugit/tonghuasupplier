package cn.thinkfree.service.user.builder;

import cn.thinkfree.database.constants.UserLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StrategyFactory {



    @Autowired
    CompanyAdminStrategy companyAdminStrategy;
    @Autowired
    CompanyProvinceStrategy companyProvinceStrategy;
    @Autowired
    CompanyCityStrategy companyCityStrategy;
    @Autowired
    CompanyAreaStrategy companyAreaStrategy;

    public Strategy getStrategy(Short level){
        if(UserLevel.Company_Admin.shortVal().equals(level)){
            return companyAdminStrategy;
        }else if(UserLevel.Company_City.shortVal().equals(level)){
            return companyCityStrategy;
        }else if(UserLevel.Company_Area.shortVal().equals(level)){
            return companyAreaStrategy;
        }else if(UserLevel.Company_Province.shortVal().equals(level)){
            return companyProvinceStrategy;
        }else if(UserLevel.Creator.shortVal().equals(level)){
            return null;
        }
        return null;
    }


}
