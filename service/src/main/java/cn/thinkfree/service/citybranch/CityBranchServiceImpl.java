package cn.thinkfree.service.citybranch;

import cn.thinkfree.core.security.filter.util.SessionUserDetailsUtil;
import cn.thinkfree.database.constants.OneTrue;
import cn.thinkfree.database.constants.UserEnabled;
import cn.thinkfree.database.constants.UserLevel;
import cn.thinkfree.database.mapper.*;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.vo.CityBranchSEO;
import cn.thinkfree.database.vo.CityBranchVO;
import cn.thinkfree.database.vo.UserVO;
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

    /**
     * 判断查询还是创建
     */
    private final int SearchFlag = 1;

    /**
     * list非空判断
     */
    private final int FlagZero = 0;

    @Override
    public boolean checkRepeat(CityBranchVO cityBranchVO) {
        if (StringUtils.isNotBlank(cityBranchVO.getCityBranchName())) {
            CityBranchExample cityBranchExample = new CityBranchExample();
            CityBranchExample.Criteria criteria = cityBranchExample.createCriteria();
            criteria.andCityBranchNameEqualTo(cityBranchVO.getCityBranchName());
            if (cityBranchVO.getId() != null) {
                criteria.andIdNotEqualTo(cityBranchVO.getId());
            }
            return cityBranchMapper.countByExample(cityBranchExample) >FlagZero?true:false;
        }
        return false;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean addCityBranch(CityBranchVO cityBranchVO) {

        // 城市分站保存
        cityBranchVO.setCreateTime(new Date());
        cityBranchVO.setIsDel(OneTrue.YesOrNo.NO.shortVal());
        cityBranchVO.setIsEnable(UserEnabled.Enabled_false.shortVal());
        cityBranchVO.setCityBranchCode(AccountHelper.createUserNo("c"));

        boolean result = cityBranchMapper.insertSelective(cityBranchVO)>FlagZero?true:false;
        // 循环保存门店信息
        if (result) {
            this.branchCompanyProvinceInsert(cityBranchVO);
        }
        return result;
    }

    private void branchCompanyProvinceInsert (CityBranchVO cityBranchVO) {

        Optional.ofNullable(cityBranchVO).map(CityBranchVO::getStoreInfoVOList).ifPresent(u->{
            u.forEach(e->{
                e.setBranchCompanyCode(cityBranchVO.getBranchCompanyCode());
                e.setCityBranchCode(cityBranchVO.getCityBranchCode());
                storeInfoMapper.insertSelective(e);
            });
        });
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateCityBranch(CityBranchVO cityBranchVO) {

        // 删除原来城市分站数据
        Optional.ofNullable(cityBranchVO).map(b->b.getId()).ifPresent(u->{
            CityBranch cityBranch = cityBranchMapper.selectByPrimaryKey(u);
            Optional.ofNullable(cityBranch).map(c->c.getCityBranchCode()).ifPresent(d->{
                this.deleteStores(d);
            });
        });
        this.branchCompanyProvinceInsert(cityBranchVO);
        return cityBranchMapper.updateByPrimaryKeySelective(cityBranchVO)>FlagZero?true:false;
    }

    @Override
    public boolean enableCityBranch(CityBranch cityBranch) {

        return cityBranchMapper.updateByPrimaryKeySelective(cityBranch)>FlagZero?true:false;
    }

    private void deleteStores(String cityBranchCode) {
        StoreInfoExample storeInfoExample= new StoreInfoExample();
        storeInfoExample.createCriteria().andCityBranchCodeEqualTo(cityBranchCode);
        if (storeInfoMapper.countByExample(storeInfoExample) >0) {
            storeInfoMapper.deleteByExample(storeInfoExample);
        }
    }

    @Override
    public boolean deleteCityBranch(CityBranch cityBranch) {

        boolean result = this.enableCityBranch(cityBranch);
        if (result) {
            if (cityBranch.getId() != null) {
                Optional.ofNullable(cityBranchMapper.selectByPrimaryKey(cityBranch.getId())).map(u->u.getCityBranchCode()).ifPresent(e->{
                    this.deleteStores(e);
                });
            }
        }
        return result;
    }

    @Override
    public CityBranchVO cityBranchDetails(Integer id) {

        return cityBranchMapper.selectCityBranchById(id);
    }

    @Override
    public PageInfo<CityBranchVO> cityBranchList(CityBranchSEO cityBranchSEO) {

        // 负责人姓名电话不为空拼接模糊查询条件
        if(StringUtils.isNotBlank(cityBranchSEO.getLegalName())) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("%");
            stringBuffer.append(cityBranchSEO.getLegalName());
            stringBuffer.append("%");
            cityBranchSEO.setLegalName(stringBuffer.toString());
        }
        PageHelper.startPage(cityBranchSEO.getPage(),cityBranchSEO.getRows());
        List<CityBranchVO> cityBranchList = cityBranchMapper.selectBranchCompanyByParam(cityBranchSEO);
        return new PageInfo<>(cityBranchList);
    }

    @Override
    public List<CityBranch> selectByProCit(String branchCompanyCode) {

        // 分公司查询条件
        CityBranchExample cityBranchExample = new CityBranchExample();
        CityBranchExample.Criteria criteria = cityBranchExample.createCriteria();
        criteria.andIsEnableEqualTo(UserEnabled.Enabled_true.code.shortValue());
        criteria.andIsDelEqualTo(OneTrue.YesOrNo.NO.shortVal());
        UserVO userVO = (UserVO) SessionUserDetailsUtil.getUserDetails();
        if (userVO != null && userVO.getPcUserInfo() != null && userVO.getPcUserInfo().getLevel() != null) {
            // 权限等级
            short level = userVO.getPcUserInfo().getLevel();

            // 市账号
            if (UserLevel.Company_City.code == level && userVO.getCityBranch() != null
                    && StringUtils.isNotBlank(userVO.getCityBranch().getCityBranchCode())) {
                criteria.andCityBranchCodeEqualTo(userVO.getCityBranch().getCityBranchCode());
            }
        }

        // 分公司编码
        if (StringUtils.isNotBlank(branchCompanyCode)) {
            criteria.andBranchCompanyCodeEqualTo(branchCompanyCode);
        }

        return cityBranchMapper.selectByExample(cityBranchExample);
    }

    @Override
    public List<CityBranch> selectByProCitCode(String province, String city) {

        // 拼接地区条件
        CityBranchExample cityBranchExample = new CityBranchExample();
        CityBranchExample.Criteria criteria = cityBranchExample.createCriteria();
        criteria.andIsDelEqualTo(OneTrue.YesOrNo.NO.shortVal());
        criteria.andIsEnableEqualTo(UserEnabled.Enabled_true.code.shortValue());
        if (province != null) {

            criteria.andProvinceCodeEqualTo(province);
        }
        if (city != null) {

            criteria.andCityCodeEqualTo(city);
        }
        return cityBranchMapper.selectByExample(cityBranchExample);
    }

    @Override
    public List<City> selectCity() {

        CityBranchExample cityBranchExample = new CityBranchExample();
        cityBranchExample.createCriteria().andIsDelEqualTo(OneTrue.YesOrNo.NO.shortVal());
        List<CityBranch> cityBranchList = cityBranchMapper.selectByExample(cityBranchExample);
        if (cityBranchList.size() >0 ) {

            // 地区市编码集合
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
        return cityBranchMapper.selectCityBranchById(id);
    }

    @Override
    public List<CityBranch> cityBranchesByCompanyCode(Integer flag,String branchCompanyCode) {

        // 通过分公司编码获取城市分站（查询0 ，创建1）
        CityBranchExample cityBranchExample = new CityBranchExample();
        CityBranchExample.Criteria criteria = cityBranchExample.createCriteria();
        criteria.andBranchCompanyCodeEqualTo(branchCompanyCode)
                .andIsDelEqualTo(OneTrue.YesOrNo.NO.shortVal());
        if (flag==SearchFlag) {
            criteria.andIsEnableEqualTo(UserEnabled.Enabled_true.code.shortValue());
        }
        return cityBranchMapper.selectByExample(cityBranchExample);
    }
}
