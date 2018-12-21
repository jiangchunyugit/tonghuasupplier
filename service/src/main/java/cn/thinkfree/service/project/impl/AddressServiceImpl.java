package cn.thinkfree.service.project.impl;

import cn.thinkfree.database.mapper.AreaMapper;
import cn.thinkfree.database.mapper.CityMapper;
import cn.thinkfree.database.mapper.ProvinceMapper;
import cn.thinkfree.database.model.*;
import cn.thinkfree.service.project.AddressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 地址服务层
 *
 * @author song
 * @version 1.0
 * @date 2018/12/21 12:50
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class AddressServiceImpl implements AddressService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddressServiceImpl.class);

    @Autowired
    private ProvinceMapper provinceMapper;
    @Autowired
    private CityMapper cityMapper;
    @Autowired
    private AreaMapper areaMapper;


    @Override
    public String getAddress(Project project) {
        Province province = getProvince(project.getProvince());
        if (province == null) {
            LOGGER.error("未查询到省份信息，code:{}", project.getProvince());
            throw new RuntimeException();
        }
        City city = getCity(project.getCity());
        if (city == null) {
            LOGGER.error("未查询到市信息，code:{}", project.getCity());
            throw new RuntimeException();
        }
        Area area = getArea(project.getRegion());
        if (area == null) {
            LOGGER.error("未查询到区信息，code:{}", project.getRegion());
            throw new RuntimeException();
        }
        return province.getProvinceName() + city.getCityName() + area.getAreaName() + project.getAddressDetail();
    }

    private Province getProvince(String code){
        ProvinceExample example = new ProvinceExample();
        example.createCriteria().andProvinceCodeEqualTo(code);
        List<Province> provinces = provinceMapper.selectByExample(example);
        return provinces != null && provinces.size() > 0 ? provinces.get(0) : null;
    }

    private City getCity(String code) {
        CityExample example = new CityExample();
        example.createCriteria().andCityCodeEqualTo(code);
        List<City> cities = cityMapper.selectByExample(example);
        return cities != null && cities.size() > 0 ? cities.get(0) : null;
    }

    private Area getArea(String code) {
        AreaExample example = new AreaExample();
        example.createCriteria().andAreaCodeEqualTo(code);
        List<Area> areas = areaMapper.selectByExample(example);
        return areas != null && areas.size() > 0 ? areas.get(0) : null;
    }
}
