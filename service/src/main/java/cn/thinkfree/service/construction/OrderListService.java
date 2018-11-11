package cn.thinkfree.service.construction;

import cn.thinkfree.core.utils.JSONUtil;
import cn.thinkfree.database.appvo.PersionVo;
import cn.thinkfree.database.mapper.*;
import cn.thinkfree.database.model.*;
import cn.thinkfree.service.approvalflow.AfInstanceService;
import cn.thinkfree.service.config.HttpLinks;
import cn.thinkfree.service.neworder.NewOrderUserService;
import cn.thinkfree.service.utils.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderListService {
    @Autowired
    ConstructionOrderMapper constructionOrderMapper;

    @Autowired
    CommonService commonService;

    @Autowired
    ProjectMapper projectMapper;

    @Autowired
    NewOrderUserService newOrderUserService;

    @Autowired
    EmployeeMsgMapper employeeMsgMapper;

    @Autowired
    CompanyInfoMapper companyInfoMapper;

    @Autowired
    ProjectBigSchedulingMapper projectBigSchedulingMapper;

    @Autowired
    ProjectSchedulingMapper projectSchedulingMapper;

    @Autowired
    OrderUserMapper orderUserMapper;

    @Autowired
    AfInstanceService afInstanceService;

    @Autowired
    FundsOrderMapper fundsOrderMapper;

    @Autowired
    DesignerOrderMapper designerOrderMapper;

    @Resource
    private HttpLinks httpLinks;

    public static final String FORMAT = "yyyy-MM-dd HH:mm:ss";

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
     * 查询用户信息 -用户中心接口
     *
     * @param userId
     * @return
     */
    public PersionVo getOwnerId(String userId) {
        PersionVo owner = new PersionVo();
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("userId", userId);
        requestMap.put("roleId", "CC");
        HttpUtils.HttpRespMsg httpRespMsg = HttpUtils.post(httpLinks.getUserCenterGetUserMsg(), requestMap);
        Map responseMap = JSONUtil.json2Bean(httpRespMsg.getContent(), Map.class);
        owner.setName(responseMap.get("nickName").toString());
        owner.setPhone(responseMap.get("phone").toString());
        return owner;
    }

    /**
     * 设计师 & 项目经理
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

    /**
     * 公司名称
     * TODO 批量
     *
     * @param companyId
     * @return
     */
    public String getCompanyInfo(String companyId) {
        String companyName = constructionOrderMapper.getCompanyName(companyId);
        return companyName;
    }

    /**
     * 查询 施工阶段
     *
     * @param stage
     * @return
     */
    public String getContstructionStage(int stage) {
        ProjectBigSchedulingExample example = new ProjectBigSchedulingExample();
        example.createCriteria().andSortEqualTo(stage);
        List<ProjectBigScheduling> list = projectBigSchedulingMapper.selectByExample(example);
        return list.get(0).getName();
    }

    /**
     * 查询 延期天数
     *
     * @param listProjectNo
     * @return
     */
    public List<ProjectScheduling> getdelayDay(List<String> listProjectNo) {
        ProjectSchedulingExample example = new ProjectSchedulingExample();
        example.createCriteria().andProjectNoIn(listProjectNo);
        return projectSchedulingMapper.selectByExample(example);
    }

    /**
     * 查询 延期天数
     *
     * @param listProjectNo
     * @return
     */
    public Map<String, Integer> getApprove(List<String> listProjectNo) {
        return afInstanceService.getProjectCheckResult(listProjectNo);
    }

    /**
     * 查询 支付
     *
     * @param listProjectNo
     * @return
     */
    public List<FundsOrder> getFundsOrder(List<String> listProjectNo) {
        FundsOrderExample example = new FundsOrderExample();
        example.createCriteria().andProjectNoIn(listProjectNo);
        List<FundsOrder> list = fundsOrderMapper.selectByExample(example);
        return list;
    }


    //查询合同
    public List<ConstructionOrder> getMoney(List<String> listOrdertNo) {
        ConstructionOrderExample constructionOrderExample = new ConstructionOrderExample();
        constructionOrderExample.createCriteria().andOrderNoIn(listOrdertNo);
        return constructionOrderMapper.selectByExample(constructionOrderExample);
    }

    //查询竣工
    public List<ProjectScheduling> getProjectScheduling(List<String> listProjectNo) {
        ProjectSchedulingExample projectSchedulingExample = new ProjectSchedulingExample();
        projectSchedulingExample.createCriteria().andProjectNoIn(listProjectNo);
        return projectSchedulingMapper.selectByExample(projectSchedulingExample);
    }
}
