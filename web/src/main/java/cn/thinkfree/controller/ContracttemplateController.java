package cn.thinkfree.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
import cn.thinkfree.database.model.ContractTemplate;
import cn.thinkfree.service.contracttemplate.ContractTemplateService;
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
	


//	/**
//     * 合同类型
//     * @author lqd
//     * @return list
//     */
//	@ApiOperation(value = "合同类型", notes = "合同类型key和value对应的值")
//    @GetMapping("/ContracTypelistNum")
//    @MyRespBody
//    public MyRespBundle<Map<String,String>> ContracTypelistNum(){
//
//         List<Map<String,String>>  list = new ArrayList<>();  
//         Map<String,String> resMap = new HashMap<>();
//         for (CompanyType type : CompanyType.values()){
//        	 resMap.put(type.getCode()+"", type.getMes());
//		}
//         list.add(resMap);
//        return sendJsonData(ResultMessage.SUCCESS,list);
//    }
//	
	
	/**
     * 根据合同模板类型查询合同模板列表
     * @author lqd
     * @param String type
     * @return PcContractTemplate
     */
    @ApiOperation(value = "前端--运营后台--合同模板管理列表--吕启栋", notes = "根据合同类型查询合同模板数据(0设计公司合同 1装修公司合同 2设计业主合同 3装修业主合同)")
	@GetMapping(value = "/list")
    @MyRespBody
    public MyRespBundle<List<ContractTemplate>> list(@ApiParam("合同类型") @RequestParam(value ="type") String type){

		List<ContractTemplate> list = contractTemplateService.ContractTemplateList(type);
        
        return sendJsonData(ResultMessage.SUCCESS,list);
    }
    
    
//    @PostMapping(value = "/test")
//    @MyRespBody
//    public MyRespBundle<List<ContractTemplate>> test(
//    @NotNull(message = "辅导作文类型不能为空") @RequestParam(required = false) String type,BindingResult bindingResult){
//
//
//    	printInfoMes("has error ? {}", bindingResult.hasErrors());
//    	
//    	// 如果有捕获到参数不合法
//        if (bindingResult.hasErrors()) {
//            // 得到全部不合法的字段
//            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
//            // 遍历不合法字段
//            fieldErrors.forEach(
//                    fieldError -> {
//                        // 获取不合法的字段名和不合法原因
//                    	printInfoMes("error field is : {} ,message is : {}", fieldError.getField(), fieldError.getDefaultMessage());
//                    }
//            );
//
//        }
//		List<ContractTemplate> list = contractTemplateService.ContractTemplateList(type);
//        
//        return sendJsonData(ResultMessage.SUCCESS,list);
//    }
//    
    
    
    /**
     * 根据合同模板类型修改编辑合同模板是否可用
     * @author lqd
     * @param String type
     * @return PcContractTemplate
     */
    @ApiOperation(value = "前端--运营后台--根据合同模板类型修改编辑合同模板--吕启栋", notes = "根据合同模板类型修改编辑合同模板是否 可用/不可用(0可用 1不可用)")
    @PostMapping("/updateContractTemplateStatus")
    @MyRespBody
    public MyRespBundle<String> updateContractTemplateStatus( @ApiParam("合同类型") @RequestParam String type,
    		@ApiParam("合同编辑是否可用状态（0可用 1不可用）")@RequestParam String status){

    	boolean flag = contractTemplateService.updateContractTemplateStatus(type,status);
        
        return sendJsonData(ResultMessage.SUCCESS,flag);
    }
    
    
    /**
     * 添加合同（新增）
     * 
     *  
     */
    @ApiOperation(value = "前端--运营后台--新增/修改合同模板数据--吕启栋", notes = "根据合同类型添加合同模板数据(修改完合同模板返回合同模板pdf路径和上传时间)")
    @PostMapping("/insertOrModifyTemplate ")
    @MyRespBody
    
    public MyRespBundle<String> insertOrModifyTemplate(@ApiParam("合同id") @RequestParam(required = false) String id, 
    		@ApiParam("合同类型0设计公司合同 1装修公司合同 2设计业主合同 3装修业主合同") @RequestParam String type,
    		@ApiParam("合同名称")@RequestParam String contractTpName,
    		@ApiParam("合同备注") @RequestParam String contractTpRemark){
    	
    	boolean flag = contractTemplateService.updateContractTemplateInfo(id,type, contractTpName, contractTpRemark);
    	
        return sendJsonData(ResultMessage.SUCCESS,true);
    }
    

    /**
     * 上传合同
     */
    @ApiOperation(value = "前端--运营后台--上传合同模板--吕启栋", notes = "根据合同类型添加合同模板数据(修改完合同模板返回合同模板pdf路径和上传时间)")
    @PostMapping("/uploadTemplate")
    @MyRespBody
    
    public MyRespBundle<String>  uploadTemplate(
    		@ApiParam("合同类型0设计公司合同 1装修公司合同 2设计业主合同 3装修业主合同") @RequestParam String type,
    		@ApiParam("附件")@AppParameter @RequestParam("file") MultipartFile file){
    	
    	boolean flag = contractTemplateService.uploadFile(type,file);
    	
        return sendJsonData(ResultMessage.SUCCESS,true);
    }
    

	/**
	 *
	 * 合同模板预览
	 * @author lqd
	 * @param
	 * @return
	 */
	/**
	 * 预览pdf文件
	 * @param fileName
	 */
	@ApiOperation(value = "前端--运营后台--预览合同模板--吕启栋", notes = "根据合同类型预览合同模板（只返回存储路径  项目访问地址需拼接）")
	@GetMapping("/preview")
	public MyRespBundle<String> pdfStreamHandler( @ApiParam("合同类型") String type,
			HttpServletResponse response) {
		
		 String url =  contractTemplateService.getTemplatePdfUrl(type);
		 
		 return sendJsonData(ResultMessage.SUCCESS,url);
	}


	/**
     * 
     * 下载合同类型
     * @author lqd
     * @param 
     * @return 
     */
    @ApiOperation(value = "前端--运营后台--下载合同模板--吕启栋 ", notes = "根据合同类型查下载合同（下载必须走form表达提交啊）")
    @PostMapping("/download")
    @MyRespBody
    public String download(@ApiParam("合同类型") @RequestParam String type,HttpServletResponse response){

    	 return downloadFile(type, response);
    }


	private String downloadFile(String type, HttpServletResponse response) {
		//根据合同类型 查询合同url 然后下载
    	 List<ContractTemplate> list = contractTemplateService.ContractTemplateList(type);
    	 String contractTemplateUrl =  "";
    	 if(list != null && list.size() > 0){
    		 contractTemplateUrl = list.get(0).getUploadUrl();
    		 //下载文件
    	        String fileName = list.get(0).getContractTpName()+"_"+list.get(0).getPdfUrl();// 设置文件名，根据业务需要替换成要下载的文件名
    	        
    	        if (fileName != null) {
    	            //设置文件路径
    	            File file = null;
					try {
						file = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "static/contract/template/pdf/"+type+".pdf");
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
    	            if (file.exists()) {
    	                response.setContentType("application/force-download");// 设置强制下载不打开
    	              //  response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);// 设置文件名
    	                setFileDownloadHeader(response, fileName);
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
    
	
	
	 /**
     * 设置让浏览器弹出下载对话框的Header.
     * @param
     */
    public static void setFileDownloadHeader(HttpServletResponse response, String fileName) {
            // 中文文件名支持
            String encodedfileName;
			try {
				encodedfileName = new String(fileName.getBytes("GBK"), "ISO8859-1");
				 response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedfileName + "\"");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
           
        
    }
    
    
}
