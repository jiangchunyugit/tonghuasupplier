package cn.thinkfree.service.businessentity;

import cn.thinkfree.core.utils.SpringBeanUtil;
import cn.thinkfree.database.constants.OneTrue;
import cn.thinkfree.database.constants.UserEnabled;
import cn.thinkfree.database.mapper.BusinessEntityMapper;
import cn.thinkfree.database.mapper.CityBranchMapper;
import cn.thinkfree.database.mapper.StoreInfoMapper;
import cn.thinkfree.database.model.BusinessEntity;
import cn.thinkfree.database.model.BusinessEntityExample;
import cn.thinkfree.database.model.StoreInfo;
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
    public int addBusinessEntity(BusinessEntityVO businessEntityVO) {
        BusinessEntity businessEntity = new BusinessEntity();
        SpringBeanUtil.copy(businessEntityVO,businessEntity);
        businessEntity.setCreateTime(new Date());
        businessEntity.setIsDel(OneTrue.YesOrNo.NO.shortVal());
        businessEntity.setIsEnable(UserEnabled.Enabled_false.shortVal());

        int result = businessEntityMapper.insertSelective(businessEntity);
        if (businessEntityVO.getStoreInfoList().size()>0) {

            businessEntityVO.getStoreInfoList().forEach(e->{
                e.setCityBranchId(businessEntity.getCityBranchId());
                e.setBranchCompanyId(businessEntity.getBranchCompId());
                e.setBusinessEntityId(businessEntity.getId());
                storeInfoMapper.insertSelective(e);
            });
        }
        return result;
    }

    @Override
    public int updateBusinessEntity(BusinessEntityVO businessEntityVO) {

        StoreInfoExample storeInfoExample= new StoreInfoExample();
        storeInfoExample.createCriteria().andBusinessEntityIdEqualTo(businessEntityVO.getId());
        storeInfoMapper.deleteByExample(storeInfoExample);
        BusinessEntity businessEntity = new BusinessEntity();
        SpringBeanUtil.copy(businessEntityVO,businessEntity);
        if (businessEntityVO.getStoreInfoList().size()>0) {

            businessEntityVO.getStoreInfoList().forEach(e->{
                e.setCityBranchId(businessEntity.getCityBranchId());
                e.setBranchCompanyId(businessEntity.getBranchCompId());
                e.setBusinessEntityId(businessEntity.getId());
                storeInfoMapper.insertSelective(e);
            });
        }
        return businessEntityMapper.updateByPrimaryKeySelective(businessEntity);
    }

    @Override
    public BusinessEntityVO businessEntityDetails(Integer id) {

        // 经营主体信息
        BusinessEntityExample businessEntityExample = new BusinessEntityExample();
        businessEntityExample.createCriteria().andIdEqualTo(id);
        List<BusinessEntityVO> businessEntityVOS = businessEntityMapper.selectWithCompany(businessEntityExample);

        StoreInfoExample storeInfoExample = new StoreInfoExample();
        StoreInfoExample.Criteria criteria = storeInfoExample.createCriteria();
        criteria.andBusinessEntityIdEqualTo(id);
        List<StoreInfo> storeList = storeInfoMapper.selectByExample(storeInfoExample);

        // businessEntityVO
        BusinessEntityVO businessEntityVO = new BusinessEntityVO();

        if (businessEntityVOS.size()>0) {

            businessEntityVO = businessEntityVOS.get(0);
            businessEntityVO.setStoreInfoList(storeList);
        }
        return businessEntityVO;
    }

    @Override
    public PageInfo<BusinessEntityVO> businessEntityList(BusinessEntitySEO businessEntitySEO) {

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
        List<BusinessEntityVO> businessEntityVOS = businessEntityMapper.selectWithCompany(businessEntityExample);
        return new PageInfo<>(businessEntityVOS);
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
