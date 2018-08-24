package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.model.CompanyInfo;
import cn.thinkfree.database.model.SystemMessage;
import cn.thinkfree.database.vo.CompanyInfoSEO;
import cn.thinkfree.database.vo.IndexProjectReportVO;
import cn.thinkfree.database.vo.ProjectVO;
import cn.thinkfree.service.company.CompanyInfoService;
import cn.thinkfree.service.project.ProjectService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/company")
@Api("子公司管理")
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
//    @ApiImplicitParams({
//            @ApiImplicitParam(paramType="query", name = "provinceCode", value = "省", required = true, dataType = "int"),
//            @ApiImplicitParam(paramType="query", name = "cityCode", value = "市", required = true, dataType = "int"),
//            @ApiImplicitParam(paramType="query", name = "areaCode", value = "县", required = true, dataType = "int"),
//            @ApiImplicitParam(paramType="query", name = "phone", value = "地图选点", required = true, dataType = "String"),
//            @ApiImplicitParam(paramType="query", name = "address", value = "详细地址", required = true, dataType = "String"),
//            @ApiImplicitParam(paramType="query", name = "companyName", value = "分公司名称", required = true, dataType = "String"),
//            @ApiImplicitParam(paramType="query", name = "telephone", value = "联系方式", required = true, dataType = "String"),
//            @ApiImplicitParam(paramType="query", name = "legalName", value = "负责人姓名", required = true, dataType = "String"),
//            @ApiImplicitParam(paramType="query", name = "legalPhone", value = "负责人手机号", required = true, dataType = "String"),
//            @ApiImplicitParam(paramType="query", name = "regPhone", value = "备注", required = false, dataType = "String")
//    })
    public MyRespBundle<String> saveCompanyInfo(@ApiParam("公司信息") CompanyInfo companyInfo){

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
//    @ApiImplicitParams({
//            @ApiImplicitParam(paramType="query", name = "provinceCode", value = "省", required = true, dataType = "int"),
//            @ApiImplicitParam(paramType="query", name = "cityCode", value = "市", required = true, dataType = "int"),
//            @ApiImplicitParam(paramType="query", name = "name", value = "县", required = true, dataType = "int"),
//            @ApiImplicitParam(paramType="query", name = "phone", value = "地图选点", required = true, dataType = "String"),
//            @ApiImplicitParam(paramType="query", name = "address", value = "详细地址", required = true, dataType = "String"),
//            @ApiImplicitParam(paramType="query", name = "companyName", value = "分公司名称", required = true, dataType = "String"),
//            @ApiImplicitParam(paramType="query", name = "telephone", value = "联系方式", required = true, dataType = "String"),
//            @ApiImplicitParam(paramType="query", name = "legalName", value = "负责人姓名", required = true, dataType = "String"),
//            @ApiImplicitParam(paramType="query", name = "legalPhone", value = "负责人手机号", required = true, dataType = "String"),
//            @ApiImplicitParam(paramType="query", name = "regPhone", value = "备注", required = false, dataType = "String")
//    })
    public MyRespBundle<String> updateCompanyInfo(@ApiParam("公司信息")CompanyInfo companyInfo){

        int line = companyInfoService.updateCompanyInfo(companyInfo);
        if(line > 0){
            return sendJsonData(ResultMessage.SUCCESS, line);
        }
        return sendJsonData(ResultMessage.FAIL, line);
    }

    /**
     * 查询公司信息
     */

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @MyRespBody
    @ApiOperation(value="查询子公司信息")
    public MyRespBundle<PageInfo<CompanyInfo>> list(@ApiParam("查询公司参数")CompanyInfoSEO companyInfoSEO){

        PageInfo<CompanyInfo> pageInfo = companyInfoService.list(companyInfoSEO);

        return sendJsonData(ResultMessage.SUCCESS, pageInfo);
    }
    /**
     * 子公司管理：项目信息 项目情况
     */
    @RequestMapping(value = "/findProjectById", method = RequestMethod.POST)
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
    public MyRespBundle<PageInfo<ProjectVO>> details(@RequestParam(value = "companyID") String companyId, @RequestParam(value = "status",required = false) Integer status,
                                                     @RequestParam(value = "rows") Integer rows, @RequestParam(value = "page") Integer page){

        PageInfo<ProjectVO> projectvo = projectService.selectProjectVOForCompany(companyId,status,rows,page);

        return sendJsonData(ResultMessage.SUCCESS, projectvo);
    }


}
