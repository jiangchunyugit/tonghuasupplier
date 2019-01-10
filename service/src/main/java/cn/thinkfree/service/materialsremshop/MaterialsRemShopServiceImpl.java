package cn.thinkfree.service.materialsremshop;

import cn.thinkfree.database.mapper.MaterialsRemLeaseContractMapper;
import cn.thinkfree.database.mapper.MaterialsRemShopMapper;
import cn.thinkfree.database.model.MaterialsRemLeaseContract;
import cn.thinkfree.database.model.MaterialsRemLeaseContractExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author jiangchunyu(后台)
 * @date 2018
 * @Description 门店
 */
@Service
public class MaterialsRemShopServiceImpl implements MaterialsRemShopService {

    @Autowired
    MaterialsRemShopMapper materialsRemShopMapper;

    @Autowired
    MaterialsRemLeaseContractMapper materialsRemLeaseContractMapper;

    @Override
    public List<MaterialsRemLeaseContract> getMaterialsRemShops(String dealerCompanyId) {

        MaterialsRemLeaseContractExample materialsRemLeaseContractExample = new MaterialsRemLeaseContractExample();
        materialsRemLeaseContractExample.createCriteria()
                .andGhdwdmEqualTo(dealerCompanyId);
        return materialsRemLeaseContractMapper.selectByExample(materialsRemLeaseContractExample);
    }

    @Override
    public List<MaterialsRemLeaseContract> getMaterialsRemLeaseContracts(String dealerCompanyId, String fddm) {

        MaterialsRemLeaseContractExample materialsRemLeaseContractExample = new MaterialsRemLeaseContractExample();
        materialsRemLeaseContractExample.createCriteria()
                .andGhdwdmEqualTo(dealerCompanyId)
                .andFddmEqualTo(fddm);
        return materialsRemLeaseContractMapper.selectByExample(materialsRemLeaseContractExample);
    }
}
