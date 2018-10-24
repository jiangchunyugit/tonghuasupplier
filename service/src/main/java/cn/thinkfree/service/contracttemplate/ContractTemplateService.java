package cn.thinkfree.service.contracttemplate;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import cn.thinkfree.database.model.ContractTemplate;
import cn.thinkfree.database.vo.MyContractTemplateDetails;

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
    Map<String,String> updateContractTemplateStatus(String type,String stauts);
    
    
    /**
     * 根据合同模板类型编辑合同模板的可用/不可用
     * @param map 返回pdf路径和上传时间
     * 生成pdf 
     * 
     */
    Map<String,String> updateContractTemplateInfo(String type,String contractTpName, String contractTpRemark,MultipartFile file);
    
    /**
	 * 
	 * 根据合同类型 查询 合同附加条款项目信息
	 * @param type
	 * @return 
	 */
    
    MyContractTemplateDetails getMyContractTemplateDetailsByType(String type);
    
    
    /**
     * 根据合同类型查询 动态设置的分类列表
     * 
     */
    Map<String,String> getTemplateCategoryList(String type);
	
    
    /**
     * 
     * 动态设置字典值
     * @param CategoryId 大类别id  对象的所有子字段key + value 
     * @retuen 成功失败 
     * 
     */
    Map<String,String> insertdict(String CategoryId,Map<String,String> map);
    
    
   /**
    * 添加 合同模板动态值 
    * code  和value 
    * @return true  false 
    */
    Map<String,String>  insertInfoContractTemplate(ContractTemplate pcContractTemplate);
    
    
    /**
     * 根据合同类型 查询合同类型模板
     * @author lvqidong
     * @param type
     * @return string
     */
    String getTemplatePdfUrl(String type);
    
    /**
     * 
     * 根据合同类型 查询 合同字典code
     * @param contractNumber 
     * @retuen map 
     */
    Map<String,String> queryContractDic(String type);
}
