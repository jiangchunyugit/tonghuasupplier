package cn.thinkfree.database.mapper;

import cn.thinkfree.database.model.BuildSchemeConfig;
import cn.thinkfree.database.model.BuildSchemeConfigExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BuildSchemeConfigMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table build_scheme_config
     *
     * @mbg.generated
     */
    long countByExample(BuildSchemeConfigExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table build_scheme_config
     *
     * @mbg.generated
     */
    int deleteByExample(BuildSchemeConfigExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table build_scheme_config
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(String schemeNo);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table build_scheme_config
     *
     * @mbg.generated
     */
    int insert(BuildSchemeConfig record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table build_scheme_config
     *
     * @mbg.generated
     */
    int insertSelective(BuildSchemeConfig record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table build_scheme_config
     *
     * @mbg.generated
     */
    List<BuildSchemeConfig> selectByExample(BuildSchemeConfigExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table build_scheme_config
     *
     * @mbg.generated
     */
    BuildSchemeConfig selectByPrimaryKey(String schemeNo);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table build_scheme_config
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") BuildSchemeConfig record, @Param("example") BuildSchemeConfigExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table build_scheme_config
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") BuildSchemeConfig record, @Param("example") BuildSchemeConfigExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table build_scheme_config
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(BuildSchemeConfig record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table build_scheme_config
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(BuildSchemeConfig record);
}