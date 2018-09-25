package cn.thinkfree.service.contractTemplate;

import java.util.List;
import java.util.Map;

import cn.thinkfree.database.model.PcContractTemplate;
import cn.thinkfree.database.vo.MyContractTemplateDetails;

public interface ContractTemplateService {

	/**
	 * 
	 * 查询 合同模板list  
	 * @param ContractSEO
	 * @return pageList
	 */
    List<PcContractTemplate> PcContractTemplateList(String type);
    
    /**
	 * 
	 * 根据合同类型 查询 合同附加条款项目信息
	 * @param ContractSEO
	 * @return pageList
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
    Map<String,String>  insertInfoContractTemplate(PcContractTemplate pcContractTemplate);
    
    
  
}
