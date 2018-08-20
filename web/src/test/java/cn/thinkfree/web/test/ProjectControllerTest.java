package cn.thinkfree.web.test;

import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.SysConstants;
import cn.thinkfree.database.model.CompanyInfo;
import cn.thinkfree.database.model.PcUserInfo;
import cn.thinkfree.database.model.UserRegister;
import cn.thinkfree.database.vo.ProjectDetailsVO;
import cn.thinkfree.database.vo.ProjectSEO;
import cn.thinkfree.database.vo.UserVO;
import cn.thinkfree.service.constants.ProjectStatus;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import javax.servlet.Filter;

import java.lang.reflect.Type;
import java.util.Date;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WebAppConfiguration
public class ProjectControllerTest {

    private MockMvc mvc;
    @Autowired
    private WebApplicationContext context;


    @Autowired
    private Filter springSecurityFilterChain;

    private Gson gson ;

    protected UserVO userVO ;
    @Before
    public void setup() throws Exception {
        System.out.println(springSecurityFilterChain);
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

    private void builderUserVO() {
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

    }


    @Test
    public void listTest() throws Exception {

        ProjectSEO projectSEO = new ProjectSEO();
        projectSEO.setPage(0);
        projectSEO.setRows(15);
        String rs = mvc.perform(get("/project/list").with(user(userVO)).content(gson.toJson(projectSEO)))
                .andExpect(status().isOk())
                .andDo(print())         //打印出请求和相应的内容
                .andReturn().getResponse().getContentAsString();
        MyRespBundle rsb = gson.fromJson(rs, MyRespBundle.class);
        Assert.assertNotNull(rsb.getData());

    }

    @Test
    public void findDetailByProjectNoTest() throws Exception {
        String projectNo = "ITEM18081417190100000EH";
        String rs = mvc.perform(get("/project/details").with(user(userVO)).param("projectNo",projectNo))
                .andExpect(status().isOk())
                .andDo(print())         //打印出请求和相应的内容
                .andReturn().getResponse().getContentAsString();

        Type type = new TypeToken<MyRespBundle<ProjectDetailsVO>>() {}.getType();
        MyRespBundle<ProjectDetailsVO> rsb = gson.fromJson(rs, type);
        Assert.assertEquals(projectNo,rsb.getData().getProjectNo());
    }

    @Test
    public void stopProjectTest() throws Exception {
        String projectNo = "ITEM18081814245500000EH";
        String rs = mvc.perform(post("/project/stop").with(user(userVO)).param("projectNo",projectNo))
                .andExpect(status().isOk())
                .andDo(print())         //打印出请求和相应的内容
                .andReturn().getResponse().getContentAsString();

        String find = mvc.perform(get("/project/details").with(user(userVO)).param("projectNo",projectNo))
                .andExpect(status().isOk())
                .andDo(print())         //打印出请求和相应的内容
                .andReturn().getResponse().getContentAsString();

        Type type = new TypeToken<MyRespBundle<ProjectDetailsVO>>() {}.getType();
        MyRespBundle<ProjectDetailsVO> rsb = gson.fromJson(find, type);
        Assert.assertEquals(ProjectStatus.StopTheWork.shortVal(),rsb.getData().getStatus());
    }

    @Test
    public void delProjectTest() throws Exception {
        String projectNo = "ITEM18081814175600001EH";
        String rs = mvc.perform(delete("/project/del").with(user(userVO)).param("projectNo",projectNo))
                .andExpect(status().isOk())
                .andDo(print())         //打印出请求和相应的内容
                .andReturn().getResponse().getContentAsString();

        String find = mvc.perform(get("/project/details").with(user(userVO)).param("projectNo",projectNo))
                .andExpect(status().isOk())
                .andDo(print())         //打印出请求和相应的内容
                .andReturn().getResponse().getContentAsString();

        Type type = new TypeToken<MyRespBundle<ProjectDetailsVO>>() {}.getType();
        MyRespBundle<ProjectDetailsVO> rsb = gson.fromJson(find, type);
        Assert.assertEquals(SysConstants.YesOrNo.YES.shortVal(),rsb.getData().getIsDelete());
    }

}
