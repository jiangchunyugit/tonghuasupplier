package cn.thinkfree.service.contract;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import com.github.pagehelper.PageInfo;

import cn.thinkfree.database.model.ContractInfo;
import cn.thinkfree.database.vo.ContractClauseVO;
import cn.thinkfree.database.vo.ContractSEO;
import cn.thinkfree.database.vo.ContractVo;
import cn.thinkfree.database.vo.contract.ContractCostVo;
import cn.thinkfree.database.vo.contract.ContractDetailsVo;
import cn.thinkfree.database.vo.remote.SyncContractVO;
import cn.thinkfree.database.vo.remote.SyncOrderVO;

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
    boolean auditContract(String contractNumber,String companyId,String auditStatus,String auditCase);
    
    
    
    /**
	 * 
	 * 确认保证金
	 * @param ContractSEO
	 * @return String mess
	 */
    boolean  ackEarnestMoney(String contractNumber,String companyId);
    
    
    
    /**
  	 * 
  	 * 合同详情 
  	 * @param ContractSEO
  	 * @return String mess
  	 */
    ContractDetailsVo  contractDetails(String contractNumber,String companyId);
     
     
    
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
      * @param
      * @return
      */
      String createContractDoc(String contractNumber);
      
      
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
       * @param  companyId , orderNumber, type (02设计合同 03装饰合同)
       * @return String 合同编号
       */
      String createOrderContract(String companyId,String orderNumber,String type);
      
      /**
       * 审核 业务合同 
       * @param orderNumber 
       * @auditStatus 0 不通过  1 通过
       */
      boolean auditStatusOrderContract(String orderNumber,String auditStatus);
      

      /**
       * 财务审核通过 生成合同pdf
       *
       */
      boolean createOrderContract(String orderNumber);

      /**
       * 根据订单号获取
       * @param  orderNumber 
       * @return String pdf_url 
       */
      String getPdfUrlByOrderNumber(String orderNumber);
      
      /**
       * 获取合同中的结算比例
       * @param contractNumber
       * @param CompanyId
       * @param roleType公司类型
       */
      Map<String, Object>  balanceInfo(String contractNumber, String CompanyId, String roleType);
      
      
      /**
       * 新增设计合同
       * @param  orderNumber 
       * @param  orderNumber 
       */
      boolean insertDesignOrderContract(String orderNumber,String companyId,Map<String,String> paramMap);
      
      /**
       * 新增施工合同
       * @param  orderNumber 
       */
      boolean insertRoadWorkOrderContract(String orderNumber,String companyId,Map<String,String> paramMap);
      
      /**
       * 根据公司编号查询公司合同
       * @param  orderNumber
       * @return list 
       */
      List<ContractInfo>  getEnterContractBycompanyId(String companyId);

    /**
     * 获取远程推送信息
     * @param contractNumber
     * @return
     */
    Optional<SyncContractVO> selectSyncDateByContractNumber(String contractNumber);


      /**
       * 根据 合同编写 提供合同设置比例值
       * @param  contractNumber
       * @retuen list
       * @author lvqidong
       */

      List<ContractCostVo> queryListContractCostVoBycontractNumber(String contractNumber, String roleId);


    /**
     * 根据公司类型 获取费用名称 和 code
     * @
     * type 0 设计 1装饰
     *
     */
      Map<String,String> getCostNames(String type);

    /**
     * 查询订单同步数据
     * @param contractID
     * @return
     */
    Optional<SyncOrderVO> selectSyncDateByOrder(String contractID);
}
