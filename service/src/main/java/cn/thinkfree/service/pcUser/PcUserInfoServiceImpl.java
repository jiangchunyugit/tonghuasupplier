package cn.thinkfree.service.pcUser;

import cn.thinkfree.core.constants.SysConstants;
import cn.thinkfree.core.security.filter.util.SessionUserDetailsUtil;
import cn.thinkfree.core.security.utils.MultipleMd5;
import cn.thinkfree.database.constants.UserEnabled;
import cn.thinkfree.database.constants.UserLevel;
import cn.thinkfree.database.mapper.CompanyInfoMapper;
import cn.thinkfree.database.mapper.PcUserInfoMapper;
import cn.thinkfree.database.mapper.UserRegisterMapper;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.vo.MyPageHelper;
import cn.thinkfree.database.vo.PcUserInfoVo;
import cn.thinkfree.database.vo.UserVO;
import cn.thinkfree.service.constants.UserRegisterType;
import cn.thinkfree.service.utils.UserNoUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
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
     * @param
     * @return
     */
    @Override
    public PageInfo<PcUserInfoVo> findByParam(MyPageHelper myPageHelper) {
        UserVO uservo = (UserVO) SessionUserDetailsUtil.getUserDetails();

        Map<String, Object> params = new HashMap<>();
        Object param = myPageHelper.getData();
        if(null != param && StringUtils.isNotBlank(param.toString())){
            param = "%" + param +"%";
            params.put("param", param);
        }
        params.put("companyId", uservo.getRelationMap());
        PageHelper.startPage(myPageHelper.getPage(), myPageHelper.getRows());
        List<PcUserInfoVo> pcUserInfoVos = pcUserInfoMapper.findByParam(params);
        PageInfo<PcUserInfoVo> pcUserInfoVoPageInfo = new PageInfo<>(pcUserInfoVos);
        return pcUserInfoVoPageInfo;
    }

    /**
     * 删除账户 pc_user_info  user_register
     * @param userId
     * @return
     */
    @Override
    @Transactional
    public boolean delPcUserInfo(String userId) {

//        int pcline = pcUserInfoMapper.deleteByPrimaryKey(userId);

        //注册表修改
        UserRegister userRegister = new UserRegister();
        userRegister.setUserId(userId);
        userRegister.setIsDelete(SysConstants.YesOrNo.YES.shortVal());
        UserRegisterExample exampleReg = new UserRegisterExample();
        exampleReg.createCriteria().andUserIdEqualTo(userId);
        int regLine = userRegisterMapper.updateByExampleSelective(userRegister, exampleReg);
        if(regLine > 0){
            return true;
        }
        return false;
    }

    /**
     * 添加账户
     * @param pcUserInfoVo
     * @return
     */
    @Override
    @Transactional
    public boolean saveUserInfo(PcUserInfoVo pcUserInfoVo) {
        //判断输入的手机号码是否已经注册过
        List<String> phones = userRegisterMapper.findPhoneAll();
        boolean flag = phones.contains(pcUserInfoVo.getPhone());
        if(flag){
            return false;
        }
        UserVO userVO = (UserVO) SessionUserDetailsUtil.getUserDetails();
        String userId = UserNoUtils.getUserNo("PC");
        Date date = new Date();
        pcUserInfoVo.setCreateTime(date);
        pcUserInfoVo.setId(userId);
        pcUserInfoVo.setRootCompanyId(userVO.getPcUserInfo().getRootCompanyId());
        pcUserInfoVo.setEnabled(UserEnabled.Enabled_false.shortVal());
        //根据新增公司id和登录用户公司id 是否相等判断level
        if(userVO.getCompanyID().equals(pcUserInfoVo.getCompanyId())){
            pcUserInfoVo.setLevel(userVO.getPcUserInfo().getLevel());
        }else{
            pcUserInfoVo.setLevel(getLevel(userVO.getPcUserInfo().getLevel()));
        }
        pcUserInfoVo.setParentCompanyId(userVO.getCompanyID());
        //省市区存储
        CompanyInfo companyInfo = companyInfoMapper.findByCompanyId(pcUserInfoVo.getCompanyId());
        if(null != companyInfo){
            pcUserInfoVo.setCity(companyInfo.getCityCode().toString());
            pcUserInfoVo.setProvince(companyInfo.getProvinceCode().toString());
            pcUserInfoVo.setArea(companyInfo.getAreaCode().toString());
        }

        //注册表
        UserRegister userRegister = new UserRegister();
        userRegister.setRegisterTime(date);
        userRegister.setIsDelete(SysConstants.YesOrNo.NO.shortVal());
        userRegister.setType(UserRegisterType.Staff.shortVal());
        userRegister.setUpdateTime(date);
        MultipleMd5 md5 = new MultipleMd5();
        //加密
        /*if(null == pcUserInfoVo.getPassword() || "".equals(pcUserInfoVo.getPassword())){
            pcUserInfoVo.setPassword("123456");
        }*/
        userRegister.setPassword(md5.encode(pcUserInfoVo.getPassword()));
        userRegister.setPhone(pcUserInfoVo.getRegPhone());
        userRegister.setUserId(userId);
        int pcLine = pcUserInfoMapper.insertUserInfoVo(pcUserInfoVo);
        int regLine = userRegisterMapper.insertSelective(userRegister);

        if(pcLine > 0 && regLine > 0){
            return true;
        }
        return false;
    }

    /**
     * 更新用户信息
     * @param
     * @return
     */
    @Override
    @Transactional
    public boolean updateUserInfo(PcUserInfoVo pcUserInfoVo) {
        /*PcUserInfo pcUserInfo = new PcUserInfo();
        pcUserInfo.setId(pcUserInfoVo.getId());
        pcUserInfo.setMemo(pcUserInfoVo.getMemo());
        pcUserInfo.setPhone(pcUserInfoVo.getPhone());
        pcUserInfo.setName(pcUserInfoVo.getName());*/

        UserRegister userRegister = new UserRegister();
        userRegister.setUserId(pcUserInfoVo.getId());
        userRegister.setUpdateTime(new Date());
        MultipleMd5 md5 = new MultipleMd5();
        userRegister.setPassword(md5.encode(pcUserInfoVo.getPassword()));

        int pcLine = pcUserInfoMapper.updateById(pcUserInfoVo);
        int regLine = userRegisterMapper.updateByUserId(userRegister);

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
//        MultipleMd5 md5 = new MultipleMd5();
//        pcUserInfoVo.setPassword(md5.matches());
        return pcUserInfoVo;
    }

    @Override
    @Transactional
    public String updatePassWord(String oldPassWord, String newPassWord) {
        MultipleMd5 md5 = new MultipleMd5();
        String oldPass = md5.encode(oldPassWord);
        UserVO userVO = (UserVO) SessionUserDetailsUtil.getUserDetails();
        if(userVO.getPassword().equals(oldPass)){
            UserRegister userRegister = new UserRegister();
            userRegister.setUserId(userVO.getUserRegister().getUserId());
            //加密
            userRegister.setPassword(md5.encode(newPassWord));
            UserRegisterExample example = new UserRegisterExample();
            example.createCriteria().andUserIdEqualTo(userVO.getUserRegister().getUserId());
            int line = userRegisterMapper.updateByExampleSelective(userRegister, example);
            if(line > 0){
                return "操作成功";
            }
            return "操作失败";
        }
        return "原密码输入错误";

    }

    @Override
    public String canEnabled(String id, Integer enabled) {
        PcUserInfo pcUserInfo = new PcUserInfo();
        pcUserInfo.setId(id);
        pcUserInfo.setEnabled(enabled.shortValue());
        PcUserInfoExample example = new PcUserInfoExample();
        example.createCriteria().andIdEqualTo(id);
        int line = pcUserInfoMapper.updateByExampleSelective(pcUserInfo, example);
        if(line > 0){
            return "操作成功";
        }
        return "操作失败";
    }

    public short getLevel(Short level){
        if(UserLevel.Creator.shortVal() == level){
            return UserLevel.Company_Admin.shortVal();
        }else if(UserLevel.Company_Admin.shortVal() == level){
            return UserLevel.Company_Province.shortVal();
        }else if (UserLevel.Company_Province.shortVal() == level){
            return UserLevel.Company_City.shortVal();
        }else if(UserLevel.Company_City.shortVal() == level){
            return UserLevel.Company_Area.shortVal();
        }else{
            return UserLevel.Company_City_Master.shortVal();
        }
    }

}
