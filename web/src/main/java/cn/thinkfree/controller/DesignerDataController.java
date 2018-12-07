package cn.thinkfree.controller;

import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.database.vo.DesignerDataVo;
import cn.thinkfree.service.designerdata.DesignerDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author gejiaming
 */
@Api(tags = "设计师个人资料-app")
@RestController
@RequestMapping("designerData")
public class DesignerDataController {
    @Autowired
    DesignerDataService designerDataService;

    @ApiOperation("编辑资料")
    @RequestMapping(value = "editData", method = {RequestMethod.GET, RequestMethod.POST})
    public MyRespBundle editData(
            @RequestParam(value = "userId") @ApiParam(name = "userId", value = "用户编号", required = true) String userId,
            @RequestParam(value = "type") @ApiParam(name = "type", value = "1,编辑性别  2,编辑生日 3,编辑所在地区 4,编辑从业年限  5,编辑量房费  6,编辑设计费  7,编辑设计师擅长风格  8,编辑个人简介  9,编辑证书与奖项", required = true) Integer type,
            @RequestParam(value = "birthday", required = false) @ApiParam(name = "birthday", value = "日期 yyyy-MM-dd", required = false) String birthday,
            @RequestParam(value = "province", required = false) @ApiParam(name = "province", value = "省份编码", required = false) String province,
            @RequestParam(value = "city", required = false) @ApiParam(name = "city", value = "城市编码", required = false) String city,
            @RequestParam(value = "area", required = false) @ApiParam(name = "area", value = "地区编码", required = false) String area,
            @RequestParam(value = "years", required = false) @ApiParam(name = "years", value = "从业年限", required = false) Integer years,
            @RequestParam(value = "volumeRoomMoney", required = false) @ApiParam(name = "volumeRoomMoney", value = "量房费", required = false) String volumeRoomMoney,
            @RequestParam(value = "moneyLow", required = false) @ApiParam(name = "moneyLow", value = "设计费用最低(单位：元/m2)", required = false) String moneyLow,
            @RequestParam(value = "moneyHigh", required = false) @ApiParam(name = "moneyHigh", value = "设计费用最高(单位：元/m2)", required = false) String moneyHigh,
            @RequestParam(value = "styleCodes", required = false) @ApiParam(name = "styleCodes", value = "风格编码", required = false) List<String> styleCodes,
            @RequestParam(value = "personalProfile", required = false) @ApiParam(name = "personalProfile", value = "个人简介", required = false) String personalProfile,
            @RequestParam(value = "certificatePrize", required = false) @ApiParam(name = "certificatePrize", value = "证书与奖项", required = false) String certificatePrize,
            @RequestParam(value = "sex", required = false) @ApiParam(name = "sex", value = "性别 1男，2女", required = false) Integer sex) {
        MyRespBundle result;
        switch (type) {
            case 1:
                result = designerDataService.editSex(userId, sex);
                break;
            case 2:
                result = designerDataService.editBirthday(userId, birthday);
                break;
            case 3:
                result = designerDataService.editAdress(userId, province, city, area);
                break;
            case 4:
                result = designerDataService.editYears(userId, years);
                break;
            case 5:
                result = designerDataService.editVolumeRoomMoney(userId, volumeRoomMoney);
                break;
            case 6:
                result = designerDataService.editDesignFee(userId, moneyLow, moneyHigh);
                break;
            case 7:
                result = designerDataService.editDesignerStyle(userId, styleCodes);
                break;
            case 8:
                result = designerDataService.editPersonalProfile(userId, personalProfile);
                break;
            default:
                result = designerDataService.editCertificatePrize(userId, certificatePrize);
                break;

        }
        return result;
    }

    @ApiOperation("获取设计师个人资料")
    @RequestMapping(value = "getData", method = {RequestMethod.GET, RequestMethod.POST})
    public MyRespBundle<DesignerDataVo> getData(@RequestParam(value = "userId") @ApiParam(name = "userId", value = "用户编号", required = true) String userId){
        return designerDataService.getData(userId);
    }


}
