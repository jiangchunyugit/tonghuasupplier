package cn.thinkfree.service.platform.basics.impl;

import cn.thinkfree.core.constants.BasicsDataParentEnum;
import cn.thinkfree.database.mapper.*;
import cn.thinkfree.database.model.*;
import cn.thinkfree.service.platform.basics.BasicsService;
import cn.thinkfree.service.platform.vo.CardTypeVo;
import cn.thinkfree.service.platform.vo.PageVo;
import cn.thinkfree.service.utils.HttpUtils;
import cn.thinkfree.service.utils.OrderNoUtils;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author xusonghui
 * 基础数据查询接口
 */
@Service
public class BasicsServiceImpl implements BasicsService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private ProvinceMapper provinceMapper;
    @Autowired
    private CityMapper cityMapper;
    @Autowired
    private AreaMapper areaMapper;
    @Autowired
    private BasicsDataMapper basicsDataMapper;

    @Override
    public List<BasicsData> queryData(String groupCode) {
        //暂时写死
        BasicsDataExample dataExample = new BasicsDataExample();
        dataExample.createCriteria().andBasicsGroupEqualTo(groupCode).andDelStateEqualTo(2);
        return basicsDataMapper.selectByExample(dataExample);
    }

    @Override
    public PageVo<List<BasicsData>> queryData(String groupCode, int pageSize, int pageIndex) {
        //暂时写死
        BasicsDataExample dataExample = new BasicsDataExample();
        dataExample.createCriteria().andBasicsGroupEqualTo(groupCode).andDelStateEqualTo(2);
        long total = basicsDataMapper.countByExample(dataExample);
        PageHelper.startPage(pageIndex - 1, pageSize);
        List<BasicsData> dataList = basicsDataMapper.selectByExample(dataExample);
        PageVo<List<BasicsData>> pageVo = new PageVo<>();
        pageVo.setData(dataList);
        pageVo.setPageIndex(pageIndex);
        pageVo.setPageSize(pageSize);
        pageVo.setTotal(total);
        return pageVo;
    }

    @Override
    public List<BasicsData> queryData(String groupCode, List<String> keyCodes) {
        //暂时写死
        BasicsDataExample dataExample = new BasicsDataExample();
        dataExample.createCriteria().andBasicsGroupEqualTo(groupCode).andDelStateEqualTo(2).andBasicsCodeIn(keyCodes);
        return basicsDataMapper.selectByExample(dataExample);
    }

    @Override
    public void createBasics(String dataId, String groupCode, String basicsName, String remark) {
        if(!BasicsDataParentEnum.allCodes().contains(groupCode)){
            throw new RuntimeException("无效的分组类型");
        }
        if(StringUtils.isNotBlank(dataId)){
            BasicsData basicsData = basicsDataMapper.selectByPrimaryKey(dataId);
            if(basicsData == null){
                throw new RuntimeException("无效的数据ID");
            }
            if(!groupCode.equals(basicsData.getBasicsGroup())){
                throw new RuntimeException("无效的数据");
            }
            basicsData = new BasicsData();
            basicsData.setBasicsName(basicsName);
            basicsData.setBasicsGroup(groupCode);
            basicsData.setRemark(remark);
            basicsData.setId(dataId);
            int res = basicsDataMapper.updateByPrimaryKeySelective(basicsData);
            logger.info("编辑基础数据：params={},res={}", JSONObject.toJSONString(HttpUtils.getHttpParams()),res);
        }else{
            createData(groupCode, basicsName, remark);
        }
    }

    /**
     * 创建基础数据
     * @param groupCode
     * @param basicsName
     * @param remark
     */
    private void createData(String groupCode, String basicsName, String remark) {
        BasicsDataExample dataExample = new BasicsDataExample();
        dataExample.createCriteria().andBasicsGroupEqualTo(groupCode).andBasicsNameEqualTo(basicsName);
        if(!basicsDataMapper.selectByExample(dataExample).isEmpty()){
            BasicsData basicsData = new BasicsData();
            basicsData.setDelState(2);
            basicsData.setRemark(remark);
            int res = basicsDataMapper.updateByExample(basicsData,dataExample);
            //更新已删除的状态
            logger.info("更新已删除的基础数据为未删除：params={},res={}", JSONObject.toJSONString(HttpUtils.getHttpParams()),res);
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
            logger.info("新增基础数据：params={},res={}", JSONObject.toJSONString(HttpUtils.getHttpParams()),res);
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
        int res = basicsDataMapper.updateByPrimaryKeySelective(basicsData);
        logger.info("删除基础数据：params={},res={}", JSONObject.toJSONString(HttpUtils.getHttpParams()),res);
    }

    @Override
    public List<Map<String,String>> allParentCode(){
        return BasicsDataParentEnum.allTypes();
    }
    private String getCode(String groupCode){
        String dataCode = OrderNoUtils.getCode(6);
        int i = 0;
        while (true){
            BasicsDataExample dataExample = new BasicsDataExample();
            dataExample.createCriteria().andBasicsGroupEqualTo(groupCode).andBasicsCodeEqualTo(dataCode);
            List<BasicsData> basicsData = basicsDataMapper.selectByExample(dataExample);
            if(basicsData.isEmpty()){
                break;
            }
            dataCode = OrderNoUtils.getCode(6);
            i++;
            if(i > 50){
                throw new RuntimeException("编码重复");
            }
        }
        return dataCode;
    }

    @Override
    public List<BasicsData> cardTypes() {
        return queryData(BasicsDataParentEnum.ID_CARD_TYPE.getCode());
    }
    @Override
    public List<BasicsData> countryType() {
        return queryData(BasicsDataParentEnum.COUNTRY_TYPE.getCode());
    }
    @Override
    public List<BasicsData> cancelDesign() {
        return queryData(BasicsDataParentEnum.CANCEL_DESIGN.getCode());
    }
    @Override
    public List<BasicsData> refund() {
        return queryData(BasicsDataParentEnum.REFUND.getCode());
    }
    @Override
    public List<BasicsData> cancelCons() {
        return queryData(BasicsDataParentEnum.CANCEL_CONS.getCode());
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
