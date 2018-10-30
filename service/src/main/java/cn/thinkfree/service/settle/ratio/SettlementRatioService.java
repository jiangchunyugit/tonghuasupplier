package cn.thinkfree.service.settle.ratio;

import java.util.Map;

import com.github.pagehelper.PageInfo;

import cn.thinkfree.database.model.SettlementRatioInfo;
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
	 * 
	 * 创建/修改 结算比例
	 * @param ContractSEO
	 * @return boolean
	 */
    boolean insertOrupdateSettlementRatio(SettlementRatioInfo settlementRatioSEO);
    
    
    /**
   	 * 
   	 * 拷贝结算比例
   	 * @param ContractSEO
   	 * @return boolean
   	 */
    boolean copySettlementRatio(String ratioNumber);
    
    
    
    /**
   	 * 
   	 * 拷贝结算比例
   	 * @param ContractSEO
   	 * @return SettlementRatioInfo
   	 */
    SettlementRatioInfo getSettlementRatio(String ratioNumber);
    
    
    /**
   	 * 
   	 * 作废结算比例
   	 * @param ContractSEO
   	 * @return SettlementRatioInfo
   	 */
    boolean cancellatSettlementRatio(String ratioNumber);
    
    
    /**
     * 获取费用名称从埃森哲获取
     * @param ratioNumber
     * @return
     */
    Map<String,String> getCostNames();
    
    
    
    
    
      
}
