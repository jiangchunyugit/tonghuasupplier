package cn.thinkfree.service.settlerule.ratio;

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
    PageInfo<SettlementRatioInfo> pageContractBySEO(SettlementRatioSEO settlementRatioSEO);
    
    
    /**
	 * 
	 * 创建 结算比例
	 * @param ContractSEO
	 * @return pageList
	 */
    boolean insertSettlementRatio(SettlementRatioInfo settlementRatioSEO);
    
    
    
    
    
    
      
}
