package cn.thinkfree.service.sysMsg;

import cn.thinkfree.database.model.PcUserInfo;
import cn.thinkfree.database.model.SystemMessage;
import cn.thinkfree.database.vo.UserVO;

import java.util.List;
import java.util.Map;

public interface SystemMessageService {
    /**
     * 根据id删除公告信息
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 根据id删除公告信息
     * @param id
     * @return
     */
    SystemMessage selectByPrimaryKey(Integer id);

    /**
     * 根据条件查询公告信息
     * @param sendUserId
     * @param sendTime
     * @return
     */
    List<SystemMessage> selectByParam(UserVO userVO, Integer no, Integer pageSize, Object sendUserId, String sendTime);

    int saveSysMsg(PcUserInfo userInfo, SystemMessage record);
}
