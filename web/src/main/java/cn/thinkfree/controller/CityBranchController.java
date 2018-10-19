package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.constants.OneTrue;
import cn.thinkfree.database.constants.UserEnabled;
import cn.thinkfree.database.model.CityBranch;
import cn.thinkfree.database.model.CompanyInfo;
import cn.thinkfree.database.utils.BeanValidator;
import cn.thinkfree.database.vo.CityBranchSEO;
import cn.thinkfree.database.vo.CityBranchVO;
import cn.thinkfree.database.vo.CompanyInfoVo;
import cn.thinkfree.database.vo.Severitys;
import cn.thinkfree.service.citybranch.CityBranchService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/cityBranch")
@Api(value = "城市分站",description = "城市分站")
public class CityBranchController extends AbsBaseController{

    @Autowired
    CityBranchService cityBranchService;

    /**
     * 创建城市分站
     */
    @RequestMapping(value = "/saveCityBranch", method = RequestMethod.POST)
    @MyRespBody
    @ApiOperation(value="城市分站管理：新增")
    public MyRespBundle<String> saveCityBranch(@ApiParam("城市分站信息") CityBranchVO cityBranchVO){
        BeanValidator.validate(cityBranchVO, Severitys.Insert.class);
        int line = cityBranchService.addCityBranch(cityBranchVO);
        if(line > 0){
            return sendJsonData(ResultMessage.SUCCESS, line);
        }
        return sendJsonData(ResultMessage.FAIL, line);
    }

    /**
     * 编辑城市分站信息
     */
    @RequestMapping(value = "/updateCityBranch", method = RequestMethod.POST)
    @MyRespBody
    @ApiOperation(value="城市分站管理：编辑")
    public MyRespBundle<String> updateCityBranch(@ApiParam("城市分站信息")CityBranchVO cityBranchVO){
        BeanValidator.validate(cityBranchVO, Severitys.Update.class);
        int line = cityBranchService.updateCityBranch(cityBranchVO);
        if(line > 0){
            return sendJsonData(ResultMessage.SUCCESS, line);
        }
        return sendJsonData(ResultMessage.FAIL, line);
    }

    /**
     * 查询城市分站信息
     */
    @RequestMapping(value = "/cityBranchlist", method = RequestMethod.GET)
    @MyRespBody
    @ApiOperation(value="城市分站管理：城市分站分页查询")
    public MyRespBundle<PageInfo<CompanyInfo>> cityBranchlist(@ApiParam("查询城市分站参数")CityBranchSEO cityBranchSEO){

        PageInfo<CityBranch> pageInfo = cityBranchService.cityBranchList(cityBranchSEO);

        return sendJsonData(ResultMessage.SUCCESS, pageInfo);
    }

    /**
     * 城市分站详情
     */
    @RequestMapping(value = "/cityBranchDetails", method = RequestMethod.GET)
    @MyRespBody
    @ApiOperation(value="城市分站管理：查看")
    public MyRespBundle<CityBranchVO> cityBranchDetails(@ApiParam("城市分站id")@RequestParam(value = "cityBranchId") Integer cityBranchId){

        if(cityBranchId ==null) {
            return sendJsonData(ResultMessage.FAIL, cityBranchId);
        }
        CityBranchVO cityBranchVO = cityBranchService.cityBranchDetails(cityBranchId);
        return sendJsonData(ResultMessage.SUCCESS, cityBranchVO);
    }

    /**
     * 城市分站
     */
    @RequestMapping(value = "/cityBranchDelete", method = RequestMethod.DELETE)
    @MyRespBody
    @ApiOperation(value="城市分站管理：删除")
    public MyRespBundle<PageInfo<CompanyInfoVo>> cityBranchDelete(@ApiParam("城市分站id")@RequestParam(value = "cityBranchId") Integer cityBranchId){

        if(cityBranchId ==null) {
            return sendJsonData(ResultMessage.FAIL,cityBranchId);
        }
        BeanValidator.validate(cityBranchId, Severitys.Update.class);
        CityBranch cityBranch = new CityBranch();
        cityBranch.setId(cityBranchId);
        cityBranch.setIsDel(OneTrue.YesOrNo.YES.val.shortValue());
        int line = cityBranchService.updateCityBranchStatus(cityBranch);
        if(line > 0){
            return sendJsonData(ResultMessage.SUCCESS, line);
        }
        return sendJsonData(ResultMessage.FAIL, line);
    }

    /**
     * 城市分站
     */
    @RequestMapping(value = "/cityBranchEnable", method = RequestMethod.POST)
    @MyRespBody
    @ApiOperation(value="城市分站管理：启用")
    public MyRespBundle<PageInfo<CompanyInfoVo>> cityBranchEnable(@ApiParam("城市分站id")@RequestParam(value = "cityBranchId") Integer cityBranchId){

        if(cityBranchId ==null) {
            return sendJsonData(ResultMessage.FAIL,cityBranchId);
        }
        BeanValidator.validate(cityBranchId, Severitys.Update.class);
        CityBranch cityBranch = new CityBranch();
        cityBranch.setId(cityBranchId);
        cityBranch.setIsEnable(UserEnabled.Enabled_false.shortVal().shortValue());
        int line = cityBranchService.updateCityBranchStatus(cityBranch);
        if(line > 0){
            return sendJsonData(ResultMessage.SUCCESS, line);
        }
        return sendJsonData(ResultMessage.FAIL, line);
    }

    /**
     * 城市分站
     */
    @RequestMapping(value = "/cityBranchDelete", method = RequestMethod.POST)
    @MyRespBody
    @ApiOperation(value="城市分站管理：禁用")
    public MyRespBundle<PageInfo<CompanyInfoVo>> cityBranchDisable(@ApiParam("城市分站id")@RequestParam(value = "cityBranchId") Integer cityBranchId){

        if(cityBranchId ==null) {
            return sendJsonData(ResultMessage.FAIL,cityBranchId);
        }
        BeanValidator.validate(cityBranchId, Severitys.Update.class);
        CityBranch cityBranch = new CityBranch();
        cityBranch.setId(cityBranchId);
        cityBranch.setIsDel(UserEnabled.Disable.shortVal());
        int line = cityBranchService.updateCityBranchStatus(cityBranch);
        if(line > 0){
            return sendJsonData(ResultMessage.SUCCESS, line);
        }
        return sendJsonData(ResultMessage.FAIL, line);
    }
}

