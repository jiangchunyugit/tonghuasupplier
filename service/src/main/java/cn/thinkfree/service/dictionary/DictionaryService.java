package cn.thinkfree.service.dictionary;

import cn.thinkfree.database.model.*;

import java.util.List;

public interface DictionaryService {

    /**
     * 查询所有省份
     * @return
     */
    List<Province> findAllProvince();

    /**
     * 根据省份查询市区
     * @param province
     * @return
     */
    List<City> findCityByProvince(String province);

    /**
     * 根据市区查询县区
     * @param city
     * @return
     */
    List<Area> findAreaByCity(String city);

    /**
     * 获取所有房屋类型
     * @return
     */
    List<PreProjectHouseType> findAllHouseType();

    /**
     * 获取房屋新旧程度
     * @return
     */
    List<HousingStatus> findAlHouseStatus();

    /**
     * 获取项目套餐
     * @return
     */
    List<ProjectType> findAllProjectType();

    /**
     * 根据区域编码查询公司信息
     * @param areaCode
     * @return
     */
    List<CompanyInfo> findCompanyByAreaCode(Integer areaCode);
}
