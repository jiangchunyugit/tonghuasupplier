package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.vo.CompanySubmitVo;
import cn.thinkfree.service.companysubmit.CompanySubmitService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ying007
 * @date 2018/09/14
 * 公司资质
 */
@RestController
@RequestMapping(value = "/companyAudit")
@Api(value = "公司入驻",description = "公司入驻")
public class CompanyInfoSubmitController extends AbsBaseController {
    @Autowired
    CompanySubmitService companySubmitService;

    @RequestMapping(value = "/upCompanyInfo", method = RequestMethod.POST)
    @MyRespBody
    @ApiOperation(value="审核资质上传")
    public MyRespBundle<String> upCompanyInfo(@ApiParam("公司资质信息")CompanySubmitVo companySubmitVo){
        boolean flag = companySubmitService.upCompanyInfo(companySubmitVo);
        if(flag){
            return sendJsonData(ResultMessage.SUCCESS, "操作成功");
        }
        return sendJsonData(ResultMessage.FAIL, "操作失败");
    }

    /**
     * 账号添加---->提交----->确认
     * @param companySubmitVo
     * @return
     */
    /*@RequestMapping(value = "/addCompanyInfo", method = RequestMethod.POST)
    @MyRespBody
    @ApiOperation(value="账号添加(创建公司信息)---->提交----->确认")
    public MyRespBundle<String> addCompanyInfo(@ApiParam("公司资质信息")CompanySubmitVo companySubmitVo){
        boolean flag = companySubmitService.addCompanyInfo(companySubmitVo);
        if(flag){
            return sendJsonData(ResultMessage.SUCCESS, "操作成功");
        }
        return sendJsonData(ResultMessage.FAIL, "操作失败");
    }*/

    //运营人员审批

    //完成签约付费
}
