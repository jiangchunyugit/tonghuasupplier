package cn.thinkfree.web.test;

import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.vo.account.*;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WebAppConfiguration
public class AccountControllerTest extends AbsControllerTest {


    @Test
    public void create() throws Exception {
        PermissionVO saveObj = new PermissionVO();
        saveObj.setName("测试");
        saveObj.setDesc("测试测试");

        String rs = mvc.perform(post("/account/permission")
                .with(user(userVO))
//                .contentType(MediaType.APPLICATION_JSON)
//                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                .content(gson.toJson(saveObj))
                .param("name","测试")
                .param("desc","测试用")
                )
                .andExpect(status().isOk())
                .andDo(print())         //打印出请求和相应的内容
                .andReturn().getResponse().getContentAsString();
         ;
        MyRespBundle<PermissionVO> rsb = gson.fromJson(rs,  new TypeToken<MyRespBundle<PermissionVO>>() {}.getType());
        Assert.assertNotNull(rsb.getData().getName());
    }

    @Test
    public void list() throws Exception {
        String rs = mvc.perform(get("/account/permission")
                .with(user(userVO))
        )
                .andExpect(status().isOk())
                .andDo(print())         //打印出请求和相应的内容
                .andReturn().getResponse().getContentAsString();
        MyRespBundle<PageInfo<PermissionVO>> rsb = gson.fromJson(rs,  new TypeToken<MyRespBundle<PageInfo<PermissionVO>>>() {}.getType());
        Assert.assertNotNull(rsb.getData().getSize());
    }

    @Test
    public void listByName() throws Exception {
        String rs = mvc.perform(get("/account/permission")
                        .with(user(userVO))
                        .param("name","测试")
        )
                .andExpect(status().isOk())
                .andDo(print())         //打印出请求和相应的内容
                .andReturn().getResponse().getContentAsString();
        MyRespBundle<PageInfo<PermissionVO>> rsb = gson.fromJson(rs,  new TypeToken<MyRespBundle<PageInfo<PermissionVO>>>() {}.getType());
        Assert.assertNotEquals(rsb.getData().getSize(),0);
    }
    @Test
    public void listByState() throws Exception {
        String rs = mvc.perform(get("/account/permission")
                .with(user(userVO))
                .param("state","1")
        )
                .andExpect(status().isOk())
                .andDo(print())         //打印出请求和相应的内容
                .andReturn().getResponse().getContentAsString();
        MyRespBundle<PageInfo<PermissionVO>> rsb = gson.fromJson(rs,  new TypeToken<MyRespBundle<PageInfo<PermissionVO>>>() {}.getType());
        Assert.assertEquals(rsb.getData().getSize(),0);
    }

    @Test
    public void findOne() throws Exception {
        String rs = mvc.perform(get("/account/permission/5")
                .with(user(userVO))
        )
                .andExpect(status().isOk())
                .andDo(print())         //打印出请求和相应的内容
                .andReturn().getResponse().getContentAsString();
        MyRespBundle<PermissionVO> rsb = gson.fromJson(rs,  new TypeToken<MyRespBundle<PermissionVO>>() {}.getType());
        Assert.assertEquals(rsb.getData().getName(),"测试");
    }

    @Test
    public void edit() throws Exception {
        String rs = mvc.perform(post("/account/permission/5")
                .with(user(userVO))
                .param("desc","改动")
        )
                .andExpect(status().isOk())
                .andDo(print())         //打印出请求和相应的内容
                .andReturn().getResponse().getContentAsString();

        MyRespBundle<PermissionVO> rsb = gson.fromJson(rs,  new TypeToken<MyRespBundle<PermissionVO>>() {}.getType());
        Assert.assertEquals(rsb.getData().getDesc(),"改动");
    }

    @Test
    public void resource() throws Exception {
        String rs = mvc.perform(get("/account/permission/1/resource")
                .with(user(userVO))
        )
                .andExpect(status().isOk())
                .andDo(print())         //打印出请求和相应的内容
                .andReturn().getResponse().getContentAsString();

        MyRespBundle<List<SystemResourceTreeVO>> rsb = gson.fromJson(rs,  new TypeToken<MyRespBundle<List<SystemResourceTreeVO>>>() {}.getType());
        Assert.assertNotNull(rsb.getData());

    }
    @Test
    public void resourceDetails() throws Exception {
        String rs = mvc.perform(get("/account/permission/1/resource/12/details")
                .with(user(userVO))
        )
                .andExpect(status().isOk())
                .andDo(print())         //打印出请求和相应的内容
                .andReturn().getResponse().getContentAsString();

        MyRespBundle<List<SystemResourceTreeVO>> rsb = gson.fromJson(rs,  new TypeToken<MyRespBundle<List<SystemResourceTreeVO>>>() {}.getType());
        Assert.assertNotNull(rsb.getData());

    }


    @Test
    public void authorize() throws Exception {
        String rs = mvc.perform(post("/account/permission/5/resource")
                .with(user(userVO))
                .param("resource","1")
                .param("resource","2")
        )
                .andExpect(status().isOk())
                .andDo(print())         //打印出请求和相应的内容
                .andReturn().getResponse().getContentAsString();

        MyRespBundle<String> rsb = gson.fromJson(rs,  new TypeToken<MyRespBundle<String>>() {}.getType());
        Assert.assertEquals(rsb.getData(),"操作成功!");

    }
    @Test
    public void clearResource() throws Exception {
        String rs = mvc.perform(post("/account/permission/5/resource")
                .with(user(userVO))
        )
                .andExpect(status().isOk())
                .andDo(print())         //打印出请求和相应的内容
                .andReturn().getResponse().getContentAsString();

        MyRespBundle<String> rsb = gson.fromJson(rs,  new TypeToken<MyRespBundle<String>>() {}.getType());
        Assert.assertEquals(rsb.getData(),"操作成功!");

    }


    @Test
    public void createRole() throws Exception {


        String rs = mvc.perform(post("/account/role")
                        .with(user(userVO))
                        .param("name","测试")
                        .param("desc","测试用")
                        .param("root","4")
                        .param("city","1")
                        .param("province","2")
        )
                .andExpect(status().isOk())
                .andDo(print())         //打印出请求和相应的内容
                .andReturn().getResponse().getContentAsString();
        ;
        MyRespBundle<SystemRoleVO> rsb = gson.fromJson(rs,  new TypeToken<MyRespBundle<SystemRoleVO>>() {}.getType());
        Assert.assertEquals( rsb.getData().getScope(),Integer.valueOf("7"));
    }

    @Test
    public void listRole() throws Exception {
        String rs = mvc.perform(get("/account/role")
                .with(user(userVO))
        )
                .andExpect(status().isOk())
                .andDo(print())         //打印出请求和相应的内容
                .andReturn().getResponse().getContentAsString();
        MyRespBundle<PageInfo<SystemRoleVO>> rsb = gson.fromJson(rs,  new TypeToken<MyRespBundle<PageInfo<SystemRoleVO>>>() {}.getType());
        Assert.assertNotNull(rsb.getData().getSize());
    }
    @Test
    public void detailRole() throws Exception {
        String rs = mvc.perform(get("/account/role/65")
                .with(user(userVO))
        )
                .andExpect(status().isOk())
                .andDo(print())         //打印出请求和相应的内容
                .andReturn().getResponse().getContentAsString();
        MyRespBundle<SystemRoleVO> rsb = gson.fromJson(rs,  new TypeToken<MyRespBundle<SystemRoleVO>>() {}.getType());
        System.out.println(rsb.getData().getSelectedRoot());
        System.out.println(rsb.getData().getSelectedProvince());
        System.out.println(rsb.getData().getSelectedCity());
        Assert.assertEquals(rsb.getData().getSelectedCity(),Integer.valueOf(1));
    }

    @Test
    public void editRole() throws Exception {
        String rs = mvc.perform(post("/account/role/65")
                .with(user(userVO))
                .param("desc","改动")
        )
                .andExpect(status().isOk())
                .andDo(print())         //打印出请求和相应的内容
                .andReturn().getResponse().getContentAsString();

        MyRespBundle<SystemRoleVO> rsb = gson.fromJson(rs,  new TypeToken<MyRespBundle<SystemRoleVO>>() {}.getType());
        Assert.assertEquals(rsb.getData().getScope(),Integer.valueOf(0));
    }

    @Test
    public void permissions() throws Exception {
        String rs = mvc.perform(get("/account/role/57/permission")
                .with(user(userVO))
        )
                .andExpect(status().isOk())
                .andDo(print())         //打印出请求和相应的内容
                .andReturn().getResponse().getContentAsString();

        MyRespBundle<List<PermissionVO>> rsb = gson.fromJson(rs,  new TypeToken<MyRespBundle<List<PermissionVO>>>() {}.getType());
        Assert.assertNotNull(rsb.getData());

    }


    @Test
    public void grant() throws Exception {
        String rs = mvc.perform(post("/account/role/5/permission")
                .with(user(userVO))
                .param("permission","5")
                .param("permission","6")
        )
                .andExpect(status().isOk())
                .andDo(print())         //打印出请求和相应的内容
                .andReturn().getResponse().getContentAsString();

        MyRespBundle<String> rsb = gson.fromJson(rs,  new TypeToken<MyRespBundle<String>>() {}.getType());
        Assert.assertEquals(rsb.getData(),"操作成功!");

    }
    @Test
    public void clearPermission() throws Exception {
        String rs = mvc.perform(post("/account/role/5/permission")
                .with(user(userVO))
        )
                .andExpect(status().isOk())
                .andDo(print())         //打印出请求和相应的内容
                .andReturn().getResponse().getContentAsString();

        MyRespBundle<String> rsb = gson.fromJson(rs,  new TypeToken<MyRespBundle<String>>() {}.getType());
        Assert.assertEquals(rsb.getData(),"操作成功!");

    }

    @Test
    public void saveAccount() throws Exception {
        AccountVO save = new AccountVO();
        PcUserInfo pcUserInfo = new PcUserInfo();
        pcUserInfo.setMemo("神TM奇了");
        save.setPcUserInfo(pcUserInfo);
        ThirdAccountVO third = new ThirdAccountVO();
        third.setAccount("3569812");
        third.setWorkNumber("20182001");
        third.setPhone("15865561492");
        third.setId("4");
        third.setEmail("email");
        third.setName("名字");
        third.setDept("部门");
        third.setGroup("组");
        save.setThirdAccount(third);
        Set<SystemUserStore> stores = new HashSet<>();

        SystemUserStore sus1= new SystemUserStore();
        sus1.setBranchCode("b1");
        sus1.setCityBranchCode("c1");
        sus1.setStoreCode("s1");
        stores.add(sus1);
        SystemUserStore sus2= new SystemUserStore();
        sus2.setBranchCode("b2");
        sus2.setCityBranchCode("c2");
        sus2.setStoreCode("s2");
        stores.add(sus2);
        SystemUserStore sus3= new SystemUserStore();
        sus3.setBranchCode("b3");
        sus3.setCityBranchCode("c3");
        sus3.setStoreCode("s3");
        stores.add(sus3);
        SystemUserStore sus4= new SystemUserStore();
        sus4.setBranchCode("b4");
        sus4.setCityBranchCode("c4");
        sus4.setStoreCode("s4");
        stores.add(sus4);
        SystemUserStore sus5= new SystemUserStore();
        sus5.setBranchCode("b2");
        sus5.setCityBranchCode("c2");
        sus5.setStoreCode("s2");
        stores.add(sus5);
        save.setStoreList(stores);


        String rs = mvc.perform(post("/account/info")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(save))
                        .with(user(userVO))
//                .param("pcUserInfo.memo","神TM奇")
////                .param("branchCompany.id",null)
////                .param("branchCompany.provinceCode",null)
////                .param("cityBranch.id",null)
////                .param("cityBranch.provinceCode",null)
////                .param("cityBranch.cityCode",null)
////                .param("roles[0].id","1")
////                .param("roles[1].id","2")
////                .param("roles[2].id","3")
//                .param("thirdAccount.dept","临时部门")
//                .param("thirdAccount.group","临时组")
//                .param("thirdAccount.name","临时姓名")
//                .param("thirdAccount.workNumber","20182001")
//                .param("thirdAccount.account","3569812")
//                .param("thirdAccount.phone","15865561492")
//                .param("thirdAccount.email","email")
//                .param("thirdAccount.id","4")
//                .param("")
        )
                .andExpect(status().isOk())
                .andDo(print())         //打印出请求和相应的内容
                .andReturn().getResponse().getContentAsString();

        MyRespBundle<AccountVO> rsb = gson.fromJson(rs,  new TypeToken<MyRespBundle<AccountVO>>() {}.getType());
        Assert.assertEquals(rsb.getData(),save);
    }


    @Test
    public void saveAccountException() throws Exception {
        AccountVO save = new AccountVO();
        PcUserInfo pcUserInfo = new PcUserInfo();
        pcUserInfo.setMemo("神TM奇了");
        save.setPcUserInfo(pcUserInfo);
        ThirdAccountVO third = new ThirdAccountVO();
        third.setAccount("3569812");
        third.setWorkNumber("20182001");
        third.setPhone("15865561492");
        third.setId("4");
        third.setEmail("email");
        third.setName("名字");
        third.setDept("部门");
        third.setGroup("组");
        save.setThirdAccount(third);
        Set<SystemUserStore> stores = new HashSet<>();

        SystemUserStore sus1= new SystemUserStore();
        sus1.setBranchCode("b1");
        sus1.setCityBranchCode("c1");
        sus1.setStoreCode("s1");
        stores.add(sus1);
        SystemUserStore sus2= new SystemUserStore();
        sus2.setBranchCode("b2");
        sus2.setCityBranchCode("c2");
        sus2.setStoreCode("s2");
        stores.add(sus2);
        SystemUserStore sus3= new SystemUserStore();
        sus3.setBranchCode("b3");
        sus3.setCityBranchCode("c3");
        sus3.setStoreCode("s3");
        stores.add(sus3);
        SystemUserStore sus4= new SystemUserStore();
//        sus4.setBranchCode("b4");
//        sus4.setCityBranchCode("c4");
        sus4.setStoreCode("s4");
        stores.add(sus4);
        SystemUserStore sus5= new SystemUserStore();
        sus5.setBranchCode("b2");
        sus5.setCityBranchCode("c2");
        sus5.setStoreCode("s2");
        stores.add(sus5);
        save.setStoreList(stores);

        SystemRole r1 = new SystemRole();
        r1.setId(1);
        SystemRole r2 = new SystemRole();
        r2.setId(2);
        SystemRole r3 = new SystemRole();
        r3.setId(3);
        save.setRoles(Lists.newArrayList(r1,r2,r3));

        String str = new Gson().toJson(save);
        System.out.println(str);
        String rs = mvc.perform(post("/account/info")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(str)
                        .with(user(userVO))

        )
                .andExpect(status().isOk())
                .andDo(print())         //打印出请求和相应的内容
                .andReturn().getResponse().getContentAsString();

        MyRespBundle<AccountVO> rsb = gson.fromJson(rs,  new TypeToken<MyRespBundle<AccountVO>>() {}.getType());
        Assert.assertEquals(rsb.getData(),"无辖区");
    }


    @Test
    public void findAccount() throws Exception {

        String rs = mvc.perform(get("/account/info/PC000001540521613203oRBCy00000")
                .with(user(userVO))
        )
                .andExpect(status().isOk())
                .andDo(print())         //打印出请求和相应的内容
                .andReturn().getResponse().getContentAsString();

        MyRespBundle<AccountVO> rsb = gson.fromJson(rs,  new TypeToken<MyRespBundle<AccountVO>>() {}.getType());
        Assert.assertEquals(rsb.getData().getPcUserInfo().getId(),"PC000001540521613203oRBCy00000");
    }

    @Test
    public void editAccount() throws Exception {

        String rs = mvc.perform(post("/account/info/PC000001540521613203oRBCy00000")
                .with(user(userVO))
                        .param("pcUserInfo.memo","备注")
                .param("branchCompany.id","40")
                .param("branchCompany.provinceCode","37")
                .param("cityBranch.id","45")
                .param("cityBranch.provinceCode","37")
                .param("cityBranch.cityCode","3701")
                .param("roles[0].id","1")
                .param("roles[1].id","2")
                .param("roles[2].id","3")
        )
                .andExpect(status().isOk())
                .andDo(print())         //打印出请求和相应的内容
                .andReturn().getResponse().getContentAsString();

        MyRespBundle<String> rsb = gson.fromJson(rs,  new TypeToken<MyRespBundle<String>>() {}.getType());
        Assert.assertEquals(rsb.getData() ,"操作成功!");
    }


    @Test
    public void delAccount() throws Exception {

        String rs = mvc.perform(delete("/account/info/PC000001540519228894yLDL800000")
                        .with(user(userVO))
        )
                .andExpect(status().isOk())
                .andDo(print())         //打印出请求和相应的内容
                .andReturn().getResponse().getContentAsString();

        MyRespBundle<String> rsb = gson.fromJson(rs,  new TypeToken<MyRespBundle<String>>() {}.getType());
        Assert.assertEquals(rsb.getData(),"操作成功!");
    }

    @Test
    public void me() throws Exception {

        String rs = mvc.perform(get("/account/me")
                .with(user(userVO))
        )
                .andExpect(status().isOk())
                .andDo(print())         //打印出请求和相应的内容
                .andReturn().getResponse().getContentAsString();

        MyRespBundle<Map<String,Object>> rsb = gson.fromJson(rs,  new TypeToken<MyRespBundle<Map<String,Object>>>() {}.getType());
        Assert.assertEquals(rsb.getData(),"操作成功!");
    }


    @Test
    public void infoList() throws Exception {

        String rs = mvc.perform(get("/account/info")
                .with(user(userVO))
//                .param("name","临时")
        )
                .andExpect(status().isOk())
                .andDo(print())         //打印出请求和相应的内容
                .andReturn().getResponse().getContentAsString();

        MyRespBundle<PageInfo<AccountListVO>> rsb = gson.fromJson(rs,  new TypeToken<MyRespBundle<PageInfo<AccountListVO>>>() {}.getType());
        Assert.assertEquals(rsb.getData(),"操作成功!");
    }

    @Test
    public void regionsAndRole() throws Exception {
        String rs = mvc.perform(get("/account/info/1/regions/2/role")
                        .with(user(userVO))
                .param("level","Creator")
        )
                .andExpect(status().isOk())
                .andDo(print())         //打印出请求和相应的内容
                .andReturn().getResponse().getContentAsString();

        MyRespBundle<PageInfo<AccountListVO>> rsb = gson.fromJson(rs,  new TypeToken<MyRespBundle<PageInfo<AccountListVO>>>() {}.getType());
        Assert.assertEquals(rsb.getData(),"操作成功!");
///info/{id}/regions/{rid}/role
    }

    @Test
    public void saveRegionsAndRole() throws Exception {
        String rs = mvc.perform(post("/account/info/1/regions/2/role")
                .with(user(userVO))
                .param("roles","")
//                .param("roles","2")
        )
                .andExpect(status().isOk())
                .andDo(print())         //打印出请求和相应的内容
                .andReturn().getResponse().getContentAsString();

        MyRespBundle<PageInfo<AccountListVO>> rsb = gson.fromJson(rs,  new TypeToken<MyRespBundle<PageInfo<AccountListVO>>>() {}.getType());
        Assert.assertEquals(rsb.getData(),"操作成功!");
///info/{id}/regions/{rid}/role
    }

}
