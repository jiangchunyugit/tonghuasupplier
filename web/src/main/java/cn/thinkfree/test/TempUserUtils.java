package cn.thinkfree.test;

import cn.thinkfree.core.security.filter.util.SessionUserDetailsUtil;
import cn.thinkfree.core.security.filter.util.TempTestUtils;
import cn.thinkfree.database.model.CompanyInfo;
import cn.thinkfree.database.model.PcUserInfo;
import cn.thinkfree.database.model.UserRegister;
import cn.thinkfree.database.vo.UserVO;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 临时用户工具类
 * 为了给前端测试
 */
@Component("tempTestUtils")
public class TempUserUtils implements TempTestUtils {

    @PostConstruct
    public void init(){
        System.out.println("神奇的反向操作");
        SessionUserDetailsUtil.tempTestUtils = this;
    }


    @Override
    public UserVO builderUserVO() {
        UserVO userVO = new UserVO();
        UserRegister userRegister = new UserRegister();
        PcUserInfo pcUserInfo = new PcUserInfo();
        CompanyInfo companyInfo = new CompanyInfo();
        userRegister.setPhone("pcTest");
        userRegister.setPassword("14e1b600b1fd579f47433b88e8d85291");
        userRegister.setType(Short.valueOf("3"));
        userRegister.setUserId("PC20180815");
        userRegister.setId(22);
        userVO.setUserRegister(userRegister);

        pcUserInfo.setArea("610125");
        pcUserInfo.setCity("6101");
        pcUserInfo.setProvince("61");
        pcUserInfo.setRootCompanyId("BD2018080710405900001");
        pcUserInfo.setCompanyId("BD2018080710405900001");
        pcUserInfo.setLevel(Short.valueOf("1"));
        userVO.setPcUserInfo(pcUserInfo);

        companyInfo.setRootCompanyId("BD2018080710405900001");
        companyInfo.setCompanyId("BD2018080710405900001");
        companyInfo.setParentCompanyId("BD2018080710405900001");
        companyInfo.setAreaCode(110105);
        companyInfo.setProvinceCode(Short.valueOf("11"));
        companyInfo.setCityCode(Short.valueOf("1101"));
        userVO.setCompanyInfo(companyInfo);

        userVO.setRelationMap(Lists.newArrayList("BD2018080710405900001"));
        return userVO;
    }

}
