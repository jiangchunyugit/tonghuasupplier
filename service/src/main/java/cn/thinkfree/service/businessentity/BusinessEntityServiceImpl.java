package cn.thinkfree.service.businessentity;

import cn.thinkfree.core.utils.SpringBeanUtil;
import cn.thinkfree.database.constants.OneTrue;
import cn.thinkfree.database.constants.UserEnabled;
import cn.thinkfree.database.mapper.BusinessEntityMapper;
import cn.thinkfree.database.mapper.CityBranchMapper;
import cn.thinkfree.database.mapper.HrOrganizationEntityMapper;
import cn.thinkfree.database.mapper.StoreInfoMapper;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.vo.BusinessEntitySEO;
import cn.thinkfree.database.vo.BusinessEntityVO;
import cn.thinkfree.service.utils.AccountHelper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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
        businessEntity.setBusinessEntityCode(AccountHelper.createUserNo("e"));

        int result = businessEntityMapper.insertSelective(businessEntity);
        if (businessEntityVO.getStoreInfoList()!= null && businessEntityVO.getStoreInfoList().size()>0) {
            this.insertStoreInfo(businessEntityVO.getStoreInfoList(),businessEntity);
        }
        return result;
    }

    private void insertStoreInfo(List<StoreInfo> storeInfos,BusinessEntity businessEntity) {

        storeInfos.forEach(e->{
            e.setCityBranchCode(businessEntity.getCityBranchCode());
            e.setBranchCompanyCode(businessEntity.getBranchCompanyCode());
            e.setBusinessEntityCode(businessEntity.getBusinessEntityCode());
            storeInfoMapper.insertSelective(e);
        });
    }

    @Override
    public int updateBusinessEntity(BusinessEntityVO businessEntityVO) {

        StoreInfoExample storeInfoExample= new StoreInfoExample();
        if (StringUtils.isNotBlank(businessEntityVO.getBusinessEntityCode())) {

            storeInfoExample.createCriteria().andBusinessEntityCodeEqualTo(businessEntityVO.getBusinessEntityCode());
            storeInfoMapper.deleteByExample(storeInfoExample);
        }
        BusinessEntity businessEntity = new BusinessEntity();
        SpringBeanUtil.copy(businessEntityVO,businessEntity);
        if (businessEntityVO.getStoreInfoList() != null) {
            this.insertStoreInfo(businessEntityVO.getStoreInfoList(),businessEntity);
        }
        return businessEntityMapper.updateByPrimaryKeySelective(businessEntity);
    }

    @Override
    public int enableBusinessEntity(BusinessEntityVO businessEntityVO) {

        BusinessEntity businessEntity = new BusinessEntity();
        SpringBeanUtil.copy(businessEntityVO,businessEntity);
        return businessEntityMapper.updateByPrimaryKeySelective(businessEntity);
    }

    @Autowired
    HrOrganizationEntityMapper hrOrganizationEntityMapper;

    @Override
    public BusinessEntityVO businessEntityDetails(Integer id) {

        // 经营主体信息
        List<BusinessEntityVO> businessEntityVOS = businessEntityMapper.selectWithId(id);

        // businessEntityVO
        BusinessEntityVO businessEntityVO = new BusinessEntityVO();
        if (businessEntityVOS.size()>0) {
            businessEntityVO = businessEntityVOS.get(0);
        }
        return businessEntityVO;
    }

    @Override
    public PageInfo<BusinessEntityVO> businessEntityList(BusinessEntitySEO businessEntitySEO) {

        PageHelper.startPage(businessEntitySEO.getPage(),businessEntitySEO.getRows());
        List<BusinessEntityVO> businessEntityVOS = businessEntityMapper.selectWithCompany(businessEntitySEO);
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
