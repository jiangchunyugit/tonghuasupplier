package cn.thinkfree.service.sysMsg;

import cn.thinkfree.core.constants.SysConstants;
import cn.thinkfree.core.logger.AbsLogPrinter;
import cn.thinkfree.core.security.filter.util.SessionUserDetailsUtil;
import cn.thinkfree.database.mapper.CompanyUserSetMapper;
import cn.thinkfree.database.mapper.SystemMessageMapper;
import cn.thinkfree.database.mapper.UserRoleSetMapper;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.vo.SystemMessageVo;
import cn.thinkfree.database.vo.UserVO;
import cn.thinkfree.service.constants.UserJobs;
import cn.thinkfree.service.remote.CloudService;
import cn.thinkfree.service.remote.RemoteResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SystemMessageServiceImpl extends AbsLogPrinter implements SystemMessageService {


    @Autowired
    SystemMessageMapper sysMsgMapper;

    @Autowired
    UserRoleSetMapper userRoleSetMapper;

    @Autowired
    CloudService cloudService;

    @Autowired
    CompanyUserSetMapper companyUserSetMapper;

    @Override
    @Transactional
    public int deleteByPrimaryKey(Integer id) {
        return sysMsgMapper.deleteByPrimaryKey(id);
    }

    @Override
    public SystemMessage selectByPrimaryKey(Integer id) {
        return sysMsgMapper.selectByPrimaryKey(id);
    }

    @Override
    public PageInfo<SystemMessageVo> selectByParam(Integer no, Integer pageSize, Object sendUserId, String sendTime) {
        UserVO userVO = (UserVO) SessionUserDetailsUtil.getUserDetails();

        Map<String, Object> param = new HashMap<>();

        if(null == sendUserId) sendUserId = "";
        if(null == sendTime) sendTime = "";
        param.put("sendTime", sendTime);
        param.put("sendUserId", sendUserId);
        param.put("companyId", userVO.getRelationMap());

        PageHelper.startPage(no,pageSize);
        List<SystemMessageVo> systemMessage = sysMsgMapper.selectByParam(param);
        //查询岗位 信息
        UserRoleSetExample example = new UserRoleSetExample();
        //设置岗位显示的信息
        Short isShow = 1;
        example.createCriteria().andIsShowEqualTo(isShow);
        List<UserRoleSet> userRoleSets = userRoleSetMapper.selectByExample(example);
        Map<String, String> map = new HashMap<>();
        for(UserRoleSet userRoleSet: userRoleSets){
            map.put(userRoleSet.getId().toString(),userRoleSet.getRoleName());
        }
        for (SystemMessageVo vo: systemMessage){
            String roleName = "";
            if(null != vo.getReceiveRole()){
                String[] roleId = vo.getReceiveRole().split(",");
                for(int i = 0; i < roleId.length; i++){
                    roleName += map.get(roleId[i]) + " ";
                }
                vo.setRoleName(roleName);
            }else{
                vo.setRoleName(roleName);
            }

        }
        PageInfo<SystemMessageVo> pageInfo = new PageInfo<>(systemMessage);
        return pageInfo;
    }

    @Override
    @Transactional
    public int saveSysMsg(SystemMessage record) {
        UserVO userVO = (UserVO) SessionUserDetailsUtil.getUserDetails();
        record.setSendUserId(userVO.getPcUserInfo().getId());
        record.setSendUser(userVO.getPcUserInfo().getName());
        record.setCompanyId(userVO.getCompanyID());
        record.setSendTime(new Date());
        int row=sysMsgMapper.insertSelective(record);

        printInfoMes("保存完毕,通知远程服务器");
        String[] roles=record.getReceiveRole().split(",");
        List<String> roleCodes = new ArrayList<>();
        for(String role:roles){
            UserJobs userJobs = UserJobs.findByCode(role);
            if(userJobs != null){
                roleCodes.add(userJobs.roleCode);
            }
        }
        if(!roleCodes.isEmpty() ){
            CompanyUserSetExample companyUserSetExample = new CompanyUserSetExample();
            companyUserSetExample.createCriteria().andIsBindEqualTo(SysConstants.YesOrNo.YES.shortVal())
                    .andIsJobEqualTo(SysConstants.YesOrNo.YES.shortVal())
                    .andRoleIdIn(roleCodes)
                    .andCompanyIdIn(userVO.getRelationMap());
            List<CompanyUserSet> companyUserSets = companyUserSetMapper.selectByExample(companyUserSetExample);
            RemoteResult<String> rs = cloudService.sendNotice(record, companyUserSets.stream().map(CompanyUserSet::getUserId).collect(Collectors.toList()));
            if(!rs.isComplete()) throw new RuntimeException("远程公告发送失败!");
        }
        return row;
    }
}
