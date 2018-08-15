package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.AppParameter;
import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.annotation.MySysLog;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRequBundle;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.core.constants.SysLogAction;
import cn.thinkfree.core.constants.SysLogModule;
import cn.thinkfree.core.utils.SpringContextHolder;
import cn.thinkfree.core.utils.WebFileUtil;
import cn.thinkfree.database.mapper.PreProjectGuideMapper;
import cn.thinkfree.database.model.PreProjectGuide;
import cn.thinkfree.database.model.UserInfo;
import cn.thinkfree.service.user.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ExampleController extends AbsBaseController {

    @Autowired
    UserService userService;

    @Autowired
    RestTemplate restTemplate;


    @GetMapping("/test")
    public void test(String code){
        userService.countCompanyUser(code);
    }



    @PostMapping("/file")
    @MyRespBody
    @MySysLog(desc = "",action = SysLogAction.LOGIN,module = SysLogModule.PC_USER)
    public void file(@AppParameter @RequestParam("file") MyRequBundle< MultipartFile> file){
        System.out.println(file.getModel());
        System.out.println(file);
        ApplicationContext sp = SpringContextHolder.getApplicationContext();
        System.out.println(sp);
        String fs = WebFileUtil.fileCopy("static/", file.getModel());
        System.out.println(fs);
    }

    @GetMapping("/rest")
    public void testRest(){
        System.out.println(restTemplate);

        Map<String,String> map = new HashMap<String, String>();
        map.put("username","2");
        map.put("password","321");
        MultiValueMap header = new LinkedMultiValueMap();
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("multipart/form-data; charset=UTF-8");
        headers.setContentType(type);
        HttpEntity<String> requ = new HttpEntity<String>("companyId=BD2018080710405900001",headers);

        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("projectFile", new FileSystemResource(new File("G:\\thinkfree\\导入报价单模板.xlsx")));
        param.add("priceFile", new FileSystemResource(new File("G:\\thinkfree\\新建项目导入模板.xlsx")));
        param.add("companyId","BD2018080710405900001");

        String rs = restTemplate.postForObject("http://10.240.10.93:8789/import/file",
                param,
                String.class);
        System.out.println(rs);
    }


    @Autowired
    PreProjectGuideMapper preProjectGuideMapper;
    @MyRespBody
    @GetMapping("/union")
    public MyRespBundle<PageInfo<PreProjectGuide>> page(){
        PageHelper.startPage(1,2);
        List<PreProjectGuide> ps = preProjectGuideMapper.selectByExample(null);
        PageInfo<PreProjectGuide> pi = new PageInfo<PreProjectGuide>(ps);
        return sendJsonData(ResultMessage.SUCCESS,pi);
    }




}
