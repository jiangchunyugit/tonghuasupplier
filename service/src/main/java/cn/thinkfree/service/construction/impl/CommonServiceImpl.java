package cn.thinkfree.service.construction.impl;


import cn.thinkfree.database.mapper.*;
import cn.thinkfree.database.model.*;
import cn.thinkfree.service.construction.CommonService;
import cn.thinkfree.service.construction.vo.ConstructionCityVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class CommonServiceImpl implements CommonService {

    @Autowired
    ConstructionOrderMapper constructionOrderMapper;
    @Autowired
    ConstructionOrderPayMapper constructionOrderPayMapper;
    @Autowired
    CityMapper cityMapper;
    @Autowired
    ProjectMapper projectMapper;
    @Autowired
    ProjectBigSchedulingMapper projectBigSchedulingMapper;

    /**
     * 查询 当前状态值 By projectNo
     */
    @Override
    public Integer queryStateCode(String projectNo) {
        ConstructionOrderExample example = new ConstructionOrderExample();
        example.createCriteria().andProjectNoEqualTo(projectNo);
        List<ConstructionOrder> constructionOrderList = constructionOrderMapper.selectByExample(example);
        return constructionOrderList.get(0).getOrderStage();
    }

    /**
     * 查询 当前状态值 By orderNo
     */
    @Override
    public Integer queryStateCodeByOrderNo(String orderNo) {
        ConstructionOrderExample example = new ConstructionOrderExample();
        example.createCriteria().andOrderNoEqualTo(orderNo);
        List<ConstructionOrder> constructionOrderList = constructionOrderMapper.selectByExample(example);
        return constructionOrderList.get(0).getOrderStage();
    }

    /**
     * 查询 当前详细阶段（支付/施工） By orderNo
     */
    @Override
    public String queryStateCodeDetailByOrderNo(String orderNo) {
        ConstructionOrderPayExample example = new ConstructionOrderPayExample();
        example.createCriteria().andOrderNoEqualTo(orderNo);
        List<ConstructionOrderPay> constructionOrderList = constructionOrderPayMapper.selectByExample(example);
        if (constructionOrderList.size() > 0) {
            return constructionOrderList.get(0).getFeeName();
        }else {
            return "暂无信息";
        }
    }

    /**
     * 更新状态值 By projectNo
     */
    @Override
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
    @Override
    public boolean updateStateCodeByOrderNo(String orderNo, int stateCode) {
        ConstructionOrderExample example = new ConstructionOrderExample();
        example.createCriteria().andOrderNoEqualTo(orderNo);
        List<ConstructionOrder> list = constructionOrderMapper.selectByExample(example);

        ConstructionOrder constructionOrder = new ConstructionOrder();
        constructionOrder.setOrderStage(stateCode);

        int isUpdate = constructionOrderMapper.updateByExampleSelective(constructionOrder, example);
        //同步到project表中
        int isUpdateProject = updateToProject(list.get(0).getProjectNo(), stateCode);
        if (isUpdate == 1 && isUpdateProject == 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 更新状态值-同步到project表中
     */
    @Override
    public int updateToProject(String projectNo, int stateCode) {
        ProjectExample projectExample = new ProjectExample();
        projectExample.createCriteria().andProjectNoEqualTo(projectNo);
        Project project = new Project();
        project.setStage(stateCode);
        return projectMapper.updateByExampleSelective(project, projectExample);
    }

    /**
     * 施工订单 城市列表
     *
     * @return
     */
    @Override
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
    @Override
    public String getCityNameByCode(String cityCode) {
        CityExample cityExample = new CityExample();
        cityExample.createCriteria().andCityCodeEqualTo(cityCode);
        List<City> list = cityMapper.selectByExample(cityExample);
        if (list == null || list.isEmpty()) {
            return "";
        }
        return list.get(0).getCityName();
    }


    /**
     *  查询施工阶段名称
     * @param orderNo
     * @param sort
     * @return
     */
    @Override
    public String getSchedulingName (String orderNo,Integer sort){

        //查询订单编号
        ConstructionOrderExample example = new ConstructionOrderExample();
        example.createCriteria().andOrderNoEqualTo(orderNo);
        List<ConstructionOrder> list = constructionOrderMapper.selectByExample(example);
        if (list.isEmpty()) {
            return null;
        }

        //查询当前施工阶段名称
        String schedulingNo = list.get(0).getSchemeNo();
        ProjectBigSchedulingExample example2 = new ProjectBigSchedulingExample();
        example2.createCriteria().andSchemeNoEqualTo(schedulingNo).andSortEqualTo(sort);
        List<ProjectBigScheduling> schedulingList2 = projectBigSchedulingMapper.selectByExample(example2);
        if (schedulingList2.isEmpty()) {
            return null;
        }
        return schedulingList2.get(0).getName();
    }

}
