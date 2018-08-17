package cn.thinkfree.service.dictionary;

import cn.thinkfree.database.mapper.AreaMapper;
import cn.thinkfree.database.mapper.CityMapper;
import cn.thinkfree.database.mapper.ProvinceMapper;
import cn.thinkfree.database.model.*;
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


    /**
     * 查询所有省份
     *
     * @return
     */
    @Override
    public List<Province> findAllProvince() {
        return provinceMapper.selectByExample(null);
    }

    /**
     * 根据省份查询市区
     *
     * @param province
     * @return
     */
    @Override
    public List<City> findCityByProvince(String province) {
        CityExample cityExample = new CityExample();
        cityExample.createCriteria().andProvinceCodeEqualTo(province);
        return cityMapper.selectByExample(cityExample);
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
}
