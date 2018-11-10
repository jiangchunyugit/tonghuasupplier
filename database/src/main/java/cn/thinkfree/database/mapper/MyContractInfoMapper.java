package cn.thinkfree.database.mapper;

import java.util.List;

import cn.thinkfree.database.annotation.AuthAnnotation;
import cn.thinkfree.database.model.ContractInfo;
import cn.thinkfree.database.model.ContractInfoExample;
import cn.thinkfree.database.vo.ContractSEO;
import cn.thinkfree.database.vo.ContractVo;

public interface MyContractInfoMapper {
   
	/**
     * {新增合同}
     *  @author lvqidong
     *  @date 2018-09-20 
     */
	public int insertSelective(ContractVo record);
	
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
	//@AuthAnnotation()
	public  List<ContractVo>  selectContractPage(ContractSEO secVo);
	
	
	
	/**
	 * 根据 合同编号查询合同信息
	 * @param contractVo
	 * @return
	 */
	public ContractVo selectContractBycontractNumber(ContractVo contractVo);

	List<ContractInfo> selectByExample(ContractInfoExample example);

	ContractInfo  selectOneByExample(String code);
}