package cn.thinkfree.web.test;

import cn.thinkfree.database.vo.PcApplyInfoSEO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;



//
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WebAppConfiguration
public class CompanyJoinTest extends AbsControllerTest{
    @Test
    public void listTest() throws Exception {
//
//        ProjectSEO projectSEO = new ProjectSEO();
//        projectSEO.setPage(0);
//        projectSEO.setRows(15);
        PcApplyInfoSEO pcApplyInfoSEO = new PcApplyInfoSEO();
        pcApplyInfoSEO.setApplyType(Short.valueOf("0"));
//        pcApplyInfoSEO.setApplyDate(new Date());
//        pcApplyInfoSEO.setApplyThingType();
//        String rs = mvc.perform(get("/companyApply/applyThink").content(gson.toJson(projectSEO)))
//                .andExpect(status().isOk())
//                .andDo(print())         //打印出请求和相应的内容
//                .andReturn().getResponse().getContentAsString();
//        MyRespBundle rsb = gson.fromJson(rs, MyRespBundle.class);
//        Assert.assertNotNull(rsb.getData());

    }
}
