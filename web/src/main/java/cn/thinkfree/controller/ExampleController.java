package cn.thinkfree.controller;

import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.database.mapper.UserInfoMapper;
import cn.thinkfree.database.model.User;
import cn.thinkfree.database.model.UserInfo;
import cn.thinkfree.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExampleController extends AbsBaseController {

    @Autowired
    UserService userService;

    @GetMapping("/userinfo/{id}")
    public UserInfo testUserInfo(@PathVariable Integer id){
        System.out.println(id );
        User u = userService.findById(id + "");
        System.out.println(u);
        return null;
    }

    @GetMapping("/test")
    public void test(String code){
        userService.countCompanyUser(code);
    }
}
