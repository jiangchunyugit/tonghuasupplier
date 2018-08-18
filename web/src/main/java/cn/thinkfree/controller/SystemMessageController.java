package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.annotation.MySysLog;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.core.constants.SysLogAction;
import cn.thinkfree.core.constants.SysLogModule;
import cn.thinkfree.core.security.filter.util.SessionUserDetailsUtil;
import cn.thinkfree.database.model.SystemMessage;
import cn.thinkfree.database.vo.UserVO;
import cn.thinkfree.service.sysMsg.SystemMessageService;
import com.github.pagehelper.PageInfo;
import com.sun.xml.internal.bind.v2.TODO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/sysMsg")
public class SystemMessageController extends AbsBaseController {

    @Autowired
    SystemMessageService sysMsgService;

    /**
     * 公告查询
     * @param pn
     * @param pageSize
     * @param sendUserId
     * @param sendTime
     * @return
     */
    @RequestMapping(value = "/findByParam", method = RequestMethod.POST)
    @MyRespBody
    @ApiOperation(value="查询公告信息", notes="根据操作人和日期查询公告信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "page", value = "当前页码", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType="query", name = "rows", value = "每页展示条数", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType="query", name = "sendUserId", value = "发送人id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "sendTime", value = "日期", required = true, dataType = "String")
    })
    public MyRespBundle<PageInfo<SystemMessage>> list(@RequestParam(value = "page")Integer page, @RequestParam(value = "rows")Integer rows,
                                                      @RequestParam(value = "sendUserId")String sendUserId, @RequestParam(value = "sendTime")String sendTime){
        if (page == null){
            page = 0;
        }
        if (rows == null){
            rows = 15;
        }
        UserVO uservo = (UserVO)SessionUserDetailsUtil.getUserDetails();
        if(uservo.getPcUserInfo() == null){
            return sendJsonData(ResultMessage.FAIL, "用户为空");
        }
        List<SystemMessage> sysMsg = sysMsgService.selectByParam(uservo, page, rows, sendUserId, sendTime);
        PageInfo<SystemMessage> pageInfo = new PageInfo<>(sysMsg);
        return sendJsonData(ResultMessage.SUCCESS, pageInfo);
    }

    /**
     * 公告删除
     * @param id
     * @return
     */
//    @MySysLog(desc = "/sysMsg/delSysMsg",action = SysLogAction.QUERY,module = SysLogModule.PC_NEWS)
    @RequestMapping(value = "/delSysMsg", method = RequestMethod.POST)
    @MyRespBody
    @ApiOperation(value="公告删除", notes="根据公告id删除公告")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "id", value = "公告id", required = true, dataType = "Integer")
    })
    public MyRespBundle<String> delSysMsg(@RequestParam(required = false,defaultValue = "1",value = "id")Integer id){
        if(null == id){
            return sendJsonData(ResultMessage.FAIL, "参数为空");
        }
        int line = sysMsgService.deleteByPrimaryKey(id);
        if(line > 0){
            return sendJsonData(ResultMessage.SUCCESS, line);
        }
        return sendJsonData(ResultMessage.FAIL, line);
    }

    /**
     * 单条公告查询
     * @param id
     * @return
     */
    @RequestMapping(value = "/findById", method = RequestMethod.POST)
    @MyRespBody
    @ApiOperation(value="公告列表查看", notes="根据公告id查看信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "id", value = "公告id", required = true, dataType = "Integer")
    })
    public MyRespBundle<PageInfo<SystemMessage>> findById(@RequestParam(value = "id")Integer id){
        if(null == id){
            return sendJsonData(ResultMessage.FAIL, "参数为空");
        }
        SystemMessage sysMsg = sysMsgService.selectByPrimaryKey(id);
        if(null == sysMsg){
            return sendJsonData(ResultMessage.FAIL, "失败");
        }
        return sendJsonData(ResultMessage.SUCCESS, sysMsg);
    }

    /**
     * 添加公告
     * @param sysMsg
     * @return
     */
//    @MySysLog(desc = "/sysMsg/saveSysMsg",action = SysLogAction.QUERY,module = SysLogModule.PC_NEWS)
    @RequestMapping(value = "/saveSysMsg", method = RequestMethod.POST)
    @MyRespBody
    @ApiOperation(value="新增公告", notes="")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "content", value = "发布内容", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "title", value = "标题", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "receiveRole", value = "对象", required = true, dataType = "String")
    })
    public MyRespBundle<String> saveSysMsg(@RequestParam(value = "content") String content, @RequestParam(value = "title") String title, @RequestParam(value = "receiveRole") String receiveRole){
        UserVO uservo = (UserVO)SessionUserDetailsUtil.getUserDetails();
        if(uservo.getPcUserInfo() == null){
            return sendJsonData(ResultMessage.FAIL, "用户为空");
        }
        SystemMessage sysMsg = new SystemMessage();
        sysMsg.setReceiveRole(receiveRole);
        sysMsg.setContent(content);
        sysMsg.setTitle(title);
        int line = sysMsgService.saveSysMsg(uservo.getPcUserInfo(),sysMsg);
        if(line > 0){
            return sendJsonData(ResultMessage.FAIL, line);
        }
        return sendJsonData(ResultMessage.SUCCESS, line);
    }
}
