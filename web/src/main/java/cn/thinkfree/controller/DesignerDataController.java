package cn.thinkfree.controller;

import cn.thinkfree.core.bundle.MyRespBundle;
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

    @ApiOperation("编辑性别")
    @RequestMapping(value = "editSex", method = {RequestMethod.GET, RequestMethod.POST})
    public MyRespBundle editSex(
            @RequestParam("userId") @ApiParam(name = "userId", value = "用户编号", required = true) String userId,
            @RequestParam("sex") @ApiParam(name = "sex", value = "性别 1男，2女", required = true) Integer sex) {
        return designerDataService.editSex(userId, sex);
    }

    @ApiOperation("编辑生日")
    @RequestMapping(value = "editBirthday", method = {RequestMethod.GET, RequestMethod.POST})
    public MyRespBundle editBirthday(
            @RequestParam("userId") @ApiParam(name = "userId", value = "用户编号", required = true) String userId,
            @RequestParam("birthday") @ApiParam(name = "birthday", value = "日期 yyyy-MM-dd", required = true) String birthday) {
        return designerDataService.editBirthday(userId,birthday);
    }
    @ApiOperation("编辑所在地区")
    @RequestMapping(value = "editAdress", method = {RequestMethod.GET, RequestMethod.POST})
    public MyRespBundle editAdress(
            @RequestParam("userId") @ApiParam(name = "userId", value = "用户编号", required = true) String userId,
            @RequestParam("province") @ApiParam(name = "province", value = "省份编码", required = true) String province,
            @RequestParam("city") @ApiParam(name = "city", value = "城市编码", required = true) String city,
            @RequestParam("area") @ApiParam(name = "area", value = "地区编码", required = true) String area) {
        return designerDataService.editAdress(userId,province,city,area);
    }
    @ApiOperation("编辑从业年限")
    @RequestMapping(value = "editYears", method = {RequestMethod.GET, RequestMethod.POST})
    public MyRespBundle editYears(
            @RequestParam("userId") @ApiParam(name = "userId", value = "用户编号", required = true) String userId,
            @RequestParam("years") @ApiParam(name = "years", value = "从业年限", required = true) Integer years) {
        return designerDataService.editYears(userId,years);
    }

    @ApiOperation("编辑量房费")
    @RequestMapping(value = "editVolumeRoomMoney", method = {RequestMethod.GET, RequestMethod.POST})
    public MyRespBundle editVolumeRoomMoney(
            @RequestParam("userId") @ApiParam(name = "userId", value = "用户编号", required = true) String userId,
            @RequestParam("volumeRoomMoney") @ApiParam(name = "volumeRoomMoney", value = "", required = true) String volumeRoomMoney) {
        return designerDataService.editVolumeRoomMoney(userId,volumeRoomMoney);
    }

    @ApiOperation("编辑设计费")
    @RequestMapping(value = "editDesignFee", method = {RequestMethod.GET, RequestMethod.POST})
    public MyRespBundle editDesignFee(
            @RequestParam("userId") @ApiParam(name = "userId", value = "用户编号", required = true) String userId,
            @RequestParam("moneyLow") @ApiParam(name = "moneyLow", value = "设计费用最低(单位：元/m2)", required = true) String moneyLow,
            @RequestParam("moneyHigh") @ApiParam(name = "moneyHigh", value = "设计费用最高(单位：元/m2)", required = true) String moneyHigh) {
        return designerDataService.editDesignFee(userId,moneyLow,moneyHigh);
    }

//    @ApiOperation("获取设计师擅长风格")
//    @RequestMapping(value = "getDesignerStyle", method = {RequestMethod.GET, RequestMethod.POST})
//    public MyRespBundle getDesignerStyle(
//            @RequestParam("userId") @ApiParam(name = "userId", value = "用户编号", required = true) String userId) {
//        return designerDataService.getDesignerStyle(userId);
//    }

    @ApiOperation("编辑设计师擅长风格")
    @RequestMapping(value = "editDesignerStyle", method = {RequestMethod.GET, RequestMethod.POST})
    public MyRespBundle editDesignerStyle(
            @RequestParam("userId") @ApiParam(name = "userId", value = "用户编号", required = true) String userId,
            @RequestParam("styleCode") @ApiParam(name = "styleCode", value = "风格编码", required = true) List<String> styleCodes) {
        return designerDataService.editDesignerStyle(userId,styleCodes);
    }

    @ApiOperation("编辑个人简介")
    @RequestMapping(value = "editPersonalProfile", method = {RequestMethod.GET, RequestMethod.POST})
    public MyRespBundle editPersonalProfile(
            @RequestParam("userId") @ApiParam(name = "userId", value = "用户编号", required = true) String userId,
            @RequestParam("personalProfile") @ApiParam(name = "personalProfile", value = "个人简介", required = true) String personalProfile) {
        return designerDataService.editPersonalProfile(userId,personalProfile);
    }

    @ApiOperation("编辑证书与奖项")
    @RequestMapping(value = "editCertificatePrize", method = {RequestMethod.GET, RequestMethod.POST})
    public MyRespBundle editCertificatePrize(
            @RequestParam("userId") @ApiParam(name = "userId", value = "用户编号", required = true) String userId,
            @RequestParam("certificatePrize") @ApiParam(name = "certificatePrize", value = "证书与奖项", required = true) String certificatePrize) {
        return designerDataService.editCertificatePrize(userId,certificatePrize);
    }


}
