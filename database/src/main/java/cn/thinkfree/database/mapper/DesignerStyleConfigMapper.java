package cn.thinkfree.database.mapper;

import cn.thinkfree.database.model.DesignerStyleConfig;
import cn.thinkfree.database.model.DesignerStyleConfigExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DesignerStyleConfigMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table designer_style_config
     *
     * @mbg.generated
     */
    long countByExample(DesignerStyleConfigExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table designer_style_config
     *
     * @mbg.generated
     */
    int deleteByExample(DesignerStyleConfigExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table designer_style_config
     *
     * @mbg.generated
     */
    int insert(DesignerStyleConfig record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table designer_style_config
     *
     * @mbg.generated
     */
    int insertSelective(DesignerStyleConfig record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table designer_style_config
     *
     * @mbg.generated
     */
    List<DesignerStyleConfig> selectByExample(DesignerStyleConfigExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table designer_style_config
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") DesignerStyleConfig record, @Param("example") DesignerStyleConfigExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table designer_style_config
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") DesignerStyleConfig record, @Param("example") DesignerStyleConfigExample example);
}