package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.model.PcApplyInfo;
import cn.thinkfree.database.vo.CompanyApplySEO;
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
    @ApiOperation(value="公司申请事项：入驻，资质变更，续约")
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
    @RequestMapping(value = "/list", method = RequestMethod.POST)
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
    @RequestMapping(value = "/findById", method = RequestMethod.POST)
    @MyRespBody
    @ApiOperation(value="公司管理---->办理")
    public MyRespBundle<PcApplyInfoVo> findById(@RequestParam(value = "id")Integer id){
        PcApplyInfoVo pcApplyInfoVo = companyApplyService.findById(id);
        return sendJsonData(ResultMessage.SUCCESS, pcApplyInfoVo);
    }

    //添加账号 1，app注册运营添加账号：参数：id，角色 回显数据 返回公司id  提交：插入公司表
//           2，运营注册：参数：角色  返回id                         提交：插入公司表，申请表

}
