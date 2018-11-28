package cn.thinkfree.service.platform.designer.impl;

import cn.thinkfree.core.constants.RoleFunctionEnum;
import cn.thinkfree.database.mapper.CompanyInfoMapper;
import cn.thinkfree.database.model.*;
import cn.thinkfree.service.platform.designer.CreatePayOrderService;
import cn.thinkfree.service.platform.designer.DesignDispatchService;
import cn.thinkfree.service.platform.designer.DesignerService;
import cn.thinkfree.service.platform.designer.UserCenterService;
import cn.thinkfree.service.platform.employee.ProjectUserService;
import cn.thinkfree.service.platform.vo.DesignerMsgVo;
import cn.thinkfree.service.platform.vo.UserMsgVo;
import cn.thinkfree.service.utils.HttpUtils;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 创建支付订单
 *
 * @author xusonghui
 */
@Service
public class CreatePayOrderServiceImpl implements CreatePayOrderService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * 创建支付订单url
     */
    private String createPayUrl = "/financialapi/funds/order";
    @Autowired
    private DesignDispatchService designDispatchService;
    @Autowired
    private ProjectUserService projectUserService;
    @Autowired
    private DesignerService designerService;
    @Autowired
    private CompanyInfoMapper companyInfoMapper;
    @Autowired
    private UserCenterService userCenterService;

    /**
     * 创建量房支付订单
     *
     * @param projectNo
     */
    @Override
    public void createVolumeRoomPay(String projectNo, Integer appointmentAmount) {
        Project project = designDispatchService.queryProjectByNo(projectNo);
        if (project == null) {
            logger.error("没有查询到项目编号为【{}】的项目", projectNo);
            throw new RuntimeException("创建量房订单失败");
        }
        DesignerOrder designerOrder = designDispatchService.queryDesignerOrder(projectNo);
        if (designerOrder == null) {
            logger.error("没有查询到项目编号为【{}】的设计订单", projectNo);
            throw new RuntimeException("创建量房订单失败");
        }
        OrderUser ownerMsg = projectUserService.queryOrderUserOne(projectNo, RoleFunctionEnum.OWNER_POWER);
        if (ownerMsg == null) {
            logger.error("没有查询到项目编号为【{}】的业主信息", projectNo);
            throw new RuntimeException("创建量房订单失败");
        }
        OrderUser designerMsg = projectUserService.queryOrderUserOne(projectNo, RoleFunctionEnum.DESIGN_POWER);
        if (designerMsg == null) {
            logger.error("没有查询到项目编号为【{}】的设计师信息", projectNo);
            throw new RuntimeException("创建量房订单失败");
        }
        DesignerMsgVo designerMsgVo = designerService.queryDesignerByUserId(designerMsg.getUserId());
        designerMsgVo.setVolumeRoomMoney(appointmentAmount.toString());
        if (designerMsgVo == null) {
            throw new RuntimeException("创建量房订单失败");
        }
        checkMoney(designerMsgVo.getVolumeRoomMoney());
        CompanyInfo companyInfo = getDesignCompanyMsg(designerOrder.getCompanyId());
        createPay(project, designerOrder, ownerMsg, designerMsg, designerMsgVo, companyInfo);
    }

    private void checkMoney(String money) {
        try {
            new BigDecimal(money);
        } catch (Exception e) {
            throw new RuntimeException("无效的金额");
        }
    }

    /**
     * 根据公司ID查询手机公司信息
     *
     * @param companyId
     * @return
     */
    private CompanyInfo getDesignCompanyMsg(String companyId) {
        if (StringUtils.isBlank(companyId)) {
            throw new RuntimeException("无效的公司ID");
        }
        CompanyInfoExample infoExample = new CompanyInfoExample();
        infoExample.createCriteria().andCompanyIdEqualTo(companyId);
        CompanyInfo companyInfo = companyInfoMapper.selectByCompanyId(companyId);
        if (companyInfo == null) {
            throw new RuntimeException("没有查询到设计公司信息");
        }
        return companyInfo;
    }

    private void createPay(Project project, DesignerOrder designerOrder, OrderUser ownerMsg, OrderUser designerMsg, DesignerMsgVo designerMsgVo, CompanyInfo companyInfo) {
        UserMsgVo ownerMsgVo = userCenterService.queryUser(ownerMsg.getUserId());
        if (ownerMsgVo == null) {
            throw new RuntimeException("没有查询到业主信息");
        }
        Map<String, String> params = new HashMap<>();
        //实际支付费用：总费用扣除<积分优惠券抵扣>剩下的金额
        params.put("actualAmount", designerMsgVo.getVolumeRoomMoney());
        //公司id
        params.put("companyId", companyInfo.getCompanyId());
        //公司名称
        params.put("companyName", companyInfo.getCompanyName());
        //施工阶段(水电工程)
        params.put("constructionStage", "");
        //业主姓名
        params.put("consumerName", ownerMsgVo.getUserName());
        //合同类型：1全款，2分期
        params.put("contractType", "");
        //设计案例id
        params.put("designId", "");
        //设计师id
        params.put("designUserId", designerMsg.getUserId());
        //合同结束时间
        params.put("endTime", "");
        //订单号：设计单号、施工单号、合同编号
        params.put("fromOrderid", designerOrder.getOrderNo());
        //1首款,2尾款,0无
        params.put("isEnd", "0");
        //项目地址
        params.put("projectAddr", project.getAddressDetail());
        //项目编号，是否订单号
        params.put("projectNo", project.getProjectNo());
        //签约时间
        params.put("signedTime", "");
        //阶段排序号
        params.put("sort", "");
        //合同开始时间
        params.put("startTime", "");
        //装修类型(个性化)
        params.put("styleType", project.getStyle());
        //订单类型：设计1、施工2、合同3
        params.put("type", "1");
        //费用类型：量房费
        params.put("typeSub", "1");
        //业主id
        params.put("userId", ownerMsg.getUserId());
        List<Map<String, String>> listParams = new ArrayList<>();
        listParams.add(params);
        HttpUtils.HttpRespMsg httpRespMsg = HttpUtils.postJson(userCenterService.getUrl(createPayUrl), JSONObject.toJSONString(listParams));
        logger.info(JSONObject.toJSONString(httpRespMsg));
        if (httpRespMsg.getResponseCode() != 200) {
            throw new RuntimeException("创建订单服务异常");
        }
        JSONObject data = JSONObject.parseObject(httpRespMsg.getContent());
        if (!"1000".equals(data.getInteger("code") + "")) {
            throw new RuntimeException(data.getString("msg"));
        }
    }
}
