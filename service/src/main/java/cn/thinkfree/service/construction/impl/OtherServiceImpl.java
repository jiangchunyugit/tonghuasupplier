package cn.thinkfree.service.construction.impl;

import cn.thinkfree.core.base.RespData;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.mapper.ConstructionOrderMapper;
import cn.thinkfree.database.mapper.EmployeeMsgMapper;
import cn.thinkfree.database.mapper.OrderUserMapper;
import cn.thinkfree.database.mapper.ProjectMapper;
import cn.thinkfree.database.model.*;
import cn.thinkfree.service.construction.OrderListCommonService;
import cn.thinkfree.service.construction.OtherService;
import cn.thinkfree.service.construction.vo.PrecisionPriceVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OtherServiceImpl implements OtherService {

    @Autowired
    ConstructionOrderMapper constructionOrderMapper;

    @Autowired
    ProjectMapper projectMapper;

    @Autowired
    OrderUserMapper orderUserMapper;

    @Autowired
    EmployeeMsgMapper employeeMsgMapper;

    @Autowired
    OrderListCommonService orderListCommonService;

    /**
     * 精准报价
     * @param companyNo
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public MyRespBundle<PageInfo<PrecisionPriceVo>> getPrecisionPriceList(String companyNo,int pageNum,int pageSize){

        if (StringUtils.isBlank(companyNo)) {
            return RespData.error(ResultMessage.ERROR.code, "公司编号不能为空");
        }

        PageHelper.startPage(pageNum, pageSize);
        PageInfo<PrecisionPriceVo> pageInfo = new PageInfo<>();
        PageInfo<ConstructionOrder> pageInfo2 = new PageInfo<>();

        ConstructionOrderExample example = new ConstructionOrderExample();
        example.setOrderByClause("create_time DESC");
        example.createCriteria().andCompanyIdEqualTo(companyNo).andStatusEqualTo(1);

        List<ConstructionOrder> list = constructionOrderMapper.selectByExample(example);
        if (list.size() <= 0){
            return RespData.error(ResultMessage.ERROR.code, "公司编号不符");
        }
        List<PrecisionPriceVo> listVo = new ArrayList<>();
        pageInfo2.setList(list);

        /* 项目编号List */
        List<String> listProjectNo = new ArrayList<>();
        for (ConstructionOrder constructionOrder : list) {
            listProjectNo.add(constructionOrder.getProjectNo());
        }

        /* 订单编号List */
        List<String> listOrdertNo = new ArrayList<>();
        for (ConstructionOrder constructionOrder : list) {
            listOrdertNo.add(constructionOrder.getOrderNo());
        }

        // 所属地区 & 项目地址 & 预约日期
        List<Project> list1 = orderListCommonService.getProjectInfo(listProjectNo);

        // 设计师
        List<Map<String, String>> list2 = orderListCommonService.getEmployeeInfo(listProjectNo, "CD");

        continueOut:
        for (ConstructionOrder constructionOrder : list) {
            PrecisionPriceVo precisionPriceVo = new PrecisionPriceVo();
            // 所属地区 & 项目地址 & 预约日期
            for (Project project : list1) {
                if (constructionOrder.getProjectNo().equals(project.getProjectNo())) {
                    precisionPriceVo.setArea(project.getArea());
                    precisionPriceVo.setAddressDetail(project.getAddressDetail());
                    precisionPriceVo.setAppointmentTime(project.getCreateTime());
                    precisionPriceVo.setDecorationBudget(project.getDecorationBudget());
                }
            }
            // 订单编号 & 项目编号
            precisionPriceVo.setOrderNo(constructionOrder.getOrderNo());
            precisionPriceVo.setProjectNo(constructionOrder.getProjectNo());
            // 设计师
            for (Map<String, String> OrderUser : list2) {
                if (constructionOrder.getProjectNo().equals(OrderUser.get("projectNo"))) {
                    precisionPriceVo.setDesignerName(OrderUser.get("name"));
                }
            }
            listVo.add(precisionPriceVo);
        }
        pageInfo.setList(listVo);
        pageInfo.setTotal(pageInfo2.getList().size());
        return RespData.success(pageInfo);
    }
}
