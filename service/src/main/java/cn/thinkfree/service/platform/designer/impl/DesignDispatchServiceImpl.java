package cn.thinkfree.service.platform.designer.impl;

import cn.thinkfree.core.constants.DesignStateEnum;
import cn.thinkfree.database.mapper.DesignOrderMapper;
import cn.thinkfree.database.mapper.OptionLogMapper;
import cn.thinkfree.database.mapper.ProjectMapper;
import cn.thinkfree.database.model.*;
import cn.thinkfree.service.platform.designer.DesignDispatchService;
import cn.thinkfree.service.platform.designer.vo.DesignOrderVo;
import cn.thinkfree.service.platform.designer.vo.PageVo;
import cn.thinkfree.service.utils.DateUtils;
import cn.thinkfree.service.utils.ReflectUtils;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author xusonghui
 * 设计派单服务实现类
 */
@Service
public class DesignDispatchServiceImpl implements DesignDispatchService {

    @Autowired
    private OptionLogMapper optionLogMapper;
    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private DesignOrderMapper designOrderMapper;

    /**
     * 查询设计订单，主表为design_order,附表为project
     *
     * @param projectNo          订单编号（project.project_no）
     * @param userMsg            业主姓名或电话（调用用户中心查询获取userId）
     * @param orderSource        订单来源（project.order_source）
     * @param createTimeStart    创建时间开始（design_order.create_time）
     * @param createTimeEnd      创建时间结束（design_order.create_time）
     * @param styleCode          装饰风格（design_order.type）
     * @param money              装修预算（project.decoration_budget）
     * @param acreage            建筑面积（project.area）
     * @param designerOrderState 订单状态（design_order.order_stage）
     * @param companyState       公司状态（根据状态查询公司）
     * @param optionUserName     操作人姓名（option_log.option_user_name）
     * @param optionTimeStart    操作时间开始（option_log.option_time）
     * @param optionTimeEnd      操作时间结束（option_log.option_time）
     * @return
     */
    @Override
    public PageVo<List<DesignOrderVo>> queryDesignerOrder(
            String projectNo, String userMsg, String orderSource, String createTimeStart, String createTimeEnd,
            String styleCode, String money, String acreage, int designerOrderState, String companyState, String optionUserName,
            String optionTimeStart, String optionTimeEnd, int pageSize, int pageIndex) {
        //TODO 模糊查询用户信息
        List<String> userIds = new ArrayList<>();
        //TODO 根据公司状态查询公司ID
        List<String> companyIds = new ArrayList<>();
        ProjectExample projectExample = new ProjectExample();
        ProjectExample.Criteria projectCriteria = projectExample.createCriteria();
        if (!userIds.isEmpty()) {
            projectCriteria.andOwnerIdIn(ReflectUtils.listToList(userIds));
        }
        if (!companyIds.isEmpty()) {
            projectCriteria.andCompanyIdIn(ReflectUtils.listToList(companyIds));
        }
        if (StringUtils.isNotBlank(orderSource)) {
            projectCriteria.andOrderSourceEqualTo(Short.parseShort(orderSource));
        }
        if (StringUtils.isNotBlank(projectNo)) {
            projectCriteria.andProjectNoLike(projectNo);
        }
        if (StringUtils.isNotBlank(money)) {
            projectCriteria.andDecorationBudgetEqualTo(Integer.parseInt(money));
        }
        List<Project> projects = projectMapper.selectByExample(projectExample);
        Map<String, Project> projectMap = ReflectUtils.listToMap(projects, "projectNo");
        List<String> projectNos = ReflectUtils.getList(projects, "projectNo");
        OptionLogExample optionLogExample = new OptionLogExample();
        OptionLogExample.Criteria logExampleCriteria = optionLogExample.createCriteria();
        if (StringUtils.isNotBlank(optionUserName)) {
            logExampleCriteria.andOptionUserNameLike(optionUserName);
        }
        if (StringUtils.isNotBlank(optionTimeStart)) {
            logExampleCriteria.andOptionTimeGreaterThanOrEqualTo(DateUtils.strToDate(optionTimeStart));
        }
        if (StringUtils.isNotBlank(optionTimeEnd)) {
            logExampleCriteria.andOptionTimeLessThanOrEqualTo(DateUtils.strToDate(optionTimeEnd));
        }
        List<OptionLog> optionLogs = optionLogMapper.selectByExample(optionLogExample);
        List<String> orderNos = ReflectUtils.getList(optionLogs, "linkNo");
        DesignOrderExample orderExample = new DesignOrderExample();
        DesignOrderExample.Criteria orderExampleCriteria = orderExample.createCriteria();
        if (StringUtils.isNotBlank(createTimeStart)) {
            orderExampleCriteria.andCreateTimeGreaterThanOrEqualTo(DateUtils.strToDate(createTimeStart));
        }
        if (StringUtils.isNotBlank(createTimeEnd)) {
            orderExampleCriteria.andCreateTimeLessThanOrEqualTo(DateUtils.strToDate(createTimeEnd));
        }
        if (StringUtils.isNotBlank(styleCode)) {
            orderExampleCriteria.andTypeEqualTo(Short.parseShort(styleCode));
        }
        if (designerOrderState < 0) {
            orderExampleCriteria.andOrderStageEqualTo(Short.parseShort(designerOrderState + ""));
        }
        if (!projectNos.isEmpty()) {
            orderExampleCriteria.andProjectNoIn(projectNos);
        }
        if (!orderNos.isEmpty()) {
            orderExampleCriteria.andOrderNoIn(orderNos);
        }
        long total = designOrderMapper.countByExample(orderExample);
        PageHelper.startPage(pageIndex - 1, pageSize);
        List<DesignOrder> designOrders = designOrderMapper.selectByExample(orderExample);
        List<DesignOrderVo> designOrderVos = new ArrayList<>();
        for (DesignOrder designOrder : designOrders) {
            DesignOrderVo designOrderVo = new DesignOrderVo();
            Project project = projectMap.get(designOrder.getProjectNo());
            designOrderVo.setProject(project);
            designOrderVo.setDesignOrder(designOrder);
            designOrderVos.add(designOrderVo);
        }
        PageVo<List<DesignOrderVo>> pageVo = new PageVo<>();
        pageVo.setPageSize(pageSize);
        pageVo.setTotal(total);
        pageVo.setData(designOrderVos);
        return pageVo;
    }

    /**
     * 不指派
     *
     * @param projectNo      项目编号
     * @param reason         不派单原因
     * @param optionUserId   操作人ID
     * @param optionUserName 操作人姓名
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void notDispatch(String projectNo, String reason, String optionUserId, String optionUserName) {
        Project project = queryProjectByNo(projectNo);
        DesignOrder designOrder = queryDesignOrder(projectNo);
        //设置该设计订单所属公司
        DesignOrder updateOrder = new DesignOrder();
        updateOrder.setOrderStage(Short.parseShort(DesignStateEnum.STATE_20.getState() + ""));
        DesignOrderExample orderExample = new DesignOrderExample();
        orderExample.createCriteria().andOrderNoEqualTo(designOrder.getOrderNo());
        designOrderMapper.updateByExampleSelective(updateOrder,orderExample);
        //记录操作日志
        OptionLog optionLog = new OptionLog();
        optionLog.setLinkNo(designOrder.getOrderNo());
        optionLog.setOptionTime(new Date());
        optionLog.setOptionType("DO");
        optionLog.setOptionUserId(optionUserId);
        optionLog.setOptionUserName(optionUserName);
        optionLog.setRemark(reason);
        optionLogMapper.insertSelective(optionLog);
    }

    /**
     * 根据项目编号查询项目信息
     *
     * @param projectNo 项目编号
     * @return
     */
    private Project queryProjectByNo(String projectNo) {
        ProjectExample projectExample = new ProjectExample();
        projectExample.createCriteria().andProjectNoEqualTo(projectNo);
        List<Project> projects = projectMapper.selectByExample(projectExample);
        if (projects.isEmpty()) {
            throw new RuntimeException("没有查询到该项目");
        }
        return projects.get(0);
    }

    /**
     * 根据项目编号查询设计订单
     *
     * @param projectNo 项目编号
     * @return
     */
    private DesignOrder queryDesignOrder(String projectNo) {
        DesignOrderExample orderExample = new DesignOrderExample();
        orderExample.createCriteria().andProjectNoEqualTo(projectNo).andStatusEqualTo(1);
        List<DesignOrder> designOrders = designOrderMapper.selectByExample(orderExample);
        if (designOrders.isEmpty()) {
            throw new RuntimeException("没有查询到相关设计订单");
        }
        return designOrders.get(0);
    }

    /**
     * 指派
     *
     * @param projectNo      项目编号
     * @param companyId      公司ID
     * @param optionUserId   操作人ID
     * @param optionUserName 操作人姓名
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void dispatch(String projectNo, String companyId, String optionUserId, String optionUserName) {
        Project project = queryProjectByNo(projectNo);
        DesignOrder designOrder = queryDesignOrder(projectNo);
        //设置该设计订单所属公司
        DesignOrder updateOrder = new DesignOrder();
        updateOrder.setCompanyId(companyId);
        updateOrder.setOrderStage(Short.parseShort(DesignStateEnum.STATE_10.getState() + ""));
        DesignOrderExample orderExample = new DesignOrderExample();
        orderExample.createCriteria().andOrderNoEqualTo(designOrder.getOrderNo());
        designOrderMapper.updateByExampleSelective(updateOrder,orderExample);
        //记录操作日志
        OptionLog optionLog = new OptionLog();
        optionLog.setLinkNo(designOrder.getOrderNo());
        optionLog.setOptionTime(new Date());
        optionLog.setOptionType("DO");
        optionLog.setOptionUserId(optionUserId);
        optionLog.setOptionUserName(optionUserName);
        optionLog.setRemark("指派订单给公司【" + companyId + "】");
        optionLogMapper.insertSelective(optionLog);
    }
}
