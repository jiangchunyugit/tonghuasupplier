package cn.thinkfree.service.platform.employee.impl;

import cn.thinkfree.database.mapper.*;
import cn.thinkfree.database.model.*;
import cn.thinkfree.service.platform.designer.UserCenterService;
import cn.thinkfree.service.platform.employee.EmployeeService;
import cn.thinkfree.service.platform.vo.EmployeeMsgVo;
import cn.thinkfree.service.platform.vo.RoleVo;
import cn.thinkfree.service.platform.vo.UserMsgVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        //1入驻待审核，2入驻不通过，3已入驻，4解约待审核，5解约不通过，6已解约
        EmployeeMsgExample employeeMsgExample = new EmployeeMsgExample();
        employeeMsgExample.createCriteria().andUserIdEqualTo(userId);
        //1在职，2离职
        EmployeeMsg employeeMsg = new EmployeeMsg();
        employeeMsg.setEmployeeApplyState(employeeApplyState);
        int res = employeeMsgMapper.updateByExampleSelective(employeeMsg, employeeMsgExample);
        logger.info("更新用户申请状态：res={}", res);
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
        if(roleSets.isEmpty()){
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
    public void submitCardMsg(String userId, int cardType, String cardNo, String realName, String photo1, String photo2, String photo3) {
        EmployeeMsg employeeMsg = new EmployeeMsg();
        employeeMsg.setCertificateType(cardType);
        employeeMsg.setCertificate(cardNo);
        employeeMsg.setRealName(realName);
        employeeMsg.setCertificatePhotoUrl1(photo1);
        employeeMsg.setCertificatePhotoUrl2(photo2);
        employeeMsg.setCertificatePhotoUrl3(photo3);
        EmployeeMsgExample employeeMsgExample = new EmployeeMsgExample();
        employeeMsgExample.createCriteria().andUserIdEqualTo(userId);
        int res = employeeMsgMapper.updateByExampleSelective(employeeMsg, employeeMsgExample);
        logger.info("保存用户实名认证资料：res={}", res);
    }

    @Override
    public List<RoleVo> queryRoles() {
        UserRoleSetExample roleSetExample = new UserRoleSetExample();
        //查询展示，且未删除的
        roleSetExample.createCriteria().andIsShowEqualTo(Short.parseShort("1")).andIsDelEqualTo(2);
        List<UserRoleSet> roleSets = roleSetMapper.selectByExample(roleSetExample);
        List<RoleVo> roleVos = new ArrayList<>();
        for(UserRoleSet userRoleSet : roleSets){
            RoleVo roleVo = new RoleVo(userRoleSet.getRoleCode(),userRoleSet.getRoleName());
            roleVos.add(roleVo);
        }
        return roleVos;
    }

    @Override
    public void createRole(String roleCode, String roleName) {
        UserRoleSetExample roleSetExample = new UserRoleSetExample();
        roleSetExample.createCriteria().andIsDelEqualTo(2);
        roleSetExample.or().andRoleCodeEqualTo(roleCode);
        roleSetExample.or().andRoleNameEqualTo(roleName);
        List<UserRoleSet> roleSets = roleSetMapper.selectByExample(roleSetExample);
        if(roleSets.isEmpty()){
            throw new RuntimeException("该角色已存在");
        }
        UserRoleSet userRoleSet = new UserRoleSet();
        userRoleSet.setCreateTime(new Date());
        userRoleSet.setIsDel(2);
        userRoleSet.setIsShow(Short.parseShort("1"));
        userRoleSet.setRoleCode(roleCode);
        userRoleSet.setRoleName(roleName);
        roleSetMapper.insertSelective(userRoleSet);
    }

    @Override
    public void delRole(String roleCode) {
        UserRoleSet userRoleSet = new UserRoleSet();
        userRoleSet.setIsDel(1);
        UserRoleSetExample setExample = new UserRoleSetExample();
        setExample.createCriteria().andRoleCodeEqualTo(roleCode);
        roleSetMapper.updateByExample(userRoleSet,setExample);
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
        if(employeeMsgs.isEmpty()){
            throw new RuntimeException("没有查询到用户");
        }
        EmployeeMsg employeeMsg = employeeMsgs.get(0);
        UserMsgVo userMsgVo = userCenterService.queryUser(userId);
        UserRoleSet userRoleSet = queryRoleSet(employeeMsg.getRoleCode());
        EmployeeMsgVo msgVo = new EmployeeMsgVo();
        msgVo.setAuthState(employeeMsg.getAuthState());
        msgVo.setIconUrl(userMsgVo.getUserIcon());
        msgVo.setPhone(userMsgVo.getUserPhone());
        msgVo.setRealName(employeeMsg.getRealName());
        msgVo.setUserId(userId);
        msgVo.setCompanyName("这里是公司名称");
        msgVo.setBindCompanyState(StringUtils.isNotBlank(employeeMsg.getCompanyId()) ? 1 : 2);
        msgVo.setRoleCode(userRoleSet.getRoleCode());
        msgVo.setRoleName(userRoleSet.getRoleName());
        return msgVo;
    }

    /**
     * 通过角色编码查询角色信息
     * @param roleCode 角色编码
     * @return
     */
    public UserRoleSet queryRoleSet(String roleCode){
        UserRoleSetExample roleSetExample = new UserRoleSetExample();
        roleSetExample.createCriteria().andRoleCodeEqualTo(roleCode);
        List<UserRoleSet> userRoleSets = roleSetMapper.selectByExample(roleSetExample);
        if(userRoleSets.isEmpty()){
            throw new RuntimeException("无效的用户角色");
        }
        return userRoleSets.get(0);
    }

    /**
     * 判断公司是否存在
     *
     * @param companyId 公司ID
     */
    private void checkCompanyExit(String companyId) {
//        CompanyInfoExample companyInfoExample = new CompanyInfoExample();
//        companyInfoExample.createCriteria().andCompanyIdEqualTo(companyId).andIsDeleteEqualTo(Short.parseShort("2")).andAuditStatusEqualTo("7");
//        List<CompanyInfo> companyInfos = companyInfoMapper.selectByExample(companyInfoExample);
//        if(companyInfos.isEmpty()){
//            throw new RuntimeException("没有查询到该公司");
//        }
    }
}
