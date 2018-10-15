package cn.thinkfree.service.companysubmit;

import java.util.Map;

import cn.thinkfree.database.vo.CompanyListSEO;
import cn.thinkfree.database.vo.CompanyListVo;
import cn.thinkfree.database.vo.CompanySubmitVo;
import cn.thinkfree.database.vo.CompanyTemporaryVo;
import com.github.pagehelper.PageInfo;

import javax.servlet.http.HttpServletResponse;

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
    
    
    /**
     * 运营审批
     * @author lvqidong
     * 
     */
    
    Map<String,String>  auditContract(String companyId,String auditStatus,String auditCase);

    /**
     * 公司列表
     * @param companyListSEO
     * @return
     */
    PageInfo<CompanyListVo> list(CompanyListSEO companyListSEO);

    /**
     * 列表导出
     * @param companyListSEO
     */
    void downLoad(HttpServletResponse response, CompanyListSEO companyListSEO);

    /**
     * 入驻公司资质变更回显
     * @param companyId
     * @return
     */
    CompanySubmitVo findCompanyInfo(String companyId);

    /**
     * 入驻公司资质变更更新
     * @param companyTemporaryVo
     * @return
     */
    boolean changeCompanyInfo(CompanyTemporaryVo companyTemporaryVo);
}
