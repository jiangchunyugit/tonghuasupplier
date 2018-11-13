package cn.thinkfree.service.construction.impl;

import cn.thinkfree.core.base.RespData;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.mapper.ConstructionOrderMapper;
import cn.thinkfree.database.mapper.EmployeeMsgMapper;
import cn.thinkfree.database.mapper.OrderUserMapper;
import cn.thinkfree.database.mapper.ProjectMapper;
import cn.thinkfree.database.model.*;
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
        example.createCriteria().andStatusEqualTo(1);

        List<ConstructionOrder> list = constructionOrderMapper.selectByExample(example);
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
        List<Project> list1 = getProjectInfo(listProjectNo);

        // 设计师
        List<Map<String, String>> list2 = getEmployeeInfo(listProjectNo, "CD");

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
        Page p = (Page) pageInfo2.getList();
        pageInfo.setTotal(p.getPages());
        return RespData.success(pageInfo);
    }


    /**
     * 查询项目信息
     *
     * @param listProjectNo
     * @return
     */
    public List<Project> getProjectInfo(List<String> listProjectNo) {
        ProjectExample example = new ProjectExample();
        example.createCriteria().andProjectNoIn(listProjectNo);
        return projectMapper.selectByExample(example);
    }


    /**
     * 设计师
     *
     * @param listProjectNo
     * @return
     */
    public List<Map<String, String>> getEmployeeInfo(List<String> listProjectNo, String role) {
        OrderUserExample example = new OrderUserExample();
        example.createCriteria().andProjectNoIn(listProjectNo).andRoleCodeEqualTo(role).andIsTransferEqualTo((short) 0);
        List<OrderUser> list = orderUserMapper.selectByExample(example);
        List<Map<String, String>> listName = new ArrayList<>();
        for (OrderUser orderUser : list) {
            EmployeeMsgExample example2 = new EmployeeMsgExample();
            example2.createCriteria().andUserIdEqualTo(orderUser.getUserId()).andRoleCodeEqualTo(role).andEmployeeStateEqualTo(1);
            List<EmployeeMsg> listEm = employeeMsgMapper.selectByExample(example2);
            for (EmployeeMsg employeeMsg : listEm) {
                Map<String, String> map = new HashMap<>();
                map.put("projectNo", orderUser.getProjectNo());
                map.put("name", employeeMsg.getRealName());
                listName.add(map);
            }
        }
        return listName;
    }

}
