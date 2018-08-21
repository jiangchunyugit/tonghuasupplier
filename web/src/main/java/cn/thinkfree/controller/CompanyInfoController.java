package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.model.CompanyInfo;
import cn.thinkfree.database.model.SystemMessage;
import cn.thinkfree.service.company.CompanyInfoService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/company")
@ApiOperation(value="子公司管理")
public class CompanyInfoController extends AbsBaseController{

    @Autowired
    CompanyInfoService companyInfoService;

    /**
     * 新增公司
     */
    @RequestMapping(value = "/saveCompanyInfo", method = RequestMethod.POST)
    @MyRespBody
    @ApiOperation(value="子公司管理：新增")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "provinceCode", value = "省", required = true, dataType = "int"),
            @ApiImplicitParam(paramType="query", name = "cityCode", value = "市", required = true, dataType = "int"),
            @ApiImplicitParam(paramType="query", name = "areaCode", value = "县", required = true, dataType = "int"),
            @ApiImplicitParam(paramType="query", name = "phone", value = "地图选点", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "address", value = "详细地址", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "companyName", value = "分公司名称", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "telephone", value = "联系方式", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "legalName", value = "负责人姓名", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "legalPhone", value = "负责人手机号", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "regPhone", value = "备注", required = false, dataType = "String")
    })
    public MyRespBundle<String> saveCompanyInfo(CompanyInfo companyInfo){

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
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "provinceCode", value = "省", required = true, dataType = "int"),
            @ApiImplicitParam(paramType="query", name = "cityCode", value = "市", required = true, dataType = "int"),
            @ApiImplicitParam(paramType="query", name = "name", value = "县", required = true, dataType = "int"),
            @ApiImplicitParam(paramType="query", name = "phone", value = "地图选点", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "address", value = "详细地址", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "companyName", value = "分公司名称", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "telephone", value = "联系方式", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "legalName", value = "负责人姓名", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "legalPhone", value = "负责人手机号", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "regPhone", value = "备注", required = false, dataType = "String")
    })
    public MyRespBundle<String> updateCompanyInfo(CompanyInfo companyInfo){

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
    @ApiOperation(value="子公司查询")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "page", value = "当前页码", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType="query", name = "rows", value = "每页展示条数", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType="query", name = "sendUserId", value = "发送人id", required = false, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "sendTime", value = "日期", required = false, dataType = "String")
    })
    public MyRespBundle<PageInfo<SystemMessage>> list(CompanyInfo companyInfo){

        PageInfo<CompanyInfo> pageInfo = companyInfoService.list(companyInfo);

        return sendJsonData(ResultMessage.SUCCESS, pageInfo);
    }
}
