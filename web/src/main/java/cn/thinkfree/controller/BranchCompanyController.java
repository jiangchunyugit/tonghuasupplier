package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.annotation.MySysLog;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.core.constants.SysLogAction;
import cn.thinkfree.core.constants.SysLogModule;
import cn.thinkfree.database.constants.OneTrue;
import cn.thinkfree.database.constants.UserEnabled;
import cn.thinkfree.database.model.BranchCompany;
import cn.thinkfree.database.model.HrOrganizationEntity;
import cn.thinkfree.database.utils.BeanValidator;
import cn.thinkfree.database.vo.*;
import cn.thinkfree.service.branchcompany.BranchCompanyService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author jiangchunyu(后台)
 * @date 2018
 * @Description 分公司（省分站）
 */
@RestController
@RequestMapping(value = "/branchCompany")
@Api(value = "前端使用---分公司站点---蒋春雨",description = "前端使用---分公司站点---蒋春雨")
public class BranchCompanyController extends AbsBaseController{

    @Autowired
    BranchCompanyService branchCompanyService;

    /**
     * 查询分公司信息
     */
    @RequestMapping(value = "/ebsBranchCompanylist", method = RequestMethod.GET)
    @MyRespBody
    @ApiOperation(value="分公司站点：埃森哲分公司")
    public MyRespBundle<List<HrOrganizationEntity>> ebsBranchCompanylist(){

        return sendJsonData(ResultMessage.SUCCESS, branchCompanyService.ebsBranchCompanylist());
    }

    /**
     * 创建分公司
     */
    @RequestMapping(value = "/saveBranchCompany", method = RequestMethod.POST)
    @MyRespBody
    @ApiOperation(value="分公司站点：创建分站")
    @MySysLog(action = SysLogAction.SAVE,module = SysLogModule.PC_PROJECT,desc = "添加省分站")
    public MyRespBundle<String> saveBranchCompany(@ApiParam("分公司信息") BranchCompanyVO branchCompanyVO){
        BeanValidator.validate(branchCompanyVO,Severitys.Insert.class);
        if (branchCompanyService.checkRepeat(branchCompanyVO)) {
            return sendFailMessage("站点名称已存在");
        }
        if(branchCompanyService.addBranchCompany(branchCompanyVO)){
            return sendJsonData(ResultMessage.SUCCESS, "操作成功");
        }
        return sendJsonData(ResultMessage.FAIL, "操作失败");
    }

    /**
     * 编辑分公司信息
     */
    @RequestMapping(value = "/updateBranchCompany", method = RequestMethod.POST)
    @MyRespBody
    @ApiOperation(value="分公司站点：编辑分站(id不可为空)")
    @MySysLog(action = SysLogAction.EDIT,module = SysLogModule.PC_PROJECT,desc = "编辑省分站")
    public MyRespBundle<String> updateBranchCompany(@ApiParam("分公司信息")BranchCompanyVO branchCompanyVO){
        BeanValidator.validate(branchCompanyVO, Severitys.Update.class);
        if (branchCompanyService.checkRepeat(branchCompanyVO)) {
            return sendFailMessage("站点名称已存在");
        }
        if(branchCompanyService.updateBranchCompany(branchCompanyVO)){
            return sendJsonData(ResultMessage.SUCCESS, "操作成功");
        }
        return sendJsonData(ResultMessage.FAIL, "操作失败");
    }

    /**
     * 查询分公司信息
     */
    @RequestMapping(value = "/branchCompanylist", method = RequestMethod.GET)
    @MyRespBody
    @ApiOperation(value="分公司站点：分站管理（省分站分公司分页查询）")
    public MyRespBundle<PageInfo<BranchCompanyVO>> branchCompanylist(@ApiParam("查询分公司参数")BranchCompanySEO branchCompanySEO){

        PageInfo<BranchCompanyVO> pageInfo = branchCompanyService.branchCompanyList(branchCompanySEO);

        return sendJsonData(ResultMessage.SUCCESS, pageInfo);
    }

    /**
     * 分公司详情
     */
    @RequestMapping(value = "/branchCompanyDetails", method = RequestMethod.GET)
    @MyRespBody
    @ApiOperation(value="分公司站点：查看分站||编辑回写")
    public MyRespBundle<BranchCompanyVO> branchCompanyDetails(@ApiParam("分公司id")@RequestParam(value = "id") Integer id){

        BranchCompanyVO branchCompanyVO = branchCompanyService.branchCompanyDetails(id);
        return sendJsonData(ResultMessage.SUCCESS, branchCompanyVO);
    }

    /**
     * 分公司
     */
    @RequestMapping(value = "/branchCompanys", method = RequestMethod.GET)
    @MyRespBody
    @ApiOperation(value="分公司站点：分站名称||选择省分站名称")
    public MyRespBundle<List<BranchCompany>> branchCompanys(){

        int flag = 1;
        List<BranchCompany> branchCompanies = branchCompanyService.branchCompanies(flag);

        return sendJsonData(ResultMessage.SUCCESS, branchCompanies);
    }

    /**
     * 分公司
     */
    @RequestMapping(value = "/branchCompanySearch", method = RequestMethod.GET)
    @MyRespBody
    @ApiOperation(value="分公司站点：查询条件")
    public MyRespBundle<List<BranchCompany>> branchCompanySearch(){

        int flag = 0;
        List<BranchCompany> branchCompanies = branchCompanyService.branchCompanies(flag);

        return sendJsonData(ResultMessage.SUCCESS, branchCompanies);
    }

    /**
     * 分公司
     */
    @RequestMapping(value = "/citybranchCompany", method = RequestMethod.GET)
    @MyRespBody
    @ApiOperation(value="分公司站点：城市分站创建分公司")
    public MyRespBundle<List<BranchCompanyVO>> citybranchCompany(){

        List<BranchCompanyVO> branchCompanies = branchCompanyService.addCitybranchCompany();

        return sendJsonData(ResultMessage.SUCCESS, branchCompanies);
    }

    /**
     * 分公司
     */
    @RequestMapping(value = "/companyRelations", method = RequestMethod.GET)
    @MyRespBody
    @ApiOperation(value="分公司站点：分公司全部信息和所属期城市分站信息")
    public MyRespBundle<List<CompanyRelationVO>> companyRelations(){

        List<CompanyRelationVO> companyRelationVOList = branchCompanyService.companyRelationList();

        return sendJsonData(ResultMessage.SUCCESS, companyRelationVOList);
    }

    /**
     * 分公司
     */
    @PostMapping(value = "/branchCompanyDelete")
    @MyRespBody
    @ApiOperation(value="分公司站点：删除")
    @MySysLog(action = SysLogAction.DEL,module = SysLogModule.PC_PROJECT,desc = "删除省分站")
    public MyRespBundle<String> branchCompanyDelete(@ApiParam("分公司id")@RequestParam(value = "id") Integer id){

        BranchCompany branchCompany = new BranchCompany();
        branchCompany.setId(id);
        branchCompany.setIsDel(OneTrue.YesOrNo.YES.val.shortValue());
        if(branchCompanyService.enableBranchCompany(branchCompany)){
            return sendJsonData(ResultMessage.SUCCESS, "操作成功");
        }
        return sendJsonData(ResultMessage.FAIL, "操作失败");
    }

    /**
     * 分公司
     */
    @RequestMapping(value = "/branchCompanyEnable", method = RequestMethod.POST)
    @MyRespBody
    @ApiOperation(value="分公司站点：启用")
    @MySysLog(action = SysLogAction.CHANGE_STATE,module = SysLogModule.PC_PROJECT,desc = "启用省分站")
    public MyRespBundle<String> branchCompanyEnable(@ApiParam("分公司id")@RequestParam(value = "id") Integer id){

        BranchCompany branchCompany = new BranchCompany();
        branchCompany.setId(id);
        branchCompany.setIsEnable(UserEnabled.Enabled_true.shortVal());
        if(branchCompanyService.enableBranchCompany(branchCompany)){
            return sendJsonData(ResultMessage.SUCCESS, "操作成功");
        }
        return sendJsonData(ResultMessage.FAIL, "操作失败");
    }

    /**
     * 分公司
     */
    @RequestMapping(value = "/branchCompanyDisable", method = RequestMethod.POST)
    @MyRespBody
    @ApiOperation(value="分公司站点：禁用")
    @MySysLog(action = SysLogAction.CHANGE_STATE,module = SysLogModule.PC_PROJECT,desc = "禁用省分站")
    public MyRespBundle<PageInfo<String>> branchCompanyDisable(@ApiParam("分公司id")@RequestParam(value = "id") Integer id){

        BranchCompany branchCompany = new BranchCompany();
        branchCompany.setId(id);
        branchCompany.setIsEnable(UserEnabled.Disable.shortVal());
        if(branchCompanyService.enableBranchCompany(branchCompany)){
            return sendJsonData(ResultMessage.SUCCESS, "操作成功");
        }
        return sendJsonData(ResultMessage.FAIL, "操作失败");
    }

    @GetMapping(value = "/siteInfo")
    @MyRespBody
    @ApiOperation(value = "(权限)运营平台---站点信息")
    public MyRespBundle<SiteInfo> siteInfo() {

        return sendJsonData(ResultMessage.SUCCESS, branchCompanyService.getSiteInfo());
    }

    @GetMapping(value = "/branchCompanyByIdList")
    @MyRespBody
    @ApiOperation(value = "(数据权限)运营平台---站点信息")
    public MyRespBundle<List<BranchCompany>> branchCompanyByIdList() {

        return sendJsonData(ResultMessage.SUCCESS, branchCompanyService.getBranchCompanyByIdList());
    }
}

