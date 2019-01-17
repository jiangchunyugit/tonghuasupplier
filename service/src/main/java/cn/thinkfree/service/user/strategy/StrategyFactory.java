package cn.thinkfree.service.user.strategy;

import cn.thinkfree.database.constants.UserLevel;
import cn.thinkfree.database.constants.UserRegisterType;
import cn.thinkfree.service.user.strategy.build.*;
import cn.thinkfree.service.user.strategy.relation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StrategyFactory {



//    @Autowired
//    CompanyAdminRelationStrategy companyAdminStrategy;
//    @Autowired
//    CompanyProvinceRelationStrategy companyProvinceStrategy;
//    @Autowired
//    CompanyCityRelationStrategy companyCityStrategy;
//    @Autowired
//    CompanyAreaRelationStrategy companyAreaStrategy;
    @Autowired
    StoreRelationStrategy storeRelationStrategy;

    @Autowired
    EnterpriseUserBuildStrategy enterpriseLoginStrategy;

    @Autowired
    PlatformUserBuildStrategy platformUserBuildStrategy;

    @Autowired
    StaffBuildStrategy staffBuildStrategy;

    @Autowired
    SmartUserBuildStrategy smartLoginStrategy;


    public RelationStrategy getStrategy(Short level){
//        if(UserLevel.Company_Admin.shortVal().equals(level)){
//            return companyAdminStrategy;
//        }else if(UserLevel.Company_City.shortVal().equals(level)){
//            return companyCityStrategy;
//        }else if(UserLevel.Company_Area.shortVal().equals(level)){
//            return companyAreaStrategy;
//        }else if(UserLevel.Company_Province.shortVal().equals(level)){
//            return companyProvinceStrategy;
//        }else if(UserLevel.Creator.shortVal().equals(level)){
//            return null;
//        }
        return storeRelationStrategy;
    }


    public UserBuildStrategy getStrategy(UserRegisterType type) {
        switch (type){
            case Personal:return smartLoginStrategy;
            case Enterprise: return enterpriseLoginStrategy;
            case Customer: return smartLoginStrategy;
            case Staff: return staffBuildStrategy;
            case Platform:return platformUserBuildStrategy;
            default:return smartLoginStrategy;
        }
    }
}
