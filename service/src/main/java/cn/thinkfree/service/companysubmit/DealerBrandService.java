package cn.thinkfree.service.companysubmit;

import cn.thinkfree.database.model.DealerBrandInfo;
import cn.thinkfree.database.vo.AuditBrandInfoVO;
import cn.thinkfree.database.vo.BrandDetailVO;
import cn.thinkfree.database.vo.BrandItemsVO;
import cn.thinkfree.database.vo.PcAuditInfoVO;

import java.util.List;
import java.util.Map;


/**
 * 经销商接口
 */
public interface DealerBrandService {

    /**
     * 添加经销商品牌
     * @param dealerBrandInfo
     * @return
     */
    Map<String, Object> saveBrand(DealerBrandInfo dealerBrandInfo);

    /**
     * 经销商品牌审核回显
     * @param brandDetailVO
     * @return
     */
    List<AuditBrandInfoVO> showBrandDetail(BrandDetailVO brandDetailVO);

    /**
     * 经销商品牌审核回显品牌品类条目
     * @param companyId
     * @param agencyCode
     * @return
     */
    List<BrandItemsVO> showBrandItems(String companyId, String agencyCode);

    /**
     * 品牌审批
     * @param pcAuditInfoVO
     * @return
     */
    Map<String,Object> auditBrand(PcAuditInfoVO pcAuditInfoVO);

    /**
     * 品牌变更
     * @param dealerBrandInfo
     * @return
     */
    boolean updateBrand(DealerBrandInfo dealerBrandInfo);

    /**
     * 审批变更品牌
     * @param pcAuditInfoVO
     * @return
     */
    Map<String,Object> auditChangeBrand(PcAuditInfoVO pcAuditInfoVO);

    /**
     * 显示已经签约的品牌
     * @param companyId
     * @return
     */
    List<DealerBrandInfo> showSignBrand(String companyId);

}
