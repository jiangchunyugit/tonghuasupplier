package cn.thinkfree.database.mapper;

import cn.thinkfree.database.model.PcContractTerms;
import cn.thinkfree.database.model.PcContractTermsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PcContractTermsMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_contract_terms
     *
     * @mbg.generated
     */
    long countByExample(PcContractTermsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_contract_terms
     *
     * @mbg.generated
     */
    int deleteByExample(PcContractTermsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_contract_terms
     *
     * @mbg.generated
     */
    int insert(PcContractTerms record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_contract_terms
     *
     * @mbg.generated
     */
    int insertSelective(PcContractTerms record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_contract_terms
     *
     * @mbg.generated
     */
    List<PcContractTerms> selectByExample(PcContractTermsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_contract_terms
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") PcContractTerms record, @Param("example") PcContractTermsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_contract_terms
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") PcContractTerms record, @Param("example") PcContractTermsExample example);
}