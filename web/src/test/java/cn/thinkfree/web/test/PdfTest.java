package cn.thinkfree.web.test;

import cn.thinkfree.core.constants.AfConfigs;
import cn.thinkfree.service.config.PdfConfig;
import cn.thinkfree.service.utils.AfUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO
 *
 * @author song
 * @version 1.0
 * @date 2018/11/11 15:29
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WebAppConfiguration
public class PdfTest {

    @Resource
    private PdfConfig pdfConfig;

    @Test
    public void exportPdf() {
        Map<String, Object> data = new HashMap<>();
        data.put("configName", "开工申请");
        AfUtils.createPdf(pdfConfig, AfConfigs.START_APPLICATION.configNo, "审批流", "{}");
    }
}
