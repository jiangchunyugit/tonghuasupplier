package cn.thinkfree.service.user;

import cn.thinkfree.database.mapper.UserInfoMapper;
import cn.thinkfree.database.model.PcUserInfo;
import cn.thinkfree.database.model.UserInfo;
import cn.thinkfree.database.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class UserInfoServiceImpl implements UserInfoService{

    @Autowired
    UserInfoMapper userInfoMapper;

    @Override
    public List<UserInfo> selectByParam(UserVO userVO) {
        if(!userVO.isRoot()){
            return userInfoMapper.selectByParam(userVO.getRelationMap());
        }
        List<String> list = new ArrayList<>();
        list.add(userVO.getCompanyID());
        return userInfoMapper.selectByParam(list);
    }
}
