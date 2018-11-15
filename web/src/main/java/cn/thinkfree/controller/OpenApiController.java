package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.model.CompanyInfo;
import cn.thinkfree.database.model.SystemMessage;
import cn.thinkfree.database.vo.ProjectQuotationVO;
import cn.thinkfree.database.vo.SelectItem;
import cn.thinkfree.service.cache.RedisService;
import cn.thinkfree.service.company.CompanyInfoService;
import cn.thinkfree.service.project.ProjectService;
import cn.thinkfree.service.sysMsg.SystemMessageService;
import cn.thinkfree.service.user.UserService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
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
    UserService userService;

    @Autowired
    RedisService redisService;

    @ApiOperation(value = "APP模糊查询公司列表",notes = "默认30条数据")
    @PostMapping("/companyInfo")
    @MyRespBody
    public MyRespBundle<List<SelectItem>> companyInfo(String name){

        List<SelectItem> items = companyInfoService.listCompanyByLikeName(name);

        return sendJsonData(ResultMessage.SUCCESS,items);
    }


    /**
     * 忘记密码
     * @param email
     * @return
     */
    @ApiOperation(value = "忘记密码1",notes = "忘记密码第一步,验证邮箱")
    @PostMapping("/forget")
    @MyRespBody
    public MyRespBundle<String> forgetPwd(String email){
        String mes = userService.forgetPwd(email);
        return sendSuccessMessage(mes);
    }


    /**
     * 忘记密码 重置密码
     * @param email
     * @param pwd
     * @param code
     * @return
     */
    @ApiOperation(value = "重置密码",notes = "忘记密码第二部,重置密码")
    @MyRespBody
    @PostMapping("/forget/reset")
    public MyRespBundle<String> resetPwd(String email,String pwd,String code){
        if(StringUtils.isBlank(email) || StringUtils.isBlank(pwd)|| StringUtils.isBlank(code)){
            return sendFailMessage("参数不对");
        }
        String mes = userService.updatePassWordOnForget(email,pwd,code);
        return sendSuccessMessage(mes);
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

}
