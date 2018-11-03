package cn.thinkfree.controller;

import java.util.List;
import java.util.Map;

import cn.thinkfree.database.constants.CompanyAuditStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.model.*;
import cn.thinkfree.service.account.PermissionService;
import cn.thinkfree.service.account.SystemRoleService;
import cn.thinkfree.database.model.Area;
import cn.thinkfree.database.model.City;
import cn.thinkfree.database.model.CompanyInfo;
import cn.thinkfree.database.model.ConstructionBaseDic;
import cn.thinkfree.database.model.HousingStatus;
import cn.thinkfree.database.model.PreProjectHouseType;
import cn.thinkfree.database.model.ProjectType;
import cn.thinkfree.database.model.Province;
import cn.thinkfree.database.model.UserRoleSet;
import cn.thinkfree.database.vo.MybaseDic;
import cn.thinkfree.service.basedic.BaseDicService;
import cn.thinkfree.service.dictionary.DictionaryService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@ApiOperation("字典相关接口")
@RestController
@RequestMapping("dictionary")
public class DictionaryController extends AbsBaseController {


    @Autowired
    DictionaryService dictionaryService;

    @Autowired
    BaseDicService baseDicService;

    @Autowired
    PermissionService permissionService;

    @Autowired
    SystemRoleService systemRoleService;



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


    /**
     * 查询 字典列表信息
     * @author lqd
     * @return json
     */
    @PostMapping("/getBaseDicList")
    @MyRespBody
    @ApiOperation(value = "平台字典关联列表", notes = "根据type查询---0户型结构字典 1 房屋类型字典 2房屋属性 3计费项目类型（设计）4计费项目设置（施工）5施工阶段设置 6项目阶段设置 ")
    public MyRespBundle<List<MybaseDic>> getBaseDicList(@RequestParam(required = true) @ApiParam("字典类型") String type){

    	List<MybaseDic> resDicList = baseDicService.getDicListByType(type);

        return sendJsonData(ResultMessage.SUCCESS, resDicList);
    }

    /**
     * 新增字典列表信息
     * @author lqd
     * @return json
     * @RequestParam(required = false)
     */
    @PostMapping("/insertBaseDic")
    @MyRespBody
    @ApiOperation(value = "平台字典关联列表", notes = "根据type查询---0户型结构字典 1 房屋类型字典 2房屋属性 3计费项目类型（设计）4计费项目设置（施工）5施工阶段设置 6项目阶段设置 ")
    @ApiImplicitParams({ @ApiImplicitParam(paramType = "insert", dataType = "String", name = "type", value = "字典类型值", required = true)
    		,@ApiImplicitParam(paramType = "insert", dataType = "String", name = "dicValue", value = "字典value值", required = true),
        @ApiImplicitParam(paramType = "insert", dataType = "String", name = "remarks", value = "备注", required = true)
    }
    )
    public MyRespBundle<String>  insertBaseDic( @RequestParam(required = true)  String type,
    		@RequestParam(required = true) String dicValue,@RequestParam(required = false) String remarks){
	  boolean flag = baseDicService.insertDic(type, dicValue,remarks);
      if(flag){
          return sendJsonData(ResultMessage.SUCCESS, "操作成功");
      }
      return sendJsonData(ResultMessage.FAIL, "操作失败");
    }

    /**
     * 新增字典列表信息
     * @author lqd
     * @return json
     */
    @PostMapping("/updateBaseDic")
    @MyRespBody
    @ApiOperation(value = "修改平台字典名称", notes = "根据id修改名称 ")
    @ApiImplicitParams({ @ApiImplicitParam(paramType = "update", dataType = "String", name = "dicCode", value = "字典类型值", required = true)
	,@ApiImplicitParam(paramType = "update", dataType = "String", name = "dicValue", value = "字典value值", required = true),
	@ApiImplicitParam(paramType = "update", dataType = "String", name = "remarks", value = "备注", required = true)
	})
    public MyRespBundle<String>  updateBaseDic(  @RequestParam(required = true)  String dicCode,
    		@RequestParam(required = true) String dicValue,@RequestParam(required = false) String remarks){

	  boolean flag = baseDicService.updateDicName(dicCode, dicValue, remarks);
      if(flag){
          return sendJsonData(ResultMessage.SUCCESS, "操作成功");
      }
      return sendJsonData(ResultMessage.FAIL, "操作失败");
    }

    /**
     * 查询 字典列表信息
     * @author lqd
     * @return json
     */
    @PostMapping("/getConstructionBaseDicList")
    @MyRespBody
    @ApiOperation(value = "施工平台施工项目列表")
    public MyRespBundle<List<ConstructionBaseDic>> getConstructionBaseDicList(){

    	List<ConstructionBaseDic> resDicList = baseDicService.getConstructionDicList();

        return sendJsonData(ResultMessage.SUCCESS, resDicList);
    }



    /**
    *  新增 字典列表信息
    * @author lqd
    * @return json
    */
    @PostMapping("/inserConstructionBaseDicList")
   @MyRespBody
   @ApiOperation(value = "新增施工平台施工项目列表")
   public MyRespBundle<String> inserConstructionBaseDicList(@ApiParam("施工平台施工项目vo实体")  ConstructionBaseDic vo){

       boolean flag = baseDicService.insertConstructionBaseDic(vo);

       if(flag){
           return sendJsonData(ResultMessage.SUCCESS, "操作成功");
       }
       return sendJsonData(ResultMessage.FAIL, "操作失败");
   }




   /**
    *  新增 字典列表信息
    * @author lqd
    * @return json
    */
    @PostMapping("/updateConstructionBaseDicList")
   @MyRespBody
   @ApiOperation(value = "新增施工平台施工项目列表")
   public MyRespBundle<String> updateConstructionBaseDicList(@ApiParam("施工平台施工项目vo实体") @RequestBody ConstructionBaseDic vo){

       boolean flag = baseDicService.updateConstructionBaseDicName(vo);

       if(flag){
           return sendJsonData(ResultMessage.SUCCESS, "操作成功");
       }
       return sendJsonData(ResultMessage.FAIL, "操作失败");
   }





    /**
     * 根据适用范围获取可用角色
     * @param scope
     * @return
     */
    @GetMapping("/account/roles")
    @MyRespBody
    public MyRespBundle<List<SystemRole>> roles(Integer scope){
        List<SystemRole> permissions = systemRoleService.listRoleByScope(scope);
        return sendJsonData(ResultMessage.SUCCESS,permissions);
    }

    /**
     * 公司入驻状态
     * @return
     */
    @GetMapping("/join/status")
    @MyRespBody
    @ApiOperation(value = "公司入驻状态")
    public MyRespBundle<List<Map<String, String>>> status(){
        List<Map<String, String>> status = CompanyAuditStatus.map();
        return sendJsonData(ResultMessage.SUCCESS,status);
    }

}
