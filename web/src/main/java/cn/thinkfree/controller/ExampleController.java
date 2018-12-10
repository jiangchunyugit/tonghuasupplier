package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.AppParameter;
import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.annotation.MySysLog;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRequBundle;
import cn.thinkfree.core.constants.SysLogAction;
import cn.thinkfree.core.constants.SysLogModule;
import cn.thinkfree.core.utils.SpringContextHolder;
import cn.thinkfree.core.utils.WebFileUtil;
import cn.thinkfree.database.event.SendValidateCode;
import cn.thinkfree.database.event.account.AccountCreate;
import cn.thinkfree.database.event.account.ResetPassWord;
import cn.thinkfree.database.event.sync.CompanyJoin;
import cn.thinkfree.database.event.sync.FinishContract;
import cn.thinkfree.database.model.ContractInfo;
import cn.thinkfree.database.vo.PcUserInfoVo;
import cn.thinkfree.service.contract.AgencyService;
import cn.thinkfree.service.contract.ContractService;
import cn.thinkfree.service.event.CustomListenerServie;
import cn.thinkfree.service.event.EventService;
import cn.thinkfree.service.pcthirdpartdate.ThirdPartDateService;
import cn.thinkfree.service.user.UserService;
import cn.thinkfree.service.utils.ExcelData;
import cn.thinkfree.service.utils.ExcelUtils;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class ExampleController extends AbsBaseController {
	


    @Autowired
    UserService userService;

    @Autowired
    RestTemplate restTemplate;
    
    @Autowired
    EventService eventService;


   @Autowired
   CustomListenerServie customListenerServie;


   
   @Autowired
   ThirdPartDateService thirdPartDateService;

    @Autowired
    AgencyService agencyService;
   
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


   /* @Autowired
   // PreProjectGuideMapper preProjectGuideMapper;
    @MyRespBody
    @GetMapping("/union")
    public MyRespBundle<PageInfo<PreProjectGuide>> page(){
        PageHelper.startPage(1,2);
        List<PreProjectGuide> ps = preProjectGuideMapper.selectByExample(null);
        PageInfo<PreProjectGuide> pi = new PageInfo<PreProjectGuide>(ps);
        return sendJsonData(ResultMessage.SUCCESS,pi);
    }*/

	@Autowired
	ContractService contractService;
	
	@Autowired
    private ApplicationContext applicationContext;
    /**
     * 测试创建合同
     * @return
     */
    @MyRespBody
    @GetMapping("/createContract")
    public String createContract(){
    	try {
    		ContractInfo s = new ContractInfo();
        	s.setContractStatus("1");
        	//applicationContext.publishEvent(new AuditEvent(s));
        	customListenerServie.publish(s);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
    	//contractService.createContractDoc("HT2018080710405900001");
    
        return "成功";
    }
    @ExceptionHandler(value=Exception.class)
    @RequestMapping("export")
    public void export(HttpServletResponse response) throws Exception{

    	ExcelData data = new ExcelData();
        data.setName("用户信息数据");
        //添加表头
        List<String> titles = new ArrayList();
        //for(String title: excelInfo.getNames())
        titles.add("用户名");
        titles.add("性别");
        titles.add("公司");
        titles.add("ce");
        titles.add("ssr");
        data.setTitles(titles);
        //添加列
        List<List<Object>> rows = new ArrayList();
        List<Object> row = null;
        List<PcUserInfoVo> s = new ArrayList<>();
        PcUserInfoVo ss = new PcUserInfoVo();
//        		ss.setCity("11");;
        		s.add(ss);
       for(int i=0; i<s.size();i++){
           row=new ArrayList();
           row.add("1");
//           row.add(s.get(i).getCity());
//           row.add(s.get(i).getCity());
//           row.add(s.get(i).getCity());
//           row.add(s.get(i).getCity());
           rows.add(row);

       }

        data.setRows(rows);
        SimpleDateFormat fdate=new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String fileName=fdate.format(new Date())+".xls";
        ExcelUtils.exportExcel(response, fileName, data);
    }


    //@RequestMapping(value = "projectExport", method = RequestMethod.GET)
    @GetMapping("/projectExport")
    public void projectExport(HttpServletRequest request, HttpServletResponse response) {
//        try {
//            
//            Map map=new HashMap<String,Object>();
//            map.put("test","测试");
//           
//            ByteArrayOutputStream baos = PDFUtil.createPDF("templates/project.html", map);
//            //设置response文件头
//          
//            PDFUtil.renderPdf(response, baos.toByteArray(), "pdf文件");
//            baos.close();
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//    	   contractService.createOrderContract("11233333", "111111", "1");
//    	   contractService.createOrderContract("11233333", "11111122", "1");
//    	 String url =  contractService.getPdfUrlByOrderNumber("111111");
        contractService.createContractDoc("HT2018080710405900001");
        
    }

    @GetMapping("/test")
    //@NoRepeatSubmit
    public void test(){
//
//////        eventService.publish(new CompanyJoin("BD2018080710405900001"));
//////        eventService.publish(new FinishContract("SJHT201811091623204760001"));
////        eventService.publish(new CreateOrder("SJHT201811091623204760001"));
//
//        List<SyncOrderVO> listvo = thirdPartDateService.getOrderContract("DO1811280009YS");
//        // for (int i = 0; i < listvo.size(); i++) {
//            CreateOrder order = new CreateOrder();
//        order.setData(listvo);
//            eventService.publish(order);
//        //   }
        agencyService.createPdf("131415");
    }
    @GetMapping("/send")
    public void send(){

//        eventService.publish(new CompanyJoin("BD2018080710405900001"));
//        eventService.publish(new FinishContract("SJHT201811091623204760001"));
//        eventService.publish(new CompanyJoin("SJ1812050027"));
        eventService.publish(new FinishContract("SJHT201812051302134730001"));
//        eventService.publish(new AccountCreate("${code}","530495062@qq.com","${pwd}","${name}"));
//        eventService.publish(new ResetPassWord( "530495062@qq.com","530495062@qq.com","${pwd}"));
//        eventService.publish(new SendValidateCode( "530495062@qq.com","530495062@qq.com","${code}"));
    }

}
