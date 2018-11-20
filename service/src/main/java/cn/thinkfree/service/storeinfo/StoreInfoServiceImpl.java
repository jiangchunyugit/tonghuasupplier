package cn.thinkfree.service.storeinfo;

import cn.thinkfree.database.constants.OneTrue;
import cn.thinkfree.database.constants.UserEnabled;
import cn.thinkfree.database.mapper.BusinessEntityMapper;
import cn.thinkfree.database.mapper.CityBranchMapper;
import cn.thinkfree.database.mapper.HrOrganizationEntityMapper;
import cn.thinkfree.database.mapper.StoreInfoMapper;
import cn.thinkfree.database.model.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jiangchunyu(后台)
 * @date 2018
 * @Description 门店信息
 */
@Service
public class StoreInfoServiceImpl implements StoreInfoService {

    @Autowired
    StoreInfoMapper storeInfoMapper;

    @Autowired
    HrOrganizationEntityMapper hrOrganizationEntityMapper;

    @Autowired
    BusinessEntityMapper businessEntityMapper;

    @Autowired
    CityBranchMapper cityBranchMapper;

    @Override
    public List<StoreInfo> storeInfoListByCityId(String cityCode) {

        // 经营主体获取门店（启用）
        BusinessEntityExample businessEntityExample = new BusinessEntityExample();
        businessEntityExample.createCriteria().andCityBranchCodeEqualTo(cityCode)
                .andIsDelEqualTo(OneTrue.YesOrNo.NO.shortVal())
                .andIsEnableEqualTo(UserEnabled.Enabled_true.code.shortValue());
        List<BusinessEntity> businessEntities = businessEntityMapper.selectByExample(businessEntityExample);
        List<String> businessEntityCodes= businessEntities.stream().filter(e->StringUtils.isNotBlank(e.getBusinessEntityCode())).map(e->e.getBusinessEntityCode()).collect(Collectors.toList());
        StoreInfoExample storeInfoExample = new StoreInfoExample();
        if (businessEntityCodes.size() > 0) {

            // 获取门店信息
            storeInfoExample.createCriteria().andBusinessEntityCodeIn(businessEntityCodes);
            return storeInfoMapper.selectByCityBranchCode(storeInfoExample);
        }
        return new ArrayList<>();
    }

    @Override
    public List<StoreInfo> storeInfoListByCompanyId(String branchCompanyCode) {

        // 获取城市分站编号
        CityBranchExample cityBranchExample = new CityBranchExample();
        cityBranchExample.createCriteria()
                .andIsDelEqualTo(OneTrue.YesOrNo.NO.shortVal())
                .andIsEnableEqualTo(UserEnabled.Enabled_true.code.shortValue())
                .andBranchCompanyCodeEqualTo(branchCompanyCode);
        List<CityBranch> cityBranches = cityBranchMapper.selectByExample(cityBranchExample);
        List<String> cityBranchCodes= cityBranches.stream().filter(e->StringUtils.isNotBlank(e.getCityBranchCode())).map(e->e.getCityBranchCode()).collect(Collectors.toList());
        if (cityBranchCodes.size() > 0 ) {

            // 获取经营主体编号
            BusinessEntityExample businessEntityExample = new BusinessEntityExample();
            businessEntityExample.createCriteria().andCityBranchCodeIn(cityBranchCodes)
                    .andIsDelEqualTo(OneTrue.YesOrNo.NO.shortVal())
                    .andIsEnableEqualTo(UserEnabled.Enabled_true.code.shortValue());
            List<BusinessEntity> businessEntities = businessEntityMapper.selectByExample(businessEntityExample);
            List<String> businessEntityCodes= businessEntities.stream().filter(e->StringUtils.isNotBlank(e.getBusinessEntityCode())).map(e->e.getBusinessEntityCode()).collect(Collectors.toList());
            StoreInfoExample storeInfoExample = new StoreInfoExample();
            if (businessEntityCodes.size() > 0) {

                // 获取门店
                storeInfoExample.createCriteria().andBusinessEntityCodeIn(businessEntityCodes);
                return storeInfoMapper.selectByCityBranchCode(storeInfoExample);
            }
        }
        return new ArrayList<>();
    }

    @Override
    public StoreInfo storeInfoById(String id) {

        StoreInfoExample storeInfoExample = new StoreInfoExample();
        storeInfoExample.createCriteria().andStoreIdEqualTo(id);

        List<StoreInfo> storeInfos = storeInfoMapper.selectByExample(storeInfoExample);
        StoreInfo storeInfo = new StoreInfo();
        if (storeInfos.size()>0) {
            storeInfo = storeInfos.get(0);
        }
        return storeInfo;
    }

    @Override
    public List<HrOrganizationEntity> getHrOrganizationEntity() {

        HrOrganizationEntityExample hrOrganizationEntityExample = new HrOrganizationEntityExample();
        return hrOrganizationEntityMapper.selectByExample(hrOrganizationEntityExample);
    }
}
