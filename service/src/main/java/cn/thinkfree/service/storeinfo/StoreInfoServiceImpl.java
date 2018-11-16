package cn.thinkfree.service.storeinfo;

import cn.thinkfree.database.constants.UserEnabled;
import cn.thinkfree.database.mapper.BusinessEntityMapper;
import cn.thinkfree.database.mapper.CityBranchMapper;
import cn.thinkfree.database.mapper.HrOrganizationEntityMapper;
import cn.thinkfree.database.mapper.StoreInfoMapper;
import cn.thinkfree.database.model.*;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

        BusinessEntityExample businessEntityExample = new BusinessEntityExample();
        businessEntityExample.createCriteria().andCityBranchCodeEqualTo(cityCode)
                .andIsEnableEqualTo(UserEnabled.Enabled_true.code.shortValue());
        List<BusinessEntity> businessEntities = businessEntityMapper.selectByExample(businessEntityExample);
        List<String> businessEntityCodes= businessEntities.stream().filter(e->StringUtils.isNotBlank(e.getBusinessEntityCode())).map(e->e.getBusinessEntityCode()).collect(Collectors.toList());
        StoreInfoExample storeInfoExample = new StoreInfoExample();
        if (businessEntityCodes.size() > 0) {
            storeInfoExample.createCriteria().andBusinessEntityCodeIn(businessEntityCodes);
            return storeInfoMapper.selectByCityBranchCode(storeInfoExample);
        }
        return new ArrayList<>();
    }

    @Override
    public List<StoreInfo> storeInfoListByCompanyId(String branchCompanyCode) {

        CityBranchExample cityBranchExample = new CityBranchExample();
        cityBranchExample.createCriteria().andBranchCompanyCodeEqualTo(branchCompanyCode);
        List<CityBranch> cityBranches = cityBranchMapper.selectByExample(cityBranchExample);
        List<String> cityBranchCodes= cityBranches.stream().filter(e->StringUtils.isNotBlank(e.getCityBranchCode())).map(e->e.getCityBranchCode()).collect(Collectors.toList());
        if (cityBranchCodes.size() > 0 ) {
            BusinessEntityExample businessEntityExample = new BusinessEntityExample();
            businessEntityExample.createCriteria().andCityBranchCodeIn(cityBranchCodes)
                    .andIsEnableEqualTo(UserEnabled.Enabled_true.code.shortValue());
            List<BusinessEntity> businessEntities = businessEntityMapper.selectByExample(businessEntityExample);
            List<String> businessEntityCodes= businessEntities.stream().filter(e->StringUtils.isNotBlank(e.getBusinessEntityCode())).map(e->e.getBusinessEntityCode()).collect(Collectors.toList());
            StoreInfoExample storeInfoExample = new StoreInfoExample();
            if (businessEntityCodes.size() > 0) {
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

        PageHelper.startPage(1,40);
        return hrOrganizationEntityMapper.selectByExample(hrOrganizationEntityExample);
    }
}
