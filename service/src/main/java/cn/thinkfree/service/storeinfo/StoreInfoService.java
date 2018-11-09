package cn.thinkfree.service.storeinfo;

import cn.thinkfree.database.model.StoreInfo;

import java.util.List;

public interface StoreInfoService {

    /**
     * 通过id查询门店详细信息
     * @param id
     * @return
     */
    StoreInfo storeInfoById (Integer id);

    /**
     * 通过城市id查询门店
     * @param id
     * @return
     */
    List<StoreInfo> storeInfoListByCityId (Integer id);
}
