package cn.thinkfree.web.test;

import cn.thinkfree.database.model.CompanyInfo;
import cn.thinkfree.database.model.PcUserInfo;
import cn.thinkfree.database.model.UserRegister;
import cn.thinkfree.database.vo.UserVO;
import com.google.common.collect.Lists;
import com.google.gson.*;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;
import java.lang.reflect.Type;
import java.util.Date;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

public  abstract class AbsControllerTest {

    protected MockMvc mvc;

    @Autowired
    protected WebApplicationContext context;

    protected Gson gson ;

    protected UserVO userVO ;

    @Before
    public void setup() throws Exception {
        this.mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity() )
                .build();
        gson = new GsonBuilder()
                .serializeNulls()
                .enableComplexMapKeySerialization()
                .registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                        return new Date(json.getAsJsonPrimitive().getAsLong());
                    }
                })
                .create();
        builderUserVO();
    }

    protected void builderUserVO() {
        userVO = new UserVO();
        UserRegister userRegister = new UserRegister();
        PcUserInfo pcUserInfo = new PcUserInfo();
        CompanyInfo companyInfo = new CompanyInfo();
        userRegister.setPhone("pcTest");
        userRegister.setPassword("14e1b600b1fd579f47433b88e8d85291");
        userRegister.setType(Short.valueOf("3"));
        userRegister.setUserId("PC20180815");
        userRegister.setId(22);
        userVO.setUserRegister(userRegister);

//        pcUserInfo.setArea("610125");
//        pcUserInfo.setCity("6101");
//        pcUserInfo.setProvince("61");
        pcUserInfo.setRootCompanyId("BD2018080710405900001");
//        pcUserInfo.setCompanyId("BD2018080710405900001");
        pcUserInfo.setLevel(Short.valueOf("1"));
        userVO.setPcUserInfo(pcUserInfo);

//        companyInfo.setRootCompanyId("BD2018080710405900001");
        companyInfo.setCompanyId("BD2018080710405900001");
//        companyInfo.setParentCompanyId("BD2018080710405900001");
        companyInfo.setAreaCode(110105);
        companyInfo.setProvinceCode(Short.valueOf("11"));
        companyInfo.setCityCode(Short.valueOf("1101"));
        userVO.setCompanyInfo(companyInfo);

        userVO.setRelationMap(Lists.newArrayList("BD2018080710405900001"));

    }

}
