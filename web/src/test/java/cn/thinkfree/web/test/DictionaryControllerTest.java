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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;
import java.lang.reflect.Type;
import java.util.Date;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WebAppConfiguration
public class DictionaryControllerTest extends AbsControllerTest {




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
