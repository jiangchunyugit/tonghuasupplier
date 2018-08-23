package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.vo.IndexMenuVO;
import cn.thinkfree.service.index.IndexService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(description = "系统相关操作")
@RestController
public class SystemController extends AbsBaseController {


    @Autowired
    IndexService indexService;


    /**
     * 获取首页菜单
     * @return
     */
    @ApiOperation(value = "获取菜单",notes = "根据当前登录用户信息获取菜单")
    @GetMapping("/menu")
    @MyRespBody
    public MyRespBundle<List<IndexMenuVO>> menu(){
        List<IndexMenuVO> indexMenuVOS = indexService.listIndexMenu();
        return sendJsonData(ResultMessage.SUCCESS,indexMenuVOS);
    }




}
