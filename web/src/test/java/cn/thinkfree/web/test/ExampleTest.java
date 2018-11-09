package cn.thinkfree.web.test;

import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.database.mapper.AreaMapper;
import cn.thinkfree.database.mapper.CityMapper;
import cn.thinkfree.database.mapper.ProvinceMapper;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.vo.account.PermissionVO;
import cn.thinkfree.service.user.UserService;
import com.github.pagehelper.PageInfo;
import com.google.gson.reflect.TypeToken;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WebAppConfiguration
public class ExampleTest extends AbsControllerTest {


    @Test
    public void saveTest(){

    }

    @Test
    public void findTest() throws Exception {
//
        String rs = mvc.perform(get("/projectExport")
                .with(user(userVO))

        )
                .andExpect(status().isOk())
                .andDo(print())         //打印出请求和相应的内容
                .andReturn().getResponse().getContentAsString();
//        MyRespBundle<PageInfo<PermissionVO>> rsb = gson.fromJson(rs,  new TypeToken<MyRespBundle<PageInfo<PermissionVO>>>() {}.getType());
    }

}
