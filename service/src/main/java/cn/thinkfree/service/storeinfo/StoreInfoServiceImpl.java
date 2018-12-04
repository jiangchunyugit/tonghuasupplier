package cn.thinkfree.service.storeinfo;

import cn.thinkfree.database.constants.OneTrue;
import cn.thinkfree.database.constants.UserEnabled;
import cn.thinkfree.database.mapper.*;
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

    @Autowired
    BusinessEntityStoreMapper businessEntityStoreMapper;

    @Autowired
    BranchCompanyMapper branchCompanyMapper;

    @Override
    public List<StoreInfo> storeInfoListByCityId(String cityCode) {

        StoreInfoExample storeInfoExample = new StoreInfoExample();
        storeInfoExample.createCriteria().andCityBranchCodeEqualTo(cityCode);
        return storeInfoMapper.selectByCityBranchCode(storeInfoExample);
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
        StoreInfoExample storeInfoExample = new StoreInfoExample();
        if (cityBranchCodes.size() > 0) {

            // 获取门店
            storeInfoExample.createCriteria().andCityBranchCodeIn(cityBranchCodes);
            return storeInfoMapper.selectByCityBranchCode(storeInfoExample);
        }
        return new ArrayList<>();
    }

    @Override
    public List<HrOrganizationEntity> getHrOrganizationEntity() {

        HrOrganizationEntityExample hrOrganizationEntityExample = new HrOrganizationEntityExample();
        hrOrganizationEntityExample.createCriteria().andZbizType2EqualTo("04");
        return hrOrganizationEntityMapper.selectByExample(hrOrganizationEntityExample);
    }

    @Override
    public List<HrOrganizationEntity> storeInfoListByEbsCompanyId(String organizationId,String cityBranchCode) {
        BranchCompanyExample branchCompanyExample = new BranchCompanyExample();
        branchCompanyExample.createCriteria().andBranchCompEbsidEqualTo(organizationId);
        List<BranchCompany> branchCompanies = branchCompanyMapper.selectByExample(branchCompanyExample);
        List<String> storeIds= new ArrayList<>();
        if (branchCompanies.size()>0) {
            StoreInfoExample storeInfoExample = new StoreInfoExample();
            StoreInfoExample.Criteria criteria = storeInfoExample.createCriteria();
            if (StringUtils.isNotBlank(branchCompanies.get(0).getBranchCompanyCode())) {

                if (StringUtils.isNotBlank(cityBranchCode)) {
                    criteria.andCityBranchCodeNotEqualTo(cityBranchCode);
                }
                criteria.andBranchCompanyCodeEqualTo(branchCompanies.get(0).getBranchCompanyCode());
                List<StoreInfo> storeInfos = storeInfoMapper.selectByExample(storeInfoExample);
                if (storeInfos.size()>0) {
                    storeIds = storeInfos.stream().map(e->e.getStoreId()).collect(Collectors.toList());
                }
            }
        }
        HrOrganizationEntityExample hrOrganizationEntityExample = new HrOrganizationEntityExample();
        HrOrganizationEntityExample.Criteria criteria = hrOrganizationEntityExample.createCriteria();
        criteria.andSuperiorOrganizationIdEqualTo(organizationId);
        criteria.andZbizType2EqualTo("04");
        if (storeIds.size() >0) {
            criteria.andOrganizationIdNotIn(storeIds);
        }
        return hrOrganizationEntityMapper.selectByExample(hrOrganizationEntityExample);
    }

    @Override
    public List<StoreInfo> businessEntityStoreByCityBranchCode(String cityBranchCode,String businessEntityCode) {

        BusinessEntityStoreExample businessEntityStoreExample = new BusinessEntityStoreExample();
        businessEntityStoreExample.createCriteria().andCityBranchCodeEqualTo(cityBranchCode)
                .andBusinessEntityCodeNotEqualTo(businessEntityCode);
        List<BusinessEntityStore> businessEntityStores = businessEntityStoreMapper.selectByExample(businessEntityStoreExample);
        List<String> storeIdList = businessEntityStores.stream().map(e->e.getStoreId()).collect(Collectors.toList());
        StoreInfoExample storeInfoExample = new StoreInfoExample();
        StoreInfoExample.Criteria criteria= storeInfoExample.createCriteria();
        criteria.andCityBranchCodeEqualTo(cityBranchCode);
        if (storeIdList.size()>0) {
            criteria.andStoreIdNotIn(storeIdList);
        }
        return storeInfoMapper.selectByCityBranchCode(storeInfoExample);
    }
}
