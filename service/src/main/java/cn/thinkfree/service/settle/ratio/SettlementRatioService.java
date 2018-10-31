package cn.thinkfree.service.settle.ratio;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.github.pagehelper.PageInfo;

import cn.thinkfree.database.model.SettlementRatioInfo;
import cn.thinkfree.database.vo.settle.SettlementRatioAudit;
import cn.thinkfree.database.vo.settle.SettlementRatioSEO;

public interface SettlementRatioService {

	/**
	 * 
	 * 根据条件分页查询 合同数据
	 * @param ContractSEO
	 * @return pageList
	 */
    PageInfo<SettlementRatioInfo> pageSettlementRatioBySEO(SettlementRatioSEO settlementRatioSEO);
    
    
    /**
     * 导出数据 （根据数据导出）
     * @param ContractSEO
     * @return null
     */
	
    void exportList(SettlementRatioSEO settlementRatioSEO,HttpServletResponse response);
    
    /**
	 * 
	 * 创建/修改 结算比例
	 * @param settlementRatioSEO
	 * @return boolean
	 */
    boolean insertOrupdateSettlementRatio(SettlementRatioInfo settlementRatioSEO);
    
    
    /**
   	 * 
   	 * 拷贝结算比例
   	 * @param ratioNumber
   	 * @return boolean
   	 */
    boolean copySettlementRatio(String ratioNumber);
    
    
    
    /**
   	 * 
   	 * 拷贝结算比例
   	 * @param ratioNumber
   	 * @return SettlementRatioInfo
   	 */
    SettlementRatioAudit getSettlementRatio(String ratioNumber);


    /**
   	 *
   	 * 作废结算比例
   	 * @param ratioNumber
   	 * @return SettlementRatioInfo
   	 */
    boolean cancelledSettlementRatio(String ratioNumber);


    /**
     * 获取费用名称从埃森哲获取
     * @param
     * @return
     */
    Map<String,String> getCostNames();



	/**
	 * 批量审核
	 * @param ratioNumbers
	 * @return
	 */
	boolean  batchcCheckSettlementRatio(List<String> ratioNumbers,String auditStatus,String auditCase);

      
}
