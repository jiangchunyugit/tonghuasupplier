package cn.thinkfree.service.pcUser;

import cn.thinkfree.core.security.utils.MultipleMd5;
import cn.thinkfree.database.constants.Contant;
import cn.thinkfree.database.mapper.CompanyInfoMapper;
import cn.thinkfree.database.mapper.PcUserInfoMapper;
import cn.thinkfree.database.mapper.UserRegisterMapper;
import cn.thinkfree.database.model.CompanyInfo;
import cn.thinkfree.database.model.PcUserInfo;
import cn.thinkfree.database.model.UserRegister;
import cn.thinkfree.database.vo.PcUserInfoVo;
import cn.thinkfree.database.vo.UserVO;
import cn.thinkfree.service.utils.UserNoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PcUserInfoServiceImpl implements PcUserInfoService {

    @Autowired
    PcUserInfoMapper pcUserInfoMapper;

    @Autowired
    UserRegisterMapper userRegisterMapper;

    @Autowired
    CompanyInfoMapper companyInfoMapper;

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

    /**
     * 添加账户
     * @param userVO
     * @param pcUserInfo
     * @param userRegister
     * @return
     */
    @Override
    @Transactional
    public boolean saveUserInfo(UserVO userVO, PcUserInfo pcUserInfo, UserRegister userRegister) {
        Date date = new Date();
        pcUserInfo.setCreateTime(date);
        pcUserInfo.setId(UserNoUtils.getUserNo("pc"));
        pcUserInfo.setRootCompanyId(userVO.getPcUserInfo().getRootCompanyId());
        short c = 0;
        pcUserInfo.setEnabled(c);
        if(userVO.getPcUserInfo().getLevel() == Contant.COMPANY_ADMIN){
            pcUserInfo.setLevel(Contant.COMPANY_PROVINCE);
        }else if (userVO.getPcUserInfo().getLevel() == Contant.COMPANY_PROVINCE){
            pcUserInfo.setLevel(Contant.COMPANY_CITY);
        }else{
            pcUserInfo.setLevel(Contant.COMPANY_CITY);
        }
        pcUserInfo.setParentCompanyId(userVO.getCompanyID());
        //TODO 省市区未存
//        CompanyInfo companyInfo = companyInfoMapper.findByCompanyId(pcUserInfo.getCompanyId());
//        pcUserInfo.setCity(companyInfo.getCityCode());


        userRegister.setRegisterTime(date);
        userRegister.setType(Contant.REGISTER_TYPE);
        userRegister.setUpdateTime(date);
        MultipleMd5 md5 = new MultipleMd5();
        //加密
        userRegister.setPassword(md5.encode(userRegister.getPassword()));

        int pcLine = pcUserInfoMapper.insertSelective(pcUserInfo);
        int regLine = userRegisterMapper.insertSelective(userRegister);

        boolean flag = false;
        if(pcLine > 0 && regLine > 0){
            flag = true;
        }
        return flag;
    }

    /**
     * 更新用户信息
     * @param pcUserInfo
     * @param userRegister
     * @return
     */
    @Override
    @Transactional
    public boolean updateUserInfo(PcUserInfo pcUserInfo, UserRegister userRegister) {
        int pcLine = pcUserInfoMapper.insertSelective(pcUserInfo);
        userRegister.setUpdateTime(new Date());
        int regLine = userRegisterMapper.insertSelective(userRegister);

        boolean flag = false;
        if(pcLine > 0 && regLine > 0){
            flag = true;
        }
        return flag;
    }

    /**
     * 单个用户信息查询
     * @param userId
     * @return
     */
    @Override
    public PcUserInfoVo findByUserId(String userId) {
        PcUserInfoVo pcUserInfoVo = pcUserInfoMapper.findByUserId(userId);
        MultipleMd5 md5 = new MultipleMd5();
        //TODO  解密？？？
//        pcUserInfoVo.setPassword(md5.matches());
        return pcUserInfoVo;
    }

}
