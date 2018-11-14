package cn.thinkfree.web.test;

import cn.thinkfree.core.constants.AfConfigs;
import cn.thinkfree.database.vo.AfExportPdfVO;
import cn.thinkfree.database.vo.AfUserVO;
import cn.thinkfree.service.config.PdfConfig;
import cn.thinkfree.service.utils.AfUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    @Autowired
    private PdfConfig pdfConfig;

    @Test
    public void exportPdf() {
        AfExportPdfVO exportPdfVO = new AfExportPdfVO();

        exportPdfVO.setAddress("地址");
        exportPdfVO.setConfigName("开工审批");
        exportPdfVO.setConfigNo(AfConfigs.START_APPLICATION.configNo);
        exportPdfVO.setCreateTime("现在");
        exportPdfVO.setCreateUsername("张三");
        exportPdfVO.setCreateRoleName("角色");
        exportPdfVO.setCustomerName("李四");
        exportPdfVO.setPhoneNo("18612990419");
        exportPdfVO.setInstanceNo("145269872233365");
        exportPdfVO.setProjectNo("adsfasdfasdfasdf");
        exportPdfVO.setRemark("这个哈尔覅时代峰峻按实际佛那就是对佛教会发生的风景安静的佛");

        List<AfUserVO> userVOs = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            AfUserVO userVO = new AfUserVO();
            userVO.setRoleName("角色" + i);
            userVO.setUsername("用户姓名" + i);
            userVOs.add(userVO);
        }
        exportPdfVO.setApprovalUsers(userVOs);


        String exportFileName = AfUtils.createPdf(pdfConfig, exportPdfVO, "{}");
        String pdfUrl = AfUtils.uploadFile(pdfConfig.getExportDir(), exportFileName, pdfConfig.getFileUploadUrl());
        System.out.println(pdfUrl);
    }
}
