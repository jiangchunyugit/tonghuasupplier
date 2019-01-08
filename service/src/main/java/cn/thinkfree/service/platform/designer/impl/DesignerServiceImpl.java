package cn.thinkfree.service.platform.designer.impl;

import cn.thinkfree.core.constants.BasicsDataParentEnum;
import cn.thinkfree.core.constants.RoleFunctionEnum;
import cn.thinkfree.database.mapper.*;
import cn.thinkfree.database.model.*;
import cn.thinkfree.service.platform.basics.BasicsService;
import cn.thinkfree.service.platform.basics.RoleFunctionService;
import cn.thinkfree.service.platform.designer.DesignDispatchService;
import cn.thinkfree.service.platform.designer.DesignerService;
import cn.thinkfree.service.platform.designer.UserCenterService;
import cn.thinkfree.service.platform.vo.*;
import cn.thinkfree.service.utils.DateUtils;
import cn.thinkfree.service.utils.ExcelToListMap;
import cn.thinkfree.service.utils.ReflectUtils;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author xusonghui
 * 设计师服务
 */
@Service
public class DesignerServiceImpl implements DesignerService {

    @Autowired
    private DesignerMsgMapper designerMsgMapper;
    @Autowired
    private DesignerStyleRelationMapper relationMapper;
    @Autowired
    private EmployeeMsgMapper employeeMsgMapper;
    @Autowired
    private ProvinceMapper provinceMapper;
    @Autowired
    private CityMapper cityMapper;
    @Autowired
    private AreaMapper areaMapper;
    @Autowired
    private UserCenterService userCenterService;
    @Autowired
    private BasicsService basicsService;
    @Autowired
    private RoleFunctionService functionService;
    @Autowired
    private CompanyInfoMapper companyInfoMapper;
    @Autowired
    private DesignDispatchService designDispatchService;

    /**
     * @param designerName          设计师用户名
     * @param designerRealName      设计师真实姓名
     * @param phone                 手机号
     * @param authState             实名认证状态
     * @param province              所在省份
     * @param city                  所在城市
     * @param area                  所在区域
     * @param level                 设计师等级
     * @param identity              设计师身份
     * @param cardNo                身份证号
     * @param source                来源
     * @param tag                   标签
     * @param registrationTimeStart 注册时间
     * @param registrationTimeEnd   注册时间
     * @param sort                  排序区域
     * @param pageSize              每页多少条
     * @param pageIndex             第几页
     * @return
     */
    @Override
    public PageVo<List<DesignerMsgListVo>> queryDesigners(
            String designerName, String designerRealName, String phone, String authState,
            String province, String city, String area, String level, String identity, String cardNo, String source,
            String tag, String registrationTimeStart, String registrationTimeEnd, String sort, int pageSize, int pageIndex) {
        List<String> userIds = null;
        if(!StringUtils.isEmpty(phone)){
            List<UserMsgVo> userMsgVos = userCenterService.queryUserMsg(null, phone);
            if(userMsgVos == null || userMsgVos.isEmpty()){
                return PageVo.def(new ArrayList<>());
            }
            userIds = new ArrayList<>();
            userIds.addAll(ReflectUtils.getList(userMsgVos,"staffId"));
        }
        //调用用户中心，模糊查询用户名信息
        List<String> designerMsgIds = getDesignerMsgs(level, identity, source, tag, sort);
        if(designerMsgIds != null){
            if(designerMsgIds.isEmpty()){
               return PageVo.def(new ArrayList<>());
            }
            if(userIds == null){
                userIds = new ArrayList<>();
            }
            userIds.addAll(designerMsgIds);
        }
        if(userIds != null && userIds.isEmpty()){
            return PageVo.def(new ArrayList<>());
        }
        List<String> observeCompanyIds = designDispatchService.getCompanyIds();
        if(observeCompanyIds != null && observeCompanyIds.isEmpty()){
            return PageVo.def(new ArrayList<>());
        }
        String roleCode = functionService.queryRoleCode(RoleFunctionEnum.DESIGN_POWER);
        EmployeeMsgExample employeeMsgExample = new EmployeeMsgExample();
        addWhere(designerRealName, authState, province, city, area, cardNo, registrationTimeStart, registrationTimeEnd, userIds, observeCompanyIds, roleCode, employeeMsgExample.or());
        addWhere(designerRealName, authState, province, city, area, cardNo, registrationTimeStart, registrationTimeEnd, userIds, null, roleCode, employeeMsgExample.or().andCompanyIdIsNull());
        long total = employeeMsgMapper.countByExample(employeeMsgExample);
        if(total == 0){
            return PageVo.def(new ArrayList<>());
        }
        PageHelper.startPage(pageIndex, pageSize);
        List<EmployeeMsg> employeeMsgs = employeeMsgMapper.selectByExample(employeeMsgExample);
        Map<String, DesignerMsg> designerMsgMap = getStringDesignerMsgMap(employeeMsgs);
        List<DesignerMsgListVo> msgVos = new ArrayList<>();
        Map<String,String> provinceMap = basicsService.getProvince(ReflectUtils.getList(employeeMsgs,"province").toArray(new String[]{}));
        Map<String,String> cityMap = basicsService.getCity(ReflectUtils.getList(employeeMsgs,"city").toArray(new String[]{}));
        Map<String,String> areaMap = basicsService.getArea(ReflectUtils.getList(employeeMsgs,"area").toArray(new String[]{}));
        List<UserMsgVo> userMsgVos = userCenterService.queryUsers(ReflectUtils.getList(employeeMsgs,"userId"));
        Map<String,UserMsgVo> userMsgVoMap = ReflectUtils.listToMap(userMsgVos,"staffId");
        Map<String, CompanyInfo> companyInfoMap = getStringCompanyInfoMap(employeeMsgs);
        for (EmployeeMsg employeeMsg : employeeMsgs) {
            DesignerMsgListVo msgVo = new DesignerMsgListVo();
            DesignerMsg msg = designerMsgMap.get(employeeMsg.getUserId());
            if (msg != null) {
                msgVo.setIdentityName(msg.getIdentity() + "");
                msgVo.setLevelName(msg.getLevel() + "");
                msgVo.setSource(msg.getSource() + "");
                msgVo.setTag(msg.getTag() + "");
                msgVo.setUserId(msg.getUserId());
            }
            msgVo.setAddress(provinceMap.get(employeeMsg.getProvince()) + "," + cityMap.get(employeeMsg.getCity()) + "," + areaMap.get(employeeMsg.getArea()));
            if(msgVo.getAddress().contains("null")){
                msgVo.setAddress("");
            }
            msgVo.setAuthState(employeeMsg.getAuthState() + "");
            CompanyInfo companyInfo = companyInfoMap.get(employeeMsg.getCompanyId());
            if(companyInfo != null){
                msgVo.setCompanyName(companyInfo.getCompanyName());
            }
            UserMsgVo userMsgVo = userMsgVoMap.get(employeeMsg.getUserId());
            if(userMsgVo != null){
                msgVo.setPhone(userMsgVo.getUserPhone());
                msgVo.setRegistrationTime(userMsgVo.getRegisterTime());
            }
            msgVo.setSex(employeeMsg.getSex() + "");
            msgVo.setRealName(employeeMsg.getRealName());
            msgVo.setBindCompanyState(employeeMsg.getEmployeeApplyState() + "");
            msgVos.add(msgVo);
        }
        PageVo<List<DesignerMsgListVo>> msgVo = new PageVo<>();
        msgVo.setTotal(total);
        msgVo.setPageSize(pageSize);
        msgVo.setData(msgVos);
        msgVo.setPageIndex(pageIndex);
        return msgVo;
    }

    private void addWhere(String designerRealName, String authState, String province, String city, String area, String cardNo,
                          String registrationTimeStart, String registrationTimeEnd, List<String> userIds, List<String> observeCompanyIds,
                          String roleCode, EmployeeMsgExample.Criteria msgExampleCriteria) {
        if(StringUtils.isEmpty(roleCode)){
            roleCode = "CD";
        }
        msgExampleCriteria.andRoleCodeEqualTo(roleCode);
        if(userIds != null && !userIds.isEmpty()){
            msgExampleCriteria.andUserIdIn(userIds);
        }
        if (!StringUtils.isEmpty(designerRealName)) {
            msgExampleCriteria.andRealNameEqualTo(designerRealName);
        }
        if (!StringUtils.isEmpty(authState)) {
            msgExampleCriteria.andAuthStateEqualTo(Integer.parseInt(authState));
        }
        if (!StringUtils.isEmpty(province)) {
            msgExampleCriteria.andProvinceEqualTo(province);
        }
        if (!StringUtils.isEmpty(city)) {
            msgExampleCriteria.andCityEqualTo(city);
        }
        if (!StringUtils.isEmpty(area)) {
            msgExampleCriteria.andAreaEqualTo(area);
        }
        if (!StringUtils.isEmpty(registrationTimeStart)) {
            msgExampleCriteria.andBindDateGreaterThanOrEqualTo(DateUtils.strToDate(registrationTimeStart));
        }
        if (!StringUtils.isEmpty(registrationTimeEnd)) {
            msgExampleCriteria.andBindDateLessThanOrEqualTo(DateUtils.strToDate(registrationTimeEnd));
        }
        if (!StringUtils.isEmpty(cardNo)) {
            msgExampleCriteria.andCertificateLike("%" + cardNo + "%");
        }
        if(observeCompanyIds != null){
            msgExampleCriteria.andCompanyIdIn(observeCompanyIds);
        }
    }

    private Map<String, CompanyInfo> getStringCompanyInfoMap(List<EmployeeMsg> employeeMsgs) {
        if(employeeMsgs == null || employeeMsgs.isEmpty()){
            return new HashMap<>();
        }
        List<String> companyIds = ReflectUtils.getList(employeeMsgs,"companyId");
        if(companyIds == null || companyIds.isEmpty()){
            return new HashMap<>();
        }
        CompanyInfoExample infoExample = new CompanyInfoExample();
        infoExample.createCriteria().andCompanyIdIn(companyIds);
        List<CompanyInfo> companyInfos = companyInfoMapper.selectByExample(infoExample);
        return ReflectUtils.listToMap(companyInfos,"companyId");
    }

    private Map<String, DesignerMsg> getStringDesignerMsgMap(List<EmployeeMsg> employeeMsgs) {
        if(employeeMsgs == null || employeeMsgs.isEmpty()){
            return new HashMap<>();
        }
        List<String> userIds = ReflectUtils.getList(employeeMsgs, "userId");
        DesignerMsgExample msgExample = new DesignerMsgExample();
        msgExample.createCriteria().andUserIdIn(userIds);
        List<DesignerMsg> designerMsgs = designerMsgMapper.selectByExample(msgExample);
        return ReflectUtils.listToMap(designerMsgs,"userId");
    }

    private List<String> getDesignerMsgs(String level, String identity, String source, String tag, String sort) {
        DesignerMsgExample msgExample = new DesignerMsgExample();
        DesignerMsgExample.Criteria criteria = msgExample.createCriteria();
        boolean isCondition = false;
        if (!StringUtils.isEmpty(level) && Long.parseLong(level) > 0) {
            criteria.andLevelEqualTo(Long.parseLong(level));
            isCondition = true;
        }
        if (!StringUtils.isEmpty(identity)) {
            criteria.andIdentityEqualTo(Long.parseLong(identity));
            isCondition = true;
        }
        if (!StringUtils.isEmpty(source)) {
            criteria.andSourceEqualTo(Integer.parseInt(source));
            isCondition = true;
        }
        if (!StringUtils.isEmpty(tag)) {
            criteria.andTagEqualTo(Long.parseLong(tag));
            isCondition = true;
        }
        if (!StringUtils.isEmpty(sort)) {
            criteria.andWeightEqualTo(Long.parseLong(sort));
            isCondition = true;
        }
        if(!isCondition){
            return null;
        }
        List<DesignerMsg> designerMsgs = designerMsgMapper.selectByExample(msgExample);
        return ReflectUtils.getList(designerMsgs,"userId");
    }

    /**
     * 根据用户ID查询设计师信息
     *
     * @param userId 用户ID
     * @return
     */
    @Override
    public DesignerMsgVo queryDesignerByUserId(String userId) {
        if (StringUtils.isEmpty(userId)) {
            throw new RuntimeException("设计师ID不能为空");
        }
        DesignerMsg designerMsg = getDesignerMsg(userId);
        UserMsgVo userMsgVo = userCenterService.queryUser(userId);
        DesignerMsgVo designerMsgVo = new DesignerMsgVo();
        designerMsgVo.setRealName(userMsgVo.getRealName());
        designerMsgVo.setUserName(userMsgVo.getUserName());
        designerMsgVo.setPhone(userMsgVo.getUserPhone());
        EmployeeMsg employeeMsg = queryEmployeeMsgByUserId(userId);
        if (employeeMsg != null) {
            designerMsgVo.setSex(employeeMsg.getSex());
            designerMsgVo.setBirthday(DateUtils.dateToStr(employeeMsg.getBindDate()));
            designerMsgVo.setEmail(employeeMsg.getEmail());
            designerMsgVo.setCertificate(employeeMsg.getCertificate());
            designerMsgVo.setCertificateUrl1(employeeMsg.getCertificatePhotoUrl1());
            designerMsgVo.setCertificateUrl2(employeeMsg.getCertificatePhotoUrl2());
            designerMsgVo.setCertificateUrl3(employeeMsg.getCertificatePhotoUrl3());
            designerMsgVo.setAuthState(employeeMsg.getAuthState());
            designerMsgVo.setRegisterTime(DateUtils.dateToStr(employeeMsg.getBindDate()));
        }
        if (designerMsg != null) {
            designerMsgVo.setSource(designerMsg.getSource());
            String designTag = "云设计家设计师";
            if (designerMsg.getTag() == null || designerMsg.getTag() != 1) {
                designTag = "待定";
            }
            designerMsgVo.setDesignTag(designTag);
            designerMsgVo.setDesignTagCode(designerMsg.getTag() + "");
            if (designerMsg.getLevel() != null) {
                designerMsgVo.setLevel(designerMsg.getLevel().intValue());
            }
            String identity = "社会化设计师";
            if (designerMsg.getIdentity() == null || designerMsg.getIdentity() != 1) {
                identity = "待定";
            }
            designerMsgVo.setIdentityCode(designerMsg.getIdentity() + "");
            designerMsgVo.setVolumeRoomMoney(designerMsg.getVolumeRoomMoney().toString());
            designerMsgVo.setDesignerMoneyLow(designerMsg.getDesignerMoneyLow().toString());
            designerMsgVo.setDesignerMoneyHigh(designerMsg.getDesignerMoneyHigh().toString());
            designerMsgVo.setIdentity(identity);
        }
        designerMsgVo.setCompanyName(employeeMsg.getCompanyId());
        designerMsgVo.setWorkingTime(employeeMsg.getWorkingTime());
        List<DesignerStyleConfigVo> styleConfigs = queryDesignerStyleByUserId(userId);
        List<String> styles = ReflectUtils.getList(styleConfigs, "styleName");
        List<String> styleCodes = ReflectUtils.getList(styleConfigs, "styleCode");
        designerMsgVo.setDesignerStyles(styles);
        designerMsgVo.setDesignerStyleCodes(styleCodes);
        designerMsgVo.setAddress(getAddress(employeeMsg));
        designerMsgVo.setEmployeeState(employeeMsg.getEmployeeState());
        return designerMsgVo;
    }

    private String getAddress(EmployeeMsg employeeMsg){
        StringBuffer stringBuffer = new StringBuffer();
        if(employeeMsg.getProvince() != null){
            stringBuffer.append(basicsService.getProvince(employeeMsg.getProvince()).get(employeeMsg.getProvince())).append(",");
        }
        if(employeeMsg.getCity() != null){
            stringBuffer.append(basicsService.getCity(employeeMsg.getCity()).get(employeeMsg.getCity())).append(",");
        }
        if(employeeMsg.getArea() != null){
            stringBuffer.append(basicsService.getArea(employeeMsg.getArea()).get(employeeMsg.getArea())).append(",");
        }
        if(stringBuffer.length() >= 1){
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        }
        return stringBuffer.toString();
    }

    private DesignerMsg getDesignerMsg(String userId) {
        DesignerMsgExample msgExample = new DesignerMsgExample();
        msgExample.createCriteria().andUserIdEqualTo(userId);
        List<DesignerMsg> designerMsgs = designerMsgMapper.selectByExample(msgExample);
        if (designerMsgs.isEmpty()) {
            return null;
        }
        return designerMsgs.get(0);
    }

    /**
     * 根据用户ID查询员工信息
     *
     * @param userId 用户ID
     * @return
     */
    @Override
    public EmployeeMsg queryEmployeeMsgByUserId(String userId) {
        EmployeeMsgExample msgExample = new EmployeeMsgExample();
        msgExample.createCriteria().andUserIdEqualTo(userId);
        List<EmployeeMsg> employeeMsgs = employeeMsgMapper.selectByExample(msgExample);
        if (!employeeMsgs.isEmpty()) {
            return employeeMsgs.get(0);
        }
        return null;
    }

    /**
     * 根据公司ID查询设计师信息
     *
     * @param companyId 公司ID
     * @return
     */
    @Override
    public List<EmployeeMsg> queryDesignerByCompanyId(String companyId) {
        EmployeeMsgExample msgExample = new EmployeeMsgExample();
        msgExample.createCriteria().andCompanyIdEqualTo(companyId);
        List<EmployeeMsg> employeeMsgs = employeeMsgMapper.selectByExample(msgExample);
        return employeeMsgs;
    }

    /**
     * 根据设计师id查询擅长风格
     *
     * @param userId
     * @return
     */
    @Override
    public List<DesignerStyleConfigVo> queryDesignerStyleByUserId(String userId) {
        DesignerStyleRelationExample relationExample = new DesignerStyleRelationExample();
        relationExample.createCriteria().andDesignerIdEqualTo(userId);
        List<DesignerStyleRelation> designerStyleRelations = relationMapper.selectByExample(relationExample);
        if (!designerStyleRelations.isEmpty()) {
            List<String> styleCodes = ReflectUtils.getList(designerStyleRelations, "styleCode");
            List<BasicsData> basicsDatas = basicsService.queryData(BasicsDataParentEnum.DESIGN_STYLE.getCode(), styleCodes);
            List<DesignerStyleConfigVo> styleConfigs = new ArrayList<>();
            for (BasicsData basicsData : basicsDatas) {
                DesignerStyleConfigVo styleConfigVo = new DesignerStyleConfigVo();
                styleConfigVo.setDelState(basicsData.getDelState());
                styleConfigVo.setRemark(basicsData.getRemark());
                styleConfigVo.setStyleCode(basicsData.getBasicsCode());
                styleConfigVo.setStyleName(basicsData.getBasicsName());
                styleConfigs.add(styleConfigVo);
            }
            return styleConfigs;
        }
        return new ArrayList<>();
    }

    /**
     * 设置排序
     *
     * @param userId 用户ID
     * @param sort   排序值
     */
    @Override
    public void setDesignerSort(String userId, int sort) {
        DesignerMsgExample msgExample = new DesignerMsgExample();
        msgExample.createCriteria().andUserIdEqualTo(userId);
        DesignerMsg designerMsg = new DesignerMsg();
        designerMsg.setWeight(Long.parseLong(sort + ""));
        designerMsgMapper.updateByExampleSelective(designerMsg, msgExample);
    }

    /**
     * 查询设计风格
     *
     * @return
     */
    @Override
    public List<DesignerStyleConfigVo> queryDesignerStyle() {
        List<BasicsData> basicsDatas = basicsService.queryData(BasicsDataParentEnum.DESIGN_STYLE.getCode());
        List<DesignerStyleConfigVo> styleConfigs = new ArrayList<>();
        for (BasicsData basicsData : basicsDatas) {
            DesignerStyleConfigVo styleConfigVo = new DesignerStyleConfigVo();
            styleConfigVo.setDelState(basicsData.getDelState());
            styleConfigVo.setRemark(basicsData.getRemark());
            styleConfigVo.setStyleCode(basicsData.getBasicsCode());
            styleConfigVo.setStyleName(basicsData.getBasicsName());
            styleConfigs.add(styleConfigVo);
        }
        return styleConfigs;
    }

    /**
     * 编辑设计师信息
     *
     * @param userId            用户ID
     * @param tag               设计师标签
     * @param identity          设计师身份
     * @param workingTime       工作年限
     * @param volumeRoomMoney   量房费
     * @param designerMoneyLow  设计费低
     * @param designerMoneyHigh 设计费高
     * @param masterStyle       擅长风格
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void editDesignerMsg(
            String userId, String tag, String identity, String workingTime, String volumeRoomMoney,
            String designerMoneyLow, String designerMoneyHigh, String masterStyle) {
        //更新设计师信息
        DesignerMsgExample msgExample = new DesignerMsgExample();
        msgExample.createCriteria().andUserIdEqualTo(userId);
        DesignerMsg designerMsg = new DesignerMsg();
        designerMsg.setTag(Long.parseLong(tag));
        designerMsg.setIdentity(Long.parseLong(identity));
        designerMsg.setVolumeRoomMoney(new BigDecimal(volumeRoomMoney));
        designerMsg.setDesignerMoneyLow(new BigDecimal(designerMoneyLow));
        designerMsg.setDesignerMoneyHigh(new BigDecimal(designerMoneyHigh));
        designerMsgMapper.updateByExampleSelective(designerMsg, msgExample);
        //更新员工信息
        EmployeeMsg employeeMsg = new EmployeeMsg();
        employeeMsg.setWorkingTime(Integer.parseInt(workingTime));
        EmployeeMsgExample employeeMsgExample = new EmployeeMsgExample();
        employeeMsgExample.createCriteria().andUserIdEqualTo(userId);
        employeeMsgMapper.updateByExampleSelective(employeeMsg, employeeMsgExample);
        //更新擅长风格信息
        String[] styleCodes = masterStyle.split(",");
        DesignerStyleRelationExample relationExample = new DesignerStyleRelationExample();
        relationExample.createCriteria().andDesignerIdEqualTo(userId);
        relationMapper.deleteByExample(relationExample);
        for (String styleCode : styleCodes) {
            DesignerStyleRelation styleRelation = new DesignerStyleRelation();
            styleRelation.setDesignerId(userId);
            styleRelation.setStyleCode(styleCode);
            relationMapper.insertSelective(styleRelation);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createDesigner(
            String userName, String phone, String email, int sex, String province, String city, String area, String workingTime,
            String masterStyle, String volumeRoomMoney, String designerMoneyLow, String designerMoneyHigh) {
        if(StringUtils.isEmpty(userName)){
            throw new RuntimeException("必须输入设计师姓名");
        }
        if(StringUtils.isEmpty(phone)){
            throw new RuntimeException("必须输入手机号");
        }
        if(StringUtils.isEmpty(workingTime)){
            throw new RuntimeException("必须输入工作年限");
        }
        if(StringUtils.isEmpty(masterStyle)){
           throw new RuntimeException("必须选择擅长风格");
        }
        if(StringUtils.isEmpty(volumeRoomMoney)){
            throw new RuntimeException("必须设置量房费");
        }
        if(StringUtils.isEmpty(designerMoneyLow)){
            throw new RuntimeException("必须设置设计费最低金额");
        }
        if(StringUtils.isEmpty(designerMoneyHigh)){
            throw new RuntimeException("必须设置设计费最高金额");
        }
        UserMsgVo userMsgVo = userCenterService.queryByPhone(phone);
        if(userMsgVo != null){
            throw new RuntimeException("该用户已注册");
        }
        userMsgVo = userCenterService.registerUser(userName, phone,false);
        String userId = userMsgVo.getStaffId();
        EmployeeMsg employeeMsg = new EmployeeMsg();
        employeeMsg.setEmail(email);
        employeeMsg.setProvince(province);
        employeeMsg.setRealName(userName);
        employeeMsg.setCity(city);
        employeeMsg.setSex(sex);
        employeeMsg.setArea(area);
        employeeMsg.setWorkingTime(Integer.parseInt(workingTime));
        employeeMsg.setUserId(userId);
        String roleCode = functionService.queryRoleCode(RoleFunctionEnum.DESIGN_POWER);
        //设计师编码
        employeeMsg.setRoleCode(roleCode);
        //未认证状态
        employeeMsg.setAuthState(Integer.parseInt("1"));
        employeeMsgMapper.insertSelective(employeeMsg);
        DesignerMsg designerMsg = new DesignerMsg();
        designerMsg.setVolumeRoomMoney(new BigDecimal(volumeRoomMoney));
        designerMsg.setDesignerMoneyLow(new BigDecimal(designerMoneyLow));
        designerMsg.setDesignerMoneyHigh(new BigDecimal(designerMoneyHigh));
        designerMsg.setUserId(userId);
        //后台创建
        designerMsg.setSource(3);
        //未审核
        designerMsg.setReviewState(1);
        designerMsgMapper.insertSelective(designerMsg);
        List<String> styleCodes = JSONObject.parseArray(masterStyle,String.class);
        for (String styleCode : styleCodes) {
            DesignerStyleRelation styleRelation = new DesignerStyleRelation();
            styleRelation.setDesignerId(userId);
            styleRelation.setStyleCode(styleCode);
            relationMapper.insertSelective(styleRelation);
        }
    }

    /**
     * 导入设计师excel
     *
     * @param designerFile excel文件
     * @param optionId     操作人ID
     * @param companyId    操作人所属公司
     */
    @Override
    public void importDesign(MultipartFile designerFile, String optionId, String companyId) {
        try {
            InputStream inputStream1 = designerFile.getInputStream();
            List<ExcelToListMap.TableTitle> tableTitles = new ArrayList<>();
            tableTitles.add(new ExcelToListMap.TableTitle("用户名", "userName"));
            tableTitles.add(new ExcelToListMap.TableTitle("手机号", "userPhone"));
            tableTitles.add(new ExcelToListMap.TableTitle("性别", "sex", new String[]{"男:1", "女:2"}));
            tableTitles.add(new ExcelToListMap.TableTitle("省", "provide", provinces()));
            tableTitles.add(new ExcelToListMap.TableTitle("市", "city", cites()));
            tableTitles.add(new ExcelToListMap.TableTitle("县/地区", "area", areas()));
            tableTitles.add(new ExcelToListMap.TableTitle("量房费", "money"));
            tableTitles.add(new ExcelToListMap.TableTitle("设计费", "designMoney"));
            List<Map<String, String>> listMap = ExcelToListMap.analysis(inputStream1, tableTitles);
            //TODO 批量注册接口
            System.out.println(JSONObject.toJSONString(listMap));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("excel解析失败:" + e.getMessage());
        }
    }

    /**
     * 获取省份
     *
     * @return
     */
    private Map<String, String> provinces() {
        ProvinceExample provinceExample = new ProvinceExample();
        provinceExample.createCriteria();
        List<Province> provinces = provinceMapper.selectByExample(provinceExample);
        return ReflectUtils.listToMap(provinces, "provinceName", "provinceCode");
    }

    /**
     * 获取城市
     *
     * @return
     */
    private Map<String, String> cites() {
        CityExample cityExample = new CityExample();
        cityExample.createCriteria();
        List<City> cities = cityMapper.selectByExample(cityExample);
        return ReflectUtils.listToMap(cities, "cityName", "cityCode");
    }

    /**
     * 获取地区
     *
     * @return
     */
    private Map<String, String> areas() {
        AreaExample areaExample = new AreaExample();
        areaExample.createCriteria();
        List<Area> areas = areaMapper.selectByExample(areaExample);
        return ReflectUtils.listToMap(areas, "areaName", "areaCode");
    }
}
