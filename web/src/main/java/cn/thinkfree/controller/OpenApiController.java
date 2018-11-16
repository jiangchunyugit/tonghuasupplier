package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.model.CompanyInfo;
import cn.thinkfree.database.model.SystemMessage;
import cn.thinkfree.database.vo.EnterCompanyOrganizationVO;
import cn.thinkfree.database.vo.ProjectQuotationVO;
import cn.thinkfree.database.vo.SelectItem;
import cn.thinkfree.service.branchcompany.BranchCompanyService;
import cn.thinkfree.service.company.CompanyInfoService;
import cn.thinkfree.service.project.ProjectService;
import cn.thinkfree.service.sysMsg.SystemMessageService;
import com.github.pagehelper.PageInfo;
import com.sun.tools.javac.comp.Enter;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 不需要token的部分
 */
@ApiOperation("手机端或Pc端 不需要token的接口")
@RestController
@RequestMapping("/open/v1")
public class OpenApiController extends AbsBaseController {

    @Autowired
    ProjectService projectService;

    @Autowired
    SystemMessageService systemMessageService;

    @Autowired
    CompanyInfoService companyInfoService;

    @Autowired
    BranchCompanyService branchCompanyService;

    @ApiOperation(value = "APP模糊查询公司列表",notes = "默认30条数据")
    @PostMapping("/companyInfo")
    @MyRespBody
    public MyRespBundle<List<SelectItem>> companyInfo(String name){

        List<SelectItem> items = companyInfoService.listCompanyByLikeName(name);

        return sendJsonData(ResultMessage.SUCCESS,items);
    }


    /**
     * 报价单
     * @param projectNo
     * @return
     */
    @GetMapping("/quotation")
    @MyRespBody
    public MyRespBundle<ProjectQuotationVO> quotation(@RequestParam String projectNo){
        ProjectQuotationVO projectQuotationVO = projectService.selectProjectQuotationVoByProjectNo(projectNo);
        return sendJsonData(ResultMessage.SUCCESS,projectQuotationVO);
    }

    @RequestMapping(value = "/findById", method = RequestMethod.GET)
    @MyRespBody
    public MyRespBundle<PageInfo<SystemMessage>> findById(@RequestParam(value = "id")Integer id){
        SystemMessage sysMsg = systemMessageService.selectByPrimaryKey(id);
        if(null == sysMsg){
            return sendJsonData(ResultMessage.FAIL, "失败");
        }
        return sendJsonData(ResultMessage.SUCCESS, sysMsg);
    }

    /**
     * 通过公司编号获取运营平台组织架构
     * @param companyId
     * @return
     */
    @GetMapping("/getCompanyOrganizationByCompanyId")
    @ApiOperation(value = "for徐洋---通过公司编号获取运营平台组织架构---蒋春雨",notes = "通过公司编号获取运营平台组织架构")
    @MyRespBody
    public MyRespBundle<EnterCompanyOrganizationVO> getCompanyOrganizationByCompanyId(@ApiParam("入驻公司id")@RequestParam String companyId){
        EnterCompanyOrganizationVO enterCompanyOrganizationVO = branchCompanyService.getCompanyOrganizationByCompanyId(companyId);
        return sendJsonData(ResultMessage.SUCCESS,enterCompanyOrganizationVO);
    }

    /**
     * 通过用户id获取运营平台组织架构
     * @param userId
     * @return
     */
    @GetMapping("/getCompanyInfoByUserId")
    @ApiOperation(value = "for徐洋---通过用户id获取运营平台组织架构---蒋春雨",notes = "通过用户id获取运营平台组织架构")
    @MyRespBody
    public MyRespBundle<List<CompanyInfo>> getCompanyInfoByUserId(@ApiParam("userId")@RequestParam String userId){
        List<CompanyInfo> companyInfos = branchCompanyService.getCompanyOrganizationByUser(userId);
        return sendJsonData(ResultMessage.SUCCESS,companyInfos);
    }

}
