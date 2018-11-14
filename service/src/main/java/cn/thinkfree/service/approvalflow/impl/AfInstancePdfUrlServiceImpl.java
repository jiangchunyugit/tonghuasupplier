package cn.thinkfree.service.approvalflow.impl;

import cn.thinkfree.database.mapper.AfInstancePdfUrlMapper;
import cn.thinkfree.database.model.AfInstance;
import cn.thinkfree.service.approvalflow.AfInstancePdfUrlService;
import cn.thinkfree.service.config.PdfConfig;
import cn.thinkfree.service.utils.AfUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * TODO
 *
 * @author song
 * @version 1.0
 * @date 2018/11/13 15:56
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class AfInstancePdfUrlServiceImpl implements AfInstancePdfUrlService {

    @Autowired
    private AfInstancePdfUrlMapper instancePdfUrlMapper;
    @Autowired
    private PdfConfig pdfConfig;

    @Override
    public void create(AfInstance instance) {
        AfUtils.createPdf(pdfConfig, null, instance.getData());
    }
}
