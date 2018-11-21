package cn.thinkfree.service.construction.impl;

import cn.thinkfree.core.base.RespData;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ConstructionStateEnumB;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.mapper.CityMapper;
import cn.thinkfree.database.mapper.CompanyInfoMapper;
import cn.thinkfree.database.mapper.ConstructionOrderMapper;
import cn.thinkfree.database.model.*;
import cn.thinkfree.service.construction.CommonService;
import cn.thinkfree.service.construction.ConstructionStateServiceB;
import cn.thinkfree.service.construction.ConstrutionDistributionOrder;
import cn.thinkfree.service.construction.vo.ConstructionOrderDistributionNumVo;
import cn.thinkfree.service.construction.vo.DistributionOrderCityVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ConstrutionDistributionOrderImpl implements ConstrutionDistributionOrder {

    @Autowired
    ConstructionStateServiceB constructionStateServiceB;
    @Autowired
    CompanyInfoMapper companyInfoMapper;
    @Autowired
    CityMapper cityMapper;
    @Autowired
    ConstructionOrderMapper constructionOrderMapper;
    @Autowired
    CommonService commonService;


    /**
     * 施工订单(项目派单)列表统计
     * @return
     */
    @Override
    public MyRespBundle<ConstructionOrderDistributionNumVo> getComDistributionOrderNum() {

        ConstructionOrderExample example = new ConstructionOrderExample();
        List<ConstructionOrder> list = constructionOrderMapper.selectByExample(example);

        /* 统计状态个数 待派单/待接单/已接单*/
        int waitDistributionOrder = 0, waitReceipt = 0, alreadyReceipt= 0;
        for (ConstructionOrder constructionOrder : list) {
            // 订单状态 统计
            int stage = constructionOrder.getOrderStage();
            if (stage == ConstructionStateEnumB.STATE_500.getState() || stage == ConstructionStateEnumB.STATE_510.getState()) {
                waitDistributionOrder++;//待派单
            }
            if (stage == ConstructionStateEnumB.STATE_520.getState()) {
                waitReceipt++;//待接单
            }
            if (stage > ConstructionStateEnumB.STATE_520.getState() && stage < ConstructionStateEnumB.STATE_700.getState()) {
                alreadyReceipt++;//已接单
            }
        }

        ConstructionOrderDistributionNumVo constructionOrderDistributionNumVo = new ConstructionOrderDistributionNumVo();
        constructionOrderDistributionNumVo.setCityList(commonService.getCityList());
        constructionOrderDistributionNumVo.setOrderNum(list.size());
        constructionOrderDistributionNumVo.setWaitDistributionOrder(waitDistributionOrder);
        constructionOrderDistributionNumVo.setWaitReceipt(waitReceipt);
        constructionOrderDistributionNumVo.setAlreadyReceipt(alreadyReceipt);
        return RespData.success(constructionOrderDistributionNumVo);
    }

    /**
     *  施工派单-公司/城市列表接口（含搜索公司）
     * @param companyName
     * @return
     */
    @Override
    public MyRespBundle<List<DistributionOrderCityVo>> getCityList(String companyName) {

        CompanyInfoExample example = new CompanyInfoExample();
        if (!StringUtils.isBlank(companyName)){
            example.createCriteria().andCompanyNameLike("%"+companyName+"%");
        }
        List<DistributionOrderCityVo> listData = new ArrayList<>();
        example.createCriteria().andRoleIdLike("%BD%");
        List<CompanyInfo> list = companyInfoMapper.selectByExample(example);
        for (CompanyInfo companyInfo : list){
            CityExample cityExample = new CityExample();
            cityExample.createCriteria().andCityCodeEqualTo(String.valueOf(companyInfo.getCityCode()));
            List<City> listCity = cityMapper.selectByExample(cityExample);
            for (City city1 :listCity){
                DistributionOrderCityVo DistributionOrderCityVo = new DistributionOrderCityVo();
                DistributionOrderCityVo.setCity(city1.getCityName());
                DistributionOrderCityVo.setCompanyName(companyInfo.getCompanyName());
                DistributionOrderCityVo.setCompanyId(companyInfo.getCompanyId());
                listData.add(DistributionOrderCityVo);
            }
        }
        return RespData.success(listData);
    }

    /**
     *  施工派单- 确认派单
     * @param companyId
     * @return
     */
    @Override
    public MyRespBundle<String> DistributionCompany(String orderNo,String companyId) {

        if (StringUtils.isBlank(orderNo)) {
            return RespData.error(ResultMessage.ERROR.code, "订单编号不能为空");
        }

        if (StringUtils.isBlank(companyId)) {
            return RespData.error(ResultMessage.ERROR.code, "公司ID不能为空");
        }

        ConstructionOrderExample example = new ConstructionOrderExample();
        example.createCriteria().andOrderNoEqualTo(orderNo);
        ConstructionOrder constructionOrder = new ConstructionOrder();
        constructionOrder.setCompanyId(companyId);

        // 改变订单状态
        MyRespBundle<String> r = constructionStateServiceB.operateDispatchToConstruction(orderNo);
        if (!ResultMessage.SUCCESS.code.equals(r.getCode())){
            return RespData.error(ResultMessage.ERROR.code, r.getMessage());
        }

        // 绑定装饰公司
        int isUpdate = constructionOrderMapper.updateByExampleSelective(constructionOrder,example);
        if (isUpdate == 1) {
            return RespData.success();
        } else {
            return RespData.error(ResultMessage.ERROR.code, "派单失败,请稍后重试");
        }

        //获取施工方案编号

    }

}
