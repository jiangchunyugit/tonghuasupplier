package cn.thinkfree.database.mapper;

import cn.thinkfree.database.model.ConstructionBaseDic;
import cn.thinkfree.database.model.ConstructionBaseDicExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ConstructionBaseDicMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_construction_base_dic
     *
     * @mbg.generated
     */
    long countByExample(ConstructionBaseDicExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_construction_base_dic
     *
     * @mbg.generated
     */
    int deleteByExample(ConstructionBaseDicExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_construction_base_dic
     *
     * @mbg.generated
     */
    int insert(ConstructionBaseDic record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_construction_base_dic
     *
     * @mbg.generated
     */
    int insertSelective(ConstructionBaseDic record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_construction_base_dic
     *
     * @mbg.generated
     */
    List<ConstructionBaseDic> selectByExample(ConstructionBaseDicExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_construction_base_dic
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") ConstructionBaseDic record, @Param("example") ConstructionBaseDicExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_construction_base_dic
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") ConstructionBaseDic record, @Param("example") ConstructionBaseDicExample example);
}