package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 账号相关
 */
@RestController
@RequestMapping("/account")
public class AccountController extends AbsBaseController {


    @GetMapping("'/role/{id}/resource")
    @MyRespBody
    public MyRespBundle list(){


        return null;
    }

//    @PostMapping("/role")
//    @MyRespBody
//    public MyRespBundle<String> role(SystemRole systemRole){
//
//        return null;
//
//    }


}
