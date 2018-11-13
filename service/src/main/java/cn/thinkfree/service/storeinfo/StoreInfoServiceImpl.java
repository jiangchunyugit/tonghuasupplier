package cn.thinkfree.service.storeinfo;

import cn.thinkfree.database.mapper.HrOrganizationEntityMapper;
import cn.thinkfree.database.mapper.StoreInfoMapper;
import cn.thinkfree.database.model.HrOrganizationEntity;
import cn.thinkfree.database.model.HrOrganizationEntityExample;
import cn.thinkfree.database.model.StoreInfo;
import cn.thinkfree.database.model.StoreInfoExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreInfoServiceImpl implements StoreInfoService {

    @Autowired
    StoreInfoMapper storeInfoMapper;

    @Autowired
    HrOrganizationEntityMapper hrOrganizationEntityMapper;

    @Override
    public List<StoreInfo> storeInfoListByCityId(Integer id) {

        StoreInfoExample storeInfoExample = new StoreInfoExample();
        storeInfoExample.createCriteria().andCityBranchIdEqualTo(id);
        return storeInfoMapper.selectByExample(storeInfoExample);
    }

    @Override
    public List<StoreInfo> storeInfoListByCompanyId(Integer id) {

        StoreInfoExample storeInfoExample = new StoreInfoExample();
        storeInfoExample.createCriteria().andBranchCompanyIdEqualTo(id);
        return storeInfoMapper.selectByExample(storeInfoExample);
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
