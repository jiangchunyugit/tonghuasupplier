package cn.thinkfree.service.storeinfo;

import cn.thinkfree.database.mapper.HrOrganizationEntityMapper;
import cn.thinkfree.database.mapper.StoreInfoMapper;
import cn.thinkfree.database.model.HrOrganizationEntity;
import cn.thinkfree.database.model.HrOrganizationEntityExample;
import cn.thinkfree.database.model.StoreInfo;
import cn.thinkfree.database.model.StoreInfoExample;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StoreInfoServiceImpl implements StoreInfoService {

    @Autowired
    StoreInfoMapper storeInfoMapper;

    @Autowired
    HrOrganizationEntityMapper hrOrganizationEntityMapper;

    @Override
    public List<StoreInfo> storeInfoListByCityId(String cityCode) {

        StoreInfoExample storeInfoExample = new StoreInfoExample();
        if (StringUtils.isNotBlank(cityCode)) {
            storeInfoExample.createCriteria().andCityBranchCodeEqualTo(cityCode);
            return storeInfoMapper.selectByExample(storeInfoExample);
        }
        return new ArrayList<>();
    }

    @Override
    public List<StoreInfo> storeInfoListByCompanyId(String branchCompanyCode) {

        StoreInfoExample storeInfoExample = new StoreInfoExample();

        if (StringUtils.isNotBlank(branchCompanyCode)) {
            storeInfoExample.createCriteria().andBranchCompanyCodeEqualTo(branchCompanyCode);
            return storeInfoMapper.selectByExample(storeInfoExample);
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
