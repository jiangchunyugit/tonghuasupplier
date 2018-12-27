package cn.thinkfree.service.storeinfo;

import cn.thinkfree.database.model.HrOrganizationEntity;
import cn.thinkfree.database.model.StoreInfo;

import java.util.List;

/**
 * @author jiangchunyu(后台)
 * @date 2018
 * @Description 门店信息
 */
public interface StoreInfoService {

    /**
     * 通过城市编码查询门店(少栋哥，构建关系图（不校验主体名称）)
     * @param cityBranchCode
     * @return
     */
    List<StoreInfo> storeInfoListByCityId (String cityBranchCode);

    /**
     * 通过城市编码查询门店
     * @param cityBranchCode
     * @return
     */
    List<StoreInfo> storeInfoListByCityCode (String cityBranchCode);
    /**
     * 通过分公司查询门店
     * @param branchCompanyCode
     * @return
     */
    List<StoreInfo> storeInfoListByCompanyId(String branchCompanyCode);

    /**
     * 通过埃森哲分公司id查询门店
     * @param organizationId
     * @return
     */
    List<HrOrganizationEntity> storeInfoListByEbsCompanyId(String organizationId,String cityBranchCode);

    /**
     * 主体选择门店
     * @param cityBranchCode
     * @return
     */
    List<StoreInfo> businessEntityStoreByCityBranchCode(String cityBranchCode,String businessEntityCode);

    /**
     * 获取所有门店信息
     * @return
     */
    List<HrOrganizationEntity> getHrOrganizationEntity();

//    List
}
