package cn.thinkfree.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.thinkfree.core.annotation.AppParameter;
import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRequBundle;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.model.PcContractTemplate;
import cn.thinkfree.service.constants.CompanyType;
import cn.thinkfree.service.contractTemplate.ContractTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 
 * 合同管理
 * @date 2018-09-20
 * @author lvqidong
 */
@Api(value = "合同模板管理",description = "合同模板管理信息描述")
@RestController
@RequestMapping(value = "/contracttemplate")
public class ContracttemplateController extends AbsBaseController{
	
	@Autowired
	ContractTemplateService contractTemplateService;
	
	
	/**
     * 合同类型
     * @author lqd
     * @return list
     */
	@ApiOperation(value = "合同类型", notes = "合同类型key和value对应的值")
    @GetMapping("/ContracTypelistNum")
    @MyRespBody
    public MyRespBundle<Map<String,String>> ContracTypelistNum(){

         List<Map<String,String>>  list = new ArrayList<>();  
         Map<String,String> resMap = new HashMap<>();
         for (CompanyType type : CompanyType.values()){
        	 resMap.put(type.getCode()+"", type.getMes());
		}
         list.add(resMap);
        return sendJsonData(ResultMessage.SUCCESS,list);
    }
	
	
	/**
     * 根据合同模板类型查询合同模板列表
     * @author lqd
     * @param String type
     * @return PcContractTemplate
     */
    @ApiOperation(value = "合同模板管理列表", notes = "根据合同类型查询合同模板数据")
    @PostMapping("/list")
    @MyRespBody
    public MyRespBundle<List<PcContractTemplate>> list(@ApiParam("合同类型") String type){

    	List<PcContractTemplate> list = contractTemplateService.PcContractTemplateList(type);
        
        return sendJsonData(ResultMessage.SUCCESS,list);
    }
    
    
    /**
     * 根据合同模板类型修改编辑合同模板是否可用
     * @author lqd
     * @param String type
     * @return PcContractTemplate
     */
    @ApiOperation(value = "根据合同模板类型修改编辑合同模板", notes = "根据合同模板类型修改编辑合同模板是否可用/不可用")
    @PostMapping("/updateContractTemplate")
    @MyRespBody
    public MyRespBundle<Map<String,String>> updateContractTemplate(@ApiParam("合同类型") String type,@ApiParam("合同编辑是否可用状态（0可用 1不可用）") String status){

    	Map<String,String> resMap = contractTemplateService.updateContractTemplateStatus(type,status);
        
        return sendJsonData(ResultMessage.SUCCESS,resMap);
    }
    
    
    /**
     * 添加合同（新增）
     * 
     */
    @ApiOperation(value = "添加合同模板数据", notes = "根据合同类型添加合同模板数据")
    @PostMapping("/insert")
    @MyRespBody
    public MyRespBundle<List<PcContractTemplate>> insert(@ApiParam("合同类型") String type){

        
        return sendJsonData(ResultMessage.SUCCESS,null);
    }
    
    
    /**
     * 根据合同类型 查询合同模板 设置分类列表
     * @author lqd
     * @param type
     * @return 
     */
    @ApiOperation(value = "查询合同模板下的所有分类", notes = "根据合同类型查询合同模板子分类的集合 key 为模板中保证金设置code value为值名称")
    @PostMapping("/categorylist")
    @MyRespBody
    public MyRespBundle< Map<String,String>> categorylist(@ApiParam("合同类型") String type){

    	 Map<String,String> map = contractTemplateService.getTemplateCategoryList(type);
        
        return sendJsonData(ResultMessage.SUCCESS,map);
    }
    
    /**
     * 根据合同分类设置动态的值
     * @author lqd
     * @param 
     * @return 
     */
    @ApiOperation(value = "添加动态模板值", notes = "根据合同类型查询合同模板子分类的集合 key 为模板中保证金设置code value为值名称")
    @PostMapping("/insertCategoryDitc")
    @MyRespBody
    public MyRespBundle< Map<String,String>> insertCategoryDitc(@ApiParam("添加项目分类id") String categoryId,
    		@ApiParam("自定义key和value的值") Map<String,String> paramMap){

    	 Map<String,String> resmap = contractTemplateService.insertdict(categoryId, paramMap);
        
        return sendJsonData(ResultMessage.SUCCESS,resmap);
    }
    
    
    
    /**
     * 
     * 下载合同类型
     * @author lqd
     * @param 
     * @return 
     */
    @ApiOperation(value = "下载合同", notes = "根据合同类型查下载合同")
    @PostMapping("/download")
    @MyRespBody
    public String download(@ApiParam("合同类型") String type,HttpServletResponse response){

    	 return downloadFile(type, response);
    }


	private String downloadFile(String type, HttpServletResponse response) {
		//根据合同类型 查询合同url 然后下载
    	 List<PcContractTemplate> list = contractTemplateService.PcContractTemplateList(type);
    	 String contractTemplateUrl =  "";
    	 if(list != null && list.size() > 0){
    		 contractTemplateUrl = list.get(0).getUploadUrl();
    		 //下载文件
    	        String fileName = "aim_test.txt";// 设置文件名，根据业务需要替换成要下载的文件名
    	        if (fileName != null) {
    	            //设置文件路径
    	            String realPath = "D://11//";
    	            File file = new File(realPath , fileName);
    	            if (file.exists()) {
    	                response.setContentType("application/force-download");// 设置强制下载不打开
    	                response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);// 设置文件名
    	                byte[] buffer = new byte[1024];
    	                FileInputStream fis = null;
    	                BufferedInputStream bis = null;
    	                try {
    	                    fis = new FileInputStream(file);
    	                    bis = new BufferedInputStream(fis);
    	                    OutputStream os = response.getOutputStream();
    	                    int i = bis.read(buffer);
    	                    while (i != -1) {
    	                        os.write(buffer, 0, i);
    	                        i = bis.read(buffer);
    	                    }
    	                    printInfoMes("下载成功");
    	                } catch (Exception e) {
    	                    e.printStackTrace();
    	                    printErrorMes("下载合同模板类型",e.getMessage());
    	                } finally {
    	                    if (bis != null) {
    	                        try {
    	                            bis.close();
    	                        } catch (IOException e) {
    	                            e.printStackTrace();
    	                        }
    	                    }
    	                    if (fis != null) {
    	                        try {
    	                            fis.close();
    	                        } catch (IOException e) {
    	                            e.printStackTrace();
    	                        }
    	                    }
    	                }
    	            }
    	        }
    	 }
    	    return null;
	}
    
    
    
    
    
}
