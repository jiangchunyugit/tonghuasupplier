package cn.thinkfree.service.construction.impl;

import cn.thinkfree.core.base.RespData;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.mapper.CityMapper;
import cn.thinkfree.database.mapper.CompanyInfoMapper;
import cn.thinkfree.database.mapper.ConstructionOrderMapper;
import cn.thinkfree.database.model.*;
import cn.thinkfree.service.construction.ConstructionStateServiceB;
import cn.thinkfree.service.construction.ConstrutionDistributionOrder;
import cn.thinkfree.service.construction.vo.DistributionOrderCityVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    /**
     *  施工派单-公司/城市列表接口（含搜索公司）
     * @param companyName
     * @return
     */
    @Override
    public MyRespBundle<DistributionOrderCityVo> getCityList(String companyName) {
        DistributionOrderCityVo DistributionOrderCityVo = new DistributionOrderCityVo();
        CompanyInfoExample example = new CompanyInfoExample();
        if (!StringUtils.isBlank(companyName)){
            example.createCriteria().andCompanyNameLike(companyName);
        }
        List<CompanyInfo> list = companyInfoMapper.selectByExample(example);
        for (CompanyInfo companyInfo : list){
            CityExample cityExample = new CityExample();
            cityExample.createCriteria().andCityCodeEqualTo(String.valueOf(companyInfo.getCityCode()));
            List<City> listCity = cityMapper.selectByExample(cityExample);
            for (City city1 :listCity){
                DistributionOrderCityVo.setCity(city1.getCityName());
                DistributionOrderCityVo.setCompanyName(companyInfo.getCompanyName());
                DistributionOrderCityVo.setCompanyId(companyInfo.getCompanyId());

            }
        }
        return RespData.success(DistributionOrderCityVo);
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
        if (ResultMessage.SUCCESS.code.equals(r.getCode())){
            return RespData.error(ResultMessage.ERROR.code, r.getMessage());
        }

        // 绑定装饰公司
        int isUpdate = constructionOrderMapper.updateByExampleSelective(constructionOrder,example);
        if (isUpdate == 1) {
            return RespData.success();
        } else {
            return RespData.error(ResultMessage.ERROR.code, "派单失败,请稍后重试");
        }
    }

}
