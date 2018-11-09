package cn.thinkfree.service.storeinfo;

import cn.thinkfree.database.mapper.StoreInfoMapper;
import cn.thinkfree.database.model.StoreInfo;
import cn.thinkfree.database.model.StoreInfoExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreInfoServiceImpl implements StoreInfoService {

    @Autowired
    StoreInfoMapper storeInfoMapper;

    @Override
    public List<StoreInfo> storeInfoListByCityId(Integer id) {

        StoreInfoExample storeInfoExample = new StoreInfoExample();
        storeInfoExample.createCriteria().andCityBranchIdEqualTo(id);
        return storeInfoMapper.selectByExample(storeInfoExample);
    }

    @Override
    public StoreInfo storeInfoById(Integer id) {
        return storeInfoMapper.selectByPrimaryKey(id);
    }
}
