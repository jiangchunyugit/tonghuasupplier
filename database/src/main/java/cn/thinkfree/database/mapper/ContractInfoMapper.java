package cn.thinkfree.database.mapper;

import java.util.List;

import cn.thinkfree.database.model.UserRole;
import cn.thinkfree.database.vo.ContractSEO;
import cn.thinkfree.database.vo.ContractVo;

public interface ContractInfoMapper {
   
	/**
     * {新增合同}
     *  @author lvqidong
     *  @date 2018-09-20 
     */
	public int insertSelective(UserRole record);
	
	/**
	 * {修改合同状态}
	 * @author lvqidong
     *  @date 2018-09-20 
	 */
	public int updateContractStatus(ContractVo contractVo); 
	
	/**
	 * {分页查询合同数据}
	 * @author lvqidong
     *  @date 2018-09-20 
	 */
	public  List<ContractVo>  selectContractMap(ContractSEO secVo);
	
	
	
	/**
	 * 根据 合同编号查询合同信息
	 * @param contractVo
	 * @return
	 */
	public ContractVo selectContractBycontractNumber(ContractVo contractVo);
	
	
}