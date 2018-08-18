package cn.thinkfree.service.pcUser;

import cn.thinkfree.database.mapper.PcUserInfoMapper;
import cn.thinkfree.database.mapper.UserRegisterMapper;
import cn.thinkfree.database.model.PcUserInfo;
import cn.thinkfree.database.model.UserRegister;
import cn.thinkfree.database.vo.PcUserInfoVo;
import cn.thinkfree.database.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PcUserInfoServiceImpl implements PcUserInfoService {

    @Autowired
    PcUserInfoMapper pcUserInfoMapper;

    @Autowired
    UserRegisterMapper userRegisterMapper;

    @Override
    public List<PcUserInfo> selectByParam(UserVO userVO) {
        return pcUserInfoMapper.selectByParam(userVO.getRelationMap());
    }

    /**
     * 权限管理  模糊查询
     * @param param
     * @return
     */
    @Override
    public List<PcUserInfoVo> findByParam(UserVO userVO, String param) {
        Map<String, Object> params = new HashMap<>();
        if(null == param || "".equals(param)){
            param = "";
        }else{
            param = "%" + param +"%";
        }
        params.put("param", param);
        params.put("companyId", userVO.getRelationMap());
        return pcUserInfoMapper.findByParam(params);
    }

    /**
     * 删除账户 pc_user_info  user_register
     * @param userId
     * @return
     */
    @Override
    @Transactional
    public boolean delPcUserInfo(String userId) {
        boolean flag = false;
        int pcline = pcUserInfoMapper.deleteByPrimaryKey(userId);
        int regLine = userRegisterMapper.deleteByUserId(userId);
        if(pcline > 0 && regLine > 0){
            flag = true;
        }
        return flag;
    }
}
