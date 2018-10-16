package cn.thinkfree.service.platform.employee.service.impl;

import cn.thinkfree.database.mapper.EmployeeApplyLogMapper;
import cn.thinkfree.database.mapper.EmployeeMsgMapper;
import cn.thinkfree.database.mapper.UserRoleSetMapper;
import cn.thinkfree.database.model.*;
import cn.thinkfree.service.platform.employee.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public void reviewEmployee(String userId, int authState) {
        EmployeeMsgExample msgExample = new EmployeeMsgExample();
        msgExample.createCriteria().andUserIdEqualTo(userId);
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
        if(applyLog != null){
            throw new RuntimeException("有未处理的申请");
        }
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
        applyLog.setDealState(1);
        applyLog.setUserId(userId);
        applyLog.setCompanyId(companyId);
        res = applyLogMapper.insertSelective(applyLog);
        logger.info("保存用户申请记录：res={}", res);
    }

    /**
     * 查询是否有未处理的申请
     * @param userId
     * @return
     */
    private EmployeeApplyLog queryApplyLog(String userId) {
        EmployeeApplyLogExample invalidExample = new EmployeeApplyLogExample();
        invalidExample.createCriteria().andDealStateEqualTo(2).andInvalidTimeGreaterThanOrEqualTo(new Date());
        EmployeeApplyLog applyLog = new EmployeeApplyLog();
        applyLog.setDealState(3);
        int res = applyLogMapper.insertSelective(applyLog);
        logger.info("跟新申请信息为已过期：res={}", res);
        //查询改用户是否有未处理的申请
        EmployeeApplyLogExample applyLogExample = new EmployeeApplyLogExample();
        applyLogExample.createCriteria().andUserIdEqualTo(userId).andDealStateEqualTo(2).andInvalidTimeGreaterThanOrEqualTo(new Date());
        List<EmployeeApplyLog> applyLogs = applyLogMapper.selectByExample(applyLogExample);
        if(applyLogs.isEmpty()){
            return null;
        }
        return applyLogs.get(0);
    }

    @Override
    public void dealApply(String userId, int employeeApplyState, String dealExplain, String dealUserId, String companyId) {
        if (employeeApplyState == 1 || employeeApplyState == 4) {
            throw new RuntimeException("申请状态异常");
        }
        EmployeeApplyLog applyLog = queryApplyLog(userId);
        if(applyLog == null){
            throw new RuntimeException("没有查询到申请记录");
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
        int res = employeeMsgMapper.updateByExampleSelective(employeeMsg, employeeMsgExample);
        logger.info("更新用户信息：res={}", res);
        EmployeeApplyLogExample employeeApplyLogExample = new EmployeeApplyLogExample();
        employeeApplyLogExample.createCriteria().andUserIdEqualTo(userId).andDealStateEqualTo(2);
        applyLog = new EmployeeApplyLog();
        applyLog.setDealTime(new Date());
        applyLog.setDealExplain(dealExplain);
        applyLog.setDealUserId(dealUserId);
        //1，已处理，2未处理，3已过期
        applyLog.setDealState(2);
        res = applyLogMapper.updateByExampleSelective(applyLog, employeeApplyLogExample);
        logger.info("更新申请记录：res={}", res);
    }

    @Override
    public void submitCardMsg(String userId, int cardType, String cardNo, String realName) {
        EmployeeMsg employeeMsg = new EmployeeMsg();
        employeeMsg.setCertificateType(cardType);
        employeeMsg.setCertificate(cardNo);
        employeeMsg.setRealName(realName);
        EmployeeMsgExample employeeMsgExample = new EmployeeMsgExample();
        employeeMsgExample.createCriteria().andUserIdEqualTo(userId);
        int res = employeeMsgMapper.updateByExampleSelective(employeeMsg, employeeMsgExample);
        logger.info("保存用户实名认证资料：res={}", res);
    }

    @Override
    public List<UserRoleSet> queryRoles() {
        UserRoleSetExample roleSetExample = new UserRoleSetExample();
        roleSetExample.createCriteria();
        return roleSetMapper.selectByExample(roleSetExample);
    }

    @Override
    public void setUserRole(String userId, String roleCode) {
        EmployeeMsg employeeMsg = new EmployeeMsg();
        employeeMsg.setRoleCode(roleCode);
        EmployeeMsgExample employeeMsgExample = new EmployeeMsgExample();
        employeeMsgExample.createCriteria().andUserIdEqualTo(userId);
        int res = employeeMsgMapper.updateByExampleSelective(employeeMsg, employeeMsgExample);
        logger.info("保存用户角色：res={}", res);
    }
}
