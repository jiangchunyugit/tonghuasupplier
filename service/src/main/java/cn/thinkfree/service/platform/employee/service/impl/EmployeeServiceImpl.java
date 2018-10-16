package cn.thinkfree.service.platform.employee.service.impl;

import cn.thinkfree.database.mapper.EmployeeApplyLogMapper;
import cn.thinkfree.database.mapper.EmployeeMsgMapper;
import cn.thinkfree.database.model.EmployeeApplyLog;
import cn.thinkfree.database.model.EmployeeApplyLogExample;
import cn.thinkfree.database.model.EmployeeMsg;
import cn.thinkfree.database.model.EmployeeMsgExample;
import cn.thinkfree.service.platform.employee.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author xusonghui
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMsgMapper employeeMsgMapper;
    @Autowired
    private EmployeeApplyLogMapper applyLogMapper;

    @Override
    public void reviewEmployee(String userId, int authState) {
        EmployeeMsgExample msgExample = new EmployeeMsgExample();
        msgExample.createCriteria().andUserIdEqualTo(userId);
        EmployeeMsg employeeMsg = new EmployeeMsg();
        employeeMsg.setAuthState(authState);
        employeeMsgMapper.updateByExampleSelective(employeeMsg, msgExample);
    }

    @Override
    public void employeeApply(String userId, int employeeApplyState) {
        if (employeeApplyState != 1 && employeeApplyState != 4) {
            throw new RuntimeException("申请状态异常");
        }
        //1入驻待审核，2入驻不通过，3已入驻，4解约待审核，5解约不通过，6已解约
        EmployeeMsgExample employeeMsgExample = new EmployeeMsgExample();
        employeeMsgExample.createCriteria().andUserIdEqualTo(userId);
        //1在职，2离职
        int employeeState;
        if (employeeApplyState == 1 || employeeApplyState == 2 || employeeApplyState == 6) {
            employeeState = 2;
        } else {
            employeeState = 1;
        }
        EmployeeMsg employeeMsg = new EmployeeMsg();
        employeeMsg.setEmployeeState(employeeState);
        employeeMsgMapper.updateByExampleSelective(employeeMsg, employeeMsgExample);
        EmployeeApplyLog applyLog = new EmployeeApplyLog();
        applyLog.setApplyTime(new Date());
        //1，已处理，2未处理
        applyLog.setDealState(1);
        applyLog.setUserId(userId);
        applyLogMapper.insertSelective(applyLog);
    }

    @Override
    public void dealApply(String userId, int employeeApplyState, String dealExplain, String dealUserId) {
        if (employeeApplyState == 1 || employeeApplyState == 4) {
            throw new RuntimeException("申请状态异常");
        }
        //1入驻待审核，2入驻不通过，3已入驻，4解约待审核，5解约不通过，6已解约
        EmployeeMsgExample employeeMsgExample = new EmployeeMsgExample();
        employeeMsgExample.createCriteria().andUserIdEqualTo(userId);
        //1在职，2离职
        EmployeeMsg employeeMsg = new EmployeeMsg();
        int employeeState;
        if (employeeApplyState == 1 || employeeApplyState == 2 || employeeApplyState == 6) {
            employeeState = 2;
        } else {
            employeeState = 1;
        }
        if(employeeApplyState == 3){
            employeeMsg.setBindDate(new Date());
        }
        employeeMsg.setEmployeeState(employeeState);
        employeeMsgMapper.updateByExampleSelective(employeeMsg, employeeMsgExample);
        EmployeeApplyLogExample employeeApplyLogExample = new EmployeeApplyLogExample();
        employeeApplyLogExample.createCriteria().andUserIdEqualTo(userId).andDealStateEqualTo(2);
        EmployeeApplyLog applyLog = new EmployeeApplyLog();
        applyLog.setDealTime(new Date());
        applyLog.setDealExplain(dealExplain);
        applyLog.setDealUserId(dealUserId);
        //1，已处理，2未处理
        applyLog.setDealState(2);
        applyLogMapper.updateByExampleSelective(applyLog, employeeApplyLogExample);
    }
}
