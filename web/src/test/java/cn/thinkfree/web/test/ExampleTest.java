package cn.thinkfree.web.test;

import cn.thinkfree.database.model.User;
import cn.thinkfree.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ExampleTest {

    @Autowired
    UserService userService;

    @Test
    public void saveTest(){
        User user = new User();
        user.setUsername("Test");
        user.setPassword("pwd");
        userService.save(user);

    }

    @Test
    public void findTest(){
        System.out.println(userService);
        User user = userService.findById("1");
        System.out.println(user.getPassword());
        Assert.assertNotNull(user);
    }

}
