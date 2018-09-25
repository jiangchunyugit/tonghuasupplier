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
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
     * 公司申请事项
     * @param pcApplyInfo
     * @return
     */
    @RequestMapping(value = "/applyThink", method = RequestMethod.POST)
    @MyRespBody
    @ApiOperation(value="添加公司申请事项：入驻，资质变更，续约")
    public MyRespBundle<String> applyThink(@ApiParam("申请信息")PcApplyInfo pcApplyInfo){
        boolean flag = companyApplyService.addApplyInfo(pcApplyInfo);
        if(flag){
            return sendJsonData(ResultMessage.SUCCESS, "操作成功");
        }
        return sendJsonData(ResultMessage.FAIL, "操作失败");
    }

    /**
     * 根据参数查询申请
     * @param companyApplySEO
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @MyRespBody
    @ApiOperation(value="公司管理")
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
    @ApiOperation(value="公司管理---->删除")
    public MyRespBundle<String> delApply(@RequestParam(value = "id") Integer id){
        boolean flag = companyApplyService.updateApply(id);
        if(flag){
            return sendJsonData(ResultMessage.SUCCESS, "操作成功");
        }
        return sendJsonData(ResultMessage.FAIL, "操作失败");
    }

    /**
     * 申请记录查询
     * @return
     */
    @RequestMapping(value = "/findById", method = RequestMethod.GET)
    @MyRespBody
    @ApiOperation(value="公司管理---->办理")
    public MyRespBundle<PcApplyInfoVo> findById(@RequestParam(value = "id")Integer id){
        PcApplyInfoVo pcApplyInfoVo = companyApplyService.findById(id);
        return sendJsonData(ResultMessage.SUCCESS, pcApplyInfoVo);
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
    @ApiOperation(value="公司管理--->（办理）添加账号--->提交（返回公司id）")
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
    @ApiOperation(value="公司管理--->（办理）添加账号--->提交（返回公司id）---->确认(发送短信)")
    public MyRespBundle<String> addCompanyAdmin(@ApiParam("申请信息")PcApplyInfoSEO pcApplyInfoSEO){
        boolean flag = companyApplyService.addCompanyAdmin(pcApplyInfoSEO);
        if(flag){
            return sendJsonData(ResultMessage.SUCCESS, "操作成功");
        }
        return sendJsonData(ResultMessage.FAIL, "操作失败");
    }

    /**
     * 激活账号
     * @return
     */
    @RequestMapping(value = "/actiCompanyAdmin", method = RequestMethod.POST)
    @MyRespBody
    @ApiOperation(value="激活账号")
    public MyRespBundle<String> actiCompanyAdmin(@ApiParam("用户注册")UserRegister userRegister){
        boolean flag = companyApplyService.updateRegister(userRegister);
        if(flag){
            return sendJsonData(ResultMessage.SUCCESS, "操作成功");
        }
        return sendJsonData(ResultMessage.FAIL, "操作失败");
    }

}
