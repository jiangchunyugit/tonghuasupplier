package cn.thinkfree.service.pcUser;

import cn.thinkfree.core.base.MyLogger;
import cn.thinkfree.core.constants.SysConstants;
import cn.thinkfree.core.exception.MyException;
import cn.thinkfree.core.security.filter.util.SessionUserDetailsUtil;
import cn.thinkfree.core.security.utils.MultipleMd5;
import cn.thinkfree.core.utils.LogUtil;
import cn.thinkfree.database.constants.RoleScope;
import cn.thinkfree.database.constants.UserEnabled;
import cn.thinkfree.database.constants.UserLevel;
import cn.thinkfree.database.event.account.AccountCreate;
import cn.thinkfree.database.event.account.ResetPassWord;
import cn.thinkfree.database.mapper.*;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.vo.MyPageHelper;
import cn.thinkfree.database.vo.PcUserInfoVo;
import cn.thinkfree.database.vo.UserVO;
import cn.thinkfree.database.vo.account.AccountListVO;
import cn.thinkfree.database.vo.account.AccountSEO;
import cn.thinkfree.database.vo.account.AccountVO;
import cn.thinkfree.database.vo.account.ThirdAccountVO;
import cn.thinkfree.database.constants.UserRegisterType;
import cn.thinkfree.service.event.EventService;
import cn.thinkfree.service.utils.AccountHelper;
import cn.thinkfree.service.utils.UserNoUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PcUserInfoServiceImpl implements PcUserInfoService {

    private static MyLogger logger = LogUtil.getLogger(PcUserInfoServiceImpl.class);

    @Autowired
    PcUserInfoMapper pcUserInfoMapper;

    @Autowired
    UserRegisterMapper userRegisterMapper;

    @Autowired
    CompanyInfoMapper companyInfoMapper;

    @Autowired
    SystemUserRoleMapper systemUserRoleMapper;

    @Autowired
    CityBranchMapper cityBranchMapper;

    @Autowired
    BranchCompanyMapper branchCompanyMapper;

    @Autowired
    SystemRoleMapper systemRoleMapper;

    @Autowired
    EventService eventService;

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
    @Transactional(rollbackFor = Exception.class)
    public boolean delPcUserInfo(String userId) {

        PcUserInfo pcUserInfo = new PcUserInfo();
        pcUserInfo.setIsDelete(SysConstants.YesOrNo.YES.shortVal());
        pcUserInfo.setId(userId);
        PcUserInfoExample example1 = new PcUserInfoExample();
        example1.createCriteria().andIdEqualTo(userId);
        int pcline = pcUserInfoMapper.updateByExampleSelective(pcUserInfo, example1);

        //注册表修改
        UserRegister userRegister = new UserRegister();
        userRegister.setUserId(userId);
        userRegister.setIsDelete(SysConstants.YesOrNo.YES.shortVal());
        UserRegisterExample exampleReg = new UserRegisterExample();
        exampleReg.createCriteria().andUserIdEqualTo(userId);
        int regLine = userRegisterMapper.updateByExampleSelective(userRegister, exampleReg);
        if(regLine > 0 && pcline > 0){
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
    @Transactional(rollbackFor = Exception.class)
    public boolean saveUserInfo(PcUserInfoVo pcUserInfoVo) {
        if (isEnable(pcUserInfoVo.getRegPhone())) {
            return false;
        }

        UserVO userVO = (UserVO) SessionUserDetailsUtil.getUserDetails();
        String userId = UserNoUtils.getUserNo("PC");
        Date date = new Date();
        pcUserInfoVo.setCreateTime(date);
        pcUserInfoVo.setId(userId);
        pcUserInfoVo.setIsDelete(SysConstants.YesOrNo.NO.shortVal());
        pcUserInfoVo.setRootCompanyId(userVO.getPcUserInfo().getRootCompanyId());
        //临时启用
        pcUserInfoVo.setEnabled(UserEnabled.Enabled_true.shortVal());


        //注册表
        UserRegister userRegister = new UserRegister();
        userRegister.setRegisterTime(date);
        userRegister.setIsDelete(SysConstants.YesOrNo.NO.shortVal());
        userRegister.setType(UserRegisterType.Staff.shortVal());
        userRegister.setUpdateTime(date);
        MultipleMd5 md5 = new MultipleMd5();

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

    private boolean isEnable(String name) {
        //判断输入的账号是否已经注册过
        List<String> phones = userRegisterMapper.findPhoneAll();
        boolean flag = phones.contains(name);
        if(flag){
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
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUserInfo(PcUserInfoVo pcUserInfoVo) {

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

    /**
     * 新增用户账号
     * 1.判断是否存在
     * 2.处理账号表
     * 3.处理用户信息表
     * 4.处理用户权限表
     * 5.发送用户新建事件
     * @param accountVO
     * @return
     */
    @Transactional
    @Override
    public AccountVO saveUserAccount(AccountVO accountVO) {

        if(isExists(accountVO)){
            logger.error("已存在用户:{}",accountVO);
            throw  new MyException("已存在的用户");
        }
        PasswordEncoder passwordEncoder = new MultipleMd5();
        String userCode = getUserCode(UserRegisterType.Platform);
        String password = AccountHelper.createUserPassWord();

        logger.info("创建账号 => 获取用户编号:{}",userCode);

        UserRegister account = getUserRegister(accountVO);
        account.setUserId(userCode);
        account.setPassword(passwordEncoder.encode(password));
        userRegisterMapper.insertSelective(account);

        logger.info("创建账号 => 初始化账号信息,准备处理密码");

        PcUserInfo userInfo = getUserInfo(accountVO);
        userInfo.setId(userCode);
        pcUserInfoMapper.insertSelective(userInfo);

        List<SystemRole> roles = accountVO.getRoles();
        saveUserRole(userCode,roles);

        AccountCreate accountCreate = new AccountCreate(userCode,account.getPhone(),password);
        eventService.publish(accountCreate);
        return accountVO;
    }



    /**
     * 查询账号详情
     *
     * @param id
     * @return
     */
    @Override
    public AccountVO findAccountVOByID(String id) {

        AccountVO accountVO = new AccountVO();

        PcUserInfo pcUserInfo = pcUserInfoMapper.selectByPrimaryKey(id);
        accountVO.setPcUserInfo(pcUserInfo);

        if(StringUtils.isNotBlank(pcUserInfo.getBranchCompanyId())){
            BranchCompany branchCompany = branchCompanyMapper.selectByPrimaryKey(Integer.valueOf(pcUserInfo.getBranchCompanyId()));
            accountVO.setBranchCompany(branchCompany);
        }
        if(StringUtils.isNotBlank(pcUserInfo.getCityBranchCompanyId())){
            CityBranch cityBranch = cityBranchMapper.selectByPrimaryKey(Integer.valueOf(pcUserInfo.getCityBranchCompanyId()));
            accountVO.setCityBranch(cityBranch);
        }

        // TODO 补充第三方数据
        ThirdAccountVO thirdAccountVO = new ThirdAccountVO();
        accountVO.setThirdAccount(thirdAccountVO);

        List<SystemRole> roles = systemRoleMapper.selectSystemRoleVOForGrant(id,convertScope(pcUserInfo));
        accountVO.setRoles(roles);

        return accountVO;
    }

    /**
     * 更新账号信息
     *
     * @param id
     * @param accountVO
     * @return
     */
    @Transactional
    @Override
    public String updateAccountVO(String id, AccountVO accountVO) {

        PcUserInfo update = pcUserInfoMapper.selectByPrimaryKey(id);
        if(accountVO.getPcUserInfo()!= null ){
            update.setMemo(accountVO.getPcUserInfo().getMemo());
        }
        logger.info("编辑账号,补充个人所属信息:{}",accountVO);
        makeUpOwnScope(update,accountVO);
        pcUserInfoMapper.updateByPrimaryKey(update);

        refreshSystemRole(id,accountVO);

        return "操作成功!";
    }

    /**
     * 刷新系统权限
     * @param id
     * @param accountVO
     */
    private void refreshSystemRole(String id, AccountVO accountVO) {
        SystemUserRoleExample systemUserRoleExample = new SystemUserRoleExample();
        systemUserRoleExample.createCriteria().andUserIdEqualTo(id);
        systemUserRoleMapper.deleteByExample(systemUserRoleExample);

        saveUserRole(id,accountVO.getRoles());
    }

    /**
     * 账号启停
     *
     * @param id
     * @param state
     * @return
     */
    @Transactional
    @Override
    public String updateAccountState(String id, Integer state) {
        PcUserInfo update = new PcUserInfo();
        update.setId(id);
        update.setEnabled(state.shortValue());
        pcUserInfoMapper.updateByPrimaryKeySelective(update);
        return "操作成功!";
    }

    /**
     * 更新密码 - 初次登录重置
     *
     * @param id
     * @param passWord
     * @return
     */
    @Transactional
    @Override
    public String updatePassWordForInit(String id, String passWord) {
        UserRegister update = new UserRegister();
        update.setPassword(new MultipleMd5().encode(passWord));
        UserRegisterExample condition = new UserRegisterExample();
        condition.createCriteria().andUserIdEqualTo(id);
        userRegisterMapper.updateByExampleSelective(update,condition);
        return "操作成功!";
    }

    /**
     * 分页查询账号
     *
     * @param accountSEO
     * @return
     */
    @Override
    public PageInfo<AccountListVO> pageAccountVO(AccountSEO accountSEO) {

        PageHelper.startPage(accountSEO.getPage(),accountSEO.getRows());
        List<AccountListVO> result = pcUserInfoMapper.selectAccountListVO(accountSEO);
        return new PageInfo<>(result);
    }

    /**
     * 插入系统角色信息
     * @param userCode
     * @param roles
     */
    private void saveUserRole(String userCode, List<SystemRole> roles) {
        if(roles != null ){
            roles.forEach(r->{
                SystemUserRole saveObj = new SystemUserRole();
                saveObj.setRoleId(r.getId());
                saveObj.setUserId(userCode);
                systemUserRoleMapper.insertSelective(saveObj);
            });
        }
    }
    /**
     * 转换权限范围
     * @param pcUserInfo
     * @return
     */
    private String convertScope(PcUserInfo pcUserInfo) {
        if (pcUserInfo == null){
            throw  new MyException("数据状态异常");
        }
        if(UserLevel.Company_Admin.shortVal().equals(pcUserInfo.getLevel())){
            return RoleScope.ROOT.code.toString();
        }else if(UserLevel.Company_Province.shortVal().equals(pcUserInfo.getLevel())){
            return RoleScope.PROVINCE.code.toString();
        }else {
            return RoleScope.CITY.code.toString();
        }

    }

    /**
     * 删除账号信息
     *
     * @param id
     * @return
     */
    @Transactional
    @Override
    public String delAccountByID(String id) {

        PcUserInfo pcUserInfo = pcUserInfoMapper.selectByPrimaryKey(id);

        if(!UserEnabled.Enabled_false.shortVal().equals(pcUserInfo.getEnabled())){
            PcUserInfo delObj = new PcUserInfo();
            delObj.setIsDelete(SysConstants.YesOrNo.YES.shortVal());
            delObj.setId(id);
            pcUserInfoMapper.updateByPrimaryKeySelective(delObj);
        }else{
            throw new MyException("数据状态不可删除");
        }
//        pcUserInfoMapper.deleteByPrimaryKey(id);
        UserRegisterExample userRegisterExample = new UserRegisterExample();
        userRegisterExample.createCriteria().andUserIdEqualTo(id);
        UserRegister delObj = new UserRegister();
        delObj.setIsDelete(SysConstants.YesOrNo.YES.shortVal());
        userRegisterMapper.updateByExampleSelective(delObj,userRegisterExample);
//        userRegisterMapper.deleteByExample(userRegisterExample);

//        SystemUserRoleExample systemUserRoleExample = new SystemUserRoleExample();
//        systemUserRoleExample.createCriteria().andUserIdEqualTo(id);
//        systemUserRoleMapper.deleteByExample(systemUserRoleExample);

        return "操作成功";
    }

    /**
     * 获取用户标识
     * @param type
     * @return
     */
    private String getUserCode(UserRegisterType type) {
        return AccountHelper.createUserNo(AccountHelper.UserType.PC.prefix);
    }

    /**
     * 检查是否存在账号
     * @param accountVO
     * @return
     */
    private boolean isExists(AccountVO accountVO) {

        String account = accountVO.getThirdAccount().getId();

        PcUserInfoExample condition = new PcUserInfoExample();
        condition.createCriteria().andThirdIdEqualTo(account).andIsDeleteEqualTo(SysConstants.YesOrNo.NO.shortVal());

        List<PcUserInfo> result = pcUserInfoMapper.selectByExample(condition);
        return !result.isEmpty();
    }

    /**
     * 获取用户信息
     * @param accountVO
     * @return
     */
    private PcUserInfo getUserInfo(AccountVO accountVO) {
        PcUserInfo  userInfo = new PcUserInfo();
        // 初始默认值
        userInfo.setEnabled(SysConstants.YesOrNo.NO.shortVal());
        userInfo.setIsDelete(SysConstants.YesOrNo.NO.shortVal());
        userInfo.setCreateTime(new Date());


        UserVO userVO = (UserVO) SessionUserDetailsUtil.getUserDetails();
        userInfo.setCreator(userVO.getUsername());
        userInfo.setRootCompanyId(userVO.getPcUserInfo().getRootCompanyId());

        makeUpOwnScope(userInfo,accountVO);

        // 处理设置信息
        if(accountVO.getPcUserInfo() != null){
            userInfo.setMemo(accountVO.getPcUserInfo().getMemo());
        }
        userInfo.setEmail(accountVO.getThirdAccount().getEmail());
        userInfo.setPhone(accountVO.getThirdAccount().getPhone());
        userInfo.setName(accountVO.getThirdAccount().getName());
        userInfo.setRootCompanyId(userVO.getPcUserInfo().getRootCompanyId());
        userInfo.setThirdId(accountVO.getThirdAccount().getId());

        return userInfo;
    }

    /**
     * 补全所属信息
     * @param userInfo
     * @param accountVO
     */
    private void makeUpOwnScope(PcUserInfo userInfo, AccountVO accountVO) {
        // 处理用户级别
        if(accountVO.getBranchCompany() != null  && accountVO.getCityBranch() != null){
            userInfo.setLevel(UserLevel.Company_City.shortVal());
            userInfo.setCityBranchCompanyId(accountVO.getCityBranch().getId().toString());
            userInfo.setBranchCompanyId(accountVO.getBranchCompany().getId().toString());
            userInfo.setProvince(accountVO.getCityBranch().getProvinceCode().toString());
            userInfo.setCity(accountVO.getCityBranch().getCityCode().toString());
        }else if(accountVO.getBranchCompany() != null &&  accountVO.getCityBranch() == null){
            userInfo.setBranchCompanyId(accountVO.getBranchCompany().getId().toString());
            userInfo.setProvince(accountVO.getBranchCompany().getProvinceCode().toString());
            userInfo.setLevel(UserLevel.Company_Province.shortVal());
            userInfo.setCityBranchCompanyId(null);
            userInfo.setCity(null);
        }else if( accountVO.getBranchCompany() == null  && accountVO.getCityBranch() == null){
            userInfo.setLevel(UserLevel.Company_Admin.shortVal());
            userInfo.setProvince(null);
            userInfo.setCity(null);
            userInfo.setCityBranchCompanyId(null);
            userInfo.setBranchCompanyId(null);
        }
    }

    /**
     * 重置密码
     *
     * @param id
     * @return
     */
    @Transactional
    @Override
    public String updateForResetPassWord(String id) {
        UserRegister update = new UserRegister();
        String password = AccountHelper.createUserPassWord();
        update.setPassword(new MultipleMd5().encode(password));
        UserRegisterExample condition = new UserRegisterExample();
        condition.createCriteria().andUserIdEqualTo(id);
        userRegisterMapper.updateByExampleSelective(update,condition);

        List<UserRegister> accounts = userRegisterMapper.selectByExample(condition);
        if(accounts.isEmpty() || accounts.size() != 1){
            throw  new MyException("多重账号信息");
        }

        ResetPassWord event = new ResetPassWord(id,accounts.get(0).getPhone(),password);
        eventService.publish(event);
        return "操作成功";
    }

    /**
     * 抽取账号信息
     * @param accountVO
     * @return
     */
    private UserRegister getUserRegister(AccountVO accountVO) {
        UserRegister userRegister = new UserRegister();
        userRegister.setIsDelete(SysConstants.YesOrNo.NO.shortVal());
        userRegister.setType(UserRegisterType.Platform.shortVal());
        userRegister.setPhone(accountVO.getThirdAccount().getWorkNumber());
        userRegister.setRegisterTime(new Date());
        return userRegister;
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
