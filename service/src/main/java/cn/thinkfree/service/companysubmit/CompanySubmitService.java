package cn.thinkfree.service.companysubmit;

import java.util.Map;

import cn.thinkfree.database.model.PcAuditInfo;
import cn.thinkfree.database.model.PcAuditTemporaryInfo;
import cn.thinkfree.database.vo.*;
import com.github.pagehelper.PageInfo;

import javax.servlet.http.HttpServletResponse;

/**
 * @author ying007
 * 公司入驻业务
 */
public interface CompanySubmitService {

    /**
     * 查询审批不通过的原因
     * @param contractNumber
     * @return
     */
    PcAuditInfo findAuditCase(String contractNumber);

    /**
     * 审批详情查询
     * @param companyId
     * @return
     */
    AuditInfoVO findAuditStatus(String companyId);

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
    
    Map<String,String>  auditContract(String companyId,String auditStatus,String auditCase, String auditLevel);

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

    /**
     * 入驻公司资质变更审批
     * @param companyId
     * @param auditStatus
     * @param auditCase
     * @return
     */
    String auditChangeCompany(String companyId,String auditStatus, String auditCase);

    /**
     * 入驻公司资质变更审批回显。注：申请列表的申请事项如果是资质变更，则使用此接口
     * @param companyId
     * @return
     */
    PcAuditTemporaryInfo findCompanyTemporaryInfo(String companyId);
}
