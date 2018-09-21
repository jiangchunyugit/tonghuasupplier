package cn.thinkfree.service.companysubmit;

import cn.thinkfree.core.utils.WebFileUtil;
import cn.thinkfree.database.mapper.CompanyInfoExpandMapper;
import cn.thinkfree.database.mapper.CompanyInfoMapper;
import cn.thinkfree.database.mapper.PcCompanyFinancialMapper;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.vo.CompanySubmitFileVo;
import cn.thinkfree.database.vo.CompanySubmitVo;
import cn.thinkfree.service.constants.CompanyAuditStatus;
import cn.thinkfree.service.utils.UserNoUtils;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

/**
 * @author ying007
 * 公司入驻业务
 */
@Service
public class CompanySubmitServiceImpl implements CompanySubmitService {

    @Autowired
    CompanyInfoExpandMapper companyInfoExpandMapper;

    @Autowired
    PcCompanyFinancialMapper pcCompanyFinancialMapper;

    @Autowired
    CompanyInfoMapper companyInfoMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean upCompanyInfo(CompanySubmitVo companySubmitVo) {
        Date date = new Date();

        //1.更新表company_info
        int ci = updateCompanyInfo(companySubmitVo, date);

        //2.插入表pc_company_financial
        int pcf = addFinancial(companySubmitVo, date);

        //3.更新表company_info_expand
        int cie = updateCompanyExpand(companySubmitVo, date);

        if(ci > 0 && pcf > 0 && cie > 0){
            return true;
        }
        return false;
    }

    private int updateCompanyExpand(CompanySubmitVo companySubmitVo, Date date) {
        //公司资质上传文件
        CompanySubmitFileVo companySubmitFileVo = companySubmitVo.getCompanySubmitFileVo();

        CompanyInfoExpand companyInfoExpand = companySubmitVo.getCompanyInfoExpand();
        companyInfoExpand.setUpdateTime(date);
        //企业税务登记证
        companyInfoExpand.setTaxCodePhotoUrl(WebFileUtil.fileCopy("static/", companySubmitFileVo.getTaxCodePhotoUrl()));
        CompanyInfoExpandExample companyInfoExpandExample = new CompanyInfoExpandExample();
        companyInfoExpandExample.createCriteria().andCompanyIdEqualTo(companySubmitVo.getCompanyInfo().getCompanyId());
        return companyInfoExpandMapper.updateByExampleSelective(companyInfoExpand,companyInfoExpandExample);
    }

    private int addFinancial(CompanySubmitVo companySubmitVo, Date date) {
        //公司资质上传文件
        CompanySubmitFileVo companySubmitFileVo = companySubmitVo.getCompanySubmitFileVo();

        PcCompanyFinancial pcCompanyFinancial = companySubmitVo.getPcCompanyFinancial();
        pcCompanyFinancial.setCompanyId(companySubmitVo.getCompanyInfo().getCompanyId());
        pcCompanyFinancial.setCreateTime(date);
        pcCompanyFinancial.setUpdateTime(date);
        //开户行许可证
        pcCompanyFinancial.setLicenseUrl(WebFileUtil.fileCopy("static/", companySubmitFileVo.getLicenseUrl()));
        return pcCompanyFinancialMapper.insertSelective(pcCompanyFinancial);
    }

    private int updateCompanyInfo(CompanySubmitVo companySubmitVo, Date date) {
        //公司资质上传文件
        CompanySubmitFileVo companySubmitFileVo = companySubmitVo.getCompanySubmitFileVo();
        CompanyInfo companyInfo = companySubmitVo.getCompanyInfo();

        companyInfo.setPhone(companyInfo.getLegalPhone());
        companyInfo.setUpdateTime(date);
        //资质上传成功后审批状态改为已激活
        companyInfo.setAuditStatus(CompanyAuditStatus.ACTIVATION.stringVal());
        //营业执照
        companyInfo.setBusinessPhotoUrl(WebFileUtil.fileCopy("static/", companySubmitFileVo.getBusinessPhotoUrl()));
        //装修施工资质证书
        companyInfo.setWorkPhotoUrl(WebFileUtil.fileCopy("static/", companySubmitFileVo.getWorkPhotoUrl()));
        //法人身份证正面
        companyInfo.setLefalCardUpUrl(WebFileUtil.fileCopy("static/", companySubmitFileVo.getLefalCardUpUrl()));
        //法人身份证反面
        companyInfo.setLefalCardDownUrl(WebFileUtil.fileCopy("static/", companySubmitFileVo.getLefalCardDownUrl()));
        CompanyInfoExample companyInfoExample = new CompanyInfoExample();
        companyInfoExample.createCriteria().andCompanyIdEqualTo(companyInfo.getCompanyId());
        return companyInfoMapper.updateByExampleSelective(companyInfo,companyInfoExample);
    }
}
