package cn.thinkfree.web.test;

import cn.thinkfree.core.utils.JSONUtil;
import cn.thinkfree.database.vo.AfChangeOrderDTO;
import cn.thinkfree.service.config.HttpLinks;
import cn.thinkfree.service.utils.AfUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 *
 * @author song
 * @version 1.0
 * @date 2018/12/6 17:09
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WebAppConfiguration
public class AfChangOrderSendHttpTest {

    @Resource
    private HttpLinks httpLinks;

    @Test
    public void sentHttp() {
        List<AfChangeOrderDTO> changeOrderDTOS = new ArrayList<>();
        AfChangeOrderDTO changeOrderDTO = new AfChangeOrderDTO();
        changeOrderDTO.setProjectNo("PN181205000KSO");
        changeOrderDTO.setFeeName("lla");
        changeOrderDTO.setFeeAmount("0.01");
        changeOrderDTO.setOrderId("CO181205000MFE");
//        changeOrderDTO.setRemark("");
        changeOrderDTOS.add(changeOrderDTO);

        int code = AfUtils.postJson(httpLinks.getCreateFee(), JSONUtil.bean2JsonStr(changeOrderDTOS));
        System.out.println(code);
    }
}
