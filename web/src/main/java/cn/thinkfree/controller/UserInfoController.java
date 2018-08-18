package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.core.security.filter.util.SessionUserDetailsUtil;
import cn.thinkfree.database.model.PcUserInfo;
import cn.thinkfree.database.vo.UserVO;
import cn.thinkfree.service.pcUser.PcUserInfoService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/userInfo")
public class UserInfoController  extends AbsBaseController {

    @Autowired
    PcUserInfoService pcUserInfoService;

    @RequestMapping(value = "/findByParam", method = RequestMethod.GET)
    @MyRespBody
    @ApiOperation(value="公告详情", notes="操作人信息")

    public MyRespBundle<PageInfo<PcUserInfo>> findByParam(){
        UserVO uservo = (UserVO) SessionUserDetailsUtil.getUserDetails();

        List<PcUserInfo> pcUserInfos = pcUserInfoService.selectByParam(uservo);
        PageInfo<PcUserInfo> pageInfo = new PageInfo<>(pcUserInfos);
        return sendJsonData(ResultMessage.SUCCESS, pageInfo);
    }
}
