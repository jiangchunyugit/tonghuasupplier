package cn.thinkfree.service.sysMsg;

import cn.thinkfree.core.base.MyLogger;
import cn.thinkfree.core.utils.LogUtil;
import cn.thinkfree.database.mapper.SystemMessageMapper;
import cn.thinkfree.database.model.PcUserInfo;
import cn.thinkfree.database.model.SystemMessage;
import cn.thinkfree.database.vo.UserVO;
import cn.thinkfree.service.sysMsg.SystemMessageService;
import com.github.pagehelper.PageHelper;
import com.sun.xml.internal.bind.v2.TODO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.ToDoubleBiFunction;

@Service
public class SystemMessageServiceImpl implements SystemMessageService {

    MyLogger logger = LogUtil.getLogger(SystemMessageService.class);

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
    public List<SystemMessage> selectByParam(UserVO userVO, Integer no, Integer pageSize, Object sendUserId, String sendTime) {
        Map<String, Object> param = new HashMap<>();
        try{
            if(null == sendUserId) sendUserId = "";
            if(null == sendTime) sendTime = "";
            param.put("sendTime", sendTime);
            param.put("sendUserId", sendUserId);
            if(!userVO.isRoot()){
                //主公司
                param.put("companyId", userVO.getRelationMap());
            }else{
                List<String> list = new ArrayList<>();
                list.add(userVO.getCompanyID());
                param.put("companyId", list);
            }

        }catch (Exception e){
            logger.error("error:",e);
            e.printStackTrace();
        }
        PageHelper.startPage(no,pageSize);
        return sysMsgMapper.selectByParam(param);
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
