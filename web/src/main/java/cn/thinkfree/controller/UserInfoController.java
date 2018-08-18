package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.core.security.filter.util.SessionUserDetailsUtil;
import cn.thinkfree.database.model.UserInfo;
import cn.thinkfree.database.vo.UserVO;
import cn.thinkfree.service.user.UserInfoService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/userInfo")
public class UserInfoController  extends AbsBaseController {

    @Autowired
    UserInfoService userInfoService;

    @RequestMapping(value = "/findByParam", method = RequestMethod.GET)
    @MyRespBody
    @ApiOperation(value="公告详情", notes="操作人信息")

    public MyRespBundle<PageInfo<UserInfo>> findByParam(){
        UserVO uservo = (UserVO) SessionUserDetailsUtil.getUserDetails();
        if(null == uservo.getPcUserInfo()){
            return sendJsonData(ResultMessage.FAIL, "用户为空");
        }
        List<UserInfo> userInfo = userInfoService.selectByParam(uservo);
        PageInfo<UserInfo> pageInfo = new PageInfo<>(userInfo);
        return sendJsonData(ResultMessage.SUCCESS, pageInfo);
    }
}
