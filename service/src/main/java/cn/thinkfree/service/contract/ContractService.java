package cn.thinkfree.service.contract;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.github.pagehelper.PageInfo;

import cn.thinkfree.database.vo.ContractClauseVO;
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
     * 导出数据 （根据数据导出）
     * @param ContractSEO
     * @return null
     */
	
    void exportList(ContractSEO projectSEO,HttpServletResponse response);
    
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
      boolean  insertContractClause(String contractNumber,String companyId,ContractClauseVO contractClausevo);
      
      
      
      /**
       * 合同详情 （查询 运营设置完的合同）
       * @param contractNumber 
       * @return 
       */
      Map<String,String> getContractBycontractNumber(String contractNumber);
      
      
      /**
       * 查看合同详情 (公司信息和合同条款信息)
       * @param  contractNumber, companyId
       * @return list
       */
      Map<String,Object> getContractDetailInfo(String contractNumber,String companyId);
      
      
      

      /**
       * 根据订单号和订单类型生成合同和公司
       * @param  companyId , orderNumber, type (0设计合同 1装饰合同)
       * @return list
       */
      boolean createOrderContract(String companyId,String orderNumber,String type);
      
      
      
      /**
       * 根据订单号获取
       * @param  orderNumber 
       * @return String pdf_url 
       */
      String getPdfUrlByOrderNumber(String orderNumber);
      
      
      
}
