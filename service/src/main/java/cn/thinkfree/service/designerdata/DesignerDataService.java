package cn.thinkfree.service.designerdata;

import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.database.vo.DesignerDataVo;

import java.util.List;

/**
 * @author gejiaming
 */
public interface DesignerDataService {
    /**
     * 编辑性别
     * @param userId
     * @param sex
     * @return
     */
    MyRespBundle editSex(String userId, Integer sex);

    /**
     * 编辑生日
     * @param userId
     * @param birthday
     * @return
     */
    MyRespBundle editBirthday(String userId, String birthday);

    /**
     * 编辑所在地区
     * @param userId
     * @param province
     * @param city
     * @param area
     * @return
     */
    MyRespBundle editAdress(String userId, String province, String city, String area);

    /**
     * 编辑从业年限
     * @param userId
     * @param years
     * @return
     */
    MyRespBundle editYears(String userId, Integer years);

    /**
     * 编辑量房费
     * @param userId
     * @param volumeRoomMoney
     * @return
     */
    MyRespBundle editVolumeRoomMoney(String userId, String volumeRoomMoney);

    /**
     * 编辑设计费
     * @param userId
     * @param moneyLow
     * @param moneyHigh
     * @return
     */
    MyRespBundle editDesignFee(String userId, String moneyLow, String moneyHigh);

    /**
     * 编辑个人简介
     * @param userId
     * @param personalProfile
     * @return
     */
    MyRespBundle editPersonalProfile(String userId, String personalProfile);

    /**
     * 编辑证书与奖项
     * @param userId
     * @param certificatePrize
     * @return
     */
    MyRespBundle editCertificatePrize(String userId, String certificatePrize);

    /**
     * 编辑设计师擅长风格
     * @param userId
     * @param styleCodes
     * @return
     */
    MyRespBundle editDesignerStyle(String userId, List<String> styleCodes);

    /**
     * 获取设计师个人资料
     * @param userId
     * @return
     */
    MyRespBundle<DesignerDataVo> getData(String userId);
}
