package cn.thinkfree.service.construction.impl;

import cn.thinkfree.core.base.RespData;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.BasicsDataParentEnum;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.core.constants.RoleFunctionEnum;
import cn.thinkfree.database.mapper.*;
import cn.thinkfree.database.model.*;
import cn.thinkfree.service.construction.OrderListCommonService;
import cn.thinkfree.service.construction.OtherService;
import cn.thinkfree.service.construction.vo.OfferProjectVo;
import cn.thinkfree.service.construction.vo.PrecisionPriceVo;
import cn.thinkfree.service.platform.basics.BasicsService;
import cn.thinkfree.service.platform.basics.RoleFunctionService;
import cn.thinkfree.service.platform.designer.UserCenterService;
import cn.thinkfree.service.platform.employee.ProjectUserService;
import cn.thinkfree.service.platform.vo.UserMsgVo;
import cn.thinkfree.service.utils.PageInfoUtils;
import cn.thinkfree.service.utils.ReflectUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class OtherServiceImpl implements OtherService {
    @Autowired
    private ConstructionOrderMapper constructionOrderMapper;
    @Autowired
    private OrderUserMapper orderUserMapper;
    @Autowired
    private EmployeeMsgMapper employeeMsgMapper;
    @Autowired
    private OrderListCommonService orderListCommonService;
    @Autowired
    private ProjectQuotationCheckMapper quotationCheckMapper;
    @Autowired
    private RoleFunctionService functionService;
    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private ProjectUserService projectUserService;
    @Autowired
    private UserCenterService userCenterService;
    @Autowired
    private CompanyInfoMapper companyInfoMapper;
    @Autowired
    private DesignerOrderMapper designerOrderMapper;
    @Autowired
    private BasicsService basicsService;

    /**
     * 精准报价
     * @param companyNo
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public MyRespBundle<PageInfo<PrecisionPriceVo>> getOfferList(String companyNo,int pageNum,int pageSize){
        if (StringUtils.isBlank(companyNo)) {
            return RespData.error(ResultMessage.ERROR.code, "公司编号不能为空");
        }
        PageHelper.startPage(pageNum, pageSize);
        PageInfo<ConstructionOrder> pageInfo2 = new PageInfo<>();
        ConstructionOrderExample example = new ConstructionOrderExample();
        example.setOrderByClause("create_time DESC");
        example.createCriteria().andCompanyIdEqualTo(companyNo).andStatusEqualTo(1);
        List<ConstructionOrder> constructionOrders = constructionOrderMapper.selectByExample(example);
        pageInfo2.setList(constructionOrders);
        if (constructionOrders.size() <= 0){
            return new MyRespBundle<>();
        }
        /* 项目编号List */
        List<String> projectNos = ReflectUtils.getList(constructionOrders,"projectNo");
        // 所属地区 & 项目地址 & 预约日期
        List<Project> projects = orderListCommonService.getProjectInfo(projectNos);
        Map<String,Project> projectMap = ReflectUtils.listToMap(projects,"projectNo");
        // 设计师
        String designCode = functionService.queryRoleCode(RoleFunctionEnum.DESIGN_POWER);
        List<OrderUser> orderUsers = getOrderUsers(projectNos, designCode);
        List<String> userIds = ReflectUtils.getList(orderUsers,"userId");
        Map<String,String> userMap = ReflectUtils.listToMap(orderUsers,"projectNo","userId");
        List<EmployeeMsg> employeeMsgs = getEmployeeMsgs(userIds);
        Map<String,EmployeeMsg> employeeMsgMap = ReflectUtils.listToMap(employeeMsgs,"userId");
        List<ProjectQuotationCheck> quotationChecks = getProjectQuotationChecks(projectNos);
        Map<String,ProjectQuotationCheck> quotationCheckMap = ReflectUtils.listToMap(quotationChecks,"projectNo");
        List<PrecisionPriceVo> listVo = new ArrayList<>();
        for (ConstructionOrder constructionOrder : constructionOrders) {
            PrecisionPriceVo precisionPriceVo = new PrecisionPriceVo();
            Project project = projectMap.get(constructionOrder.getProjectNo());
            if (project != null) {
                precisionPriceVo.setArea(project.getArea());
                precisionPriceVo.setAddressDetail(project.getAddressDetail());
                precisionPriceVo.setAppointmentTime(project.getCreateTime().getTime());
                precisionPriceVo.setDecorationBudget(project.getDecorationBudget());
            }
            // 订单编号 & 项目编号
            precisionPriceVo.setOrderNo(constructionOrder.getOrderNo());
            precisionPriceVo.setProjectNo(constructionOrder.getProjectNo());
            precisionPriceVo.setOrderStage(constructionOrder.getOrderStage() + "");
            String designId = userMap.get(constructionOrder.getProjectNo());
            EmployeeMsg employeeMsg = null;
            if(designId != null){
                employeeMsg = employeeMsgMap.get(designId);
            }
            if(employeeMsg != null){
                precisionPriceVo.setDesignerName(employeeMsg.getRealName());
            }
            ProjectQuotationCheck quotationCheck = quotationCheckMap.get(constructionOrder.getProjectNo());
            if(quotationCheck != null){
                precisionPriceVo.setOfferCheck(quotationCheck.getCheckStatus() == null ? -1 : quotationCheck.getCheckStatus());
            }
            listVo.add(precisionPriceVo);
        }
        return RespData.success(PageInfoUtils.pageInfo(pageInfo2,listVo));
    }

    @Override
    public MyRespBundle<OfferProjectVo> getProject(String projectNo) {
        ProjectExample example = new ProjectExample();
        example.createCriteria().andProjectNoEqualTo(projectNo);
        List<Project> projects = projectMapper.selectByExample(example);
        if(projects.isEmpty()){
            return RespData.error("没有查询到该项目");
        }
        Project project = projects.get(0);
        String designerId = projectUserService.queryUserIdOne(projectNo,RoleFunctionEnum.DESIGN_POWER);
        String ownerId = projectUserService.queryUserIdOne(projectNo,RoleFunctionEnum.OWNER_POWER);
        DesignerOrderExample orderExample = new DesignerOrderExample();
        orderExample.createCriteria().andProjectNoEqualTo(projectNo);
        List<DesignerOrder> designerOrders = designerOrderMapper.selectByExample(orderExample);
        String companyName = "--";
        if(!designerOrders.isEmpty()){
            companyName = getCompanyName(designerOrders.get(0).getCompanyId());
        }
        EmployeeMsg employeeMsg = employeeMsgMapper.selectByPrimaryKey(designerId);
        UserMsgVo ownerMsg = userCenterService.queryUser(ownerId);
        OfferProjectVo projectVo = new OfferProjectVo();
        projectVo.setAddress(project.getAddressDetail());
        projectVo.setDesignerName(employeeMsg.getRealName());
        String houseType = "--";
        BasicsData basicsData = basicsService.queryDataOne(BasicsDataParentEnum.HOUSE_TYPE.getCode(),project.getHouseType() + "");
        if(basicsData != null){
            houseType = basicsData.getBasicsName();
        }
        projectVo.setHouseType(houseType);
        String huxing = "--";
        basicsData = basicsService.queryDataOne(BasicsDataParentEnum.HOUSE_STRUCTURE.getCode(),project.getHouseType() + "");
        if(basicsData != null){
            huxing = basicsData.getBasicsName();
        }
        projectVo.setHuxing(huxing);
        projectVo.setOwnerName(ownerMsg.getUserName());
        projectVo.setPhone(ownerMsg.getUserPhone());
        projectVo.setIsDesign("是");
        projectVo.setRemark("---");
        projectVo.setDesignCompanyName(companyName);
        projectVo.setProjectNo(projectNo);
        return RespData.success(projectVo);
    }

    private String getCompanyName(String companyId){
        CompanyInfoExample companyInfoExample = new CompanyInfoExample();
        companyInfoExample.createCriteria().andCompanyIdEqualTo(companyId);
        List<CompanyInfo> companyInfos = companyInfoMapper.selectByExample(companyInfoExample);
        if(companyInfos.isEmpty()){
            return "--";
        }
        return companyInfos.get(0).getCompanyName();
    }

    private List<ProjectQuotationCheck> getProjectQuotationChecks(List<String> projectNos) {
        if(projectNos == null || projectNos.isEmpty()){
            return new ArrayList<>();
        }
        ProjectQuotationCheckExample checkExample = new ProjectQuotationCheckExample();
        checkExample.createCriteria().andProjectNoIn(projectNos);
        return quotationCheckMapper.selectByExample(checkExample);
    }

    private List<EmployeeMsg> getEmployeeMsgs(List<String> userIds) {
        if(userIds == null || userIds.isEmpty()){
            return new ArrayList<>();
        }
        EmployeeMsgExample msgExample = new EmployeeMsgExample();
        msgExample.createCriteria().andUserIdIn(userIds);
        return employeeMsgMapper.selectByExample(msgExample);
    }

    private List<OrderUser> getOrderUsers(List<String> projectNos, String designCode) {
        if(projectNos == null || projectNos.isEmpty()){
            return new ArrayList<>();
        }
        OrderUserExample userExample = new OrderUserExample();
        userExample.createCriteria().andProjectNoIn(projectNos).andRoleCodeEqualTo(designCode).andIsTransferEqualTo(Short.parseShort("0"));
        return orderUserMapper.selectByExample(userExample);
    }
}
