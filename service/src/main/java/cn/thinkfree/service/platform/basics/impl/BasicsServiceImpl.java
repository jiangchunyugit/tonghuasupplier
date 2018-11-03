package cn.thinkfree.service.platform.basics.impl;

import cn.thinkfree.database.mapper.AreaMapper;
import cn.thinkfree.database.mapper.BasicsDataMapper;
import cn.thinkfree.database.mapper.CityMapper;
import cn.thinkfree.database.mapper.ProvinceMapper;
import cn.thinkfree.database.model.*;
import cn.thinkfree.service.platform.basics.BasicsService;
import cn.thinkfree.service.platform.vo.CardTypeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xusonghui
 * 基础数据查询接口
 */
@Service
public class BasicsServiceImpl implements BasicsService {
    @Autowired
    private ProvinceMapper provinceMapper;
    @Autowired
    private CityMapper cityMapper;
    @Autowired
    private AreaMapper areaMapper;
    @Autowired
    private BasicsDataMapper basicsDataMapper;

    private List<BasicsData> queryData(String type) {
        //暂时写死
        BasicsDataExample dataExample = new BasicsDataExample();
        dataExample.createCriteria().andBasicsGroupEqualTo(type);
        return basicsDataMapper.selectByExample(dataExample);
    }

    @Override
    public List<BasicsData> cardTypes() {
        return queryData("ID_CARD_TYPE");
    }
    @Override
    public List<BasicsData> countryType() {
        return queryData("COUNTRY_TYPE");
    }
    @Override
    public List<BasicsData> cancelDesign() {
        return queryData("CANCEL_DESIGN");
    }
    @Override
    public List<BasicsData> refund() {
        return queryData("REFUND");
    }
    @Override
    public List<BasicsData> cancelCons() {
        return queryData("CANCEL_CONS");
    }

    @Override
    public List<Map<String, String>> pua(int type, String parentCode) {
        List<Map<String, String>> listMap = new ArrayList<>();
        if(type == 1){
            List<Province> provinces = provinceMapper.selectByExample(new ProvinceExample());
            for(Province province : provinces){
                Map<String,String> map = new HashMap<>();
                map.put("code",province.getProvinceCode());
                map.put("name",province.getProvinceName());
                listMap.add(map);
            }
        }else if(type == 2){
            CityExample cityExample = new CityExample();
            cityExample.createCriteria().andProvinceCodeEqualTo(parentCode);
            List<City> cities = cityMapper.selectByExample(cityExample);
            for(City city : cities){
                Map<String,String> map = new HashMap<>();
                map.put("code",city.getCityCode());
                map.put("name",city.getCityName());
                listMap.add(map);
            }
        }else if(type == 3){
            AreaExample areaExample = new AreaExample();
            areaExample.createCriteria().andCityCodeEqualTo(parentCode);
            List<Area> areas = areaMapper.selectByExample(areaExample);
            for(Area area : areas){
                Map<String,String> map = new HashMap<>();
                map.put("code",area.getAreaCode());
                map.put("name",area.getAreaName());
                listMap.add(map);
            }
        }
        return listMap;
    }
}
