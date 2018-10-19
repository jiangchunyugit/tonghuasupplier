package cn.thinkfree.service.businessentity;

import cn.thinkfree.core.utils.SpringBeanUtil;
import cn.thinkfree.database.constants.OneTrue;
import cn.thinkfree.database.constants.UserEnabled;
import cn.thinkfree.database.mapper.BusinessEntityMapper;
import cn.thinkfree.database.mapper.CityBranchMapper;
import cn.thinkfree.database.mapper.StoreInfoMapper;
import cn.thinkfree.database.model.BusinessEntity;
import cn.thinkfree.database.model.BusinessEntityExample;
import cn.thinkfree.database.model.StoreInfoExample;
import cn.thinkfree.database.vo.BusinessEntitySEO;
import cn.thinkfree.database.vo.BusinessEntityVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BusinessEntityServiceImpl implements BusinessEntityService {

    @Autowired
    BusinessEntityMapper businessEntityMapper;

    @Autowired
    CityBranchMapper cityBranchMapper;

    @Autowired
    StoreInfoMapper storeInfoMapper;

    @Override
    public int addBusinessEntity(BusinessEntity businessEntity) {
        businessEntity.setCreateTime(new Date());
        businessEntity.setIsDel(OneTrue.YesOrNo.NO.shortVal());
        businessEntity.setIsEnable(UserEnabled.Enabled_false.shortVal());
        return businessEntityMapper.insertSelective(businessEntity);
    }

    @Override
    public int updateBusinessEntity(BusinessEntity businessEntity) {
        return businessEntityMapper.updateByPrimaryKeySelective(businessEntity);
    }

    @Override
    public BusinessEntityVO businessEntityDetails(Integer id) {

        // 经营主体信息
        BusinessEntity businessEntity = new BusinessEntity();
        businessEntity = businessEntityMapper.selectByPrimaryKey(id);

        // todo  店面信息(接入店面信息接口)(自建店面表取店面主体)
        StoreInfoExample storeInfoExample = new StoreInfoExample();
        StoreInfoExample.Criteria criteria = storeInfoExample.createCriteria();
        criteria.andBusinessEntityIdEqualTo(id);
        List<String> storeList = storeInfoMapper.selectByExample(storeInfoExample).stream().map(e->e.getName()).collect(Collectors.toList());

        // businessEntityVO
        BusinessEntityVO businessEntityVO = new BusinessEntityVO();
        businessEntityVO.setStoreNm(storeList);
        SpringBeanUtil.copy(businessEntityVO,businessEntity);

        return businessEntityVO;
    }

    @Override
    public PageInfo<BusinessEntity> businessEntityList(BusinessEntitySEO businessEntitySEO) {

        BusinessEntityExample businessEntityExample = new BusinessEntityExample();
        BusinessEntityExample.Criteria criteria = businessEntityExample.createCriteria();
        criteria.andIsDelNotEqualTo(OneTrue.YesOrNo.YES.shortVal());

        // 经营主体名称查询条件（模糊查询）
        if(StringUtils.isNotBlank(businessEntitySEO.getBusinessEntityNm())) {
            criteria.andEntityNameLike(businessEntitySEO.getBusinessEntityNm());
        }

        // 分公司，城市分站，查询条件
        if(businessEntitySEO.getBranchCompanyEbsId() != null) {
            criteria.andBranchCompEbsidEqualTo(businessEntitySEO.getBranchCompanyEbsId());
        }
        if(businessEntitySEO.getCityBranchid() != null){
            criteria.andCityBranchEbsidEqualTo(businessEntitySEO.getCityBranchid());
        }

        // 启动状态
        if(businessEntitySEO.getIsEnable() != null) {
            criteria.andIsEnableEqualTo(businessEntitySEO.getIsEnable().shortValue());
        }

        PageHelper.startPage(businessEntitySEO.getPage(),businessEntitySEO.getRows());
        List<BusinessEntity> branchCompanies = businessEntityMapper.selectByExample(businessEntityExample);
        return new PageInfo<>(branchCompanies);
    }

    @Override
    public List<BusinessEntity> businessEntitys() {

        BusinessEntityExample businessEntityExample = new BusinessEntityExample();
        BusinessEntityExample.Criteria criteria = businessEntityExample.createCriteria();
        criteria.andIsDelNotEqualTo(OneTrue.YesOrNo.YES.shortVal());
        return businessEntityMapper.selectByExample(businessEntityExample);
    }

    @Override
    public BusinessEntity businessEntityById(Integer id) {
        return businessEntityMapper.selectByPrimaryKey(id);
    }
}
