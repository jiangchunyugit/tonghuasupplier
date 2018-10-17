package cn.thinkfree.database.mapper;

import cn.thinkfree.database.model.SystemRole;
import cn.thinkfree.database.model.SystemRoleExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SystemRoleMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_system_role
     *
     * @mbg.generated
     */
    long countByExample(SystemRoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_system_role
     *
     * @mbg.generated
     */
    int deleteByExample(SystemRoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_system_role
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_system_role
     *
     * @mbg.generated
     */
    int insert(SystemRole record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_system_role
     *
     * @mbg.generated
     */
    int insertSelective(SystemRole record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_system_role
     *
     * @mbg.generated
     */
    List<SystemRole> selectByExample(SystemRoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_system_role
     *
     * @mbg.generated
     */
    SystemRole selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_system_role
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") SystemRole record, @Param("example") SystemRoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_system_role
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") SystemRole record, @Param("example") SystemRoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_system_role
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(SystemRole record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_system_role
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(SystemRole record);

    /**
     * 查询角色
     * @param systemRoleExample
     * @return
     */
    List<SystemRole> selectSystemRoleVOByExample(SystemRoleExample systemRoleExample);

    /**
     * 角色详情
     * @param id
     * @return
     */
    SystemRole selectSystemRoleVOByID(Integer id);
}