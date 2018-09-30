package cn.thinkfree.service.user.strategy;

import cn.thinkfree.database.constants.UserLevel;
import cn.thinkfree.service.constants.UserRegisterType;
import cn.thinkfree.service.user.strategy.relation.*;
import cn.thinkfree.service.user.strategy.build.EnterpriseUserBuildStrategy;
import cn.thinkfree.service.user.strategy.build.UserBuildStrategy;
import cn.thinkfree.service.user.strategy.build.SmartUserBuildStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StrategyFactory {



    @Autowired
    CompanyAdminRelationStrategy companyAdminStrategy;
    @Autowired
    CompanyProvinceRelationStrategy companyProvinceStrategy;
    @Autowired
    CompanyCityRelationStrategy companyCityStrategy;
    @Autowired
    CompanyAreaRelationStrategy companyAreaStrategy;

    @Autowired
    EnterpriseUserBuildStrategy enterpriseLoginStrategy;

    @Autowired
    SmartUserBuildStrategy smartLoginStrategy;


    public RelationStrategy getStrategy(Short level){
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


    public UserBuildStrategy getStrategy(UserRegisterType type) {
        switch (type){
            case Staff: return smartLoginStrategy;
            case Customer: return smartLoginStrategy;
            case Personal:return smartLoginStrategy;
            case Enterprise: return enterpriseLoginStrategy;
            default:return smartLoginStrategy;
        }
    }
}
