package cn.thinkfree.service.businessentity;

import cn.thinkfree.database.constants.OneTrue;
import cn.thinkfree.database.constants.UserEnabled;
import cn.thinkfree.database.mapper.*;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.vo.*;
import cn.thinkfree.service.utils.AccountHelper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author jiangchunyu(后台)
 * @date 2018
 * @Description 经营主体
 */
@Service
public class BusinessEntityServiceImpl implements BusinessEntityService {

    @Autowired
    BusinessEntityMapper businessEntityMapper;

    @Autowired
    CityBranchMapper cityBranchMapper;

    @Autowired
    StoreInfoMapper storeInfoMapper;

    @Autowired
    HrOrganizationEntityMapper hrOrganizationEntityMapper;

    @Autowired
    BusinessEntityRelationMapper businessEntityRelationMapper;

    @Autowired
    BusinessEntityStoreMapper businessEntityStoreMapper;

    /**
     * 判断查询还是创建
     */
    private final int SearchFlag = 1;

    /**
     * list非空判断
     */
    private final int FlagZero = 0;

    @Override
    public boolean checkRepeat(BusinessEntity businessEntity) {
        if (StringUtils.isNotBlank(businessEntity.getEntityName())) {
            BusinessEntityExample businessEntityExample = new BusinessEntityExample();
            BusinessEntityExample.Criteria criteria = businessEntityExample.createCriteria();
            criteria.andEntityNameEqualTo(businessEntity.getEntityName());
            if (businessEntity.getId() != null) {
                criteria.andIdNotEqualTo(businessEntity.getId());
            }
            return businessEntityMapper.countByExample(businessEntityExample) >FlagZero?true:false;
        }
        return false;
    }

    @Override
    public boolean addBusinessEntity(BusinessEntity businessEntity) {

        // 新增经营主体
        businessEntity.setCreateTime(new Date());
        businessEntity.setIsDel(OneTrue.YesOrNo.NO.shortVal());
        businessEntity.setIsEnable(UserEnabled.Enabled_false.shortVal());
        businessEntity.setBusinessEntityCode(AccountHelper.createUserNo("e"));
        return businessEntityMapper.insertSelective(businessEntity)>FlagZero?true:false;
    }

    @Override
    public boolean updateBusinessEntity(BusinessEntity businessEntity) {

        return businessEntityMapper.updateByPrimaryKeySelective(businessEntity)>FlagZero?true:false;
    }

    @Override
    public BusinessEntityVO businessEntityDetails(Integer id) {

        // 经营主体信息
        BusinessEntityVO businessEntityVO = businessEntityMapper.selectWithId(id);
        Optional.ofNullable(businessEntityVO).map(u->u.getBusinessEntityCode()).ifPresent(e->{
            businessEntityVO.setBusinessEntityRelationVOS(businessEntityRelationMapper.selectBusinessEntityRelationVO(e));
        });
        return businessEntityVO;
    }

    @Override
    public BusinessEntityStoreVO businessEntityStoreDetails(Integer id) {
        BusinessEntity businessEntity = businessEntityMapper.selectByPrimaryKey(id);
        BusinessEntityStoreVO businessEntityStoreVO = new BusinessEntityStoreVO();
        Optional.ofNullable(businessEntity).map(u->u.getBusinessEntityCode()).ifPresent((String u) ->{
            if (this.storeCount(u)) {
                List<BusinessEntityRelationVO> businessEntityRelationVOList = businessEntityRelationMapper.selectBusinessEntityRelationVO(u);
                businessEntityStoreVO.setBusinessEntityRelationVOS(businessEntityRelationVOList);
            }
        });
        businessEntityStoreVO.setBusinessEntityCode(businessEntity.getBusinessEntityCode());
        return businessEntityStoreVO;
    }

    @Override
    public PageInfo<BusinessEntityVO> businessEntityList(BusinessEntitySEO businessEntitySEO) {

        PageHelper.startPage(businessEntitySEO.getPage(),businessEntitySEO.getRows());
        List<BusinessEntityVO> businessEntityVOS = businessEntityMapper.selectWithCompany(businessEntitySEO);
        return new PageInfo<>(businessEntityVOS);
    }

    @Override
    public List<BusinessEntity> businessEntices() {

        BusinessEntityExample businessEntityExample = new BusinessEntityExample();
        BusinessEntityExample.Criteria criteria = businessEntityExample.createCriteria();
        criteria.andIsDelNotEqualTo(OneTrue.YesOrNo.YES.shortVal());
        return businessEntityMapper.selectByExample(businessEntityExample);
    }

    @Override
    public BusinessEntity businessEntityById(Integer id) {
        return businessEntityMapper.selectByPrimaryKey(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean insertBusinessEntityStore(BusinessEntityStoreVO businessEntityStoreVO) {
        if (businessEntityStoreVO != null) {
            // 查找主体信息冗余表中
            List<BusinessEntity> businessEntitys = new ArrayList<>();
            BusinessEntity businessEntity = new BusinessEntity();
            BusinessEntityExample businessEntityExample = new BusinessEntityExample();
            businessEntityExample.createCriteria().andBusinessEntityCodeEqualTo(businessEntityStoreVO.getBusinessEntityCode());
            businessEntitys = businessEntityMapper.selectByExample(businessEntityExample);
            if (businessEntitys.size() > FlagZero) {
                businessEntity = businessEntitys.get(0);
            } else {
                return false;
            }
            // 删除原有门店信息
            if (StringUtils.isNotBlank(businessEntityStoreVO.getBusinessEntityCode())) {
                this.deleteStores(businessEntityStoreVO.getBusinessEntityCode());
            }

            int flag =0;
            // 新增门店信息
            if (businessEntityStoreVO.getBusinessEntityRelationVOS() != null) {
                for (BusinessEntityRelationVO e:businessEntityStoreVO.getBusinessEntityRelationVOS()) {
                    if (e.getBusinessEntityStoreList()!= null && e.getBusinessEntityStoreList().size()>FlagZero){
                        e.setBusinessEntityCode(businessEntityStoreVO.getBusinessEntityCode());
                        businessEntityRelationMapper.insertSelective(e);
                        for (BusinessEntityStore s:e.getBusinessEntityStoreList()){
                            flag = 1;
                            s.setBusinessEntityRelationId(e.getId());
                            s.setBusinessEntityCode(e.getBusinessEntityCode());
                            s.setEntityName(businessEntity.getEntityName());
                            s.setCityBranchCode(e.getCityBranchCode());
                            businessEntityStoreMapper.insertSelective(s);
                        }
                    }
                }
                if (flag == 1) {
                    businessEntity.setBranchCompanyCode(businessEntityStoreVO.getBusinessEntityRelationVOS().get(0).getBranchCompanyCode());
                    businessEntityMapper.updateByExampleSelective(businessEntity,businessEntityExample);
                } else {
                    businessEntity.setBranchCompanyCode("");
                    businessEntityMapper.updateByExample(businessEntity,businessEntityExample);
                }
                return true;
            }
       }
        return false;
    }

    /**
     * 删除店面信息
     * @param businessEntityCode
     */
    private void deleteStores(String businessEntityCode) {
        if (this.storeCount(businessEntityCode)) {
            BusinessEntityRelationExample businessEntityRelationExample = new BusinessEntityRelationExample();
            businessEntityRelationExample.createCriteria().andBusinessEntityCodeEqualTo(businessEntityCode);
            businessEntityRelationMapper.deleteByExample(businessEntityRelationExample);

            BusinessEntityStoreExample businessEntityStoreExample = new BusinessEntityStoreExample();
            businessEntityStoreExample.createCriteria().andBusinessEntityCodeEqualTo(businessEntityCode);
            businessEntityStoreMapper.deleteByExample(businessEntityStoreExample);
        }
    }

    /**
     * 判断初选门店
     * @param businessEntityCode
     * @return
     */
    private boolean storeCount(String businessEntityCode) {
        BusinessEntityRelationExample businessEntityRelationExample = new BusinessEntityRelationExample();
        businessEntityRelationExample.createCriteria().andBusinessEntityCodeEqualTo(businessEntityCode);
        return businessEntityRelationMapper.countByExample(businessEntityRelationExample)>FlagZero?true:false;
    }

    @Override
    public boolean deleteBusinessEntity(BusinessEntity businessEntity) {

        boolean result = this.updateBusinessEntity(businessEntity);
        if (result) {
            if (businessEntity.getId() != null) {
                Optional.ofNullable(businessEntityMapper.selectByPrimaryKey(businessEntity.getId())).map(u->u.getBusinessEntityCode()).ifPresent(e->{
                    this.deleteStores(e);
                });
            }
        }
        return result;
    }
}
