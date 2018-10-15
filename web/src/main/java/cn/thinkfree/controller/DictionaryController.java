package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.model.*;
import cn.thinkfree.service.dictionary.DictionaryService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@ApiOperation("字典相关接口")
@RestController
@RequestMapping("dictionary")
public class DictionaryController extends AbsBaseController {


    @Autowired
    DictionaryService dictionaryService;



    /**
     * 获取省份信息
     * @return
     */
    @ApiOperation(value = "获取省份信息",notes = "获取省份信息")
    @GetMapping("/province")
    @MyRespBody
    public MyRespBundle<List<Province>>  province(){

        List<Province> provinceList = dictionaryService.findAllProvince();

        return sendJsonData(ResultMessage.SUCCESS,provinceList);
    }


    /**
     * 获取市区信息
     * @param provinceCode
     * @return
     */
    @ApiOperation(value = "获取省份下市区信息",notes = "获取省份下市区信息")
    @GetMapping("/city")
    @MyRespBody
    public MyRespBundle<List<City>> city(@ApiParam("省份编码") @RequestParam String provinceCode){
        List<City> cityList = dictionaryService.findCityByProvince(provinceCode);
        return sendJsonData(ResultMessage.SUCCESS,cityList);
    }

    /**
     * 获取区县信息
     * @param cityCode
     * @return
     */
    @ApiOperation(value = "根据市区获取县区",notes = "根据市区获取县信息")
    @GetMapping("/area")
    @MyRespBody
    public MyRespBundle<List<Area>> area(@RequestParam String cityCode){
        List<Area> areaList = dictionaryService.findAreaByCity(cityCode);
        return sendJsonData(ResultMessage.SUCCESS,areaList);
    }

    /**
     * 获取房屋类型
     * @return
     */
    @ApiOperation(value = "获取房屋类型",notes = "获取房屋类型")
    @GetMapping("/houseType")
    @MyRespBody
    public MyRespBundle<List<PreProjectHouseType>> houseType(){
        List<PreProjectHouseType>  preProjectHouseTypes= dictionaryService.findAllHouseType();
        return sendJsonData(ResultMessage.SUCCESS,preProjectHouseTypes);
    }

    /**
     * 获取房屋新旧程度
     * @return
     */
    @ApiOperation(value = "获取房屋新旧程度",notes = "获取房屋新旧程度")
    @GetMapping("/houseStatus")
    @MyRespBody
    public MyRespBundle<List<HousingStatus>> houseStatus(){
        List<HousingStatus>  preProjectHouseTypes= dictionaryService.findAlHouseStatus();
        return sendJsonData(ResultMessage.SUCCESS,preProjectHouseTypes);
    }

    /**
     * 获取项目套餐类型
     * @return
     */
    @ApiOperation(value = "获取项目套餐类型",notes = "获取项目套餐类型")
    @GetMapping("/projectType")
    @MyRespBody
    public MyRespBundle<List<ProjectType>> projectType(){
        List<ProjectType> projectTypes = dictionaryService.findAllProjectType();
        return sendJsonData(ResultMessage.SUCCESS,projectTypes);
    }

    /**
     * 根据县区编码获取公司信息
     * @param areaCode
     * @return
     */
    @ApiOperation(value = "根据县区获取公司信息",notes = "根据县区获取公司")
    @GetMapping("/companyByAreaCode")
    @MyRespBody
    public MyRespBundle<String> company(@RequestParam  Integer areaCode){
        List<CompanyInfo> companyInfos=dictionaryService.findCompanyByAreaCode(areaCode);
        return sendJsonData(ResultMessage.SUCCESS,companyInfos);
    }
    /**
     * 根据省市级编码获取公司信息
     * @param cityCode
     * @return
     */
    @ApiOperation(value = "根据省市级级获取公司信息",notes = "根据省市级级获取公司")
    @GetMapping("/findCompanyByCode")
    @MyRespBody
    public MyRespBundle<String> companyByCityCode(@RequestParam(required = false) Integer provinceCode, @RequestParam(required = false) Integer cityCode){
        List<CompanyInfo> companyInfos=dictionaryService.findCompanyByCode(provinceCode,cityCode);
        return sendJsonData(ResultMessage.SUCCESS,companyInfos);
    }
    /**
     * 获取岗位信息
     */
    @GetMapping("/getRole")
    @MyRespBody
    @ApiOperation(value = "员工详情(公告--->对象)--->修改岗位--->岗位信息")
    public MyRespBundle<List<UserRoleSet>> getRole(){

        List<UserRoleSet> userRoleSet = dictionaryService.getRole();
        return sendJsonData(ResultMessage.SUCCESS, userRoleSet);
    }

    /**
     * 获取岗位信息
     */
    @GetMapping("/getCompanyRole")
    @MyRespBody
    @ApiOperation(value = "入驻公司角色")
    public MyRespBundle<List<UserRoleSet>> getCompanyRole(){

        List<UserRoleSet> userRoleSet = dictionaryService.getCompanyRole();
        return sendJsonData(ResultMessage.SUCCESS, userRoleSet);
    }
//    @GetMapping("/resource")
//    @MyRespBody
//    public MyRespBundle<List<SystemResource>> resources(){
//        List<SystemResource> resources =dictionaryService.listResource();
//        return sendJsonData(ResultMessage.SUCCESS,resources);
//    }

}
