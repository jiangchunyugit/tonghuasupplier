package cn.thinkfree.service.materialsremagency;

import cn.thinkfree.database.model.MaterialsRemAgency;
import cn.thinkfree.database.vo.AgencyContractCompanyInfoVo;

import java.util.List;

/**
 * @author jiangchunyu(后台)
 * @date 2018
 * @Description 经销商信息
 */
public interface MaterialsRemAgencyService {

    /**
     * 获取经销商信息
     * @param code
     * @param name
     * @return
     */
    List<MaterialsRemAgency> getMaterialsRemAgencys(String code,String name);

    /**
     * 合同经销商信息
     * @param companyId
     * @param companyName
     * @return
     */
    List<AgencyContractCompanyInfoVo> getAgencyCompanyInfos(String companyId, String companyName);
}
