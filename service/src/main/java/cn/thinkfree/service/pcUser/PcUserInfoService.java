package cn.thinkfree.service.pcUser;

import cn.thinkfree.database.model.PcUserInfo;
import cn.thinkfree.database.vo.PcUserInfoVo;
import cn.thinkfree.database.vo.UserVO;

import java.util.List;

public interface PcUserInfoService {
    List<PcUserInfo> selectByParam(UserVO userVO);
    List<PcUserInfoVo> findByParam(UserVO userVO, String param);
    boolean delPcUserInfo(String userId);

}
