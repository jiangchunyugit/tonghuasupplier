package cn.thinkfree.service.platform.basics.impl;

import cn.thinkfree.database.mapper.*;
import cn.thinkfree.database.model.*;
import cn.thinkfree.service.platform.basics.BasicsService;
import cn.thinkfree.service.platform.vo.CardTypeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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
    @Autowired
    private BasicsDataParentCodeMapper parentCodeMapper;

    @Override
    public List<BasicsData> queryData(String groupCode) {
        //暂时写死
        BasicsDataExample dataExample = new BasicsDataExample();
        dataExample.createCriteria().andBasicsGroupEqualTo(groupCode).andDelStateEqualTo(2);
        return basicsDataMapper.selectByExample(dataExample);
    }

    @Override
    public void createBasics(String groupCode, String basicsName, String remark) {
        BasicsDataParentCode parentCode = parentCodeMapper.selectByPrimaryKey(groupCode);
        if(parentCode == null){
            throw new RuntimeException("无效的分组类型");
        }
        BasicsDataExample dataExample = new BasicsDataExample();
        dataExample.createCriteria().andBasicsGroupEqualTo(groupCode).andBasicsNameEqualTo(basicsName);
        if(!basicsDataMapper.selectByExample(dataExample).isEmpty()){
            BasicsData basicsData = new BasicsData();
            basicsData.setDelState(2);
            basicsData.setRemark(remark);
            int res = basicsDataMapper.updateByExample(basicsData,dataExample);
            //更新已删除的状态
        }else{
            String dataCode = getCode(groupCode);
            BasicsData basicsData = new BasicsData();
            basicsData.setId(UUID.randomUUID().toString().replaceAll("-",""));
            basicsData.setDelState(2);
            basicsData.setRemark(remark);
            basicsData.setCreateTime(new Date());
            basicsData.setBasicsGroup(groupCode);
            basicsData.setBasicsCode(dataCode);
            basicsData.setBasicsName(basicsName);
            int res = basicsDataMapper.insertSelective(basicsData);
            //新增基础数据
        }
    }

    @Override
    public void delBasics(String dataId) {
        if(basicsDataMapper.selectByPrimaryKey(dataId) == null){
            throw new RuntimeException("无效的数据ID");
        }
        BasicsData basicsData = new BasicsData();
        basicsData.setDelState(1);
        basicsData.setId(dataId);
        basicsDataMapper.updateByPrimaryKeySelective(basicsData);
    }

    @Override
    public List<BasicsDataParentCode> allParentCode(){
        return parentCodeMapper.selectByExample(new BasicsDataParentCodeExample());
    }
    private String getCode(String groupCode){
        BasicsDataExample dataExample = new BasicsDataExample();
        dataExample.createCriteria().andBasicsGroupEqualTo(groupCode);
        List<BasicsData> basicsData = basicsDataMapper.selectByExample(dataExample);
        return basicsData.size() + 1 + "";
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
