package cn.thinkfree.service.construction.impl;

import cn.thinkfree.core.base.RespData;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ConstructionStateEnumB;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.mapper.BuildPayConfigMapper;
import cn.thinkfree.database.mapper.ConstructionOrderMapper;
import cn.thinkfree.database.mapper.ConstructionOrderPayMapper;
import cn.thinkfree.database.mapper.ProjectBigSchedulingMapper;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.vo.ProjectBigSchedulingDetailsVO;
import cn.thinkfree.service.construction.CommonService;
import cn.thinkfree.service.construction.ConstructionAndPayStateService;
import cn.thinkfree.service.newscheduling.NewSchedulingService;
import cn.thinkfree.service.utils.HttpUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ConstructionAndPayStateServiceImpl implements ConstructionAndPayStateService {

    @Autowired
    CommonService commonService;
    @Autowired
    ConstructionOrderMapper constructionOrderMapper;
    @Autowired
    ConstructionOrderPayMapper constructionOrderPayMapper;
    @Autowired
    ProjectBigSchedulingMapper projectBigSchedulingMapper;
    @Autowired
    BuildPayConfigMapper buildPayConfigMapper;
    @Autowired
    private NewSchedulingService schedulingService;

    /**
     * 支付阶段地址接口
     */
    @Value("${custom.service.ip}")
    private String userCenterIp;

    @Value("${custom.service.port}")
    private String userCenterPort;

    private static String contractUrl = "/financialapi/funds/orderStatusPayable";

    public String getUrl(String suffix) {
        return "http://" + userCenterIp + ":" + userCenterPort + suffix;
       // return "http://10.240.10.88" + suffix;
    }

    /**
     *
     *  首款 支付
     *  开工报告
     *
     *  阶段1开工
     *  阶段1付款
     *     ~
     *  阶段2开工
     *  阶段2付款
     *
     *  竣工验收
     *  尾款支付
     *
     */


    /**
     * 是否可以阶段款
     * 财务服务-刘博
     */
    @Override
    public MyRespBundle<String> isStagePay(String orderNo, Integer sort) {
        if (StringUtils.isBlank(orderNo)) {
            return RespData.error(ResultMessage.ERROR.code, "订单编号不能为空");
        }
        ConstructionOrderPayExample example1 = new ConstructionOrderPayExample();
        example1.createCriteria().andOrderNoEqualTo(orderNo).andSortEqualTo(sort.shortValue()).andIsEndEqualTo("construction");
        List<ConstructionOrderPay> list = constructionOrderPayMapper.selectByExample(example1);
        if (!list.isEmpty()) {
            return RespData.success();
        }
        return RespData.error(ResultMessage.ERROR.code,"false");
    }


    /**
     * 竣工验收通过
     * 内部服务-传让
     */
    @Override
    public boolean isBeComplete(String projectNo, Integer sort) {

        //通过projectNo查询orderNo
        ConstructionOrderExample example = new ConstructionOrderExample();
        example.createCriteria().andProjectNoEqualTo(projectNo);
        List<ConstructionOrder> constructionOrderList = constructionOrderMapper.selectByExample(example);
        if (constructionOrderList.isEmpty()) {
            return false;
        }
        String orderNo = constructionOrderList.get(0).getOrderNo();
        String schemeNo = constructionOrderList.get(0).getSchemeNo();

        /* sort==0 开工报告 */
        if (sort == 0) {
            Integer stateCode = commonService.queryStateCodeByOrderNo(orderNo);
            Integer stage = ConstructionStateEnumB.STATE_600.getState();
            if (stateCode.equals(stage)) {
                return true;
            } else {
                return false;
            }
        } else {
            ConstructionOrderPayExample example1 = new ConstructionOrderPayExample();
            example1.createCriteria().andOrderNoEqualTo(orderNo);
            List<ConstructionOrderPay> list = constructionOrderPayMapper.selectByExample(example1);
            if (list.isEmpty()) {
                return false;
            }

            int sortCode = list.get(0).getSort();
            String isEnd = list.get(0).getIsEnd();

            // 逆向操作
            if (sort < sortCode) {
                return false;
            }

            // 查询 sort 上一个阶段 付款配置
            List<String> listStage = getBuildPay(schemeNo);

            List<ProjectBigSchedulingDetailsVO> schedulingDetailsVOs = schedulingService.getScheduling(projectNo).getData();
            Integer preSort = getPreScheduleSort(schedulingDetailsVOs, sort);

            if (listStage.contains(preSort.toString())) {
                if ("pay".equals(isEnd)) {
                    return true;
                } else {
                    return false;
                }
            }

//            for (int i = sort - 1; i >= 0; i--) {
//                if (listStage.contains(String.valueOf(i))) {
//                    if ("pay".equals(isEnd)) {
//                        return true;
//                    }else {
//                        return false;
//                    }
//                }
//            }

            return true;

        }

    }

    private Integer getPreScheduleSort(List<ProjectBigSchedulingDetailsVO> schedulingDetailsVOs, Integer currentSort) {
        Integer preScheduleSort = 0;
        for (ProjectBigSchedulingDetailsVO schedulingDetailsVO : schedulingDetailsVOs) {
            if (currentSort.equals(schedulingDetailsVO.getBigSort())) {
                break;
            }
            preScheduleSort = schedulingDetailsVO.getBigSort();
        }

        return preScheduleSort;
    }

    /**
     * 施工支付配置
     *
     * @param schemeNo
     * @return
     */
    public List<String> getBuildPay(String schemeNo) {
        List<String> listStage = new ArrayList<>();
        BuildPayConfigExample example = new BuildPayConfigExample();
        example.createCriteria().andSchemeNoEqualTo(schemeNo).andDeleteStateIn(Arrays.asList(2, 3));
        List<BuildPayConfig> list = buildPayConfigMapper.selectByExample(example);
        for (BuildPayConfig buildPayConfig : list) {
            listStage.add(buildPayConfig.getStageCode());
        }
        return listStage;
    }



    /**
     * 支付阶段通知
     */
    @Override
    public void notifyPay(String orderNo,Integer sort) {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("fromOrderid", orderNo);
            map.put("sort", sort);
            HttpUtils.post(getUrl(contractUrl), getParams(map));
        } catch (Exception e) {
            System.out.println("支付阶段通知异常");
        }

    }


    private String getParams(Map<String, Object> map) {
        Iterator<String> it = map.keySet().iterator();
        String params = "";
        while (it.hasNext()) {
            String key = it.next();
            Object value = map.get(key);
            params = params + "&" + key + "=" + value;
        }
        if (params.indexOf("&") >= 0) {
            params = params.substring(1);
        }
        return params;
    }

}
