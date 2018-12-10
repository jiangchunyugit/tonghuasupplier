package cn.thinkfree.service.materialsremshop;

import cn.thinkfree.database.model.MaterialsRemLeaseContract;
import cn.thinkfree.database.model.MaterialsRemShop;

import java.util.List;

/**
 * @author jiangchunyu(后台)
 * @date 2018
 * @Description 门店
 */
public interface MaterialsRemShopService {

    /**
     * 获取门店信息
     * @return
     */
    List<MaterialsRemShop> getMaterialsRemShops();

    /**
     * 通过门店id获取摊铺
     * @param fddm
     * @return
     */
    List<MaterialsRemLeaseContract> getMaterialsRemLeaseContracts(String fddm);
}
