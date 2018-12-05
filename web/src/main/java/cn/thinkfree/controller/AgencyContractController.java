package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.annotation.MySysLog;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.core.constants.SysLogAction;
import cn.thinkfree.core.constants.SysLogModule;
import cn.thinkfree.core.security.filter.util.SessionUserDetailsUtil;
import cn.thinkfree.database.constants.UserEnabled;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.utils.BeanValidator;
import cn.thinkfree.database.vo.UserVO;
import cn.thinkfree.database.vo.account.*;
import cn.thinkfree.database.vo.agency.AgencySEO;
import cn.thinkfree.database.vo.agency.MyAgencyContract;
import cn.thinkfree.database.vo.agency.paramAgency;
import cn.thinkfree.service.account.*;
import cn.thinkfree.service.companyuser.CompanyUserService;
import cn.thinkfree.service.contract.AgencyService;
import cn.thinkfree.service.pcUser.PcUserInfoService;
import cn.thinkfree.service.user.UserService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 账号相关
 */
@Api(description="经销商合同")
@RestController
@RequestMapping("/agencyContract")
public class AgencyContractController extends AbsBaseController {

    @Autowired
    AgencyService agencyService;


    @ApiResponses({
            @ApiResponse(code = 200,message = "操作成功",response = AgencyContract.class)
    })
    @ApiOperation(value="前端-运营平台-经销商合同(运营合同列表)", notes="经销商合同列表")
    @PostMapping("/getPageList")
    @MyRespBody
    public MyRespBundle<PageInfo<MyAgencyContract>> getPageList(@ApiParam("搜索条件")  AgencySEO seo){

        PageInfo<MyAgencyContract> pageInfo = agencyService.pageContractBySEO( seo);

        return sendJsonData(ResultMessage.SUCCESS,pageInfo);
    }

    @ApiResponses({
            @ApiResponse(code = 200,message = "操作成功",response = AgencyContract.class)
    })
    @ApiOperation(value="前端-运营平台-经销商合同(财务合同列表)", notes="经销商合同列表")
    @PostMapping("/getFinancePageList")
    @MyRespBody
    public MyRespBundle<PageInfo<MyAgencyContract>> getFinancePageList(@ApiParam("搜索条件")  AgencySEO seo){

        PageInfo<MyAgencyContract> pageInfo = agencyService.pageContractBySEO( seo);

        return sendJsonData(ResultMessage.SUCCESS,pageInfo);
    }

    /**
     *  运营录入合同
     * @param seo
     * @return
     */
    @ApiResponses({
            @ApiResponse(code = 200,message = "操作成功",response = AgencyContract.class)
    })
    @ApiOperation(value="前端-运营平台-运营录入经销商合同", notes="新增经销商合同(新增的时候status 为0 )")
    @PostMapping("/insertContract")
    @MyRespBody
    public MyRespBundle<PageInfo<String>> insertContract(@ApiParam("搜索条件") @RequestBody List<paramAgency> paramAgencyList){

           boolean  flag = agencyService.insertContract( paramAgencyList);

        return sendJsonData(ResultMessage.SUCCESS,flag);
    }





}
