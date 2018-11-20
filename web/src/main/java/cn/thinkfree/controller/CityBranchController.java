package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.constants.OneTrue;
import cn.thinkfree.database.constants.UserEnabled;
import cn.thinkfree.database.model.City;
import cn.thinkfree.database.model.CityBranch;
import cn.thinkfree.database.utils.BeanValidator;
import cn.thinkfree.database.vo.CityBranchSEO;
import cn.thinkfree.database.vo.CityBranchVO;
import cn.thinkfree.database.vo.CityBranchWtihProCitVO;
import cn.thinkfree.database.vo.Severitys;
import cn.thinkfree.service.citybranch.CityBranchService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
 * @author jiangchunyu(后台)
 * @date 2018
 * @Description 城市分站（市分站）
 */
@RestController
@RequestMapping(value = "/cityBranch")
@Api(value = "前端使用---城市分站---蒋春雨",description = "前端使用---城市分站---蒋春雨")
public class CityBranchController extends AbsBaseController{

    @Autowired
    CityBranchService cityBranchService;

    /**
     * 创建城市分站
     */
    @PostMapping(value = "/saveCityBranch")
    @MyRespBody
    @ApiOperation(value="城市分站：创建分站")
    public MyRespBundle<String> saveCityBranch(@ApiParam("城市分站信息")  CityBranch cityBranch){
        BeanValidator.validate(cityBranch, Severitys.Insert.class);
        int line = cityBranchService.addCityBranch(cityBranch);
        if(line > 0){
            return sendJsonData(ResultMessage.SUCCESS, line);
        }
        return sendJsonData(ResultMessage.FAIL, line);
    }

    /**
     * 编辑城市分站信息
     */
    @PostMapping(value = "/updateCityBranch")
    @MyRespBody
    @ApiOperation(value="城市分站：编辑分站")
    public MyRespBundle<String> updateCityBranch(@ApiParam("城市分站信息") CityBranch cityBranch){
        BeanValidator.validate(cityBranch, Severitys.Update.class);
        int line = cityBranchService.updateCityBranch(cityBranch);
        if(line > 0){
            return sendJsonData(ResultMessage.SUCCESS, line);
        }
        return sendJsonData(ResultMessage.FAIL, line);
    }

    /**
     * 查询城市分站信息
     */
    @GetMapping(value = "/cityBranchlistByCompanyId")
    @MyRespBody
    @ApiOperation(value="城市分站：分站详情（根据分公司id进行分站联动查询）")
    public MyRespBundle<List<CityBranch>> cityBranchlistByCompanyId(@ApiParam("分公司编号")Integer id){

        List<CityBranch> cityBranchList = cityBranchService.cityBranchesByCompany(id);

        return sendJsonData(ResultMessage.SUCCESS, cityBranchList);
    }

    /**
     * 查询城市分站信息
     */
    @GetMapping(value = "/cityBranchlistByCompanyCode")
    @MyRespBody
    @ApiOperation(value="城市分站：分站详情（根据分公司编号进行分站联动查询）")
    public MyRespBundle<List<CityBranch>> cityBranchlistByCompanyCode(@ApiParam("分公司编号")@RequestParam String branchCompanyCode){

        int flag = 1;
        List<CityBranch> cityBranchList = cityBranchService.cityBranchesByCompanyCode(flag,branchCompanyCode);

        return sendJsonData(ResultMessage.SUCCESS, cityBranchList);
    }

    /**
     * 查询城市分站信息
     */
    @GetMapping(value = "/cityBranchlistByCompanyCodeSearch")
    @MyRespBody
    @ApiOperation(value="城市分站：分站详情（根据分公司编号进行分站联动查询）")
    public MyRespBundle<List<CityBranch>> cityBranchlistByCompanyCodeSearch(@ApiParam("分公司编号")@RequestParam String branchCompanyCode){

        int flag = 0;
        List<CityBranch> cityBranchList = cityBranchService.cityBranchesByCompanyCode(flag,branchCompanyCode);

        return sendJsonData(ResultMessage.SUCCESS, cityBranchList);
    }

    /**
     * 查询城市分站信息
     */
    @GetMapping(value = "/cityBranchlist")
    @MyRespBody
    @ApiOperation(value="城市分站：分站管理（市地区城市分站分页查询）")
    public MyRespBundle<PageInfo<CityBranchVO>> cityBranchlist(@ApiParam("查询城市分站参数")CityBranchSEO cityBranchSEO){

        PageInfo<CityBranchVO> pageInfo = cityBranchService.cityBranchList(cityBranchSEO);

        return sendJsonData(ResultMessage.SUCCESS, pageInfo);
    }
    /**
     * 查询城市分站信息
     */
    @GetMapping(value = "/cityBranchlistOfCompany")
    @MyRespBody
    @ApiOperation(value="城市分站管理：分站管理>分站详情>分站信息(分站省地区查看分站市地区详情（分页查询）)")
    public MyRespBundle<PageInfo<CityBranchWtihProCitVO>> cityBranchlistOfCompany(@ApiParam("查询城市分站参数")CityBranchSEO cityBranchSEO){

        PageInfo<CityBranchWtihProCitVO> pageInfo = cityBranchService.cityBranchWithProList(cityBranchSEO);
        return sendJsonData(ResultMessage.SUCCESS, pageInfo);
    }
    /**
     * 城市分站详情
     */
    @GetMapping(value = "/cityBranchDetails")
    @MyRespBody
    @ApiOperation(value="城市分站：查看分站")
    public MyRespBundle<CityBranchVO> cityBranchDetails(@ApiParam("城市分站id")@RequestParam(value = "id") Integer id){

        CityBranchVO cityBranchVO = cityBranchService.cityBranchDetails(id);
        return sendJsonData(ResultMessage.SUCCESS, cityBranchVO);
    }

    /**
     * 城市分站详情
     */
    @GetMapping(value = "/cityBranchById")
    @MyRespBody
    @ApiOperation(value="城市分站：编辑回写")
    public MyRespBundle<CityBranchVO> cityBranchById(@ApiParam("城市分站id")@RequestParam(value = "id") Integer id){

        CityBranchVO cityBranchVO = cityBranchService.cityBranchById(id);
        return sendJsonData(ResultMessage.SUCCESS, cityBranchVO);
    }

    /**
     * 城市分站
     */
    @PostMapping(value = "/cityBranchDelete")
    @MyRespBody
    @ApiOperation(value="城市分站：删除")
    public MyRespBundle<String> cityBranchDelete(@ApiParam("城市分站id")@RequestParam(value = "id") Integer id){

        CityBranch cityBranch = new CityBranch();
        cityBranch.setId(id);
        cityBranch.setIsDel(OneTrue.YesOrNo.YES.val.shortValue());
        int line = cityBranchService.updateCityBranchStatus(cityBranch);
        if(line > 0){
            return sendJsonData(ResultMessage.SUCCESS, "操作成功");
        }
        return sendJsonData(ResultMessage.FAIL, "操作失败");
    }

    /**
     * 城市分站
     */
    @PostMapping(value = "/cityBranchEnable")
    @MyRespBody
    @ApiOperation(value="城市分站：启用")
    public MyRespBundle<String> cityBranchEnable(@ApiParam("城市分站id")@RequestParam(value = "id") Integer id){

        CityBranch cityBranch = new CityBranch();
        cityBranch.setId(id);
        cityBranch.setIsEnable(UserEnabled.Enabled_true.shortVal().shortValue());
        int line = cityBranchService.updateCityBranchStatus(cityBranch);
        if(line > 0){
            return sendJsonData(ResultMessage.SUCCESS, "操作成功");
        }
        return sendJsonData(ResultMessage.FAIL, "操作失败");
    }

    /**
     * 城市分站
     */
    @PostMapping(value = "/cityBranchDisable")
    @MyRespBody
    @ApiOperation(value="城市分站：禁用")
    public MyRespBundle<String> cityBranchDisable(@ApiParam("城市分站id")@RequestParam(value = "id") Integer id){

        CityBranch cityBranch = new CityBranch();
        cityBranch.setId(id);
        cityBranch.setIsEnable(UserEnabled.Disable.shortVal());
        int line = cityBranchService.updateCityBranchStatus(cityBranch);
        if(line > 0){
            return sendJsonData(ResultMessage.SUCCESS, "操作成功");
        }
        return sendJsonData(ResultMessage.FAIL, "操作失败");
    }

    /**
     * 城市分站
     */
    @PostMapping(value = "/cityBranchRuZhuAdd")
    @MyRespBody
    @ApiOperation(value="城市分站：城市分站")
    public MyRespBundle<List<CityBranch>> cityBranchRuZhuAdd(@ApiParam("分公司编码")String branchCompanyCode, @ApiParam("城市code") Integer cityCode){

        return sendJsonData(ResultMessage.SUCCESS,cityBranchService.selectByProCit(branchCompanyCode,cityCode));
    }

    /**
     * 城市分站
     */
    @PostMapping(value = "/cityBranchRuZhu")
    @MyRespBody
    @ApiOperation(value="城市分站：城市分站")
    public MyRespBundle<List<CityBranch>> cityBranchRuZhu(@ApiParam("省份code")@RequestParam(value = "provinceCode") Integer provinceCode, @ApiParam("城市code")@RequestParam(value = "cityCode") Integer cityCode){

        return sendJsonData(ResultMessage.SUCCESS,cityBranchService.selectByProCitCode(provinceCode,cityCode));
    }

    /**
     * 城市分站
     */
    @GetMapping(value = "/cityBranchSearch")
    @MyRespBody
    @ApiOperation(value="城市分站：选择地区")
    public MyRespBundle<List<City>> cityBranchSearch(){

        return sendJsonData(ResultMessage.SUCCESS,cityBranchService.selectCity());
    }
}

