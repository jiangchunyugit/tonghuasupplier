package cn.thinkfree.service.platform.employee.impl;

import cn.thinkfree.database.mapper.CompanyInfoMapper;
import cn.thinkfree.database.mapper.EmployeeApplyLogMapper;
import cn.thinkfree.database.mapper.EmployeeMsgMapper;
import cn.thinkfree.database.mapper.UserRoleSetMapper;
import cn.thinkfree.database.model.*;
import cn.thinkfree.service.platform.basics.BasicsService;
import cn.thinkfree.service.platform.designer.UserCenterService;
import cn.thinkfree.service.platform.employee.EmployeeService;
import cn.thinkfree.service.platform.vo.EmployeeMsgVo;
import cn.thinkfree.service.platform.vo.PageVo;
import cn.thinkfree.service.platform.vo.RoleVo;
import cn.thinkfree.service.platform.vo.UserMsgVo;
import cn.thinkfree.service.utils.DateUtils;
import cn.thinkfree.service.utils.OrderNoUtils;
import cn.thinkfree.service.utils.ReflectUtils;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author xusonghui
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private EmployeeMsgMapper employeeMsgMapper;
    @Autowired
    private EmployeeApplyLogMapper applyLogMapper;
    @Autowired
    private UserRoleSetMapper roleSetMapper;
    @Autowired
    private CompanyInfoMapper companyInfoMapper;
    @Autowired
    private UserCenterService userCenterService;
    @Autowired
    private BasicsService basicsService;

    @Override
    public void reviewEmployee(String userId, int authState, String companyId) {
        checkCompanyExit(companyId);
        EmployeeMsgExample msgExample = new EmployeeMsgExample();
        msgExample.createCriteria().andUserIdEqualTo(userId).andCompanyIdEqualTo(companyId);
        List<EmployeeMsg> employeeMsgs = employeeMsgMapper.selectByExample(msgExample);
        if (employeeMsgs.isEmpty()) {
            throw new RuntimeException("没有查询到该员工");
        }
        EmployeeMsg employeeMsg = new EmployeeMsg();
        employeeMsg.setAuthState(authState);
        int res = employeeMsgMapper.updateByExampleSelective(employeeMsg, msgExample);
        logger.info("更新用户实名认证审核状态：res={}", res);
    }

    @Override
    public void employeeApply(String userId, int employeeApplyState, String companyId) {
        if (employeeApplyState != 1 && employeeApplyState != 4) {
            throw new RuntimeException("申请状态异常");
        }
        EmployeeApplyLog applyLog = queryApplyLog(userId);
        if (applyLog != null) {
            throw new RuntimeException("有未处理的申请");
        }
        checkCompanyExit(companyId);
        if (employeeApplyState == 4) {
            dissolutionApply(userId, employeeApplyState, companyId);
        } else if (employeeApplyState == 1) {
            EmployeeMsg employeeMsg = checkEmployeeMsg(userId);
            if (Arrays.asList(new Integer[]{3, 4, 5}).contains(employeeMsg.getEmployeeApplyState())) {
                throw new RuntimeException("无效的操作");
            }
            employeeMsg.setCompanyId(companyId);
            employeeMsg.setEmployeeState(employeeApplyState);
            employeeMsg.setApplyTime(new Date());
            int res = employeeMsgMapper.updateByPrimaryKey(employeeMsg);
            logger.info("新增用户绑定记录：res={}", res);
            applyLog = new EmployeeApplyLog();
            applyLog.setApplyTime(new Date());
            //设置3个小时后失效
            applyLog.setInvalidTime(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 3));
            //1，已处理，2未处理，3已过期
            applyLog.setDealState(2);
            applyLog.setUserId(userId);
            applyLog.setCompanyId(companyId);
            res = applyLogMapper.insertSelective(applyLog);
            logger.info("保存用户申请记录：res={}", res);
        }
    }

    /**
     * 检查用户是否存在，不存在则创建
     *
     * @param userId
     * @return
     */
    private EmployeeMsg checkEmployeeMsg(String userId) {
        EmployeeMsg employeeMsg = employeeMsgMapper.selectByPrimaryKey(userId);
        if (employeeMsg != null) {
            return employeeMsg;
        }
        employeeMsg = new EmployeeMsg();
        employeeMsg.setUserId(userId);
        employeeMsg.setApplyTime(new Date());
        employeeMsg.setEmployeeApplyState(-1);
        employeeMsg.setAuthState(1);
        employeeMsgMapper.insertSelective(employeeMsg);
        return employeeMsg;
    }

    private void dissolutionApply(String userId, int employeeApplyState, String companyId) {
        //1入驻待审核，2入驻不通过，3已入驻，4解约待审核，5解约不通过，6已解约
        EmployeeMsgExample employeeMsgExample = new EmployeeMsgExample();
        employeeMsgExample.createCriteria().andUserIdEqualTo(userId);
        //1在职，2离职
        EmployeeMsg employeeMsg = new EmployeeMsg();
        employeeMsg.setEmployeeApplyState(employeeApplyState);
        int res = employeeMsgMapper.updateByExampleSelective(employeeMsg, employeeMsgExample);
        logger.info("更新用户申请状态：res={}", res);
        EmployeeApplyLog applyLog = new EmployeeApplyLog();
        applyLog.setApplyTime(new Date());
        //设置3个小时后失效
        applyLog.setInvalidTime(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 3));
        //1，已处理，2未处理，3已过期
        applyLog.setDealState(2);
        applyLog.setUserId(userId);
        applyLog.setCompanyId(companyId);
        res = applyLogMapper.insertSelective(applyLog);
        logger.info("保存用户申请记录：res={}", res);
    }

    /**
     * 查询是否有未处理的申请
     *
     * @param userId
     * @return
     */
    private EmployeeApplyLog queryApplyLog(String userId) {
        EmployeeApplyLogExample invalidExample = new EmployeeApplyLogExample();
        invalidExample.createCriteria().andDealStateEqualTo(2).andInvalidTimeLessThanOrEqualTo(new Date());
        EmployeeApplyLog applyLog = new EmployeeApplyLog();
        applyLog.setDealState(3);
        int res = applyLogMapper.updateByExampleSelective(applyLog, invalidExample);
        logger.info("跟新申请信息为已过期：res={}", res);
        //查询改用户是否有未处理的申请
        EmployeeApplyLogExample applyLogExample = new EmployeeApplyLogExample();
        applyLogExample.createCriteria().andUserIdEqualTo(userId).andDealStateEqualTo(2).andInvalidTimeGreaterThanOrEqualTo(new Date());
        List<EmployeeApplyLog> applyLogs = applyLogMapper.selectByExample(applyLogExample);
        if (applyLogs.isEmpty()) {
            return null;
        }
        return applyLogs.get(0);
    }

    @Override
    public void dealApply(String userId, int employeeApplyState, String dealExplain, String dealUserId, String roleCode, String companyId) {
        if (employeeApplyState == 1 || employeeApplyState == 4) {
            throw new RuntimeException("申请状态异常");
        }
        EmployeeApplyLog applyLog = queryApplyLog(userId);
        if (applyLog == null) {
            throw new RuntimeException("没有查询到申请记录");
        }
        if (!applyLog.getCompanyId().equals(companyId)) {
            throw new RuntimeException("公司信息异常");
        }
        checkCompanyExit(companyId);
        UserRoleSetExample roleSetExample = new UserRoleSetExample();
        roleSetExample.createCriteria().andRoleCodeEqualTo(roleCode);
        List<UserRoleSet> roleSets = roleSetMapper.selectByExample(roleSetExample);
        if (roleSets.isEmpty()) {
            throw new RuntimeException("无效的角色编码");
        }
        //1入驻待审核，2入驻不通过，3已入驻，4解约待审核，5解约不通过，6已解约
        EmployeeMsgExample employeeMsgExample = new EmployeeMsgExample();
        employeeMsgExample.createCriteria().andUserIdEqualTo(userId);
        //1在职，2离职
        EmployeeMsg employeeMsg = new EmployeeMsg();
        int employeeState;
        if (employeeApplyState == 1 || employeeApplyState == 2 || employeeApplyState == 6) {
            employeeState = 2;
            employeeMsg.setCompanyId("");
        } else {
            employeeState = 1;
            employeeMsg.setCompanyId(companyId);
        }
        if (employeeApplyState == 3) {
            employeeMsg.setBindDate(new Date());
        }
        employeeMsg.setEmployeeState(employeeState);
        employeeMsg.setEmployeeApplyState(employeeApplyState);
        employeeMsg.setRoleCode(roleCode);
        int res = employeeMsgMapper.updateByExampleSelective(employeeMsg, employeeMsgExample);
        logger.info("更新用户信息：res={}", res);
        EmployeeApplyLogExample employeeApplyLogExample = new EmployeeApplyLogExample();
        employeeApplyLogExample.createCriteria().andUserIdEqualTo(userId).andDealStateEqualTo(2);
        applyLog = new EmployeeApplyLog();
        applyLog.setDealTime(new Date());
        applyLog.setDealExplain(dealExplain);
        applyLog.setDealUserId(dealUserId);
        //1，已处理，2未处理，3已过期
        applyLog.setDealState(1);
        res = applyLogMapper.updateByExampleSelective(applyLog, employeeApplyLogExample);
        logger.info("更新申请记录：res={}", res);
    }

    @Override
    public void submitCardMsg(String userId, int cardType, String cardNo, String realName, String country, String photo1, String photo2, String photo3) {
        checkEmployeeMsg(userId);
        List<BasicsData> basicsData = basicsService.cardTypes();
        List<String> types = ReflectUtils.getList(basicsData, "basicsCode");
        if (!types.contains(cardType + "")) {
            throw new RuntimeException("无效的证件类型");
        }
        if (StringUtils.isBlank(cardNo)) {
            throw new RuntimeException("证件编号不能为空");
        }
        if (StringUtils.isBlank(realName)) {
            throw new RuntimeException("真实姓名不能为空");
        }
        if (StringUtils.isBlank(country)) {
            throw new RuntimeException("所属国家编码不能为空");
        }
        if (StringUtils.isBlank(photo1)) {
            throw new RuntimeException("照片1不能为空");
        }
        if (StringUtils.isBlank(photo2)) {
            throw new RuntimeException("照片2不能为空");
        }
        if (StringUtils.isBlank(photo3)) {
            throw new RuntimeException("照片3不能为空");
        }
        EmployeeMsg employeeMsg = new EmployeeMsg();
        employeeMsg.setUserId(userId);
        employeeMsg.setCertificateType(cardType);
        employeeMsg.setCertificate(cardNo);
        employeeMsg.setAuthState(3);
        employeeMsg.setCountryCode(country);
        employeeMsg.setRealName(realName);
        employeeMsg.setCertificatePhotoUrl1(photo1);
        employeeMsg.setCertificatePhotoUrl2(photo2);
        employeeMsg.setCertificatePhotoUrl3(photo3);
        int res = employeeMsgMapper.updateByPrimaryKey(employeeMsg);
        logger.info("保存用户实名认证资料：res={}", res);
    }
    /**
     * 查询角色信息
     * @param searchKey 搜索关键字
     * @param state 角色状态，-1全部，1启用，2未启用
     * @return
     */
    @Override
    public PageVo<List<RoleVo>> queryRoles(String searchKey, int state, int pageSize, int pageIndex) {
        UserRoleSetExample roleSetExample = new UserRoleSetExample();
        //查询展示，且未删除的
        UserRoleSetExample.Criteria criteria = roleSetExample.createCriteria();
        criteria.andIsDelEqualTo(2);
        if(state == 1 || state == 2 || state == 3){
            criteria.andIsShowEqualTo(Short.parseShort(state + ""));
        }
        if (StringUtils.isNotBlank(searchKey)){
            roleSetExample.or().andRoleCodeLike("%" + searchKey + "%");
            roleSetExample.or().andRoleNameLike("%" + searchKey + "%");
        }
        long total = roleSetMapper.countByExample(roleSetExample);
        PageHelper.startPage(pageIndex - 1, pageSize);
        List<UserRoleSet> roleSets = roleSetMapper.selectByExample(roleSetExample);
        List<RoleVo> roleVos = new ArrayList<>();
        for (UserRoleSet userRoleSet : roleSets) {
            RoleVo roleVo = new RoleVo(userRoleSet.getRoleCode(), userRoleSet.getRoleName());
            roleVo.setCreateTime(DateUtils.dateToStr(userRoleSet.getCreateTime()));
            roleVo.setCreateUserName(userRoleSet.getCreator());
            roleVo.setIsEnable(userRoleSet.getIsShow());
            roleVo.setRemark(userRoleSet.getCf());
            roleVos.add(roleVo);
        }
        PageVo<List<RoleVo>> pageVo = new PageVo<>();
        pageVo.setPageIndex(pageIndex);
        pageVo.setPageSize(pageSize);
        pageVo.setData(roleVos);
        pageVo.setTotal(total);
        return pageVo;
    }

    @Override
    public void enableRole(String roleCode, int state) {
        UserRoleSetExample setExample = new UserRoleSetExample();
        setExample.createCriteria().andRoleCodeEqualTo(roleCode);
        UserRoleSet roleSet = new UserRoleSet();
        roleSet.setIsShow(Short.parseShort(state + ""));
        roleSetMapper.updateByExampleSelective(roleSet,setExample);
    }

    @Override
    public void createRole(String roleName, String remark) {
        if (StringUtils.isBlank(roleName)) {
            throw new RuntimeException("角色名称不能为空");
        }
        String roleCode = getCode();
        UserRoleSetExample roleSetExample = new UserRoleSetExample();
        roleSetExample.createCriteria().andIsDelEqualTo(2);
        roleSetExample.or().andRoleCodeEqualTo(roleCode);
        roleSetExample.or().andRoleNameEqualTo(roleName);
        List<UserRoleSet> roleSets = roleSetMapper.selectByExample(roleSetExample);
        if (roleSets.isEmpty()) {
            throw new RuntimeException("该角色编码/角色名称已存在");
        }
        UserRoleSet userRoleSet = new UserRoleSet();
        userRoleSet.setCreateTime(new Date());
        userRoleSet.setIsDel(2);
        //1启用，2未启用，3禁用
        userRoleSet.setIsShow(Short.parseShort("2"));
        userRoleSet.setRoleCode(roleCode);
        userRoleSet.setRoleName(roleName);
        userRoleSet.setCf(remark);
        roleSetMapper.insertSelective(userRoleSet);
    }

    @Override
    public void editRole(String roleCode, String roleName, String remark) {
        UserRoleSetExample roleSetExample = new UserRoleSetExample();
        roleSetExample.createCriteria().andRoleCodeEqualTo(roleCode);
        List<UserRoleSet> roleSets = roleSetMapper.selectByExample(roleSetExample);
        if(roleSets.isEmpty()){
            throw new RuntimeException("无效的角色编码");
        }
        UserRoleSet userRoleSet = roleSets.get(0);
        userRoleSet.setRoleName(roleName);
        userRoleSet.setCf(remark);
        roleSetMapper.updateByPrimaryKeySelective(userRoleSet);
    }

    private String getCode(){
        String roleCode = OrderNoUtils.getCode(6);
        int i = 0;
        while (true){
            UserRoleSetExample roleSetExample = new UserRoleSetExample();
            roleSetExample.createCriteria().andRoleCodeEqualTo(roleCode);
            List<UserRoleSet> roleSets = roleSetMapper.selectByExample(roleSetExample);
            if(roleSets.isEmpty()){
                break;
            }
            roleCode = OrderNoUtils.getCode(6);
            i ++;
            if(i > 50){
                throw new RuntimeException("角色编码重复");
            }
        }
        return roleCode;
    }

    @Override
    public void delRole(String roleCode) {
        UserRoleSet userRoleSet = new UserRoleSet();
        userRoleSet.setIsDel(1);
        UserRoleSetExample setExample = new UserRoleSetExample();
        setExample.createCriteria().andRoleCodeEqualTo(roleCode);
        roleSetMapper.updateByExampleSelective(userRoleSet, setExample);
    }

    @Override
    public void setUserRole(String userId, String roleCode, String companyId) {
        checkCompanyExit(companyId);
        EmployeeMsgExample msgExample = new EmployeeMsgExample();
        msgExample.createCriteria().andUserIdEqualTo(userId).andCompanyIdEqualTo(companyId);
        List<EmployeeMsg> employeeMsgs = employeeMsgMapper.selectByExample(msgExample);
        if (employeeMsgs.isEmpty()) {
            throw new RuntimeException("没有查询到该员工");
        }
        EmployeeMsg employeeMsg = new EmployeeMsg();
        employeeMsg.setRoleCode(roleCode);
        EmployeeMsgExample employeeMsgExample = new EmployeeMsgExample();
        employeeMsgExample.createCriteria().andUserIdEqualTo(userId);
        int res = employeeMsgMapper.updateByExampleSelective(employeeMsg, employeeMsgExample);
        logger.info("保存用户角色：res={}", res);
    }

    @Override
    public EmployeeMsgVo employeeMsgById(String userId) {
        EmployeeMsgExample msgExample = new EmployeeMsgExample();
        msgExample.createCriteria().andUserIdEqualTo(userId).andEmployeeStateEqualTo(1);
        List<EmployeeMsg> employeeMsgs = employeeMsgMapper.selectByExample(msgExample);
        EmployeeMsg employeeMsg;
        if (!employeeMsgs.isEmpty()) {
            employeeMsg = employeeMsgs.get(0);
        } else {
            employeeMsg = new EmployeeMsg();
            employeeMsg.setAuthState(1);
            employeeMsg.setEmployeeApplyState(-1);
        }
        UserMsgVo userMsgVo = userCenterService.queryUser(userId);
        UserRoleSet userRoleSet = queryRoleSet(employeeMsg.getRoleCode());
        List<BasicsData> cardTypes = basicsService.cardTypes();
        List<BasicsData> countryCodes = basicsService.countryType();
        Map<String,BasicsData> cardTypeMap = ReflectUtils.listToMap(cardTypes,"basicsCode");
        Map<String,BasicsData> countryCodeMap = ReflectUtils.listToMap(countryCodes,"basicsCode");
        EmployeeMsgVo msgVo = getEmployeeMsgVo(userRoleSet.getRoleName(),cardTypeMap,countryCodeMap,employeeMsg,userMsgVo);
        return msgVo;
    }

    /**
     * 通过角色编码查询角色信息
     *
     * @param roleCode 角色编码
     * @return
     */
    public UserRoleSet queryRoleSet(String roleCode) {
        if (roleCode == null) {
            return new UserRoleSet();
        }
        UserRoleSetExample roleSetExample = new UserRoleSetExample();
        roleSetExample.createCriteria().andRoleCodeEqualTo(roleCode);
        List<UserRoleSet> userRoleSets = roleSetMapper.selectByExample(roleSetExample);
        if (userRoleSets.isEmpty()) {
            throw new RuntimeException("无效的用户角色");
        }
        return userRoleSets.get(0);
    }

    @Override
    public PageVo<List<EmployeeMsgVo>> queryStaffByPlatform(String roleCode, String searchKey, String city, int pageSize, int pageIndex) {
        if (StringUtils.isBlank(roleCode)) {
            throw new RuntimeException("角色编码不能为空");
        }
        EmployeeMsgExample msgExample = new EmployeeMsgExample();
        EmployeeMsgExample.Criteria criteria = msgExample.createCriteria().andRoleCodeEqualTo(roleCode);
        if(StringUtils.isNotBlank(city)){
            criteria.andCityEqualTo(city);
        }
        if (StringUtils.isNotBlank(searchKey)) {
            msgExample.or().andUserIdLike("%" + searchKey + "%");
            msgExample.or().andRealNameLike("%" + searchKey + "%");
            msgExample.or().andCertificateLike("%" + searchKey + "%");
        }
        long total = employeeMsgMapper.countByExample(msgExample);
        PageHelper.startPage(pageIndex - 1, pageSize);
        List<EmployeeMsg> msgs = employeeMsgMapper.selectByExample(msgExample);
        if (msgs.isEmpty()) {
            PageVo<List<EmployeeMsgVo>> pageVo = new PageVo<>();
            pageVo.setPageSize(pageSize);
            pageVo.setTotal(total);
            pageVo.setData(new ArrayList<>());
            pageVo.setPageIndex(pageIndex);
            return pageVo;
        }
        List<String> userIds = ReflectUtils.getList(msgs, "userId");
        List<RoleVo> roleVos = queryRoles(null,-1,10000,1).getData();
        Map<String, String> roleMap = ReflectUtils.listToMap(roleVos, "roleCode", "roleName");
        Map<String, UserMsgVo> userMsgVoMap = userCenterService.queryUserMap(userIds);
        List<BasicsData> cardTypes = basicsService.cardTypes();
        List<BasicsData> countryCodes = basicsService.countryType();
        Map<String,BasicsData> cardTypeMap = ReflectUtils.listToMap(cardTypes,"basicsCode");
        Map<String,BasicsData> countryCodeMap = ReflectUtils.listToMap(countryCodes,"basicsCode");
        List<EmployeeMsgVo> employeeMsgVos = new ArrayList<>();
        for (EmployeeMsg employeeMsg : msgs) {
            UserMsgVo userMsgVo = userMsgVoMap.get(employeeMsg.getUserId());
            String roleName = roleMap.get(employeeMsg.getRoleCode());
            EmployeeMsgVo msgVo = getEmployeeMsgVo(roleName, cardTypeMap, countryCodeMap, employeeMsg, userMsgVo);
            employeeMsgVos.add(msgVo);
        }
        PageVo<List<EmployeeMsgVo>> pageVo = new PageVo<>();
        pageVo.setPageSize(pageSize);
        pageVo.setTotal(total);
        pageVo.setData(employeeMsgVos);
        pageVo.setPageIndex(pageIndex);
        return pageVo;
    }

    @Override
    public List<EmployeeMsg> queryDesignerByCompanyId(String companyId, String roleCode) {
        EmployeeMsgExample msgExample = new EmployeeMsgExample();
        msgExample.createCriteria().andCompanyIdEqualTo(companyId).andRoleCodeEqualTo(roleCode).andAuthStateEqualTo(2).andEmployeeStateEqualTo(1);
        List<EmployeeMsg> employeeMsgs = employeeMsgMapper.selectByExample(msgExample);
        return employeeMsgs;
    }

    @Override
    public PageVo<List<EmployeeMsgVo>> queryStaffByDesignCompanyId(String companyId, String roleCode, String searchKey, int pageSize, int pageIndex) {
        if (StringUtils.isBlank(roleCode)) {
            throw new RuntimeException("角色编码不能为空");
        }
        EmployeeMsgExample msgExample = new EmployeeMsgExample();
        EmployeeMsgExample.Criteria criteria = msgExample.createCriteria().andRoleCodeEqualTo(roleCode);
        if(StringUtils.isBlank(companyId)){
            throw new RuntimeException("公司ID不能为空");
        }
        criteria.andCompanyIdEqualTo(companyId);
        if (StringUtils.isNotBlank(searchKey)) {
            msgExample.or().andUserIdLike("%" + searchKey + "%");
            msgExample.or().andRealNameLike("%" + searchKey + "%");
            msgExample.or().andCertificateLike("%" + searchKey + "%");
        }
        long total = employeeMsgMapper.countByExample(msgExample);
        PageHelper.startPage(pageIndex - 1, pageSize);
        List<EmployeeMsg> msgs = employeeMsgMapper.selectByExample(msgExample);
        if (msgs.isEmpty()) {
            return PageVo.def(new ArrayList<>());
        }
        List<String> userIds = ReflectUtils.getList(msgs, "userId");
        List<RoleVo> roleVos = queryRoles(null,-1,10000,1).getData();
        Map<String, String> roleMap = ReflectUtils.listToMap(roleVos, "roleCode", "roleName");
        Map<String, UserMsgVo> userMsgVoMap = userCenterService.queryUserMap(userIds);
        List<BasicsData> cardTypes = basicsService.cardTypes();
        List<BasicsData> countryCodes = basicsService.countryType();
        Map<String,BasicsData> cardTypeMap = ReflectUtils.listToMap(cardTypes,"basicsCode");
        Map<String,BasicsData> countryCodeMap = ReflectUtils.listToMap(countryCodes,"basicsCode");
        List<EmployeeMsgVo> employeeMsgVos = new ArrayList<>();
        for (EmployeeMsg employeeMsg : msgs) {
            UserMsgVo userMsgVo = userMsgVoMap.get(employeeMsg.getUserId());
            String roleName = roleMap.get(employeeMsg.getRoleCode());
            EmployeeMsgVo msgVo = getEmployeeMsgVo(roleName, cardTypeMap, countryCodeMap, employeeMsg, userMsgVo);
            employeeMsgVos.add(msgVo);
        }
        PageVo<List<EmployeeMsgVo>> pageVo = new PageVo<>();
        pageVo.setPageSize(pageSize);
        pageVo.setTotal(total);
        pageVo.setData(employeeMsgVos);
        pageVo.setPageIndex(pageIndex);
        return pageVo;
    }

    @Override
    public PageVo<List<EmployeeMsgVo>> queryEmployee(String companyId, String roleCode, String searchKey, int pageSize, int pageIndex) {
        if (StringUtils.isBlank(companyId)) {
            throw new RuntimeException("公司ID不能为空");
        }
        if (StringUtils.isBlank(roleCode)) {
            throw new RuntimeException("角色编码不能为空");
        }
        EmployeeMsgExample msgExample = new EmployeeMsgExample();
        msgExample.createCriteria().andCompanyIdEqualTo(companyId).andRoleCodeEqualTo(roleCode);
        if (StringUtils.isNotBlank(searchKey)) {
            msgExample.or().andUserIdLike("%" + searchKey + "%");
            msgExample.or().andRealNameLike("%" + searchKey + "%");
            msgExample.or().andCertificateLike("%" + searchKey + "%");
        }
        long total = employeeMsgMapper.countByExample(msgExample);
        PageHelper.startPage(pageIndex - 1, pageSize);
        List<EmployeeMsg> msgs = employeeMsgMapper.selectByExample(msgExample);
        if (msgs.isEmpty()) {
            PageVo<List<EmployeeMsgVo>> pageVo = new PageVo<>();
            pageVo.setPageSize(pageSize);
            pageVo.setTotal(total);
            pageVo.setData(new ArrayList<>());
            pageVo.setPageIndex(pageIndex);
            return pageVo;
        }
        List<String> userIds = ReflectUtils.getList(msgs, "userId");
        List<RoleVo> roleVos = queryRoles(null,-1,10000,1).getData();
        Map<String, String> roleMap = ReflectUtils.listToMap(roleVos, "roleCode", "roleName");
        Map<String, UserMsgVo> userMsgVoMap = userCenterService.queryUserMap(userIds);
        List<BasicsData> cardTypes = basicsService.cardTypes();
        List<BasicsData> countryCodes = basicsService.countryType();
        Map<String,BasicsData> cardTypeMap = ReflectUtils.listToMap(cardTypes,"basicsCode");
        Map<String,BasicsData> countryCodeMap = ReflectUtils.listToMap(countryCodes,"basicsCode");
        List<EmployeeMsgVo> employeeMsgVos = new ArrayList<>();
        for (EmployeeMsg employeeMsg : msgs) {
            UserMsgVo userMsgVo = userMsgVoMap.get(employeeMsg.getUserId());
            String roleName = roleMap.get(employeeMsg.getRoleCode());
            EmployeeMsgVo msgVo = getEmployeeMsgVo(roleName, cardTypeMap, countryCodeMap, employeeMsg, userMsgVo);
            employeeMsgVos.add(msgVo);
        }
        PageVo<List<EmployeeMsgVo>> pageVo = new PageVo<>();
        pageVo.setPageSize(pageSize);
        pageVo.setTotal(total);
        pageVo.setData(employeeMsgVos);
        pageVo.setPageIndex(pageIndex);
        return pageVo;
    }

    private EmployeeMsgVo getEmployeeMsgVo(String roleName, Map<String, BasicsData> cardTypeMap, Map<String, BasicsData> countryCodeMap,
                                           EmployeeMsg employeeMsg, UserMsgVo userMsgVo) {
        EmployeeMsgVo msgVo = new EmployeeMsgVo();
        msgVo.setAuthState(employeeMsg.getAuthState());
        if (userMsgVo != null) {
            msgVo.setIconUrl(userMsgVo.getUserIcon());
            msgVo.setPhone(userMsgVo.getUserPhone());
        }
        BasicsData cardType = cardTypeMap.get(employeeMsg.getCertificateType() + "");
        BasicsData countryCode = countryCodeMap.get(employeeMsg.getCountryCode());
        msgVo.setRealName(employeeMsg.getRealName());
        msgVo.setUserId(employeeMsg.getUserId());
        String companyId = employeeMsg.getCompanyId();
        if(StringUtils.isNotBlank(companyId)){
            CompanyInfo companyInfo = checkCompanyExit(companyId);
            msgVo.setCompanyName(companyInfo.getCompanyName());
        }
        //1未绑定，2已绑定，3实名认证审核中，4审核不通过
        int bindCompanyState = 1;
        //1入驻待审核，2入驻不通过，3已入驻，4解约待审核，5解约不通过，6已解约
        bindCompanyState = getBindCompanyState(employeeMsg, bindCompanyState);
        msgVo.setBindCompanyState(bindCompanyState);
        msgVo.setRoleCode(employeeMsg.getRoleCode());
        msgVo.setRoleName(roleName);
        msgVo.setCertificate(employeeMsg.getCertificate());
        msgVo.setCertificatePhotoUrl1(employeeMsg.getCertificatePhotoUrl1());
        msgVo.setCertificatePhotoUrl2(employeeMsg.getCertificatePhotoUrl2());
        msgVo.setCertificatePhotoUrl3(employeeMsg.getCertificatePhotoUrl3());
        msgVo.setCertificateType(employeeMsg.getCertificateType() + "");
        msgVo.setCountryCode(employeeMsg.getCountryCode());
        msgVo.setSex(employeeMsg.getSex() + "");
        msgVo.setEmail(employeeMsg.getEmail());
        msgVo.setWorkTime(employeeMsg.getWorkingTime() + "");
        msgVo.setAddress(employeeMsg.getProvince() + "," + employeeMsg.getCity());
        if(cardType != null){
            msgVo.setCertificateTypeName(cardType.getBasicsName());
        }
        if(countryCode != null){
            msgVo.setCountryCodeName(countryCode.getBasicsName());
        }
        return msgVo;
    }

    private int getBindCompanyState(EmployeeMsg employeeMsg, int bindCompanyState) {
        if(employeeMsg.getEmployeeApplyState() == null){
            return bindCompanyState;
        }
        switch (employeeMsg.getEmployeeApplyState()) {
            case 1:
                bindCompanyState = 3;
                break;
            case 2:
                bindCompanyState = 4;
                break;
            case 3:
                bindCompanyState = 2;
                break;
            case 4:
                bindCompanyState = 2;
                break;
            case 5:
                bindCompanyState = 2;
                break;
            case 6:
                bindCompanyState = 1;
                break;
        }
        return bindCompanyState;
    }

    /**
     * 判断公司是否存在
     *
     * @param companyId 公司ID
     */
    private CompanyInfo checkCompanyExit(String companyId) {
        CompanyInfoExample companyInfoExample = new CompanyInfoExample();
        companyInfoExample.createCriteria().andCompanyIdEqualTo(companyId).andIsDeleteEqualTo(Short.parseShort("2"))
                .andIsCheckEqualTo(Short.parseShort("1")).andAuditStatusEqualTo("8");
        List<CompanyInfo> companyInfos = companyInfoMapper.selectByExample(companyInfoExample);
        if(companyInfos.isEmpty()){
            throw new RuntimeException("没有查询到该公司");
        }
        return companyInfos.get(0);
    }
}
