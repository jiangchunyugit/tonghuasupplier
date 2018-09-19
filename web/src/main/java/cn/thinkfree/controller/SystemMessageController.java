package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.model.SystemMessage;
import cn.thinkfree.database.utils.BeanValidator;
import cn.thinkfree.database.vo.SystemMessageVo;
import cn.thinkfree.service.sysMsg.SystemMessageService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/sysMsg")
@Api(value = "公告管理",description = "公告管理")
public class SystemMessageController extends AbsBaseController {

    @Autowired
    SystemMessageService sysMsgService;

    /**
     * 公告查询
     * @param page
     * @param rows
     * @param sendUserId
     * @param sendTime
     * @return
     */
    @RequestMapping(value = "/findByParam", method = RequestMethod.GET)
    @MyRespBody
    @ApiOperation(value="查询公告信息", notes="根据操作人和日期查询公告信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "page", value = "当前页码", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType="query", name = "rows", value = "每页展示条数", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType="query", name = "sendUserId", value = "发送人id", required = false, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "sendTime", value = "日期", required = false, dataType = "String")
    })
    public MyRespBundle<PageInfo<SystemMessage>> list(@RequestParam(value = "page")Integer page, @RequestParam(value = "rows")Integer rows,
                                                      @RequestParam(required = false,value = "sendUserId")String sendUserId, @RequestParam(required = false,value = "sendTime")String sendTime){
        PageInfo<SystemMessageVo> pageInfo = sysMsgService.selectByParam(page, rows, sendUserId, sendTime);

        return sendJsonData(ResultMessage.SUCCESS, pageInfo);
    }

    /**
     * 公告删除
     * @param id
     * @return
     */
//    @MySysLog(desc = "/sysMsg/delSysMsg",action = SysLogAction.QUERY,module = SysLogModule.PC_NEWS)
    @RequestMapping(value = "/delSysMsg", method = RequestMethod.DELETE)
    @MyRespBody
    @ApiOperation(value="公告删除", notes="根据公告id删除公告")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "id", value = "公告id", required = true, dataType = "Integer")
    })
    public MyRespBundle<String> delSysMsg(@RequestParam(value = "id")Integer id){
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
    @RequestMapping(value = "/findById", method = RequestMethod.GET)
    @MyRespBody
    @ApiOperation(value="公告列表查看", notes="根据公告id查看信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "id", value = "公告id", required = true, dataType = "Integer")
    })
    public MyRespBundle<PageInfo<SystemMessage>> findById(@RequestParam(value = "id")Integer id){
        SystemMessage sysMsg = sysMsgService.selectByPrimaryKey(id);
        if(null == sysMsg){
            return sendJsonData(ResultMessage.FAIL, "失败");
        }
        return sendJsonData(ResultMessage.SUCCESS, sysMsg);
    }

    /**
     * 添加公告
     * @param systemMessage
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
    public MyRespBundle<String> saveSysMsg(@ApiParam("公告信息") SystemMessage systemMessage){
        BeanValidator.validate(systemMessage);
        int line = sysMsgService.saveSysMsg(systemMessage);
        if(line > 0){
            return sendJsonData(ResultMessage.SUCCESS, line);
        }
        return sendJsonData(ResultMessage.FAIL, line);
    }

}
