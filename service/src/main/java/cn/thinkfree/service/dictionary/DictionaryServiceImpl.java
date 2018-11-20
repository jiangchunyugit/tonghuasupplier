package cn.thinkfree.service.dictionary;

import cn.thinkfree.core.constants.SysConstants;
import cn.thinkfree.core.security.filter.util.SessionUserDetailsUtil;
import cn.thinkfree.database.constants.UserLevel;
import cn.thinkfree.database.mapper.*;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.vo.UserVO;
import cn.thinkfree.service.constants.CompanyConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DictionaryServiceImpl implements DictionaryService {


    @Autowired
    ProvinceMapper provinceMapper;

    @Autowired
    CityMapper cityMapper;

    @Autowired
    AreaMapper areaMapper;

    @Autowired
    PreProjectHouseTypeMapper preProjectHouseTypeMapper;

    @Autowired
    HousingStatusMapper housingStatusMapper;

    @Autowired
    ProjectTypeMapper projectTypeMapper;

    @Autowired
    CompanyInfoMapper companyInfoMapper;

    @Autowired
    UserRoleSetMapper userRoleSetMapper;

//    @Autowired
//    SystemResourceMapper systemResourceMapper;

    @Autowired
    HrPeopleEntityMapper hrPeopleEntityMapper;

    /**
     * 查询所有省份
     *
     * @return
     */
    @Override
    public List<Province> findAllProvince() {
        //根据登录人获取
        UserVO	userVO = (UserVO) SessionUserDetailsUtil.getUserDetails();
        if(userVO.getCompanyInfo().getCompanyClassify().equals(CompanyConstants.ClassClassify.JOINCOMPANY)){
            return provinceMapper.selectByExample(null);
        }else{
            Short level =userVO.getPcUserInfo().getLevel();
            if(level.equals(UserLevel.Company_Province.shortVal()) || level.equals(UserLevel.Company_City.shortVal())){
                ProvinceExample example = new ProvinceExample();
                example.createCriteria().andProvinceCodeEqualTo(userVO.getPcUserInfo().getProvince());
                return provinceMapper.selectByExample(example);
            }else{
                return provinceMapper.selectByExample(null);
            }
        }

    }

    /**
     * 根据省份查询市区
     *
     * @param province
     * @return
     */
    @Override
    public List<City> findCityByProvince(String province) {
        UserVO	userVO = (UserVO) SessionUserDetailsUtil.getUserDetails();
        if(userVO.getCompanyInfo().getCompanyClassify().equals(CompanyConstants.ClassClassify.JOINCOMPANY)) {
            CityExample cityExample = new CityExample();
            cityExample.createCriteria().andProvinceCodeEqualTo(province);
            return cityMapper.selectByExample(cityExample);
        }else{
            CityExample cityExample = new CityExample();
            cityExample.createCriteria().andCityCodeEqualTo(userVO.getPcUserInfo().getCity());
            return cityMapper.selectByExample(cityExample);

        }
    }

    /**
     * 根据市区查询县区
     *
     * @param city
     * @return
     */
    @Override
    public List<Area> findAreaByCity(String city) {
        AreaExample areaExample = new AreaExample();
        areaExample.createCriteria().andCityCodeEqualTo(city);
        return areaMapper.selectByExample(areaExample);
    }

    /**
     * 获取所有房屋类型
     *
     * @return
     */
    @Override
    public List<PreProjectHouseType> findAllHouseType() {
        return preProjectHouseTypeMapper.selectByExample(null);
    }

    /**
     * 获取房屋新旧程度
     *
     * @return
     */
    @Override
    public List<HousingStatus> findAlHouseStatus() {
        HousingStatusExample housingStatusExample = new HousingStatusExample();
        housingStatusExample.setOrderByClause(" sort_no");
        return housingStatusMapper.selectByExample(housingStatusExample);
    }

    /**
     * 获取项目套餐
     *
     * @return
     */
    @Override
    public List<ProjectType> findAllProjectType() {
        ProjectTypeExample projectTypeExample = new ProjectTypeExample();
        projectTypeExample.setOrderByClause(" sort_no");
        return projectTypeMapper.selectByExample(projectTypeExample);
    }

    /**
     * 根据区域编码查询公司信息
     *
     * @param areaCode
     * @return
     */
    @Override
    public List<CompanyInfo> findCompanyByAreaCode(Integer areaCode) {

        UserVO userVO = (UserVO) SessionUserDetailsUtil.getUserDetails();
        List<String> companyRelationMap = userVO.getRelationMap();

        CompanyInfoExample companyInfoExample = new CompanyInfoExample();
        companyInfoExample.createCriteria().andIsDeleteEqualTo(SysConstants.YesOrNoSp.NO.shortVal())
//                .andRootCompanyIdEqualTo(userVO.getPcUserInfo().getRootCompanyId())
                .andAreaCodeEqualTo(areaCode);

        return companyInfoMapper.selectByExample(companyInfoExample);
    }

    @Override
    public List<UserRoleSet> getRole() {
        UserRoleSetExample example = new UserRoleSetExample();
        //查询岗位显示的信息
        Short isShow = 1;
        example.createCriteria().andIsShowEqualTo(isShow);
        return userRoleSetMapper.selectByExample(example);
    }

    @Override
    public List<UserRoleSet> getCompanyRole() {
        UserRoleSetExample example = new UserRoleSetExample();
        //查询岗位显示的信息
        Integer pid = 0;
        example.createCriteria().andPidEqualTo(pid);
        return userRoleSetMapper.selectByExample(example);
    }

    @Override
    public List<CompanyInfo> findCompanyByCode(Integer provinceCode, Integer cityCode) {
        CompanyInfoExample example = new CompanyInfoExample();
        UserVO userVO = (UserVO) SessionUserDetailsUtil.getUserDetails();

        if(StringUtils.isNotBlank(provinceCode.toString())){
            example.createCriteria().andIsDeleteEqualTo(SysConstants.YesOrNoSp.NO.shortVal())
//                    .andRootCompanyIdEqualTo(userVO.getPcUserInfo().getRootCompanyId())
                    .andProvinceCodeEqualTo(provinceCode.shortValue());
        }
        if(StringUtils.isNotBlank(provinceCode.toString())){
            example.createCriteria().andIsDeleteEqualTo(SysConstants.YesOrNoSp.NO.shortVal())
//                    .andRootCompanyIdEqualTo(userVO.getPcUserInfo().getRootCompanyId())
                    .andProvinceCodeEqualTo(cityCode.shortValue());
        }
        return null;
    }

    /**
     * 根据条件查询埃森哲数据
     *
     * @param condition
     * @return
     */
    @Override
    public List<HrPeopleEntity> findThirdPeople(String condition) {
        HrPeopleEntityExample peopleEntityExample = new HrPeopleEntityExample();
        peopleEntityExample.createCriteria().andPeopleNameLike(condition+"%");
        return hrPeopleEntityMapper.selectByExample(peopleEntityExample);
    }

//    /**
//     * 查询可用权限
//     *
//     * @return
//     */
//    @Override
//    public List<SystemResource> listResource() {
//        SystemResourceExample systemResourceExample = new SystemResourceExample();
//        systemResourceExample.setOrderByClause("  sort_num");
//        systemResourceExample.createCriteria().andTypeEqualTo(MenuType.MENU.code+"");
//
//        return  systemResourceMapper.selectByExample(systemResourceExample);
//    }


}
