package cn.thinkfree.service.designerdata;

import cn.thinkfree.core.base.RespData;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.database.mapper.*;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.vo.DesignerDataVo;
import cn.thinkfree.service.utils.DateUtil;
import cn.thinkfree.service.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gejiaming
 */
@Service
public class DesignerDataServiceImpl implements DesignerDataService {
    @Autowired
    EmployeeMsgMapper employeeMsgMapper;
    @Autowired
    DesignerMsgMapper designerMsgMapper;
    @Autowired
    DesignerStyleRelationMapper designerStyleRelationMapper;

    /**
     * 编辑性别
     *
     * @param userId
     * @param sex
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyRespBundle editSex(String userId, Integer sex) {
        if (sex == null) {
            return RespData.error("请上传参数");
        }
        Map<String, Object> map = checkDesigner(userId);
        if (!(boolean) map.get("result")) {
            return RespData.error(map.get("message").toString());
        }
        EmployeeMsgExample example = (EmployeeMsgExample) map.get("example");

        EmployeeMsg employeeMsg = new EmployeeMsg();
        employeeMsg.setSex(sex);
        int i = employeeMsgMapper.updateByExampleSelective(employeeMsg, example);
        if (i == 0) {
            return RespData.error("修改失败");
        }
        return RespData.success();
    }

    /**
     * 编辑生日
     *
     * @param userId
     * @param birthday
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyRespBundle editBirthday(String userId, String birthday) {
        if (birthday.trim().isEmpty()) {
            return RespData.error("请上传参数");
        }
        Map<String, Object> map = checkDesigner(userId);
        if (!(boolean) map.get("result")) {
            return RespData.error(map.get("message").toString());
        }
        EmployeeMsgExample example = (EmployeeMsgExample) map.get("example");
        EmployeeMsg employeeMsg = new EmployeeMsg();
        Date date = DateUtils.strToDate(birthday, "yyyy-MM-dd");
        employeeMsg.setBirthday(date);
        int i = employeeMsgMapper.updateByExampleSelective(employeeMsg, example);
        if (i == 0) {
            return RespData.error("修改失败");
        }
        return RespData.success();
    }

    /**
     * 编辑所在地区
     *
     * @param userId
     * @param province
     * @param city
     * @param area
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyRespBundle editAdress(String userId, String province, String city, String area) {
        if (province.trim().isEmpty() || city.trim().isEmpty() || area.trim().isEmpty()) {
            return RespData.error("请上传参数");
        }
        Map<String, Object> map = checkDesigner(userId);
        if (!(boolean) map.get("result")) {
            return RespData.error(map.get("message").toString());
        }
        EmployeeMsgExample example = (EmployeeMsgExample) map.get("example");
        EmployeeMsg employeeMsg = new EmployeeMsg();
        employeeMsg.setProvince(province);
        employeeMsg.setCity(city);
        employeeMsg.setArea(area);
        int i = employeeMsgMapper.updateByExampleSelective(employeeMsg, example);
        if (i == 0) {
            return RespData.error("修改失败");
        }
        return RespData.success();
    }

    /**
     * 编辑从业年限
     *
     * @param userId
     * @param years
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyRespBundle editYears(String userId, Integer years) {
        if (years == null) {
            return RespData.error("请上传参数");
        }
        Map<String, Object> map = checkDesigner(userId);
        if (!(boolean) map.get("result")) {
            return RespData.error(map.get("message").toString());
        }
        EmployeeMsgExample example = (EmployeeMsgExample) map.get("example");
        EmployeeMsg employeeMsg = new EmployeeMsg();
        employeeMsg.setWorkingTime(years);
        int i = employeeMsgMapper.updateByExampleSelective(employeeMsg, example);
        if (i == 0) {
            return RespData.error("修改失败");
        }
        return RespData.success();
    }

    /**
     * 编辑量房费
     *
     * @param userId
     * @param volumeRoomMoney
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyRespBundle editVolumeRoomMoney(String userId, String volumeRoomMoney) {
        if (volumeRoomMoney.trim().isEmpty()) {
            return RespData.error("请上传参数");
        }
        Map<String, Object> map = checkDesignerDetails(userId);
        if (!(boolean) map.get("result")) {
            return RespData.error(map.get("message").toString());
        }
        DesignerMsgExample example = (DesignerMsgExample) map.get("example");
        DesignerMsg designerMsg = new DesignerMsg();
        designerMsg.setVolumeRoomMoney(new BigDecimal(volumeRoomMoney));
        int i = designerMsgMapper.updateByExampleSelective(designerMsg, example);
        if (i == 0) {
            return RespData.error("修改失败");
        }
        return RespData.success();
    }

    /**
     * 编辑设计费
     *
     * @param userId
     * @param moneyLow
     * @param moneyHigh
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyRespBundle editDesignFee(String userId, String moneyLow, String moneyHigh) {
        if (moneyLow.trim().isEmpty() || moneyHigh.trim().isEmpty()) {
            return RespData.error("请上传参数");
        }
        Map<String, Object> map = checkDesignerDetails(userId);
        if (!(boolean) map.get("result")) {
            return RespData.error(map.get("message").toString());
        }
        DesignerMsgExample example = (DesignerMsgExample) map.get("example");
        BigDecimal lowMonney = new BigDecimal(moneyLow);
        BigDecimal highMoney = new BigDecimal(moneyHigh);
        if (lowMonney.longValue() >= highMoney.longValue()) {
            return RespData.error("输入不合法");
        }
        DesignerMsg designerMsg = new DesignerMsg();
        designerMsg.setDesignerMoneyHigh(highMoney);
        designerMsg.setDesignerMoneyLow(lowMonney);
        int i = designerMsgMapper.updateByExampleSelective(designerMsg, example);
        if (i == 0) {
            return RespData.error("修改失败");
        }
        return RespData.success();
    }

    /**
     * 编辑个人简介
     *
     * @param userId
     * @param personalProfile
     * @return
     */
    @Override
    public MyRespBundle editPersonalProfile(String userId, String personalProfile) {
        if (personalProfile.trim().isEmpty()) {
            return RespData.error("请上传参数");
        }
        Map<String, Object> map = checkDesignerDetails(userId);
        if (!(boolean) map.get("result")) {
            return RespData.error(map.get("message").toString());
        }
        DesignerMsgExample example = (DesignerMsgExample) map.get("example");
        if (personalProfile.trim().length() >= 200) {
            return RespData.error("输入不合法");
        }
        DesignerMsg designerMsg = new DesignerMsg();
        designerMsg.setPersonalProfile(personalProfile);
        int i = designerMsgMapper.updateByExampleSelective(designerMsg, example);
        if (i == 0) {
            return RespData.error("修改失败");
        }
        return RespData.success();
    }

    /**
     * 编辑证书与奖项
     *
     * @param userId
     * @param certificatePrize
     * @return
     */
    @Override
    public MyRespBundle editCertificatePrize(String userId, String certificatePrize) {
        if (certificatePrize.trim().isEmpty()) {
            return RespData.error("请上传参数");
        }
        Map<String, Object> map = checkDesignerDetails(userId);
        if (!(boolean) map.get("result")) {
            return RespData.error(map.get("message").toString());
        }
        DesignerMsgExample example = (DesignerMsgExample) map.get("example");
        if (certificatePrize.trim().length() >= 200) {
            return RespData.error("输入不合法");
        }
        DesignerMsg designerMsg = new DesignerMsg();
        designerMsg.setCertificatePrize(certificatePrize);
        int i = designerMsgMapper.updateByExampleSelective(designerMsg, example);
        if (i == 0) {
            return RespData.error("修改失败");
        }
        return RespData.success();
    }

    /**
     * 编辑设计师擅长风格
     *
     * @param userId
     * @param styleCodes
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyRespBundle editDesignerStyle(String userId, List<String> styleCodes) {
        if (styleCodes == null || styleCodes.size() == 0) {
            return RespData.error("请上传参数");
        }
        DesignerStyleRelationExample example = new DesignerStyleRelationExample();
        DesignerStyleRelationExample.Criteria criteria = example.createCriteria();
        criteria.andDesignerIdEqualTo(userId);
        List<DesignerStyleRelation> designerStyleRelations = designerStyleRelationMapper.selectByExample(example);
        if (designerStyleRelations.size() != 0) {
            designerStyleRelationMapper.deleteByExample(example);
        }
        for (String styleCode : styleCodes) {
            DesignerStyleRelation relation = new DesignerStyleRelation();
            relation.setDesignerId(userId);
            relation.setStyleCode(styleCode);
            int i = designerStyleRelationMapper.insertSelective(relation);
            if (i == 0) {
                return RespData.error("styleCode=" + styleCode + ";插入失败");
            }
        }
        return RespData.success();
    }

    /**
     * 校验设计师是否存在(员工表)
     *
     * @return
     */
    public Map<String, Object> checkDesigner(String userId) {
        Map<String, Object> map = new HashMap<>();
        EmployeeMsgExample example = new EmployeeMsgExample();
        EmployeeMsgExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        List<EmployeeMsg> employeeMsgs = employeeMsgMapper.selectByExample(example);
        if (employeeMsgs.size() == 0) {
            map.put("result", false);
            map.put("message", "查无此员工");
            return map;
        }
        map.put("result", true);
        map.put("example", example);
        return map;
    }

    /**
     * 校验设计师是否存在(设计师信息表)
     *
     * @return
     */
    public Map<String, Object> checkDesignerDetails(String userId) {
        Map<String, Object> map = new HashMap<>();
        DesignerMsgExample example = new DesignerMsgExample();
        DesignerMsgExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        List<DesignerMsg> designerMsgs = designerMsgMapper.selectByExample(example);
        if (designerMsgs.size() == 0) {
            map.put("result", false);
            map.put("message", "查无此员工");
            return map;
        }
        map.put("result", true);
        map.put("example", example);
        return map;
    }

    /**
     * 获取设计师个人资料
     *
     * @param userId
     * @return
     */
    @Override
    public MyRespBundle<DesignerDataVo> getData(String userId) {
        DesignerDataVo designerDataVo = new DesignerDataVo();
        //获取员工表信息
        List<EmployeeMsg> employeeMsgs = employeeMsgMapper.selectForUserId(userId);
        if (employeeMsgs.size() == 0) {
            return RespData.error("查无此员工");
        }
        EmployeeMsg employeeMsg = employeeMsgs.get(0);
        if (employeeMsg.getSex() != null) {
            designerDataVo.setSex(employeeMsg.getSex());
        }
        if (employeeMsg.getBirthday() != null) {
            designerDataVo.setBirthday(DateUtil.getStringDate(employeeMsg.getBirthday(), "yyyy-MM-dd"));
        }
        if (employeeMsg.getProvince() != null) {
            designerDataVo.setProvince(employeeMsg.getProvince());
        }
        if (employeeMsg.getCity() != null) {
            designerDataVo.setCity(employeeMsg.getCity());
        }
        if (employeeMsg.getArea() != null) {
            designerDataVo.setArea(employeeMsg.getArea());
        }
        if (employeeMsg.getWorkingTime() != null) {
            designerDataVo.setYears(employeeMsg.getWorkingTime());
        }
        //获取设计师信息
        DesignerMsgExample example1 = new DesignerMsgExample();
        DesignerMsgExample.Criteria criteria1 = example1.createCriteria();
        criteria1.andUserIdEqualTo(userId);
        List<DesignerMsg> designerMsgs = designerMsgMapper.selectByExample(example1);
        if (designerMsgs.size() > 0) {
            DesignerMsg designerMsg = designerMsgs.get(0);
            if (designerMsg.getVolumeRoomMoney() != null) {
                designerDataVo.setVolumeRoomMoney(designerMsg.getVolumeRoomMoney().toString());
            }
            if (designerMsg.getDesignerMoneyLow() != null) {
                designerDataVo.setMoneyLow(designerMsg.getDesignerMoneyLow().toString());
            }
            if (designerMsg.getDesignerMoneyHigh() != null) {
                designerDataVo.setMoneyHigh(designerMsg.getDesignerMoneyHigh().toString());
            }
            if (designerMsg.getPersonalProfile() != null) {
                designerDataVo.setPersonalProfile(designerMsg.getPersonalProfile());
            }
            if (designerMsg.getCertificatePrize() != null) {
                designerDataVo.setCertificatePrize(designerMsg.getCertificatePrize());
            }
        }
        //获取设计师设计风格
        List<String> designerTypes = designerStyleRelationMapper.selectByUserId(userId);
        designerDataVo.setStyleCodes(designerTypes);
        return RespData.success(designerDataVo);
    }
}
