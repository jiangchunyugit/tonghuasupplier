package cn.thinkfree.web.test;

import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.SysConstants;
import cn.thinkfree.database.model.*;
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
import java.util.List;

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
    public void provinceTest() throws Exception {


        String rs = mvc.perform(get("/dictionary/province").with(user(userVO)))
                .andExpect(status().isOk())
                .andDo(print())         //打印出请求和相应的内容
                .andReturn().getResponse().getContentAsString();
        MyRespBundle<List<Province>> rsb = gson.fromJson(rs, MyRespBundle.class);
        Assert.assertNotNull(rsb.getData());

    }

    @Test
    public void cityTest() throws Exception {
        String provinceCode = "11";
        String rs = mvc.perform(get("/dictionary/city").with(user(userVO)).param("provinceCode",provinceCode))
                .andExpect(status().isOk())
                .andDo(print())         //打印出请求和相应的内容
                .andReturn().getResponse().getContentAsString();

        Type type = new TypeToken<MyRespBundle<List<City>>>() {}.getType();
        MyRespBundle<List<City>> rsb = gson.fromJson(rs, type);
        Assert.assertEquals(rsb.getData().size(),1);
    }

    @Test
    public void areaTest() throws Exception {
        String cityCode = "1101";
        String rs = mvc.perform(get("/dictionary/area").with(user(userVO)).param("cityCode",cityCode))
                .andExpect(status().isOk())
                .andDo(print())         //打印出请求和相应的内容
                .andReturn().getResponse().getContentAsString();

        Type type = new TypeToken<MyRespBundle<List<Area>>>() {}.getType();
        MyRespBundle<List<Area>> rsb = gson.fromJson(rs, type);
        Assert.assertEquals(16,rsb.getData().size());
    }

    @Test
    public void houseTypeTest() throws Exception {

        String rs = mvc.perform(get("/dictionary/houseType").with(user(userVO)))
                .andExpect(status().isOk())
                .andDo(print())         //打印出请求和相应的内容
                .andReturn().getResponse().getContentAsString();


        Type type = new TypeToken<MyRespBundle<List<PreProjectHouseType>>>() {}.getType();
        MyRespBundle<PreProjectHouseType> rsb = gson.fromJson(rs, type);
        Assert.assertNotNull(rsb.getData());
    }

}
