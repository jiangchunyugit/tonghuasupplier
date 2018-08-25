package cn.thinkfree.service.sysMsg;

import cn.thinkfree.core.security.filter.util.SessionUserDetailsUtil;
import cn.thinkfree.database.mapper.SystemMessageMapper;
import cn.thinkfree.database.mapper.UserRoleSetMapper;
import cn.thinkfree.database.model.PcUserInfo;
import cn.thinkfree.database.model.SystemMessage;
import cn.thinkfree.database.model.UserRoleSet;
import cn.thinkfree.database.model.UserRoleSetExample;
import cn.thinkfree.database.vo.SystemMessageVo;
import cn.thinkfree.database.vo.UserVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class SystemMessageServiceImpl implements SystemMessageService {


    @Autowired
    SystemMessageMapper sysMsgMapper;

    @Autowired
    UserRoleSetMapper userRoleSetMapper;


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
            String[] roleId = vo.getReceiveRole().split(",");
            String roleName = "";
            for(int i = 0; i < roleId.length; i++){
                roleName += map.get(roleId[i]) + " ";
            }
            vo.setRoleName(roleName);
        }
        PageInfo<SystemMessageVo> pageInfo = new PageInfo<>(systemMessage);
        return pageInfo;
    }

    @Override
    @Transactional
    public int saveSysMsg(PcUserInfo userInfo, SystemMessage record) {
        //TODO  发布消息 触发事件
        record.setSendUserId(userInfo.getId());
        record.setSendUser(userInfo.getName());
        record.setCompanyId(userInfo.getCompanyId());
        record.setSendTime(new Date());
        return sysMsgMapper.insertSelective(record);
    }
}
