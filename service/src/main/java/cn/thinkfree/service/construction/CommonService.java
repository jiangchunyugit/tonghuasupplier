package cn.thinkfree.service.construction;


import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.constants.ConstructionStateEnumB;
import cn.thinkfree.database.mapper.CityMapper;
import cn.thinkfree.database.mapper.ConstructionOrderMapper;
import cn.thinkfree.database.model.City;
import cn.thinkfree.database.model.CityExample;
import cn.thinkfree.database.model.ConstructionOrder;
import cn.thinkfree.database.model.ConstructionOrderExample;
import cn.thinkfree.service.construction.vo.ConstructionCityVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommonService extends AbsBaseController {
    @Autowired
    ConstructionOrderMapper constructionOrderMapper;

    @Autowired
    CityMapper cityMapper;

    /**
     * 查询 当前状态值 By projectNo
     */
    public Integer queryStateCode(String projectNo) {
        ConstructionOrderExample example = new ConstructionOrderExample();
        example.createCriteria().andProjectNoEqualTo(projectNo);
        List<ConstructionOrder> constructionOrderList = constructionOrderMapper.selectByExample(example);
        return constructionOrderList.get(0).getOrderStage();
    }

    /**
     * 查询 当前状态值 By orderNo
     */
    public Integer queryStateCodeByOrderNo(String orderNo) {
        ConstructionOrderExample example = new ConstructionOrderExample();
        example.createCriteria().andOrderNoEqualTo(orderNo);
        List<ConstructionOrder> constructionOrderList = constructionOrderMapper.selectByExample(example);
        return constructionOrderList.get(0).getOrderStage();
    }


    /**
     * 更新状态值 By projectNo
     */
    public boolean updateStateCode(String projectNo, int stateCode) {
        ConstructionOrderExample example = new ConstructionOrderExample();
        example.createCriteria().andProjectNoEqualTo(projectNo);
        ConstructionOrder constructionOrder = new ConstructionOrder();
        constructionOrder.setOrderStage(stateCode);
        int isUpdate = constructionOrderMapper.updateByExampleSelective(constructionOrder, example);
        if (isUpdate == 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 更新状态值 By orderNo
     */
    public boolean updateStateCodeByOrderNo(String orderNo, int stateCode) {
        ConstructionOrderExample example = new ConstructionOrderExample();
        example.createCriteria().andOrderNoEqualTo(orderNo);
        ConstructionOrder constructionOrder = new ConstructionOrder();
        constructionOrder.setOrderStage(stateCode);
        int isUpdate = constructionOrderMapper.updateByExampleSelective(constructionOrder, example);
        if (isUpdate == 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 施工订单 城市列表
     *
     * @return
     */
    public List<ConstructionCityVo> getCityList() {
        CityExample cityExample = new CityExample();
        cityExample.setOrderByClause("city_code ASC");
        List<City> list = cityMapper.selectByExample(cityExample);
        List<ConstructionCityVo> listVo = new ArrayList<>();
        for (City c : list) {
            ConstructionCityVo constructionCityVo = new ConstructionCityVo();
            constructionCityVo.setCityCode(c.getCityCode());
            constructionCityVo.setCityName(c.getCityName());
            listVo.add(constructionCityVo);
        }
        return listVo;
    }

    /**
     * 施工订单 城市编码转城市名称
     *
     * @return
     */
    public String getCityNameByCode(String cityCode) {
        CityExample cityExample = new CityExample();
        cityExample.createCriteria().andCityCodeEqualTo(cityCode);
        List<City> list = cityMapper.selectByExample(cityExample);
        return list.get(0).getCityName();
    }


}
