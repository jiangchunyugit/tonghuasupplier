package cn.thinkfree.service.construction;


import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.constants.ConstructionStateEnumB;
import cn.thinkfree.database.mapper.CityMapper;
import cn.thinkfree.database.mapper.ConstructionOrderMapper;
import cn.thinkfree.database.mapper.ConstructionOrderPayMapper;
import cn.thinkfree.database.mapper.ProjectMapper;
import cn.thinkfree.database.model.*;
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
    ConstructionOrderPayMapper constructionOrderPayMapper;

    @Autowired
    CityMapper cityMapper;

    @Autowired
    ProjectMapper projectMapper;

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
     * 查询 当前详细阶段（支付/施工） By orderNo
     */
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
        if (list == null || list.isEmpty()) {
            return "";
        }
        return list.get(0).getCityName();
    }

}
