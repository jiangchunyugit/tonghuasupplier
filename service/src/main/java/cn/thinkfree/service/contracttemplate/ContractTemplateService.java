package cn.thinkfree.service.contracttemplate;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import cn.thinkfree.database.model.ContractTemplate;

public interface ContractTemplateService {

	/**
	 * 
	 * 查询 合同模板list  
	 * @param type
	 * @return PcContractTemplatelist
	 */
    List<ContractTemplate> ContractTemplateList(String type);
    
   
    /**
     * 根据合同模板类型编辑合同模板的可用/不可用
     * @param map
     * 
     */
    boolean  updateContractTemplateStatus(String type,String stauts);
    
    
    /**
     * 根据合同模板类型编辑合同模板的可用/不可用
     * @param map 返回pdf路径和上传时间
     * 生成pdf 
     * 
     */
    boolean updateContractTemplateInfo(String id,String type,String contractTpName, String contractTpRemark);
    
    
    
    /**
     * 
     * 根据合同类型 查询 合同字典code
     * @param contractNumber 
     * @retuen map 
     */
    Map<String,String> queryContractDic(String type);
    
    
    
    /**
     * 根据合同类型 查询合同类型模板
     * @author lvqidong
     * @param type
     * @return string
     */
    String getTemplatePdfUrl(String type);
    
    
    /**
     * 上传
     * 
     */
    boolean uploadFile(String type,MultipartFile  file);
    
    
    
}
