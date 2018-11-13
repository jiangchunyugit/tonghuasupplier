package cn.thinkfree.service.storeinfo;

import cn.thinkfree.database.model.HrOrganizationEntity;
import cn.thinkfree.database.model.StoreInfo;

import java.util.List;

public interface StoreInfoService {

    /**
     * 通过id查询门店详细信息
     * @param id
     * @return
     */
    StoreInfo storeInfoById (String id);

    /**
     * 通过城市id查询门店
     * @param id
     * @return
     */
    List<StoreInfo> storeInfoListByCityId (Integer id);

    /**
     * 通过分公司查询门店
     * @param id
     * @return
     */
    List<StoreInfo> storeInfoListByCompanyId(Integer id);

    /**
     * 查询全部门店信息
     * @return
     */
    List<HrOrganizationEntity> getHrOrganizationEntity();
}
