package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.core.security.filter.util.SessionUserDetailsUtil;
import cn.thinkfree.database.model.PcUserInfo;
import cn.thinkfree.database.vo.MyPageHelper;
import cn.thinkfree.database.vo.PcUserInfoVo;
import cn.thinkfree.database.vo.UserVO;
import cn.thinkfree.service.pcUser.PcUserInfoService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping(value = "/userInfo")
@Api(value = "账户信息（权限管理）", description = "账户信息（权限管理）")
public class PcUserInfoController extends AbsBaseController {

    @Autowired
    PcUserInfoService pcUserInfoService;

    @RequestMapping(value = "/findByParam", method = RequestMethod.GET)
    @MyRespBody
    @ApiOperation(value="公告详情:获取操作人信息", notes="操作人信息")
    public MyRespBundle<PageInfo<PcUserInfo>> findByParam(){
        UserVO uservo = (UserVO) SessionUserDetailsUtil.getUserDetails();

        List<PcUserInfo> pcUserInfos = pcUserInfoService.selectByParam(uservo);
        PageInfo<PcUserInfo> pageInfo = new PageInfo<>(pcUserInfos);
        return sendJsonData(ResultMessage.SUCCESS, pageInfo);
    }

    /**
     * 新增账户
     */
    @RequestMapping(value = "/saveByUserInfo", method = RequestMethod.POST)
    @MyRespBody
    @ApiOperation(value="新增")
    public MyRespBundle<String> saveByUserInfo(@ApiParam("账户信息")PcUserInfoVo pcUserInfoVo){

        boolean flag = pcUserInfoService.saveUserInfo(pcUserInfoVo);
        if(flag){
            return sendJsonData(ResultMessage.SUCCESS, flag);
        }
        return sendJsonData(ResultMessage.FAIL, flag);
    }

    /**
     * 账户查询更据条件查询
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
//    @MyRespBody
    @ApiOperation(value="模糊查询", notes="")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "page", value = "当前页码", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType="query", name = "rows", value = "一页显示行数", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType="query", name = "data", value = "模糊查询类型随便", required = false, dataType = "Object")
    })
    public MyRespBundle<PageInfo<PcUserInfoVo>> list(MyPageHelper pageHelper){
        UserVO uservo = (UserVO) SessionUserDetailsUtil.getUserDetails();

        PageInfo<PcUserInfoVo> pcUserInfoVoPageInfo = pcUserInfoService.findByParam(uservo, pageHelper);

        return sendJsonData(ResultMessage.SUCCESS, pcUserInfoVoPageInfo);
    }


    /**
     * 单条账户查询
     */
    @RequestMapping(value = "/findByUserId", method = RequestMethod.GET)
    @MyRespBody
    @ApiOperation(value="点击编辑查询的账户信息", notes="")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "userId", value = "用户id", required = true, dataType = "String")
    })
    public MyRespBundle<PcUserInfoVo> findByUserId(@RequestParam(value="userId")String userId){

        PcUserInfoVo pcUserInfoVos = pcUserInfoService.findByUserId(userId);

        return sendJsonData(ResultMessage.SUCCESS, pcUserInfoVos);
    }

    /**
     *  删除账户
     */
    @RequestMapping(value = "/delByUserId", method = RequestMethod.DELETE)
    @MyRespBody
    @ApiOperation(value="删除账户", notes="")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "userId", value = "用户id", required = true, dataType = "String")
    })
    public MyRespBundle<String> delByUserId(@RequestParam(value="userId")String userId){

        boolean flag = pcUserInfoService.delPcUserInfo(userId);

        if (flag){
            return sendJsonData(ResultMessage.SUCCESS, flag);
        }
        return sendJsonData(ResultMessage.FAIL, flag);
    }

    /**
     * 修改账户
     */
    @RequestMapping(value = "/updateByUserId", method = RequestMethod.POST)
    @MyRespBody
    @ApiOperation(value="编辑账户")
    /*@ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "name", value = "姓名", required = false, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "phone", value = "手机号", required = false, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "password", value = "密码", required = false, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "memo", value = "备注", required = false, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "id", value = "账号id", required = true, dataType = "String")
    })*/
    public MyRespBundle<String> updateByUserId(@ApiParam("账户信息")PcUserInfoVo pcUserInfoVo){

        boolean flag = pcUserInfoService.updateUserInfo(pcUserInfoVo);
        if (flag){
            return sendJsonData(ResultMessage.SUCCESS, flag);
        }
        return sendJsonData(ResultMessage.SUCCESS, flag);
    }
}
