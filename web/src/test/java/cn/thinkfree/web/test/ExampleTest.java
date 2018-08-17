package cn.thinkfree.web.test;

import cn.thinkfree.database.mapper.AreaMapper;
import cn.thinkfree.database.mapper.CityMapper;
import cn.thinkfree.database.mapper.ProvinceMapper;
import cn.thinkfree.database.model.*;
import cn.thinkfree.service.user.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ExampleTest {


    @Autowired
    ProvinceMapper provinceMapper;
    @Autowired
    CityMapper cityMapper;
    @Autowired
    AreaMapper areaMapper;

    @Test
    public void saveTest(){

    }

    @Test
    public void findTest(){

    }

}
