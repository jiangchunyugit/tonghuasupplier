package cn.thinkfree.service.platform.designer.impl;

import cn.thinkfree.database.mapper.*;
import cn.thinkfree.database.model.*;
import cn.thinkfree.service.platform.designer.DesignerService;
import cn.thinkfree.service.platform.designer.vo.DesignerMsgListVo;
import cn.thinkfree.service.platform.designer.vo.PageVo;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author xusonghui
 * 设计师服务
 */
@Service
public class DesignerServiceImpl implements DesignerService {
    /**
     * 设计师编码code
     */
    private static final String DESIGNER_CODE = "CD";

    @Autowired
    private DesignerMsgMapper designerMsgMapper;
    @Autowired
    private DesignerStyleRelationMapper relationMapper;
    @Autowired
    private DesignerStyleConfigMapper styleConfigMapper;
    @Autowired
    private EmployeeMsgMapper employeeMsgMapper;
    @Autowired
    private ProvinceMapper provinceMapper;
    @Autowired
    private CityMapper cityMapper;
    @Autowired
    private AreaMapper areaMapper;

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
            String province, String city, String area, String level, String identity, String source,
            String tag, String registrationTimeStart, String registrationTimeEnd, String sort, int pageSize, int pageIndex) {
        //调用用户中心，模糊查询用户名信息
        EmployeeMsgExample employeeMsgExample = new EmployeeMsgExample();
        EmployeeMsgExample.Criteria msgExampleCriteria = employeeMsgExample.createCriteria();
        DesignerMsgExample msgExample = new DesignerMsgExample();
        DesignerMsgExample.Criteria criteria = msgExample.createCriteria();
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
        List<EmployeeMsg> employeeMsgs = employeeMsgMapper.selectByExample(employeeMsgExample);
        List<String> userIds = ReflectUtils.getList(employeeMsgs, "userId");
        Map<String, EmployeeMsg> employeeMsgMap = ReflectUtils.listToMap(employeeMsgs, "userId");
        if (!userIds.isEmpty()) {
            criteria.andUserIdIn(userIds);
        }
        if (!StringUtils.isEmpty(level)) {
            criteria.andLevelEqualTo(Long.parseLong(level));
        }
        if (!StringUtils.isEmpty(identity)) {
            criteria.andIdentityEqualTo(Long.parseLong(identity));
        }
        if (!StringUtils.isEmpty(source)) {
            criteria.andSourceEqualTo(Integer.parseInt(source));
        }
        if (!StringUtils.isEmpty(tag)) {
            criteria.andTagEqualTo(Long.parseLong(tag));
        }
        if (!StringUtils.isEmpty(sort)) {
            criteria.andWeightEqualTo(Long.parseLong(sort));
        }
        long total = designerMsgMapper.countByExample(msgExample);
        PageHelper.startPage(pageIndex - 1, pageSize);
        List<DesignerMsg> designerMsgs = designerMsgMapper.selectByExample(msgExample);
        List<DesignerMsgListVo> msgVos = new ArrayList<>();
        for (DesignerMsg msg : designerMsgs) {
            DesignerMsgListVo msgVo = new DesignerMsgListVo();
            msgVo.setIdentityName(msg.getIdentity() + "");
            msgVo.setLevelName(msg.getLevel() + "");
            msgVo.setSource(msg.getSource() + "");
            msgVo.setTag(msg.getTag() + "");
            msgVo.setUserId(msg.getUserId());
            EmployeeMsg employeeMsg = employeeMsgMap.get(msg.getUserId());
            if (employeeMsg != null) {
                msgVo.setAddress(employeeMsg.getProvince() + "," + employeeMsg.getCity() + "," + employeeMsg.getArea());
                msgVo.setAuthState(employeeMsg.getAuthState() + "");
                msgVo.setCompanyName(employeeMsg.getCompanyId());
                msgVo.setRegistrationTime(DateUtils.dateToStr(employeeMsg.getBindDate()));
                msgVo.setSex(employeeMsg.getSex() + "");
                msgVo.setRealName(employeeMsg.getRealName());
            }
            msgVos.add(msgVo);
        }
        PageVo<List<DesignerMsgListVo>> msgVo = new PageVo<>();
        msgVo.setTotal(total);
        msgVo.setPageSize(pageSize);
        msgVo.setData(msgVos);
        return msgVo;
    }

    /**
     * 根据用户ID查询设计师信息
     *
     * @param userId 用户ID
     * @return
     */
    @Override
    public DesignerMsg queryDesignerByUserId(String userId) {
        DesignerMsgExample msgExample = new DesignerMsgExample();
        msgExample.createCriteria().andUserIdEqualTo(userId);
        List<DesignerMsg> designerMsgs = designerMsgMapper.selectByExample(msgExample);
        if (!designerMsgs.isEmpty()) {
            return designerMsgs.get(0);
        }
        return null;
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
    public List<DesignerStyleConfig> queryDesignerStyleByUserId(String userId) {
        DesignerStyleRelationExample relationExample = new DesignerStyleRelationExample();
        relationExample.createCriteria().andDesignerIdEqualTo(userId);
        List<DesignerStyleRelation> designerStyleRelations = relationMapper.selectByExample(relationExample);
        if (!designerStyleRelations.isEmpty()) {
            List<String> styleCodes = ReflectUtils.getList(designerStyleRelations, "styleCode");
            DesignerStyleConfigExample styleConfigExample = new DesignerStyleConfigExample();
            styleConfigExample.createCriteria().andStyleCodeIn(styleCodes);
            List<DesignerStyleConfig> designerStyleConfigs = styleConfigMapper.selectByExample(styleConfigExample);
            return designerStyleConfigs;
        }
        return null;
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
    public List<DesignerStyleConfig> queryDesignerStyle() {
        DesignerStyleConfigExample configExample = new DesignerStyleConfigExample();
        configExample.createCriteria();
        List<DesignerStyleConfig> styleConfigs = styleConfigMapper.selectByExample(configExample);
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
            String phone, String email, String province, String city, String area, String workingTime,
            String masterStyle, String volumeRoomMoney, String designerMoneyLow, String designerMoneyHigh) {
        // TODO 调用用户中心，注册用户
        String userId = UUID.randomUUID().toString().replaceAll("-", "");
        EmployeeMsg employeeMsg = new EmployeeMsg();
        employeeMsg.setEmail(email);
        employeeMsg.setProvince(province);
        employeeMsg.setCity(city);
        employeeMsg.setArea(area);
        employeeMsg.setWorkingTime(Integer.parseInt(workingTime));
        employeeMsg.setUserId(userId);
        //设计师编码
        employeeMsg.setRoleCode(DESIGNER_CODE);
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
        String[] styleCodes = masterStyle.split(",");
        for (String styleCode : styleCodes) {
            DesignerStyleRelation styleRelation = new DesignerStyleRelation();
            styleRelation.setDesignerId(userId);
            styleRelation.setStyleCode(styleCode);
            relationMapper.insertSelective(styleRelation);
        }
    }

    /**
     * 导入设计师excel
     * @param designerFile excel文件
     * @param optionId     操作人ID
     * @param companyId    操作人所属公司
     */
    @Override
    public void importDesign(MultipartFile designerFile, String optionId, String companyId) {
        try{
            InputStream inputStream1 = designerFile.getInputStream();
            List<ExcelToListMap.TableTitle> tableTitles = new ArrayList<>();
            tableTitles.add(new ExcelToListMap.TableTitle("用户名","userName"));
            tableTitles.add(new ExcelToListMap.TableTitle("手机号","userPhone"));
            tableTitles.add(new ExcelToListMap.TableTitle("性别","sex",new String[]{"男:1","女:2"}));
            tableTitles.add(new ExcelToListMap.TableTitle("省","provide",provinces()));
            tableTitles.add(new ExcelToListMap.TableTitle("市","city", cites()));
            tableTitles.add(new ExcelToListMap.TableTitle("县/地区","area",areas()));
            tableTitles.add(new ExcelToListMap.TableTitle("量房费","money"));
            tableTitles.add(new ExcelToListMap.TableTitle("设计费","designMoney"));
            List<Map<String,String>> listMap = ExcelToListMap.analysis(inputStream1,tableTitles);
            //TODO 批量注册接口
            System.out.println(JSONObject.toJSONString(listMap));
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("excel解析失败:" + e.getMessage());
        }
    }

    /**
     * 获取省份
     * @return
     */
    private Map<String,String> provinces(){
        ProvinceExample provinceExample = new ProvinceExample();
        provinceExample.createCriteria();
        List<Province> provinces = provinceMapper.selectByExample(provinceExample);
        return ReflectUtils.listToMap(provinces,"provinceName","provinceCode");
    }

    /**
     * 获取城市
     * @return
     */
    private Map<String,String> cites(){
        CityExample cityExample = new CityExample();
        cityExample.createCriteria();
        List<City> cities = cityMapper.selectByExample(cityExample);
        return ReflectUtils.listToMap(cities,"cityName","cityCode");
    }

    /**
     * 获取地区
     * @return
     */
    private Map<String,String> areas(){
        AreaExample areaExample = new AreaExample();
        areaExample.createCriteria();
        List<Area> areas = areaMapper.selectByExample(areaExample);
        return ReflectUtils.listToMap(areas,"areaName","areaCode");
    }
}
