package cn.thinkfree.service.user;

import cn.thinkfree.database.model.PcUserInfo;
import cn.thinkfree.database.model.UserInfo;
import cn.thinkfree.database.vo.UserVO;

import java.util.List;

public interface UserInfoService {
    List<UserInfo> selectByParam(UserVO userVO);
}
