package cn.thinkfree.service.construction.impl;

import cn.thinkfree.core.base.RespData;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ConstructionStateEnum;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.mapper.BuildSchemeCompanyRelMapper;
import cn.thinkfree.database.mapper.CityMapper;
import cn.thinkfree.database.mapper.CompanyInfoMapper;
import cn.thinkfree.database.mapper.ConstructionOrderMapper;
import cn.thinkfree.database.model.*;
import cn.thinkfree.service.construction.CommonService;
import cn.thinkfree.service.construction.ConstructionStateService;
import cn.thinkfree.service.construction.ConstrutionDistributionOrder;
import cn.thinkfree.service.construction.vo.ConstructionOrderDistributionNumVo;
import cn.thinkfree.service.construction.vo.DistributionOrderCityVo;
import cn.thinkfree.service.platform.basics.BasicsService;
import cn.thinkfree.service.platform.build.BuildConfigService;
import cn.thinkfree.service.utils.ReflectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class ConstrutionDistributionOrderImpl implements ConstrutionDistributionOrder {

    @Autowired
    ConstructionStateService constructionStateService;
    @Autowired
    CompanyInfoMapper companyInfoMapper;
    @Autowired
    CityMapper cityMapper;
    @Autowired
    ConstructionOrderMapper constructionOrderMapper;
    @Autowired
    CommonService commonService;
    @Autowired
    BuildConfigService buildConfigService;
    @Autowired
    private BuildSchemeCompanyRelMapper relMapper;
    @Autowired
    private BasicsService basicsService;


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
            if (stage == ConstructionStateEnum.STATE_500.getState() || stage == ConstructionStateEnum.STATE_510.getState()) {
                waitDistributionOrder++;//待派单
            }
            if (stage == ConstructionStateEnum.STATE_530.getState()) {
                waitReceipt++;//待接单
            }
            if (stage > ConstructionStateEnum.STATE_530.getState() && stage < ConstructionStateEnum.STATE_700.getState()) {
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

        BuildSchemeCompanyRelExample relExample = new BuildSchemeCompanyRelExample();
        relExample.createCriteria().andIsEableEqualTo(1).andDelStateEqualTo(2);
        List<BuildSchemeCompanyRel> rels = relMapper.selectByExample(relExample);
        if(rels.isEmpty()){
            return RespData.success(new ArrayList<>());
        }
        List<String> companyIds = ReflectUtils.getList(rels,"companyId");
        CompanyInfoExample example = new CompanyInfoExample();
        CompanyInfoExample.Criteria criteria = example.createCriteria();
        if (!StringUtils.isBlank(companyName)){
            criteria.andCompanyNameLike("%"+companyName+"%");
        }
        criteria.andCompanyIdIn(companyIds);
        criteria.andRoleIdEqualTo("BD").andAuditStatusEqualTo("8");
        List<CompanyInfo> list = companyInfoMapper.selectByExample(example);
        if(list.isEmpty()){
            return RespData.success(new ArrayList<>());
        }
        Map<String, String> citys = basicsService.getCity();
        List<DistributionOrderCityVo> listData = new ArrayList<>();
        for (CompanyInfo companyInfo : list){
            DistributionOrderCityVo DistributionOrderCityVo = new DistributionOrderCityVo();
            DistributionOrderCityVo.setCity(citys.get(companyInfo.getCityCode() + ""));
            DistributionOrderCityVo.setCompanyName(companyInfo.getCompanyName());
            DistributionOrderCityVo.setCompanyId(companyInfo.getCompanyId());
            listData.add(DistributionOrderCityVo);
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

        /* 判断该公司是否又施工方案 */
        String schemeNo = buildConfigService.getSchemeNoByCompanyId(companyId);
        if (StringUtils.isBlank(schemeNo)){
            return RespData.error(ResultMessage.ERROR.code, "该公司没有施工方案不能派单");
        }

        ConstructionOrderExample example = new ConstructionOrderExample();
        example.createCriteria().andOrderNoEqualTo(orderNo);
        ConstructionOrder constructionOrder = new ConstructionOrder();
        constructionOrder.setCompanyId(companyId);
        constructionOrder.setSchemeNo(schemeNo);

        // 改变订单状态
        MyRespBundle<String> r = constructionStateService.operateDispatchToConstruction(orderNo);
        if (!ResultMessage.SUCCESS.code.equals(r.getCode())){
            return RespData.error(ResultMessage.ERROR.code, r.getMsg());
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
