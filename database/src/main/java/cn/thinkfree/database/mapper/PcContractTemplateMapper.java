package cn.thinkfree.database.mapper;

import cn.thinkfree.database.model.PcContractTemplate;
import cn.thinkfree.database.model.PcContractTemplateExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PcContractTemplateMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_contract_template
     *
     * @mbg.generated
     */
    long countByExample(PcContractTemplateExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_contract_template
     *
     * @mbg.generated
     */
    int deleteByExample(PcContractTemplateExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_contract_template
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_contract_template
     *
     * @mbg.generated
     */
    int insert(PcContractTemplate record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_contract_template
     *
     * @mbg.generated
     */
    int insertSelective(PcContractTemplate record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_contract_template
     *
     * @mbg.generated
     */
    List<PcContractTemplate> selectByExample(PcContractTemplateExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_contract_template
     *
     * @mbg.generated
     */
    PcContractTemplate selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_contract_template
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") PcContractTemplate record, @Param("example") PcContractTemplateExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_contract_template
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") PcContractTemplate record, @Param("example") PcContractTemplateExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_contract_template
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(PcContractTemplate record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_contract_template
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(PcContractTemplate record);
    
    
    
    /**
     * 根据公司类型 查询 合同模板记录
     * @author lvqidong
     * @returen list 
     */
    List<PcContractTemplate> queryListByType(String contractTpType);
    
    
    /**
     * 根据合同类型 修改 合同模板
     * 
     */
    int updatePcContractTemplateStatus(PcContractTemplate tlp);
    
    
    
}