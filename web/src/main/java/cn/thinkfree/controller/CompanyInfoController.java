package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.model.CompanyInfo;
import cn.thinkfree.database.model.SystemMessage;
import cn.thinkfree.database.utils.BeanValidator;
import cn.thinkfree.database.vo.*;
import cn.thinkfree.service.company.CompanyInfoService;
import cn.thinkfree.service.project.ProjectService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import cn.thinkfree.database.model.CompanyUserSet;

@RestController
@RequestMapping(value = "/company")
@Api(value = "子公司管理",description = "子公司管理")
public class CompanyInfoController extends AbsBaseController{

    @Autowired
    CompanyInfoService companyInfoService;

    @Autowired
    ProjectService projectService;

    /**
     * 新增公司
     */
    @RequestMapping(value = "/saveCompanyInfo", method = RequestMethod.POST)
    @MyRespBody
    @ApiOperation(value="子公司管理：新增")
    public MyRespBundle<String> saveCompanyInfo(@ApiParam("公司信息") CompanyInfo companyInfo){
        BeanValidator.validate(companyInfo, Severitys.Insert.class);

        int line = companyInfoService.addCompanyInfo(companyInfo);
        if(line > 0){
            return sendJsonData(ResultMessage.SUCCESS, line);
        }
        return sendJsonData(ResultMessage.FAIL, line);
    }
    /**
     * 编辑公司信息
     */
    @RequestMapping(value = "/updateCompanyInfo", method = RequestMethod.POST)
    @MyRespBody
    @ApiOperation(value="子公司管理：编辑")
    public MyRespBundle<String> updateCompanyInfo(@ApiParam("公司信息")CompanyInfo companyInfo){
        BeanValidator.validate(companyInfo, Severitys.Update.class);
        int line = companyInfoService.updateCompanyInfo(companyInfo);
        if(line > 0){
            return sendJsonData(ResultMessage.SUCCESS, line);
        }
        return sendJsonData(ResultMessage.FAIL, line);
    }

    /**
     * 查询公司信息
     */

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @MyRespBody
    @ApiOperation(value="查询子公司信息")
    public MyRespBundle<PageInfo<CompanyInfo>> list(@ApiParam("查询公司参数")CompanyInfoSEO companyInfoSEO){
        BeanValidator.validate(companyInfoSEO);
        PageInfo<CompanyInfo> pageInfo = companyInfoService.list(companyInfoSEO);

        return sendJsonData(ResultMessage.SUCCESS, pageInfo);
    }
    /**
     * 子公司管理：项目信息 项目情况
     */
    @RequestMapping(value = "/findProjectById", method = RequestMethod.GET)
    @MyRespBody
    @ApiOperation(value="子公司管理：项目信息--->项目情况")
    public MyRespBundle<IndexProjectReportVO> findProjectById(@RequestParam(value = "companyId") String companyId){
        List<String> companyRelationMap = new ArrayList<>();
        companyRelationMap.add(companyId);
        IndexProjectReportVO indexProjectReportVO = projectService.countProjectReportVO(companyRelationMap);

        return sendJsonData(ResultMessage.SUCCESS, indexProjectReportVO);
    }
    /**
     * 子公司管理：项目信息---->项目详情
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "page", value = "当前页", required = true, dataType = "int"),
            @ApiImplicitParam(paramType="query", name = "rows", value = "行", required = true, dataType = "int"),
            @ApiImplicitParam(paramType="query", name = "companyId", value = "公司id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "status", value = "状态", required = false, dataType = "int")
    })
    @RequestMapping(value = "/details", method = RequestMethod.GET)
    @MyRespBody
    @ApiOperation(value="子公司管理：项目信息---->项目详情")
    public MyRespBundle<PageInfo<ProjectVO>> details(@RequestParam(value = "companyId") String companyId, @RequestParam(value = "status",required = false) Integer status,
                                                     @RequestParam(value = "rows") Integer rows, @RequestParam(value = "page") Integer page){

        PageInfo<ProjectVO> projectvo = projectService.selectProjectVOForCompany(companyId,status,rows,page);

        return sendJsonData(ResultMessage.SUCCESS, projectvo);
    }
    /**
     *
     * 子公司：员工信息
     * @return
     */
    @RequestMapping(value = "/staffMseeage",method = RequestMethod.GET)
    @MyRespBody
    @ApiOperation(value="子公司：员工信息")
    public MyRespBundle<PageInfo<StaffsVO>> staffMessage(
            @RequestParam("company_id")String company_id,@RequestParam("page")Integer page,@RequestParam("rows")Integer rows) {

        PageInfo<StaffsVO> companyUserSet = companyInfoService.staffMessage(company_id,page,rows);
        return sendJsonData(ResultMessage.SUCCESS,companyUserSet);
    }

    /**
     * 公司详情
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "companyId", value = "公司id", required = true, dataType = "String"),
    })
    @RequestMapping(value = "/companyDetails", method = RequestMethod.GET)
    @MyRespBody
    @ApiOperation(value="子公司管理：公司详情")
    public MyRespBundle<PageInfo<CompanyInfoVo>> companyDetails(@RequestParam(value = "companyId") String companyId){

        CompanyInfoVo companyInfo = companyInfoService.companyDetails(companyId);

        return sendJsonData(ResultMessage.SUCCESS, companyInfo);
    }
}

