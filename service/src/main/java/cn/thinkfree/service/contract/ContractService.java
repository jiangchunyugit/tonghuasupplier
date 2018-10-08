package cn.thinkfree.service.contract;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;

import cn.thinkfree.database.vo.ContractDetails;
import cn.thinkfree.database.vo.ContractSEO;
import cn.thinkfree.database.vo.ContractVo;

public interface ContractService {

	/**
	 * 
	 * 根据条件分页查询 合同数据
	 * @param ContractSEO
	 * @return pageList
	 */
    PageInfo<ContractVo> pageContractBySEO(ContractSEO projectSEO);
	
    
    
    /**
	 * 
	 * 财务审核
	 * @param ContractSEO
	 * @return map  code mess
	 */
    Map<String,String>  auditContract(String contractNumber,String companyId,String auditStatus,String auditCase);
    
    
    
    /**
	 * 
	 * 确认保证金
	 * @param ContractSEO
	 * @return String mess
	 */
    Map<String,String>   ackEarnestMoney(String contractNumber,String companyId);
    
    
    
    /**
  	 * 
  	 * 合同详情 
  	 * @param ContractSEO
  	 * @return String mess
  	 */
    ContractDetails  contractDetails(String contractNumber,String companyId);
     
     
    
     /**
      * 根据合同编号查询合同地址
      * @param contractNumber
      * @return
      */
     String  selectContractBycontractNumber(String contractNumber);
     
     
     
     /**
      * 
      * 根据合同编号和公司编号 生成合同
      * @param contractNumber
      * @return map  code 0 成功  1 失败  mess 失败的原因
      */
      Map<String,String> createContractDoc(String contractNumber);
      
      
      /**
       * 新增合同 条款信息(如果信息存再删除添加 不存在新增 )
       *  @param contractNumber
       *  @param map 合同
       */
      Map<String,String>  insertContractClause(String contractNumber,String companyId,Map<String,String> map);
      
      
      
      /**
       * 合同详情 （查询 运营设置完的合同）
       * @param contractNumber 
       * @return 
       */
      Map<String,String> getContractBycontractNumber(String contractNumber);
      
      
      
}
