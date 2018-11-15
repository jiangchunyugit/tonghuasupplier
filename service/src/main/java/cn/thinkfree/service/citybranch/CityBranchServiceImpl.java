package cn.thinkfree.service.citybranch;

import cn.thinkfree.core.utils.SpringBeanUtil;
import cn.thinkfree.database.constants.OneTrue;
import cn.thinkfree.database.constants.UserEnabled;
import cn.thinkfree.database.mapper.*;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.vo.CityBranchSEO;
import cn.thinkfree.database.vo.CityBranchVO;
import cn.thinkfree.database.vo.CityBranchWtihProCitVO;
import cn.thinkfree.service.utils.AccountHelper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jiangchunyu(后台)
 * @date 2018
 * @Description 城市分站
 */
@Service
public class CityBranchServiceImpl implements CityBranchService {

    @Autowired
    CityBranchMapper cityBranchMapper;

    @Autowired
    StoreInfoMapper storeInfoMapper;

    @Autowired
    BranchCompanyMapper branchCompanyMapper;

    @Autowired
    BusinessEntityMapper businessEntityMapper;

    @Autowired
    CityMapper cityMapper;
    @Override
    public int addCityBranch(CityBranch cityBranch) {

        // 城市分站保存
        cityBranch.setCreateTime(new Date());
        cityBranch.setIsDel(OneTrue.YesOrNo.NO.shortVal());
        cityBranch.setIsEnable(UserEnabled.Enabled_false.shortVal());
        cityBranch.setCityBranchCode(AccountHelper.createUserNo("c"));

        // 通过分公司获取省份code
        BranchCompany branchCompany = new BranchCompany();
        BranchCompanyExample branchCompanyExample = new BranchCompanyExample();
        branchCompanyExample.createCriteria().andBranchCompanyCodeEqualTo(cityBranch.getBranchCompanyCode());
//        branchCompany = branchCompanyMapper.selectByPrimaryKey(cityBranch.getBranchCompId());
        List<BranchCompany> branchCompanys = branchCompanyMapper.selectByExample(branchCompanyExample);
        if (branchCompanys.size()>0) {
            branchCompany = branchCompanys.get(0);
        }
        if (branchCompany != null) {
            cityBranch.setProvinceCode(branchCompany.getProvinceCode());
        }
        int result = cityBranchMapper.insertSelective(cityBranch);

//        // 回刷经营主体,保存店面信息
//        BusinessEntity businessEntity = new BusinessEntity();
//        businessEntity.setBranchCompId(cityBranch.getBranchCompId());
//        businessEntity.setCityBranchId(cityBranch.getId());
//        cityBranchVO.getStoreInfoVOList().forEach(e->{
//            businessEntity.setId(e.getBusinessEntityId());
//            businessEntityMapper.updateByPrimaryKeySelective(businessEntity);
//            e.setCityBranchId(cityBranch.getId());
//            StoreInfo storeInfo = new StoreInfo();
//            SpringBeanUtil.copy(e,storeInfo);
//            storeInfoMapper.insertSelective(storeInfo);
//        });
        return result;
    }

    @Override
    public int updateCityBranch(CityBranch cityBranch) {
        // 城市分站更新
//        CityBranch cityBranch = new CityBranch();
//        SpringBeanUtil.copy(cityBranchVO,cityBranch);

        int result = cityBranchMapper.updateByPrimaryKeySelective(cityBranch);
//        cityBranchVO.getStoreInfoVOList().forEach(e->{
//
//            e.setCityBranchId(cityBranch.getId());
//            storeInfoMapper.updateByPrimaryKeySelective(e);
//        });
        return result;
    }

    @Override
    public CityBranchVO cityBranchDetails(Integer id) {
        CityBranchVO cityBranchVO =  this.cityBranchById(id);

        if (null != cityBranchVO && StringUtils.isNotBlank(cityBranchVO.getCityBranchCode())) {
            cityBranchVO.setBusinessEntityVOS(businessEntityMapper.selectWithCityBranchCode(cityBranchVO.getCityBranchCode()));
        }
//        PcUserInfoExample pcUserInfoExample = new PcUserInfoExample();
//        pcUserInfoExample.createCriteria().andCityBranchCompanyIdEqualTo(cityBranchVO.getId().toString());
//        List<PcUserInfo> pcUserInfoList = pcUserInfoMapper.selectByExample(pcUserInfoExample);
//        cityBranchVO.setPcUserInfoList(pcUserInfoList);
        return cityBranchVO;
    }

    @Override
    public PageInfo<CityBranchVO> cityBranchList(CityBranchSEO cityBranchSEO) {

        CityBranchExample cityBranchExample = new CityBranchExample();
        CityBranchExample.Criteria criteria = cityBranchExample.createCriteria();
        criteria.andIsDelNotEqualTo(OneTrue.YesOrNo.YES.shortVal());
        // 负责人姓名电话不为空拼接模糊查询条件
        if(StringUtils.isNotBlank(cityBranchSEO.getLegalName())) {
            String condition = "%"+cityBranchSEO.getLegalName()+"%";
            cityBranchSEO.setLegalName(condition);
        }
        PageHelper.startPage(cityBranchSEO.getPage(),cityBranchSEO.getRows());
        List<CityBranchVO> cityBranchList = cityBranchMapper.selectBranchCompanyByParam(cityBranchSEO);
        return new PageInfo<>(cityBranchList);
    }

    @Override
    public PageInfo<CityBranchWtihProCitVO> cityBranchWithProList(CityBranchSEO cityBranchSEO) {

        PageHelper.startPage(cityBranchSEO.getPage(),cityBranchSEO.getRows());
        List<CityBranchWtihProCitVO> cityBranchWtihProCitVOList = cityBranchMapper.selectCityBranchWithProCit(cityBranchSEO.getBranchCompanyCode());
        return new PageInfo<>(cityBranchWtihProCitVOList);
    }

    @Override
    public List<CityBranch> cityBranchs() {

        CityBranchExample cityBranchExample = new CityBranchExample();
        CityBranchExample.Criteria criteria = cityBranchExample.createCriteria();
        criteria.andIsDelNotEqualTo(OneTrue.YesOrNo.YES.shortVal());
        return cityBranchMapper.selectByExample(cityBranchExample);
    }

    @Override
    public int updateCityBranchStatus(CityBranch cityBranch) {
        return cityBranchMapper.updateByPrimaryKeySelective(cityBranch);
    }

    @Override
    public List<CityBranch> selectByProCit(Integer province, Integer city) {

        CityBranchExample cityBranchExample = new CityBranchExample();
        CityBranchExample.Criteria criteria = cityBranchExample.createCriteria();
        if (province != null) {

            criteria.andProvinceCodeEqualTo(province.shortValue());
        }
        if (city != null) {

            criteria.andCityCodeEqualTo(city.shortValue());
        }
        return cityBranchMapper.selectByExample(cityBranchExample);
    }

    @Override
    public List<City> selectCity() {

        CityBranchExample cityBranchExample = new CityBranchExample();

        List<CityBranch> cityBranchList = cityBranchMapper.selectByExample(cityBranchExample);
        if (cityBranchList.size() >0 ) {

            List<String> cityCodes = cityBranchList.stream().filter(e->e.getCityCode() != null).map(e->e.getCityCode().toString()).collect(Collectors.toList());

            if (cityCodes.size() == 0) {
                return new ArrayList();
            }
            CityExample cityExample = new CityExample();
            cityExample.createCriteria().andCityCodeIn(cityCodes);
            List<City> cityList = cityMapper.selectByExample(cityExample);

            return cityList;
        }
        return new ArrayList<>();
    }

    @Override
    public CityBranchVO cityBranchById(Integer id) {
        return cityBranchMapper.selectBranchDetails(id);
    }

    @Override
    public List<CityBranch> cityBranchlistByCompany(Integer id) {

        BranchCompanyExample branchCompanyExample= new BranchCompanyExample();
        branchCompanyExample.createCriteria().andIdEqualTo(id);
        BranchCompany branchCompany = branchCompanyMapper.selectByPrimaryKey(id);
        if (branchCompany != null) {

            CityBranchExample cityBranchExample = new CityBranchExample();
            cityBranchExample.createCriteria().andBranchCompanyCodeEqualTo(branchCompany.getBranchCompanyCode())
            .andIsDelEqualTo(OneTrue.YesOrNo.NO.shortVal());
            return cityBranchMapper.selectByExample(cityBranchExample);
        }
        return null;
    }

    @Override
    public List<CityBranch> cityBranchlistByCompanyCode(String cityCode) {
        CityBranchExample cityBranchExample = new CityBranchExample();
        cityBranchExample.createCriteria().andBranchCompanyCodeEqualTo(cityCode)
                .andIsDelEqualTo(OneTrue.YesOrNo.NO.shortVal());
        return cityBranchMapper.selectByExample(cityBranchExample);
    }
}
