package cn.thinkfree.web.test;

import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.database.vo.SelectItem;
import cn.thinkfree.database.vo.account.PermissionVO;
import com.github.pagehelper.PageInfo;
import com.google.gson.reflect.TypeToken;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WebAppConfiguration
public class OpenApiControllerTest  extends AbsControllerTest{

    @Test
    public void list() throws Exception {
        String rs = mvc.perform(post("/open/v1/companyInfo").param("name","居然")
                .with(user(userVO))
        )
                .andExpect(status().isOk())
                .andDo(print())         //打印出请求和相应的内容
                .andReturn().getResponse().getContentAsString();
        MyRespBundle<List<SelectItem>> rsb = gson.fromJson(rs,  new TypeToken<MyRespBundle<List<SelectItem>>>() {}.getType());
        Assert.assertNotNull(rsb.getData().size()   );
    }
}
