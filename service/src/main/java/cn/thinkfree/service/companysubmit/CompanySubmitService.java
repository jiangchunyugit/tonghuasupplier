package cn.thinkfree.service.companysubmit;

import cn.thinkfree.database.vo.CompanySubmitVo;

/**
 * @author ying007
 * 公司入驻业务
 */
public interface CompanySubmitService {
    /**
     * 公司资质上传是否成功
     * @param companySubmitVo 公司资质
     * @return true false
     */
    boolean upCompanyInfo(CompanySubmitVo companySubmitVo);
}
