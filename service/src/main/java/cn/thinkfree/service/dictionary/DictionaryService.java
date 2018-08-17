package cn.thinkfree.service.dictionary;

import cn.thinkfree.database.model.Area;
import cn.thinkfree.database.model.City;
import cn.thinkfree.database.model.Province;

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

}
