package cn.thinkfree.service.materialsremshop;

import cn.thinkfree.database.mapper.MaterialsRemLeaseContractMapper;
import cn.thinkfree.database.mapper.MaterialsRemShopMapper;
import cn.thinkfree.database.model.MaterialsRemLeaseContract;
import cn.thinkfree.database.model.MaterialsRemLeaseContractExample;
import cn.thinkfree.database.model.MaterialsRemShop;
import cn.thinkfree.database.model.MaterialsRemShopExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
//        List<MaterialsRemLeaseContract> materialsRemLeaseContracts =
//        List<String> contracts = materialsRemLeaseContracts.stream().map(e->e.getFddm()).collect(Collectors.toList());
//        MaterialsRemShopExample materialsRemShopExample = new MaterialsRemShopExample();
//        materialsRemShopExample.createCriteria().andFddmIn(contracts);
//        return materialsRemShopMapper.selectByExample(materialsRemShopExample);
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
