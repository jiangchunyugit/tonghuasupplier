package cn.thinkfree.service.sysMsg;

import cn.thinkfree.database.mapper.SystemMessageMapper;
import cn.thinkfree.database.model.PcUserInfo;
import cn.thinkfree.database.model.SystemMessage;
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
    public PageInfo<SystemMessage> selectByParam(UserVO userVO, Integer no, Integer pageSize, Object sendUserId, String sendTime) {
        Map<String, Object> param = new HashMap<>();

        if(null == sendUserId) sendUserId = "";
        if(null == sendTime) sendTime = "";
        param.put("sendTime", sendTime);
        param.put("sendUserId", sendUserId);
        param.put("companyId", userVO.getRelationMap());

        PageHelper.startPage(no,pageSize);
        List<SystemMessage> systemMessage = sysMsgMapper.selectByParam(param);
        PageInfo<SystemMessage> pageInfo = new PageInfo<>(systemMessage);
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
