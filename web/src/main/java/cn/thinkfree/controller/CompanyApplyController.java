package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.model.PcApplyInfo;
import cn.thinkfree.database.model.UserRegister;
import cn.thinkfree.database.vo.CompanyApplySEO;
import cn.thinkfree.database.vo.PcApplyInfoSEO;
import cn.thinkfree.database.vo.PcApplyInfoVo;
import cn.thinkfree.service.companyapply.CompanyApplyService;
import cn.thinkfree.service.pcUser.PcUserInfoService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author ying007
 * 公司申请事项接口：入驻申请，资质变更。。。
 *
 * -------根据申请类型applyType------判断是否显示办理和删除按钮---------
 */
@RestController
@RequestMapping(value = "/companyApply")
@Api(value = "公司申请",description = "公司申请")
public class CompanyApplyController extends AbsBaseController {

    @Autowired
    CompanyApplyService companyApplyService;



    /**
     * 发送邮件验证码
     * @param email
     * @return
     */
    @RequestMapping(value = "/sendMessage", method = RequestMethod.GET)
    @MyRespBody
    @ApiOperation(value="前端--设计/装饰公司申请--发送邮件验证码--李阳")
    public MyRespBundle<String> sendMessage(@ApiParam("邮箱") @RequestParam String email){
        String mes = companyApplyService.sendMessage(email);
        return sendSuccessMessage(mes);
    }

    /**
     * 公司申请事项
     * @param pcApplyInfoSEO
     * @return
     */
    @RequestMapping(value = "/applyThink", method = RequestMethod.POST)
    @MyRespBody
    @ApiOperation(value="前端--设计/装饰公司管理中心--设计/装饰公司入驻--入驻申请--注：添加公司申请事项-->0：入驻，1：资质变更 2:续约--李阳")
    public MyRespBundle<String> applyThink(@ApiParam("申请信息")PcApplyInfoSEO pcApplyInfoSEO){
        boolean flag = companyApplyService.addApplyInfo(pcApplyInfoSEO);
        if(flag){
            return sendJsonData(ResultMessage.SUCCESS, "操作成功");
        }
        return sendJsonData(ResultMessage.FAIL, "操作失败,验证码错误或公司名称已被注册。");
    }

    /**
     * 根据参数查询申请
     * @param companyApplySEO
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @MyRespBody
    @ApiOperation(value="前端--运营后台--公司管理--列表--李阳")
    public MyRespBundle<PageInfo<PcApplyInfoVo>> list(@ApiParam("申请信息查询参数")CompanyApplySEO companyApplySEO){
        PageInfo<PcApplyInfoVo> pageInfo = companyApplyService.findByParam(companyApplySEO);
        return sendJsonData(ResultMessage.SUCCESS, pageInfo);
    }


    /**
     * 删除申请记录（is_delete)
     * @param id
     * @return
     */
    @RequestMapping(value = "/delApply", method = RequestMethod.POST)
    @MyRespBody
    @ApiOperation(value="前端--运营后台--公司管理--操作--删除--申请id--李阳")
    public MyRespBundle<String> delApply(@RequestParam(value = "id") Integer id){
        boolean flag = companyApplyService.updateApply(id);
        if(flag){
            return sendJsonData(ResultMessage.SUCCESS, "操作成功");
        }
        return sendJsonData(ResultMessage.FAIL, "操作失败");
    }

    /**
     * 指定申请记录查询
     * @return
     */
    @RequestMapping(value = "/findById", method = RequestMethod.GET)
    @MyRespBody
    @ApiOperation(value="前端--运营后台--公司管理--操作--办理--申请id--李阳")
    public MyRespBundle<PcApplyInfoVo> findById(@RequestParam(value = "id")Integer id){
        PcApplyInfoVo pcApplyInfoVo = companyApplyService.findById(id);
        return sendJsonData(ResultMessage.SUCCESS, pcApplyInfoVo);
    }


    //申请列表条数
    @RequestMapping(value = "/countApply", method = RequestMethod.GET)
    @MyRespBody
    @ApiOperation(value="前端--运营后台--设计公司/装饰公司--申请信息--个数查询--李阳")
    public MyRespBundle<Long> countApply(@RequestParam(value = "roleId")String roleId){
        Long line = companyApplyService.countApply(roleId);
        return sendJsonData(ResultMessage.SUCCESS, line);
    }


    /**
     * 1，a:添加账号---》返回id   b:添加账号--》1，app注册运营添加账号：参数：角色  返回公司id  提交：插入公司表，注册表，更新注册表公司id
                                           2，运营注册：参数：角色  返回id               提交：插入公司表，申请表，注册表
     * 2，登陆
     * 3，激活：修改注册表
     * 4，登陆
     * 注：添加账号及发送短信后申请表状态改为已办理  不显示办理按钮。。
     */

    /**
     * a:添加账号---》返回id
     * @return
     */
    @RequestMapping(value = "/generateCompanyId", method = RequestMethod.GET)
    @MyRespBody
    @ApiOperation(value="前端--运营后台--公司管理--添加账号--提交（返回公司id）--李阳")
    public MyRespBundle<String> generateCompanyId(@RequestParam(value = "roleId")String roleId){
        String companyId = companyApplyService.generateCompanyId(roleId);
        return sendJsonData(ResultMessage.SUCCESS, companyId);
    }

    /**
     * b:添加账号--》创建用户
     * @return
     */
    @RequestMapping(value = "/addCompanyAdmin", method = RequestMethod.POST)
    @MyRespBody
    @ApiOperation(value="前端--运营后台--公司管理--添加账号--二次确认界面---确认(发送短信)--李阳")
    public MyRespBundle<Map<String, Object>> addCompanyAdmin(@ApiParam("申请信息")PcApplyInfoSEO pcApplyInfoSEO){
        Map<String, Object> map = companyApplyService.addCompanyAdmin(pcApplyInfoSEO);
        return sendJsonData(ResultMessage.SUCCESS, map);
    }

    /**
     * 激活账号
     * @return
     */
    /*@RequestMapping(value = "/actiCompanyAdmin", method = RequestMethod.POST)
    @MyRespBody
    @ApiOperation(value="激活账号")
    public MyRespBundle<String> actiCompanyAdmin(@ApiParam("用户注册")UserRegister userRegister){
        boolean flag = companyApplyService.updateRegister(userRegister);
        if(flag){
            return sendJsonData(ResultMessage.SUCCESS, "操作成功");
        }
        return sendJsonData(ResultMessage.FAIL, "操作失败");
    }*/

}
