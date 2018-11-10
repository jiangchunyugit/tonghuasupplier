package cn.thinkfree.database.mapper;

import cn.thinkfree.database.model.ReserveProject;
import cn.thinkfree.database.model.ReserveProjectExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ReserveProjectMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table reserve_project
     *
     * @mbg.generated
     */
    long countByExample(ReserveProjectExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table reserve_project
     *
     * @mbg.generated
     */
    int deleteByExample(ReserveProjectExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table reserve_project
     *
     * @mbg.generated
     */
    int insert(ReserveProject record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table reserve_project
     *
     * @mbg.generated
     */
    int insertSelective(ReserveProject record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table reserve_project
     *
     * @mbg.generated
     */
    List<ReserveProject> selectByExample(ReserveProjectExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table reserve_project
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") ReserveProject record, @Param("example") ReserveProjectExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table reserve_project
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") ReserveProject record, @Param("example") ReserveProjectExample example);
}